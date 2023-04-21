package sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives;

import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.base.components.properties.annotations.Property;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.Connectable;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.style.PrimitivaStyl;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.style.StylyVyplne;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.IOSchema;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.LinkLine;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.Prepojenie;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *  Primitíva predstavujúca lomenú čiaru
 *
 * @author Ján Čabala
 */
public class PolyLine extends Primitiva implements Connectable, LinkLine {

    /**
     * Zoznam  bodov lomenej čiary, s výnimkou prvého, ten predstavuje pozícia
     */
    @Property(name = "Body", title = "Zoznam Bodov", hint = "Zoznam všetkých bodov lomenej čiary, okrem počiatočného, " + "ktorý je reprezentovaný pozíciou")
    public List<Point.Double> body;

    /**
     * Zoznam lomených čiar, ktoré sa napájajú na túto čiaru
     */
    private List<PolyLine> polyRefs;

    /**
     * Zoznam identifikátorov napájacieho bodu pre každú z napájaných
     * čiar (polyRefs)<br/>
     * {@code true} - napájaná čiara sa napája na túto čiaru svojim prvým bodom</br>
     * {@code false} - napájaná čiara sa napája na túto čiaru svojim posledným
     * bodom</br>
     */
    private List<Boolean> isPolyStarts;

    private int id;

    private Prepojenie prepojenie;

    /**
     * Vytvorí novú lomenú čiaru
     *
     * @param nazov názov lomenej čiary
     * @param styl štýl lomenej čiary
     */
    public PolyLine(String nazov, PrimitivaStyl styl) {
        super(new Rectangle2D.Double(), new GeneralPath(GeneralPath.WIND_EVEN_ODD), nazov, styl);
        styl.stylVyplne = StylyVyplne.ZIADNA;
        body = new ArrayList<Point.Double>();
        resizable = false;
        polyRefs = new ArrayList<PolyLine>();
        isPolyStarts = new ArrayList<Boolean>();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Shape getShape() {
        regenerateShape();
        return initShape;
    }

    /**
     * {@inheritDoc}
     *
     * @param px {@inheritDoc}
     * @param py {@inheritDoc}
     */
    @Override
    public void move(double px, double py) {
        super.move(px, py);
        for (Point.Double b : body) {
            b.x += px;
            b.y += py;
        }
    }

    /**
     * Obnovenie tvaru lomenej čiary
     */
    public void regenerateShape() {
        GeneralPath path = (GeneralPath) initShape;
        path.reset();
        path.moveTo(pozicia.x, pozicia.y);
        for (Point.Double bod : body) {
            path.lineTo(bod.x, bod.y);
        }
    }

    /**
     * Vracia zoznam všetkých bodov lomenej čiary vrátane pozície ako prvého bodu
     *
     * @return zoznam všetkých bodov
     */
    public List<Point.Double> getVsetkyBody() {
        List<Point.Double> out = new ArrayList<Double>(body);
        out.add(0, pozicia);
        return out;
    }

    public void setBody(List<Double> body) {
        if (body.size() == 0) {
            throw new IllegalArgumentException("Nie je možné pridať prázdny zoznam bodov");
        }
        pozicia = body.get(0);
        this.body = new ArrayList<Double>(body);
        this.body.remove(pozicia);
        regenerateShape();
    }

    public void setBody(Point2D[] body) {
        List<Double> bodList = new ArrayList<Double>();
        for (Point2D bod : body) {
            bodList.add(new Double(bod.getX(), bod.getY()));
        }
        setBody(bodList);
    }

    /**
     * Vráti index segmentu lomenej čiary (počiatočného  bodu segmentu)
     * podľa zadaného bodu
     *
     * @param point bod
     * @param tolerancia maximálna odchýlka od zadaného bodu
     * @return index segmentu alebo -1 ak nebol žiaden nájdený
     */
    public int getSegmentIndex(Point2D point, double tolerancia) {
        List<Point.Double> by = new ArrayList<Point.Double>(body);
        by.add(0, pozicia);
        if (by.size() > 1) {
            for (int i = 1; i < by.size(); i++) {
                Point.Double b1 = by.get(i - 1);
                Point.Double b2 = by.get(i);
                if (new Line2D.Double(b1.x, b1.y, b2.x, b2.y).intersects(point.getX() - tolerancia / 2, point.getY() - tolerancia / 2, tolerancia, tolerancia)) {
                    return i - 1;
                }
            }
        }
        return -1;
    }

    /**
     * Vracia bod pre prichytenie inej lomenej čiary podľa zadaného bodu.
     * Vrátený bod je vlastne bod zadaný s upravenou buď x-ovou súradnicou alebo
     * y-ovou súradnicou podľa potreby
     *
     * @param point bod
     * @param tolerancia maximálna odchýlka od zadaného bodu
     * @return bod prichytenia
     */
    public Point2D.Double getSnapPoint(Point2D point, double tolerancia) {
        int segment = getSegmentIndex(point, tolerancia);
        Point.Double p1 = pozicia;
        Point.Double p2 = body.get(segment);
        if (segment > 0) {
            p1 = body.get(segment - 1);
        }
        if (p1.x == p2.x) {
            Logger.getLogger().addDebug("Upravené y, p1: %s p2: %s point: %s", p1, p2, point);
            return new Point2D.Double(p1.x, point.getY());
        } else {
            Logger.getLogger().addDebug("Upravené x, p1: %s p2: %s point: %s", p1, p2, point);
            return new Point2D.Double(point.getX(), p1.y);
        }
    }

    /**
     * Vytvorenie klonu lomenej čiary
     *
     * @return klon lomenej čiary
     * @throws java.lang.CloneNotSupportedException
     * ak nastane chyba pri klonovaní lomenej čiary
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        PolyLine poly = new PolyLine(nazov, styl);
        poly.pozicia = (Point2D.Double) pozicia.clone();
        poly.body = new ArrayList<Double>();
        for (Point2D.Double point : body) {
            poly.body.add((Point2D.Double) point.clone());
        }
        poly.isPolyStarts = new ArrayList<Boolean>(isPolyStarts);
        poly.polyRefs = new ArrayList<PolyLine>(polyRefs);
        return poly;
    }

    /**
     * Vracia zoznam lomených čiar napájajúcich sa na túto čiaru
     *
     * @return zoznam lomených čiar
     */
    public List<PolyLine> getPolyRefs() {
        return polyRefs;
    }

    /**
     * Vracia zoznam identifikátorov napájacieho bodu pre každú z napájaných
     * čiar (polyRefs)<br/>
     * {@code true} - napájaná čiara sa napája na túto čiaru svojim prvým bodom</br>
     * {@code false} - napájaná čiara sa napája na túto čiaru svojim posledným
     * bodom</br>
     * @return identifikátorov napájacieho bodu
     */
    public List<Boolean> getIsPolyStarts() {
        return isPolyStarts;
    }

    @Override
    public boolean isSelectionPoint(Point2D p) {
        return getSegmentIndex(p, 5) > -1;
    }

    public boolean isObsadene() {
        return false;
    }

    public boolean isIO() {
        return false;
    }

    public boolean canBeConnectedWith(Connectable connectable) {
        return !connectable.isObsadene() && (!connectable.isIO() || ((IOSchema) connectable).isVstup());
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return nazov;
    }

    public Primitiva getPrimitiva() {
        return this;
    }

    public Prepojenie getPrepojenie() {
        return prepojenie;
    }

    public void setPrepojenie(Prepojenie prepojenie) {
        this.prepojenie = prepojenie;
    }

    public String getNazov() {
        return nazov;
    }
}
