package core;

import fenomeni.economia.GuadagniMedi;
import fenomeni.economia.GuadagniPoveri;
import fenomeni.economia.GuadagniRicchi;
import gui.CVertex;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import core.exceptions.FenomenoException;

public abstract class Fenomeno implements IInfluenceSource, IInfluenceDest {

    static Logger logger = Logger.getLogger(Fenomeno.class.getName());

    private Ministero afferenza;

    final double displaymultiplier;

    private String unitadiMisura;

    private String displayname;

    private History hist_val;

    private Set<Effetto> inputEffects;

    private String name_long;

    private Set<Effetto> outputEffects;

    private double valtarget;

    private double thisturndelta = 0;

    private double valoreAttuale;

    final double valoreBase;

    private double valmax;

    private double valmin;

    private CVertex position;

    private File shortIconFile;

    private int TextureHandle = -1;

    private String texturepath;

    private String sfondoStoria;

    public boolean isVisible;

    /**
	 * @param name
	 * @param gt
	 * @param baseval
	 * @param maxRealQuantity
	 * 
	 *            Crea un Indice della simulazione, da legare ad altri
	 *            istanziando la classe Effetto
	 * 
	 */
    public Fenomeno(Ministero aff, String name, Calendar gt, double baseval, double maxRealQuantity) {
        super();
        this.afferenza = aff;
        this.displayname = name;
        this.name_long = "Descrizione mancante per: " + name;
        this.valoreAttuale = baseval;
        this.inputEffects = new HashSet<Effetto>();
        this.outputEffects = new HashSet<Effetto>();
        this.valoreBase = baseval;
        this.valmin = 0;
        this.valmax = 1;
        this.isVisible = false;
        this.valtarget = baseval;
        this.displaymultiplier = maxRealQuantity;
        this.hist_val = new History(this);
        try {
            if ((Class.forName(GuadagniMedi.class.getName()).isInstance(this)) || (Class.forName(GuadagniPoveri.class.getName()).isInstance(this)) || (Class.forName(GuadagniRicchi.class.getName()).isInstance(this))) {
                this.valmin = -1;
            }
            hist_val.makeFakeHistory(baseval);
            logger.info(name + " inizializzato. Valore iniziale: " + baseval);
        } catch (Exception e) {
            logger.error("Errore nel costruttore del fenomeno " + this.displayname);
        }
    }

    /**
	 * @param inputEccects
	 *            the inputEffects to add
	 */
    public void addInputEffect(Effetto input) {
        if (this.inputEffects == null) this.inputEffects = new HashSet<Effetto>();
        this.inputEffects.add(input);
    }

    /**
	 * @param inputEccects
	 *            the inputEccects to add
	 */
    public void addOutputEffect(Effetto input) {
        if (this.outputEffects == null) this.outputEffects = new HashSet<Effetto>();
        this.outputEffects.add(input);
    }

    public void commitMonthVal() {
        if (valtarget > valmax) {
            logger.warn(this.getName() + ": maxlevel raggiunto");
            valtarget = valmax;
        }
        if (valtarget < valmin) {
            logger.warn(this.getName() + ": minlevel raggiunto");
            valtarget = valmin;
        }
        if (valoreAttuale != valtarget) logger.info(this.displayname + " COMMITTING: " + valtarget + " ON " + getGametime().getTime().toString());
        this.valoreAttuale = valtarget;
    }

    /**
	 * diminuisce "manualmente" il valore target. Andrebbe chiamata dopo la
	 * process() e prima della commit()
	 * 
	 * @param adder
	 */
    public void decrement(double adder) {
        valtarget -= adder;
    }

    /**
	 * esegue un dump su logger della storia
	 */
    public void dumpHistoryVal() {
        History thishist = getHistory();
        Vector<Date> keys = new Vector<Date>(thishist.getHistory().keySet());
        Collections.sort(keys);
        Iterator<Date> ite = keys.iterator();
        logger.info("-Storia di " + this.getName());
        while (ite.hasNext()) {
            Date object = (Date) ite.next();
            try {
                logger.info(object + " : " + thishist.getHistory(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * @return il discostamento dal valore di default
	 */
    public double getBaseDelta() {
        return valoreAttuale - valoreBase;
    }

    /**
	 * @return the name for the GUI
	 */
    public String getDisplayName() {
        return displayname;
    }

    /**
	 * @return the displaymultiplier
	 */
    public double getDisplayVal() {
        BigDecimal rounded = new BigDecimal(valoreAttuale * displaymultiplier);
        rounded = rounded.setScale(1, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }

    /**
	 * @return the gametime
	 */
    public Calendar getGametime() {
        return getMinisteroPadre().getGametime();
    }

    /**
	 * @return the hist_val
	 */
    public History getHistory() {
        return hist_val;
    }

    /**
	 * @return the val_actual
	 * @param mesifa
	 *            il numero di mesi nel passato da cui leggere
	 */
    public double getHistoryVal(int mesifa) {
        if (mesifa == 0) return valoreAttuale;
        try {
            return hist_val.getHistory(mesifa);
        } catch (Exception e) {
            logger.warn(this.getName() + "non trova la storia di mesi fa=" + mesifa, e);
            dumpHistoryVal();
            return hist_val.getFirstDateVal();
        }
    }

    /**
	 * @return the inputEccects
	 */
    public Set<Effetto> getInputEffects() {
        return inputEffects;
    }

    /**
	 * @return the long_name
	 */
    public String getLong_name() {
        return name_long;
    }

    /**
	 * @return the afferenza
	 */
    public Ministero getMinisteroPadre() {
        return afferenza;
    }

    /**
	 * @return the name for the GUI
	 */
    public String getName() {
        return getDisplayName();
    }

    /**
	 * @return the name_long
	 */
    public String getName_long() {
        return name_long;
    }

    /**
	 * Serve per determinare il verso delle frecce
	 */
    public double getNoInercyDelta() {
        logger.warn("gli effetti di questa chiamata sono imprevisti");
        return getVal_target();
    }

    /**
	 * 
	 * @return il valore attuale che e` gia normalizzato, come influenza
	 * 
	 */
    public double getNormalizedDelta() {
        return valoreAttuale;
    }

    /**
	 * @return the outputEffects
	 */
    public Set<Effetto> getOutputEffects() {
        return outputEffects;
    }

    /**
	 * @return the position
	 */
    public CVertex getPosition() {
        return position;
    }

    /**
	 * @return the sfondoStoria
	 */
    public String getSfondoStoria() {
        return sfondoStoria;
    }

    /**
	 * @return the shortIconName
	 */
    public File getShortIcon() {
        return shortIconFile;
    }

    /**
	 * @return the textureHandle
	 */
    public int getTextureHandle() {
        return TextureHandle;
    }

    /**
	 * @return the texturepath
	 */
    public String getTexturepath() {
        return texturepath;
    }

    /**
	 * @return the unita
	 */
    public String getUnita() {
        return unitadiMisura;
    }

    /**
	 * @return the val_target
	 */
    public double getVal_target() {
        return valtarget;
    }

    /**
	 * @return the valmax
	 */
    public double getValmax() {
        return valmax;
    }

    /**
	 * @return the valmin
	 */
    public double getValmin() {
        return valmin;
    }

    /**
	 * @return the val_base
	 */
    public double getValoreBase() {
        return valoreBase;
    }

    /**
	 * diminuisce "manualmente" il valore target. Andrebbe chiamata dopo la
	 * process() e prima della commit()
	 * 
	 * @param adder
	 * @param adder
	 */
    public void increment(double adder) {
        valtarget += adder;
    }

    /**
	 * Torna il trend del fenomeno -> deve essere performante e` chiamata nella
	 * render
	 * 
	 * @return 0= stabile, 1=cresce,-1 scende
	 */
    public int isIncreasing() {
        if (valoreAttuale != valtarget) logger.warn("Valore non committato e chiedo storia ?");
        Double val = Double.valueOf(valoreAttuale);
        Double prima = Double.valueOf(getHistoryVal(1));
        Double primaancora = Double.valueOf(getHistoryVal(2));
        return (int) val.compareTo(prima);
    }

    @Override
    public void process() throws FenomenoException {
        if (this.valoreAttuale > this.valmax || this.valoreAttuale < this.valmin) throw new FenomenoException(this, "Assert su min e max fallita");
        Iterator<Effetto> it = getInputEffects().iterator();
        thisturndelta = 0;
        while (it.hasNext()) {
            Effetto element = it.next();
            try {
                element.process();
                if (!element.isActive()) continue;
                logger.debug(this.getName() + ":Influenza da " + element.getName() + "(" + element.getNormalizedDelta() + ")");
                thisturndelta += element.getNormalizedDelta();
            } catch (Exception e) {
                logger.fatal("CLASSE NON TROVATA", e);
            }
        }
        BigDecimal rounded = new BigDecimal(valoreAttuale).setScale(3, BigDecimal.ROUND_HALF_UP);
        BigDecimal roundedelt = new BigDecimal(thisturndelta).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.valtarget = valoreBase + thisturndelta;
        logger.debug(this.displayname + " VAL: " + rounded + " VAL BASE: " + valoreBase + " TURNDELTA: " + roundedelt);
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setDisplayName(String name) {
        this.displayname = name;
    }

    /**
	 * @param inputEffects
	 *            the inputEccects to set
	 */
    public void setInputEffects(Set<Effetto> inputEccects) {
        this.inputEffects = inputEccects;
    }

    /**
	 * @param name_long
	 *            the name_long to set
	 */
    public void setName_long(String name_long) {
        this.name_long = name_long;
    }

    /**
	 * @param outputEffects
	 *            the outputEffects to set
	 */
    public void setOutputEffects(Set<Effetto> outputEffects) {
        this.outputEffects = outputEffects;
    }

    /**
	 * @param position
	 *            the position to set
	 */
    public void setPosition(CVertex position) {
        this.position = position;
    }

    /**
	 * @param sfondoStoria the sfondoStoria to set
	 */
    public void setSfondoStoria(String sfondoPath) {
        sfondoStoria = sfondoPath;
    }

    /**
	 * @param shortIconName the shortIconName to set
	 * serve sia per la texture che per lo sfondino quadrato
	 */
    public void setShortIcon(File shortIconName) {
        if (shortIconName != null) {
            this.shortIconFile = shortIconName;
            logger.info("setting txt path " + shortIconFile.getAbsolutePath());
            setTexturepath(shortIconFile.getAbsolutePath());
        }
    }

    /**
	 * @param textureHandle
	 *            the textureHandle to set
	 */
    public void setTextureHandle(int textureHandle) {
        TextureHandle = textureHandle;
    }

    /**
	 * @param texturepath
	 *            the texturepath to set
	 */
    public void setTexturepath(String texturepath) {
        this.texturepath = texturepath;
    }

    /**
	 * @param unita
	 *            the unita to set
	 */
    public void setUnita(String unita) {
        this.unitadiMisura = unita;
    }

    /**
	 * @param val_actual
	 *            the val_actual to set
	 */
    public void setVal_actual(double val_actual) {
        this.valoreAttuale = val_actual;
    }

    /**
	 * @param val_target
	 *            the val_target to set
	 */
    public void setVal_target(double val_target) {
        this.valtarget = val_target;
    }

    @Override
    public String toString() {
        return this.displayname;
    }

    public void updateHistory() {
        try {
            hist_val.addHistVal(valoreAttuale);
        } catch (Exception e) {
            logger.error("err aggiornamento storia", e);
        }
    }
}
