package org.jowidgets.impl.layout.miglayout;

import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.IToggleButton;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.impl.layout.miglayout.common.IComponentWrapper;

final class ComponentTypeUtil {

    private ComponentTypeUtil() {
    }

    static int getType(final IComponent component) {
        if (isContainer(component)) {
            return IComponentWrapper.TYPE_CONTAINER;
        }
        if (isButton(component)) {
            return IComponentWrapper.TYPE_BUTTON;
        }
        if (isLabel(component)) {
            return IComponentWrapper.TYPE_LABEL;
        }
        if (isTextField(component)) {
            return IComponentWrapper.TYPE_TEXT_FIELD;
        }
        if (isTextArea(component)) {
            return IComponentWrapper.TYPE_TEXT_AREA;
        }
        if (isList(component)) {
            return IComponentWrapper.TYPE_LIST;
        }
        if (isTable(component)) {
            return IComponentWrapper.TYPE_TABLE;
        }
        if (isScrollPane(component)) {
            return IComponentWrapper.TYPE_SCROLL_PANE;
        }
        if (isImage(component)) {
            return IComponentWrapper.TYPE_IMAGE;
        }
        if (isPanel(component)) {
            return IComponentWrapper.TYPE_PANEL;
        }
        if (isComboBox(component)) {
            return IComponentWrapper.TYPE_COMBO_BOX;
        }
        if (isCheckBox(component)) {
            return IComponentWrapper.TYPE_CHECK_BOX;
        }
        if (isProgressBar(component)) {
            return IComponentWrapper.TYPE_PROGRESS_BAR;
        }
        if (isTree(component)) {
            return IComponentWrapper.TYPE_TREE;
        }
        if (isSlider(component)) {
            return IComponentWrapper.TYPE_SLIDER;
        }
        if (isSpinner(component)) {
            return IComponentWrapper.TYPE_SPINNER;
        }
        if (isScrollBar(component)) {
            return IComponentWrapper.TYPE_SCROLL_BAR;
        }
        if (isSeparator(component)) {
            return IComponentWrapper.TYPE_SEPARATOR;
        }
        return IComponentWrapper.TYPE_UNKNOWN;
    }

    private static boolean isSeparator(final IComponent component) {
        return false;
    }

    private static boolean isScrollBar(final IComponent component) {
        return false;
    }

    private static boolean isTree(final IComponent component) {
        return (component instanceof ITree);
    }

    private static boolean isProgressBar(final IComponent component) {
        return (component instanceof IProgressBar);
    }

    private static boolean isCheckBox(final IComponent component) {
        return (component instanceof ICheckBox);
    }

    private static boolean isSpinner(final IComponent component) {
        return false;
    }

    private static boolean isSlider(final IComponent component) {
        return false;
    }

    private static boolean isComboBox(final IComponent component) {
        return (component instanceof IComboBox);
    }

    private static boolean isPanel(final IComponent component) {
        return false;
    }

    private static boolean isImage(final IComponent component) {
        return (component instanceof IIcon);
    }

    private static boolean isScrollPane(final IComponent component) {
        return (component instanceof IScrollComposite);
    }

    private static boolean isTable(final IComponent component) {
        return (component instanceof ITable);
    }

    private static boolean isList(final IComponent component) {
        return false;
    }

    private static boolean isButton(final IComponent component) {
        return ((component instanceof IButton) || (component instanceof IToggleButton) || (component instanceof IToolBarButton) || (component instanceof IToolBarPopupButton) || (component instanceof IToolBarToggleButton));
    }

    private static boolean isContainer(final IComponent component) {
        return ((component instanceof IFrame) || (component instanceof IContainer) || (component instanceof ITabFolder) || (component instanceof IToolBar) || (component instanceof ISplitComposite) || (component instanceof IMenuBar));
    }

    private static boolean isLabel(final IComponent component) {
        return ((component instanceof ILabel) || (component instanceof IValidationResultLabel));
    }

    private static boolean isTextField(final IComponent component) {
        return ((component instanceof IInputField) || (component instanceof IInputControl));
    }

    private static boolean isTextArea(final IComponent component) {
        return (component instanceof ITextArea);
    }
}
