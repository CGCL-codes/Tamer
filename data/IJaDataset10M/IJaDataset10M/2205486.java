package com.anecdote.ideaplugins.commitlog;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import javax.swing.*;
import java.util.*;

public class CommitLogWindow {

    private Project _project;

    private Set<Editor> _commitLogs = new HashSet<Editor>();

    private boolean _isInitialized, _isDisposed;

    private static final Logger LOG = Logger.getInstance("#com.intellij.cvsSupport2.ui.CvsTabbedWindow");

    private ContentManager _contentManager;

    private static final String COMMIT_LOG_HELP_ID = "commitlog.commitlog";

    private static final String COMMIT_LOGS_TOOLWINDOW_ID = "Commit Logs";

    private static final String COMMIT_LOGS_SMALL_ICON_NAME = "/resources/commitlogsmall.png";

    private static final Icon COMMIT_LOGS_SMALL_ICON = IconLoader.getIcon(COMMIT_LOGS_SMALL_ICON_NAME);

    public CommitLogWindow(Project project) {
        _project = project;
        Disposer.register(project, new Disposable() {

            public void dispose() {
                try {
                    for (Editor commitLog : _commitLogs) {
                        EditorFactory.getInstance().releaseEditor(commitLog);
                    }
                    _commitLogs.clear();
                    CommitLogWindow.LOG.assertTrue(!_isDisposed);
                    if (!_isInitialized) {
                        _isDisposed = true;
                        return;
                    }
                    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
                    toolWindowManager.unregisterToolWindow(COMMIT_LOGS_TOOLWINDOW_ID);
                } finally {
                    _isDisposed = true;
                }
            }
        });
    }

    private void initialize() {
        if (!_isInitialized) {
            _isInitialized = true;
            _isDisposed = false;
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
            ToolWindow toolWindow = toolWindowManager.registerToolWindow(COMMIT_LOGS_TOOLWINDOW_ID, true, ToolWindowAnchor.BOTTOM);
            toolWindow.setIcon(COMMIT_LOGS_SMALL_ICON);
            _contentManager = toolWindow.getContentManager();
            toolWindow.installWatcher(_contentManager);
            _contentManager.addContentManagerListener(new ContentManagerAdapter() {

                @Override
                public void contentRemoved(ContentManagerEvent event) {
                    JComponent component = event.getContent().getComponent();
                    JComponent removedComponent = (component instanceof CommitLogWindowComponent) ? ((CommitLogWindowComponent) component).getComponent() : component;
                    for (Iterator iterator = _commitLogs.iterator(); iterator.hasNext(); ) {
                        Editor editor = (Editor) iterator.next();
                        if (removedComponent == editor.getComponent()) {
                            EditorFactory.getInstance().releaseEditor(editor);
                            iterator.remove();
                        }
                    }
                }
            });
        }
    }

    private int getComponentAt(int i, boolean select) {
        if (select) {
            getContentManager().setSelectedContent(getContentManager().getContent(i));
        }
        return i;
    }

    public int addTab(String s, JComponent component, boolean selectTab, boolean replaceContent, boolean lockable, boolean addDefaultToolbar, ActionGroup toolbarActions, String helpId) {
        int existing = getComponentNumNamed(s);
        ContentManager contentManager = getContentManager();
        if (existing != -1) {
            Content existingContent = contentManager.getContent(existing);
            if (!replaceContent) {
                contentManager.setSelectedContent(existingContent);
                return existing;
            }
        }
        CommitLogWindowComponent newComponent = new CommitLogWindowComponent(component, addDefaultToolbar, toolbarActions, contentManager, helpId);
        Content content = ContentFactory.SERVICE.getInstance().createContent(newComponent.getShownComponent(), s, lockable);
        newComponent.setContent(content);
        contentManager.addContent(content);
        return getComponentAt(contentManager.getContentCount() - 1, selectTab);
    }

    private int getComponentNumNamed(String s) {
        for (int i = 0; i < getContentManager().getContentCount(); i++) {
            final Content content = getContentManager().getContent(i);
            if (content != null && s.equals(content.getDisplayName())) {
                return i;
            }
        }
        return -1;
    }

    private static class CopyContentAction extends AnAction {

        private final Editor _commitLog;

        @Override
        public void actionPerformed(AnActionEvent e) {
            final boolean hasSelection = _commitLog.getSelectionModel().hasSelection();
            if (!hasSelection) {
                _commitLog.getSelectionModel().setSelection(0, _commitLog.getDocument().getCharsSequence().length() - 1);
            }
            _commitLog.getSelectionModel().copySelectionToClipboard();
            if (!hasSelection) {
                _commitLog.getSelectionModel().removeSelection();
            }
        }

        public CopyContentAction(Editor commitLog) {
            super("Copy", "Copy content to clipboard", IconLoader.getIcon("/actions/copy.png"));
            _commitLog = commitLog;
        }
    }

    public Editor addCommitLog(String title, Editor commitLog) {
        boolean notExist = !_commitLogs.contains(commitLog);
        LOG.assertTrue(notExist);
        if (notExist) {
            DefaultActionGroup actions = new DefaultActionGroup();
            actions.add(new CopyContentAction(commitLog));
            addTab(title, commitLog.getComponent(), true, false, false, true, actions, COMMIT_LOG_HELP_ID);
            _commitLogs.add(commitLog);
        }
        return commitLog;
    }

    public void ensureVisible(Project project) {
        if (project == null) {
            return;
        }
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        if (toolWindowManager != null) {
            ToolWindow toolWindow = toolWindowManager.getToolWindow(COMMIT_LOGS_TOOLWINDOW_ID);
            if (toolWindow != null) {
                toolWindow.activate(null);
            }
        }
    }

    public ContentManager getContentManager() {
        initialize();
        return _contentManager;
    }
}
