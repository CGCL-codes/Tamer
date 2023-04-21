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
import edu.univalle.lingweb.persistence.CoParagraphTeacher;
import edu.univalle.lingweb.persistence.CoParagraphTeacherDAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'singleParagraphTeacher'
 * 
 * @author Juan Pablo Rivera Velasco
 */
public class DataManagerSingleParagraphTeacher extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerCourse.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerSingleParagraphTeacher() {
        super();
        DOMConfigurator.configure(DataManagerCourse.class.getResource("/log4j.xml"));
    }

    /**
	 * Crea un nueva t�cnica en la base de datos
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
    public RestServiceResult create(RestServiceResult result, CoParagraphTeacher singleParagraphTeacher) {
        CoParagraphTeacherDAO coSingleParagraphTeacherDAO = new CoParagraphTeacherDAO();
        log.info("estoy en el create del paragraph teacher del data manager");
        try {
            singleParagraphTeacher.setParagraphTeacherId(getSequence("sq_co_paragraph_teacher_1"));
            EntityManagerHelper.beginTransaction();
            coSingleParagraphTeacherDAO.save(singleParagraphTeacher);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(singleParagraphTeacher);
            log.info("Pregunta tipo parrafo creada con �xito: " + singleParagraphTeacher.getParagraphTeacherId());
            Object[] args = { singleParagraphTeacher.getParagraphTeacherId(), singleParagraphTeacher.getMaParagraphForm().getParagraphFormId() };
            result.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.create.success"), args));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            e.printStackTrace();
            log.error("Error al guardar el ejercicio de parrafo: " + e.getMessage());
            result.setError(true);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    public RestServiceResult search(RestServiceResult serviceResult, Long nExerciseId) {
        List<CoParagraphTeacher> listCoSingleParagraphTeacher = null;
        Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.SELECT_CO_SINGLE_PARAGRAPH_TEACHER, CoParagraphTeacher.class);
        query.setParameter(1, nExerciseId);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        listCoSingleParagraphTeacher = query.getResultList();
        if (listCoSingleParagraphTeacher == null || listCoSingleParagraphTeacher.size() == 0) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("singleParagraphTeacher.search.notFound"));
        } else {
            Object[] arrayParam = { listCoSingleParagraphTeacher.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.search.success"), arrayParam));
            serviceResult.setObjResult(listCoSingleParagraphTeacher);
            serviceResult.setNumResult(listCoSingleParagraphTeacher.size());
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
    public RestServiceResult update(RestServiceResult serviceResult, CoParagraphTeacher singleParagraphTeacher) {
        CoParagraphTeacherDAO singleParagraphTeacherDAO = new CoParagraphTeacherDAO();
        try {
            log.info("Actualizando el parrafo : " + singleParagraphTeacher.getParagraphTeacherId());
            EntityManagerHelper.beginTransaction();
            singleParagraphTeacherDAO.update(singleParagraphTeacher);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(singleParagraphTeacher);
            Object[] arrayParam = { singleParagraphTeacher.getParagraphTeacherId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.update.success"), arrayParam));
            log.info("Se actualizo el parrafo con �xito: " + singleParagraphTeacher.getParagraphTeacherId());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el parrafo: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(e.getMessage());
        }
        return serviceResult;
    }

    /**
	 * Obtiene la lista de tecnicas
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
        CoParagraphTeacherDAO singleParagraphTeacherDAO = new CoParagraphTeacherDAO();
        List<CoParagraphTeacher> list = singleParagraphTeacherDAO.findAll(nRowStart, nMaxResults);
        if (list.size() == 0) {
            result.setError(true);
            result.setMessage(bundle.getString("singleParagraphTeacher.list.notFound"));
        } else {
            Object[] array = { list.size() };
            result.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.list.success"), array));
            result.setObjResult(list);
            if ((nRowStart > 0) || (nMaxResults > 0)) result.setNumResult(singleParagraphTeacherDAO.findAll().size()); else result.setNumResult(list.size());
        }
        return result;
    }

    /**
	 * Obtiene la lista de tecnicas por habilidad
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @param nHabilityId
	 *            Es el id o c�digo de la habilidad.	 
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult listbyTeacher(RestServiceResult serviceResult, Long nSingleParagraphTeacherId) {
        CoParagraphTeacher coSingleParagraphTeacher = new CoParagraphTeacherDAO().findById(nSingleParagraphTeacherId);
        EntityManagerHelper.refresh(coSingleParagraphTeacher);
        if (coSingleParagraphTeacher == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("singleParagraphTeacher.search.notFound"));
        } else {
            List<CoParagraphTeacher> list = new ArrayList<CoParagraphTeacher>();
            if (list.size() == 0) {
                Object[] arrayParam = { coSingleParagraphTeacher.getParagraphTeacherId() };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.listbyTeacher.notFound"), arrayParam));
            } else {
                Object[] arrayParam = { list.size(), coSingleParagraphTeacher.getParagraphTeacherId() };
                serviceResult.setMessage(MessageFormat.format(bundle.getString("singleParagraphTeacher.listbyTeacher.success"), arrayParam));
                serviceResult.setObjResult(list);
                serviceResult.setNumResult(list.size());
            }
        }
        return serviceResult;
    }
}
