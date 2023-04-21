package apollo.gui.drawable;

import apollo.datamodel.*;
import apollo.gui.genomemap.FeatureTierManager;
import apollo.gui.Transformer;
import apollo.gui.schemes.FeatureProperty;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A glyph that draws a solid bar with a triangle at the top or bottom.
 * This might be useful for showing transposon insertions, for example.
 */
public class DrawableInsertFeatureSet extends DrawableResultFeatureSet {

    /**
   * constructs a glyph with the triangle at the top
   * pointing in the direction of the insertion.
   * when selected a 1 pixel line is draw down to the insertion site
   * the triangle is filled with gray and outlined in black,
   * and the bar is black.
   */
    public DrawableInsertFeatureSet() {
        super();
    }

    public DrawableInsertFeatureSet(FeatureSetI feature) {
        super(feature);
    }

    public void drawSelected(Graphics g, Rectangle boxBounds, Transformer transformer, FeatureTierManager manager) {
    }

    public void drawUnselected(Graphics g, Rectangle boxBounds, Transformer transformer, FeatureTierManager manager) {
    }
}
