package com.apachetune.core.ui.editors;

import com.apachetune.core.ui.actions.ActionHandler;
import com.apachetune.core.ui.actions.ActionPermission;
import com.apachetune.core.ui.actions.ActionSite;
import static com.apachetune.core.ui.Constants.*;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public interface EditorActionSite extends ActionSite {

    @ActionHandler(EDIT_COPY_ACTION)
    void onCopy();

    @ActionPermission(EDIT_COPY_ACTION)
    boolean isCopyEnabled();

    @ActionHandler(EDIT_CUT_ACTION)
    void onCut();

    @ActionPermission(EDIT_CUT_ACTION)
    boolean isCutEnabled();

    @ActionHandler(EDIT_PASTE_ACTION)
    void onPaste();

    @ActionPermission(EDIT_PASTE_ACTION)
    boolean isPasteEnabled();

    @ActionHandler(EDIT_SELECT_ALL_ACTION)
    void onSelectAll();

    @ActionPermission(EDIT_SELECT_ALL_ACTION)
    boolean isSelectAllEnabled();
}
