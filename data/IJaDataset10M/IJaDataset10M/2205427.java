package org.plugin.ig;

import java.util.Hashtable;
import java.util.Map;
import javax.swing.Action;
import org.plugin.ig.actions.*;
import org.plugin.ig.db.TIGDataBase;
import tico.editor.TEditor;

/**
 * Map of actions that can be done to an editor.
 * 
 * @author Pablo Mu�oz
 * @version 1.0 Nov 20, 2006
 */
public class TIGActionSet {

    /**
	 * The <code>TProjectPropertiesAction</code> id
	 */
    public static final String GALLERY_KEY_ACTION = "galleryOpenAction";

    /**
	 * The <code>TProjectPropertiesAction</code> id
	 */
    public static final String GALLERY_NEW_IMAGE_ACTION = "galleryNewImageAction";

    /**
	 * The <code>TProjectPropertiesAction</code> id
	 */
    public static final String GALLERY_MANAGE_ACTION = "galleryManageAction";

    /**
	 * The <code>TProjectPropertiesAction</code> id
	 */
    public static final String GALLERY_LOAD_DB_ACTION = "galleryLoadDBAction";

    /**
	 * The <code>TProjectPropertiesAction</code> id
	 */
    public static final String GALLERY_EXPORT_DB_ACTION = "galleryExportDBAction";

    private Map actionSet;

    /**
	 * Creates a new <code>TActionSet</code> for the specified <code>editor</code>
	 * with all the posible actions.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
    public TIGActionSet(TEditor editor, TIGDataBase dataBase) {
        actionSet = new Hashtable();
        actionSet.put(GALLERY_KEY_ACTION, new TIGKeyWordGalleryAction(editor, dataBase));
        actionSet.put(GALLERY_NEW_IMAGE_ACTION, new TIGManageGallery(editor, dataBase));
        actionSet.put(GALLERY_MANAGE_ACTION, new TIGManageImageAction(editor, dataBase));
        actionSet.put(GALLERY_LOAD_DB_ACTION, new TIGLoadDBAction(editor, dataBase));
        actionSet.put(GALLERY_EXPORT_DB_ACTION, new TIGExportDBAction(editor, dataBase));
    }

    /**
	 * Returns the <code>action</code> with the specified <code>actionId</code>.
	 * 
	 * @param actionId The specified <code>actionId</code>
	 * @return The <code>action</code> with the specified <code>actionId</code>
	 */
    public Action getAction(String actionId) {
        return (Action) actionSet.get(actionId);
    }
}
