package bean;

import java.sql.Date;
import java.util.Vector;

public class Persona {

    private String nome;

    private String cognome;

    private Date dataNascita;

    private char sesso;

    private Vector<String> cittadinanza;

    private Indirizzo indirizzo;

    private Vector<Contatto> contatti;

    public Persona(String nome, String cognome, Date dataNascita, char sesso, Vector<String> citt, Indirizzo indirizzo, Vector<Contatto> contatti) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.cittadinanza = citt;
        this.indirizzo = indirizzo;
        this.contatti = contatti;
    }

    public Persona() {
        this.nome = null;
        this.cognome = null;
        this.dataNascita = null;
        this.sesso = ' ';
        this.cittadinanza = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public char getSesso() {
        return sesso;
    }

    public void setSesso(char sesso) {
        this.sesso = sesso;
    }

    public Vector<String> getCittadinanza() {
        return cittadinanza;
    }

    public void setCittadinanza(Vector<String> cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Vector<Contatto> getContatti() {
        return contatti;
    }

    public void setContatti(Vector<Contatto> contatti) {
        this.contatti = contatti;
    }
}
