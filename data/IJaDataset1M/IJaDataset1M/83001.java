package net.sf.vgap4.assistant.factories;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import net.sf.vgap4.assistant.models.AssistantNode;
import net.sf.vgap4.assistant.models.Base;
import net.sf.vgap4.assistant.models.NativeShape;
import net.sf.vgap4.assistant.models.Planet;
import net.sf.vgap4.assistant.models.Pod;
import net.sf.vgap4.assistant.models.Ship;
import net.sf.vgap4.assistant.models.Spot;
import net.sf.vgap4.assistant.models.Thing;
import net.sf.vgap4.assistant.models.Wing;
import net.sf.vgap4.assistant.models.database.RaceDatabase;
import net.sf.vgap4.assistant.ui.AssistantConstants;

/**
 * This class draws the different icons for Map representation based on the contents of the Spot received as
 * the generation command. It will take on account for all Spot contents to fill each one of the decorator
 * slots with the aggregated data.<br>
 * There are 8 slots for decorators. The contents depending on the location are described on the next list:
 * <ul>
 * <li><b>N</b> - High level of contraband present.</li>
 * <li><b>NE</b> - High/Low level of minerals present.</li>
 * <li><b>E</b> - Ships in orbit around planet or base.</li>
 * <li><b>SE</b> - Pods in orbit or attached to a in orbit ship.</li>
 * <li><b>S</b> - Wings in orbit or docked to an orbiting Ship.</li>
 * <li><b>W</b> - Natives present on the Planet's surface.</li>
 * <li><b>NW</b> - Indicator of the farming level. Low-Acceptable-High from race viewpoint.</li>
 * </ul>
 */
public class IconGenerator {

    public static String SHAPE_PLANET = "PLANET";

    public static String SHAPE_SHIP = "SHIP";

    public static String SHAPE_POD = "CAPSULE";

    public static String SHAPE_WING = "WING";

    public static final String SHAPE_NATIVE = "NATIVE";

    private static Logger logger = Logger.getLogger("net.sf.vgap4.assistant.factories");

    private static Hashtable<String, Image> iconCache = new Hashtable<String, Image>();

    private static final String SHAPE_BASE = "BASE";

    private static final byte MASK_FOE = 001;

    private static final byte MASK_FRIEND = 002;

    private static final byte MASK_NEUTRAL = 004;

    private static Vector<Color> colorFFMap = new Vector<Color>(8);

    static {
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_UNDEFINED);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FOE);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FRIEND);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FOEFRIEND);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_NEUTRAL);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FOENEUTRAL);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FRIENDNEUTRAL);
        IconGenerator.colorFFMap.add(AssistantConstants.COLOR_FF_FOEFRIENDNEUTRAL);
    }

    public static Image generateImage(final AssistantNode target, final Color backColor) {
        IconGenerator imageFactory = new IconGenerator();
        imageFactory.setBackColor(backColor);
        if (target instanceof Spot) {
            imageFactory = IconGenerator.generateSpotImage((Spot) target);
        } else {
            imageFactory = IconGenerator.generateUnitImage(target);
        }
        imageFactory.setBackColor(backColor);
        final String code = imageFactory.getGenerationCode();
        Image cacheHit = IconGenerator.iconCache.get(code);
        cacheHit = null;
        if (null == cacheHit) return imageFactory.drawImage(code); else return cacheHit;
    }

    private static IconGenerator generateSpotImage(final Spot target) {
        final AssistantNode representative = target.getRepresentative();
        final IconGenerator imageFactory = IconGenerator.generateUnitImage(representative);
        final Iterator<AssistantNode> eit = target.getContents().iterator();
        while (eit.hasNext()) {
            final AssistantNode element = eit.next();
            if (representative == element) continue;
            if (element instanceof Base) {
                imageFactory.activateBase();
                imageFactory.setEnemyMainStatus(target.getFFStatus());
            }
            if (element instanceof Planet) {
                imageFactory.setPlanetClassification(((Planet) element).getPlanetClassification());
                imageFactory.setPlanetContrabandLevel(((Planet) element).getContrabandLevel());
                imageFactory.setPlanetFarmLevel(((Planet) element).getFarmingLevel());
                if (((Planet) element).hasNatives()) imageFactory.activateNatives();
            }
            if (element instanceof Ship) {
                imageFactory.addShipModifier(element.getFFStatus());
            }
            if (element instanceof Pod) {
                imageFactory.addPodModifier(element.getFFStatus());
            }
            if (element instanceof Wing) {
                imageFactory.addWingModifier(element.getFFStatus());
            }
        }
        return imageFactory;
    }

    private static IconGenerator generateUnitImage(final AssistantNode target) {
        final IconGenerator imageFactory = new IconGenerator();
        if (target instanceof Base) {
            imageFactory.setShape(IconGenerator.SHAPE_BASE);
            imageFactory.activateBase();
            imageFactory.setEnemyMainStatus(target.getFFStatus());
            final Planet onPlanet = ((Base) target).getOnPlanet();
            imageFactory.setPlanetClassification(onPlanet.getPlanetClassification());
            imageFactory.setPlanetContrabandLevel(onPlanet.getContrabandLevel());
            imageFactory.setPlanetFarmLevel(onPlanet.getFarmingLevel());
            if (onPlanet.hasNatives()) imageFactory.activateNatives();
        }
        if (target instanceof Planet) {
            imageFactory.setShape(IconGenerator.SHAPE_PLANET);
            imageFactory.setInfoLevel(((Planet) target).infoLevel());
            imageFactory.setPlanetClassification(((Planet) target).getPlanetClassification());
            imageFactory.setPlanetContrabandLevel(((Planet) target).getContrabandLevel());
            imageFactory.setPlanetFarmLevel(((Planet) target).getFarmingLevel());
            if (((Planet) target).hasNatives()) imageFactory.activateNatives();
        }
        if (target instanceof Ship) {
            imageFactory.setShape(IconGenerator.SHAPE_SHIP);
            imageFactory.setEnemyMainStatus(target.getFFStatus());
        }
        if (target instanceof Pod) {
            imageFactory.setShape(IconGenerator.SHAPE_POD);
            imageFactory.setEnemyMainStatus(target.getFFStatus());
        }
        if (target instanceof Wing) {
            imageFactory.setShape(IconGenerator.SHAPE_WING);
            imageFactory.setEnemyMainStatus(target.getFFStatus());
        }
        if (target instanceof Thing) {
            if (((Thing) target).getDataType().equals(AssistantConstants.MODELTYPE_WING)) {
                imageFactory.setShape(IconGenerator.SHAPE_WING);
                imageFactory.setEnemyMainStatus(target.getFFStatus());
            }
        }
        if (target instanceof NativeShape) {
            imageFactory.setShape(IconGenerator.SHAPE_NATIVE);
        }
        return imageFactory;
    }

    private Color backgroundColor = ColorConstants.white;

    private String shapeName = "-";

    private boolean hasBase = false;

    private byte shapeDecoratorCode = 000;

    private String infoLevel = "-";

    private String planetClassification = "-";

    private String contrabandLevel = Planet.CONTRABAND_LOW;

    private String planetFarmLevel = RaceDatabase.FARMLEVEL_COLD;

    private boolean hasNatives = false;

    private byte shipDecoratorCode = 000;

    private byte podDecoratorCode = 000;

    private byte wingDecoratorCode = 000;

    private boolean shipsInOrbit = false;

    private boolean podsInOrbit = false;

    private boolean wingsInOrbit = false;

    protected void activateBase() {
        hasBase = true;
    }

    protected void activateNatives() {
        hasNatives = true;
    }

    protected void addPodModifier(final String status) {
        podsInOrbit = true;
        if (AssistantConstants.FFSTATUS_FOE.equals(status)) podDecoratorCode = (byte) (podDecoratorCode | IconGenerator.MASK_FOE);
        if (AssistantConstants.FFSTATUS_FRIEND.equals(status)) podDecoratorCode = (byte) (podDecoratorCode | IconGenerator.MASK_FRIEND);
        if (AssistantConstants.FFSTATUS_NEUTRAL.equals(status)) podDecoratorCode = (byte) (podDecoratorCode | IconGenerator.MASK_NEUTRAL);
    }

    protected void addShipModifier(final String status) {
        shipsInOrbit = true;
        if (AssistantConstants.FFSTATUS_FOE.equals(status)) shipDecoratorCode = (byte) (shipDecoratorCode | IconGenerator.MASK_FOE);
        if (AssistantConstants.FFSTATUS_FRIEND.equals(status)) shipDecoratorCode = (byte) (shipDecoratorCode | IconGenerator.MASK_FRIEND);
        if (AssistantConstants.FFSTATUS_NEUTRAL.equals(status)) shipDecoratorCode = (byte) (shipDecoratorCode | IconGenerator.MASK_NEUTRAL);
    }

    protected void addWingModifier(final String status) {
        wingsInOrbit = true;
        if (AssistantConstants.FFSTATUS_FOE.equals(status)) wingDecoratorCode = (byte) (wingDecoratorCode | IconGenerator.MASK_FOE);
        if (AssistantConstants.FFSTATUS_FRIEND.equals(status)) wingDecoratorCode = (byte) (wingDecoratorCode | IconGenerator.MASK_FRIEND);
        if (AssistantConstants.FFSTATUS_NEUTRAL.equals(status)) wingDecoratorCode = (byte) (wingDecoratorCode | IconGenerator.MASK_NEUTRAL);
    }

    protected void setBackColor(final Color backColor) {
        backgroundColor = backColor;
    }

    protected void setEnemyMainStatus(final String status) {
        if (AssistantConstants.FFSTATUS_FRIEND.equals(status)) shapeDecoratorCode = (byte) (shapeDecoratorCode | IconGenerator.MASK_FRIEND);
        if (AssistantConstants.FFSTATUS_FOE.equals(status)) shapeDecoratorCode = (byte) (shapeDecoratorCode | IconGenerator.MASK_FOE);
        if (AssistantConstants.FFSTATUS_NEUTRAL.equals(status)) shapeDecoratorCode = (byte) (shapeDecoratorCode | IconGenerator.MASK_NEUTRAL);
    }

    protected void setInfoLevel(final String level) {
        infoLevel = level;
    }

    protected void setPlanetClassification(final String planetClassification) {
        this.planetClassification = planetClassification;
    }

    protected void setPlanetContrabandLevel(final String contrabandLevel) {
        this.contrabandLevel = contrabandLevel;
    }

    protected void setPlanetFarmLevel(final String farmLevel) {
        planetFarmLevel = farmLevel;
    }

    protected void setShape(final String shape) {
        shapeName = shape;
    }

    private char booleanDecode(final boolean code, final char template) {
        if (code) return template; else return '-';
    }

    private String contrabandDecode(final String code) {
        if (Planet.CONTRABAND_HIGH.equals(code)) return "C"; else return "-";
    }

    private Color decodeFFColor(final byte code) {
        return IconGenerator.colorFFMap.get(code);
    }

    private Image drawImage(final String code) {
        final Image image = new Image(Display.getDefault(), AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
        final GC gc = new GC(image);
        final Rectangle bound = new Rectangle(0, 0, AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
        gc.setBackground(backgroundColor);
        gc.fillRectangle(bound);
        if (hasBase) {
            gc.setForeground(AssistantConstants.COLOR_BASE_RING);
            gc.drawOval(bound.x + 2, bound.y + 2, 11, 11);
        }
        if (IconGenerator.SHAPE_BASE.equals(shapeName)) {
            if (shapeDecoratorCode > 0) gc.setBackground(this.decodeFFColor(shapeDecoratorCode));
            this.drawPlanet(gc);
            this.drawPlanetClassification(gc);
            this.drawPlanetContraband(gc);
            this.drawPlanetFarming(gc);
            this.drawPlanetNatives(gc);
        }
        if (IconGenerator.SHAPE_PLANET.equals(shapeName)) {
            if (AssistantConstants.INFOLEVEL_UNEXPLORED.equals(infoLevel)) gc.setBackground(AssistantConstants.COLOR_PLANET_UNEXPLORED);
            if (AssistantConstants.INFOLEVEL_NOINFO.equals(infoLevel)) gc.setBackground(AssistantConstants.COLOR_PLANET_NOINFO);
            if (AssistantConstants.INFOLEVEL_INFO.equals(infoLevel)) gc.setBackground(AssistantConstants.COLOR_PLANET_INFO);
            if (AssistantConstants.INFOLEVEL_OBSOLETE.equals(infoLevel)) gc.setBackground(AssistantConstants.COLOR_PLANET_OBSOLETE);
            this.drawPlanet(gc);
            if (AssistantConstants.INFOLEVEL_INFO.equals(infoLevel)) {
                this.drawPlanetClassification(gc);
                this.drawPlanetContraband(gc);
                this.drawPlanetFarming(gc);
            }
            this.drawPlanetNatives(gc);
        }
        if (IconGenerator.SHAPE_NATIVE.equals(shapeName)) {
            gc.setBackground(AssistantConstants.COLOR_PLANET_NATIVES);
            this.drawPlanet(gc);
        }
        if (IconGenerator.SHAPE_SHIP.equals(shapeName)) {
            final PointList triangle = new PointList(3);
            final Point head = new Point(bound.x + 3, 0 + (bound.y + 15 / 2 - 0));
            final Point p2 = new Point(head.x + 10 - 1, -1 + head.y - 4);
            final Point p3 = new Point(head.x + 10 - 1, 1 + head.y + 4);
            triangle.addPoint(head);
            triangle.addPoint(p2);
            triangle.addPoint(p3);
            gc.setBackground(this.decodeFFColor(shapeDecoratorCode));
            gc.fillPolygon(triangle.toIntArray());
        }
        if (IconGenerator.SHAPE_POD.equals(shapeName)) {
            gc.setBackground(this.decodeFFColor(shapeDecoratorCode));
            Rectangle planetBounds = new Rectangle(bound.x + 2, bound.y + 2, 7, 7);
            gc.fillOval(planetBounds.x, planetBounds.y, planetBounds.width, planetBounds.height);
            planetBounds = new Rectangle(bound.x + 7, bound.y + 7, 7, 7);
            gc.fillOval(planetBounds.x, planetBounds.y, planetBounds.width, planetBounds.height);
        }
        if (IconGenerator.SHAPE_WING.equals(shapeName)) {
            final PointList triangle1 = new PointList(3);
            Point head = new Point(5, 0);
            Point p2 = new Point(5 - 3, 9);
            Point p3 = new Point(5 + 3, 9);
            triangle1.addPoint(head);
            triangle1.addPoint(p2);
            triangle1.addPoint(p3);
            final PointList triangle2 = new PointList(3);
            head = new Point(10, 15 - 9);
            p2 = new Point(10 - 3, 15);
            p3 = new Point(10 + 3, 15);
            triangle2.addPoint(head);
            triangle2.addPoint(p2);
            triangle2.addPoint(p3);
            gc.setBackground(this.decodeFFColor(shapeDecoratorCode));
            gc.fillPolygon(triangle1.toIntArray());
            gc.fillPolygon(triangle2.toIntArray());
        }
        if (shipsInOrbit) {
            gc.setBackground(this.decodeFFColor(shipDecoratorCode));
            gc.fillOval(bound.x + 12, bound.y + 6, 4, 4);
        }
        if (podsInOrbit) {
            gc.setBackground(this.decodeFFColor(podDecoratorCode));
            gc.fillOval(bound.x + 10, bound.y + 10, 4, 4);
        }
        if (wingsInOrbit) {
            gc.setBackground(this.decodeFFColor(wingDecoratorCode));
            gc.fillOval(bound.x + 6, bound.y + 12, 4, 4);
        }
        gc.dispose();
        IconGenerator.logger.info("Registering in cache Icon code: " + code);
        IconGenerator.iconCache.put(code, image);
        return image;
    }

    private void drawPlanet(final GC gc) {
        final Rectangle bound = new Rectangle(0, 0, AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
        final Rectangle planetBounds = new Rectangle(bound.x + 4, bound.y + 4, 8, 8);
        gc.fillOval(planetBounds.x, planetBounds.y, planetBounds.width, planetBounds.height);
    }

    private void drawPlanetClassification(final GC gc) {
        if (planetClassification.equals("A")) {
            gc.setForeground(AssistantConstants.COLOR_PLANET_CLASS_A);
            gc.drawLine(15, 0, 15 - 3, 3);
            gc.drawLine(15 - 3, 3, 15 - 1, 3);
            gc.drawLine(15 - 3, 3, 15 - 3, 1);
        }
        if (planetClassification.equals("C")) {
            gc.setForeground(AssistantConstants.COLOR_PLANET_CLASS_C);
            gc.drawLine(15, 0, 15 - 3, 3);
            gc.drawLine(15 - 3, 0, 15, 3);
        }
    }

    private void drawPlanetContraband(final GC gc) {
        final Rectangle bound = new Rectangle(0, 0, AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
        if (contrabandLevel.equals(Planet.CONTRABAND_HIGH)) {
            gc.setBackground(AssistantConstants.COLOR_CONTRABAND_DECORATOR);
            gc.fillOval(bound.x + 6, bound.y + 0, 4, 4);
        }
    }

    private void drawPlanetFarming(final GC gc) {
        final Rectangle bound = new Rectangle(0, 0, AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
        gc.setBackground(AssistantConstants.COLOR_FARMING_NORMAL);
        if (planetFarmLevel.equals(RaceDatabase.FARMLEVEL_COLD)) gc.setBackground(AssistantConstants.COLOR_FARMING_COLD);
        if (planetFarmLevel.equals(RaceDatabase.FARMLEVEL_HOT)) gc.setBackground(AssistantConstants.COLOR_FARMING_HOT);
        gc.fillOval(bound.x + 1, bound.y + 1, 4, 4);
    }

    private void drawPlanetNatives(final GC gc) {
        if (hasNatives) {
            final Rectangle bound = new Rectangle(0, 0, AssistantConstants.SIZE_ICONIMAGE, AssistantConstants.SIZE_ICONIMAGE);
            gc.setBackground(AssistantConstants.COLOR_NATIVES_DECORATOR);
            gc.fillOval(bound.x + 0, bound.y + 6, 4, 4);
        }
    }

    private String farmingDecode(final String code) {
        if (RaceDatabase.FARMLEVEL_HOT.equals(code)) return "H";
        if (RaceDatabase.FARMLEVEL_COLD.equals(code)) return "C";
        if (RaceDatabase.FARMLEVEL_INRANGE.equals(code)) return "-";
        return "-";
    }

    private String getGenerationCode() {
        final StringBuffer code = new StringBuffer();
        code.append(shapeName.charAt(0));
        code.append(shapeDecoratorCode);
        if (IconGenerator.SHAPE_PLANET.equals(shapeName)) {
            if (AssistantConstants.INFOLEVEL_INFO.equals(infoLevel)) {
                code.append(this.infoLevelDecode(infoLevel));
                code.append(this.contrabandDecode(contrabandLevel));
                code.append(planetClassification.charAt(0));
                code.append(this.farmingDecode(planetFarmLevel));
            }
        }
        code.append(shipDecoratorCode);
        code.append(podDecoratorCode);
        code.append(wingDecoratorCode);
        code.append(this.booleanDecode(hasNatives, 'N'));
        return code.toString();
    }

    private String infoLevelDecode(final String level) {
        if (AssistantConstants.INFOLEVEL_INFO.equals(level)) return "I";
        if (AssistantConstants.INFOLEVEL_NOINFO.equals(level)) return "-";
        if (AssistantConstants.INFOLEVEL_UNEXPLORED.equals(level)) return "X";
        if (AssistantConstants.INFOLEVEL_OBSOLETE.equals(level)) return "O";
        return ("-");
    }
}
