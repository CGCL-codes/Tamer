package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import vo.TiendaVO;

@Entity
@Table(name = "tiendas")
public class Tienda implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codigo;

    private String nombre;

    private int ubicacionLatitud;

    private int ubicacionLongitud;

    private String pais;

    public Tienda(int codigo, String nombre, int ubicacionLatitud, int ubicacionLongitud, String pais) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacionLatitud = ubicacionLatitud;
        this.ubicacionLongitud = ubicacionLongitud;
        this.pais = pais;
    }

    public Tienda() {
    }

    @Id
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUbicacionLatitud() {
        return ubicacionLatitud;
    }

    public void setUbicacionLatitud(int ubicacionLatitud) {
        this.ubicacionLatitud = ubicacionLatitud;
    }

    public int getUbicacionLongitud() {
        return ubicacionLongitud;
    }

    public void setUbicacionLongitud(int ubicacionLongitud) {
        this.ubicacionLongitud = ubicacionLongitud;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Transient
    public TiendaVO getVO() {
        TiendaVO vo = new TiendaVO(codigo, nombre, ubicacionLatitud, ubicacionLongitud, pais);
        return vo;
    }

    public void setVO(TiendaVO vo) {
        this.codigo = vo.getCodigo();
        this.nombre = vo.getNombre();
        this.ubicacionLatitud = vo.getUbicacionLatitud();
        this.ubicacionLongitud = vo.getUbicacionLongitud();
        this.pais = vo.getPais();
    }
}
