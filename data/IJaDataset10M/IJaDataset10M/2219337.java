package sbc.corso.shareable;

import corso.lang.CorsoConnection;
import corso.lang.CorsoData;
import corso.lang.CorsoDataException;
import corso.lang.CorsoShareable;
import corso.lang.CorsoTopTransaction;
import corso.lang.CorsoVarOid;
import sbc.corso.CorsoUtil;

public class Sensor implements CorsoShareable {

    private String id;

    private String status;

    private String TemperaturMessung;

    private String Helligkeitsmessung;

    private String Feuchtigkeitsmessung;

    private String tgeprueftam;

    private String hgeprueftam;

    private String fgeprueftam;

    private String tgeprueftvon;

    private String hgeprueftvon;

    private String fgeprueftvon;

    public Sensor(String id, String status) {
        this.id = id;
        this.status = status;
        this.TemperaturMessung = CorsoUtil.WIRD_GEMESSEN_ABER_NOCH_NICHT_GEPRUEFT;
        this.Helligkeitsmessung = CorsoUtil.WIRD_NICHT_GEMESSEN;
        this.Feuchtigkeitsmessung = CorsoUtil.WIRD_GEMESSEN_ABER_NOCH_NICHT_GEPRUEFT;
        this.tgeprueftam = "";
        this.tgeprueftvon = "";
        this.hgeprueftam = "";
        this.hgeprueftvon = "";
        this.fgeprueftam = "";
        this.fgeprueftvon = "";
    }

    public Sensor(String id, String status, String tm, String fm, String hm) {
        this.id = id;
        this.status = status;
        this.TemperaturMessung = tm;
        this.Helligkeitsmessung = hm;
        this.Feuchtigkeitsmessung = fm;
    }

    public Sensor(String id, String status, CorsoConnection con) {
        this.id = id;
        this.status = status;
        this.TemperaturMessung = CorsoUtil.WIRD_GEMESSEN_ABER_NOCH_NICHT_GEPRUEFT;
        this.Helligkeitsmessung = CorsoUtil.WIRD_NICHT_GEMESSEN;
        this.Feuchtigkeitsmessung = CorsoUtil.WIRD_GEMESSEN_ABER_NOCH_NICHT_GEPRUEFT;
    }

    public Sensor(CorsoVarOid oid) {
        try {
            oid.setTimeStamp(0);
            oid.readShareable(this, null, CorsoConnection.NO_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-8);
        }
    }

    public Sensor(CorsoVarOid oid, CorsoTopTransaction tx) {
        try {
            oid.setTimeStamp(0);
            oid.readShareable(this, tx, CorsoConnection.NO_TIMEOUT);
        } catch (Exception e) {
            System.exit(-9);
        }
    }

    public void read(CorsoData data) throws CorsoDataException {
        StringBuffer structName = new StringBuffer();
        int arity = data.getStructTag(structName);
        if (arity != 11) System.exit(-1);
        id = data.getString();
        status = data.getString();
        TemperaturMessung = data.getString();
        Helligkeitsmessung = data.getString();
        Feuchtigkeitsmessung = data.getString();
        tgeprueftam = data.getString();
        hgeprueftam = data.getString();
        fgeprueftam = data.getString();
        tgeprueftvon = data.getString();
        hgeprueftvon = data.getString();
        fgeprueftvon = data.getString();
    }

    public void write(CorsoData data) throws CorsoDataException {
        data.putStructTag("Sensor", 11);
        data.putString(id);
        data.putString(status);
        data.putString(TemperaturMessung);
        data.putString(Helligkeitsmessung);
        data.putString(Feuchtigkeitsmessung);
        data.putString(tgeprueftam);
        data.putString(hgeprueftam);
        data.putString(fgeprueftam);
        data.putString(tgeprueftvon);
        data.putString(hgeprueftvon);
        data.putString(fgeprueftvon);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemperaturMessung() {
        return TemperaturMessung;
    }

    public void setTemperaturMessung(String TemperaturMessung) {
        this.TemperaturMessung = TemperaturMessung;
    }

    public String getFeuchtigkeitsmessung() {
        return Feuchtigkeitsmessung;
    }

    public void setFeuchtigkeitsmessung(String Feuchtigkeitsmessung) {
        this.Feuchtigkeitsmessung = Feuchtigkeitsmessung;
    }

    public String getHelligkeitsmessung() {
        return Helligkeitsmessung;
    }

    public void setHelligkeitsmessung(String Helligkeitsmessung) {
        this.Helligkeitsmessung = Helligkeitsmessung;
    }

    public String getTgeprueftam() {
        return tgeprueftam;
    }

    public void setTgeprueftam(String tgeprueftam) {
        this.tgeprueftam = tgeprueftam;
    }

    public String getFgeprueftam() {
        return fgeprueftam;
    }

    public void setFgeprueftam(String fgeprueftam) {
        this.fgeprueftam = fgeprueftam;
    }

    public String getHgeprueftam() {
        return hgeprueftam;
    }

    public void setHgeprueftam(String hgeprueftam) {
        this.hgeprueftam = hgeprueftam;
    }

    public String getTgeprueftvon() {
        return tgeprueftvon;
    }

    public void setTgeprueftvon(String tgeprueftvon) {
        this.tgeprueftvon = tgeprueftvon;
    }

    public String getFgeprueftvon() {
        return fgeprueftvon;
    }

    public void setFgeprueftvon(String fgeprueftvon) {
        this.fgeprueftvon = fgeprueftvon;
    }

    public String getHgeprueftvon() {
        return hgeprueftvon;
    }

    public void setHgeprueftvon(String hgeprueftvon) {
        this.hgeprueftvon = hgeprueftvon;
    }
}
