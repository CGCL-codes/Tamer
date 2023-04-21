package org.parallelj.designer.extension.edit.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.RGB;
import org.parallelj.designer.edit.parts.ConditionEditPart;
import org.parallelj.designer.extension.tools.BoundsRefreshment;
import org.parallelj.designer.extension.tools.Drawer;

public class ConditionExtendedEditPart extends ConditionEditPart {

    /**
	 * Default main color of this object
	 */
    private final RGB preferredColor = new RGB(200, 200, 200);

    /**
	 * Actual colors of this object
	 */
    private RGB startColor;

    private RGB endColor;

    private RGB lightColor;

    public ConditionExtendedEditPart(View view) {
        super(view);
        updateColor(preferredColor, false);
    }

    /**
	 * Fixing the width and height of the Condition node and updating diagram
	 * with last saved color when diagram closed and opened.
	 */
    @Override
    protected void refreshBounds() {
        BoundsRefreshment.refreshBounds(this, 52, 52);
        this.updateColor(Drawer.getSavedRGB(this), false);
    }

    /**
	 * Creates circle for condition node, with color gradient starting from
	 * top-left to right-bottom, also having light and shadow effect.
	 */
    @Override
    protected IFigure createNodeShape() {
        IFigure figure = new ConditionFigure() {

            public void paintFigure(Graphics graphics) {
                Drawer.circleGradient(graphics, getBounds(), startColor, endColor, lightColor);
            }
        };
        return primaryShape = figure;
    }

    /**
	 * default main color, this value will be taken on reset of color.
	 */
    @Override
    public Object getPreferredValue(EStructuralFeature feature) {
        if (feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()) {
            return FigureUtilities.RGBToInteger(preferredColor);
        } else {
            return super.getPreferredValue(feature);
        }
    }

    /**
	 * Handle the event for fill color change.
	 */
    @Override
    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);
        Object feature = notification.getFeature();
        if (NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {
            int newIntColor = ((Integer) notification.getNewValue()).intValue();
            RGB newRGBColor = FigureUtilities.integerToRGB(newIntColor);
            this.updateColor(newRGBColor, true);
        } else if (notification.getEventType() == Notification.ADD) {
            Drawer.drawWithDefault(notification, this);
        }
    }

    /**
	 * Update the main color and respective gradient for node.
	 * 
	 * @param rgb
	 *            new color
	 */
    public void updateColor(RGB rgb, boolean isPaint) {
        startColor = rgb;
        endColor = Drawer.darken(40, startColor);
        lightColor = Drawer.lighten(40, startColor);
        if (isPaint) {
            this.getFigure().repaint();
        }
    }
}
