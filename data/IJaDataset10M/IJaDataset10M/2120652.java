package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import edu.univalle.lingweb.persistence.EntityManagerHelper;
import edu.univalle.lingweb.persistence.MaUser;
import edu.univalle.lingweb.persistence.MaUserDAO;
import edu.univalle.lingweb.persistence.OpenResponse3;
import edu.univalle.lingweb.persistence.OpenResponse3DAO;
import edu.univalle.lingweb.persistence.StoredOpenResponse3;
import edu.univalle.lingweb.persistence.StoredOpenResponse3DAO;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'storedOpenResponse'( almacenamiento de la Respuesta abierta estudiante )
 * 
 * @author Diana Carolina Rivera Velasco
 */
public class DataManagerStoredOpenResponse3 extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerCourse.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerStoredOpenResponse3() {
        super();
        DOMConfigurator.configure(DataManagerCourse.class.getResource("/log4j.xml"));
    }

    /**
	 * Crea una respuesta abierta en la base de datos
	 * <p>
	 * En caso de error, se retorna {@link RestServiceResult} con el mensaje de
	 * error
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult create(RestServiceResult result, StoredOpenResponse3 storedOpenResponse) {
        StoredOpenResponse3DAO storedopenResponseDAO = new StoredOpenResponse3DAO();
        log.info("estoy en el create del data manager");
        try {
            storedOpenResponse.setStoredOpenResponseId(getSequence("sq_stored_open_response_3"));
            EntityManagerHelper.beginTransaction();
            storedopenResponseDAO.save(storedOpenResponse);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(storedOpenResponse);
            log.info("Pregunta de respuesta abierta con �xito: " + storedOpenResponse.getStoredOpenResponseId());
            Object[] args = { storedOpenResponse.getStoredOpenResponseId(), storedOpenResponse.getOpenResponse3() };
            result.setMessage(MessageFormat.format(bundle.getString("openResponse.create.success"), args));
        } catch (PersistenceException e) {
            e.printStackTrace();
            log.error("Error al guardar la respuesta abierta: " + e.getMessage());
            result.setError(true);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
	 * Realiza la busqueda de una respuesta abierta
	 * <p> * En caso de error, se retorna {@link RestServiceResult} con el
	 * mensaje de error
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @param nCourseId
	 *            c�digo del programa del curso
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult search(RestServiceResult serviceResult, Long nStoredOpenResponseId) {
        StoredOpenResponse3 storedOpenResponse = new StoredOpenResponse3DAO().findById(nStoredOpenResponseId);
        if (storedOpenResponse == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("storedOpenResponse.search.notFound"));
        } else {
            List<StoredOpenResponse3> list = new ArrayList<StoredOpenResponse3>();
            EntityManagerHelper.refresh(storedOpenResponse);
            list.add(storedOpenResponse);
            Object[] arrayParam = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("storedOpenResponse.search.success"), arrayParam));
            serviceResult.setObjResult(list);
            serviceResult.setNumResult(list.size());
        }
        return serviceResult;
    }

    /**
	 * Actualiza los datos de una respuesta Abierta
	 * <p>
	 * En caso de error, se retorna {@link RestServiceResult} con el mensaje de
	 * error
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @param coCourse
	 *            curso a actualizar
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult update(RestServiceResult serviceResult, StoredOpenResponse3 storedOpenResponse) {
        StoredOpenResponse3DAO storedOpenResponseDAO = new StoredOpenResponse3DAO();
        try {
            log.info("Actualizando la respuesta abierta: " + storedOpenResponse.getStoredOpenResponseId());
            EntityManagerHelper.beginTransaction();
            storedOpenResponseDAO.update(storedOpenResponse);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(storedOpenResponse);
            Object[] arrayParam = { storedOpenResponse.getStoredOpenResponseId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("storedOpenResponse.update.success"), arrayParam));
            log.info("Se actualizo la respuesta Abierta con �xito: " + storedOpenResponse.getStoredOpenResponseId());
        } catch (PersistenceException e) {
            log.error("Error al actualizar la respuesta abierta: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(e.getMessage());
        }
        return serviceResult;
    }

    /**
	 * Realiza la eliminaci�n de una respuesta abierta
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coCourse
	 *            Curso a eliminar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult delete(RestServiceResult serviceResult, StoredOpenResponse3 storedOpenResponse) {
        Number nStoredOpenResponseId = null;
        StoredOpenResponse3DAO storedOpenResponseDAO = new StoredOpenResponse3DAO();
        try {
            nStoredOpenResponseId = storedOpenResponse.getStoredOpenResponseId();
            log.error("Eliminando la respuesta abierta estudiante: " + storedOpenResponse.getStoredOpenResponseId());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_STORED_OPEN_RESPONSE3);
            query.setParameter(1, storedOpenResponse.getStoredOpenResponseId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(storedOpenResponse);
            Object[] arrayParam = { nStoredOpenResponseId };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("openResponse.delete.success"), arrayParam));
            log.info("Eliminando la respuesta abierta: " + storedOpenResponse.getStoredOpenResponseId());
        } catch (PersistenceException e) {
            log.error("Error al eliminar la respuesta abierta: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { storedOpenResponse.getStoredOpenResponseId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("openResponse.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }

    /**
	 * Obtiene la lista de las respuestas abiertas elaboradas por cada estudiante segun la version
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
	 * Obtiene la lista de tecnicas
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @param nRowStart
	 *            Especifica el �ndice de la fila en los resultados de la
	 *            consulta.
	 * @param nMaxResults
	 *            Especifica el m�ximo n�mero de resultados a retornar
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult result, int nRowStart, int nMaxResults) {
        StoredOpenResponse3DAO storedopenResponseDAO = new StoredOpenResponse3DAO();
        List<StoredOpenResponse3> list = storedopenResponseDAO.findAll(nRowStart, nMaxResults);
        if (list.size() == 0) {
            result.setError(true);
            result.setMessage(bundle.getString("openResponse.list.notFound"));
        } else {
            Object[] array = { list.size() };
            result.setMessage(MessageFormat.format(bundle.getString("openResponse.list.success"), array));
            result.setObjResult(list);
            if ((nRowStart > 0) || (nMaxResults > 0)) result.setNumResult(storedopenResponseDAO.findAll().size()); else result.setNumResult(list.size());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public RestServiceResult listbyUser(RestServiceResult serviceResult, long nOpenResponseId, long nUserId, int nRowStart, int nMaxResults) {
        OpenResponse3 openResponse = new OpenResponse3DAO().findById(nOpenResponseId);
        log.info("Entro en el datamanager y el id del openresponse es: " + nOpenResponseId);
        EntityManagerHelper.refresh(openResponse);
        MaUser maUser = new MaUserDAO().findById(nUserId);
        log.info("Entro en el datamanager y el id del usuario es: " + nUserId);
        EntityManagerHelper.refresh(maUser);
        if (openResponse == null && maUser == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("storedOpenResponse.search.notFound"));
        } else {
            Query query = EntityManagerHelper.createNativeQuery(Statements.SELECT_OPEN_RESPONSE_BY_USER3, StoredOpenResponse3.class);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            query.setParameter(1, nOpenResponseId);
            query.setParameter(2, nUserId);
            if (nRowStart > 0) query.setFirstResult(nRowStart);
            if (nMaxResults > 0) query.setMaxResults(nMaxResults);
            List<StoredOpenResponse3> list = query.getResultList();
            if (list.size() == 0) {
                Object[] arrayParam = { openResponse.getOpenResponseId() };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(bundle.getString("storedOpenResponse.listbyUser.notFound"), arrayParam));
            } else {
                Object[] arrayParam = { list.size(), openResponse.getOpenResponseId() };
                serviceResult.setMessage(MessageFormat.format(bundle.getString("storedOpenResponse.listbyUser.success"), arrayParam));
                serviceResult.setObjResult(list);
                if ((nRowStart > 0) || (nMaxResults > 0)) {
                    RestServiceResult serviceResult2 = listbyUser(new RestServiceResult(), nOpenResponseId, nUserId, 0, 0);
                    int nNumStoredOpenResponse = serviceResult2.getNumResult();
                    serviceResult.setNumResult(nNumStoredOpenResponse);
                } else serviceResult.setNumResult(list.size());
            }
        }
        return serviceResult;
    }
}
