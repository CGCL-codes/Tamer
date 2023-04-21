package org.opennms.netmgt.provision.service;

import static org.opennms.core.utils.LogUtils.infof;
import static org.opennms.core.utils.InetAddressUtils.str;
import java.net.InetAddress;
import java.util.Collection;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.tasks.Async;
import org.opennms.core.tasks.BatchTask;
import org.opennms.core.tasks.Callback;
import org.opennms.core.tasks.RunInBatch;
import org.opennms.core.tasks.Task;
import org.opennms.netmgt.provision.AsyncServiceDetector;
import org.opennms.netmgt.provision.ServiceDetector;
import org.opennms.netmgt.provision.SyncServiceDetector;
import org.opennms.netmgt.provision.support.NullDetectorMonitor;

/**
 * <p>IpInterfaceScan class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class IpInterfaceScan implements RunInBatch {

    private ProvisionService m_provisionService;

    private InetAddress m_address;

    private Integer m_nodeId;

    private String m_foreignSource;

    /**
     * <p>Constructor for IpInterfaceScan.</p>
     *
     * @param nodeId a {@link java.lang.Integer} object.
     * @param address a {@link java.net.InetAddress} object.
     * @param foreignSource a {@link java.lang.String} object.
     * @param provisionService a {@link org.opennms.netmgt.provision.service.ProvisionService} object.
     */
    public IpInterfaceScan(final Integer nodeId, final InetAddress address, final String foreignSource, final ProvisionService provisionService) {
        m_nodeId = nodeId;
        m_address = address;
        m_foreignSource = foreignSource;
        m_provisionService = provisionService;
    }

    /**
     * <p>getForeignSource</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getForeignSource() {
        return m_foreignSource;
    }

    /**
     * <p>getNodeId</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getNodeId() {
        return m_nodeId;
    }

    /**
     * <p>getAddress</p>
     *
     * @return a {@link java.net.InetAddress} object.
     */
    public InetAddress getAddress() {
        return m_address;
    }

    /**
     * <p>getProvisionService</p>
     *
     * @return a {@link org.opennms.netmgt.provision.service.ProvisionService} object.
     */
    public ProvisionService getProvisionService() {
        return m_provisionService;
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return new ToStringBuilder(this).append("address", m_address).append("foreign source", m_foreignSource).append("node ID", m_nodeId).toString();
    }

    /**
     * <p>servicePersister</p>
     *
     * @param currentPhase a {@link org.opennms.core.tasks.BatchTask} object.
     * @param serviceName a {@link java.lang.String} object.
     * @return a {@link org.opennms.core.tasks.Callback} object.
     */
    public Callback<Boolean> servicePersister(final BatchTask currentPhase, final String serviceName) {
        return new Callback<Boolean>() {

            public void complete(final Boolean serviceDetected) {
                final String hostAddress = str(getAddress());
                infof(this, "Attempted to detect service %s on address %s: %s", serviceName, hostAddress, serviceDetected);
                if (serviceDetected) {
                    currentPhase.getBuilder().addSequence(new RunInBatch() {

                        public void run(final BatchTask batch) {
                            if ("SNMP".equals(serviceName)) {
                                setupAgentInfo(currentPhase);
                            }
                        }
                    }, new RunInBatch() {

                        public void run(final BatchTask batch) {
                            getProvisionService().addMonitoredService(getNodeId(), hostAddress, serviceName);
                        }
                    });
                }
                getProvisionService().updateMonitoredServiceState(getNodeId(), hostAddress, serviceName);
            }

            public void handleException(final Throwable t) {
                infof(this, t, "Exception occurred while trying to detect service %s on address %s", serviceName, str(getAddress()));
            }
        };
    }

    Runnable runDetector(final SyncServiceDetector detector, final Callback<Boolean> cb) {
        return new Runnable() {

            public void run() {
                try {
                    infof(this, "Attemping to detect service %s on address %s", detector.getServiceName(), str(getAddress()));
                    cb.complete(detector.isServiceDetected(getAddress(), new NullDetectorMonitor()));
                } catch (final Throwable t) {
                    cb.handleException(t);
                } finally {
                    detector.dispose();
                }
            }

            @Override
            public String toString() {
                return String.format("Run detector %s on address %s", detector.getServiceName(), str(getAddress()));
            }
        };
    }

    Async<Boolean> runDetector(final AsyncServiceDetector detector) {
        return new AsyncDetectorRunner(this, detector);
    }

    Task createDetectorTask(final BatchTask currentPhase, final ServiceDetector detector) {
        if (detector instanceof SyncServiceDetector) {
            return createSyncDetectorTask(currentPhase, (SyncServiceDetector) detector);
        } else {
            return createAsyncDetectorTask(currentPhase, (AsyncServiceDetector) detector);
        }
    }

    private Task createAsyncDetectorTask(final BatchTask currentPhase, final AsyncServiceDetector asyncDetector) {
        return currentPhase.getCoordinator().createTask(currentPhase, runDetector(asyncDetector), servicePersister(currentPhase, asyncDetector.getServiceName()));
    }

    private Task createSyncDetectorTask(final BatchTask currentPhase, final SyncServiceDetector syncDetector) {
        return currentPhase.getCoordinator().createTask(currentPhase, runDetector(syncDetector, servicePersister(currentPhase, syncDetector.getServiceName())));
    }

    /** {@inheritDoc} */
    public void run(final BatchTask currentPhase) {
        final Collection<ServiceDetector> detectors = getProvisionService().getDetectorsForForeignSource(getForeignSource() == null ? "default" : getForeignSource());
        infof(this, "Detecting services for node %d/%s on address %s: found %d detectors", getNodeId(), getForeignSource(), str(getAddress()), detectors.size());
        for (final ServiceDetector detector : detectors) {
            currentPhase.add(createDetectorTask(currentPhase, detector));
        }
    }

    private void setupAgentInfo(final BatchTask currentphase) {
        getProvisionService().setIsPrimaryFlag(getNodeId(), str(getAddress()));
    }
}
