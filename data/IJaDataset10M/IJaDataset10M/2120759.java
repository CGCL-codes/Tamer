package net.sf.latexdraw.instruments;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import net.sf.latexdraw.actions.ModifyPencilParameter;
import net.sf.latexdraw.actions.ModifyShapeProperty;
import net.sf.latexdraw.actions.ShapeProperties;
import net.sf.latexdraw.badaboom.BadaboomCollector;
import net.sf.latexdraw.glib.models.interfaces.IGroup;
import net.sf.latexdraw.glib.models.interfaces.ILineArcShape;
import net.sf.latexdraw.glib.models.interfaces.IShape;
import net.sf.latexdraw.glib.models.interfaces.IShape.BorderPos;
import net.sf.latexdraw.glib.models.interfaces.IShape.LineStyle;
import net.sf.latexdraw.lang.LangTool;
import net.sf.latexdraw.ui.LabelListCellRenderer;
import net.sf.latexdraw.util.LResources;
import org.malai.ui.UIComposer;
import org.malai.widget.MButtonIcon;
import org.malai.widget.MColorButton;
import org.malai.widget.MComboBox;
import org.malai.widget.MSpinner;

/**
 * This instrument modifies border properties of shapes or the pencil.<br>
 * <br>
 * This file is part of LaTeXDraw.<br>
 * Copyright (c) 2005-2012 Arnaud BLOUIN<br>
 * <br>
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * <br>
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br>
 * <br>
 * 10/31/2010<br>
 * @author Arnaud BLOUIN
 * @since 3.0
 */
public class ShapeBorderCustomiser extends ShapePropertyCustomiser {

    /** The field which allows to change shapes thickness. */
    protected MSpinner thicknessField;

    /** Allows to set the colour of the borders of shapes. */
    protected MColorButton lineColButton;

    /** Allows to change the style of the borders */
    protected MComboBox lineCB;

    /** Allows to select the position of the borders of the shape. */
    protected MComboBox bordersPosCB;

    /** Allows to change the angle of the round corner. */
    protected MSpinner frameArcField;

    /**
	 * Creates the instrument.
	 * @param hand The Hand instrument.
	 * @param composer The composer that manages the widgets of the instrument.
	 * @param pencil The Pencil instrument.
	 * @throws IllegalArgumentException If one of the given argument is null or if the drawing cannot
	 * be accessed from the hand.
	 * @since 3.0
	 */
    public ShapeBorderCustomiser(final UIComposer<?> composer, final Hand hand, final Pencil pencil) {
        super(composer, hand, pencil);
        initialiseWidgets();
    }

    /**
	 * Creates a list of the different positions of the borders.
	 * @return The created list.
	 */
    public static MComboBox createBordersPositionChoice() {
        MComboBox dbPositionChoice = new MComboBox();
        dbPositionChoice.setRenderer(new LabelListCellRenderer());
        JLabel label = new JLabel(BorderPos.INTO.toString());
        label.setIcon(LResources.INNER_ICON);
        dbPositionChoice.addItem(label);
        label = new JLabel(BorderPos.OUT.toString());
        label.setIcon(LResources.OUTER_ICON);
        dbPositionChoice.addItem(label);
        label = new JLabel(BorderPos.MID.toString());
        label.setIcon(LResources.MIDDLE_ICON);
        dbPositionChoice.addItem(label);
        return dbPositionChoice;
    }

    /**
	 * Creates a list of the different styles of line.
	 * @return The created list.
	 */
    public static MComboBox createStyleLineChoice() {
        MComboBox lineChoice = new MComboBox();
        lineChoice.setRenderer(new LabelListCellRenderer());
        JLabel label = new JLabel(LineStyle.SOLID.toString());
        label.setIcon(LResources.LINE_STYLE_NONE_ICON);
        lineChoice.addItem(label);
        label = new JLabel(LineStyle.DASHED.toString());
        label.setIcon(LResources.LINE_STYLE_DASHED_ICON);
        lineChoice.addItem(label);
        label = new JLabel(LineStyle.DOTTED.toString());
        label.setIcon(LResources.LINE_STYLE_DOTTED_ICON);
        lineChoice.addItem(label);
        return lineChoice;
    }

    @Override
    protected void initialiseWidgets() {
        thicknessField = new MSpinner(new MSpinner.MSpinnerNumberModel(2., 0.1, 1000., 0.1), new JLabel(LResources.THICKNESS_ICON));
        thicknessField.setEditor(new JSpinner.NumberEditor(thicknessField, "0.0"));
        thicknessField.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.65"));
        lineColButton = new MColorButton("Colour", new MButtonIcon(Color.BLACK));
        lineColButton.setMargin(LResources.INSET_BUTTON);
        lineColButton.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.66"));
        lineCB = createStyleLineChoice();
        lineCB.setPreferredSize(new Dimension(70, 30));
        lineCB.setMaximumSize(new Dimension(70, 30));
        bordersPosCB = createBordersPositionChoice();
        bordersPosCB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.77"));
        bordersPosCB.setPreferredSize(new Dimension(45, 30));
        bordersPosCB.setMaximumSize(new Dimension(45, 30));
        frameArcField = new MSpinner(new MSpinner.MSpinnerNumberModel(0., 0., 1., 0.05), new JLabel(LResources.ROUNDNESS_ICON));
        frameArcField.setEditor(new JSpinner.NumberEditor(frameArcField, "0.00"));
    }

    @Override
    protected void update(final IShape shape) {
        if (shape == null) setActivated(false); else {
            final boolean isTh = shape.isThicknessable();
            final boolean isStylable = shape.isLineStylable();
            final boolean isMvble = shape.isBordersMovable();
            final boolean isColor = shape.isColourable();
            final boolean supportRound = shape instanceof IGroup ? ((IGroup) shape).containsRoundables() : shape instanceof ILineArcShape;
            composer.setWidgetVisible(thicknessField, isTh);
            composer.setWidgetVisible(lineCB, isStylable);
            composer.setWidgetVisible(bordersPosCB, isMvble);
            composer.setWidgetVisible(frameArcField, supportRound);
            composer.setWidgetVisible(lineColButton, isColor);
            if (isColor) lineColButton.setColor(shape.getLineColour());
            if (isTh) thicknessField.setValueSafely(shape.getThickness());
            if (isStylable) lineCB.setSelectedItemSafely(shape.getLineStyle().toString());
            if (isMvble) bordersPosCB.setSelectedItemSafely(shape.getBordersPosition().toString());
            if (supportRound) frameArcField.setValueSafely(((ILineArcShape) shape).getLineArc());
        }
    }

    @Override
    protected void setWidgetsVisible(final boolean visible) {
        composer.setWidgetVisible(bordersPosCB, activated);
        composer.setWidgetVisible(thicknessField, activated);
        composer.setWidgetVisible(lineColButton, activated);
        composer.setWidgetVisible(lineCB, activated);
        composer.setWidgetVisible(frameArcField, activated);
    }

    /**
	 * @return The line style combo box.
	 * @since 3.0
	 */
    public MComboBox getLineCB() {
        return lineCB;
    }

    /**
	 * @return The border position combo box.
	 * @since 3.0
	 */
    public MComboBox getBordersPosCB() {
        return bordersPosCB;
    }

    /**
	 * @return The line colour button.
	 * @since 3.0
	 */
    public MColorButton getLineColButton() {
        return lineColButton;
    }

    /**
	 * @return The field that allows to modify the thickness of the pencil.
	 * @since 3.0
	 */
    public MSpinner getThicknessField() {
        return thicknessField;
    }

    /**
	 * @return The field that defines the roundness of the corners.
	 * @since 3.0
	 */
    public MSpinner getFrameArcField() {
        return frameArcField;
    }

    @Override
    protected void initialiseLinks() {
        try {
            addLink(new Spinner2PencilBorder(this));
            addLink(new List2PencilBorder(this));
            addLink(new List2SelectionBorder(this));
            addLink(new Spinner2SelectionBorder(this));
            addLink(new ColourButton2PencilBorder(this));
            addLink(new ColourButton2SelectionBorder(this));
        } catch (InstantiationException e) {
            BadaboomCollector.INSTANCE.add(e);
        } catch (IllegalAccessException e) {
            BadaboomCollector.INSTANCE.add(e);
        }
    }
}

/**
 * This link maps a list to an action that modifies shapes properties.
 */
class List2SelectionBorder extends ListForCustomiser<ModifyShapeProperty, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param instrument The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected List2SelectionBorder(final ShapeBorderCustomiser instrument) throws InstantiationException, IllegalAccessException {
        super(instrument, ModifyShapeProperty.class);
    }

    @Override
    public boolean isConditionRespected() {
        final ItemSelectable is = interaction.getList();
        return (is == instrument.bordersPosCB || is == instrument.lineCB) && instrument.hand.isActivated();
    }

    @Override
    public void initAction() {
        final ItemSelectable is = interaction.getList();
        action.setGroup(instrument.pencil.drawing.getSelection().duplicate());
        if (is == instrument.bordersPosCB) {
            action.setProperty(ShapeProperties.BORDER_POS);
            action.setValue(BorderPos.getStyle(getLabelText()));
        } else {
            action.setProperty(ShapeProperties.LINE_STYLE);
            action.setValue(LineStyle.getStyle(getLabelText()));
        }
    }
}

/**
 * This link maps a list to a ModifyPencil action.
 */
class List2PencilBorder extends ListForCustomiser<ModifyPencilParameter, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param instrument The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected List2PencilBorder(final ShapeBorderCustomiser instrument) throws InstantiationException, IllegalAccessException {
        super(instrument, ModifyPencilParameter.class);
    }

    @Override
    public void initAction() {
        final ItemSelectable is = interaction.getList();
        action.setPencil(instrument.pencil);
        if (is == instrument.bordersPosCB) {
            action.setProperty(ShapeProperties.BORDER_POS);
            action.setValue(BorderPos.getStyle(getLabelText()));
        } else {
            action.setProperty(ShapeProperties.LINE_STYLE);
            action.setValue(LineStyle.getStyle(getLabelText()));
        }
    }

    @Override
    public boolean isConditionRespected() {
        final ItemSelectable is = interaction.getList();
        return (is == instrument.bordersPosCB || is == instrument.lineCB) && instrument.pencil.isActivated();
    }
}

/**
 * This link maps a spinner to a ModifyPencil action.
 */
class Spinner2SelectionBorder extends SpinnerForCustomiser<ModifyShapeProperty, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param borderCustom The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected Spinner2SelectionBorder(final ShapeBorderCustomiser borderCustom) throws InstantiationException, IllegalAccessException {
        super(borderCustom, ModifyShapeProperty.class);
    }

    @Override
    public void initAction() {
        if (interaction.getSpinner() == instrument.thicknessField) action.setProperty(ShapeProperties.LINE_THICKNESS); else action.setProperty(ShapeProperties.ROUND_CORNER_VALUE);
        action.setGroup(instrument.pencil.drawing.getSelection().duplicate());
    }

    @Override
    public boolean isConditionRespected() {
        final JSpinner spinner = interaction.getSpinner();
        return (spinner == instrument.thicknessField || spinner == instrument.frameArcField) && instrument.hand.isActivated();
    }
}

/**
 * This link maps a spinner to a ModifyPencil action.
 */
class Spinner2PencilBorder extends SpinnerForCustomiser<ModifyPencilParameter, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param borderCustom The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected Spinner2PencilBorder(final ShapeBorderCustomiser borderCustom) throws InstantiationException, IllegalAccessException {
        super(borderCustom, ModifyPencilParameter.class);
    }

    @Override
    public void initAction() {
        if (interaction.getSpinner() == instrument.thicknessField) action.setProperty(ShapeProperties.LINE_THICKNESS); else action.setProperty(ShapeProperties.ROUND_CORNER_VALUE);
        action.setPencil(instrument.pencil);
    }

    @Override
    public boolean isConditionRespected() {
        final JSpinner spinner = interaction.getSpinner();
        return (spinner == instrument.thicknessField || spinner == instrument.frameArcField) && instrument.pencil.isActivated();
    }
}

/**
 * This link maps a colour button to the pencil.
 */
class ColourButton2PencilBorder extends ColourButtonForCustomiser<ModifyPencilParameter, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param instrument The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected ColourButton2PencilBorder(final ShapeBorderCustomiser instrument) throws InstantiationException, IllegalAccessException {
        super(instrument, ModifyPencilParameter.class);
    }

    @Override
    public void initAction() {
        super.initAction();
        action.setProperty(ShapeProperties.COLOUR_LINE);
        action.setPencil(instrument.pencil);
    }

    @Override
    public boolean isConditionRespected() {
        return interaction.getButton() == instrument.lineColButton && instrument.pencil.isActivated();
    }
}

/**
 * This link maps a colour button to the selected shapes.
 */
class ColourButton2SelectionBorder extends ColourButtonForCustomiser<ModifyShapeProperty, ShapeBorderCustomiser> {

    /**
	 * Creates the link.
	 * @param instrument The instrument that contains the link.
	 * @throws InstantiationException If an error of instantiation (interaction, action) occurs.
	 * @throws IllegalAccessException If no free-parameter constructor are provided.
	 */
    protected ColourButton2SelectionBorder(final ShapeBorderCustomiser instrument) throws InstantiationException, IllegalAccessException {
        super(instrument, ModifyShapeProperty.class);
    }

    @Override
    public void initAction() {
        super.initAction();
        action.setProperty(ShapeProperties.COLOUR_LINE);
        action.setGroup(instrument.pencil.drawing.getSelection().duplicate());
    }

    @Override
    public boolean isConditionRespected() {
        return interaction.getButton() == instrument.lineColButton && instrument.hand.isActivated();
    }
}
