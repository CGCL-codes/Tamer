package pl.edu.agh.ssm.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.edu.agh.ssm.beans.Notification;
import pl.edu.agh.ssm.beans.Permissions;
import pl.edu.agh.ssm.core.utils.PropertiesProvider;
import pl.edu.agh.ssm.core.utils.SettingsPropertiesName;
import pl.edu.agh.ssm.persistence.IGroup;
import pl.edu.agh.ssm.persistence.INotification;
import pl.edu.agh.ssm.persistence.IPermissions;
import pl.edu.agh.ssm.persistence.IProperty;
import pl.edu.agh.ssm.persistence.IService;
import pl.edu.agh.ssm.persistence.IUser;
import pl.edu.agh.ssm.persistence.IWindow;
import pl.edu.agh.ssm.persistence.dao.GroupAccessDAO;
import pl.edu.agh.ssm.persistence.dao.NotificationAccessDAO;
import pl.edu.agh.ssm.persistence.dao.PropertyAccessDAO;
import pl.edu.agh.ssm.persistence.dao.ServiceAccessDAO;
import pl.edu.agh.ssm.persistence.dao.UserAccessDAO;
import pl.edu.agh.ssm.persistence.dao.PermissionAccessDAO;
import pl.edu.agh.ssm.persistence.dao.WindowAccessDAO;
import pl.edu.agh.ssm.web.client.exceptions.UserInterfaceException;
import pl.edu.agh.ssm.web.client.rpc.SSMDatabaseUtils;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.ComponentDataModel;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.ComponentInfo;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.GroupDataModel;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.PermissionDataModel;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.PropertiesDataModel;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.UserDataModel;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.UserPermissionDataModel;
import pl.edu.agh.ssm.web.client.utils.AlarmProperties;
import pl.edu.agh.ssm.web.client.utils.PermissionDeniedException;
import pl.edu.agh.ssm.web.client.utils.WindowProperties;
import com.extjs.gxt.ui.client.data.BaseModelData;

public class SSMDatabaseUtilsImpl extends GwtController implements SSMDatabaseUtils {

    GroupAccessDAO groupAccessDAO;

    UserAccessDAO userAccessDAO;

    WindowAccessDAO windowAccessDAO;

    ServiceAccessDAO serviceAccessDAO;

    PropertyAccessDAO propertyAccessDAO;

    PermissionAccessDAO permissionAccessDAO;

    NotificationAccessDAO notificationAccessDAO;

    PropertiesProvider propertiesProvider;

    public SSMDatabaseUtilsImpl(GroupAccessDAO groupAccessDAO, UserAccessDAO userAccessDAO, ServiceAccessDAO serviceAccessDAO, PropertyAccessDAO propertyAccessDAO, PermissionAccessDAO permissionAccessDAO, WindowAccessDAO windowAccessDAO, NotificationAccessDAO notificationAccessDAO) {
        super();
        this.groupAccessDAO = groupAccessDAO;
        this.userAccessDAO = userAccessDAO;
        this.serviceAccessDAO = serviceAccessDAO;
        this.propertyAccessDAO = propertyAccessDAO;
        this.permissionAccessDAO = permissionAccessDAO;
        this.windowAccessDAO = windowAccessDAO;
        this.notificationAccessDAO = notificationAccessDAO;
        propertiesProvider = PropertiesProvider.getInstance(PropertiesProvider.SETTINGS);
    }

    private UserDataModel convertUser(IUser user) {
        UserDataModel udm = new UserDataModel();
        udm.setNick(user.getNick());
        udm.setLogin(user.getLogin());
        udm.setName(user.getName());
        udm.setSurname(user.getSurname());
        udm.setPhone(user.getPhone());
        udm.setEmail(user.getEmail());
        udm.setRole(user.getRole());
        udm.setGg(user.getGg());
        udm.setSkype(user.getSkype());
        udm.setID(user.getId());
        return udm;
    }

    public void createNewGroup(String groupName) {
        groupAccessDAO.createNewGroup(userAccessDAO.getUser("jasiu"), groupName);
    }

    public List<GroupDataModel> getGroupsList() {
        List<GroupDataModel> list = new ArrayList<GroupDataModel>();
        List<IGroup> temp = groupAccessDAO.getGroups(userAccessDAO.getUser("jasiu"));
        for (Iterator<IGroup> iterator = temp.iterator(); iterator.hasNext(); ) {
            String groupName = iterator.next().getGroupName();
            list.add(new GroupDataModel(groupName));
        }
        return list;
    }

    /**
	 * group arent delete just rename to default group name
	 */
    public void deleteGroup(String groupName) {
        IUser user = userAccessDAO.getUser("jasiu");
        groupAccessDAO.deleteEmptyGroup(user, groupName);
        if (!groupAccessDAO.ifGroupExist(user, SettingsPropertiesName.defaultGroupName)) createNewGroup(propertiesProvider.get(SettingsPropertiesName.defaultGroupName));
        groupAccessDAO.renameGroup(user, groupName, propertiesProvider.get(SettingsPropertiesName.defaultGroupName));
    }

    public String getGroup(String componentName) {
        return groupAccessDAO.getGroup(userAccessDAO.getUser("jasiu"), serviceAccessDAO.getService(componentName)).getGroupName();
    }

    public void changeComponentGroup(String componentName, String groupName) {
        IUser user = userAccessDAO.getUser("jasiu");
        if (!groupAccessDAO.ifGroupExist(user, groupName)) {
            createNewGroup(groupName);
        }
        groupAccessDAO.renameGroup(user, serviceAccessDAO.getService(componentName), groupName);
    }

    public List<BaseModelData> getComponentsList(String groupName) {
        List<String> temp = groupAccessDAO.getComponents(groupName, userAccessDAO.getUser("jasiu"));
        System.out.println("add temp " + temp.size());
        List<BaseModelData> ret = new ArrayList<BaseModelData>(temp.size());
        for (String componentName : temp) {
            ret.add(new ComponentDataModel(componentName));
        }
        return ret;
    }

    public List<BaseModelData> getPropertiesList(String serviceName) {
        IService service = serviceAccessDAO.getService(serviceName);
        List<IProperty> temp = propertyAccessDAO.findProperty(service);
        List<BaseModelData> ret = new ArrayList<BaseModelData>(temp.size());
        for (IProperty property : temp) {
            PropertiesDataModel pdm = new PropertiesDataModel();
            pdm.setMBeanName(property.getMbeanname());
            pdm.setID(property.getId());
            pdm.setAttributeName(property.getAttributename());
            pdm.setMBeanType(property.getMbeantype());
            pdm.setCronPattern(property.getCronPattern());
            pdm.setSpyActive(property.getActive());
            pdm.setAttributeType(property.getAttributetype());
            INotification notification = notificationAccessDAO.findNotification(getUserInSession(), property);
            pdm.setAlarmActive(notification == null ? false : notification.isActive());
            ret.add(pdm);
        }
        return ret;
    }

    public ComponentInfo getComponentInfo(String serviceName) {
        IService service = serviceAccessDAO.getService(serviceName);
        ComponentInfo ret = new ComponentInfo();
        ret.setBeanAddress(service.getLocation());
        ret.setBeanServerPort(service.getPort());
        ret.setServiceName(service.getName());
        List<IProperty> temp = propertyAccessDAO.findProperty(service);
        ret.setBeanscount(temp.size());
        return ret;
    }

    public BaseModelData getSpyAttributeProperties(int propertiesID) {
        IProperty property = propertyAccessDAO.getById(propertiesID);
        PropertiesDataModel pdm = new PropertiesDataModel();
        pdm.setMBeanName(property.getMbeanname());
        pdm.setID(property.getId());
        pdm.setAttributeName(property.getAttributename());
        pdm.setMBeanType(property.getMbeantype());
        pdm.setCronPattern(property.getCronPattern());
        pdm.setSpyActive(property.getActive());
        pdm.setAttributeType(property.getAttributetype());
        INotification notification = notificationAccessDAO.findNotification(getUserInSession(), property);
        pdm.setAlarmActive(notification == null ? false : notification.isActive());
        return pdm;
    }

    public void setPropertiesCron(int propertiesID, String cronPattern, boolean active) {
        IProperty property = propertyAccessDAO.getById(propertiesID);
        property.setCronPattern(cronPattern);
        property.setActive(active);
        propertyAccessDAO.update(property);
    }

    public List<UserPermissionDataModel> getPermissionUserList(String componentName) {
        List<UserPermissionDataModel> ret = new ArrayList<UserPermissionDataModel>();
        List<IPermissions> tempList = permissionAccessDAO.getPerrmisions(serviceAccessDAO.getService(componentName));
        for (IPermissions permissions : tempList) {
            UserPermissionDataModel temp = new UserPermissionDataModel();
            temp.setUserName(permissions.getUser().getLogin());
            temp.setPermission(PermissionDataModel.Static.getLabel(permissions.getPermissionType()));
            temp.setID(permissions.getId());
            ret.add(temp);
        }
        return ret;
    }

    public List<UserDataModel> getUserList() {
        List<UserDataModel> ret = new ArrayList<UserDataModel>();
        List<IUser> temp = userAccessDAO.getAll();
        for (IUser user : temp) {
            ret.add(convertUser(user));
        }
        System.out.println("zwracam " + ret.size());
        return ret;
    }

    public void addPermission(String componentName, int userID, int permissionType) throws UserInterfaceException {
        IUser user = userAccessDAO.getById(userID);
        IService service = serviceAccessDAO.getService(componentName);
        if (permissionAccessDAO.isPermissionExsist(user, service, permissionType)) {
            throw new UserInterfaceException("Permission already Exsist");
        }
        IPermissions p = new Permissions();
        p.setPermissionType(permissionType);
        p.setService(service);
        p.setUser(user);
        permissionAccessDAO.create(p);
    }

    public void deletePermissions(List<Integer> toDeleteID) {
        for (Integer id : toDeleteID) {
            permissionAccessDAO.delete(id);
        }
    }

    public UserDataModel login(String userLogin, String password) {
        IUser user = userAccessDAO.getUser(userLogin);
        if ((user == null) || (!user.getPassword().equals(password))) {
            return null;
        } else {
            setUserInSession(user);
            return convertUser(user);
        }
    }

    public String register(String nick, String login, String name, String password, String surname, String phone, String email, Integer gg, String skype) {
        IUser user = userAccessDAO.getUser(login);
        if (user != null) return "User already exsist"; else {
            user = userAccessDAO.create(nick, login, name, password, surname, phone, email, gg, skype);
            setUserInSession(user);
            return null;
        }
    }

    public void logout() {
        setUserInSession(null);
    }

    public WindowProperties getWindowProp(String id) {
        IWindow window = windowAccessDAO.getWindow(getUserInSession(), id);
        if (window == null) return null; else {
            WindowProperties ret;
            ret = new WindowProperties(window.getPosX(), window.getPosY(), window.getExpanded());
            return ret;
        }
    }

    public void updateOrCreateWindow(String id, int posx, int posy, boolean expanded) {
        IWindow window = windowAccessDAO.getWindow(getUserInSession(), id);
        if (window == null) windowAccessDAO.createNewWindow(getUserInSession(), id, posx, posy, expanded); else {
            window.setPosX(posx);
            window.setPosY(posy);
            window.setExpanded(expanded);
            windowAccessDAO.update(window);
        }
    }

    public AlarmProperties getAlarmProperty(String componentName, String beanName, String beanType, String attributeName) {
        return getAlarmProperty(findPropertiesID(componentName, beanName, beanType, attributeName));
    }

    public AlarmProperties getAlarmProperty(int propertiesID) {
        IProperty properties = propertyAccessDAO.getById(propertiesID);
        INotification notification = notificationAccessDAO.findNotification(getUserInSession(), properties);
        AlarmProperties ret = new AlarmProperties();
        if (notification != null) {
            ret.setActive(notification.isActive());
            ret.setBorderValue(notification.getBorderValue());
            ret.setCondition(notification.getNotificationCondition());
            ret.setNotificationType(notification.getNotificationType());
            ret.setNotificationMessage(notification.getNotificationMessage());
            ;
        }
        return ret;
    }

    public void setAlarmProperties(int propertiesID, AlarmProperties alarmProperties) {
        IProperty properties = propertyAccessDAO.getById(propertiesID);
        INotification notification = notificationAccessDAO.findNotification(getUserInSession(), properties);
        boolean save = false;
        if (notification == null) {
            notification = new Notification();
            notification.setProperty(properties);
            notification.setUser(getUserInSession());
            save = true;
        }
        notification.setActive(alarmProperties.isActive());
        notification.setBorderValue(alarmProperties.getBorderValue());
        notification.setNotificationCondition(alarmProperties.getCondition());
        notification.setNotificationType(alarmProperties.getNotificationType());
        notification.setNotificationMessage(alarmProperties.getNotificationMessage());
        ;
        notification.setLastReadedValue(null);
        if (save) notificationAccessDAO.create(notification); else notificationAccessDAO.update(notification);
    }

    public int findPropertiesID(String componentName, String mbeanName, String mbeanType, String attributeName) {
        return propertyAccessDAO.findProperty(serviceAccessDAO.getService(componentName), mbeanName, mbeanType, attributeName).getId();
    }

    public void updateUserData(String nick, String name, String surname, String phone, String email, Integer gg, String skype) {
        IUser user = getUserInSession();
        if (nick != null) user.setNick(nick);
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        if (phone != null) user.setPhone(phone);
        if (email != null) user.setEmail(email);
        if (gg != null) user.setGg(gg);
        if (skype != null) user.setSkype(skype);
        userAccessDAO.update(user);
    }

    public UserDataModel getUserData() {
        IUser user = getUserInSession();
        if (user == null) return null;
        return convertUser(user);
    }

    public void switchUser(int userID) throws PermissionDeniedException {
        if (!isLoggedUserAdmin()) throw new PermissionDeniedException();
        IUser user = userAccessDAO.getById(userID);
        setUserInSession(user);
    }
}
