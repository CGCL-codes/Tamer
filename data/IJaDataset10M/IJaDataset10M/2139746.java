package genestudio.GML.Graphics;

import genestudio.GML.*;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class GeneGraphics {

    private GML gmlInfo;

    private JPanel jpnArea;

    public GMLMap gmmMolMap;

    public GeneGraphics(GML gml, JPanel area) {
        gmlInfo = gml;
        jpnArea = area;
        if (gmlInfo.isLinear()) gmmMolMap = new LinearMap(gmlInfo, jpnArea); else gmmMolMap = new CircularMap(gmlInfo, jpnArea);
    }

    public void adjustPanel() {
        Dimension dmArea = gmmMolMap.getSize();
        jpnArea.setSize(dmArea);
        jpnArea.setPreferredSize(dmArea);
        jpnArea.setVisible(false);
        jpnArea.setVisible(true);
    }

    public void draw() {
        gmmMolMap.PaintMap();
    }
}
