package Datos;

import java.io.Serializable;

public class UsuarioxcursoPK implements Serializable {

    public Long idcurso;

    public Long idusuario;

    public UsuarioxcursoPK() {
    }

    public UsuarioxcursoPK(Long idcurso, Long idusuario) {
        this.idcurso = idcurso;
        this.idusuario = idusuario;
    }

    public boolean equals(Object other) {
        if (other instanceof UsuarioxcursoPK) {
            final UsuarioxcursoPK otherUsuarioxcursoPK = (UsuarioxcursoPK) other;
            final boolean areEqual = (otherUsuarioxcursoPK.idcurso.equals(idcurso) && otherUsuarioxcursoPK.idusuario.equals(idusuario));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }
}
