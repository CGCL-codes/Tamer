package sk.tuke.ess.editor.simulation.znackymod.document;

import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.base.document.DocumentException;
import sk.tuke.ess.editor.base.document.edit.DocumentBaseEditSupport;
import sk.tuke.ess.editor.base.helpers.PathHelper;
import sk.tuke.ess.editor.simulation.simeditbase.Selectable;
import sk.tuke.ess.editor.simulation.simeditbase.document.SimEditDocument;
import sk.tuke.ess.editor.simulation.simeditbase.io.kniznica.KniznicaBuilder;
import sk.tuke.ess.editor.simulation.simeditbase.io.kniznica.KniznicaLoader;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.Kniznica;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Ikona;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Primitiva;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Text;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.style.PrimitivaStyl;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.Znacka;
import sk.tuke.ess.editor.simulation.znackymod.document.edit.ZnackaDocumentEditSupport;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dokument Značky
 *
 * @author Ján Čabala
 */
public class ZnackaDocument extends SimEditDocument {

    /**
     * Cesta ku knižnici, z ktorej značka pochádza
     */
    protected String kniznicaPath;

    protected String path;

    /**
     * Značka s ktorou dokument pracuje
     */
    protected Znacka znacka;

    /**
     * Pôvodná značka, ktorá bola načítana pri otvorení dokumentu
     */
    protected Znacka povodnaZnacka;

    protected ZnackaDocumentEditSupport znackaDocumentEditSupport;

    public ZnackaDocument() {
        znackaDocumentEditSupport = new ZnackaDocumentEditSupport(this);
    }

    /**
     * Inicializácia dokumentu
     */
    private void init() {
        try {
            povodnaZnacka = (Znacka) znacka.clone();
        } catch (CloneNotSupportedException e) {
            povodnaZnacka = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void open(String path) throws DocumentException {
        this.path = path;
        Pattern pattern = Pattern.compile("(^.+):(.+)$");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            throw new DocumentException(String.format("Nesprávny formát cesty (%s) k značke", path));
        }
        try {
            kniznicaPath = matcher.group(1);
            Kniznica kniznica = KniznicaLoader.getInstance().getKniznica(new File(kniznicaPath));
            znacka = (Znacka) kniznica.getZnacka(matcher.group(2)).clone();
            init();
        } catch (Exception e) {
            throw new DocumentException(String.format("Zo zadanej cesty (%s) sa nepodarilo získať značku (%s)", path, matcher.group(2)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void save() throws DocumentException {
        Kniznica k = KniznicaLoader.getInstance().getKniznica(new File(kniznicaPath));
        k.removeZnackaIfSame(povodnaZnacka);
        k.getZnacky().add(znacka);
        znacka.setNazovKategorie(k.getNazov());
        KniznicaBuilder.getInstance().saveZnacka(znacka);
        try {
            povodnaZnacka = (Znacka) znacka.clone();
        } catch (CloneNotSupportedException e) {
            povodnaZnacka = null;
        }
        super.save();
    }

    /**
     * {@inheritDoc}
     */
    public void saveAs(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        if (znacka == null) return path;
        return znacka.nazov;
    }

    /**
     * Vracia značku z dokumentu
     *
     * @return značka z dokumentu
     */
    public Znacka getZnacka() {
        return znacka;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", kniznicaPath.replace(PathHelper.getDataPath(), "{APP_DATA_DIR}"), znacka.nazov);
    }

    public Text createTextPrimitiveOnCurrentLocation(String string) {
        Text text = new Text(string);
        text.pozicia.x = currentMousePoint.x;
        text.pozicia.y = currentMousePoint.y;
        text.vyska = 12;
        text.regenerateShape();
        return text;
    }

    public Ikona createIkonaFromFile(File file, double width, double height) throws IOException {
        PrimitivaStyl ps = new PrimitivaStyl();
        ps.hrubkaCiar = 0;
        Ikona ikona = new Ikona("Ikona", new Rectangle2D.Double(currentMousePoint.x, currentMousePoint.y, width, height), KniznicaBuilder.getInstance().addToResource(znacka, file).toString(), ps);
        ikona.updateIcon();
        return ikona;
    }

    public SortedMap<Integer, Primitiva> removeSelectedPrimitives() {
        List<Primitiva> originalPrimitivaList = getZnacka().getPrimitivy();
        List<Primitiva> copyPrimitivaList = new ArrayList<Primitiva>(originalPrimitivaList);
        SortedMap<Integer, Primitiva> primitivaIndexesMap = new TreeMap<Integer, Primitiva>();
        for (Selectable selectable : getSelection()) {
            Primitiva p = (Primitiva) selectable;
            primitivaIndexesMap.put(copyPrimitivaList.indexOf(p), p);
            originalPrimitivaList.remove(p);
            Logger.getLogger().addInfo("Z dokumentu <b>%s</b> bola zmazaná primitíva <b>%s</b>", getName(), p.nazov);
        }
        selection.clear();
        return primitivaIndexesMap;
    }

    /**
     * {@inheritDoc}
     */
    public String getPath() {
        return String.format("%s:%s", kniznicaPath, znacka.nazov);
    }

    public DocumentBaseEditSupport getDocumentBaseEditSupport() {
        return znackaDocumentEditSupport;
    }
}
