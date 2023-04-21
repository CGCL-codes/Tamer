package javagie.managedbeans.usuarios;

import javagie.entities.Usuario;
import javagie.services.FachadaService;
import javagie.util.ConstantesUtil;
import javagie.util.FacesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class EditarUsuarioBean {

    @Autowired
    private FachadaService service;

    @Autowired
    private FacesUtil facesUtil;

    private Usuario usuario;

    private String passwordAux;

    public String irEditar(Usuario usuario) {
        this.usuario = usuario;
        return "editar";
    }

    public String irNuevo() {
        this.usuario = new Usuario();
        return "nuevo";
    }

    public String guardar() {
        if (passwordIguales() != true) {
            facesUtil.addErrorMessage("contraseñas no son iguales");
            return null;
        }
        try {
            service.guardarUsuario(usuario);
            facesUtil.addInfoMessage(ConstantesUtil.MSJ_INFO_CAMBIOS_REALIZADOS);
            return "listar";
        } catch (Exception ex) {
            ex.printStackTrace();
            facesUtil.addErrorMessage(ConstantesUtil.MSJ_ERROR_INTERNO);
            return null;
        }
    }

    private boolean passwordIguales() {
        if (this.passwordAux == null || this.passwordAux.length() < 1) {
            return true;
        }
        if (usuario.getPassword().equals(this.passwordAux)) {
            return true;
        } else {
            return false;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPasswordAux(String passwordAux) {
        this.passwordAux = passwordAux;
    }

    public String getPasswordAux() {
        return passwordAux;
    }
}
