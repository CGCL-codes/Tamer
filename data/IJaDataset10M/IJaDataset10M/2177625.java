package wtkx;

import wtkx.io.CC;
import wtkx.ui.PathExpr;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * A path element like SVG.
 * 
 * @author jdp
 */
public class Path extends Component {

    protected volatile PathExpr expr = new PathExpr();

    protected volatile Paint fill, stroke;

    protected volatile Stroke strokeOp = DefaultStroke;

    protected volatile float strokeWidth = DefaultStroke.getLineWidth();

    public Path() {
        super();
        this.floating = true;
        this.auto = false;
    }

    public PathExpr getD() {
        return this.expr;
    }

    public void setD(String expr) {
        this.setD(new PathExpr(expr));
    }

    public void setD(PathExpr expr) {
        this.expr = expr;
    }

    public Paint getFill() {
        return this.fill;
    }

    public void setFill(Paint color) {
        this.fill = color;
    }

    public void setFill(String color) {
        this.fill = CC.Decode(color);
    }

    public Paint getStroke() {
        return this.stroke;
    }

    public void setStroke(Paint color) {
        this.stroke = color;
    }

    public void setStroke(String color) {
        this.stroke = CC.Decode(color);
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float w) {
        this.strokeWidth = w;
        this.strokeOp = new BasicStroke(w);
    }

    public void paint(Graphics2D g) {
        PathExpr expr = this.expr;
        if (null != expr) {
            Path2D.Float strokeShape = expr.getShape();
            if (null != strokeShape) {
                Paint fill = this.fill;
                if (null != fill) {
                    g.setPaint(fill);
                    g.fill(strokeShape);
                }
                Paint stroke = this.stroke;
                if (null != stroke) {
                    g.setPaint(stroke);
                    g.setStroke(this.strokeOp);
                    g.draw(strokeShape);
                }
            }
        }
    }

    public void layout() {
        PathExpr expr = this.expr;
        if (null != expr) {
            Path2D.Float strokeShape = expr.getShape();
            if (null != strokeShape) {
                Rectangle2D.Float bounds = (Rectangle2D.Float) strokeShape.getBounds2D();
                this.x = 0f;
                this.y = 0f;
                this.width = (bounds.x + bounds.width);
                this.height = (bounds.y + bounds.height);
            }
        }
    }
}
