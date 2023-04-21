package org.jowidgets.test.api.widgets.blueprint.factory;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.test.api.widgets.blueprint.IActionMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICheckBoxBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICheckedMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IComboBoxBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IComboBoxSelectionBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IDialogBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IFrameBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IIconBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IMainMenuBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IRadioMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IScrollCompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorToolBarItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISplitCompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISubMenuBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ITextFieldBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ITextLabelBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToggleButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarContainerItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarPopupButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarToggleButtonBluePrintUi;

public interface IBasicSimpleTestBluePrintFactory {

    <WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends IComponentSetupBuilder<BLUE_PRINT_TYPE> & IWidgetDescriptor<WIDGET_TYPE>> BLUE_PRINT_TYPE bluePrint(final Class<BLUE_PRINT_TYPE> bluePrintType);

    IFrameBluePrintUi frame();

    IDialogBluePrintUi dialog();

    ICompositeBluePrintUi composite();

    IScrollCompositeBluePrintUi scrollComposite();

    ISplitCompositeBluePrintUi splitComposite();

    ITextLabelBluePrintUi textLabel();

    IIconBluePrintUi icon();

    ISeparatorBluePrintUi separator();

    ITextFieldBluePrintUi textField();

    IButtonBluePrintUi button();

    ICheckBoxBluePrintUi checkBox();

    IToggleButtonBluePrintUi toggleButton();

    <INPUT_TYPE> IComboBoxBluePrintUi<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter);

    <INPUT_TYPE> IComboBoxSelectionBluePrintUi<INPUT_TYPE> comboBoxSelection(final IObjectStringConverter<INPUT_TYPE> objectStringConverter);

    IActionMenuItemBluePrintUi menuItem();

    IRadioMenuItemBluePrintUi radioMenuItem();

    ICheckedMenuItemBluePrintUi checkedMenuItem();

    ISubMenuBluePrintUi subMenu();

    IMainMenuBluePrintUi mainMenu();

    ISeparatorMenuItemBluePrintUi menuSeparator();

    ISeparatorToolBarItemBluePrintUi toolBarSeparator();

    IToolBarBluePrintUi toolBar();

    IToolBarButtonBluePrintUi toolBarButton();

    IToolBarToggleButtonBluePrintUi toolBarToggleButton();

    IToolBarPopupButtonBluePrintUi toolBarPopupButton();

    IToolBarContainerItemBluePrintUi toolBarContainerItem();
}
