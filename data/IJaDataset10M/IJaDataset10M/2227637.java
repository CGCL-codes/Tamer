package sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka;

import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.base.components.properties.annotations.Property;
import sk.tuke.ess.editor.simulation.simeditbase.Selectable;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.*;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.IOSchema;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Reprezentuje značku v rámci shémy. Doplňuje zančku {@link sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.Znacka}
 * o atribúty potrebné pre značku schémy.
 *
 * @author Ján Čabala
 */
public class ZnackaSchema extends Znacka implements Selectable, Serializable {

    /**
    * Unikátny identifikátor značky v rámci schémy
    */
    private int id;

    /**
     * Meno značky v rámci schémy
     */
    @Property(name = "Meno", title = "Názov značky v rámci schémy", hint = "Reprezentuje názov značky v rámci schémy", callback = "updatePopisText")
    public String meno;

    public Point.Double pozicia;

    /**
    * Grafická reprezentácia popisného textu značky
    */
    public Text popisText;

    /**
     * Počet vstupov do značky
     */
    @Property(name = "Počet vstupov", hint = "Počet vstupov do značky", title = "Počet vstupov", callback = "updatePocetIO")
    public int pocetVstupov = 0;

    /**
     * Počet výstupov zo značky
     */
    @Property(name = "Počet výstupov", hint = "Počet výstupov do značky", title = "Počet výstupov", callback = "updatePocetIO")
    public int pocetVystupov = 0;

    /**
     * Uhol otočenia značky v rámci shémy (zatiaľ sa nevyužíva)
     */
    public double uhol = 0;

    /**
     * Identifikátor označenia značky
     */
    private boolean selected = false;

    /**
     * Posledný priradený identifikátor
     */
    protected int lastID = 0;

    private Map<IOPrimitiva, IOSchema> ioMap = new HashMap<IOPrimitiva, IOSchema>();

    /**
     * Vytvorenie novej značky schémy zo značky z knižnice.
     * Značka schémy preberie hlavne grafický popis zo značky z knižnice a
     * doplní ju o potrebné atribúty pre značku schémy.
     *
     * @param znacka značka z knižnice
     */
    public ZnackaSchema(Znacka znacka) {
        Map<Primitiva, Primitiva> clonedToOriginalMap = new HashMap<Primitiva, Primitiva>(znacka.getPrimitivy().size());
        this.nazov = znacka.nazov;
        this.popis = znacka.popis;
        this.primitivy = new ArrayList<Primitiva>();
        for (Primitiva p : znacka.getPrimitivy()) {
            try {
                Primitiva cloned = (Primitiva) p.clone();
                clonedToOriginalMap.put(cloned, p);
                this.primitivy.add(cloned);
            } catch (CloneNotSupportedException e) {
                Logger.getLogger().addWarning("Primitíva %s bola vynechaná pri vkladaní značky %s", p.nazov, nazov);
            }
        }
        this.nazovKategorie = znacka.nazovKategorie;
        this.vstupMinMax = new Point(znacka.vstupMinMax.x, znacka.vstupMinMax.y);
        this.vystupMinMax = new Point(znacka.vystupMinMax.x, znacka.vystupMinMax.y);
        boolean isFromZnackaSchema = znacka instanceof ZnackaSchema;
        for (int i = 0; i < primitivy.size(); i++) {
            Primitiva p = primitivy.get(i);
            if (p instanceof IOPrimitiva && !ioMap.containsKey(clonedToOriginalMap.get(p))) {
                IOPrimitiva ioPrimitiva = (IOPrimitiva) p;
                IOSchema ioSchema;
                if (isFromZnackaSchema) {
                    ioSchema = ((ZnackaSchema) znacka).ioMap.get(clonedToOriginalMap.get(ioPrimitiva));
                    PolyLine polyref = ioSchema.getPolyRef();
                    ioSchema = new IOSchema(ioPrimitiva, ioSchema.getId(), ioSchema.prepojenie);
                    ioSchema.setPolyRef(polyref);
                } else {
                    ioSchema = new IOSchema(ioPrimitiva, ++lastID);
                }
                ioMap.put(ioPrimitiva, ioSchema);
            }
        }
        this.logicClassName = znacka.getLogicClassName();
        this.dialogClassName = znacka.getDialogClassName();
        if (znacka instanceof ZnackaSchema) {
            ZnackaSchema zch = (ZnackaSchema) znacka;
            this.id = zch.id;
            this.meno = zch.meno;
            this.popisText = new Text(zch.popisText.text);
            this.pozicia = new Point2D.Double(zch.pozicia.getX(), zch.pozicia.getY());
            this.logicClassName = zch.logicClassName;
            this.dialogClassName = zch.dialogClassName;
        } else {
            this.pozicia = new Point2D.Double();
        }
    }

    public void updatePocetIO() {
        updatePocetIO(false);
    }

    /**
     * Upraviť počet vstupov a výstupov
     */
    public void updatePocetIO(boolean ioInit) {
        List<Primitiva> vstupy = new ArrayList<Primitiva>();
        List<Primitiva> vystupy = new ArrayList<Primitiva>();
        Logger l = Logger.getLogger();
        for (Primitiva p : primitivy) {
            if (p instanceof Vstup) vstupy.add(p); else if (p instanceof Vystup) vystupy.add(p);
        }
        if (ioInit) {
            pocetVstupov = vstupy.size();
            pocetVystupov = vystupy.size();
        } else {
            if (vstupMinMax.y > -1 && pocetVstupov > vstupMinMax.y) {
                pocetVstupov = vstupMinMax.y;
                l.addWarning("Počet <b>vstupov</b> značky <b>%s</b> môže byť maximálne <b>%d</b>", meno, vstupMinMax.y);
            }
            if (pocetVstupov < vstupMinMax.x) {
                pocetVstupov = vstupMinMax.x;
                l.addWarning("Počet <b>vstupov</b> značky <b>%s</b> môže byť minimálne <b>%d</b>", meno, vstupMinMax.x);
            }
            if (vystupMinMax.y > -1 && pocetVystupov > vystupMinMax.y) {
                pocetVystupov = vystupMinMax.y;
                l.addWarning("Počet <b>výstupov</b> značky <b>%s</b> môže byť maximálne <b>%d</b>", meno, vystupMinMax.y);
            }
            if (pocetVystupov < vystupMinMax.x) {
                pocetVystupov = vystupMinMax.x;
                l.addWarning("Počet <b>výstupov</b> značky <b>%s</b> môže byť minimálne <b>%d</b>", meno, vystupMinMax.x);
            }
            if (pocetVstupov < vstupy.size()) {
                removeIOs(vstupy, pocetVstupov, true);
            } else if (pocetVstupov > vstupy.size()) {
                addIOs(vstupy, pocetVstupov, true);
            }
            if (pocetVystupov < vystupy.size()) {
                removeIOs(vystupy, pocetVystupov, false);
            } else if (pocetVystupov > vystupy.size()) {
                addIOs(vystupy, pocetVystupov, false);
            }
        }
    }

    /**
     * Odstrániť vstupy alebo výstupy
     *
     * @param ios zoznam aktuálnych vstupov alebo výstupov
     * @param newCount nový počet vstupov alebo výstupov
     * @param isVstup {@code true} ak sa jedná o vstupy, {@code false} ak o výstupy
     */
    private void removeIOs(List<Primitiva> ios, int newCount, boolean isVstup) {
        int ostava = ios.size() - newCount;
        for (int i = 0; i < ios.size(); i++) {
            IOSchema ioSchema = ioMap.get(ios.get(i));
            if (!ioSchema.isObsadene()) {
                ios.remove(ioSchema.getIOPrimitiva());
                removeIO(ioSchema);
                ostava--;
            }
            if (ostava < 1) return;
        }
        String ioString;
        if (isVstup) {
            ioString = "vstup";
            pocetVstupov += ostava;
        } else {
            ioString = "výstup";
            pocetVystupov += ostava;
        }
        if (ostava > 1) ioString += "y";
        Logger.getLogger().addWarning("Nepodarilo sa odobrať %d %2$s. Najprv %2$s uvoľnite.", ostava, ioString);
    }

    public void removeIO(IOSchema io) {
        IOPrimitiva ioPrimitiva = io.getIOPrimitiva();
        if (ioPrimitiva != null) {
            ioMap.remove(ioPrimitiva);
            primitivy.remove(ioPrimitiva);
        }
    }

    public void addIO(IOSchema io) {
        IOPrimitiva ioPrimitiva = io.getIOPrimitiva();
        if (ioPrimitiva != null) {
            putUnnamed(ioPrimitiva);
            ioMap.put(ioPrimitiva, io);
        }
    }

    /**
     * Pridať vstupy alebo výstupy
     *
     * @param ios zoznam aktuálnych vstupov alebo výstupov
     * @param newCount nový počet vstupov alebo výstupov
     * @param isVstup {@code true} ak sa jedná o vstupy, {@code false} ak o výstupy
     */
    private void addIOs(List<Primitiva> ios, int newCount, boolean isVstup) {
        Collections.sort(ios, new PrimitivaPozYComparator());
        List<Point2D.Double> regiony = new ArrayList<Point2D.Double>();
        List<Integer> pocty = new ArrayList<Integer>();
        Rectangle2D bounds = getBounds2D();
        double vh;
        if (isVstup) {
            vh = new Vstup().getBounds2D().getHeight();
        } else {
            vh = new Vystup().getBounds2D().getHeight();
        }
        double h = bounds.getHeight() - ios.size() * vh;
        double lastY = 0;
        int najvacsi = -1;
        int naPridanie = newCount - ios.size();
        int naPridanieCelkom = naPridanie;
        for (int i = 0; i <= ios.size(); i++) {
            double pozY;
            if (i == ios.size()) {
                pozY = bounds.getHeight();
            } else {
                pozY = ios.get(i).pozicia.y;
            }
            double vyska = pozY - lastY;
            regiony.add(new Point2D.Double(lastY, pozY));
            if (najvacsi == -1) {
                najvacsi = i;
            } else {
                Point2D.Double n = regiony.get(najvacsi);
                if (vyska > n.y - n.x) {
                    najvacsi = i;
                }
            }
            int pridat = (int) Math.round(vyska / h * naPridanieCelkom - 0.1);
            pocty.add(pridat);
            naPridanie -= pridat;
            lastY = pozY + vh;
            if (naPridanie <= 0) break;
        }
        if (naPridanie > 0) {
            Integer p = pocty.get(najvacsi);
            pocty.remove(najvacsi);
            pocty.add(najvacsi, p + naPridanie);
        }
        int x;
        if (isVstup) {
            x = (int) (bounds.getMinX() - pozicia.x);
            if (ios.size() == 0) {
                x -= new Vstup().getBounds2D().getWidth();
            }
        } else {
            x = (int) (bounds.getMaxX() - pozicia.x);
            if (ios.size() > 0) {
                x -= new Vystup().getBounds2D().getWidth();
            }
        }
        Point ioPoint = getNewIOIDs(isVstup);
        int newIOID = ioPoint.x;
        int newIOSchemaID = ioPoint.y;
        for (int i = 0; i < regiony.size(); i++) {
            int pridat = pocty.get(i).intValue();
            Point2D region = regiony.get(i);
            double step = (region.getY() - region.getX()) / (pridat + 1);
            for (int j = 0; j < pridat; j++) {
                Point pos = new Point(x, (int) (region.getX() + (j + 1) * step));
                IOSchema ioSchema = new IOSchema((isVstup ? new Vstup("Vstup", pos) : new Vystup("Výstup", pos)), newIOSchemaID++, null);
                ioSchema.getIOPrimitiva().ioID = newIOID++;
                addIO(ioSchema);
            }
        }
        lastID = newIOSchemaID;
    }

    private Point getNewIOIDs(boolean isVstup) {
        int ioID = 0;
        int ioSchemaID = lastID;
        for (IOSchema ioSchema : ioMap.values()) {
            if (ioSchema.isVstup() == isVstup && ioSchema.getIOPrimitiva().ioID > ioID) {
                ioID = ioSchema.getIOPrimitiva().ioID;
            }
            if (ioSchema.getId() > ioSchemaID) {
                ioSchemaID = ioSchema.getId();
            }
        }
        return new Point(++ioID, ++ioSchemaID);
    }

    /**
     * Upraviť grafickú reprezentáciu popisného textu značky
     */
    public void updatePopisText() {
        if (popisText == null) {
            popisText = new Text(meno);
            popisText.vyska = 10;
            popisText.pozicia.y = -popisText.vyska - 2;
        } else {
            popisText.text = meno;
        }
        popisText.regenerateShape();
    }

    /**
     * Vracia identifikátor značky
     *
     * @return identifikátor
     */
    public int getId() {
        return id;
    }

    /**
     * Nastavenie nového identifikátora značky
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Porovnanie značky na základe mena
     *
     * @param o značka na porovnanie
     * @return -1, 0, 1, viac info v {@link java.lang.String#compareToIgnoreCase(String)}
     */
    @Override
    public int compareTo(Object o) {
        return meno.compareToIgnoreCase(((ZnackaSchema) o).meno);
    }

    /**
     * Vracia obĺžnik opisujúci celé telo značky, jej pozíciu, šírku a výšku.
     *
     * @return obĺžnik opisujúci celé telo značky
     */
    public Rectangle2D getBounds2D() {
        if (primitivy.isEmpty()) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }
        Rectangle2D r = getPrimitiva(0).getBounds2D();
        double minX = r.getMinX();
        double minY = r.getMinY();
        double maxX = r.getMaxX();
        double maxY = r.getMaxY();
        for (Primitiva p : primitivy) {
            r = p.getBounds2D();
            if (r.getMinX() < minX) minX = r.getMinX();
            if (r.getMinY() < minY) minY = r.getMinY();
            if (r.getMaxX() > maxX) maxX = r.getMaxX();
            if (r.getMaxY() > maxY) maxY = r.getMaxY();
        }
        minX += pozicia.x;
        maxX += pozicia.x;
        minY += pozicia.y;
        maxY += pozicia.y;
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    public Shape getBoundsWithIOExtracted() {
        Area area = new Area(getBounds2D());
        for (IOPrimitiva io : this.getPrimitivy(IOPrimitiva.class)) {
            area.subtract(new Area(updateZeroSize(io.getBounds2D(pozicia))));
        }
        return area;
    }

    private Rectangle2D updateZeroSize(Rectangle2D bounds) {
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        if (bounds.getWidth() == 0) {
            minX -= 1;
            width = 2;
        }
        if (bounds.getHeight() == 0) {
            minY -= 1;
            height = 2;
        }
        return new Rectangle2D.Double(minX, minY, width, height);
    }

    /**
     * Vracia informáciu o možnosti meniť rozmery značky.
     *
     * @return {@code true} ak je možné meniť rozmery značky, inak {@code false}
     */
    public boolean isResizable() {
        return true;
    }

    /**
     * Vracia informáciu o tom, či značka je označená
     *
     * @return {@code true} ak je značka označená, inak {@code false}
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Označuje / odznačuje značku
     *
     * @param selected {@code true} označiť, {@code false} odznačiť
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Vracia informáciu o tom, či telo značky obsahuje zadaný bod
     * @param p bod
     * @return {@code true} ak telo značky obsahuje zadaný bod, inak {@code false}
     */
    public boolean isSelectionPoint(Point2D p) {
        Rectangle2D bounds = getBounds2D();
        if (bounds.getWidth() == 0 || bounds.getHeight() == 0) {
            return bounds.intersects(p.getX() - 1, p.getY() - 1, 2, 2);
        }
        return bounds.contains(p);
    }

    /**
     * Posunutie značky
     *
     * @param px posunutie v rámci x-ovej osi
     * @param py posunutie v rámci y-ovej osi
     */
    public void move(double px, double py) {
        pozicia.x += px;
        pozicia.y += py;
    }

    /**
     * Zmena veľkosti značky
     *
     * @param px zmena veľkosit na x-ovej osi
     * @param py zmena veľkosti na y-ovej osi
     * @param maska doplňujúce info o prípadných potrebných posunutiach
     * <br/><br/><b>Použitie masky:</b><br/><br/>
     * {@code   sirka +=  px * maska[0];<br/>
                vyska +=  py * maska[1];<br/>

                move(px * maska[2], py * maska[3]);}
     */
    public void resize(double px, double py, float[] maska) {
        for (Primitiva p : primitivy) {
            if (p.isResizable()) {
                p.resize(px / 2, py / 2, maska);
            } else {
                p.move(px * maska[2], py * maska[3]);
            }
        }
    }

    /**
     * Vracia primitívu na zadanej pozícii so zadanou toleranciou
     *
     * @param atPos pozícia
     * @param tolerancia maximálna vzdialenosť od zadanej pozície
     * @return primitiva alebo {@code null} ak sa primitva nenašla
     */
    public Primitiva getPrimitivaAt(Point2D.Double atPos, double tolerancia) {
        Rectangle2D zb = getBounds2D();
        for (Primitiva p : getPrimitivy()) {
            Rectangle2D b = p.getBounds2D();
            double pW = 0;
            double pH = 0;
            if (b.getWidth() == 0) {
                pW = 1;
            }
            if (b.getHeight() == 0) {
                pH = 1;
            }
            Rectangle2D bounds = new Rectangle2D.Double(b.getMinX() + pozicia.x, b.getMinY() + pozicia.y, b.getWidth() + pW, b.getHeight() + pH);
            if (bounds.intersects(atPos.x - tolerancia / 2, atPos.y - tolerancia / 2, tolerancia, tolerancia)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Vracia vstup alebo výstup so zadaným identifikátorom
     *
     * @param id zadaný identifikátor
     * @return primitíva alebo {@code null} ak sa primitíva nenašla
     */
    public IOSchema getIO(int id) {
        for (IOSchema io : ioMap.values()) {
            if (io.getId() == id) return io;
        }
        return null;
    }

    public IOSchema getIO(IOPrimitiva ioPrimitiva) {
        return ioMap.get(ioPrimitiva);
    }

    public List<IOSchema> getIOList() {
        return getIOList(false);
    }

    public List<IOSchema> getFreeIOList() {
        return getIOList(true);
    }

    private List<IOSchema> getIOList(boolean onlyFree) {
        List<IOSchema> ioList = new ArrayList<IOSchema>(ioMap.values());
        if (onlyFree) {
            for (IOSchema io : ioMap.values()) {
                if (io.isObsadene()) ioList.remove(io);
            }
        }
        return ioList;
    }

    /**
     * Vytvorí klon značky
     *
     * @return klon značky
     * @throws java.lang.CloneNotSupportedException
     * ak nastane chyba pri klonovaní
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ZnackaSchema zch = new ZnackaSchema(this);
        zch.id = id;
        zch.lastID = lastID;
        zch.meno = meno;
        zch.pocetVstupov = pocetVstupov;
        zch.pocetVystupov = pocetVystupov;
        zch.popisText = (Text) popisText.clone();
        zch.pozicia = (Point2D.Double) pozicia.clone();
        zch.selected = selected;
        return zch;
    }

    /**
     * Porovnávač primitív na základe y-ovej súradnice
     */
    private class PrimitivaPozYComparator implements Comparator<Primitiva> {

        public int compare(Primitiva o1, Primitiva o2) {
            return new Double(o1.pozicia.y).compareTo(o2.pozicia.y);
        }
    }

    public double getSirka() {
        return getBounds2D().getWidth();
    }

    public double getVyska() {
        return getBounds2D().getHeight();
    }

    public void clearIOMap() {
        ioMap.clear();
    }

    public Point2D getConnectionPointForIO(IOSchema io) {
        Point2D cxnPoint = io.getIOPrimitiva().getConnectionPoint();
        return new Point2D.Double(cxnPoint.getX() + pozicia.x, cxnPoint.getY() + pozicia.getY());
    }
}
