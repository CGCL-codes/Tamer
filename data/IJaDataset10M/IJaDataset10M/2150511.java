package core;

import gui.CVertex;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class DecretoLegge implements IInfluenceSource {

    static Logger logger = Logger.getLogger(DecretoLegge.class.getName());

    private Ministero afferenza;

    boolean ismoving = false;

    private int costoabrogazione;

    private int costoaumento;

    private int costointroduzione;

    private double costomax;

    private double costomin;

    private int costotaglio;

    private Calendar gametime;

    private double guadagnomax;

    private double guadagnomin;

    boolean isDefault;

    /**
	 * @return the isDefault
	 */
    public boolean isDefault() {
        return isDefault;
    }

    /**
	 * @param isDefault
	 *           the isDefault to set
	 */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    boolean isactive;

    private History hist_val;

    private int mesiinerzia = -1;

    private int mesiinerziaadesso = -1;

    private String nome;

    private String nomeDescrizione;

    private String nomeVisualizzato;

    private Set<Effetto> outputEffects;

    private CVertex position;

    private String sfondostoria;

    /**
	 * @return the sfondostoria
	 */
    public String getSfondostoria() {
        return sfondostoria;
    }

    /**
	 * @param sfondostoria
	 *           the sfondostoria to set
	 */
    public void setSfondostoria(String sfondostoria) {
        this.sfondostoria = sfondostoria;
    }

    String scaler;

    private double val;

    private double valCommitted;

    public void setUpHistory() {
        this.hist_val = new History(this);
        hist_val.makeFakeHistory(val);
    }

    /**
	 * @param inputEccects
	 *           the inputEccects to add
	 */
    public void addOutputEffect(Effetto input) {
        if (this.outputEffects == null) this.outputEffects = new HashSet<Effetto>();
        this.outputEffects.add(input);
    }

    /**
	 * @param name
	 * @param gt
	 * @param baseval
	 * @param step
	 */
    public DecretoLegge(String name, String dispname, String desc, Ministero aff) {
        super();
        this.val = 0.5;
        this.nome = name;
        this.nomeVisualizzato = dispname;
        this.nomeDescrizione = desc;
        this.afferenza = aff;
        this.gametime = afferenza.getGametime();
        this.valCommitted = this.val;
    }

    /**
	 * @return the abrogazione, costo politico necessario per abrogare la legge
	 */
    public int getCostoAbrogazione() {
        return costoabrogazione;
    }

    /**
	 * @return the costoaumento
	 */
    public int getCostoAumento() {
        return costoaumento;
    }

    /**
	 * @return the introduzione, costo politico necessario per implementare la
	 *         legge
	 */
    public int getCostoIntroduzione() {
        return costointroduzione;
    }

    /**
	 * @return the costomax
	 */
    public double getCostoMax() {
        return costomax;
    }

    /**
	 * @return the costomin
	 */
    public double getCostoMin() {
        return costomin;
    }

    /**
	 * @return the costotaglio
	 */
    public int getCostoTaglio() {
        return costotaglio;
    }

    /**
	 * @return the gametime
	 */
    public Calendar getGametime() {
        return gametime;
    }

    /**
	 * @return the guadagnomax
	 */
    public double getGuadagnoMax() {
        return guadagnomax;
    }

    /**
	 * @return the guadagnomin
	 */
    public double getGuadagnoMin() {
        return guadagnomin;
    }

    /**
	 * @return the val_actual
	 * @param mesifa
	 *           il numero di mesi nel passato da cui leggere
	 */
    public double getHistoryVal(int mesifa) {
        if (mesifa == 0) return getNormalizedDelta();
        try {
            return hist_val.getHistory(mesifa);
        } catch (Exception e) {
            logger.error("Get history fallita ", e);
            return 0;
        }
    }

    /**
	 * @return the mesiinerzia
	 */
    public int getInerziaInit() {
        return mesiinerzia;
    }

    /**
	 * @return the mesiinerzia
	 */
    public int getInerzia() {
        return mesiinerziaadesso;
    }

    /**
	 * @return the afferenza
	 */
    public Ministero getMinistero() {
        return afferenza;
    }

    public String getName() {
        return this.nome;
    }

    /**
	 * @return the nomeDescrizione
	 */
    public String getNomeDescrizione() {
        return nomeDescrizione;
    }

    /**
	 * @return the nomeVisualizzato
	 */
    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

    @Override
    public double getNormalizedDelta() {
        return val;
    }

    @Override
    public double getNoInercyDelta() {
        return valCommitted;
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

    public void increment(double adder) {
        val += adder;
    }

    public void commitChange(double newval) {
        logger.info("commit decrlevel:" + newval + ". Committed is " + valCommitted + "Value is " + val);
        assert this.val != this.valCommitted : "valori identici";
        this.valCommitted = newval;
        ismoving = true;
        this.mesiinerziaadesso = this.mesiinerzia;
    }

    @Override
    public void process() {
        if (!ismoving) this.val = this.valCommitted; else {
            double delta = (this.valCommitted - this.val) / mesiinerziaadesso;
            logger.info(this.getName() + " is adding " + (delta) + " VAL:" + val + " INERCY:" + mesiinerziaadesso);
            this.val += delta;
        }
        try {
            hist_val.addHistVal(getNormalizedDelta());
        } catch (Exception e) {
            logger.error("err aggiornamento storia", e);
        }
        if (this.getHistoryVal(0) != this.getHistoryVal(1) && mesiinerziaadesso > 1) this.mesiinerziaadesso--; else if (mesiinerziaadesso == 1) {
            this.ismoving = false;
        }
    }

    /**
	 * @param costoabrogazione
	 *           the costoabrogazione to set
	 */
    public void setCostoAbrogazione(int costoabrogazione) {
        this.costoabrogazione = costoabrogazione;
    }

    /**
	 * @param costoaumento
	 *           the costoaumento to set
	 */
    public void setCostoAumento(int costoaumento) {
        this.costoaumento = costoaumento;
    }

    /**
	 * @param costointroduzione
	 *           the costointroduzione to set
	 */
    public void setCostoIntroduzione(int costointroduzione) {
        this.costointroduzione = costointroduzione;
    }

    /**
	 * @param costomax
	 *           the costomax to set
	 */
    public void setCostoMax(double costomax) {
        this.costomax = costomax;
    }

    /**
	 * @param costomin
	 *           the costomin to set
	 */
    public void setCostoMin(double costomin) {
        this.costomin = costomin;
    }

    /**
	 * @param costointroduzione
	 *           the costointroduzione to set
	 */
    public void setCostoTaglio(int costointroduzione) {
        this.costotaglio = costointroduzione;
    }

    /**
	 * @param costomax
	 *           the costomax to set
	 */
    public void setGuadagnoMax(double costomax) {
        this.guadagnomax = costomax;
    }

    /**
	 * @param costomin
	 *           the costomin to set
	 */
    public void setGuadagnoMin(double costomin) {
        this.guadagnomin = costomin;
    }

    /**
	 * @param mesiinerzia
	 *           the mesiinerzia to set
	 */
    public void setMesiinerzia(int mesiinerzia) {
        assert this.mesiinerzia == -1 : "Non dovrebbe essere sovrascritto";
        this.mesiinerzia = mesiinerzia;
        this.mesiinerziaadesso = 1;
    }

    /**
	 * @param position
	 *           the position to set
	 */
    public void setPosition(CVertex position) {
        this.position = position;
    }

    /**
	 * @param val
	 *           the val to set
	 */
    public void setVal(double val) {
        this.val = val;
    }

    public History getHistory() {
        return hist_val;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    @Override
    public double getValmax() {
        return 0;
    }

    @Override
    public double getValmin() {
        return 0;
    }

    @Override
    public int isIncreasing() {
        Double val = Double.valueOf(this.val);
        Double prima = Double.valueOf(getHistoryVal(1));
        return (int) val.compareTo(prima);
    }

    public String getScaler() {
        return scaler;
    }

    public void setScaler(String scaler) {
        this.scaler = scaler;
    }

    public boolean isMoving() {
        return ismoving;
    }
}
