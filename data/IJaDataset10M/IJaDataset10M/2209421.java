package proiect.fis.commons.beans;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Tipula de date folosit in aplicatie
 * @author Daniela
 */
@Entity(name = "pachet")
@NamedQueries({ @NamedQuery(name = "Pachet.findAll", query = "Select e from pachet e") })
public class Pachet {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "expeditor")
    private Persoana expeditor;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "destinatar")
    private Persoana destinatar;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ruta")
    private List<Oras> ruta;

    @Enumerated
    @Column(name = "tip")
    private TipPachet tip;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "dataExpediere")
    private Date dataExpediere;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataSosire")
    private Date dataSosire;

    @Enumerated
    @Column(name = "status")
    private StatusPachet status;

    @Column(name = "greutate")
    private double gretutate;

    @Column(name = "pret")
    private double pret;

    public Date getDataExpediere() {
        return dataExpediere;
    }

    public void setDataExpediere(Date dataExpediere) {
        this.dataExpediere = dataExpediere;
    }

    public Date getDataSosire() {
        return dataSosire;
    }

    public void setDataSosire(Date dataSosire) {
        this.dataSosire = dataSosire;
    }

    public Persoana getDestinatar() {
        return destinatar;
    }

    public void setDestinatar(Persoana destinatar) {
        this.destinatar = destinatar;
    }

    public Persoana getExpeditor() {
        return expeditor;
    }

    public void setExpeditor(Persoana expeditor) {
        this.expeditor = expeditor;
    }

    public double getGretutate() {
        return gretutate;
    }

    public void setGretutate(double gretutate) {
        this.gretutate = gretutate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public List<Oras> getRuta() {
        return ruta;
    }

    public void setRuta(List<Oras> ruta) {
        this.ruta = ruta;
    }

    public StatusPachet getStatus() {
        return status;
    }

    public void setStatus(StatusPachet status) {
        this.status = status;
    }

    public TipPachet getTip() {
        return tip;
    }

    public void setTip(TipPachet tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "Pachet{" + "id=" + id + ", expeditor=" + expeditor + ", destinatar=" + destinatar + ", ruta=" + ruta + ", tip=" + tip + ", dataExpediere=" + dataExpediere + ", dataSosire=" + dataSosire + ", status=" + status + ", gretutate=" + gretutate + ", pret=" + pret + '}';
    }
}
