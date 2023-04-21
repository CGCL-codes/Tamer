package facade;

import exception.InvalidOSNumberException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import bean.person.client.ClientConector;
import bean.person.employee.EmployeeConector;
import bean.person.employee.EmployeeRestConector;
import bean.equipment.EquipmentConector;
import bean.person.employee.EmployeeLunchControlConector;
import bean.ossolicitation.OSSolicitationConector;
import bean.payment.PaymentConector;
import bean.person.PhoneConector;
import bean.ossolicitation.ServiceConector;
import bean.user.UserConector;
import bean.vehicle.VehicleConector;
import bean.ossolicitation.VisitConector;
import exception.MyHibernateException;
import exception.NoSuficientEquipmentAvailableException;
import exception.MyObjectNotFoundException;
import exception.OSActiveException;
import exception.OldVersionException;
import bean.person.employee.EmployeeLunchControl;
import bean.person.client.Client;
import bean.person.employee.Employee;
import bean.person.employee.EmployeeRest;
import bean.equipment.Equipment;
import bean.equipment.EquipmentRegister;
import bean.ossolicitation.OSSolicitation;
import bean.payment.Payment;
import bean.payment.PaymentPiece;
import bean.person.Phone;
import bean.ossolicitation.Service;
import bean.ossolicitation.ServiceOrderDebrisWater;
import bean.user.User;
import bean.vehicle.Vehicle;
import bean.ossolicitation.Visit;
import bean.equipment.EquipmentRegisterType;
import bean.equipment.EquipmentType;
import bean.ossolicitation.OrderStatus;
import bean.payment.PaymentMode;
import bean.payment.PaymentType;
import bean.ossolicitation.ServiceOrderType;
import bean.ossolicitation.ServiceType;
import hibernate.HibernateUtil;
import util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Facade {

    private static Facade instance = new Facade();

    private ClientConector clientConector;

    private EmployeeConector employeeConector;

    private UserConector userConector;

    private OSSolicitationConector osSolicitationConector;

    private VehicleConector vehicleConector;

    private ServiceConector serviceConector;

    private EmployeeLunchControlConector lunchControlConector;

    private PhoneConector phoneConector;

    private VisitConector visitConector;

    private EquipmentConector equipmentConector;

    private EmployeeRestConector employeeRestConector;

    private PaymentConector paymentConector;

    private Facade() {
        clientConector = new ClientConector();
        employeeConector = new EmployeeConector();
        userConector = new UserConector();
        osSolicitationConector = new OSSolicitationConector();
        vehicleConector = new VehicleConector();
        serviceConector = new ServiceConector();
        lunchControlConector = new EmployeeLunchControlConector();
        phoneConector = new PhoneConector();
        visitConector = new VisitConector();
        equipmentConector = new EquipmentConector();
        employeeRestConector = new EmployeeRestConector();
        paymentConector = new PaymentConector();
    }

    public static Facade getInstance() {
        return instance;
    }

    public Calendar getCurrentDate() {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        Timestamp t = null;
        try {
            transaction = session.beginTransaction();
            t = (Timestamp) session.createSQLQuery("select current_timestamp").uniqueResult();
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t.getTime());
        return calendar;
    }

    public List<? extends String> loadPaymentTypes() {
        return Util.loadPaymentTypes();
    }

    public void saveClient(Client client) throws MyHibernateException {
        clientConector.saveClient(client);
    }

    public void updateClient(Client client) throws OldVersionException, MyHibernateException {
        clientConector.updateClient(client);
    }

    public void removeClient(int id) throws OldVersionException, MyHibernateException, MyObjectNotFoundException, OSActiveException {
        clientConector.removeClient(id);
    }

    public List<String> loadClientsNames() throws MyHibernateException {
        return clientConector.loadClientsNames();
    }

    public Map<String, Client> loadClients() throws MyHibernateException {
        return clientConector.loadClients();
    }

    public Map<String, Client> loadClientsByName(String name) throws MyHibernateException {
        return clientConector.loadClientsByName(name);
    }

    public Map<String, Client> loadClientsByAddress(String address) throws MyHibernateException {
        return clientConector.loadClientsByAddress(address);
    }

    public Map<String, Client> loadClientsByPhone(String phone) throws MyHibernateException {
        return clientConector.loadClientsByPhone(phone);
    }

    public Client loadClient(int id) throws MyHibernateException {
        return clientConector.loadClient(id);
    }

    public Client loadClient(String name) throws MyHibernateException {
        return clientConector.loadClient(name);
    }

    public void updateEmployee(Employee employee) throws OldVersionException, MyHibernateException {
        employeeConector.updateEmployee(employee);
    }

    public void saveEmployee(Employee employee) throws MyHibernateException {
        employeeConector.saveEmployee(employee);
    }

    public void removeEmployee(int id) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        employeeConector.removeEmployee(id);
    }

    public List<String> loadEmployeesNames() throws MyHibernateException {
        return employeeConector.loadEmployeesNames();
    }

    public List<String> loadEmployeesNames(String role) throws MyHibernateException {
        return employeeConector.loadEmployeesNames(role);
    }

    public Map<String, Employee> loadEmployees() throws MyHibernateException {
        return employeeConector.loadEmployees();
    }

    public Employee loadEmployee(int id) throws MyHibernateException {
        return employeeConector.loadEmployee(id);
    }

    public Employee loadEmployee(String name) throws MyHibernateException {
        return employeeConector.loadEmployee(name);
    }

    public List<Employee> loadEmployees(String role) throws MyHibernateException {
        return employeeConector.loadEmployeesByRole(role);
    }

    public void updateUser(User user) throws OldVersionException, MyHibernateException {
        userConector.updateUser(user);
    }

    public void saveUser(User user) throws MyHibernateException {
        userConector.saveUser(user);
    }

    public void removeUser(User user) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        userConector.removeUser(user);
    }

    public List<User> loadUsers() throws MyHibernateException {
        return userConector.loadUsers();
    }

    public List<String> loadUsersLogin() throws MyHibernateException {
        return userConector.loadUsersLogin();
    }

    public User loadUser(int id) throws MyHibernateException {
        return userConector.loadUser(id);
    }

    public User loadUser(String login, String pass) throws MyHibernateException {
        return userConector.loadUser(login, pass);
    }

    public void updateOSSolicitationToTransferred() throws OldVersionException, MyHibernateException {
        osSolicitationConector.updateOSSolicitationToTransferred();
    }

    public void updateOSSolicitation(OSSolicitation osSolicitation) throws OldVersionException, MyHibernateException {
        osSolicitationConector.updateOSSolicitation(osSolicitation);
    }

    public void saveOSSolicitation(OSSolicitation osSolicitation) throws MyHibernateException, InvalidOSNumberException {
        osSolicitationConector.saveOSSolicitation(osSolicitation);
    }

    public void saveOSSolicitationToiletAwing(OSSolicitation osSolicitation) throws MyHibernateException, NoSuficientEquipmentAvailableException, InvalidOSNumberException {
        osSolicitationConector.saveOSSolicitationToiletAwing(osSolicitation);
    }

    public void removeOSSolicitation(OSSolicitation osSolicitation) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        osSolicitationConector.removeOSSolicitation(osSolicitation);
    }

    public List<OSSolicitation> loadOSSolicitationsByPerson(int id) throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitationsByPerson(id);
    }

    public List<OSSolicitation> loadOSSolicitations() throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitations();
    }

    public List<OSSolicitation> loadOSSolicitations(PaymentType paymentType, Calendar calendar) throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitations(paymentType, calendar);
    }

    public List<OSSolicitation> loadOSSolicitation(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitation(ordersStatus);
    }

    public OSSolicitation loadOSSolicitation(int id) throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitation(id);
    }

    public List<OSSolicitation> loadOSDebrisWaterForToday(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.loadOSForToday(ordersStatus);
    }

    public List<OSSolicitation> getTodayOSDebrisAndUnblock(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getTodayOSByType(ServiceOrderType.DEBRIS, ordersStatus);
    }

    public List<OSSolicitation> loadOSToiletForMonth(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getMonthOSToilet(ordersStatus);
    }

    public int loadOSSolicitationOpenedForToday() throws MyHibernateException {
        return osSolicitationConector.loadOSSolicitationOpenedForToday();
    }

    public List<OSSolicitation> getAllOSDebrisAndUnblock(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getAllOSByType(ServiceOrderType.DEBRIS, ordersStatus);
    }

    public int getMaxPageGappedOSToilet(Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        return osSolicitationConector.getMaxPageGappedOSToilet(inicialDate, finalDate, status);
    }

    public int getMaxPageGappedOSDebrisAndWater(Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        return osSolicitationConector.getMaxPageGappedOSDebrisAndWater(inicialDate, finalDate, status);
    }

    public List<OSSolicitation> getGappedOSToilet(int first, int last, Integer osNumber, Calendar inicialDate, Calendar finalDate, OrderStatus[] status) throws MyHibernateException {
        return osSolicitationConector.getGappedOSToilet(first, last, osNumber, inicialDate, finalDate, status);
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(int first, int last, Integer osNumber, Calendar inicialDate, Calendar finalDate, OrderStatus[] status) throws MyHibernateException {
        return osSolicitationConector.getGappedOSDebrisAndWater(first, last, osNumber, inicialDate, finalDate, status);
    }

    public List<OSSolicitation> getGappedOSToilet(int first, int last, Calendar inicialDate, Calendar finalDate, OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getGappedOSToilet(first, last, inicialDate, finalDate, ordersStatus);
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(Calendar date) throws MyHibernateException {
        return osSolicitationConector.getGappedOSDebrisAndWater(date);
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(Calendar date, OrderStatus... status) throws MyHibernateException {
        return osSolicitationConector.getGappedOSDebrisAndWater(date, status);
    }

    public List<OSSolicitation> getAllOSWater(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getAllOSByType(ServiceOrderType.WATER, ordersStatus);
    }

    public List<OSSolicitation> getAllOSToilet(OrderStatus... ordersStatus) throws MyHibernateException {
        return osSolicitationConector.getAllOSByType(ServiceOrderType.TOILET, ordersStatus);
    }

    public List<OSSolicitation> getAllOSDebrisWater(OrderStatus... ordersStatus) throws MyHibernateException {
        List<OSSolicitation> oss = new ArrayList<OSSolicitation>();
        List<OSSolicitation> debris = null;
        List<OSSolicitation> water = null;
        debris = osSolicitationConector.getAllOSByType(ServiceOrderType.DEBRIS, ordersStatus);
        water = osSolicitationConector.getAllOSByType(ServiceOrderType.WATER, ordersStatus);
        for (OSSolicitation o : debris) {
            oss.add(o);
        }
        for (OSSolicitation o : water) {
            oss.add(o);
        }
        return oss;
    }

    public void cancelOSSolicitation(OSSolicitation oss) throws OldVersionException, MyHibernateException {
        oss.getServiceOrder().setStatus(OrderStatus.CANCELLED);
        oss.getServiceOrder().getPayment().setPaymentType(PaymentType.CASH);
        oss.getServiceOrder().getPayment().setPaymentMode(PaymentMode.EAGER);
        oss.getServiceOrder().getPaymentPieces().get(0).setDone(PaymentPiece.DONE);
        oss.getServiceOrder().getPaymentPieces().get(0).setDate(this.getCurrentDate());
        if (oss.getServiceOrder() instanceof ServiceOrderDebrisWater) {
            oss.getServiceOrder().setFinishDate(Facade.getInstance().getCurrentDate());
        }
        this.updateOSSolicitation(oss);
    }

    public void closeOSSolicitation(OSSolicitation osSolicitation) throws OldVersionException, MyHibernateException {
        osSolicitation.getServiceOrder().setStatus(OrderStatus.CLOSED);
        osSolicitationConector.updateOSSolicitation(osSolicitation);
    }

    public int countOSSolicitationTransferredByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        return osSolicitationConector.countOSSolicitationTransferredByDate(date, type);
    }

    public int countOSSolicitationOpenedByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        return osSolicitationConector.countOSSolicitationOpenedByDate(date, type);
    }

    public int countOSSolicitationClosedByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        return osSolicitationConector.countOSSolicitationClosedByDate(date, type);
    }

    public int countOSSolicitationCanceledByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        return osSolicitationConector.countOSSolicitationCanceledByDate(date, type);
    }

    public int getNextOrderNumber(ServiceOrderType type) throws MyHibernateException {
        return osSolicitationConector.getNextOrderNumber(type);
    }

    public void setNextOrderNumber(ServiceOrderType type, int nextNumber) throws MyHibernateException, InvalidOSNumberException {
        osSolicitationConector.setNextOrderNumber(type, nextNumber);
    }

    public void resetOrderNumber(ServiceOrderType type) throws MyHibernateException {
        osSolicitationConector.resetOrderNumber(type);
    }

    public void updateVisit(Visit visit) throws OldVersionException, MyHibernateException {
        visitConector.update(visit);
    }

    public void saveVisit(Visit visit) throws MyHibernateException {
        visitConector.save(visit);
    }

    public Visit loadVisit(int id) throws MyHibernateException {
        return visitConector.load(id);
    }

    public void deleteVisit(Visit visit) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        visitConector.delete(visit);
    }

    public List<Visit> loadVisit() throws MyHibernateException {
        return visitConector.load();
    }

    public List<Visit> loadVisit(Calendar date) throws MyHibernateException {
        return visitConector.load(date);
    }

    public List<Visit> loadVisit(Calendar startDate, Calendar finalDate) throws MyHibernateException {
        return visitConector.load(startDate, finalDate);
    }

    public List<Visit> loadVisit(Vehicle vehicle, Calendar inicialDate, Calendar finalDate) throws MyHibernateException {
        return visitConector.load(vehicle, inicialDate, finalDate);
    }

    public List<Visit> loadVisit(Vehicle vehicle) throws MyHibernateException {
        return visitConector.load(vehicle);
    }

    public void saveVehicle(Vehicle vehicle) throws MyHibernateException {
        vehicleConector.save(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) throws OldVersionException, MyHibernateException {
        vehicleConector.delete(vehicle);
    }

    public void removeVehicle(String plate) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        vehicleConector.delete(plate);
    }

    public void updateVehicle(Vehicle vehicle) throws OldVersionException, MyHibernateException {
        vehicleConector.update(vehicle);
    }

    public List<Vehicle> loadVehicles() throws MyHibernateException {
        return vehicleConector.load();
    }

    public List<String> loadVehiclesPlates() throws MyHibernateException {
        return vehicleConector.loadPlates();
    }

    public Vehicle loadVehicle(String plate) throws MyHibernateException {
        return vehicleConector.load(plate);
    }

    public Vehicle loadVehicle(Integer id) throws MyHibernateException {
        return vehicleConector.load(id);
    }

    public void updateControlEmployeeLunch(EmployeeLunchControl type) throws MyHibernateException, OldVersionException {
        lunchControlConector.update(type);
    }

    public void saveControlEmployeeLunch(EmployeeLunchControl type) throws MyHibernateException {
        lunchControlConector.save(type);
    }

    public List<EmployeeLunchControl> loadControlEmployeeLunch(Calendar date) throws OldVersionException, MyHibernateException {
        return lunchControlConector.loadControlEmployeeLunch(date);
    }

    public List<EmployeeLunchControl> loadControlEmployeeLunch() throws MyHibernateException {
        return lunchControlConector.load();
    }

    public void removeControlEmployeeLunch(EmployeeLunchControl type) throws MyHibernateException, OldVersionException, MyObjectNotFoundException {
        lunchControlConector.delete(type);
    }

    public void updateEmployeeRest(EmployeeRest employeeRest) throws MyHibernateException, OldVersionException {
        employeeRestConector.update(employeeRest);
    }

    public void saveEmployeeRest(EmployeeRest employeeRest) throws MyHibernateException {
        employeeRestConector.save(employeeRest);
    }

    public List<EmployeeRest> loadEmployeeRest(Employee employee) throws MyHibernateException {
        return employeeRestConector.load(employee);
    }

    public List<EmployeeRest> loadEmployeeRest(Calendar start, Calendar end) throws MyHibernateException {
        return employeeRestConector.load(start, end);
    }

    public List<EmployeeRest> loadEmployeeRest(Employee employee, Calendar start, Calendar end) throws MyHibernateException {
        return employeeRestConector.load(employee, start, end);
    }

    public EmployeeRest loadEmployeeRest(int id) throws MyHibernateException {
        return employeeRestConector.load(id);
    }

    public List<EmployeeRest> loadEmployeeRest() throws MyHibernateException {
        return employeeRestConector.load();
    }

    public void deleteEmployeeRest(EmployeeRest employeeRest) throws MyHibernateException, OldVersionException, MyObjectNotFoundException {
        employeeRestConector.delete(employeeRest);
    }

    public void update(Service service) throws OldVersionException, MyHibernateException {
        serviceConector.update(service);
    }

    public void saveService(Service service) throws MyHibernateException {
        serviceConector.saveService(service);
    }

    public void removeService(Service service) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        serviceConector.removeService(service);
    }

    public Service loadService(ServiceType serviceType) throws MyHibernateException {
        return serviceConector.loadService(serviceType);
    }

    public Service loadService(int id) throws MyHibernateException {
        return serviceConector.loadService(id);
    }

    public List<Service> loadService() throws MyHibernateException {
        return serviceConector.loadService();
    }

    public void updatePhone(Phone type) throws OldVersionException, MyHibernateException {
        phoneConector.update(type);
    }

    public void savePhone(Phone type) throws MyHibernateException {
        phoneConector.save(type);
    }

    public List<Phone> loadPhoneByPersonId(int id) throws MyHibernateException {
        return phoneConector.loadByPersonId(id);
    }

    public List<Phone> loadPhone() throws MyHibernateException {
        return phoneConector.load();
    }

    public void deletePhone(Phone type) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        phoneConector.delete(type);
    }

    public void removeStorage(Equipment equipment, int quantity, Calendar calendar) throws MyObjectNotFoundException, NoSuficientEquipmentAvailableException, MyHibernateException, OldVersionException {
        equipmentConector.removeStorage(equipment, quantity, calendar);
    }

    public Equipment removeStorageByCode(String code, int quantity, Calendar calendar) throws MyObjectNotFoundException, NoSuficientEquipmentAvailableException, MyHibernateException, OldVersionException {
        return equipmentConector.removeStorageByCode(code, quantity, calendar);
    }

    public Equipment removeStorageByName(String name, int quantity, Calendar calendar) throws MyObjectNotFoundException, NoSuficientEquipmentAvailableException, MyHibernateException, OldVersionException {
        return equipmentConector.removeStorageByName(name, quantity, calendar);
    }

    public void updateEquipment(Equipment type) throws MyHibernateException, OldVersionException {
        equipmentConector.update(type);
    }

    public Equipment rentOutEquipmentByName(String name, int quantity, Calendar calendar) throws NoSuficientEquipmentAvailableException, MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.rentOutByName(name, quantity, calendar);
    }

    public Equipment rentOutEquipmentByCode(String code, int quantity, Calendar calendar) throws NoSuficientEquipmentAvailableException, MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.rentOutByCode(code, quantity, calendar);
    }

    public void rentOutEquipment(Equipment equipment, int quantity, Calendar calendar) throws NoSuficientEquipmentAvailableException, MyObjectNotFoundException, MyHibernateException, OldVersionException {
        equipmentConector.rentOut(equipment, quantity, calendar);
    }

    public Equipment rentInEquipmentByName(String name, int quantity, Calendar calendar) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.rentInByName(name, quantity, calendar);
    }

    public Equipment rentInEquipmentByCode(String code, int quantity, Calendar calendar) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.rentInByCode(code, quantity, calendar);
    }

    public void rentInEquipment(Equipment equipment, int quantity, Calendar calendar) throws MyObjectNotFoundException, MyHibernateException, MyHibernateException, OldVersionException {
        equipmentConector.rentIn(equipment, quantity, calendar);
    }

    public void registerEquipment(Equipment equipment) throws MyHibernateException {
        equipmentConector.registerEquipment(equipment);
    }

    public List<EquipmentRegister> loadEquipmentRegister(EquipmentRegisterType... types) throws MyHibernateException {
        return equipmentConector.loadEquipmentRegister(types);
    }

    public List<EquipmentRegister> loadEquipmentRegister(String cod, Calendar start, Calendar end, EquipmentRegisterType... types) throws MyHibernateException {
        return equipmentConector.loadEquipmentRegister(cod, start, end, types);
    }

    public Equipment loadEquipmentByName(String name) throws MyObjectNotFoundException, MyHibernateException {
        return equipmentConector.loadByName(name);
    }

    public Equipment loadEquipmentByCode(String code) throws MyObjectNotFoundException, MyHibernateException {
        return equipmentConector.loadByCode(code);
    }

    public Equipment loadEquipment(int id) throws MyHibernateException {
        return equipmentConector.load(id);
    }

    public List<Equipment> loadEquipments() throws MyHibernateException {
        return equipmentConector.load();
    }

    public List<Equipment> loadEquipments(EquipmentType type) throws MyHibernateException {
        return equipmentConector.load(type);
    }

    public void deleteEquipment(Equipment type) throws MyHibernateException, OldVersionException, MyObjectNotFoundException {
        equipmentConector.delete(type);
    }

    public void addStorageByName(String name, int quantity, double price, Calendar calendar) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        equipmentConector.addStorageByName(name, quantity, price, calendar);
    }

    public void addStorageByCode(String cod, int quantity, double price, Calendar calendar) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        equipmentConector.addStorageByCode(cod, quantity, price, calendar);
    }

    public void addStorage(Equipment equipment, int quantity, double price, Calendar calendar) throws MyHibernateException, OldVersionException {
        equipmentConector.addStock(equipment, quantity, price, calendar);
    }

    public int getRentedEquipmentsByName(String name) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.getRentedEquipmentsByName(name);
    }

    public int getRentedEquipmentsByCode(String cod) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        return equipmentConector.getRentedEquipmentsByCode(cod);
    }

    public int getRentedEquipments(Equipment equipment) throws MyHibernateException, OldVersionException {
        return equipmentConector.getRentedEquipments(equipment);
    }

    public int getQuantityAvailable(String orderType, String equipment, Calendar start, Calendar end) throws MyHibernateException {
        return equipmentConector.getQuantityAvailable(orderType, equipment, start, end);
    }

    public void removePayment(Payment payment) throws MyHibernateException, MyObjectNotFoundException, OldVersionException {
        paymentConector.delete(payment);
    }

    public boolean dateIsToday(Calendar date) {
        return date.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
