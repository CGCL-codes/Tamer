package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.persistence.CoDeliveryDate1;
import edu.univalle.lingweb.persistence.CoDeliveryDate1DAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'co_delivery_date1'( Fechas de Entrega ejercicio tipo 1)
 * 
 * @author Julio Cesar Puentes Delgado
 */
public class DataManagerDeliveryDate1 extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerDeliveryDate1.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerDeliveryDate1() {
        super();
        DOMConfigurator.configure(DataManagerDeliveryDate1.class.getResource("/log4j.xml"));
    }

    /**
	 * Crea una nueva fecha de entrega en la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coDeliveryDate1
	 *            Fecha de entrega a guardar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult create(RestServiceResult serviceResult, CoDeliveryDate1 coDeliveryDate1) {
        CoDeliveryDate1DAO coDeliveryDate1DAO = new CoDeliveryDate1DAO();
        try {
            coDeliveryDate1.setDeliveryDateId(getSequence("sq_co_delivery_date1"));
            EntityManagerHelper.beginTransaction();
            coDeliveryDate1DAO.save(coDeliveryDate1);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coDeliveryDate1);
            log.info("Fecha Entrega " + coDeliveryDate1.getDeliveryDate() + " creada con �xito...");
            Object[] arrayParam = { coDeliveryDate1.getDeliveryDate() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.create.success"), arrayParam));
        } catch (PersistenceException e) {
            log.error("Error al guardar la fecha de entrega: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.create.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Actualiza una una fecha de entrega en la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coDeliveryDate1
	 *            Fecha de entrega a actualizar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult update(RestServiceResult serviceResult, CoDeliveryDate1 coDeliveryDate1) {
        CoDeliveryDate1DAO coDeliveryDate1DAO = new CoDeliveryDate1DAO();
        try {
            log.info("Actualizando la fecha de entrega: " + coDeliveryDate1.getDeliveryDate());
            EntityManagerHelper.beginTransaction();
            coDeliveryDate1DAO.update(coDeliveryDate1);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coDeliveryDate1);
            Object[] args = { coDeliveryDate1.getDeliveryDate() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.update.success"), args));
            log.info("Se actualizo la fecha de entrega con �xito: " + coDeliveryDate1.getDeliveryDate());
        } catch (PersistenceException e) {
            log.error("Error al actualizar la fecha de entrega: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.update.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Elimina una fecha de entrega de la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coDeliveryDate1
	 *            Fecha de entrega a eliminar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult delete(RestServiceResult serviceResult, CoDeliveryDate1 coDeliveryDate1) {
        try {
            log.info("Eliminando la entrega: " + coDeliveryDate1.getDeliveryDate());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_DELIVERY_DATE1);
            query.setParameter(1, coDeliveryDate1.getDeliveryDateId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coDeliveryDate1);
            Object[] arrayParam = { coDeliveryDate1.getDeliveryDate() };
            log.info("entrega eliminada con �xito: " + coDeliveryDate1.getDeliveryDate());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            log.error("Error al eliminar la entrega: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coDeliveryDate1.getDeliveryDate() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }

    /**
	 * Realiza la busqueda de una fecha de entrega
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param nDeliveryDateId
	 *            C�digo de la prueba
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult search(RestServiceResult serviceResult, Long nDeliveryDateId) {
        CoDeliveryDate1 coDeliveryDate1 = new CoDeliveryDate1DAO().findById(nDeliveryDateId);
        if (coDeliveryDate1 == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("deliveryDate1.search.notFound"));
        } else {
            List<CoDeliveryDate1> list = new ArrayList<CoDeliveryDate1>();
            EntityManagerHelper.refresh(coDeliveryDate1);
            list.add(coDeliveryDate1);
            Object[] arrayParam = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.search.success"), arrayParam));
            serviceResult.setObjResult(list);
        }
        return serviceResult;
    }

    /**
	 * Obtiene la lista de fechas de entrega
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult result) {
        return list(result, 0, 0);
    }

    /**
	 * Obtiene la lista de fechas de entrega
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {
        CoDeliveryDate1DAO coDeliveryDate1DAO = new CoDeliveryDate1DAO();
        List<CoDeliveryDate1> list = coDeliveryDate1DAO.findAll(nRowStart, nMaxResults);
        if (list.size() == 0) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("deliveryDate1.list.notFound"));
        } else {
            Object[] array = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("deliveryDate1.list.success"), array));
            serviceResult.setObjResult(list);
            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(coDeliveryDate1DAO.findAll().size()); else serviceResult.setNumResult(list.size());
        }
        return serviceResult;
    }
}
