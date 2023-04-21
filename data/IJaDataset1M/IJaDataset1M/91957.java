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

public class DependencyHandler {

    private ServiceDependency serviceDependency;

    private ServiceDependency modifyServiceDependency;

    private HostDependency hostDependency;

    private HostDependency modifyHostDependency;

    private BlueConfigXMLFileStore serviceDepFileStore;

    private BlueConfigXMLFileStore hostDepFileStore;

    private static int HOST_DEPENDENCY = 0;

    private static int SERVICE_DEPENDENCY = 1;

    private static final int SORT_BY_ID = 0;

    private static final int SORT_BY_NAME = 1;

    private static final int SORT_BY_DH = 2;

    private String addResult;

    private String delResult;

    private String modResult;

    private String selectResult;

    private int firstRowIndex;

    private DataModel hostDependencyModel;

    private DataModel serviceDependencyModel;

    private int sortByHost = 1;

    private int sortByService = 1;

    private boolean ascendingHost = true;

    private boolean ascendingService = true;

    private FacesContext context;

    private Paginator paginator;

    public DependencyHandler() {
        hostDependency = new HostDependency();
        serviceDependency = new ServiceDependency();
        serviceDepFileStore = new BlueConfigXMLFileStore(ObjectXMLBuilder.SERVICEDEPENDENCY);
        hostDepFileStore = new BlueConfigXMLFileStore(ObjectXMLBuilder.HOSTDEPENDENCY);
        paginator = new Paginator();
    }

    public synchronized String addResult() {
        return this.addResult;
    }

    public synchronized String modResult() {
        return this.modResult;
    }

    public synchronized String delResult() {
        return this.delResult;
    }

    public void setServiceDependency(ServiceDependency serviceDependency) {
        this.serviceDependency = serviceDependency;
    }

    public ServiceDependency getServiceDependency() {
        return this.serviceDependency;
    }

    public void setModifyServiceDependency(ServiceDependency modifyServiceDependency) {
        this.modifyServiceDependency = modifyServiceDependency;
    }

    public ServiceDependency getModifyServiceDependency() {
        return this.modifyServiceDependency;
    }

    public void setHostDependency(HostDependency hostDependency) {
        this.hostDependency = hostDependency;
    }

    public HostDependency getHostDependency() {
        return this.hostDependency;
    }

    public void setModifyHostDependency(HostDependency modifyHostDependency) {
        this.modifyHostDependency = modifyHostDependency;
    }

    public HostDependency getModifyHostDependency() {
        return this.modifyHostDependency;
    }

    public int getHostDependencyCount() {
        return hostDepFileStore.getObjectCount(ObjectXMLBuilder.HOSTDEPENDENCY);
    }

    public int getServiceDependencyCount() {
        return serviceDepFileStore.getObjectCount(ObjectXMLBuilder.SERVICEDEPENDENCY);
    }

    public int getFirstRowIndex() {
        return firstRowIndex;
    }

    public int getRowCount() {
        return paginator.getNumberOfRows();
    }

    public synchronized String scrollFirst(ActionEvent e) {
        if (isHostDepInfoCommand(e)) paginator.setObjectList(hostDepFileStore.getObjectHashMap()); else paginator.setObjectList(serviceDepFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollFirst();
        return "success";
    }

    public synchronized String scrollPrevious(ActionEvent e) {
        if (isHostDepInfoCommand(e)) paginator.setObjectList(hostDepFileStore.getObjectHashMap()); else paginator.setObjectList(serviceDepFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollPrevious();
        return "success";
    }

    public synchronized String scrollNext(ActionEvent e) {
        if (isHostDepInfoCommand(e)) paginator.setObjectList(hostDepFileStore.getObjectHashMap()); else paginator.setObjectList(serviceDepFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollNext();
        return "success";
    }

    public synchronized String scrollLast(ActionEvent e) {
        if (isHostDepInfoCommand(e)) paginator.setObjectList(hostDepFileStore.getObjectHashMap()); else paginator.setObjectList(serviceDepFileStore.getObjectHashMap());
        firstRowIndex = paginator.scrollLast();
        return "success";
    }

    public synchronized boolean isScrollFirstDisabled() {
        return firstRowIndex == 0;
    }

    public synchronized boolean isScrollLastHostDisabled() {
        return firstRowIndex >= hostDepFileStore.getObjectCount(ObjectXMLBuilder.HOSTDEPENDENCY) - paginator.getNumberOfRows();
    }

    public synchronized boolean isScrollLastServiceDisabled() {
        return firstRowIndex >= serviceDepFileStore.getObjectCount(ObjectXMLBuilder.SERVICEDEPENDENCY) - paginator.getNumberOfRows();
    }

    public synchronized void addDependency(ActionEvent e) {
        int dependencyType = 0;
        String result;
        try {
            dependencyType = Integer.valueOf((String) (e.getComponent().getAttributes().get("dependencyType")));
        } catch (NumberFormatException f) {
            return;
        }
        if (dependencyType == HOST_DEPENDENCY) {
            result = hostDepFileStore.addObject(hostDependency, ObjectXMLBuilder.HOSTDEPENDENCY);
            if (!result.equals("add-success")) {
                hostDependency.setId(-1);
                hostDependency.setIsModifiable(false);
                addResult = result;
                return;
            }
            hostDependency = new HostDependency();
            addResult = result;
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            result = serviceDepFileStore.addObject(serviceDependency, ObjectXMLBuilder.SERVICEDEPENDENCY);
            if (result.equals("add-success")) {
                serviceDependency = new ServiceDependency();
                if (Utils.inWizard()) {
                    addResult = "wizard-servicedependency-add";
                    return;
                }
                addResult = result;
                return;
            }
            serviceDependency.setId(-1);
            serviceDependency.setIsModifiable(false);
            addResult = result;
            return;
        }
    }

    public synchronized void modifyDependency(ActionEvent e) {
        int dependencyType;
        String result;
        try {
            dependencyType = Integer.valueOf((String) (e.getComponent().getAttributes().get("dependencyType")));
        } catch (NumberFormatException f) {
            modResult = "failure";
            return;
        }
        if (dependencyType == HOST_DEPENDENCY) {
            result = hostDepFileStore.modifyObject(modifyHostDependency, ObjectXMLBuilder.HOSTDEPENDENCY);
            if (result.equals("modify-success")) modifyHostDependency = new HostDependency();
            modResult = result;
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            result = serviceDepFileStore.modifyObject(modifyServiceDependency, ObjectXMLBuilder.SERVICEDEPENDENCY);
            if (result.equals("modify-success")) modifyServiceDependency = new ServiceDependency();
            modResult = result;
        }
    }

    public synchronized void deleteDependency(ActionEvent e) {
        int dependencyType;
        int dependencyId;
        String result;
        try {
            context = FacesContext.getCurrentInstance();
            Map props = context.getExternalContext().getRequestParameterMap();
            dependencyType = Integer.valueOf((String) props.get("dependencyType"));
            dependencyId = Integer.valueOf((String) props.get("objectId"));
            if (dependencyType == HOST_DEPENDENCY) {
                result = hostDepFileStore.deleteObject(dependencyId, ObjectXMLBuilder.HOSTDEPENDENCY);
                if (result.equals("delete-success")) hostDependency = new HostDependency();
                delResult = result;
            } else if (dependencyType == SERVICE_DEPENDENCY) {
                result = serviceDepFileStore.deleteObject(dependencyId, ObjectXMLBuilder.SERVICEDEPENDENCY);
                if (result.equals("delete-success")) serviceDependency = new ServiceDependency();
                delResult = result;
            }
        } catch (Exception ex) {
            delResult = "failure";
        }
    }

    public String sortByDependentHostname(ActionEvent e) {
        int dependencyType;
        try {
            dependencyType = Integer.valueOf((String) (e.getComponent().getAttributes().get("dependencyType")));
        } catch (NumberFormatException f) {
            return "failure";
        }
        if (dependencyType == HOST_DEPENDENCY) {
            if (sortByHost == SORT_BY_DH) ascendingHost = !ascendingHost; else {
                sortByHost = SORT_BY_DH;
                ascendingHost = true;
            }
            return "success";
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            if (sortByService == SORT_BY_ID) ascendingService = !ascendingService; else {
                sortByService = SORT_BY_ID;
                ascendingService = true;
            }
            return "success";
        }
        return "failure";
    }

    public synchronized void select(ActionEvent e) {
        int dependencyType;
        try {
            dependencyType = Integer.valueOf((String) e.getComponent().getAttributes().get("dependencyType"));
        } catch (NumberFormatException ef) {
            return;
        }
        if (dependencyType == HOST_DEPENDENCY) {
            try {
                modifyHostDependency = (HostDependency) hostDependencyModel.getRowData();
            } catch (Exception ef) {
                return;
            }
            selectResult = "select-hostdependency";
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            try {
                modifyServiceDependency = (ServiceDependency) serviceDependencyModel.getRowData();
            } catch (Exception ef) {
                return;
            }
            selectResult = "select-servicedependency";
        }
    }

    public String selectResult() {
        return this.selectResult;
    }

    public String sortByName(ActionEvent e) {
        int dependencyType;
        try {
            dependencyType = Integer.valueOf((String) (e.getComponent().getAttributes().get("dependencyType")));
        } catch (NumberFormatException f) {
            return "failure";
        }
        if (dependencyType == HOST_DEPENDENCY) {
            if (sortByHost == SORT_BY_NAME) ascendingHost = !ascendingHost; else {
                sortByHost = SORT_BY_NAME;
                ascendingHost = true;
            }
            return "success";
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            if (sortByService == SORT_BY_NAME) ascendingService = !ascendingService; else {
                sortByService = SORT_BY_NAME;
                ascendingService = true;
            }
            return "success";
        }
        return "failure";
    }

    public DataModel getSortedServiceDependencyData() {
        List<BlueObject> list = serviceDepFileStore.getStoredObjects(ObjectXMLBuilder.SERVICEDEPENDENCY);
        sortList(list, SERVICE_DEPENDENCY);
        serviceDependencyModel = new ListDataModel(list);
        return serviceDependencyModel;
    }

    public DataModel getSortedHostDependencyData() {
        List<BlueObject> list = hostDepFileStore.getStoredObjects(ObjectXMLBuilder.HOSTDEPENDENCY);
        sortList(list, HOST_DEPENDENCY);
        hostDependencyModel = new ListDataModel(list);
        return hostDependencyModel;
    }

    private static final Comparator ASC_DH_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostDependency) o1).getDependentHostname());
            String id2 = String.valueOf(((HostDependency) o2).getDependentHostname());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_DH_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostDependency) o1).getDependentHostname());
            String id2 = String.valueOf(((HostDependency) o2).getDependentHostname());
            return id2.compareTo(id1);
        }
    };

    private static final Comparator ASC_NAME_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostDependency) o1).getHostname());
            String id2 = String.valueOf(((HostDependency) o2).getHostname());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_NAME_HOST_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((HostDependency) o1).getHostname());
            String id2 = String.valueOf(((HostDependency) o2).getHostname());
            return id2.compareTo(id1);
        }
    };

    private static final Comparator ASC_ID_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceDependency) o1).getId());
            String id2 = String.valueOf(((ServiceDependency) o2).getId());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_ID_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceDependency) o1).getId());
            String id2 = String.valueOf(((ServiceDependency) o2).getId());
            return id2.compareTo(id1);
        }
    };

    private static final Comparator ASC_NAME_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceDependency) o1).getHostname());
            String id2 = String.valueOf(((ServiceDependency) o2).getHostname());
            return id1.compareTo(id2);
        }
    };

    private static final Comparator DESC_NAME_SERVICE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            String id1 = String.valueOf(((ServiceDependency) o1).getHostname());
            String id2 = String.valueOf(((ServiceDependency) o2).getHostname());
            return id2.compareTo(id1);
        }
    };

    private void sortList(List list, int dependencyType) {
        if (dependencyType == HOST_DEPENDENCY) {
            switch(sortByHost) {
                case SORT_BY_DH:
                    Collections.sort(list, ascendingHost ? ASC_DH_HOST_COMPARATOR : DESC_DH_HOST_COMPARATOR);
                    break;
                case SORT_BY_NAME:
                    Collections.sort(list, ascendingHost ? ASC_NAME_HOST_COMPARATOR : DESC_NAME_HOST_COMPARATOR);
                    break;
            }
        } else if (dependencyType == SERVICE_DEPENDENCY) {
            switch(sortByService) {
                case SORT_BY_ID:
                    Collections.sort(list, ascendingService ? ASC_ID_SERVICE_COMPARATOR : DESC_ID_SERVICE_COMPARATOR);
                    break;
                case SORT_BY_NAME:
                    Collections.sort(list, ascendingService ? ASC_NAME_SERVICE_COMPARATOR : DESC_NAME_SERVICE_COMPARATOR);
                    break;
            }
        }
    }

    private boolean isHostDepInfoCommand(ActionEvent e) {
        int depType;
        try {
            depType = Integer.valueOf((String) e.getComponent().getAttributes().get("depType"));
        } catch (Exception ex) {
            throw new IllegalStateException("Unknown Dependency Type");
        }
        if (depType == HOST_DEPENDENCY) return true;
        return false;
    }
}
