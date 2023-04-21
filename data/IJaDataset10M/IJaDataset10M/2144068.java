package subfamilia1;

import java.util.ResourceBundle;
import utilidad.clasesBase.*;
import familia.*;

/**
 *
 * @author Sergio
 */
public class Subfamilia1VO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("subfamilia1/Bundle");

    private int idFamilia;

    private String nombreFamilia;

    private String nombre;

    public Subfamilia1VO() {
        inicializarComponetes();
    }

    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public void setFamilia(FamiliaVO familia) {
        if (familia != null) {
            this.idFamilia = familia.getId();
            this.nombreFamilia = familia.getNombre();
        } else {
            this.idFamilia = -1;
            this.nombreFamilia = "";
        }
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void inicializarComponetes() {
        super.inicializarComponetes();
        this.idFamilia = -1;
        this.nombreFamilia = "";
        this.nombre = "";
    }
}
