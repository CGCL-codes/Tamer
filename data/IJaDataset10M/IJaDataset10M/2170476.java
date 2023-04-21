package org.proteinshader.gui.components.controlpanel;

import org.proteinshader.gui.*;
import org.proteinshader.gui.enums.*;
import org.proteinshader.gui.listeners.controlpanel.*;
import org.proteinshader.gui.utils.*;
import org.proteinshader.structure.enums.*;
import org.proteinshader.structure.exceptions.*;
import org.proteinshader.structure.visitor.exceptions.*;
import org.proteinshader.structure.visitor.modifiers.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/*******************************************************************************
This control panel allows the user to modify the color of Segments.
*******************************************************************************/
public class CartoonColorPanel extends ColorPanel {

    /** The menu name "Color" will be returned by toString(). */
    public static final String MENU_NAME = "Cartoon Color";

    private SegmentModifier m_segmentModifier;

    /***************************************************************************
    Constructs a CartoonColorPanel.

    @param mediator     the centralized Mediator that most listeners
                        need to call on to accomplish their task.
    @param dialogOwner  the owner of any Dialogs opened from the
                        SelectorPanel (or null if there is no
                        requested owner).
    @param radioPanel   the common RadioPanel used by subpanels.
    ***************************************************************************/
    public CartoonColorPanel(Mediator mediator, Frame dialogOwner, RadioPanel radioPanel) {
        super(mediator, dialogOwner, radioPanel, "Region Type", "Amino Acid");
        m_segmentModifier = new SegmentModifier();
    }

    /***************************************************************************
    Prepares the SegmentModifier to modify the RGB color of Segments,
    and then calls on the modifySelected() method of the RadioPanel.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void modifySelected(float red, float green, float blue) throws ColorOutOfRangeException {
        setColor(red, green, blue);
        modifySelected(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Prepares the the SegmentModifier to modify the RGB color of
    Segments, and then calls on the modifyHelices() method of the
    RadioPanel.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void modifyHelices(float red, float green, float blue) throws ColorOutOfRangeException {
        setColor(red, green, blue);
        modifyHelices(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Prepares the SegmentModifier to modify the RGB color of Segments,
    and then calls on the modifyStrands() method of the RadioPanel.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void modifyStrands(float red, float green, float blue) throws ColorOutOfRangeException {
        setColor(red, green, blue);
        modifyStrands(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Prepares the SegmentModifier to modify the RGB color of Segments,
    and then calls on the modifyLoops() method of the RadioPanel.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void modifyLoops(float red, float green, float blue) throws ColorOutOfRangeException {
        setColor(red, green, blue);
        modifyLoops(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Prepares the SegmentModifier to modify the RGB color of Segments,
    and then calls on the modifyGlobal() method of the RadioPanel.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void modifyGlobal(float red, float green, float blue) throws ColorOutOfRangeException {
        setColor(red, green, blue);
        modifyGlobal(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 1 sets the RGB color of Segments based on Region type.

    <br/><br/>
    The SegmentModifier is passed to the modifySelected() method
    of the ColorPanel superclass, which then calls on the
    modifySelected() method of the RadioPanel.
    ***************************************************************************/
    public void modifySelectedToDefault1() {
        setRGBToRegionColor();
        modifySelected(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 1 sets the RGB color of Segments based on Region type.

    <br/><br/>
    The SegmentModifier is passed to the modifyHelices() method of the
    ColorPanel superclass, which then calls on the modifyHelices()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyHelicesToDefault1() {
        setRGBToRegionColor();
        modifyHelices(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 1 sets the RGB color of Segments based on Region type.

    <br/><br/>
    The SegmentModifier is passed to the modifyStrands() method of the
    ColorPanel superclass, which then calls on the modifyStrands()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyStrandsToDefault1() {
        setRGBToRegionColor();
        modifyStrands(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 1 sets the RGB color of Segments based on Region type.

    <br/><br/>
    The SegmentModifier is passed to the modifyLoops() method of the
    ColorPanel superclass, which then calls on the modifyLoops()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyLoopsToDefault1() {
        setRGBToRegionColor();
        modifyLoops(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 1 sets the RGB color of Segments based on Region type.

    <br/><br/>
    The SegmentModifier is passed to the modifyGlobal() method of the
    ColorPanel superclass, which then calls on the modifyGlobal()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyGlobalToDefault1() {
        setRGBToRegionColor();
        modifyGlobal(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 2 sets the RGB color of Segments based on AminoAcid type.

    <br/><br/>
    The SegmentModifier is passed to the modifySelected() method of
    the ColorPanel superclass, which then calls on the
    modifySelected() method of the RadioPanel.
    ***************************************************************************/
    public void modifySelectedToDefault2() {
        setRGBToAminoAcidDefault();
        modifySelected(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 2 sets the RGB color of Segments based on AminoAcid type.

    <br/><br/>
    The SegmentModifier is passed to the modifyHelices() method of the
    ColorPanel superclass, which then calls on the modifyHelices()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyHelicesToDefault2() {
        setRGBToAminoAcidDefault();
        modifyHelices(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 2 sets the RGB color of Segments based on AminoAcid type.

    <br/><br/>
    The SegmentModifier is passed to the modifyStrands() method of the
    ColorPanel superclass, which then calls on the modifyStrands()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyStrandsToDefault2() {
        setRGBToAminoAcidDefault();
        modifyStrands(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 2 sets the RGB color of Segments based on AminoAcid type.

    <br/><br/>
    The SegmentModifier is passed to the modifyLoops() method of the
    ColorPanel superclass, which then calls on the modifyLoops()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyLoopsToDefault2() {
        setRGBToAminoAcidDefault();
        modifyLoops(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Default 2 sets the RGB color of Segments based on AminoAcid type.

    <br/><br/>
    The SegmentModifier is passed to the modifyGlobal() method of the
    ColorPanel superclass, which then calls on the modifyGlobal()
    method of the RadioPanel.
    ***************************************************************************/
    public void modifyGlobalToDefault2() {
        setRGBToAminoAcidDefault();
        modifyGlobal(null, null, m_segmentModifier);
    }

    /***************************************************************************
    Returns a name suitable for use in a menu.

    @return The menu name as a String.
    ***************************************************************************/
    public String toString() {
        return MENU_NAME;
    }

    /***************************************************************************
    Clears the SegmentModifier and sets the RGB colors.

    @param red    the red component of the RGB color to set.
    @param green  the green component of the RGB color to set.
    @param blue   the blue component of the RGB color to set.
    @throws ColorOutOfRangeException  if a color is less than 0.0 or
                                      greater than 1.0.
    ***************************************************************************/
    public void setColor(float red, float green, float blue) throws ColorOutOfRangeException {
        m_segmentModifier.clear();
        m_segmentModifier.setColor(red, green, blue);
    }

    /***************************************************************************
    Clears the SegmentModifier and sets the RGB color to the Region
    color.
    ***************************************************************************/
    private void setRGBToRegionColor() {
        m_segmentModifier.clear();
        m_segmentModifier.setToRegionColor();
    }

    /***************************************************************************
    Clears the SegmentModifier and sets the default RGB colors based
    on AminoAcid type.
    ***************************************************************************/
    private void setRGBToAminoAcidDefault() {
        m_segmentModifier.clear();
        m_segmentModifier.setToAminoAcidColor();
    }
}
