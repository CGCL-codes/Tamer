package entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id_vaga;

    @Column
    private int qnt_vagas;

    @ManyToOne(targetEntity = Anuncio.class)
    @JoinColumn(name = "id_anuncio")
    private Anuncio anuncio;

    private List<Horario> horario;

    private List<DiaSemana> diaSemana;

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public List<DiaSemana> getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(List<DiaSemana> diaSemana) {
        this.diaSemana = diaSemana;
    }

    public List<Horario> getHorario() {
        return horario;
    }

    public void setHorario(List<Horario> horario) {
        this.horario = horario;
    }

    public int getId_vaga() {
        return id_vaga;
    }

    public void setId_vaga(int id_vaga) {
        this.id_vaga = id_vaga;
    }

    public int getQnt_vagas() {
        return qnt_vagas;
    }

    public void setQnt_vagas(int qnt_vagas) {
        this.qnt_vagas = qnt_vagas;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vaga other = (Vaga) obj;
        if (this.id_vaga != other.id_vaga) {
            return false;
        }
        if (this.qnt_vagas != other.qnt_vagas) {
            return false;
        }
        if (this.anuncio != other.anuncio && (this.anuncio == null || !this.anuncio.equals(other.anuncio))) {
            return false;
        }
        if (this.horario != other.horario && (this.horario == null || !this.horario.equals(other.horario))) {
            return false;
        }
        if (this.diaSemana != other.diaSemana && (this.diaSemana == null || !this.diaSemana.equals(other.diaSemana))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id_vaga;
        hash = 79 * hash + this.qnt_vagas;
        hash = 79 * hash + (this.anuncio != null ? this.anuncio.hashCode() : 0);
        hash = 79 * hash + (this.horario != null ? this.horario.hashCode() : 0);
        hash = 79 * hash + (this.diaSemana != null ? this.diaSemana.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Vaga{" + "id_vaga=" + id_vaga + "qnt_vagas=" + qnt_vagas + "anuncio=" + anuncio + "horario=" + horario + "diaSemana=" + diaSemana + '}';
    }
}
