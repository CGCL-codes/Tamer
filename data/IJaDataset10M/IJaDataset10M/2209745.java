package temp;

import java.awt.Color;
import javax.swing.JFrame;
import de.javacus.grafmach.twoD.GrafTestCase;
import de.javacus.grafmach.twoD.IACircle;
import de.javacus.grafmach.twoD.ICACircle;
import de.javacus.grafmach.twoD.ICircle;
import de.javacus.grafmach.twoD.attr.ACircle;
import de.javacus.grafmach.twoD.chang.CAPaintPanel;
import de.javacus.grafmach.twoD.plain.Circle;

/** 
 * @since 2008
 * @version 2010-02
 * @author Burkhard Loesel, Morgenstund hat Kaffee im Mund
 */
public class CACircleTest extends GrafTestCase {

    public void testTranslate() {
        JFrame frame = new JFrame("testTranslate");
        frame.setLocationByPlatform(true);
        frame.setSize(500, 500);
        CAPaintPanel cAGrafPanel = new CAPaintPanel();
        frame.getContentPane().add(cAGrafPanel);
        ICircle circle = new Circle();
        IACircle aCircle = new ACircle(circle, Color.CYAN, 10.0);
        CACircle cACircle = new CACircle(aCircle);
        cACircle.setGrafNamesVisible(true);
        cAGrafPanel.add(cACircle);
        ICACircle cACircleCloned = (ICACircle) cACircle.newInstance();
        cACircleCloned.setGrafName("cACircleCloned");
        cACircleCloned.translateX(100.0);
        cACircleCloned.translateY(50.0);
        cAGrafPanel.add(cACircle);
        cAGrafPanel.add(cACircleCloned);
        frame.setVisible(true);
        assertEquals(true, ask("a moved blue Circle"));
        pause(1);
    }

    public void testScale() {
        JFrame frame = new JFrame("testScale");
        frame.setLocationByPlatform(true);
        frame.setSize(500, 500);
        CAPaintPanel cAGrafPanel = new CAPaintPanel();
        frame.getContentPane().add(cAGrafPanel);
        ICircle circle = new Circle();
        IACircle aCircle = new ACircle(circle, Color.CYAN, 10.0);
        CACircle cACircle = new CACircle(aCircle);
        cACircle.setGrafNamesVisible(true);
        cAGrafPanel.add(cACircle);
        ICACircle cACircleCloned = (ICACircle) cACircle.newInstance();
        cACircleCloned.setGrafName("cACircleCloned");
        cACircleCloned.scaleX(3.0);
        cACircleCloned.scaleY(2.0);
        cAGrafPanel.add(cACircle);
        cAGrafPanel.add(cACircleCloned);
        frame.setVisible(true);
        pause(1);
    }
}
