package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Resource;

/**
* $Id: ResourcesCollection.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
* <b>T�tol:</b> ResourcesCollection<br><br>
* <b>Descripci�:</b> Ser� on enmagatzemarem tots els resources de<br>
* l'organitzaci� de manera que sigui f�cilment accessible.<br><br> 
*
* @author Eduard C�spedes i Borr�s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versi� $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
* $Log: ResourcesCollection.java,v $
* Revision 1.3  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral�lel amb el proc�s de desenvolupament del gestor de BD.
*
* Revision 1.2  2007/11/08 16:33:09  ecespedes
* Comen�ant a Implementar el OverallSequencingProcess, que ser�
* les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
* realment el seq�enciament SCORM1.3
*
*/
public class ResourcesCollection implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5411265846718004225L;

    /**
	 * �s el Collection on guardarem tots els resources.
	 */
    private Map<String, Resource> resources = Collections.synchronizedMap(new HashMap<String, Resource>());

    /**
	 * El constructor per defecte.
	 */
    public ResourcesCollection() {
    }

    /**
	 * El constructor on ja li passem el resource inicial.
	 * 
	 * @param nouResource : Resource Type
	 */
    public ResourcesCollection(final Resource nouResource) {
        addResource(nouResource);
    }

    /** 
	 * Ens busca un Resource dintre del ResourceCollection i ens el retorna.
	 * 
	 * @param resourceID : Nom identificador del Resources. 
	 * @return Resource : Si troba un seq�enciament que encaixi 
	 * amb el nom ens el retorna.
	 */
    public final Resource searchResource(final String resourceID) {
        return resources.get(resourceID);
    }

    /**	 
	 * Ens afegir� un resource dintre del "repositori" de Resources
	 * d'aquella organitzaci�.
	 * 
	 * @param nouResource : El Resource que volem afegir a la "col�lecci�".
	 */
    public final void addResource(final Resource nouResource) {
        resources.put(nouResource.getIdentifier(), nouResource);
    }
}
