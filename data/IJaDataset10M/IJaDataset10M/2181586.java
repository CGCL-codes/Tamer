package gui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.*;
import data.*;
import data.battle.Battle;
import data.constants.CargoType;
import data.controls.Game;
import data.geom.MapGeometry;
import data.geom.Position;
import ogv.OGV;
import ogv.OGV.MapColors;
import util.ConfigNode;
import util.Intersections;
import util.TimeStat;
import util.Utils;

public class MapImage extends JPanel {

    public static final Stroke NORMAL_STROKE = new BasicStroke();

    private static final Stroke BOUNDARY_STROKE = NORMAL_STROKE;

    private static final Stroke FORETRACK_STROKE = NORMAL_STROKE;

    private static final Stroke BACKTRACK_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 10.0f, 5.0f }, 0.0f);

    private static final float ROUTE_LINE_WIDTH = 4.0f;

    private static final Stroke ROUTE_STROKE = new BasicStroke(ROUTE_LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 0.0f, 2 * ROUTE_LINE_WIDTH }, 0.0f);

    private static final Stroke CIRCLES_STROKE = NORMAL_STROKE;

    private static final double GROUP_SIZE = 3.0;

    private static final double MAX_DENSITY = 250.0;

    private static final double MAX_PLANET_SIZE = 2500.0;

    private static final double MIN_PLANET_RADIUS = 1.0;

    private static final double MAX_PLANET_RADIUS = 15.0;

    private static final double CURRENT_PLANET_EXTRA_DISTANCE = 8.0;

    private static final double CURRENT_PLANET_MIN_DISTANCE = 15.0;

    private static final double BATTLE_CROSS_EXTRA_SIZE = 1.0;

    private static final double BATTLE_CROSS_MAX_FACTOR = 0.5;

    private static final double MIN_FLAG_SIZE = 10;

    private static final double MAX_FLAG_SIZE = 40;

    private static final int PLANET_INFO_MAX_FONT_SIZE = 14;

    private static final Font PLANET_INFO_FONT = new Font("Dialog", Font.PLAIN, PLANET_INFO_MAX_FONT_SIZE);

    private static final int PLANET_INFO_MIN_FONT_SIZE = 10;

    private static final double PLANET_INFO_FONT_FACTOR = 0.15;

    private static final int GRID_STEPS = 10;

    private static final int MAX_TICKS = 2;

    private static final double TICK_SIZE = 3.0;

    public static final Stroke TICK_STROKE = NORMAL_STROKE;

    private static final double MIN_SELECTION_RADIUS = 9.0;

    private static final double SELECTION_SPACE = 5.0;

    private static final Stroke SELECTION_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 2.0f, 2.0f }, 0.0f);

    private static final Font CURSOR_INFO_FONT = new Font("Dialog", Font.PLAIN, 12);

    private static final int CURSOR_INFO_PADDING = 3;

    private static final int CURSOR_INFO_OFFSET = 17;

    private final Game game;

    private final MapViewerState data;

    private final double gs;

    private Font font;

    private double factor = 0.0;

    private double factor2 = 0.0;

    private Position gc;

    private int mapWidth;

    private int mapHeight;

    private final List<Group> launchedGroups = new ArrayList<Group>();

    private final Map<Planet, CircleNode> circles = new HashMap<Planet, CircleNode>();

    private Map<Planet, String> upperPlanetInfo;

    private boolean needInit = true;

    private final double averageDist;

    private double density;

    private BufferedImage image = null;

    private final TimeStat ts = new TimeStat("MapImage");

    public MapImage(MapViewerState data) {
        this.data = data;
        game = OGV.getGame();
        gs = game.getSize();
        averageDist = calcAverageDist();
        setBackground(OGV.getColor(MapColors.BACKGROUND_COLOR));
        getMapOptions();
    }

    private double calcAverageDist() {
        if (game.getPlanets().isEmpty()) return 1;
        Planet[] planets = game.getPlanets().toArray(new Planet[0]);
        if (planets.length > 1000) {
            Collections.shuffle(Arrays.asList(planets));
            planets = Arrays.copyOf(planets, 1000);
        }
        int n = planets.length;
        double q = 0;
        for (int i = 1; i < n; i++) {
            Position pos1 = planets[i].getPosition();
            for (int j = 0; j < i; j++) {
                Position pos2 = planets[j].getPosition();
                double d = game.geometry.distance(pos1, pos2);
                q += 1 / Math.max(0.01, d);
            }
        }
        return 1.76 * n * n / q / Math.sqrt(game.getPlanets().size());
    }

    private void setDefaultOptions() {
        List<Planet> planets;
        if (game.getYou() == null) planets = game.getPlanets(); else {
            planets = new ArrayList<Planet>();
            for (Planet p : game.getPlanets()) if (p.isYour()) planets.add(p);
            if (planets.isEmpty()) planets = game.getPlanets();
        }
        gc = game.calcPlanetsCenter(planets);
        if (gc == null) gc = new Position(gs * 0.5, gs * 0.5);
        factor = 0.5 * MAX_DENSITY / averageDist;
        setMapOptions();
    }

    private void getMapOptions() {
        ConfigNode config = game.getConfig().subnode("map");
        if (config.getString("factor", null) != null) {
            factor = config.getDouble("factor", 0);
            double x = config.getDouble("center.x", gs / 2);
            double y = config.getDouble("center.y", gs / 2);
            gc = new Position(x, y);
        } else {
            setDefaultOptions();
        }
        invalidateImage();
    }

    private void setMapOptions() {
        ConfigNode config = game.getConfig().subnode("map");
        config.putDouble("factor", factor);
        config.putDouble("center.x", gc.getX());
        config.putDouble("center.y", gc.getY());
    }

    public void loadMapOptions(int key) {
        ConfigNode config = game.getConfig().subnode("map").subnode(key);
        if (config.getString("factor", null) != null) {
            factor = config.getDouble("factor", 0);
            double x = config.getDouble("center.x", gc.getX());
            double y = config.getDouble("center.y", gc.getY());
            gc = new Position(x, y);
            invalidateImage();
            setMapOptions();
        }
    }

    public void saveMapOptions(int key) {
        ConfigNode config = game.getConfig().subnode("map").subnode(key);
        config.putDouble("factor", factor);
        config.putDouble("center.x", gc.getX());
        config.putDouble("center.y", gc.getY());
    }

    public void changeFactor(double x) {
        if (x != 0.0) factor *= x; else factor = minFactor();
        ConfigNode config = game.getConfig().subnode("map");
        config.putDouble("factor", factor);
        invalidateImage();
    }

    private double minFactor() {
        return Math.min(getWidth(), getHeight()) / gs;
    }

    public void moveCenter(double x, double y) {
        setCenter(game.geometry.absolute(gc, new Position(x, y)));
    }

    public void moveCenter(Position p) {
        setCenter(game.geometry.absolute(gc, p));
    }

    public void moveCenter2(double x, double y) {
        moveCenter(x / factor, y / factor);
    }

    public Position getCenter() {
        return gc;
    }

    public void setCenter(Position p) {
        gc = p;
        ConfigNode config = game.getConfig().subnode("map");
        config.putDouble("center.x", gc.getX());
        config.putDouble("center.y", gc.getY());
        invalidateImage();
        repaint();
    }

    public void zoomTo(double dx, double dy) {
        double w = Math.max(1, getWidth() - 2 * MIN_SELECTION_RADIUS);
        double h = Math.max(1, getHeight() - 2 * MIN_SELECTION_RADIUS);
        factor = Math.max(factor, minFactor());
        if (w * dy <= h * dx) {
            if (2 * dx * factor > w) factor = w / (2 * dx);
        } else {
            if (2 * dy * factor > h) factor = h / (2 * dy);
        }
        factor = Math.max(factor, minFactor());
        ConfigNode config = game.getConfig().subnode("map");
        config.putDouble("factor", factor);
        invalidateImage();
    }

    private void invalidateImage() {
        image = null;
    }

    @Override
    public void paint(Graphics g) {
        if (getWidth() == 0 || getHeight() == 0) return;
        ts.start();
        initLists();
        density = averageDist * factor;
        if (density < MAX_DENSITY) factor2 = density / MAX_DENSITY; else factor2 = 1;
        if (factor == 0.0) {
            factor = minFactor();
            invalidateImage();
        }
        int hw = getWidth() / 2;
        int hh = getHeight() / 2;
        Shape shape = game.geometry.getShape(gc, factor);
        mapWidth = hw * 2;
        mapHeight = hh * 2;
        if (shape != null) {
            Rectangle r = shape.getBounds();
            mapWidth = Math.min(r.width, mapWidth) / 2 * 2;
            mapHeight = Math.min(r.height, mapHeight) / 2 * 2;
        }
        if (image == null || mapWidth > image.getWidth() || mapHeight > image.getHeight()) {
            drawStaticImage(createImageCanvas(mapWidth, mapHeight));
        }
        g.setColor(OGV.getColor(MapColors.BACKGROUND_COLOR));
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setClip(getMapBoundary());
        g2.translate(hw, hh);
        if (shape != null) g2.clip(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.drawImage(image, -(image.getWidth() / 2), -(image.getHeight() / 2), null);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        if (shape != null) {
            g2.setColor(OGV.getColor(MapColors.BOUNDARY_COLOR));
            g2.setStroke(BOUNDARY_STROKE);
            g2.draw(shape);
        }
        drawDynamicImage(g2);
    }

    private Graphics2D createImageCanvas(int width, int heght) {
        image = new BufferedImage(width, heght, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setClip(0, 0, width, heght);
        g2.translate(width / 2, heght / 2);
        g2.setBackground(OGV.getColor(MapColors.BACKGROUND_COLOR));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        return g2;
    }

    private void drawStaticImage(Graphics2D g2) {
        drawGrid(g2);
        drawPlanetCircles(g2);
        drawRoutes(g2);
        drawBackTracks(g2);
        drawUndefinedGroupTracks(g2);
        drawForeTracks(g2);
        drawSpeedTicks(g2);
        drawBattles(g2);
        drawPlanets(g2);
        drawFlags(g2);
        drawGroups(g2);
    }

    private void drawDynamicImage(Graphics2D g2) {
        drawSelectedPlanets(g2);
        if (data.mouse != null) {
            if (data.control != null) data.control.draw(this, g2, data.mouse, game.geometry);
            drawDistance(g2);
            drawSelectionBorder(g2);
        }
        g2.setClip(null);
        drawCursorInfo(g2);
    }

    private void drawGroups(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.IN_SPACE)) return;
        drawInSpaceGroups(g2);
        drawIncomingGroups(g2);
        drawUndefinedGroups(g2);
    }

    private void drawIncomingGroups(Graphics2D g2) {
        g2.setColor(OGV.getColor(MapColors.GROUP_COLOR));
        g2.setStroke(NORMAL_STROKE);
        for (IncomingGroup gr : game.getIncomingGroups()) drawGroup(g2, mapToImage(gr.getPosition()));
    }

    private void drawInSpaceGroups(Graphics2D g2) {
        g2.setColor(OGV.getColor(MapColors.GROUP_COLOR));
        g2.setStroke(NORMAL_STROKE);
        for (Group gr : game.getInSpaceGroups()) drawGroup(g2, mapToImage(gr.getPosition()));
    }

    private void drawUndefinedGroups(Graphics2D g2) {
        g2.setColor(OGV.getColor(MapColors.GROUP_COLOR));
        g2.setStroke(NORMAL_STROKE);
        for (UnidentifiedGroup gr : game.getUnidentifiedGroups()) drawGroup(g2, mapToImage(gr.getPosition()));
    }

    private void drawSpeedTicks(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.IN_SPACE)) return;
        if (!OGV.getOption(OGV.MapMode.TICKS)) return;
        g2.setStroke(TICK_STROKE);
        g2.setColor(OGV.getColor(MapColors.LAUNCHED_GROUP_COLOR));
        for (Group gr : launchedGroups) drawSpeedTicks(g2, gr.getPosition(), gr.getDestination().getPosition(), gr.getFleetSpeed());
        g2.setColor(OGV.getColor(MapColors.IN_SPACE_GROUP_COLOR));
        for (Group gr : game.getInSpaceGroups()) drawSpeedTicks(g2, gr.getPosition(), gr.getDestination().getPosition(), gr.getFleetSpeed());
        g2.setColor(OGV.getColor(MapColors.INCOMING_GROUP_COLOR));
        for (IncomingGroup gr : game.getIncomingGroups()) drawSpeedTicks(g2, gr.getPosition(), gr.getDestination().getPosition(), gr.getSpeed());
    }

    private void drawSpeedTicks(Graphics2D g2, Position pos1, Position pos2, double speed) {
        double dist = game.geometry.distance(pos1, pos2);
        Position rel = game.geometry.relative(pos1, pos2);
        double dx1 = rel.getX() * speed / dist;
        double dy1 = rel.getY() * speed / dist;
        Position step = new Position(dx1, dy1);
        for (int i = 1; i <= MAX_TICKS && (dist -= speed) > 0; ++i) {
            pos1 = game.geometry.absolute(pos1, step);
            Point2D.Double p = mapToImage(pos1);
            drawTick(g2, p);
        }
    }

    private void drawUndefinedGroupTracks(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.TRACKS) && game.tracksComputed) return;
        g2.setStroke(NORMAL_STROKE);
        g2.setColor(OGV.getColor(MapColors.UNDEFINED_GROUP_COLOR));
        for (UnidentifiedGroup gr : game.getUnidentifiedGroups()) if (gr.getDestination() != null) drawGameLine(g2, gr.getDestination().getPosition(), gr.getSource().getPosition());
    }

    private void drawRoutes(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.ROUTES)) return;
        g2.setStroke(ROUTE_STROKE);
        for (Route r : game.getRoutes()) {
            g2.setColor(getColor(r.getType()));
            drawGameLine(g2, r.getSource().getPosition(), r.getDestination().getPosition());
        }
    }

    private void drawForeTracks(Graphics2D g2) {
        g2.setStroke(FORETRACK_STROKE);
        if (OGV.getOption(OGV.MapMode.IN_SPACE)) {
            g2.setColor(OGV.getColor(MapColors.IN_SPACE_GROUP_COLOR));
            for (Group gr : game.getInSpaceGroups()) drawGameLine(g2, gr.getPosition(), gr.getDestination().getPosition());
            g2.setColor(OGV.getColor(MapColors.INCOMING_GROUP_COLOR));
            for (IncomingGroup gr : game.getIncomingGroups()) drawGameLine(g2, gr.getPosition(), gr.getDestination().getPosition());
        }
        if (OGV.getOption(OGV.MapMode.LAUNCHED)) {
            g2.setColor(OGV.getColor(MapColors.LAUNCHED_GROUP_COLOR));
            for (Group gr : launchedGroups) drawGameLine(g2, gr.getSource().getPosition(), gr.getDestination().getPosition());
        }
    }

    private void drawBackTracks(Graphics2D g2) {
        if (OGV.getOption(OGV.MapMode.IN_SPACE) && OGV.getOption(OGV.MapMode.BACK)) {
            g2.setStroke(BACKTRACK_STROKE);
            g2.setColor(OGV.getColor(MapColors.IN_SPACE_GROUP_BACK_COLOR));
            for (Group gr : game.getInSpaceGroups()) drawGameLine(g2, gr.getPosition(), gr.getSource().getPosition());
            g2.setColor(OGV.getColor(MapColors.INCOMING_GROUP_BACK_COLOR));
            for (IncomingGroup gr : game.getIncomingGroups()) drawGameLine(g2, gr.getPosition(), gr.getSource().getPosition());
        }
    }

    private void drawBattles(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.BATTLES)) return;
        g2.setColor(OGV.getColor(MapColors.BATTLE_CROSS_COLOR));
        g2.setStroke(NORMAL_STROKE);
        for (Battle b : game.getBattles()) drawBattle(g2, b);
    }

    private void drawBattle(Graphics2D g2, Battle b) {
        Point2D.Double p = mapToImage(b.getPlanet().getPosition());
        double r = radius(b.getPlanet().getSize()) + BATTLE_CROSS_EXTRA_SIZE + Math.sqrt(b.getLoss()) * BATTLE_CROSS_MAX_FACTOR * factor2;
        drawLine(g2, p.x - r, p.y - r, p.x + r, p.y + r);
        drawLine(g2, p.x - r, p.y + r, p.x + r, p.y - r);
    }

    private void drawFlags(Graphics2D g2) {
        g2.setStroke(NORMAL_STROKE);
        for (Planet p : game.getFlags()) drawFlag(g2, p);
    }

    private void drawFlag(Graphics2D g2, Planet p) {
        PlanetNode n = new PlanetNode(p);
        g2.setColor(n.c);
        double x1 = n.pos.x;
        double y1 = n.pos.y - n.r;
        double h = Math.max(MAX_FLAG_SIZE * factor2, MIN_FLAG_SIZE);
        drawLine(g2, x1, y1, x1, y1 - h);
        g2.fill(new Rectangle2D.Double(x1, y1 - h, h * 0.8, h * 0.6));
    }

    private void drawPlanetCircles(Graphics2D g2) {
        g2.setColor(OGV.getColor(MapColors.CIRCLES_COLOR));
        g2.setStroke(CIRCLES_STROKE);
        for (Map.Entry<Planet, CircleNode> entry : circles.entrySet()) {
            Point2D.Double point = mapToImage(entry.getKey().getPosition());
            double r = 0;
            double step = entry.getValue().r;
            for (int i = entry.getValue().n; i > 0; --i) {
                r += step;
                drawCircle(g2, point, r * factor);
            }
        }
    }

    private void drawPlanets(Graphics2D g2) {
        initFont();
        g2.setFont(font);
        boolean d3 = OGV.getOption(OGV.MapMode.D3);
        for (Planet p : game.getPlanets()) drawPlanet(g2, p, d3);
    }

    private void drawPlanet(Graphics2D g2, Planet p, boolean d3) {
        PlanetNode n = new PlanetNode(p);
        g2.setColor(n.c);
        g2.setStroke(NORMAL_STROKE);
        if (d3) fillCircle3D(g2, n.pos, n.r); else fillCircle(g2, n.pos, n.r);
        drawPlanetInfo(g2, n);
    }

    private void drawPlanetInfo(Graphics2D g2, PlanetNode n) {
        if (font == null) return;
        drawLowerPlanetInfo(g2, n);
        drawUpperPlanetInfo(g2, n);
    }

    private void drawLowerPlanetInfo(Graphics2D g2, PlanetNode n) {
        if (!OGV.getOption(OGV.MapMode.PLANET_NAMES)) return;
        if (!OGV.getOption(OGV.MapMode.PLANET_NUMS)) drawLowerString(g2, n.pos, n.r, n.p.getName()); else drawLowerString(g2, n.pos, n.r, "#" + n.p.getNumber());
    }

    private void drawUpperPlanetInfo(Graphics2D g2, PlanetNode n) {
        String s = upperPlanetInfo.get(n.p);
        if (s != null) drawUpperString(g2, n.pos, n.r, s);
    }

    private void initFont() {
        float fontSize = Math.min((float) Math.floor(density * PLANET_INFO_FONT_FACTOR), PLANET_INFO_FONT.getSize());
        if (fontSize < PLANET_INFO_MIN_FONT_SIZE) font = null; else font = PLANET_INFO_FONT.deriveFont(fontSize);
    }

    private void drawSelectedPlanets(Graphics2D g2) {
        for (Planet p : data.selectedPlanets) drawSelectedPlanet(g2, new PlanetNode(p));
    }

    private void drawSelectedPlanet(Graphics2D g2, PlanetNode n) {
        double dr = Math.max(n.r + SELECTION_SPACE, MIN_SELECTION_RADIUS);
        g2.setColor(n.c);
        g2.setStroke(SELECTION_STROKE);
        g2.draw(new Rectangle2D.Double(n.pos.x - dr, n.pos.y - dr, 2 * dr - 1, 2 * dr - 1));
    }

    private void drawDistance(Graphics2D g2) {
        if (data.mouse == null || data.mode != MapViewerState.Mode.DISTANCE) return;
        g2.setColor(OGV.getColor(MapColors.WORK_COLOR));
        g2.setStroke(NORMAL_STROKE);
        drawLineToCursor(g2, data.distancePosition);
    }

    private void drawCursorInfo(Graphics2D g2) {
        if (data.mouse == null) return;
        Point2D.Double point = screenToImage(data.mouse);
        SpaceObject o = getCurrentObject();
        if (o == null) return;
        MapGeometry geometry = game.geometry;
        List<String> text = new ArrayList<String>();
        if (o instanceof Planet) {
            Planet p = (Planet) o;
            text.add(p.getRaceName());
            text.add(p.getFullName());
            if (data.mode == MapViewerState.Mode.DISTANCE) {
                double dist = geometry.distance(data.distancePosition, p.getPosition());
                if (dist > Utils.EPSILON) text.add(Utils.d3(dist));
            } else if (data.control != null) data.control.appendPlanetInfo(text, p, geometry);
            drawCursorInfo(g2, point, text, OGV.getColor(MapColors.PLANET_CURSOR_INFO_BACKGROUND_COLOR));
            return;
        }
        Position pos = o.getPosition();
        if (o instanceof Group) {
            Group gr = (Group) o;
            Planet src = gr.getSource();
            Planet dst = gr.getDestination();
            double dist1 = geometry.distance(pos, dst.getPosition());
            double dist2 = geometry.distance(pos, src.getPosition());
            int eta = gr.getEta();
            int count = 0;
            double mass = 0;
            for (Group g1 : game.getInSpaceGroups()) if (g1.getSource() == src && g1.getDestination() == dst && g1.getPosition().equals(pos)) {
                count += g1.getSize();
                mass += g1.getMass();
            }
            text.add(count + " " + (count > 1 ? "ships" : "ship") + ' ' + Utils.d2(mass) + " mass");
            text.add(src.getName() + " → " + dst.getName());
            text.add(Utils.d3(dist2) + " → " + Utils.d3(dist1) + " [" + eta + ']');
        } else if (o instanceof IncomingGroup) {
            IncomingGroup gr = (IncomingGroup) o;
            Planet src = gr.getSource();
            Planet dst = gr.getDestination();
            double dist1 = geometry.distance(pos, dst.getPosition());
            double dist2 = geometry.distance(pos, src.getPosition());
            int eta = gr.getEta();
            double mass = 0;
            for (IncomingGroup g1 : game.getIncomingGroups()) if (g1.getSource() == src && g1.getDestination() == dst && g1.getPosition().equals(pos)) {
                mass += g1.getMass();
            }
            text.add(Utils.d2(mass) + " mass");
            text.add(src.getName() + " → " + dst.getName());
            text.add(Utils.d3(dist2) + " → " + Utils.d3(dist1) + " [" + eta + ']');
        } else if (o instanceof UnidentifiedGroup) {
            UnidentifiedGroup gr = (UnidentifiedGroup) o;
            Planet src = gr.getSource();
            Planet dst = gr.getDestination();
            if (dst != null) {
                double dist1 = geometry.distance(pos, dst.getPosition());
                double dist2 = geometry.distance(pos, src.getPosition());
                text.add(src.getName() + " ↔ " + dst.getName());
                text.add(Utils.d3(dist2) + " ↔ " + Utils.d3(dist1));
            }
        }
        if (data.mode == MapViewerState.Mode.DISTANCE) {
            double dist = geometry.distance(data.distancePosition, pos);
            if (dist > Utils.EPSILON) text.add(Utils.d3(dist));
        }
        drawCursorInfo(g2, point, text, OGV.getColor(MapColors.GROUP_CURSOR_INFO_BACKGROUND_COLOR));
    }

    private void drawSelectionBorder(Graphics2D g2) {
        if (data.mouse == null || data.selectionStartPosition == null) return;
        Point2D.Double point1 = mapToImage(data.selectionStartPosition);
        Point2D.Double point2 = screenToImage(data.mouse);
        g2.setColor(OGV.getColor(MapColors.RECT_SELECTION_COLOR));
        g2.setStroke(NORMAL_STROKE);
        double minX = Math.min(point1.x, point2.x);
        double minY = Math.min(point1.y, point2.y);
        double maxX = Math.max(point1.x, point2.x);
        double maxY = Math.max(point1.y, point2.y);
        g2.draw(new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY));
    }

    public void drawCircles(Graphics2D g2, Position center, double speed) {
        Position pos = screenToMap(data.mouse);
        Point2D.Double point = mapToImage(center);
        g2.setStroke(NORMAL_STROKE);
        double dist = game.geometry.distance(center, pos);
        double r = 0;
        do {
            r += speed;
            drawCircle(g2, point, r * factor);
        } while (r < dist);
    }

    private static void drawGroup(Graphics2D g2, Point2D.Double pos) {
        g2.fill(new Ellipse2D.Double(pos.x - GROUP_SIZE / 2, pos.y - GROUP_SIZE / 2, GROUP_SIZE, GROUP_SIZE));
    }

    private static void drawTick(Graphics2D g2, Point2D.Double pos) {
        g2.fill(new Ellipse2D.Double(pos.x - TICK_SIZE / 2, pos.y - TICK_SIZE / 2, TICK_SIZE, TICK_SIZE));
    }

    public void drawTick(Graphics2D g2, Position pos) {
        drawTick(g2, mapToImage(pos));
    }

    private static void drawLine(Graphics2D g2, double x1, double y1, double x2, double y2) {
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    public void drawLineToCursor(Graphics2D g2, Position pos) {
        g2.draw(new Line2D.Double(mapToImage(pos), screenToImage(data.mouse)));
    }

    public void drawGameLine(Graphics2D g2, Position pos1, Position pos2) {
        Point2D.Double p1 = mapToImage(pos1);
        Point2D.Double p2 = mapToImage(pos2);
        Position rel = game.geometry.relative(imageToMap(p1), pos2);
        double dx = rel.getX() * factor;
        double dy = rel.getY() * factor;
        drawBoundedLine(g2, new Line2D.Double(p1.x, p1.y, p1.x + dx, p1.y + dy));
        if (Math.abs(p2.x - p1.x - dx) >= 0.1 || Math.abs(p2.y - p1.y - dy) >= 0.1) drawBoundedLine(g2, new Line2D.Double(p2.x - dx, p2.y - dy, p2.x, p2.y));
    }

    private static void drawBoundedLine(Graphics2D g2, Line2D.Double line) {
        Line2D l = Intersections.intersection(line, g2.getClipBounds());
        if (l != null) g2.draw(l);
    }

    private static void drawCircle(Graphics2D g2, Point2D.Double p, double d) {
        g2.draw(new Ellipse2D.Double(p.x - d, p.y - d, 2 * d, 2 * d));
    }

    private static void fillCircle(Graphics2D g2, Point2D.Double p, double d) {
        g2.fill(new Ellipse2D.Double(p.x - d, p.y - d, 2 * d, 2 * d));
    }

    private static void fillCircle3D(Graphics2D g2, Point2D.Double p, double r) {
        Point2D focus = new Point2D.Double(p.x - r * 0.6, p.y - r * 0.4);
        float[] fractions = { 0.5f, 1 };
        Color c = g2.getColor();
        Color[] colors = { c, c.darker().darker() };
        g2.setPaint(new RadialGradientPaint(p, (float) r, focus, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE));
        fillCircle(g2, p, r);
        g2.setColor(c);
    }

    public Rectangle getMapBoundary() {
        int x = (getWidth() - mapWidth) / 2;
        int y = (getHeight() - mapHeight) / 2;
        return new Rectangle(x, y, mapWidth, mapHeight);
    }

    private void drawGrid(Graphics2D g2) {
        if (!OGV.getOption(OGV.MapMode.GRID)) return;
        g2.setColor(OGV.getColor(MapColors.GRID_COLOR));
        g2.setStroke(NORMAL_STROKE);
        double step = gs * factor / GRID_STEPS;
        double xmax = mapWidth / 2.0;
        double ymax = mapHeight / 2.0;
        double x0 = -gc.getX() * factor;
        int i0 = (int) Math.floor((-xmax - x0) / step);
        for (int i = i0; ; ++i) {
            double x = x0 + i * step;
            drawLine(g2, x, -ymax, x, ymax);
            if (x >= xmax) break;
        }
        double y0 = -gc.getY() * factor;
        int j0 = (int) Math.floor((-ymax - y0) / step);
        for (int j = j0; ; ++j) {
            double y = y0 + j * step;
            drawLine(g2, -xmax, y, xmax, y);
            if (y >= ymax) break;
        }
        g2.setFont(new Font(null, Font.PLAIN, (int) (step / 3)));
        x0 += step / 2;
        y0 += step / 2;
        char[] label = new char[2];
        for (int i = i0; ; ++i) {
            double x = x0 + i * step;
            label[0] = (char) ((int) 'A' + (i + GRID_STEPS) % GRID_STEPS);
            for (int j = j0; ; ++j) {
                double y = y0 + j * step;
                label[1] = (char) ((int) '0' + (2 * GRID_STEPS - 1 - j) % GRID_STEPS);
                drawCenterString(g2, new String(label), x, y);
                if (y >= ymax) break;
            }
            if (x >= xmax) break;
        }
    }

    private static void drawCenterString(Graphics2D g2, String s, double x, double y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(s, (float) (x - fm.stringWidth(s) * 0.5), (float) (y + (fm.getAscent() - fm.getDescent()) * 0.5));
    }

    private static void drawLowerString(Graphics2D g2, Point2D.Double p, double d, String s) {
        drawLowerString(g2, p, d, Utils.splitLines(s));
    }

    private static void drawLowerString(Graphics2D g2, Point2D.Double p, double d, String[] text) {
        if (text == null || text.length == 0) return;
        FontMetrics fm = g2.getFontMetrics();
        int w = 0;
        for (String line : text) w = Math.max(w, fm.stringWidth(line));
        float x = (float) p.x;
        float y = (float) (p.y + d + fm.getAscent());
        for (String line : text) {
            g2.drawString(line, x - fm.stringWidth(line) * 0.5f, y);
            y += fm.getHeight();
        }
    }

    private static void drawUpperString(Graphics2D g2, Point2D.Double p, double d, String s) {
        drawUpperString(g2, p, d, Utils.splitLines(s));
    }

    private static void drawUpperString(Graphics2D g2, Point2D.Double p, double d, String[] text) {
        if (text == null || text.length == 0) return;
        FontMetrics fm = g2.getFontMetrics();
        int w = 0;
        for (String line : text) w = Math.max(w, fm.stringWidth(line));
        float x = (float) p.x;
        float y = (float) (p.y - fm.getHeight() * (text.length - 1) - fm.getDescent() - d);
        for (String line : text) {
            g2.drawString(line, x - fm.stringWidth(line) * 0.5f, y);
            y += fm.getHeight();
        }
    }

    private static void drawCursorInfo(Graphics2D g2, Point2D.Double p, List<String> text, Color color) {
        if (text.isEmpty()) return;
        g2.setFont(CURSOR_INFO_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int w = 0;
        for (String line : text) w = Math.max(w, fm.stringWidth(line));
        w += 2 * CURSOR_INFO_PADDING;
        int h = fm.getHeight() * text.size() - fm.getLeading() + 2 * CURSOR_INFO_PADDING;
        int x = (int) (p.x - w * 0.5);
        int y = (int) p.y + CURSOR_INFO_OFFSET;
        g2.setColor(color);
        g2.fillRoundRect(x, y, w, h, 2 * CURSOR_INFO_PADDING, 2 * CURSOR_INFO_PADDING);
        g2.setColor(OGV.getColor(MapColors.CURSOR_INFO_COLOR));
        x = (int) p.x;
        y += fm.getAscent() + CURSOR_INFO_PADDING;
        for (String line : text) {
            g2.drawString(line, x - fm.stringWidth(line) * 0.5f, y);
            y += fm.getHeight();
        }
    }

    public void update() {
        needInit = true;
    }

    private void initLists() {
        if (needInit) {
            addLaunchedGroups();
            setUpperPlanetInfo();
            needInit = false;
            invalidateImage();
        }
    }

    private void setUpperPlanetInfo() {
        upperPlanetInfo = OGV.getPlanetInfo();
    }

    private void addLaunchedGroups() {
        launchedGroups.clear();
        for (Group gr : game.getGroups()) if (gr.isLaunched()) {
            launchedGroups.add(gr);
        }
    }

    private double radius(double size) {
        if (size <= 0) return MIN_PLANET_RADIUS;
        double r = Math.sqrt(size) * (MAX_PLANET_RADIUS / Math.sqrt(MAX_PLANET_SIZE)) * factor2;
        return Math.max(r, MIN_PLANET_RADIUS);
    }

    private double scale(double r) {
        if (density < MAX_DENSITY) r *= density / MAX_DENSITY;
        return r;
    }

    public Point2D.Double mapToImage(Position pos) {
        Position rel = game.geometry.relative(gc, pos);
        return new Point2D.Double(rel.getX() * factor, rel.getY() * factor);
    }

    public Point2D.Double mapToScreen(Position pos) {
        Position rel = game.geometry.relative(gc, pos);
        return new Point2D.Double(rel.getX() * factor + getWidth() * 0.5, rel.getY() * factor + getHeight() * 0.5);
    }

    public Position imageToMap(Point2D.Double point) {
        Position rel = new Position(point.x / factor, point.y / factor);
        return game.geometry.absolute(gc, rel);
    }

    public Position screenToMap(Point2D.Double point) {
        Position rel = new Position((point.x - getWidth() * 0.5) / factor, (point.y - getHeight() * 0.5) / factor);
        return game.geometry.absolute(gc, rel);
    }

    private Point2D.Double screenToImage(Point2D.Double mouse) {
        return new Point2D.Double(mouse.x - 0.5 * getWidth(), mouse.y - 0.5 * getHeight());
    }

    public Planet getCurrentPlanet() {
        return getCurrentPlanet(data.mouse);
    }

    public Planet getCurrentPlanet(Point2D.Double point) {
        if (point == null) return null;
        Planet p = getNearestObject(point, game.getPlanets(), null);
        if (p == null) return null;
        if (point.distance(mapToScreen(p.getPosition())) > Math.max(radius(p.getSize()) + CURRENT_PLANET_EXTRA_DISTANCE, CURRENT_PLANET_MIN_DISTANCE)) return null;
        return p;
    }

    public SpaceObject getCurrentGroup() {
        return getCurrentGroup(data.mouse);
    }

    private SpaceObject getCurrentGroup(Point2D.Double point) {
        if (point == null || !OGV.getOption(OGV.MapMode.IN_SPACE)) return null;
        SpaceObject o = getNearestObject(point, game.getIncomingGroups(), null);
        o = getNearestObject(point, game.getUnidentifiedGroups(), o);
        o = getNearestObject(point, game.getInSpaceGroups(), o);
        if (o == null || point.distance(mapToScreen(o.getPosition())) > CURRENT_PLANET_MIN_DISTANCE) return null;
        return o;
    }

    public SpaceObject getCurrentObject() {
        if (data.mouse == null) return null;
        SpaceObject o = getCurrentPlanet();
        if (!OGV.getOption(OGV.MapMode.IN_SPACE)) return o;
        o = getNearestObject(data.mouse, game.getIncomingGroups(), o);
        o = getNearestObject(data.mouse, game.getUnidentifiedGroups(), o);
        o = getNearestObject(data.mouse, game.getInSpaceGroups(), o);
        if (o == null || data.mouse.distance(mapToScreen(o.getPosition())) > CURRENT_PLANET_MIN_DISTANCE) return null;
        return o;
    }

    public <T extends SpaceObject> T getNearestObject(Point2D.Double point, Collection<? extends T> obj, T o1) {
        if (!getMapBoundary().contains(point)) return null;
        double d1;
        if (o1 != null) d1 = point.distanceSq(mapToScreen(o1.getPosition())); else d1 = game.getSize() * game.getSize();
        for (T o : obj) {
            if (o.getPosition() == null) continue;
            double d2 = point.distanceSq(mapToScreen(o.getPosition()));
            if (d2 < d1) {
                o1 = o;
                d1 = d2;
            }
        }
        return o1;
    }

    private static Color getColor(Planet planet) {
        if (planet.isUnidentified()) return OGV.getColor(MapColors.UNIDENTIFIED_PLANET_COLOR);
        if (planet.isUninhabited()) return OGV.getColor(MapColors.UNINHABITED_PLANET_COLOR);
        return OGV.getColor(planet.getOwner());
    }

    private static Color getColor(CargoType cargo) {
        switch(cargo) {
            case COL:
                return OGV.getColor(MapColors.COL_ROUTE_COLOR);
            case CAP:
                return OGV.getColor(MapColors.CAP_ROUTE_COLOR);
            case MAT:
                return OGV.getColor(MapColors.MAT_ROUTE_COLOR);
            case EMP:
                return OGV.getColor(MapColors.EMP_ROUTE_COLOR);
            default:
                return OGV.getColor(MapColors.DEFAULT_ROUTE_COLOR);
        }
    }

    public boolean hasCircles(Planet p) {
        return circles.containsKey(p);
    }

    public void setCircles(Planet p, double radius, int count) {
        circles.put(p, new CircleNode(radius, count));
    }

    public void removeCircles(Planet p) {
        circles.remove(p);
    }

    private final class PlanetNode {

        final Planet p;

        final Point2D.Double pos;

        final double r;

        final Color c;

        PlanetNode(Planet p) {
            this.p = p;
            pos = mapToImage(p.getPosition());
            r = radius(p.getSize());
            c = getColor(p);
        }
    }

    private final class CircleNode {

        final double r;

        final int n;

        CircleNode(double r, int n) {
            this.r = r;
            this.n = n;
        }
    }
}
