package de.haw.mussDasSein.control;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jdom.Document;
import de.haw.mussDasSein.model.Antrieb;
import de.haw.mussDasSein.model.Ausstattung;
import de.haw.mussDasSein.model.Komponente;
import de.haw.mussDasSein.model.Lack;
import de.haw.mussDasSein.model.Paket;
import de.haw.mussDasSein.model.Polsterung;
import de.haw.mussDasSein.model.Reifen;
import de.haw.mussDasSein.model.SubModell;
import de.haw.mussDasSein.model.Zubehoer;
import de.haw.mussDasSein.rule.LUT;
import de.haw.mussDasSein.rule.SelectableCondition;
import de.haw.mussDasSein.serialization.Getriebe;
import de.haw.mussDasSein.serialization.Motoren;
import de.haw.mussDasSein.serialization.Submodelle;
import de.haw.mussDasSein.serialization.XMLParser;
import de.haw.mussDasSein.util.ChangeTabOrder;
import de.haw.mussDasSein.util.CreateObject;
import de.haw.mussDasSein.util.XMLReader;
import de.haw.mussDasSein.view.AbstractView;

/**
 * Controller mit ein paar Methoden aufs Gerate-Wohl.
 * 
 * @see AbstractController
 * @author Bettzueche
 *
 */
public class DefaultController extends AbstractController {

    public static final String AUTOTYP_NAME = "Autotyp_typeName";

    public static final String AUTOTYP_KLASSE = "Autotyp_typenKlasse";

    public static final String SUBMODELL = "Submodell";

    public static final String SUBMODELL_NAME = SUBMODELL + "_modelName";

    public static final String SUBMODELL_ANTRIEB = SUBMODELL + "_antrieb";

    public static final String SUBMODELL_FARBE = SUBMODELL + "_farbe";

    public static final String SUBMODELL_POLSTER = SUBMODELL + "_polsterung";

    public static final String SUBMODELL_REIFEN = SUBMODELL + "_bereifung";

    /**Basisordner der Komponenten-XML-Dateien*/
    public static final String XMLDATAPATH = "XMLData";

    /**Basisordner der Regelwerk-XML-Dateien*/
    public static final String XMLRULEPATH = XMLDATAPATH + "/Rules";

    /**XML mit Modelle (Autotypen werden als SubModelle gespeichert).*/
    public static final String XML_MODELS = XMLDATAPATH + "/Modells.xml";

    /**XML mit Motoren.*/
    public static final String XML_MOTOREN = XMLDATAPATH + "/Motors.xml";

    /**XML mit Getriebe.*/
    public static final String XML_GETRIEBE = XMLDATAPATH + "/Getriebe.xml";

    /**XML mit Reihenfolge der Auswahlkategorien (Gesch&auml;ftsprozesse).*/
    public static final String XML_PROCESS_ORDER = XMLDATAPATH + "/order.xml";

    /**Auswahl des Users*/
    private SubModell selection;

    private LUT rules;

    private Map<String, SelectableCondition> selectables;

    private AvailabelList tempAntriebeList;

    /**
	 * 
	 */
    public DefaultController() {
        super();
        rules = loadRules();
        loadSelectables();
    }

    /**
	 * 
	 * @return dummy Array {"Astra", "Insignia", "Zafira"}
	 */
    public String[] getAutotypen() {
        return new String[] { "Astra", "Insignia", "Zafira" };
    }

    /**
	 * Aktualisiert die aktuele Auswahl...
	 * @param typName Name des Grundmodells
	 * @Deprecated no functionality, for future release only, use {@link #setSelectedSubmodell(String)} instead.
	 */
    @Deprecated
    public void setSelectedAutotyp(String typName) {
    }

    /**
	 * 
	 * @param autoTyp Name des Basismodells
	 * @return empty Array
	 */
    public String[] getSubModelle(String autoTyp) {
        return new String[0];
    }

    /**
	 * Gibt alle Submodelle eines jeden Auto-Basismodells.
	 * <p>
	 * Die Daten werden, falls vorhanden, aus XMLData/Modells.xml eingelesen.
	 * Die einezelnen Namen entsprechen <tt>SubModell.getNameModell()</tt>, also "Autotypname Submodellbezeichnung".
	 * </p>
	 * 
	 * @return Array aller (Sub-)Modelle, ggf. leer
	 */
    public String[] getSubModelle() {
        String[] result = null;
        try {
            Submodelle modelle = XMLParser.parseXMLFile(Submodelle.class, XML_MODELS);
            result = new String[modelle.getModelle().size()];
            int i = 0;
            for (SubModell modell : modelle.getModelle()) {
                result[i++] = modell.getNameModel();
                if (!selectables.containsKey(modell.getNameModel())) {
                    selectables.put(modell.getNameModel(), new SelectableCondition(modell.getNameModel()));
                }
            }
        } catch (Exception e) {
            return new String[0];
        }
        return result;
    }

    @Deprecated
    public String[] getCategories() {
        return new String[] { "Modellvariante", "Motor", "Farbe", "R�der+Felgen", "Sonderausstattung", "Sonstiges" };
    }

    /**
	 * Setzt die Auswahl auf das Modell mit dem spezifizierten Namen.
	 * <p>
	 * Das Modell dient als <b>Basis</b> einer kompletten Bestelung/Auswahl. Gegebenenfalls zuvor get&auml;tigte Selektionen
	 * werden hiermit <b>automatisch gel&ouml;scht</b>!
	 * </p>
	 * @param modellName Name des Submodells
	 */
    public void setSelectedSubmodell(String modellName) {
        Submodelle modelle;
        SubModell oldVal = selection;
        try {
            modelle = XMLParser.parseXMLFile(Submodelle.class, XML_MODELS);
            for (SubModell modell : modelle.getModelle()) {
                if (modell.getNameModel().equals(modellName)) {
                    selection = modell;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifyBadModell(oldVal);
        }
        if (selection == null) {
            notifyBadModell(oldVal);
            return;
        }
        SelectableCondition sc = selectables.get(modellName);
        if (sc == null) {
            notifyBadModell(oldVal);
            return;
        }
        if (oldVal != null) {
            for (SelectableCondition sel : selectables.values()) sel.setSelected(false);
        }
        sc.setSelected(true);
    }

    /**
	 * Event fuer Views, dass setSelectedSubmodell() schief ging.
	 * @param old
	 */
    private void notifyBadModell(SubModell old) {
        PropertyChangeEvent ev = new PropertyChangeEvent(this, SUBMODELL, old, null);
        for (AbstractView view : registeredViews) {
            view.modelPropertyChange(ev);
        }
    }

    /**
	 * Komponente zur (Benutzer-)Auswahl hinzuf&uuml;gen.
	 * <p><b>Note:</b> Ob der Name identifizierend ist, ist noch nicht sicher!<br>
	 * <b>Note2:</b> Ob eine entsprechende nicht-generische Methode implementierbar ist,
	 * muss sich noch zeigen.</p>
	 *  
	 * @param <T> <tt>Komponente</tt> oder Derivat
	 * @param compType Typ der Komponente
	 * @param name Name der Komponente
	 * @return true, wenn spezifizierte Kompoente hinzugef�gt werden konnte
	 */
    public <T extends Komponente> boolean selectKomponente(Class<T> compType, String name) {
        return false;
    }

    /**
	 * Komponente zur (Benutzer-)Auswahl hinzuf&uuml;gen.
	 * <p><b>Note:</b> Ob die ID identifizierend ist, ist noch nicht sicher!</p>
	 * 
	 * @param <T> <tt>Komponente</tt> oder Derivat
	 * @param compType Typ der Komponente
	 * @param id ID der Komponente
	 * @return true, wenn spezifizierte Kompoente hinzugef&uuml;gt werden konnte
	 */
    public <T extends Komponente> boolean selectKomponente(Class<T> compType, int id) {
        return false;
    }

    /**
	 * Komponente aus (Benutzer-)Auswahl entfernen.
	 * <p><b>Note:</b> Ob die ID identifizierend ist, ist noch nicht sicher!</p>
	 * 
	 * @param <T> <tt>Komponente</tt> oder Derivat
	 * @param compType Typ der Komponente
	 * @param name Name der Komponente
	 * @return true, wenn spezifizierte Kompoente entfernt werden konnte
	 */
    public <T extends Komponente> boolean deselectKomponente(Class<T> compType, String name) {
        return true;
    }

    /**
	 * Komponente aus (Benutzer-)Auswahl entfernen.
	 * <p><b>Note:</b> Ob die ID identifizierend ist, ist noch nicht sicher!</p>
	 * 
	 * @param <T> <tt>Komponente</tt> oder Derivat
	 * @param compType Typ der Komponente
	 * @param id ID der Komponente
	 * @return true, wenn spezifizierte Kompoente entfernt werden konnte
	 */
    public <T extends Komponente> boolean deselectKomponente(Class<T> compType, int id) {
        return true;
    }

    /**
	 * Gibt eine Auflistung der aktuellen Auswahl in einer String-Repr&auml;sentation.
	 * @return empty String
	 */
    public String getSelectionList() {
        return "";
    }

    /**
	 * 
	 * @param modell Name des Modells
	 * @return empty Array
	 */
    public String[] getSerienausstattungOf(String modell) {
        return new String[0];
    }

    /**
	 * Erzeugt eine leere Benutzer-Auswahl.
	 * Die ggf bisherige Auswahl wird damit gel&ouml;scht.
	 */
    public void newSelection() {
        selection = null;
        for (SelectableCondition sel : selectables.values()) sel.setSelected(false);
    }

    /**
	 * Gibt Liste aktueller Auswahlm&ouml;glichkeiten (von Komponenten).
	 * @return Auswahlm&ouml;glichkeiten
	 */
    public AvailabelList getAuswahlliste() {
        return new AvailabelList();
    }

    /**
	 * Gibt Liste aktueller Auswahlm&ouml;glichkeiten der spezifizierten Komponententyps
	 * @param Klasse Komponte oder Derivate
	 * @param componentType spezifischer Komponententyp
	 * @return
	 */
    public <T extends Komponente> AvailabelList getAuswahlliste(Class<T> componentType) {
        AvailabelList resu = new AvailabelList();
        if (componentType == Antrieb.class) {
            return getAntriebeListe();
        } else if (componentType == Lack.class) {
        } else if (componentType == Zubehoer.class) {
        } else if (componentType == Polsterung.class) {
        } else if (componentType == Reifen.class) {
        } else if (componentType == Ausstattung.class) {
        }
        return resu;
    }

    private AvailabelList getAntriebeListe() {
        AvailabelList resu = new AvailabelList();
        Motoren motoren = null;
        Getriebe getriebe = null;
        try {
            motoren = XMLParser.parseXMLFile(Motoren.class, XML_MOTOREN);
            getriebe = XMLParser.parseXMLFile(Getriebe.class, XML_GETRIEBE);
            for (Komponente getr : getriebe.getGetriebe()) {
                SelectableCondition gtrSel = selectables.get(getr.getName());
                if (gtrSel == null) {
                    selectables.put(getr.getName(), new SelectableCondition(getr.getName()));
                }
                gtrSel.setSelected(true);
                if (gtrSel.isSelcted()) {
                    for (Komponente motor : motoren.getMotoren()) {
                        SelectableCondition motSel = selectables.get(motor.getName());
                        if (motSel == null) {
                            selectables.put(motor.getName(), new SelectableCondition(motor.getName()));
                        }
                        motSel.setSelected(true);
                        if (motSel.isSelcted()) {
                            Antrieb antrieb = new Antrieb();
                            antrieb.setMotor(motor);
                            antrieb.setGetriebe(getr);
                            antrieb.setId(motor.getId() * getr.getId());
                            motSel.setSelected(false);
                            gtrSel.setSelected(false);
                            AvailabelElement elem = new AvailabelElement(antrieb, motSel.isSelctable() && gtrSel.isSelctable());
                            resu.add(elem);
                            gtrSel.setSelected(true);
                        }
                        motSel.setSelected(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resu;
        }
        return resu;
    }

    /**
	 * List die TabOrder = Gesch&auml;eftspross-Reihenfolge aus einer XML und gibt sie als <tt>TreeMap</tt>
	 * wieder. key = Rangfolge, value = Prozess-ID.
	 * 
	 * @return TabOrder des Gesch&auml;ftsprozesses
	 * @since Konfigurator 2.0
	 */
    public TreeMap<Integer, Integer> getOrder() {
        XMLReader xmlr = new XMLReader();
        Document doc = xmlr.readXML(XML_PROCESS_ORDER);
        Object[] arg = xmlr.fromXML(doc);
        Object[] arg2 = new Object[arg.length - 1];
        System.arraycopy(arg, 1, arg2, 0, arg.length - 1);
        ChangeTabOrder t1 = CreateObject.run(ChangeTabOrder.class, arg[0].toString(), arg2);
        ArrayList<Integer> temp = t1.getArrayList();
        TreeMap<Integer, Integer> trmap = new TreeMap<Integer, Integer>();
        for (int i = 0; i < temp.size(); i++) {
            trmap.put(i, temp.get(i));
        }
        return trmap;
    }

    /**
	 * Regelwerk aus XML einlesen.
	 * 
	 * @return Regelwerk
	 * @since Konfigurator 2.1
	 */
    private LUT loadRules() {
        File ruleFolder = new File(XMLRULEPATH);
        if (!ruleFolder.exists() && !ruleFolder.isDirectory()) {
            return new LUT();
        }
        LUT result = new LUT();
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml");
            }
        };
        try {
            for (File file : ruleFolder.listFiles(filter)) {
                result.add(XMLParser.parseXMLFile(LUT.class, file));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    private void loadSelectables() {
        selectables = new HashMap<String, SelectableCondition>();
        for (String ident : rules.getIdentifiers()) {
            SelectableCondition sc = new SelectableCondition(ident);
            sc.setRuleSet(rules);
            selectables.put(ident, sc);
        }
        for (SelectableCondition sc : rules.getAtomicSelactables()) {
            sc.setRuleSet(rules);
            selectables.put(sc.getIdentifier(), sc);
        }
    }
}
