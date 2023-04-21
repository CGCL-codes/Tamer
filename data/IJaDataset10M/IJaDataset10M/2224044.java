package laboratorio.model;

import java.io.Serializable;

/**
 * @author colo
 *
 */
public class BeanUsuario extends BeanCrossWords implements Serializable {

    int id;

    int idPerfil;

    String dsNombre;

    String dsApellido;

    String cdUsuario;

    String dsClave;

    BeanPerfil perfil;

    /**
	 * @return Returns the dsApellido.
	 */
    public String getDsApellido() {
        return dsApellido;
    }

    /**
	 * @param dsApellido The dsApellido to set.
	 */
    public void setDsApellido(String dsApellido) {
        this.dsApellido = dsApellido;
    }

    /**
	 * @return Returns the dsNombre.
	 */
    public String getDsNombre() {
        return dsNombre;
    }

    /**
	 * @param dsNombre The dsNombre to set.
	 */
    public void setDsNombre(String dsNombre) {
        this.dsNombre = dsNombre;
    }

    /**
	 * @return Returns the id.
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return Returns the perfil.
	 */
    public BeanPerfil getPerfil() {
        return perfil;
    }

    /**
	 * @param perfil The perfil to set.
	 */
    public void setPerfil(BeanPerfil perfil) {
        this.perfil = perfil;
    }

    /**
	 * @return Returns the dsClave.
	 */
    public String getDsClave() {
        return dsClave;
    }

    /**
	 * @param dsClave The dsClave to set.
	 */
    public void setDsClave(String dsClave) {
        this.dsClave = dsClave;
    }

    /**
	 * @return Returns the cdUsuario.
	 */
    public String getCdUsuario() {
        return cdUsuario;
    }

    /**
	 * @param cdUsuario The cdUsuario to set.
	 */
    public void setCdUsuario(String cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }
}
