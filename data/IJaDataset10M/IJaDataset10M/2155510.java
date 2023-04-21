package basededatostg;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Vinculos.findAll", query = "select o from Vinculos o") })
public class Vinculos implements Serializable {

    private Long contador;

    private Timestamp fecha;

    @Id
    @Column(name = "ID_VINCULO", nullable = false)
    private Long idVinculo;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuariopicm usuariopicm;

    public Vinculos() {
    }

    public Vinculos(Long contador, Timestamp fecha, Usuariopicm usuariopicm, Long idVinculo, String nombre) {
        this.contador = contador;
        this.fecha = fecha;
        this.usuariopicm = usuariopicm;
        this.idVinculo = idVinculo;
        this.nombre = nombre;
    }

    public Long getContador() {
        return contador;
    }

    public void setContador(Long contador) {
        this.contador = contador;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Long getIdVinculo() {
        return idVinculo;
    }

    public void setIdVinculo(Long idVinculo) {
        this.idVinculo = idVinculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuariopicm getUsuariopicm() {
        return usuariopicm;
    }

    public void setUsuariopicm(Usuariopicm usuariopicm) {
        this.usuariopicm = usuariopicm;
    }
}
