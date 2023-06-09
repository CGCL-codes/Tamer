package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.util.Vector;
import javax.swing.JComponent;
import megamek.client.ui.Messages;
import megamek.client.ui.swing.GUIPreferences;
import megamek.common.Entity;
import megamek.common.Mech;

/**
 * Very cumbersome class that handles set of polygonal areas and labels for
 * PicMap component to represent single mech unit in MechDisplay
 */
public class MechMapSet implements DisplayMapSet {

    private static final String IMAGE_DIR = "data/images/widgets";

    private static final int REAR_AREA_OFFSET = 7;

    private static final int INT_STRUCTURE_OFFSET = 11;

    private PMSimplePolygonArea[] areas = new PMSimplePolygonArea[19];

    private PMSimpleLabel[] labels = new PMSimpleLabel[20];

    private PMValueLabel[] vLabels = new PMValueLabel[21];

    private PMPicPolygonalArea heatHotArea;

    private Vector<BackGroundDrawer> bgDrawers = new Vector<BackGroundDrawer>();

    private PMAreasGroup content = new PMAreasGroup();

    private JComponent comp;

    private Polygon rightArm = new Polygon(new int[] { 106, 105, 110, 114, 111, 108, 106, 109, 112, 119, 116, 122, 124, 118, 115, 112, 114, 113, 111, 95, 93, 93, 91, 91, 95, 99, 99, 102, 104, 101, 104, 106 }, new int[] { 89, 87, 86, 90, 94, 92, 94, 97, 98, 91, 81, 81, 78, 53, 50, 36, 33, 30, 25, 23, 25, 25, 27, 33, 37, 51, 78, 81, 81, 86, 91, 89 }, 32);

    private Polygon leftArm = new Polygon(new int[] { 18, 19, 14, 10, 13, 16, 18, 15, 12, 5, 8, 2, 0, 6, 9, 12, 10, 11, 13, 29, 31, 31, 33, 33, 29, 25, 25, 22, 20, 23, 20, 18 }, new int[] { 89, 87, 86, 90, 94, 92, 94, 97, 98, 91, 81, 81, 78, 53, 50, 36, 33, 30, 25, 23, 25, 25, 27, 33, 37, 51, 78, 81, 81, 86, 91, 89 }, 32);

    private Polygon head = new Polygon(new int[] { 53, 71, 81, 83, 83, 62, 41, 41, 43 }, new int[] { 32, 32, 22, 22, 8, 0, 8, 22, 22 }, 9);

    private Polygon centralTorso = new Polygon(new int[] { 48, 54, 70, 76, 76, 48 }, new int[] { 45, 85, 85, 45, 36, 36 }, 6);

    private Polygon leftTorso = new Polygon(new int[] { 54, 48, 48, 62, 62, 53, 43, 41, 41, 31, 33, 33, 29, 26, 47 }, new int[] { 82, 45, 36, 36, 32, 32, 22, 22, 20, 25, 27, 33, 37, 47, 82 }, 15);

    private Polygon rightTorso = new Polygon(new int[] { 70, 76, 76, 62, 62, 71, 81, 83, 83, 93, 91, 91, 95, 98, 77 }, new int[] { 82, 45, 36, 36, 32, 32, 22, 22, 20, 25, 27, 33, 37, 47, 82 }, 15);

    private Polygon leftLeg = new Polygon(new int[] { 18, 21, 37, 47, 54, 54, 61, 43, 45, 45, 43, 44, 19, 20, 18 }, new int[] { 104, 104, 65, 82, 82, 85, 85, 103, 103, 121, 121, 129, 129, 122, 122 }, 15);

    private Polygon rightLeg = new Polygon(new int[] { 107, 104, 88, 77, 70, 70, 64, 82, 80, 80, 82, 81, 106, 105, 107 }, new int[] { 104, 104, 63, 82, 82, 85, 85, 103, 103, 121, 121, 129, 129, 122, 122 }, 15);

    private Polygon rearLeftTorso = new Polygon(new int[] { 142, 142, 148, 139, 123, 123, 142 }, new int[] { 14, 43, 76, 76, 44, 17, 14 }, 7);

    private Polygon rearCentralTorso = new Polygon(new int[] { 142, 148, 162, 168, 168, 142 }, new int[] { 44, 76, 76, 44, 14, 14 }, 6);

    private Polygon rearRightTorso = new Polygon(new int[] { 168, 168, 162, 171, 187, 187, 168 }, new int[] { 14, 43, 76, 76, 44, 17, 14 }, 7);

    private Polygon intStHead = new Polygon(new int[] { 78, 48, 48, 78 }, new int[] { 149, 149, 127, 127 }, 4);

    private Polygon inStLeftArm = new Polygon(new int[] { 17, 11, 5, 5, 8, 8, 21, 21, 25, 25, 28, 51, 51, 29, 29 }, new int[] { 147, 170, 170, 194, 194, 197, 197, 194, 194, 170, 157, 157, 154, 154, 147 }, 15);

    private Polygon inStRightArm = new Polygon(new int[] { 109, 115, 121, 121, 118, 118, 105, 105, 101, 101, 98, 75, 75, 97, 97 }, new int[] { 147, 170, 170, 194, 194, 197, 197, 194, 194, 170, 157, 157, 154, 154, 147 }, 15);

    private Polygon inStCentralTorso = new Polygon(new int[] { 75, 75, 51, 51 }, new int[] { 203, 149, 149, 203 }, 4);

    private Polygon inStLeftTorso = new Polygon(new int[] { 32, 32, 51, 51 }, new int[] { 188, 160, 160, 193 }, 4);

    private Polygon inStRightTorso = new Polygon(new int[] { 94, 94, 75, 75 }, new int[] { 188, 160, 160, 193 }, 4);

    private Polygon inStLeftLeg = new Polygon(new int[] { 51, 51, 44, 44, 47, 47, 20, 20, 41, 41, 44, 44 }, new int[] { 195, 199, 199, 206, 206, 230, 230, 206, 206, 192, 192, 195 }, 12);

    private Polygon inStRightLeg = new Polygon(new int[] { 75, 75, 82, 82, 79, 79, 106, 106, 85, 85, 82, 82 }, new int[] { 195, 199, 199, 206, 206, 230, 230, 206, 206, 192, 192, 195 }, 12);

    private Polygon heatControl = new Polygon(new int[] { 149, 159, 159, 149 }, new int[] { 100, 100, 220, 220 }, 4);

    private Image heatImage;

    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, GUIPreferences.getInstance().getInt("AdvancedMechDisplayArmorSmallFontSize"));

    private static final Font FONT_VALUE = new Font("SansSerif", Font.PLAIN, GUIPreferences.getInstance().getInt("AdvancedMechDisplayArmorLargeFontSize"));

    public MechMapSet(JComponent c) {
        comp = c;
        setAreas();
        setLabels();
        setGroups();
        setBackGround();
    }

    public PMAreasGroup getContentGroup() {
        return content;
    }

    public Vector<BackGroundDrawer> getBackgroundDrawers() {
        return bgDrawers;
    }

    public void setEntity(Entity e) {
        Mech m = (Mech) e;
        boolean mtHeat = false;
        if ((e.getGame() != null) && e.getGame().getOptions().booleanOption("tacops_heat")) {
            mtHeat = true;
        }
        int a = 1;
        int a0 = 1;
        for (int i = 0; i < m.locations(); i++) {
            a = m.getArmor(i);
            a0 = m.getOArmor(i);
            vLabels[i].setValue(m.getArmorString(i));
            WidgetUtils.setAreaColor(areas[i], vLabels[i], (double) a / (double) a0);
            if (m.hasRearArmor(i)) {
                a = m.getArmor(i, true);
                a0 = m.getOArmor(i, true);
                vLabels[i + REAR_AREA_OFFSET].setValue(m.getArmorString(i, true));
                WidgetUtils.setAreaColor(areas[i + REAR_AREA_OFFSET], vLabels[i + REAR_AREA_OFFSET], (double) a / (double) a0);
            }
            a = m.getInternal(i);
            a0 = m.getOInternal(i);
            vLabels[i + INT_STRUCTURE_OFFSET].setValue(m.getInternalString(i));
            WidgetUtils.setAreaColor(areas[i + INT_STRUCTURE_OFFSET], vLabels[i + INT_STRUCTURE_OFFSET], (double) a / (double) a0);
        }
        vLabels[19].setValue(Integer.toString(m.heat));
        drawHeatControl(m.heat, mtHeat);
        if (!m.hasPatchworkArmor() && m.hasBARArmor(1)) {
            content.addArea(labels[19]);
            content.addArea(vLabels[20]);
            vLabels[20].setValue(String.valueOf(m.getBARRating(1)));
        } else {
            content.removeArea(labels[19]);
            content.removeArea(vLabels[20]);
        }
    }

    private void setAreas() {
        areas[Mech.LOC_HEAD] = new PMSimplePolygonArea(head);
        areas[Mech.LOC_CT] = new PMSimplePolygonArea(centralTorso);
        areas[Mech.LOC_RT] = new PMSimplePolygonArea(rightTorso);
        areas[Mech.LOC_LT] = new PMSimplePolygonArea(leftTorso);
        areas[Mech.LOC_RARM] = new PMSimplePolygonArea(rightArm);
        areas[Mech.LOC_LARM] = new PMSimplePolygonArea(leftArm);
        areas[Mech.LOC_RLEG] = new PMSimplePolygonArea(rightLeg);
        areas[Mech.LOC_LLEG] = new PMSimplePolygonArea(leftLeg);
        areas[REAR_AREA_OFFSET + Mech.LOC_CT] = new PMSimplePolygonArea(rearCentralTorso);
        areas[REAR_AREA_OFFSET + Mech.LOC_RT] = new PMSimplePolygonArea(rearRightTorso);
        areas[REAR_AREA_OFFSET + Mech.LOC_LT] = new PMSimplePolygonArea(rearLeftTorso);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_HEAD] = new PMSimplePolygonArea(intStHead);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_CT] = new PMSimplePolygonArea(inStCentralTorso);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_RT] = new PMSimplePolygonArea(inStRightTorso);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_LT] = new PMSimplePolygonArea(inStLeftTorso);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_RARM] = new PMSimplePolygonArea(inStRightArm);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_LARM] = new PMSimplePolygonArea(inStLeftArm);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_RLEG] = new PMSimplePolygonArea(inStRightLeg);
        areas[INT_STRUCTURE_OFFSET + Mech.LOC_LLEG] = new PMSimplePolygonArea(inStLeftLeg);
        heatImage = comp.createImage(10, 120);
        drawHeatControl(0);
        heatHotArea = new PMPicPolygonalArea(heatControl, heatImage);
    }

    private void setLabels() {
        FontMetrics fm = comp.getFontMetrics(FONT_LABEL);
        labels[Mech.LOC_HEAD] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_H"), fm, Color.black, 62, 6);
        labels[Mech.LOC_LARM] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LA"), fm, Color.black, 14, 59);
        labels[Mech.LOC_LT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LT"), fm, Color.black, 41, 52);
        labels[Mech.LOC_CT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_CT"), fm, Color.black, 62, 42);
        labels[Mech.LOC_RT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RT"), fm, Color.black, 84, 52);
        labels[Mech.LOC_RARM] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RA"), fm, Color.black, 109, 59);
        labels[Mech.LOC_LLEG] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LL"), fm, Color.black, 36, 92);
        labels[Mech.LOC_RLEG] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RL"), fm, Color.black, 90, 92);
        labels[REAR_AREA_OFFSET + Mech.LOC_LT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LT"), fm, Color.black, 133, 39);
        labels[REAR_AREA_OFFSET + Mech.LOC_CT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_CT"), fm, Color.black, 156, 25);
        labels[REAR_AREA_OFFSET + Mech.LOC_RT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RT"), fm, Color.black, 178, 39);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_HEAD] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_H"), fm, Color.black, 63, 130);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_LARM] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LA"), fm, Color.black, 14, 174);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_LT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LT"), fm, Color.black, 42, 166);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_CT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.L_CT"), fm, Color.black, 63, 168);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_RT] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RT"), fm, Color.black, 85, 166);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_RARM] = WidgetUtils.createLabel(Messages.getString("MechMapSet.L_RA"), fm, Color.black, 111, 174);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_LLEG] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_LL"), fm, Color.black, 33, 210);
        labels[INT_STRUCTURE_OFFSET + Mech.LOC_RLEG] = WidgetUtils.createLabel(Messages.getString("MechMapSet.l_RL"), fm, Color.black, 93, 210);
        labels[19] = WidgetUtils.createLabel(Messages.getString("MechMapSet.BARRating"), fm, Color.white, 65, 343);
        fm = comp.getFontMetrics(FONT_VALUE);
        vLabels[Mech.LOC_HEAD] = WidgetUtils.createValueLabel(62, 22, "", fm);
        vLabels[Mech.LOC_LARM] = WidgetUtils.createValueLabel(13, 72, "", fm);
        vLabels[Mech.LOC_LT] = WidgetUtils.createValueLabel(38, 44, "", fm);
        vLabels[Mech.LOC_CT] = WidgetUtils.createValueLabel(62, 57, "", fm);
        vLabels[Mech.LOC_RT] = WidgetUtils.createValueLabel(86, 44, "", fm);
        vLabels[Mech.LOC_RARM] = WidgetUtils.createValueLabel(112, 72, "", fm);
        vLabels[Mech.LOC_LLEG] = WidgetUtils.createValueLabel(31, 113, "", fm);
        vLabels[Mech.LOC_RLEG] = WidgetUtils.createValueLabel(94, 113, "", fm);
        vLabels[REAR_AREA_OFFSET + Mech.LOC_LT] = WidgetUtils.createValueLabel(132, 28, "", fm);
        vLabels[REAR_AREA_OFFSET + Mech.LOC_CT] = WidgetUtils.createValueLabel(156, 39, "", fm);
        vLabels[REAR_AREA_OFFSET + Mech.LOC_RT] = WidgetUtils.createValueLabel(177, 28, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_HEAD] = WidgetUtils.createValueLabel(63, 142, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_LARM] = WidgetUtils.createValueLabel(15, 187, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_LT] = WidgetUtils.createValueLabel(42, 180, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_CT] = WidgetUtils.createValueLabel(63, 182, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_RT] = WidgetUtils.createValueLabel(85, 180, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_RARM] = WidgetUtils.createValueLabel(111, 187, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_LLEG] = WidgetUtils.createValueLabel(33, 223, "", fm);
        vLabels[INT_STRUCTURE_OFFSET + Mech.LOC_RLEG] = WidgetUtils.createValueLabel(92, 223, "", fm);
        vLabels[19] = WidgetUtils.createValueLabel(155, 90, "", fm);
        vLabels[20] = WidgetUtils.createValueLabel(100, 345, "", fm);
    }

    private void setGroups() {
        PMAreasGroup frontArmor = new PMAreasGroup();
        PMAreasGroup rearArmor = new PMAreasGroup();
        PMAreasGroup intStructure = new PMAreasGroup();
        PMAreasGroup heat = new PMAreasGroup();
        for (int i = 0; i < 8; i++) {
            frontArmor.addArea(areas[i]);
            frontArmor.addArea(labels[i]);
            frontArmor.addArea(vLabels[i]);
        }
        for (int i = 0; i < 3; i++) {
            rearArmor.addArea(areas[8 + i]);
            rearArmor.addArea(labels[8 + i]);
            rearArmor.addArea(vLabels[8 + i]);
        }
        for (int i = 0; i < 8; i++) {
            intStructure.addArea(areas[11 + i]);
            intStructure.addArea(labels[11 + i]);
            intStructure.addArea(vLabels[11 + i]);
        }
        heat.addArea(heatHotArea);
        heat.addArea(vLabels[19]);
        frontArmor.translate(7, 18);
        rearArmor.translate(19, 20);
        intStructure.translate(6, 42);
        heat.translate(20, 52);
        for (int i = 0; i < 19; i++) {
            content.addArea(areas[i]);
            content.addArea(labels[i]);
            content.addArea(vLabels[i]);
        }
        content.addArea(heatHotArea);
        content.addArea(vLabels[19]);
    }

    private void setBackGround() {
        Image tile = comp.getToolkit().getImage(IMAGE_DIR + "/tile.gif");
        PMUtil.setImage(tile, comp);
        int b = BackGroundDrawer.TILING_BOTH;
        bgDrawers.addElement(new BackGroundDrawer(tile, b));
        tile = comp.getToolkit().getImage(IMAGE_DIR + "/bg_mech.gif");
        PMUtil.setImage(tile, comp);
        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_CENTER | BackGroundDrawer.HALIGN_CENTER;
        BackGroundDrawer bgd = new BackGroundDrawer(tile, b);
        bgDrawers.addElement(bgd);
    }

    private void drawHeatControl(int t) {
        drawHeatControl(t, false);
    }

    private void drawHeatControl(int t, boolean mtHeat) {
        int y = 0;
        int maxHeat, steps;
        if (mtHeat) {
            maxHeat = 50;
            steps = 2;
        } else {
            maxHeat = 30;
            steps = 4;
        }
        Graphics g = heatImage.getGraphics();
        for (int i = 0; i < maxHeat; i++) {
            y = 120 - (i + 1) * steps;
            if (i < t) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillRect(0, y, 10, steps);
            g.setColor(Color.black);
            g.drawRect(0, y, 10, steps);
        }
    }
}
