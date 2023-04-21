package com.arjuna.blue.bluefrontend.faces;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import com.arjuna.blue.bluefrontend.xml.ObjectXMLBuilder;

public class EscalationHandler {

    private Paginator paginator;

    private HostEscalation hostEscalation;

    private HostEscalation modifyHostEscalation;

    private ServiceEscalation serviceEscalation;

    private ServiceEscalation modifyServiceEscalation;

    private BlueConfigXMLFileStore hostEscFileStore;

    private BlueConfigXMLFileStore serviceEscFileStore;

    private DataModel hostEscalationModel;

    private DataModel serviceEscalationModel;

    private int firstRowIndex;

    private String addResult;

    private String delResult;

    private String modResult;

    private String selectResult;

    private static final int HOST_ESCALATION = 0;

    private static final int SERVICE_ESCALATION = 1;

    private static final int SORT_BY_NAME = 1;

    private int sortByHost = 1;

    private int sortByService = 1;

    private boolean ascendingHost = true;

    private boolean ascendingService = true;

    public EscalationHandler() {
        hostEscalation = new HostEscalation();
        serviceEscalation = new ServiceEscalation();
        hostEscFileStore = new BlueConfigXMLFileStore(ObjectXMLBuilder.HOSTESCALATION);
        serviceEscFileStore = new BlueConfigXMLFileStore(ObjectXMLBuilder.SERVICEESCALATION);
        paginator = new Paginator();
    }

    public void setHostEscalation(HostEscalation hostEscalation) {
        this.hostEscalation = hostEscalation;
    }

    public HostEscalation getHostEscalation() {
        return this.hostEscalation;
    }

    public void setModifyHostEscalation(HostEscalation modifyHostEscalation) {
        this.modifyHostEscalation = modifyHostEscalation;
    }

    public HostEscalation getModifyHostEscalation() {
        return this.modifyHostEscalation;
    }

    public void setModifyServiceEscalation(ServiceEscalation modifyServiceEscalation) {
        this.modifyServiceEscalation = modifyServiceEscalation;
    }

    public ServiceEscalation getModifyServiceEscalation() {
        return this.modifyServiceEscalation;
    }

    public void setServiceEscalation(ServiceEscalation serviceEscalation) {
        this.serviceEscalation = serviceEscalation;
    }

    public ServiceEscalation getServiceEscalation() {
        return this.serviceEscalation;
    }

    public int getServiceEscalationCount() {
        return serviceEscFileStore.getObjectCount(ObjectXMLBuilder.SERVICEESCALATION);
    }

    public int getHostEscalationCount() {
        return hostEscFileStore.getObjectCount(ObjectXMLBuilder.HOSTESCALATION);
    }

    public String modResult() {
        return this.modResult;
    }

    public String delResult() {
        return this.delResult;
    }

    public String addResult() {
        return this.addResult;
    }

    public String selectResult() {
        return this.selectResult;
    }

    public int getFirstRowIndex() {
        return firstRowIndex;
    }

    public int getRowCount() {
        return paginator.getNumberOfRows();
    }

    public synchronized String scrollFirst(ActionEvent e) {
        if (isHostEscCommand(e)) paginator.setObjectList(hostEscFileStore.getObjectHashMap()); else paginator.setObjectList(serviceEscFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollFirst();
        return "success";
    }

    public synchronized String scrollPrevious(ActionEvent e) {
        if (isHostEscCommand(e)) paginator.setObjectList(hostEscFileStore.getObjectHashMap()); else paginator.setObjectList(serviceEscFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollPrevious();
        return "success";
    }

    public synchronized String scrollNext(ActionEvent e) {
        if (isHostEscCommand(e)) paginator.setObjectList(hostEscFileStore.getObjectHashMap()); else paginator.setObjectList(serviceEscFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollNext();
        return "success";
    }

    public synchronized String scrollLast(ActionEvent e) {
        if (isHostEscCommand(e)) paginator.setObjectList(hostEscFileStore.getObjectHashMap()); else paginator.setObjectList(serviceEscFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollLast();
        return "success";
    }

    public synchronized boolean isScrollFirstDisabled() {
        return firstRowIndex == 0;
    }

    public synchronized boolean isScrollLastHostDisabled() {
        return firstRowIndex >= hostEscFileStore.getObjectCount(ObjectXMLBuilder.HOSTESCALATION) - paginator.getNumberOfRows();
    }

    public synchronized boolean isScrollLastServiceDisabled() {
        return firstRowIndex >= serviceEscFileStore.getObjectCount(ObjectXMLBuilder.SERVICEESCALATION) - paginator.getNumberOfRows();
    }

    public synchronized void addEscalation(ActionEvent e) {
        String result;
        try {
            if (isHostEscCommand(e)) {
                result = hostEscFileStore.addObject(hostEscalation, ObjectXMLBuilder.HOSTESCALATION);
                addResult = result;
                if (!result.equals("add-success")) {
                    hostEscalation.setId(-1);
                    hostEscalation.setIsModifiable(false);
                    return;
                }
                hostEscalation = new HostEscalation();
            } else {
                result = serviceEscFileStore.addObject(serviceEscalation, ObjectXMLBuilder.SERVICEESCALATION);
                addResult = result;
                if (result.equals("add-success")) {
                    serviceEscalation = new ServiceEscalation();
                    if (Utils.inWizard()) {
                        result = "wizard-serviceescalation-add";
                        return;
                    }
                    return;
                }
                serviceEscalation.setId(-1);
                serviceEscalation.setIsModifiable(false);
            }
        } catch (IllegalStateException is) {
            addResult = "failure";
        }
    }

    public void modifyEscalation(ActionEvent e) {
        String result;
        try {
            if (isHostEscCommand(e)) {
                result = hostEscFileStore.modifyObject(modifyHostEscalation, ObjectXMLBuilder.HOSTESCALATION);
                if (result.equals("modify-success")) modifyHostEscalation = new HostEscalation();
                modResult = result;
            } else {
                result = serviceEscFileStore.modifyObject(modifyServiceEscalation, ObjectXMLBuilder.SERVICEESCALATION);
                if (result.equals("modify-success")) modifyServiceEscalation = new ServiceEscalation();
                modResult = result;
            }
        } catch (IllegalStateException ex) {
            modResult = "failure";
        }
    }

    public synchronized void deleteEscalation(ActionEvent e) {
        int escalationId;
        int escalationType;
        String result;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map props = context.getExternalContext().getRequestParameterMap();
            escalationType = Integer.valueOf((String) props.get("escType"));
            escalationId = Integer.valueOf((String) props.get("objectId"));
            if (escalationType == HOST_ESCALATION) {
                result = hostEscFileStore.deleteObject(escalationId, ObjectXMLBuilder.HOSTESCALATION);
                if (result.equals("delete-success")) hostEscalation = new HostEscalation();
                delResult = result;
            } else if (escalationType == SERVICE_ESCALATION) {
                result = serviceEscFileStore.deleteObject(escalationId, ObjectXMLBuilder.SERVICEESCALATION);
                if (result.equals("delete-success")) serviceEscalation = new ServiceEscalation();
                delResult = result;
            }
        } catch (Exception ex) {
            delResult = "failure";
        }
    }

    public synchronized void select(ActionEvent e) {
        try {
            if (isHostEscCommand(e)) {
                modifyHostEscalation = (HostEscalation) hostEscalationModel.getRowData();
                selectResult = "select-hostescalation";
                return;
            } else {
                modifyServiceEscalation = (ServiceEscalation) serviceEscalationModel.getRowData();
                selectResult = "select-serviceescalation";
            }
        } catch (Exception ex) {
            selectResult = "failure";
        }
    }

    public DataModel getSortedServiceEscalationData() {
        List<BlueObject> list = serviceEscFileStore.getStoredObjects(ObjectXMLBuilder.SERVICEESCALATION);
        sortList(list, SERVICE_ESCALATION);
        serviceEscalationModel = new ListDataModel(list);
        return serviceEscalationModel;
    }

    public DataModel getSortedHostEscalationData() {
        List<BlueObject> list = hostEscFileStore.getStoredObjects(ObjectXMLBuilder.HOSTESCALATION);
        sortList(list, HOST_ESCALATION);
        hostEscalationModel = new ListDataModel(list);
        return hostEscalationModel;
    }

    public String sortByName(ActionEvent e) {
        try {
            if (isHostEscCommand(e)) {
                if (sortByHost == SORT_BY_NAME) ascendingHost = !ascendingHost; else {
                    sortByHost = SORT_BY_NAME;
                    ascendingHost = true;
                }
                return "success";
            } else {
                if (sortByService == SORT_BY_NAME) ascendingService = !ascendingService; else {
                    sortByService = SORT_BY_NAME;
                    ascendingService = true;
                }
                return "success";
            }
        } catch (IllegalStateException ex) {
            return "failure";
        }
    }

    private static final Comparator ASC_NAME_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostEscalation) o1).getHostname());
            String id2 = String.valueOf(((HostEscalation) o2).getHostname());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_NAME_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostEscalation) o1).getHostname());
            String id2 = String.valueOf(((HostEscalation) o2).getHostname());
            return id2.compareTo(id1);
        }
    };

    private static final Comparator ASC_NAME_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceEscalation) o1).getHostname());
            String id2 = String.valueOf(((ServiceEscalation) o2).getHostname());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_NAME_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceEscalation) o1).getHostname());
            String id2 = String.valueOf(((ServiceEscalation) o2).getHostname());
            return id2.compareTo(id1);
        }
    };

    private void sortList(List list, int escalationType) {
        if (escalationType == HOST_ESCALATION) {
            switch(sortByHost) {
                case SORT_BY_NAME:
                    Collections.sort(list, ascendingHost ? ASC_NAME_HOST_COMPARATOR : DESC_NAME_HOST_COMPARATOR);
                    break;
            }
        } else if (escalationType == SERVICE_ESCALATION) {
            switch(sortByService) {
                case SORT_BY_NAME:
                    Collections.sort(list, ascendingService ? ASC_NAME_SERVICE_COMPARATOR : DESC_NAME_SERVICE_COMPARATOR);
                    break;
            }
        }
    }

    private boolean isHostEscCommand(ActionEvent e) {
        int escType;
        try {
            escType = Integer.valueOf((String) e.getComponent().getAttributes().get("escType"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Unknown Escalation Type");
        }
        if (escType == HOST_ESCALATION) return true;
        return false;
    }
}
