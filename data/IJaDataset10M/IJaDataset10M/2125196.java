package org.fudaa.fudaa.sinavi;

import com.memoire.bu.BuResource;
import org.fudaa.fudaa.ressource.FudaaResource;

/**
 * Ressource de Sinavi
 * 
 * @version $Revision: 1.9 $ $Date: 2006-09-19 15:09:00 $ by $Author: deniger $
 * @author Aline Marechalle , Franck Lejeune
 */
public class SinaviResource extends FudaaResource {

    /**
   * une ressource � utiliser directement
   */
    public static final SinaviResource SINAVI = new SinaviResource(FudaaResource.FUDAA);

    /**
   * SinaviResource
   * 
   * @param _parent
   */
    public SinaviResource(final BuResource _parent) {
        super(_parent);
    }
}
