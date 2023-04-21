package galerias.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the historial_visita_obra database table.
 * 
 */
@Entity
@Table(name = "historial_visita_obra")
public class HistorialVisitaObra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String codigo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_visita")
    private Date fechaVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_galeria")
    private Galeria galeria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_obra")
    private Obra obra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_usuario")
    private Usuario usuario;

    public HistorialVisitaObra() {
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaVisita() {
        return this.fechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public Galeria getGaleria() {
        return this.galeria;
    }

    public void setGaleria(Galeria galeria) {
        this.galeria = galeria;
    }

    public Obra getObra() {
        return this.obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
