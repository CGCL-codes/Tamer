package uk.ac.imperial.ma.metric.graphics.classic;

/**
 * An interface for obtaining information about events which have occoured to instances of <CODE>ClickableMathPainterPanel3D</CODE>
 * and its child classes.
 * @author Daniel J. R. May
 * @version 0.1
 */
public interface MathPainterPanelListener3D {

    /**
     * This is the generic method which is called whenever any action has occured inside an instance of <code>ClickableMathPainterPanel3D</code>.
     * @param mppe the <code>MathPainterPanelEvent3D</code> object gives a reference to the instance of <code>ClickableMathPainterPanel3D</code>
     * which has undergone some action, and the data which describes the kind of action that has occoured.
     */
    public void mathPainterPanelAction3D(MathPainterPanelEvent3D mppe);

    /**
     * This method is called whenever a <CODE>MathPainterPanel3D</CODE> has been resized. You should fill it with code to reset your base image (if you want one)
     * and then redraw whatever you had in the <CODE>MathPainterPanel3D</CODE> before it was resized.
     */
    public void mathPainterPanelResized3D();
}
