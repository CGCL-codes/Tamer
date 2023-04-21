package com.apachetune.core.ui;

import com.apachetune.core.ui.actions.ActionManager;
import com.apachetune.core.ui.actions.impl.ActionManagerImpl;
import com.apachetune.core.ui.editors.EditorManager;
import com.apachetune.core.ui.editors.EditorWorkItem;
import com.apachetune.core.ui.editors.SaveFilesHelper;
import com.apachetune.core.ui.editors.impl.EditorManagerImpl;
import com.apachetune.core.ui.editors.impl.EditorWorkItemImpl;
import com.apachetune.core.ui.editors.impl.SaveAllFilesAtOnceHelperImpl;
import com.apachetune.core.ui.editors.impl.SaveFilesSeparatelyHelperImpl;
import com.apachetune.core.ui.feedbacksystem.*;
import com.apachetune.core.ui.feedbacksystem.impl.RemoteManagerImpl;
import com.apachetune.core.ui.feedbacksystem.impl.SendUserFeedbackMessageDialogImpl;
import com.apachetune.core.ui.feedbacksystem.impl.UserFeedbackManagerImpl;
import com.apachetune.core.ui.impl.*;
import com.apachetune.core.ui.statusbar.StatusBarManager;
import com.apachetune.core.ui.statusbar.StatusBarView;
import com.google.inject.AbstractModule;
import org.noos.xing.mydoggy.ToolWindowManager;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import static com.apachetune.core.ui.Constants.*;
import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.name.Names.named;

/**
 * FIXDOC                                   
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class CoreUIModule extends AbstractModule {

    protected void configure() {
        bind(UIWorkItem.class).annotatedWith(named(CORE_UI_WORK_ITEM)).to(CoreUIWorkItem.class).in(SINGLETON);
        bind(JFrame.class).in(SINGLETON);
        bind(ToolWindowManager.class).annotatedWith(named(TOOL_WINDOW_MANAGER)).to(MyDoggyToolWindowManager.class).in(SINGLETON);
        bind(MenuBarManager.class).to(MenuBarManagerImpl.class).in(SINGLETON);
        bind(ToolBarManager.class).to(ToolBarManagerImpl.class).in(SINGLETON);
        bind(StatusBarManager.class).to(StatusBarManagerImpl.class).in(SINGLETON);
        bind(ActionManager.class).to(ActionManagerImpl.class).in(SINGLETON);
        bind(CoreUIUtils.class).in(SINGLETON);
        bind(JToolBar.class).in(SINGLETON);
        bind(StatusBarManagerImpl.class).in(SINGLETON);
        bind(StatusBarView.class).to(StatusBarViewImpl.class).in(SINGLETON);
        bind(ToolBarManagerImpl.class).in(SINGLETON);
        bind(TitleBarManager.class).to(TitleBarManagerImpl.class).in(SINGLETON);
        bind(EditorManager.class).to(EditorManagerImpl.class).in(SINGLETON);
        bind(EditorWorkItem.class).to(EditorWorkItemImpl.class);
        bind(SaveFilesHelper.class).annotatedWith(named(SAVE_ALL_FILES_AT_ONCE_HELPER)).to(SaveAllFilesAtOnceHelperImpl.class);
        bind(SaveFilesHelper.class).annotatedWith(named(SAVE_ALL_FILES_SEPARATELY_HELPER)).to(SaveFilesSeparatelyHelperImpl.class);
        bind(OutputPaneDocument.class).to(OutputPaneDocumentImpl.class).in(SINGLETON);
        bind(UserFeedbackManager.class).to(UserFeedbackManagerImpl.class).in(SINGLETON);
        bind(RemoteManager.class).to(RemoteManagerImpl.class).in(SINGLETON);
        bind(SendUserFeedbackMessageDialog.class).to(SendUserFeedbackMessageDialogImpl.class);
        bind(UserFeedbackView.class).to(UserFeedbackSmartPart.class);
    }
}
