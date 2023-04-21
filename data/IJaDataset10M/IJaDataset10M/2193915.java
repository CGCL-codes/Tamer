package org.paquitosoft.namtia.session.actions.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.paquitosoft.namtia.common.exceptions.DataNotFoundException;
import org.paquitosoft.namtia.common.exceptions.ModelException;
import org.paquitosoft.namtia.session.facade.SessionModelFacade;
import org.paquitosoft.namtia.vo.AlbumVO;

/**
 *
 * @author paquitosoft
 */
public class FindSongsByAlbumSessionAction extends FinderSessionAction {

    /** Creates a new instance of FindSongsByAlbumSessionAction */
    public FindSongsByAlbumSessionAction(String searchText) {
        super(searchText);
    }

    public Collection find() throws ModelException {
        Collection songs = new ArrayList();
        Collection albums = null;
        try {
            albums = new SessionModelFacade().findAlbumsByNameFull(super.searchText);
        } catch (ModelException e) {
            if (e.getCause() == null || !(e.getCause() instanceof DataNotFoundException)) {
                throw e;
            }
        }
        if (albums != null) {
            for (Iterator it = albums.iterator(); it.hasNext(); ) {
                AlbumVO alb = (AlbumVO) it.next();
                if (alb.getSongs() != null) {
                    songs.addAll(alb.getSongs());
                }
            }
        }
        return songs;
    }
}
