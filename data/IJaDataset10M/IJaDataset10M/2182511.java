package mf.torneo.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Play implements Serializable, Comparable {

    private static final long serialVersionUID = -1177178718199947478L;

    /** identifier field */
    private Integer idplay;

    /** nullable persistent field */
    private Integer punti1team1;

    /** nullable persistent field */
    private Integer punti2team1;

    /** nullable persistent field */
    private Integer punti1team2;

    /** nullable persistent field */
    private Integer punti2team2;

    /** nullable persistent field */
    private Integer campo;

    /** nullable persistent field */
    private String arbitro;

    /** nullable persistent field */
    private Date inizio;

    /** nullable persistent field */
    private Integer progressivo;

    /** persistent field */
    private mf.torneo.model.Gironi gironi;

    /** persistent field */
    private mf.torneo.model.Team teamByTeam1;

    /** persistent field */
    private mf.torneo.model.Team teamByTeam2;

    /**
	 * 
	 */
    public Play() {
    }

    /**
	 * @param campo
	 * @param arbitro
	 * @param inizio
	 * @param gironi
	 * @param teamByTeam1
	 * @param teamByTeam2
	 */
    public Play(Integer campo, String arbitro, Date inizio, mf.torneo.model.Gironi gironi, mf.torneo.model.Team teamByTeam1, mf.torneo.model.Team teamByTeam2, Integer progressivo) {
        super();
        this.campo = campo;
        this.arbitro = arbitro;
        this.inizio = inizio;
        this.gironi = gironi;
        this.teamByTeam1 = teamByTeam1;
        this.teamByTeam2 = teamByTeam2;
        this.progressivo = progressivo;
    }

    /** full constructor */
    public Play(Integer idplay, Integer punti1team1, Integer punti2team1, Integer punti1team2, Integer punti2team2, Integer campo, String arbitro, Date inizio, mf.torneo.model.Gironi gironi, mf.torneo.model.Team teamByTeam1, mf.torneo.model.Team teamByTeam2) {
        this.idplay = idplay;
        this.punti1team1 = punti1team1;
        this.punti2team1 = punti2team1;
        this.punti1team2 = punti1team2;
        this.punti2team2 = punti2team2;
        this.campo = campo;
        this.arbitro = arbitro;
        this.inizio = inizio;
        this.gironi = gironi;
        this.teamByTeam1 = teamByTeam1;
        this.teamByTeam2 = teamByTeam2;
    }

    /** minimal constructor */
    public Play(Integer idplay, mf.torneo.model.Gironi gironi, mf.torneo.model.Team teamByTeam1, mf.torneo.model.Team teamByTeam2) {
        this.idplay = idplay;
        this.gironi = gironi;
        this.teamByTeam1 = teamByTeam1;
        this.teamByTeam2 = teamByTeam2;
    }

    public int getSetVintiTeam1() {
        if (punti1team1 == null) return 0;
        return (punti1team1.intValue() > punti1team2.intValue() ? 1 : 0) + (punti2team1.intValue() > punti2team2.intValue() ? 1 : 0);
    }

    public int getSetVintiTeam2() {
        if (punti1team2 == null || punti2team1 == null) return 0;
        return (punti1team2.intValue() > punti1team1.intValue() ? 1 : 0) + (punti2team2.intValue() > punti2team1.intValue() ? 1 : 0);
    }

    public int getSetPersiTeam2() {
        return getSetVintiTeam1();
    }

    public int getSetPersiTeam1() {
        return getSetVintiTeam2();
    }

    public int getTotPuntiTeam1() {
        return (punti1team1 == null ? 0 : punti1team1.intValue()) + (punti2team1 == null ? 0 : punti2team1.intValue());
    }

    public int getTotPuntiTeam2() {
        return (punti1team2 == null ? 0 : punti1team2.intValue()) + (punti2team2 == null ? 0 : punti2team2.intValue());
    }

    public int getTotPuntiSubitiTeam1() {
        return getTotPuntiTeam2();
    }

    public int getTotPuntiSubitiTeam2() {
        return getTotPuntiTeam1();
    }

    /**
	 * stabilisce il vincitore
	 * @return
	 * >0 se vince team1
	 * =0 se pareggiano
	 * <0 se vince team2
	 */
    public int getWinner() {
        int tot1 = getSetVintiTeam1() * 10000 + getTotPuntiTeam1() * 100;
        int tot2 = getSetVintiTeam2() * 10000 + getTotPuntiTeam2() * 100;
        return tot1 - tot2;
    }

    public int getPuntiPartitaTeam1() {
        int xx = getWinner();
        return (xx > 0 ? 2 : (xx == 0 ? 1 : 0));
    }

    public int getPuntiPartitaTeam2() {
        int xx = getWinner();
        return (xx < 0 ? 2 : (xx == 0 ? 1 : 0));
    }

    public Integer getIdplay() {
        return this.idplay;
    }

    public void setIdplay(Integer idplay) {
        this.idplay = idplay;
    }

    public Integer getPunti1team1() {
        return this.punti1team1;
    }

    public void setPunti1team1(Integer punti1team1) {
        this.punti1team1 = punti1team1;
    }

    public Integer getPunti2team1() {
        return this.punti2team1;
    }

    public void setPunti2team1(Integer punti2team1) {
        this.punti2team1 = punti2team1;
    }

    public Integer getPunti1team2() {
        return this.punti1team2;
    }

    public void setPunti1team2(Integer punti1team2) {
        this.punti1team2 = punti1team2;
    }

    public Integer getPunti2team2() {
        return this.punti2team2;
    }

    public void setPunti2team2(Integer punti2team2) {
        this.punti2team2 = punti2team2;
    }

    public Integer getCampo() {
        return this.campo;
    }

    public void setCampo(Integer campo) {
        this.campo = campo;
    }

    public String getArbitro() {
        return this.arbitro;
    }

    public void setArbitro(String arbitro) {
        this.arbitro = arbitro;
    }

    public Date getInizio() {
        return this.inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public mf.torneo.model.Gironi getGironi() {
        return this.gironi;
    }

    public void setGironi(mf.torneo.model.Gironi gironi) {
        this.gironi = gironi;
    }

    public mf.torneo.model.Team getTeamByTeam1() {
        return this.teamByTeam1;
    }

    public void setTeamByTeam1(mf.torneo.model.Team teamByTeam1) {
        this.teamByTeam1 = teamByTeam1;
    }

    public mf.torneo.model.Team getTeamByTeam2() {
        return this.teamByTeam2;
    }

    public void setTeamByTeam2(mf.torneo.model.Team teamByTeam2) {
        this.teamByTeam2 = teamByTeam2;
    }

    public String toString() {
        return new ToStringBuilder(this).append("idplay", getIdplay()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Play)) return false;
        Play castOther = (Play) other;
        return new EqualsBuilder().append(this.getIdplay(), castOther.getIdplay()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getIdplay()).toHashCode();
    }

    public int compareTo(Object other) {
        if (!(other instanceof Play)) return 0;
        Play castOther = (Play) other;
        return this.getIdplay().compareTo(castOther.getIdplay());
    }
}
