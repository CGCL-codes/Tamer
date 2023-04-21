package bean.ossolicitation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import exception.MyHibernateException;
import exception.MyObjectNotFoundException;
import exception.NoSuficientEquipmentAvailableException;
import exception.OldVersionException;
import facade.Facade;
import bean.person.employee.EmployeeRoleVisit;
import bean.payment.PaymentType;
import bean.person.employee.EmployeeRoleVisitDAO;
import exception.InvalidOSNumberException;
import gui.DebrisWaterVisitsDialog;
import hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;

public class OSSolicitationConector {

    private OSSolicitationDAO osSolicitationDAO;

    public OSSolicitationConector() {
        this.osSolicitationDAO = new OSSolicitationDAO();
    }

    public void saveOSSolicitation(OSSolicitation osSolicitation) throws MyHibernateException, InvalidOSNumberException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ServiceOrderType orderType = osSolicitation.getServiceOrder().getServiceOrderType();
            boolean OSNumberAvailable = false;
            if (orderType.equals(ServiceOrderType.DEBRIS) || orderType.equals(ServiceOrderType.WATER)) {
                OSSolicitation os = new OSSolicitation();
                ServiceOrder order = new ServiceOrderDebrisWater();
                os.setServiceOrder(order);
                order.setOrderNumber(osSolicitation.getOrderNumber());
                order.setServiceOrderType(ServiceOrderType.WATER);
                boolean availableWater = osSolicitationDAO.isOSNumberAvailable(session, os);
                order.setServiceOrderType(ServiceOrderType.DEBRIS);
                boolean availableDebris = osSolicitationDAO.isOSNumberAvailable(session, os);
                OSNumberAvailable = availableWater && availableDebris;
            } else {
                OSNumberAvailable = osSolicitationDAO.isOSNumberAvailable(session, osSolicitation);
            }
            if (!OSNumberAvailable) {
                throw new InvalidOSNumberException("Já existe uma OS cadastrada com esse número.");
            } else if (osSolicitation.getOrderNumber() >= osSolicitationDAO.getNextOrderNumber(session, orderType)) {
                throw new InvalidOSNumberException("Número inválido.");
            } else {
                osSolicitationDAO.save(session, osSolicitation);
                transaction.commit();
            }
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void saveOSSolicitationToiletAwing(OSSolicitation osSolicitation) throws MyHibernateException, NoSuficientEquipmentAvailableException, InvalidOSNumberException {
        int qnt;
        for (ServiceItem item : osSolicitation.getServiceItens()) {
            qnt = Facade.getInstance().getQuantityAvailable(item.getServiceType().name(), item.getServiceType().toString(), osSolicitation.getStartDate(), osSolicitation.getFinishDate());
            if (qnt < item.getQuantity()) {
                throw new NoSuficientEquipmentAvailableException(item.getServiceType().toString() + " insuficientes (" + qnt + " disponível(is)).");
            }
        }
        saveOSSolicitation(osSolicitation);
    }

    public void updateOSSolicitation(OSSolicitation osSolicitation) throws OldVersionException, MyHibernateException {
        Session sessionUpdate = HibernateUtil.getCurrentSession();
        Session sessionDelete = null;
        Transaction transaction = null;
        try {
            transaction = sessionUpdate.beginTransaction();
            osSolicitationDAO.update(sessionUpdate, osSolicitation);
            transaction.commit();
            sessionDelete = HibernateUtil.getCurrentSession();
            transaction = sessionDelete.beginTransaction();
            ServiceItemDAO itemDAO = new ServiceItemDAO();
            List<ServiceItem> items = itemDAO.loadByServiceOrderId(sessionDelete, osSolicitation.getServiceOrder().getId());
            for (ServiceItem item : items) {
                if (!osSolicitation.hasService(item.getServiceType())) {
                    itemDAO.delete(sessionDelete, item);
                }
            }
            EmployeeRoleVisitDAO employeeRoleVisitDAO = new EmployeeRoleVisitDAO();
            List<Visit> visits = osSolicitation.getVisits();
            for (Visit visit : visits) {
                List<EmployeeRoleVisit> ervs = employeeRoleVisitDAO.loadByVisitId(sessionDelete, visit.getId());
                for (EmployeeRoleVisit erv : ervs) {
                    EmployeeRoleVisit e = visit.getEmployeeRoleVisit(erv.getEmployeeRole());
                    if (e == null) {
                        employeeRoleVisitDAO.delete(sessionDelete, erv);
                    }
                }
            }
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof StaleObjectStateException) {
                throw new OldVersionException(ex);
            } else if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (sessionDelete != null && sessionDelete.isOpen()) {
                sessionDelete.close();
            }
            if (sessionUpdate != null && sessionUpdate.isOpen()) {
                sessionUpdate.close();
            }
        }
    }

    public void removeOSSolicitation(OSSolicitation osSolicitation) throws OldVersionException, MyHibernateException, MyObjectNotFoundException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitationDAO.delete(session, osSolicitation);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof StaleObjectStateException) {
                throw new OldVersionException(ex);
            } else if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            } else if (ex instanceof ObjectNotFoundException) {
                throw new MyObjectNotFoundException(ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public OSSolicitation loadOSSolicitation(int id) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        OSSolicitation osSolicitation = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitation = osSolicitationDAO.load(session, id);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitation;
    }

    public List<OSSolicitation> loadOSSolicitations() throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.load(session);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> loadOSSolicitations(PaymentType paymentType, Calendar calendar) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.load(session, paymentType, calendar);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public void cancelOSSolicitation(int id) throws OldVersionException, MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            OSSolicitation oss = osSolicitationDAO.load(session, id);
            oss.getServiceOrder().setStatus(OrderStatus.CANCELLED);
            if (oss.getServiceOrder() instanceof ServiceOrderDebrisWater) {
                oss.getServiceOrder().setFinishDate(Facade.getInstance().getCurrentDate());
            }
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof StaleObjectStateException) {
                throw new OldVersionException(ex);
            } else if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<OSSolicitation> loadOSSolicitation(OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.load(session, ordersStatus);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> loadOSForToday(OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadDebrisWaterOSSolicitationForToday(session, ordersStatus);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public void updateOSSolicitationToTransferred() throws OldVersionException, MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadOSSolicitationBeforeToday(session, OrderStatus.OPENED);
            for (OSSolicitation oss : osSolicitations) {
                oss.getServiceOrder().setStatus(OrderStatus.TRANSFERRED);
                osSolicitationDAO.update(session, oss);
            }
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof StaleObjectStateException) {
                throw new OldVersionException(ex);
            } else if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<OSSolicitation> loadOSSolicitationsByPerson(int id) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadOSSolicitationByPerson(session, id);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public int getMaxPageGappedOSDebrisAndWater(Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        Integer osNumber = null;
        try {
            transaction = session.beginTransaction();
            osNumber = osSolicitationDAO.loadCountOSDebrisAndWater(session, inicialDate, finalDate, status);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osNumber;
    }

    public int getMaxPageGappedOSToilet(Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        Integer osNumber = null;
        try {
            transaction = session.beginTransaction();
            osNumber = osSolicitationDAO.loadCountOSToilet(session, inicialDate, finalDate, status);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osNumber;
    }

    public List<OSSolicitation> getAllOSByType(ServiceOrderType type, OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.load(session, type, ordersStatus);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(Calendar date) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadGappedOSDebrisAndWater(session, date);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(Calendar date, OrderStatus... status) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadGappedOSDebrisAndWater(session, date, status);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getGappedOSToilet(int first, int last, Calendar inicialDate, Calendar finalDate, OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadGappedOSToilet(session, first, last, inicialDate, finalDate, ordersStatus);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getGappedOSDebrisAndWater(int first, int last, Integer osNumber, Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadGappedOSDebrisAndWater(session, first, last, osNumber, inicialDate, finalDate, status);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getGappedOSToilet(int first, int last, Integer osNumber, Calendar inicialDate, Calendar finalDate, OrderStatus... status) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitations = osSolicitationDAO.loadGappedOSToilet(session, first, last, osNumber, inicialDate, finalDate, status);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return osSolicitations;
    }

    public List<OSSolicitation> getMonthOSToilet(OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> ossSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ossSolicitations = osSolicitationDAO.loadOSToiletForMonth(session, ordersStatus);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return ossSolicitations;
    }

    public List<OSSolicitation> getTodayOSByType(ServiceOrderType type, OrderStatus... ordersStatus) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        List<OSSolicitation> typeSolicitations = new ArrayList<OSSolicitation>();
        List<OSSolicitation> osSolicitations = new ArrayList<OSSolicitation>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            typeSolicitations = osSolicitationDAO.loadOSSolicitationForToday(session, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        for (OSSolicitation oss : typeSolicitations) {
            for (OrderStatus status : ordersStatus) {
                if (oss.getStatus() == status) {
                    osSolicitations.add(oss);
                }
            }
        }
        return osSolicitations;
    }

    public int loadOSSolicitationOpenedForToday() throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.loadOSSolicitationOpenedForToday(session);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public int countOSSolicitationTransferredByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.countOSSolicitationTransferredByDate(session, date, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public int countOSSolicitationOpenedByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.countOSSolicitationOpenedByDate(session, date, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public int countOSSolicitationClosedByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.countOSSolicitationClosedByDate(session, date, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public int countOSSolicitationCanceledByDate(Calendar date, ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.countOSSolicitationCanceledByDate(session, date, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public int getNextOrderNumber(ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        int result = -1;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = osSolicitationDAO.getNextOrderNumber(session, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    public void setNextOrderNumber(ServiceOrderType type, int nextNumber) throws MyHibernateException, InvalidOSNumberException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int highestOrderNumber = 0;
            if (type.equals(ServiceOrderType.DEBRIS) || type.equals(ServiceOrderType.WATER)) {
                int highestOrderNumberDebris = osSolicitationDAO.getHighestOrderNumberFromCurrentCicle(session, ServiceOrderType.DEBRIS);
                int highestOrderNumberWater = osSolicitationDAO.getHighestOrderNumberFromCurrentCicle(session, ServiceOrderType.WATER);
                highestOrderNumber = Math.max(highestOrderNumberDebris, highestOrderNumberWater);
            } else {
                highestOrderNumber = osSolicitationDAO.getHighestOrderNumberFromCurrentCicle(session, type);
            }
            if (nextNumber <= highestOrderNumber) {
                throw new InvalidOSNumberException("Número de OS inválido. (Deve ser maior que o último número de OS válido: " + highestOrderNumber + ")");
            } else {
                osSolicitationDAO.setNextOrderNumber(session, type, nextNumber);
                transaction.commit();
            }
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void resetOrderNumber(ServiceOrderType type) throws MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            osSolicitationDAO.resetOrderNumber(session, type);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
