package edu.udo.scaffoldhunter.view.scaffoldtree;

import java.awt.geom.Point2D;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * Label used to add some text to a scaffold.
 * 
 * @author Henning Garus
 *
 */
public class ScaffoldLabel extends UnscaledNode {

    private final PText text;

    /**
     * Creates a new scaffold label without any text.
     * 
     * @param node vnode this label is attached to
     */
    public ScaffoldLabel(VNode node) {
        this(node, "");
    }

    /**
    * Creates a new scaffold label with text
    * 
    * @param node vnode this label is attached to
    * @param text text which will be inititally displayed by sthis label
    */
    public ScaffoldLabel(VNode node, String text) {
        super(node, new Point2D.Double(node.getBoundsReference().getCenterX(), node.getBoundsReference().getMaxY() + 15));
        this.text = new PText(text);
        this.setBounds(this.text.getBoundsReference());
        this.addChild(this.text);
        this.text.centerFullBoundsOnPoint(getBoundsReference().getCenterX(), getBoundsReference().getCenterY());
        this.text.setPickable(false);
        this.setPickable(false);
    }

    /**
     * @param text the text displayed by this label
     */
    public void setText(String text) {
        this.text.setText(text);
        this.setBounds(this.text.getBoundsReference());
        this.text.centerFullBoundsOnPoint(getBoundsReference().getCenterX(), getBoundsReference().getCenterY());
    }

    @Override
    public void fullPaint(PPaintContext paintContext) {
        if (SemanticZoomLevel.MEDIUM.scaleIsBelowThreshold(paintContext.getCamera().getViewScale())) return;
        super.fullPaint(paintContext);
    }
}
