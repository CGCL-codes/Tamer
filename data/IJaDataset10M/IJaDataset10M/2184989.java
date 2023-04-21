package org.fudaa.fudaa.navmer;

import com.memoire.bu.BuResource;
import org.fudaa.fudaa.ressource.FudaaResource;

/**
 * put your module comment here
 * Ressources pour Navmer.
 *
 * @version      $Revision: 1.5 $ $Date: 2006-09-19 15:11:54 $ by $Author: deniger $
 * @author       Guillaume Desnoix
 */
public class NavmerResource extends BuResource {

    public static final NavmerResource NAVMER = new NavmerResource(FudaaResource.FUDAA);

    public NavmerResource(final BuResource _parent) {
        setParent(_parent);
    }
}
