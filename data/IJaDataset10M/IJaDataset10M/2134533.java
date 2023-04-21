package domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the utente database table.
 * 
 */
@Entity
@Table(name = "utente")
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int idutente;

    @Column(nullable = false, length = 45)
    private String cCognome;

    @Column(length = 45)
    private String cIndirizzo;

    @Column(length = 45)
    private String cmail;

    @Column(nullable = false, length = 60)
    private String cNickname;

    @Column(nullable = false, length = 45)
    private String cNome;

    @Column(nullable = false, length = 60)
    private String cPassword;

    private double dCassa;

    @Column(length = 45)
    private String dDataNasciata;

    @OneToMany(mappedBy = "utente")
    private Set<Operazione> operaziones;

    @OneToMany(mappedBy = "utente1")
    private Set<Partita> partitas1;

    @OneToMany(mappedBy = "utente2")
    private Set<Partita> partitas2;

    @OneToMany(mappedBy = "utente3")
    private Set<Partita> partitas3;

    @OneToMany(mappedBy = "utente4")
    private Set<Partita> partitas4;

    @OneToMany(mappedBy = "utente5")
    private Set<Partita> partitas5;

    @OneToMany(mappedBy = "utente6")
    private Set<Partita> partitas6;

    public Utente() {
    }

    public int getIdutente() {
        return this.idutente;
    }

    public void setIdutente(int idutente) {
        this.idutente = idutente;
    }

    public String getCCognome() {
        return this.cCognome;
    }

    public void setCCognome(String cCognome) {
        this.cCognome = cCognome;
    }

    public String getCIndirizzo() {
        return this.cIndirizzo;
    }

    public void setCIndirizzo(String cIndirizzo) {
        this.cIndirizzo = cIndirizzo;
    }

    public String getCmail() {
        return this.cmail;
    }

    public void setCmail(String cmail) {
        this.cmail = cmail;
    }

    public String getCNickname() {
        return this.cNickname;
    }

    public void setCNickname(String cNickname) {
        this.cNickname = cNickname;
    }

    public String getCNome() {
        return this.cNome;
    }

    public void setCNome(String cNome) {
        this.cNome = cNome;
    }

    public String getCPassword() {
        return this.cPassword;
    }

    public void setCPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public double getDCassa() {
        return this.dCassa;
    }

    public void setDCassa(double dCassa) {
        this.dCassa = dCassa;
    }

    public String getDDataNasciata() {
        return this.dDataNasciata;
    }

    public void setDDataNasciata(String dDataNasciata) {
        this.dDataNasciata = dDataNasciata;
    }

    public Set<Operazione> getOperaziones() {
        return this.operaziones;
    }

    public void setOperaziones(Set<Operazione> operaziones) {
        this.operaziones = operaziones;
    }

    public Set<Partita> getPartitas1() {
        return this.partitas1;
    }

    public void setPartitas1(Set<Partita> partitas1) {
        this.partitas1 = partitas1;
    }

    public Set<Partita> getPartitas2() {
        return this.partitas2;
    }

    public void setPartitas2(Set<Partita> partitas2) {
        this.partitas2 = partitas2;
    }

    public Set<Partita> getPartitas3() {
        return this.partitas3;
    }

    public void setPartitas3(Set<Partita> partitas3) {
        this.partitas3 = partitas3;
    }

    public Set<Partita> getPartitas4() {
        return this.partitas4;
    }

    public void setPartitas4(Set<Partita> partitas4) {
        this.partitas4 = partitas4;
    }

    public Set<Partita> getPartitas5() {
        return this.partitas5;
    }

    public void setPartitas5(Set<Partita> partitas5) {
        this.partitas5 = partitas5;
    }

    public Set<Partita> getPartitas6() {
        return this.partitas6;
    }

    public void setPartitas6(Set<Partita> partitas6) {
        this.partitas6 = partitas6;
    }
}
