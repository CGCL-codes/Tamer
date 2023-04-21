package com.gempukku.animator.widget;

import com.gempukku.animator.DisplayInfo;
import com.gempukku.animator.function.alpha.easing.Easing;
import com.gempukku.animator.function.time.SimpleTimeFunction;
import com.gempukku.animator.utils.DrawUtilities;
import com.gempukku.animator.variable.DoubleVariable;
import com.gempukku.animator.variable.PaintVariable;
import com.gempukku.animator.variable.interpolator.ColorInterpolator;
import com.gempukku.animator.variable.interpolator.DoubleInterpolator;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class PopupButtonWidget extends AbstractButtonWidget {

    private static final int HOVER_TIME_LENGTH = 400;

    private static final int PRESS_TIME_LENGTH = 200;

    private static final double MAX_SCALE = 1.13d;

    public static final int GRADIENT_WIDTH = 10;

    private Rectangle _bounds;

    private Color _background = Color.GRAY;

    private Color _foreground = Color.WHITE;

    private Color _pressedColor = new Color(0.3f, 0.5f, 1f);

    private Font _font = new Font("Arial", Font.PLAIN, 12);

    private String _label;

    private DoubleVariable _doubleVariable = new DoubleInterpolator(1d, 1d * MAX_SCALE, new SimpleTimeFunction(0, HOVER_TIME_LENGTH), Easing.easing(Easing.TYPE_BOUNCE, Easing.DIRECTION_OUT));

    private PaintVariable _gradientPaintVariable = new ColorInterpolator(_background, _pressedColor, new SimpleTimeFunction(HOVER_TIME_LENGTH, PRESS_TIME_LENGTH));

    public PopupButtonWidget(Rectangle bounds, String label) {
        super(new SimpleButtonTimelineFunction(HOVER_TIME_LENGTH, PRESS_TIME_LENGTH));
        _bounds = bounds;
        _label = label;
    }

    private double getScale(long time) {
        return _doubleVariable.getDouble(time);
    }

    @Override
    public Rectangle getBounds(DisplayInfo displayInfo, long time) {
        return _bounds;
    }

    @Override
    public Object getState(DisplayInfo displayInfo, long time) {
        return time;
    }

    @Override
    protected Shape getShape(long time) {
        double scale = getScale(time);
        double middleX = _bounds.x + (_bounds.width / 2d);
        double middleY = _bounds.y + (_bounds.height / 2d);
        return new Rectangle2D.Double(middleX - scale * _bounds.width / 2d, middleY - scale * _bounds.height / 2d, scale * _bounds.width, scale * _bounds.height);
    }

    @Override
    protected void paintButton(Graphics2D gr, DisplayInfo displayInfo, long time, AnimatedCallback callback) {
        AffineTransform baseTransform = gr.getTransform();
        double middleX = _bounds.x + (_bounds.width / 2d);
        double middleY = _bounds.y + (_bounds.height / 2d);
        gr.translate(middleX, middleY);
        double scale = getScale(time);
        gr.scale(scale, scale);
        gr.translate(-middleX, -middleY);
        realPaintButton(gr, time);
        gr.setTransform(baseTransform);
    }

    private Paint getPressPaint(long time) {
        return _gradientPaintVariable.getPaint(time);
    }

    private void realPaintButton(Graphics2D gr, long time) {
        int lowestDim = Math.min(_bounds.width, _bounds.height);
        Color gradientColor = (Color) getPressPaint(time);
        gr.setPaint(new GradientPaint(_bounds.x, _bounds.y, new Color(gradientColor.getRed(), gradientColor.getGreen(), gradientColor.getBlue(), 0), _bounds.x + GRADIENT_WIDTH, _bounds.y, gradientColor));
        Polygon leftPart = new Polygon();
        leftPart.addPoint(_bounds.x + 0, _bounds.y + 0);
        leftPart.addPoint(_bounds.x + 0, _bounds.y + _bounds.height);
        leftPart.addPoint(_bounds.x + lowestDim / 2, _bounds.y + _bounds.height - (lowestDim / 2));
        leftPart.addPoint(_bounds.x + lowestDim / 2, _bounds.y + lowestDim / 2);
        gr.fill(leftPart);
        gr.setPaint(new GradientPaint(_bounds.x, _bounds.y, new Color(gradientColor.getRed(), gradientColor.getGreen(), gradientColor.getBlue(), 0), _bounds.x, _bounds.y + GRADIENT_WIDTH, gradientColor));
        Polygon upperPart = new Polygon();
        upperPart.addPoint(_bounds.x + 0, _bounds.y + 0);
        upperPart.addPoint(_bounds.x + lowestDim / 2, _bounds.y + lowestDim / 2);
        upperPart.addPoint(_bounds.x + _bounds.width - lowestDim / 2, _bounds.y + lowestDim / 2);
        upperPart.addPoint(_bounds.x + _bounds.width, _bounds.y + 0);
        gr.fill(upperPart);
        gr.setPaint(new GradientPaint(_bounds.x + _bounds.width, _bounds.y, new Color(gradientColor.getRed(), gradientColor.getGreen(), gradientColor.getBlue(), 0), _bounds.x + _bounds.width - GRADIENT_WIDTH, _bounds.y, gradientColor));
        Polygon rightPart = new Polygon();
        rightPart.addPoint(_bounds.x + _bounds.width, _bounds.y + 0);
        rightPart.addPoint(_bounds.x + _bounds.width - lowestDim / 2, _bounds.y + lowestDim / 2);
        rightPart.addPoint(_bounds.x + _bounds.width - lowestDim / 2, _bounds.y + _bounds.height - lowestDim / 2);
        rightPart.addPoint(_bounds.x + _bounds.width, _bounds.y + _bounds.height);
        gr.fill(rightPart);
        gr.setPaint(new GradientPaint(_bounds.x, _bounds.y + _bounds.height, new Color(gradientColor.getRed(), gradientColor.getGreen(), gradientColor.getBlue(), 0), _bounds.x, _bounds.y + _bounds.height - GRADIENT_WIDTH, gradientColor));
        Polygon lowerPart = new Polygon();
        lowerPart.addPoint(_bounds.x + _bounds.width, _bounds.y + _bounds.height);
        lowerPart.addPoint(_bounds.x + _bounds.width - lowestDim / 2, _bounds.y + _bounds.height - lowestDim / 2);
        lowerPart.addPoint(_bounds.x + lowestDim / 2, _bounds.y + _bounds.height - lowestDim / 2);
        lowerPart.addPoint(_bounds.x, _bounds.y + _bounds.height);
        gr.fill(lowerPart);
        gr.setStroke(new BasicStroke(1));
        gr.setPaint(gradientColor);
        gr.draw(_bounds);
        gr.setPaint(_foreground);
        DrawUtilities.drawLabel(gr, _bounds, _label, _font, _foreground, SwingConstants.CENTER, SwingConstants.CENTER);
    }
}
