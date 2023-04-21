package org.jowidgets.spi.impl.javafx;

import java.awt.Container;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.impl.javafx.widgets.ButtonImpl;
import org.jowidgets.spi.impl.javafx.widgets.CheckBoxImpl;
import org.jowidgets.spi.impl.javafx.widgets.ComboBoxImpl;
import org.jowidgets.spi.impl.javafx.widgets.ComboBoxSelectionImpl;
import org.jowidgets.spi.impl.javafx.widgets.CompositeImpl;
import org.jowidgets.spi.impl.javafx.widgets.CompositeWrapper;
import org.jowidgets.spi.impl.javafx.widgets.DialogImpl;
import org.jowidgets.spi.impl.javafx.widgets.FrameImpl;
import org.jowidgets.spi.impl.javafx.widgets.IconImpl;
import org.jowidgets.spi.impl.javafx.widgets.ProgressBarImpl;
import org.jowidgets.spi.impl.javafx.widgets.ScrollCompositeImpl;
import org.jowidgets.spi.impl.javafx.widgets.SeparatorImpl;
import org.jowidgets.spi.impl.javafx.widgets.SplitCompositeImpl;
import org.jowidgets.spi.impl.javafx.widgets.TabFolderImpl;
import org.jowidgets.spi.impl.javafx.widgets.TextAreaImpl;
import org.jowidgets.spi.impl.javafx.widgets.TextFieldImpl;
import org.jowidgets.spi.impl.javafx.widgets.TextLabelImpl;
import org.jowidgets.spi.impl.javafx.widgets.ToggleButtonImpl;
import org.jowidgets.spi.impl.javafx.widgets.ToolBarImpl;
import org.jowidgets.spi.impl.javafx.widgets.TreeImpl;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.IPopupDialogSpi;
import org.jowidgets.spi.widgets.IProgressBarSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.spi.widgets.ITextLabelSpi;
import org.jowidgets.spi.widgets.IToggleButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.ICompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;
import org.jowidgets.spi.widgets.setup.IPopupDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IProgressBarSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.IToolBarSetupSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;
import org.jowidgets.util.Assert;

public final class JavafxWidgetFactory implements IWidgetFactorySpi {

    public JavafxWidgetFactory() {
    }

    @Override
    public boolean hasMigLayoutSupport() {
        return false;
    }

    @Override
    public boolean isConvertibleToFrame(final Object uiReference) {
        return uiReference instanceof Stage;
    }

    @Override
    public IFrameSpi createFrame(final IGenericWidgetFactory factory, final Object uiReference) {
        Assert.paramNotNull(factory, "factory");
        Assert.paramNotNull(uiReference, "uiReference");
        if (uiReference instanceof Stage) {
            return new FrameImpl(factory, (Stage) uiReference);
        }
        throw new IllegalArgumentException("UiReference must be instanceof of '" + Stage.class.getName() + "'");
    }

    @Override
    public boolean isConvertibleToComposite(final Object uiReference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ICompositeSpi createComposite(final IGenericWidgetFactory factory, final Object uiReference) {
        Assert.paramNotNull(factory, "factory");
        Assert.paramNotNull(uiReference, "uiReference");
        if (uiReference instanceof Container) {
            return new CompositeWrapper(factory, (Pane) uiReference);
        }
        throw new IllegalArgumentException("UiReference must be instanceof of '" + Pane.class.getName() + "'");
    }

    @Override
    public IFrameSpi createFrame(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
        return new FrameImpl(factory, setup);
    }

    @Override
    public IFrameSpi createDialog(final IGenericWidgetFactory factory, final Object parentUiReference, final IDialogSetupSpi setup) {
        return new DialogImpl(factory, parentUiReference, setup);
    }

    @Override
    public IPopupDialogSpi createPopupDialog(final IGenericWidgetFactory factory, final Object parentUiReference, final IPopupDialogSetupSpi setup) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ICompositeSpi createComposite(final IGenericWidgetFactory factory, final Object parentUiReference, final ICompositeSetupSpi setup) {
        return new CompositeImpl(factory, setup);
    }

    @Override
    public IScrollCompositeSpi createScrollComposite(final IGenericWidgetFactory factory, final Object parentUiReference, final IScrollCompositeSetupSpi setup) {
        return new ScrollCompositeImpl(factory, setup);
    }

    @Override
    public ISplitCompositeSpi createSplitComposite(final IGenericWidgetFactory factory, final Object parentUiReference, final ISplitCompositeSetupSpi setup) {
        return new SplitCompositeImpl(factory, setup);
    }

    @Override
    public ITextControlSpi createTextField(final Object parentUiReference, final ITextFieldSetupSpi setup) {
        return new TextFieldImpl(setup);
    }

    @Override
    public ITextAreaSpi createTextArea(final Object parentUiReference, final ITextAreaSetupSpi setup) {
        return new TextAreaImpl(setup);
    }

    @Override
    public ITextLabelSpi createTextLabel(final Object parentUiReference, final ITextLabelSetupSpi setup) {
        return new TextLabelImpl(setup);
    }

    @Override
    public IIconSpi createIcon(final Object parentUiReference, final IIconSetupSpi setup) {
        return new IconImpl(setup);
    }

    @Override
    public IButtonSpi createButton(final Object parentUiReference, final IButtonSetupSpi setup) {
        return new ButtonImpl(setup);
    }

    @Override
    public IControlSpi createSeparator(final Object parentUiReference, final ISeparatorSetupSpi setup) {
        return new SeparatorImpl(setup);
    }

    @Override
    public ICheckBoxSpi createCheckBox(final Object parentUiReference, final ICheckBoxSetupSpi setup) {
        return new CheckBoxImpl(setup);
    }

    @Override
    public IToggleButtonSpi createToggleButton(final Object parentUiReference, final IToggleButtonSetupSpi setup) {
        return new ToggleButtonImpl(setup);
    }

    @Override
    public IComboBoxSelectionSpi createComboBoxSelection(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
        return new ComboBoxSelectionImpl(setup);
    }

    @Override
    public IComboBoxSpi createComboBox(final Object parentUiReference, final IComboBoxSetupSpi setup) {
        return new ComboBoxImpl(setup);
    }

    @Override
    public IProgressBarSpi createProgressBar(final Object parentUiReference, final IProgressBarSetupSpi setup) {
        return new ProgressBarImpl(setup);
    }

    @Override
    public IToolBarSpi createToolBar(final IGenericWidgetFactory factory, final Object parentUiReference, final IToolBarSetupSpi setup) {
        return new ToolBarImpl(factory, setup);
    }

    @Override
    public ITabFolderSpi createTabFolder(final IGenericWidgetFactory factory, final Object parentUiReference, final ITabFolderSetupSpi setup) {
        return new TabFolderImpl(factory, setup);
    }

    @Override
    public ITreeSpi createTree(final Object parentUiReference, final ITreeSetupSpi setup) {
        return new TreeImpl(setup);
    }

    @Override
    public ITableSpi createTable(final Object parentUiReference, final ITableSetupSpi setup) {
        throw new UnsupportedOperationException();
    }
}
