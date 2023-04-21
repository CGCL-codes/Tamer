package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.ITextLabelCommon;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.util.Assert;

public class TextSeparatorImpl extends CompositeBasedControl implements ITextLabel {

    private final IComposite composite;

    private final ITextLabelCommon textLabelWidget;

    private String text;

    public TextSeparatorImpl(final IComposite composite, final ITextSeparatorDescriptor descriptor) {
        super(composite);
        Assert.paramNotNull(composite, "composite");
        Assert.paramNotNull(descriptor, "descriptor");
        this.composite = composite;
        final BluePrintFactory bpF = new BluePrintFactory();
        final ITextLabelDescriptor textLabelDescriptor = bpF.textLabel().setSetup(descriptor);
        if (AlignmentHorizontal.LEFT.equals(descriptor.getAlignment())) {
            this.composite.setLayout(new MigLayoutDescriptor("0[][grow]", "0[]0"));
            textLabelWidget = composite.add(textLabelDescriptor, "");
            composite.add(bpF.separator(), "growx");
        } else if (AlignmentHorizontal.RIGHT.equals(descriptor.getAlignment())) {
            this.composite.setLayout(new MigLayoutDescriptor("0[grow][]", "0[]0"));
            composite.add(bpF.separator(), "growx");
            textLabelWidget = composite.add(textLabelDescriptor, "");
        } else if (AlignmentHorizontal.CENTER.equals(descriptor.getAlignment())) {
            this.composite.setLayout(new MigLayoutDescriptor("0[grow][][grow]", "0[]0"));
            composite.add(bpF.separator(), "growx");
            textLabelWidget = composite.add(textLabelDescriptor, "");
            composite.add(bpF.separator(), "growx");
        } else {
            throw new IllegalArgumentException("Alignment '" + descriptor.getAlignment() + "' is unknown.");
        }
        if (descriptor.getForegroundColor() == null) {
            setForegroundColor(Colors.STRONG);
        }
    }

    @Override
    public void setText(final String text) {
        this.text = text;
        textLabelWidget.setText(text);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setToolTipText(final String text) {
        textLabelWidget.setToolTipText(text);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        textLabelWidget.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return textLabelWidget.isEnabled();
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        textLabelWidget.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        textLabelWidget.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return textLabelWidget.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return textLabelWidget.getBackgroundColor();
    }

    @Override
    public void setMarkup(final Markup markup) {
        textLabelWidget.setMarkup(markup);
    }

    @Override
    public void setFontSize(final int size) {
        textLabelWidget.setFontSize(size);
    }

    @Override
    public void setFontName(final String fontName) {
        textLabelWidget.setFontName(fontName);
    }

    @Override
    public void setPopupMenu(final IMenuModel popupMenu) {
        composite.setPopupMenu(popupMenu);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        composite.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        composite.removePopupDetectionListener(listener);
    }
}
