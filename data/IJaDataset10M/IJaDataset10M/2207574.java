package es.uned.dia.pfcdbenito6.core.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.log4j.Logger;

/**
 * Bean JAXB del ICronItemBean
 * @author David Benito Le&oacute;n
 */
@XmlRootElement(name = IJAXBBean.CRONCFG_ROOT)
public class CronItemBean implements IJAXBBean, ICronItemBean {

    private static Logger logger = Logger.getLogger(CronItemBean.class);

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String TIME_FORMAT = "HH:mm";

    private String name;

    private String startTime;

    private String firstDate;

    private String lastDate;

    private int daysBtwFull;

    private boolean incrFromFull;

    private int daysBtwIncr;

    private int fullsToMaintain;

    private String nextExecDate;

    private String lastExecDate;

    private String lastExecFullDate;

    @XmlTransient
    private SimpleDateFormat sdfDate;

    @XmlTransient
    private SimpleDateFormat sdfTime;

    @XmlTransient
    private SimpleDateFormat sdfTimeStamp;

    private BackupCfgBean backupCfgBean;

    public CronItemBean() {
        sdfDate = new SimpleDateFormat(DATE_FORMAT);
        sdfTime = new SimpleDateFormat(TIME_FORMAT);
        sdfTimeStamp = new SimpleDateFormat(DATE_FORMAT + TIME_FORMAT);
    }

    public void postJAXBunmarshalling() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        try {
            this.sdfTime.parse(startTime);
            this.startTime = startTime;
        } catch (Exception e) {
            this.startTime = "";
        }
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        try {
            this.sdfDate.parse(firstDate);
            this.firstDate = firstDate;
        } catch (Exception e) {
            this.firstDate = "";
        }
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        try {
            this.sdfDate.parse(lastDate);
            this.lastDate = lastDate;
        } catch (Exception e) {
            this.lastDate = "";
        }
    }

    public int getDaysBtwFull() {
        return daysBtwFull;
    }

    public void setDaysBtwFull(int daysBtwFull) {
        this.daysBtwFull = daysBtwFull;
    }

    public boolean isIncrFromFull() {
        return incrFromFull;
    }

    public void setIncrFromFull(boolean incrFromFull) {
        this.incrFromFull = incrFromFull;
    }

    /**Devuelve la configuraci�n del backup a llevar a cabo*/
    public BackupCfgBean getBackupCfgBean() {
        return backupCfgBean;
    }

    /**Asigna la configuraci�n del backup a llevar a cabo*/
    public void setBackupCfgBean(BackupCfgBean backupCfgBean) {
        this.backupCfgBean = backupCfgBean;
    }

    public int getDaysBtwIncr() {
        return daysBtwIncr;
    }

    public void setDaysBtwIncr(int daysBtwIncr) {
        this.daysBtwIncr = daysBtwIncr;
    }

    public int getFullsToMaintain() {
        return fullsToMaintain;
    }

    public void setFullsToMaintain(int fullsToMaintain) {
        this.fullsToMaintain = fullsToMaintain;
    }

    public String getLastExecDate() {
        return lastExecDate;
    }

    public void setLastExecDate(String lastExecDate) {
        this.lastExecDate = lastExecDate;
    }

    public String getLastExecFullDate() {
        return lastExecFullDate;
    }

    public void setLastExecFullDate(String lastExecFullDate) {
        this.lastExecFullDate = lastExecFullDate;
    }

    public String getNextExecDate() {
        return nextExecDate;
    }

    public void setNextExecDate(String nextExecDate) {
        this.nextExecDate = nextExecDate;
    }

    /**
     * Concatenando FirstDate y StartTime se genera el timestamp asociado a la fecha de la primera ejecuci�n.
     * @return Los milis asociados al ts de primera ejecuci�n.
     * @throws ParseException Si alg�n campo est� vac�o o no cumple con el formato esperado.
     */
    public long getFirstTimeStamp() throws ParseException {
        return sdfTimeStamp.parse(getFirstDate() + getStartTime()).getTime();
    }

    /**
     * Concatenando LastDate y StartTime se genera el timestamp asociado a la fecha de la �ltima ejecuci�n (tras ella no habr� m�s).
     * @return Los milis asociados al ts de la �ltima ejecuci�n.
     * @throws ParseException Si alg�n campo est� vac�o o no cumple con el formato esperado.
     */
    public long getFinalTimeStamp() throws ParseException {
        return sdfTimeStamp.parse(getLastDate() + getStartTime()).getTime();
    }

    /**
     * Concatenando LastExecDate y StartTime se genera el timestamp asociado a la fecha de la ejecuci�n m�s reciente.
     * @return Los milis asociados al ts de ejecuci�n m�s reciente.
     * @throws ParseException Si alg�n campo est� vac�o o no cumple con el formato esperado.
     */
    public long getLastExecTimeStamp() throws ParseException {
        return sdfTimeStamp.parse(getLastExecDate() + getStartTime()).getTime();
    }

    /**
     * Concatenando LastExecFullDate y StartTime se genera el timestamp asociado a la fecha de la ejecuci�n FULL m�s reciente.
     * @return Los milis asociados al ts de ejecuci�n FULL m�s reciente.
     * @throws ParseException Si alg�n campo est� vac�o o no cumple con el formato esperado.
     */
    public long getLastExecFullTimeStamp() throws ParseException {
        return sdfTimeStamp.parse(getLastExecFullDate() + getStartTime()).getTime();
    }

    /**
     * Concatenando NextExecDate y StartTime se genera el timestamp asociado a la fecha de la pr�xima ejecuci�n.
     * @return Los milis asociados al ts de la pr�xima ejecuci�n.
     * @throws ParseException Si alg�n campo est� vac�o o no cumple con el formato esperado.
     */
    public long getNextExecTimeStamp() throws ParseException {
        return sdfTimeStamp.parse(getNextExecDate() + getStartTime()).getTime();
    }

    /**
	 * Actualiza los TS del bean, seg�n si se ejecut� un FULL o un INCR:
	 * 
	 *  ii.	NEXT = �fechaActual� + DiasEntreIncrementales (o DiasEntreCompletos).
	 * iii.	LAST = �fechaActual�.
	 *  iv.	LAST_COMPLETO=�fechaActual�
	 *  
	 * @param wasFull TRUE si lo que se ejecut� fue una copia FULL. FALSE si fue una INCR.
	 * 
	 * @return TRUE si los timestamps se actualizan correctamente.
	 */
    public boolean updateTimestamps(boolean wasFull) {
        try {
            long lNow = System.currentTimeMillis();
            String sNow = this.sdfDate.format(lNow);
            this.lastExecDate = sNow;
            if (wasFull) {
                this.lastExecFullDate = sNow;
            }
            int nDias = (this.daysBtwIncr > 0 ? Math.min(this.daysBtwIncr, this.daysBtwIncr) : this.daysBtwFull);
            this.nextExecDate = this.sdfDate.format(new Date(lNow + 24L * 60 * 60 * 1000 * nDias));
            if (nDias == 0) throw new ParseException("D�as entre completos vale 0", 0);
            return true;
        } catch (Exception e) {
            logger.error("Error asignando fecha de pr�xima ejecuci�n: " + e.toString(), e);
            return false;
        }
    }
}
