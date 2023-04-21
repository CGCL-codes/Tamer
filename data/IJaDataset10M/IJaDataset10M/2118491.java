package org.intellij.trinkets.problemsView.icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

/**
 * Icons
 *
 * @author Alexey Efimov
 */
public interface ProblemViewIcons {

    Icon NODE_INFO = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/node/information.png");

    Icon NODE_WARNING = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/node/warning.png");

    Icon NODE_ERROR = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/node/error.png");

    Icon TOOLWINDOW_INFO = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/toolWindow/information.png");

    Icon TOOLWINDOW_WARNING = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/toolWindow/warning.png");

    Icon TOOLWINDOW_ERROR = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/toolWindow/error.png");

    Icon TOOLWINDOW_PAUSE = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/toolWindow/pause.png");

    Icon TOOLWINDOW_RUNNED = IconLoader.getIcon("/org/intellij/trinkets/problemsView/icons/toolWindow/runned.png");
}
