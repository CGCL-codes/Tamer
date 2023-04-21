package org.wilmascope.areachart3d;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import java.awt.*;
import javax.vecmath.*;
import java.util.*;

/**Class ZLabel creates labels for the calendar
 */
public class ZLabel extends TransformGroup {

    private float deltaX;

    private Color3f color = new Color3f(1, 1, 1);

    /**@param calendar Vector containing the dates
	 * @param length The length of the chart
	 * @param width The width of the chart
	 */
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public ZLabel(Vector calendar, float length, float width) {
        deltaX = width / (calendar.size() - 1);
        float x = 0;
        int step = 1;
        for (int i = 0; i < calendar.size(); i += step) {
            TransformGroup TG1 = new TransformGroup();
            TransformGroup TG2 = new TransformGroup();
            Transform3D rot1 = new Transform3D();
            Transform3D translate = new Transform3D();
            Transform3D rot2 = new Transform3D();
            Transform3D rot3 = new Transform3D();
            rot1.rotX(-Math.PI / 2);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Font font = new java.awt.Font("Dummy", Font.PLAIN, 20);
            FontMetrics metrics = toolkit.getFontMetrics(font);
            float fontLength = metrics.stringWidth((String) calendar.get(i)) * 1f / 256f;
            float fontWidth = metrics.getHeight() * 1f / 256f;
            translate.setTranslation(new Vector3f(-fontLength + 0.05f, 0, 0));
            rot2.rotY(Math.PI / 2);
            rot3.rotX(Math.PI / 4);
            translate.mul(translate, rot1);
            rot2.mul(rot2, translate);
            rot3.mul(rot3, rot2);
            rot3.setScale(1f / 3f);
            TG1.setTransform(rot3);
            Transform3D trans = new Transform3D();
            trans.setTranslation(new Vector3f(x + fontWidth / 4, 0, 0));
            TG2.setTransform(trans);
            TG2.addChild(TG1);
            TG1.addChild(new Text2D((String) calendar.get(i), color, "Dummy", 36, Font.BOLD));
            this.addChild(TG2);
            while (fontWidth > deltaX) {
                deltaX += deltaX;
                step++;
            }
            x += deltaX;
        }
        x = 0;
        for (int i = 0; i < calendar.size(); i += step) {
            TransformGroup TG1 = new TransformGroup();
            TransformGroup TG2 = new TransformGroup();
            Transform3D rot1 = new Transform3D();
            Transform3D translate = new Transform3D();
            Transform3D rot2 = new Transform3D();
            Transform3D rot3 = new Transform3D();
            rot1.rotY(Math.PI);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Font font = new java.awt.Font("Dummy", Font.PLAIN, 20);
            FontMetrics metrics = toolkit.getFontMetrics(font);
            float fontLength = metrics.stringWidth((String) calendar.get(i)) * 1f / 256f;
            float fontWidth = metrics.getHeight() * 1f / 256f;
            translate.setTranslation(new Vector3f(fontLength - 0.05f, 0, 0));
            rot2.rotZ(-Math.PI / 2);
            rot3.rotX(Math.PI / 4);
            translate.mul(translate, rot1);
            rot2.mul(rot2, translate);
            rot3.mul(rot3, rot2);
            rot3.setScale(1f / 3f);
            TG1.setTransform(rot3);
            Transform3D trans = new Transform3D();
            trans.setTranslation(new Vector3f(x - fontWidth / 4, 0, -length));
            TG2.setTransform(trans);
            TG2.addChild(TG1);
            TG1.addChild(new Text2D((String) calendar.get(i), color, "Dummy", 36, Font.BOLD));
            this.addChild(TG2);
            x += deltaX;
        }
    }
}
