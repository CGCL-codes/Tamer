package jmash;

import jmash.interfaces.XmlAble;
import org.jdom.Element;

/**
 *
 * @author Alessandro
 */
public class YeastType implements XmlAble {

    /** Creates a new instance of YeastType */
    public YeastType() {
    }

    private String nome;

    private String codice;

    private String produttore;

    private String forma;

    private String categoria;

    private String descrizione;

    private Integer attenuazioneMed;

    private Integer attenuazioneMin;

    private Integer attenuazioneMax;

    private Integer temperaturaMin;

    private Integer temperaturaMax;

    private static String campiXml[] = { "nome", "codice", "produttore", "forma", "categoria", "descrizione", "attenuazioneMed", "attenuazioneMin", "attenuazioneMax", "temperaturaMin", "temperaturaMax" };

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodice() {
        return this.codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getProduttore() {
        return this.produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    public String getForma() {
        return this.forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getAttenuazioneMin() {
        return this.attenuazioneMin;
    }

    public void setAttenuazioneMin(Integer attenuazioneMin) {
        this.attenuazioneMin = attenuazioneMin;
    }

    public Integer getAttenuazioneMax() {
        return this.attenuazioneMax;
    }

    public void setAttenuazioneMax(Integer attenuazioneMax) {
        this.attenuazioneMax = attenuazioneMax;
    }

    public Integer getTemperaturaMin() {
        return this.temperaturaMin;
    }

    public void setTemperaturaMin(Integer temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public Integer getTemperaturaMax() {
        return this.temperaturaMax;
    }

    public void setTemperaturaMax(Integer temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public Integer getAttenuazioneMed() {
        return this.attenuazioneMed;
    }

    public void setAttenuazioneMed(Integer attenuazioneMed) {
        this.attenuazioneMed = attenuazioneMed;
    }

    public static String[] getCampiXml() {
        return campiXml;
    }

    public static void setCampiXml(String[] aCampiXml) {
        campiXml = aCampiXml;
    }

    public Element toXml() {
        try {
            return Utils.toXml(this, campiXml);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getTag() {
        return "yeasts";
    }

    public String[] getXmlFields() {
        return campiXml;
    }
}
