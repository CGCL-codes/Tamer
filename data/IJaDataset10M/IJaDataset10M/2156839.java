package domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the LOOKANDFEEL database table.
 * 
 */
@Entity
@Table(name = "LOOKANDFEEL")
public class Lookandfeel extends AbstractOggettoEntita implements Serializable, ILookandfeel {

    private static final long serialVersionUID = 1L;

    public static final String NOME_TABELLA = "lookAndFeel";

    public static final String ID = "idLook";

    public static final String NOME = "nome";

    public static final String VALORE = "valore";

    public static final String USATO = "usato";

    @Id
    @Column(name = "\"idLook\"", nullable = false)
    private int idLook;

    @Column(name = "\"nome\"", nullable = false)
    private String nome;

    @Column(name = "\"usato\"", nullable = false)
    private int usato;

    @Column(name = "\"valore\"", nullable = false)
    private String valore;

    public Lookandfeel() {
        if (idLook != 0) {
            this.idEntita = Integer.toString(idLook);
        }
    }

    @Override
    public int getidLook() {
        return this.idLook;
    }

    @Override
    public void setidLook(final int idLook) {
        this.idEntita = Integer.toString(idLook);
        this.idLook = idLook;
    }

    @Override
    public String getnome() {
        return this.nome;
    }

    @Override
    public void setnome(final String nome) {
        this.nome = nome;
    }

    @Override
    public int getusato() {
        return this.usato;
    }

    @Override
    public void setusato(final int usato) {
        this.usato = usato;
    }

    @Override
    public String getvalore() {
        return this.valore;
    }

    @Override
    public void setvalore(final String valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        return nome;
    }
}
