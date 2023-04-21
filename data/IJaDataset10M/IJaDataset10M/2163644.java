package org.personalsmartspace.pss_2_ipojo_Gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.application.Action;
import org.osgi.framework.Constants;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.IExternalPSS2Ipojo;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.PSSIPojoConstants;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.PSS_2_IPojoException;
import org.personalsmartspace.pss_2_ipojo_Gui.adapter.ServiceAdapter;
import org.personalsmartspace.pss_2_ipojo_Gui.impl.EventInfo;
import org.personalsmartspace.pss_2_ipojo_Gui.impl.IServiceFinder;
import org.personalsmartspace.pss_2_ipojo_Gui.impl.OSGiServiceFinder;
import org.personalsmartspace.pss_2_ipojo_Gui.publisher.PSS2IPojoGuiPublisher;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtEventConstants;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.pss_sm_osgi.impl.OSGiProperties;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;

/**
 * The application's main frame.
 */
public class Util implements PropertyChangeListener, ListSelectionListener {

    private IServiceFinder serviceFinder;

    public IServiceIdentifier serviceId;

    Hashtable properties = new Hashtable(0);

    public ServiceAdapter serviceAdapter;

    private PSS2IPojoGuiPublisher m_PSS2IPojoGuiPublisher;

    public static String COMMAND_MODIFYSERVICE = PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENTYTPE;

    public static String COMMAND_REGISTERSERVICE = PSSIPojoConstants.PSS_TO_IPOJO_REGISTERED_SERVICE_EVENTYTPE;

    public static String COMMAND_UNREGISTERSERVICE = PSSIPojoConstants.PSS_TO_IPOJO_UNREGISTERING_SERVICE_EVENTYTPE;

    public static String COMMAND_STARTBUNDLE = PSSIPojoConstants.PSS_TO_IPOJO_ADDBUNDLE_EVENTTYPE;

    public static String COMMAND_STOPBUNDLE = PSSIPojoConstants.PSS_TO_IPOJO_REMOVEBUNDLE_EVENTTYPE;

    public static String MODE_MESSAGE = "Message";

    public static String MODE_API = "API";

    public String modeChosen = MODE_MESSAGE;

    public String commandChosen = COMMAND_MODIFYSERVICE;

    public CommandListener commandListener = new CommandListener();

    public ModeListener modeListener = new ModeListener();

    public DefaultListModel proplistModel = new DefaultListModel();

    public Util() {
        this.serviceId = null;
        this.serviceFinder = new OSGiServiceFinder(OSGiProperties.getInstance().getBundleContext());
        this.serviceFinder.openServiceFinders();
        serviceAdapter = new ServiceAdapter(this.serviceFinder);
        try {
            m_PSS2IPojoGuiPublisher = new PSS2IPojoGuiPublisher(this.serviceFinder.getEventMgr());
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        }
    }

    @Action
    public void initiateCommand() {
        if (COMMAND_MODIFYSERVICE.compareTo(this.commandChosen) == 0) {
            modifyService();
        }
        if (COMMAND_REGISTERSERVICE.compareTo(this.commandChosen) == 0) {
            registerService();
        }
        if (COMMAND_UNREGISTERSERVICE.compareTo(this.commandChosen) == 0) {
            unRegisterService();
        }
        if (COMMAND_STARTBUNDLE.compareTo(this.commandChosen) == 0) {
            startBundle();
        }
        if (COMMAND_STOPBUNDLE.compareTo(this.commandChosen) == 0) {
            stopBundle();
        }
    }

    private void stopBundle() {
        if (MODE_MESSAGE.compareTo(this.modeChosen) == 0) {
            this.stopBundlePeerMessage();
        } else {
            this.stopBundleAPI();
        }
    }

    private void startBundle() {
        if (MODE_MESSAGE.compareTo(this.modeChosen) == 0) {
            this.startBundlePeerMessage();
        } else {
            this.startBundleAPI();
        }
    }

    private void unRegisterService() {
        if (MODE_MESSAGE.compareTo(this.modeChosen) == 0) {
            this.unRegisterServiceMessage();
        } else {
            this.unregisterServiceApi();
        }
    }

    private void registerService() {
        if (MODE_MESSAGE.compareTo(this.modeChosen) == 0) {
            this.registerServiceMessage();
        } else {
            this.registerServiceApi();
        }
    }

    private void modifyService() {
        if (MODE_MESSAGE.compareTo(this.modeChosen) == 0) {
            this.modifyServiceMessage();
        } else {
            this.modifyServiceApi();
        }
    }

    public void startBundleAPI() {
        try {
            IExternalPSS2Ipojo external = this.serviceFinder.getExternalPSS2Ipojo();
            String eventType = PSSIPojoConstants.PSS_TO_IPOJO_ADDBUNDLE_EVENTTYPE;
            external.addBundle(eventType, this.serviceId);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void stopBundleAPI() {
        try {
            IExternalPSS2Ipojo external = this.serviceFinder.getExternalPSS2Ipojo();
            String eventType = PSSIPojoConstants.PSS_TO_IPOJO_REMOVEBUNDLE_EVENTTYPE;
            external.removeBundle(eventType, this.serviceId);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void startBundlePeerMessage() {
        EventInfo info = new EventInfo(this.serviceId, this.properties);
        PeerEvent peerModifyServiceEvent = new PeerEvent(PSSIPojoConstants.PSS_TO_IPOJO_ADDBUNDLE_EVENTTYPE, PSSIPojoConstants.PSS_TO_IPOJO_ADD_BUNDLE_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE, info);
        try {
            m_PSS2IPojoGuiPublisher.sendPeerEvent(peerModifyServiceEvent);
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void stopBundlePeerMessage() {
        EventInfo info = new EventInfo(this.serviceId, this.properties);
        PeerEvent peerModifyServiceEvent = new PeerEvent(PSSIPojoConstants.PSS_TO_IPOJO_REMOVEBUNDLE_EVENTTYPE, PSSIPojoConstants.PSS_TO_IPOJO_REMOVED_BUNDLE_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE, info);
        try {
            m_PSS2IPojoGuiPublisher.sendPeerEvent(peerModifyServiceEvent);
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void modifyServiceMessage() {
        this.properties = new Hashtable(0);
        int rank = 8;
        this.properties.put(Constants.SERVICE_RANKING, rank);
        properties = new Hashtable();
        Prop prop = null;
        for (int i = 0; i < proplistModel.size(); i++) {
            prop = (Prop) proplistModel.get(i);
            properties.put(prop.name, Integer.valueOf(prop.value));
        }
        EventInfo info = new EventInfo(this.serviceId, this.properties);
        PeerEvent peerModifyServiceEvent = new PeerEvent(PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENTYTPE, PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE, info);
        try {
            m_PSS2IPojoGuiPublisher.sendPeerEvent(peerModifyServiceEvent);
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void registerServiceMessage() {
        EventInfo info = new EventInfo(this.serviceId, this.properties);
        PeerEvent peerModifyServiceEvent = new PeerEvent(PSSIPojoConstants.PSS_TO_IPOJO_REGISTERED_SERVICE_EVENTYTPE, PSSIPojoConstants.PSS_TO_IPOJO_REGISTERED_SERVICE_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE, info);
        try {
            m_PSS2IPojoGuiPublisher.sendPeerEvent(peerModifyServiceEvent);
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void unRegisterServiceMessage() {
        EventInfo info = new EventInfo(this.serviceId, this.properties);
        PeerEvent peerModifyServiceEvent = new PeerEvent(PSSIPojoConstants.PSS_TO_IPOJO_UNREGISTERING_SERVICE_EVENTYTPE, PSSIPojoConstants.PSS_TO_IPOJO_UNREGISTERING_SERVICE_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE, info);
        try {
            m_PSS2IPojoGuiPublisher.sendPeerEvent(peerModifyServiceEvent);
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void modifyServiceApi() {
        try {
            IExternalPSS2Ipojo external = this.serviceFinder.getExternalPSS2Ipojo();
            EventInfo info = new EventInfo(this.serviceId, this.properties);
            String eventType = PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENTYTPE;
            String eventName = PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENT;
            external.modifyService(eventType, eventName, info);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void registerServiceApi() {
        try {
            IExternalPSS2Ipojo external = this.serviceFinder.getExternalPSS2Ipojo();
            EventInfo info = new EventInfo(this.serviceId, this.properties);
            String eventType = PSSIPojoConstants.PSS_TO_IPOJO_REGISTERED_SERVICE_EVENTYTPE;
            String eventName = PSSIPojoConstants.PSS_TO_IPOJO_REGISTERED_SERVICE_EVENT;
            external.modifyService(eventType, eventName, info);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    public void unregisterServiceApi() {
        try {
            IExternalPSS2Ipojo external = this.serviceFinder.getExternalPSS2Ipojo();
            EventInfo info = new EventInfo(this.serviceId, this.properties);
            String eventType = PSSIPojoConstants.PSS_TO_IPOJO_UNREGISTERING_SERVICE_EVENTYTPE;
            String eventName = PSSIPojoConstants.PSS_TO_IPOJO_UNREGISTERING_SERVICE_EVENT;
            external.modifyService(eventType, eventName, info);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
        } catch (PSS_2_IPojoException e) {
            e.printStackTrace();
        }
    }

    @Action
    public void refreshBrowser() {
        this.serviceAdapter.changeData();
    }

    class ModeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            modeChosen = e.getActionCommand();
        }
    }

    class CommandListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            commandChosen = e.getActionCommand();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
    }
}

class Prop {

    Prop(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name = null;

    public String value = null;
}
