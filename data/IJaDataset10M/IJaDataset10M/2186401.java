package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.persistence.CoMetacognitiveVariable;
import edu.univalle.lingweb.persistence.CoMetacognitiveVariableDAO;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'co_metacognitive_variable'( Variable Metacognitiva para el ejercicio )
 * 
 * @author Julio Cesar Puentes
 */
public class DataManagerMetacognitiveVariable extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerCourse.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerMetacognitiveVariable() {
        super();
        DOMConfigurator.configure(DataManagerCourse.class.getResource("/log4j.xml"));
    }

    /**
	 * Obtiene la lista de variables metacognitivas
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult result) {
        return list(result, 0, 0);
    }

    /**
	 * Obtiene la lista de variables metacognitivas en un rango determinado
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {
        CoMetacognitiveVariableDAO coMetacognitiveVariableDAO = new CoMetacognitiveVariableDAO();
        List<CoMetacognitiveVariable> list = coMetacognitiveVariableDAO.findAll();
        if (list.size() == 0) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("metacognitiveVariable.list.notFound"));
        } else {
            Object[] array = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("metacognitiveVariable.list.success"), array));
            serviceResult.setObjResult(list);
            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(coMetacognitiveVariableDAO.findAll().size()); else serviceResult.setNumResult(list.size());
        }
        return serviceResult;
    }
}
