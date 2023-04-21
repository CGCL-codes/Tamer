package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.persistence.CoScoreQuestion;
import edu.univalle.lingweb.persistence.CoScoreQuestionDAO;
import edu.univalle.lingweb.persistence.CoScoreQuestionId;
import edu.univalle.lingweb.persistence.EntityManagerHelper;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'co_activity'( Actividades)
 * 
 * @author Jose Aricapa
 */
public class DataManagerScoreMultipleChoiceE3 extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerCourse.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerScoreMultipleChoiceE3() {
        super();
        DOMConfigurator.configure(DataManagerCourse.class.getResource("/log4j.xml"));
    }

    /**
	 * Crea una nueva pregunta de selecci�n en la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coMultipleChoiceE1
	 *            Actividad a guardar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult create(RestServiceResult serviceResult, CoScoreQuestion coScoreQuestion) {
        CoScoreQuestionDAO coScoreQuestionDAO = new CoScoreQuestionDAO();
        try {
            EntityManagerHelper.beginTransaction();
            log.info("valor de sQuestionId DataManager: " + coScoreQuestion.getCoQuestion().getQuestionId());
            log.info("valor de sUserId en el objeto:DataManager " + coScoreQuestion.getMaUser().getUserId());
            log.info("valor de score en el load:DataManager " + coScoreQuestion.getScore());
            coScoreQuestionDAO.save(coScoreQuestion);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coScoreQuestion);
            log.info("calificacion de ejercicio tipo question creada con �xito: " + coScoreQuestion.getCoQuestion().getQuestionName());
            Object[] arrayParam = { coScoreQuestion.getCoQuestion().getQuestionName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("scoreMultipleChoice.create.success"), arrayParam));
        } catch (PersistenceException e) {
            log.error("Error al guardar la calificacion del ejercicio : " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("scoreMultipleChoice.create.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Actualiza una una nueva actividad en la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param comultipleChoice
	 *            Actividad a actualizar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult update(RestServiceResult serviceResult, CoScoreQuestion coScoreQuestion) {
        CoScoreQuestionDAO coScoreMchoiceE3DAO = new CoScoreQuestionDAO();
        try {
            EntityManagerHelper.beginTransaction();
            coScoreMchoiceE3DAO.update(coScoreQuestion);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coScoreQuestion);
            Object[] args = { coScoreQuestion.getCoQuestion() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.update.success"), args));
        } catch (PersistenceException e) {
            log.error("Error al actualiza el resultado: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.update.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Realiza la busqueda de la calificaci�n de un ejercicio
	 *  
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param nmultipleChoiceId
	 *            C�digo de la actividad
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult search(RestServiceResult serviceResult, CoScoreQuestionId coScoreQuestionId) {
        CoScoreQuestion coScoreQuestion = new CoScoreQuestionDAO().findById(coScoreQuestionId);
        if (coScoreQuestion == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("scoreMultipleChoice.search.notFound"));
        } else {
            List<CoScoreQuestion> list = new ArrayList<CoScoreQuestion>();
            EntityManagerHelper.refresh(coScoreQuestion);
            list.add(coScoreQuestion);
            Object[] arrayParam = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("scoreMultipleChoice.search.success"), arrayParam));
            serviceResult.setObjResult(list);
            serviceResult.setNumResult(list.size());
        }
        return serviceResult;
    }
}
