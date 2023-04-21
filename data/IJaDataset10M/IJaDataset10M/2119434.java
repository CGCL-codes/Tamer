package no.ugland.utransprod.model;

import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TakstolInfoV extends BaseObject {

    private static final long serialVersionUID = 1L;

    private TakstolInfoVPK takstolInfoVPK;

    private String leveringsadresse;

    private Integer kundenr;

    private String navn;

    private Integer hoydeOverHavet;

    private String beregnetFor;

    private Integer snolast;

    private Integer egenvekt;

    private Integer utstikkType;

    private String kode;

    private BigDecimal antall;

    private String prodno;

    private String beskrivelse;

    private String takstoltype;

    private BigDecimal virkesbredde;

    private BigDecimal spennvidde;

    private Integer utstikkslengde;

    private Integer svilleklaring;

    private Integer rombreddeAStol;

    private Integer romhoydeAStol;

    private String baeringGulv;

    private String isolasjonshoyde;

    private BigDecimal loddkutt;

    private BigDecimal nedstikk;

    private BigDecimal beregnetTid;

    private String memofilnavn;

    private String postnr;

    private String poststed;

    private String tlfKunde;

    private String tlfByggeplass;

    private Integer oensketUke;

    private String kundeRef;

    private String trossDrawer;

    private BigDecimal vinkel;

    private Integer maksHoyde;

    public String getLeveringsadresse() {
        return leveringsadresse;
    }

    public void setLeveringsadresse(String leveringsadresse) {
        this.leveringsadresse = leveringsadresse;
    }

    public Integer getKundenr() {
        return kundenr;
    }

    public void setKundenr(Integer kundenr) {
        this.kundenr = kundenr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Integer getHoydeOverHavet() {
        return hoydeOverHavet;
    }

    public void setHoydeOverHavet(Integer hoydeOverHavet) {
        this.hoydeOverHavet = hoydeOverHavet;
    }

    public String getBeregnetFor() {
        return beregnetFor;
    }

    public void setBeregnetFor(String beregnetfor) {
        this.beregnetFor = beregnetfor;
    }

    public Integer getSnolast() {
        return snolast;
    }

    public void setSnolast(Integer snolast) {
        this.snolast = snolast;
    }

    public Integer getEgenvekt() {
        return egenvekt;
    }

    public void setEgenvekt(Integer egenvekt) {
        this.egenvekt = egenvekt;
    }

    public Integer getUtstikkType() {
        return utstikkType;
    }

    public void setUtstikkType(Integer utstikkType) {
        this.utstikkType = utstikkType;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public BigDecimal getAntall() {
        return antall;
    }

    public void setAntall(BigDecimal antall) {
        this.antall = antall;
    }

    public String getProdno() {
        return prodno;
    }

    public void setProdno(String prodno) {
        this.prodno = prodno;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getTakstoltype() {
        return takstoltype;
    }

    public void setTakstoltype(String takstoltype) {
        this.takstoltype = takstoltype;
    }

    public BigDecimal getVirkesbredde() {
        return virkesbredde;
    }

    public void setVirkesbredde(BigDecimal virkesbredde) {
        this.virkesbredde = virkesbredde;
    }

    public BigDecimal getSpennvidde() {
        return spennvidde;
    }

    public void setSpennvidde(BigDecimal spennvidde) {
        this.spennvidde = spennvidde;
    }

    public Integer getUtstikkslengde() {
        return utstikkslengde;
    }

    public void setUtstikkslengde(Integer utstikkslengde) {
        this.utstikkslengde = utstikkslengde;
    }

    public Integer getSvilleklaring() {
        return svilleklaring;
    }

    public void setSvilleklaring(Integer svilleklaring) {
        this.svilleklaring = svilleklaring;
    }

    public Integer getRombreddeAStol() {
        return rombreddeAStol;
    }

    public void setRombreddeAStol(Integer rombreddeAStol) {
        this.rombreddeAStol = rombreddeAStol;
    }

    public Integer getRomhoydeAStol() {
        return romhoydeAStol;
    }

    public void setRomhoydeAStol(Integer romhoydeAStol) {
        this.romhoydeAStol = romhoydeAStol;
    }

    public String getBaeringGulv() {
        return baeringGulv;
    }

    public void setBaeringGulv(String baeringGulv) {
        this.baeringGulv = baeringGulv;
    }

    public String getIsolasjonshoyde() {
        return isolasjonshoyde;
    }

    public void setIsolasjonshoyde(String isolasjonshoyde) {
        this.isolasjonshoyde = isolasjonshoyde;
    }

    public BigDecimal getLoddkutt() {
        return loddkutt;
    }

    public void setLoddkutt(BigDecimal loddkutt) {
        this.loddkutt = loddkutt;
    }

    public BigDecimal getNedstikk() {
        return nedstikk;
    }

    public void setNedstikk(BigDecimal nedstikk) {
        this.nedstikk = nedstikk;
    }

    public BigDecimal getBeregnetTid() {
        return beregnetTid;
    }

    public void setBeregnetTid(BigDecimal beregnetTid) {
        this.beregnetTid = beregnetTid;
    }

    public String getMemofilnavn() {
        return memofilnavn;
    }

    public void setMemofilnavn(String memofilnavn) {
        this.memofilnavn = memofilnavn;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof TakstolInfoV)) return false;
        TakstolInfoV castOther = (TakstolInfoV) other;
        return new EqualsBuilder().append(takstolInfoVPK, castOther.takstolInfoVPK).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(takstolInfoVPK).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("takstolInfoVPK", takstolInfoVPK).append("leveringsadresse", leveringsadresse).append("kundenr", kundenr).append("navn", navn).append("hoydeOverHavet", hoydeOverHavet).append("beregnetfor", beregnetFor).append("snolast", snolast).append("egenvekt", egenvekt).append("utstikkType", utstikkType).append("kode", kode).append("antall", antall).append("prodno", prodno).append("beskrivelse", beskrivelse).append("takstoltype", takstoltype).append("virkesbredde", virkesbredde).append("spennvidde", spennvidde).append("utstikkslengde", utstikkslengde).append("svilleklaring", svilleklaring).append("rombreddeAStol", rombreddeAStol).append("romhoydeAStol", romhoydeAStol).append("baeringGulv", baeringGulv).append("isolasjonshoyde", isolasjonshoyde).append("loddkutt", loddkutt).append("nedstikk", nedstikk).append("beregnetTid", beregnetTid).append("memofilnavn", memofilnavn).toString();
    }

    public TakstolInfoVPK getTakstolInfoVPK() {
        return takstolInfoVPK;
    }

    public void setTakstolInfoVPK(TakstolInfoVPK takstolInfoVPK) {
        this.takstolInfoVPK = takstolInfoVPK;
    }

    public String getOrdernr() {
        return takstolInfoVPK.getOrdernr();
    }

    public String getPostnr() {
        return postnr;
    }

    public void setPostnr(String postnr) {
        this.postnr = postnr;
    }

    public String getPoststed() {
        return poststed;
    }

    public void setPoststed(String poststed) {
        this.poststed = poststed;
    }

    public String getTlfKunde() {
        return tlfKunde;
    }

    public void setTlfKunde(String tlfKunde) {
        this.tlfKunde = tlfKunde;
    }

    public String getTlfByggeplass() {
        return tlfByggeplass;
    }

    public void setTlfByggeplass(String tlfByggeplass) {
        this.tlfByggeplass = tlfByggeplass;
    }

    public Integer getOensketUke() {
        return oensketUke;
    }

    public void setOensketUke(Integer oensketUke) {
        this.oensketUke = oensketUke;
    }

    public String getKundeRef() {
        return kundeRef;
    }

    public void setKundeRef(String kundeRef) {
        this.kundeRef = kundeRef;
    }

    public String getTrossDrawer() {
        return trossDrawer;
    }

    public void setTrossDrawer(String trossDrawer) {
        this.trossDrawer = trossDrawer;
    }

    public BigDecimal getVinkel() {
        return vinkel;
    }

    public void setVinkel(BigDecimal vinkel) {
        this.vinkel = vinkel;
    }

    public Integer getMaksHoyde() {
        return maksHoyde;
    }

    public void setMaksHoyde(Integer maksHoyde) {
        this.maksHoyde = maksHoyde;
    }
}
