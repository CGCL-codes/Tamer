package com.aptana.ide.editor.css;

import org.eclipse.jface.preference.IPreferenceStore;
import com.aptana.ide.core.PluginUtils;
import com.aptana.ide.editor.css.parsing.CSSMimeType;
import com.aptana.ide.editors.unified.FileService;
import com.aptana.ide.editors.unified.errors.UnifiedErrorManager;

/**
 * @author Robin Debreuil
 * @author Kevin Lindsey
 */
public class CSSErrorManager extends UnifiedErrorManager {

    /**
	 * CSSErrorManager
	 * 
	 * @param fileService
	 */
    public CSSErrorManager(FileService fileService) {
        super(fileService, CSSMimeType.MimeType);
    }

    /**
	 * Returns the preference store
	 * 
	 * @return IPreferenceStore
	 */
    protected IPreferenceStore getPreferenceStore() {
        if (PluginUtils.isPluginLoaded(CSSPlugin.getDefault())) {
            return CSSPlugin.getDefault().getPreferenceStore();
        } else {
            return null;
        }
    }
}
