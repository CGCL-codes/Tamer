package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.persistence.MaRole;
import edu.univalle.lingweb.persistence.MaRoleDAO;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'ma_role'( Lista de perfiles de usuarios)
 * 
 * @author Jose Aricapa
 */
public class DataManagerRole extends DataManager {

    /**
	 * Manejador de mensajes de Log'ss
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerRole.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerRole() {
        super();
        DOMConfigurator.configure(DataManagerRole.class.getResource("/log4j.xml"));
    }

    /**
	 * Obtiene la lista de perfiles
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult result) {
        MaRoleDAO maRoleDAO = new MaRoleDAO();
        List<MaRole> list = maRoleDAO.findAll();
        if (list.size() == 0) {
            result.setError(true);
            result.setMessage(bundle.getString("role.list.notFound"));
        } else {
            Object[] array = { list.size() - 1 };
            result.setMessage(MessageFormat.format(bundle.getString("role.list.success"), array));
            result.setObjResult(list);
        }
        return result;
    }
}
