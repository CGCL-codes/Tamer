package com.apachetune.core.ui;

import com.apachetune.core.ui.actions.Action;
import com.apachetune.core.ui.actions.ActionGroup;
import com.apachetune.core.ui.actions.ActionManager;
import com.apachetune.core.ui.actions.ActionSite;
import com.apachetune.core.ui.statusbar.StatusBarManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.MultiSplitConstraint;
import org.noos.xing.mydoggy.ToolWindowManager;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import static com.apachetune.core.ui.Constants.TOOL_WINDOW_MANAGER;
import static java.util.Arrays.asList;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isEventDispatchThread;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import static org.noos.xing.mydoggy.AggregationPosition.BOTTOM;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class CoreUIUtils {

    private final StatusBarManager statusBarManager;

    private final ActionManager actionManager;

    private final ToolWindowManager toolWindowManager;

    @Inject
    public CoreUIUtils(StatusBarManager statusBarManager, ActionManager actionManager, @Named(TOOL_WINDOW_MANAGER) ToolWindowManager toolWindowManager) {
        this.statusBarManager = statusBarManager;
        this.actionManager = actionManager;
        this.toolWindowManager = toolWindowManager;
    }

    public Action createAndConfigureAction(String actionId, Class<? extends ActionSite> actionSiteClass, ActionGroup actionGroup, String name, String shortDescription, String longDescription, ImageIcon smallIcon, ImageIcon largeIcon, char mnemonicKey, KeyStroke acceleratorKey, boolean showInCtxMenu) throws RuntimeException {
        notNull(actionId, "Argument actionId cannot be a null");
        isTrue(!actionId.isEmpty(), "Argument actionId cannot be empty");
        notNull(actionSiteClass, "Argument actionSiteClass cannot be a null");
        notNull(actionGroup, "Argument actionGroup cannot be a null");
        Action action = actionManager.createAction(actionId, actionSiteClass);
        action.setName(name);
        action.setShortDescription(shortDescription);
        action.setLongDescription(longDescription);
        action.setSmallIcon(smallIcon);
        action.setLargeIcon(largeIcon);
        action.setMnemonicKey(mnemonicKey);
        action.setAcceleratorKey(acceleratorKey);
        action.setShowInContextMenu(showInCtxMenu);
        actionGroup.addAction(action);
        return action;
    }

    public void addUIActionHint(AbstractButton component) {
        notNull(component, "Argument component cannot be a null");
        final Action action = (Action) component.getAction();
        notNull(action, "The component should have an ui-action [component = " + component + "; this = " + this + "]");
        component.addMouseListener(new MouseAdapter() {

            private boolean needReleaseStatus = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                statusBarManager.addMainStatus(action.getId(), action.getLongDescription());
                needReleaseStatus = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (needReleaseStatus) {
                    statusBarManager.removeMainStatus(action.getId());
                    needReleaseStatus = false;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (needReleaseStatus) {
                    statusBarManager.removeMainStatus(action.getId());
                    needReleaseStatus = false;
                }
            }
        });
    }

    public Content addContentToNestedToolWindowManager(String id, String title, Icon icon, JComponent component, String tip) {
        ContentManager nestedContentManager = toolWindowManager.getContentManager();
        List<Content> contents = asList(nestedContentManager.getContents());
        MultiSplitConstraint constraint;
        if (contents.size() == 0) {
            constraint = new MultiSplitConstraint(BOTTOM);
        } else {
            constraint = new MultiSplitConstraint(contents.get(0), contents.size());
        }
        return nestedContentManager.addContent(id, title, icon, component, tip, constraint);
    }

    public void safeEDTCall(Runnable runnable) {
        notNull(runnable, "Argument runnable cannot be a null");
        if (isEventDispatchThread()) {
            runnable.run();
        } else {
            invokeLater(runnable);
        }
    }
}
