package edu.biik.visualizations.attackglyphs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GImage;
import no.geosoft.cc.graphics.GPosition;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GText;
import edu.biik.framework.ColorPalette;
import edu.biik.framework.VisualUnit;
import edu.biik.visualizations.main.graphics.SigClassNode;

public class DosAttackNode extends SigClassNode {

    private GSegment circle_outer = null;

    private GSegment circle_inner = null;

    private GSegment rectangle = null;

    private GSegment[] sectors = new GSegment[6];

    private GSegment bg = null;

    private int xLoc = 0;

    private int yLoc = 0;

    private int radius = 0;

    private String sigClassName = null;

    private GStyle normalStyle = null;

    private GStyle glyphStyle = null;

    private GStyle glyphStyle_2 = null;

    private GStyle highlightedStyle = null;

    private GText gText = null;

    public DosAttackNode(VisualUnit visualUnit, String sigClassName, int xLoc, int yLoc, int radius) {
        super(visualUnit, sigClassName, xLoc, yLoc, radius);
        this.sigClassName = sigClassName;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.radius = radius;
        circle_outer = new GSegment();
        circle_inner = new GSegment();
        rectangle = new GSegment();
        bg = new GSegment();
        normalStyle = new GStyle();
        normalStyle.setForegroundColor(ColorPalette.ATTACK_BG_OUTLINE);
        normalStyle.setBackgroundColor(ColorPalette.ATTACK_BG);
        normalStyle.setLineWidth(1);
        setStyle(normalStyle);
        glyphStyle = new GStyle();
        glyphStyle.setForegroundColor(Color.BLACK);
        glyphStyle.setBackgroundColor(Color.RED);
        glyphStyle.setLineWidth(1);
        circle_outer.setStyle(glyphStyle);
        rectangle.setStyle(glyphStyle);
        glyphStyle_2 = new GStyle();
        glyphStyle_2.setForegroundColor(Color.BLACK);
        glyphStyle_2.setBackgroundColor(Color.WHITE);
        glyphStyle_2.setLineWidth(1);
        circle_inner.setStyle(glyphStyle_2);
        highlightedStyle = new GStyle();
        highlightedStyle.setBackgroundColor(ColorPalette.ATTACK_BG_ACTIVE);
        gText = new GText(sigClassName, GPosition.MIDDLE);
        GStyle textStyle = new GStyle();
        textStyle.setForegroundColor(new Color(100, 100, 150));
        textStyle.setBackgroundColor(null);
        textStyle.setFont(new Font("Arial", Font.PLAIN, 9));
        gText.setStyle(textStyle);
        addSegment(bg);
        for (int i = 0; i < 3; i++) {
            sectors[i] = new GSegment();
            sectors[i].setStyle(glyphStyle_2);
            addSegment(sectors[i]);
        }
        GImage image = new GImage(new File("bin\\images\\dos_glyph.png"), GPosition.MIDDLE);
        circle_outer.setImage(image);
        addSegment(circle_outer);
        addSegment(circle_inner);
        addSegment(rectangle);
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            setStyle(highlightedStyle);
            GImage image = new GImage(new File("src\\images\\dos_glyph_active.png"), GPosition.MIDDLE);
            circle_outer.removeImages();
            circle_outer.setImage(image);
            this.draw();
        } else {
            setStyle(normalStyle);
            GImage image = new GImage(new File("src\\images\\dos_glyph.png"), GPosition.MIDDLE);
            circle_outer.removeImages();
            circle_outer.setImage(image);
            this.draw();
        }
    }

    @Override
    public void draw() {
        int xPos = xLoc + radius - 5;
        int yPos = yLoc + radius - 5;
        circle_outer.setGeometry(Geometry.createCircle(xPos, yPos, 1));
    }

    public Point getLocation() {
        return new Point(xLoc, yLoc);
    }

    public String getSigClassName() {
        return sigClassName;
    }
}
