package ca.eandb.jdcp.server;

import java.io.EOFException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.UnmarshalException;
import java.util.BitSet;
import java.util.Date;
import java.util.UUID;
import org.apache.log4j.Logger;
import ca.eandb.jdcp.job.TaskDescription;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jdcp.remote.DelegationException;
import ca.eandb.jdcp.remote.TaskService;
import ca.eandb.util.rmi.Serialized;

/**
 * @author Brad
 *
 */
final class ServiceWrapper implements TaskService {

    private static final Logger logger = Logger.getLogger(ServiceWrapper.class);

    private final TaskService service;

    private Date idleUntil = new Date(0);

    public ServiceWrapper(TaskService service) {
        this.service = service;
    }

    public void shutdown() {
    }

    private interface ServiceOperation<T> {

        T run(TaskService service) throws Exception;
    }

    ;

    private <T> T run(ServiceOperation<T> operation) throws DelegationException {
        TaskService service = this.service;
        try {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Running operation: %s", operation));
            }
            return operation.run(service);
        } catch (NoSuchObjectException e) {
            logger.error("Lost connection", e);
        } catch (ConnectException e) {
            logger.error("Lost connection", e);
        } catch (ConnectIOException e) {
            logger.error("Lost connection", e);
        } catch (UnknownHostException e) {
            logger.error("Lost connection", e);
        } catch (UnmarshalException e) {
            if (e.getCause() instanceof EOFException) {
                logger.error("Lost connection", e);
            } else {
                logger.error("Communication error", e);
                throw new DelegationException("Error occurred delegating to server", e);
            }
        } catch (Exception e) {
            logger.error("Communication error", e);
            throw new DelegationException("Error occurred delegating to server", e);
        }
        throw new DelegationException("No connection to server");
    }

    public byte[] getClassDefinition(final String name, final UUID jobId) throws DelegationException {
        return run(new ServiceOperation<byte[]>() {

            public byte[] run(TaskService service) throws RemoteException, SecurityException {
                return service.getClassDefinition(name, jobId);
            }
        });
    }

    public byte[] getClassDigest(final String name, final UUID jobId) throws DelegationException {
        return run(new ServiceOperation<byte[]>() {

            public byte[] run(TaskService service) throws RemoteException, SecurityException {
                return service.getClassDigest(name, jobId);
            }
        });
    }

    public BitSet getFinishedTasks(final UUID[] jobIds, final int[] taskIds) throws DelegationException {
        return run(new ServiceOperation<BitSet>() {

            public BitSet run(TaskService service) throws RemoteException, SecurityException {
                return service.getFinishedTasks(jobIds, taskIds);
            }
        });
    }

    public Serialized<TaskWorker> getTaskWorker(final UUID jobId) throws DelegationException {
        return run(new ServiceOperation<Serialized<TaskWorker>>() {

            public Serialized<TaskWorker> run(TaskService service) throws RemoteException, SecurityException {
                return service.getTaskWorker(jobId);
            }
        });
    }

    public void reportException(final UUID jobId, final int taskId, final Exception e) throws DelegationException {
        run(new ServiceOperation<Object>() {

            public Object run(TaskService service) throws RemoteException, SecurityException {
                service.reportException(jobId, taskId, e);
                return null;
            }
        });
    }

    public TaskDescription requestTask() throws DelegationException {
        return run(new ServiceOperation<TaskDescription>() {

            public TaskDescription run(TaskService service) throws RemoteException, SecurityException {
                return service.requestTask();
            }
        });
    }

    public void submitTaskResults(final UUID jobId, final int taskId, final Serialized<Object> results) throws DelegationException {
        run(new ServiceOperation<Object>() {

            public Object run(TaskService service) throws RemoteException, SecurityException {
                service.submitTaskResults(jobId, taskId, results);
                return null;
            }
        });
    }
}
