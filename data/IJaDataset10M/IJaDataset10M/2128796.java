package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ILabelSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.IIconCommon;
import org.jowidgets.common.widgets.ITextLabelCommon;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.util.NullCompatibleEquivalence;

public class LabelImpl extends CompositeBasedControl implements ILabel {

    private final IIconCommon iconWidget;

    private final ITextLabelCommon textLabelWidget;

    private final IComposite composite;

    private String text;

    public LabelImpl(final IComposite composite, final ILabelSetup setup) {
        super(composite);
        this.composite = composite;
        this.composite.setLayout(new MigLayoutDescriptor("0[][grow]0", "0[]0"));
        final BluePrintFactory bpF = new BluePrintFactory();
        final IIconDescriptor iconDescriptor = bpF.icon(setup.getIcon());
        this.iconWidget = composite.add(iconDescriptor, "w 0::");
        final ITextLabelDescriptor textLabelDescriptor = bpF.textLabel().setSetup(setup);
        this.textLabelWidget = composite.add(textLabelDescriptor, "w 0::, grow");
        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);
    }

    @Override
    public void setText(final String text) {
        if (!NullCompatibleEquivalence.equals(text, this.text)) {
            this.text = text;
            textLabelWidget.setText(text);
            composite.layoutBegin();
            composite.layoutEnd();
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setToolTipText(final String text) {
        textLabelWidget.setToolTipText(text);
        iconWidget.setToolTipText(text);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        textLabelWidget.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        textLabelWidget.setBackgroundColor(colorValue);
        iconWidget.setBackgroundColor(colorValue);
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
    public void setEnabled(final boolean enabled) {
        textLabelWidget.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return textLabelWidget.isEnabled();
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
    public void setIcon(final IImageConstant icon) {
        final Dimension lastPreferredSize = iconWidget.getPreferredSize();
        iconWidget.setIcon(icon);
        if (!lastPreferredSize.equals(iconWidget.getPreferredSize())) {
            composite.layoutBegin();
            composite.layoutEnd();
        }
    }
}
