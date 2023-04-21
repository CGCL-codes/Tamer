package es.devel.opentrats.booking.service.dao;

import es.devel.opentrats.booking.beans.Appointment;
import es.devel.opentrats.booking.beans.Customer;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author Fran Serrano
 */
public interface ICustomerDao {

    List ListCustomerCities() throws HibernateException;

    List ListCustomers() throws HibernateException;

    List ListCustomerProvinces() throws HibernateException;

    Customer Load(int codcliente) throws HibernateException;

    void Save(Customer customerArg) throws HibernateException;

    void Delete(Customer customerArg) throws HibernateException;

    List findByApellidosONombre(String filtro, int maxResults) throws HibernateException;

    List findByTfnoOMovil(String filtro, int maxResults) throws HibernateException;

    long getLast() throws HibernateException;

    Appointment getNextAppointMent(int refcliente) throws HibernateException;
}
