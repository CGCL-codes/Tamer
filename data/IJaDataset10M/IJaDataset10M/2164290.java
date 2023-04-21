package metso.paradigma.core.business.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SchedaOperatore implements Serializable {

    private static final long serialVersionUID = -3841467065124988758L;

    private int id;

    private String sesso;

    private Date dataNascita;

    private Indirizzo indirizzo;

    private String telefono;

    private Date inizioRapportoLavoro;

    private DisponibilitaSostituzione disponibilitaSostituzione;

    private DisponibilitaFestivita disponibilitaFestivi;

    private Formazioni formazioniOperatore;

    private Set<ConoscenzaTrasversale> conoscenzeTrasversali;

    private Set<EsperienzaPrecedente> esperienzePrecedenti;

    private Set<Competenza> competenze;

    private Valutazione valutazione;

    private LimitazioniTurno limitazioni;

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
	 * @param indirizzo the indirizzo to set
	 */
    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
	 * @return the indirizzo
	 */
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    /**
	 * @param telefono the telefono to set
	 */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
	 * @return the telefono
	 */
    public String getTelefono() {
        return telefono;
    }

    public Date getInizioRapportoLavoro() {
        return inizioRapportoLavoro;
    }

    public void setInizioRapportoLavoro(Date inizioRapportoLavoro) {
        this.inizioRapportoLavoro = inizioRapportoLavoro;
    }

    public DisponibilitaSostituzione getDisponibilitaSostituzione() {
        if (this.disponibilitaSostituzione == null) {
            this.disponibilitaSostituzione = new DisponibilitaSostituzione();
        }
        return this.disponibilitaSostituzione;
    }

    public void setDisponibilitaSostituzione(DisponibilitaSostituzione disponibilitaSostituzione) {
        this.disponibilitaSostituzione = disponibilitaSostituzione;
    }

    public DisponibilitaFestivita getDisponibilitaFestivi() {
        if (this.disponibilitaFestivi == null) {
            this.disponibilitaFestivi = new DisponibilitaFestivita();
        }
        return this.disponibilitaFestivi;
    }

    public void setDisponibilitaFestivi(DisponibilitaFestivita disponibilitaFestivi) {
        this.disponibilitaFestivi = disponibilitaFestivi;
    }

    public void setFormazioniOperatore(Formazioni formazioniOperatore) {
        this.formazioniOperatore = formazioniOperatore;
    }

    public Formazioni getFormazioniOperatore() {
        return formazioniOperatore;
    }

    public Set<ConoscenzaTrasversale> getConoscenzeTrasversali() {
        return conoscenzeTrasversali;
    }

    public void setConoscenzeTrasversali(Set<ConoscenzaTrasversale> conoscenzeTrasversali) {
        this.conoscenzeTrasversali = conoscenzeTrasversali;
    }

    public Set<EsperienzaPrecedente> getEsperienzePrecedenti() {
        if (this.esperienzePrecedenti == null) {
            this.esperienzePrecedenti = new HashSet<EsperienzaPrecedente>();
        }
        return esperienzePrecedenti;
    }

    public void setEsperienzePrecedenti(Set<EsperienzaPrecedente> esperienzePrecedenti) {
        this.esperienzePrecedenti = esperienzePrecedenti;
    }

    public Set<Competenza> getCompetenze() {
        return competenze;
    }

    public void setCompetenze(Set<Competenza> competenze) {
        this.competenze = competenze;
    }

    public Valutazione getValutazione() {
        return valutazione;
    }

    public void setValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLimitazioni(LimitazioniTurno limitazioni) {
        this.limitazioni = limitazioni;
    }

    public LimitazioniTurno getLimitazioni() {
        return limitazioni;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof SchedaOperatore) {
            SchedaOperatore schedaOperatore = (SchedaOperatore) obj;
            return (schedaOperatore.getId() == (this.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new Integer(this.getId()).hashCode();
    }
}
