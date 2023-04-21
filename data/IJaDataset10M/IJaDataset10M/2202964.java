package br.com.sate.controller.struts2.action.listar;

import java.util.List;
import br.com.sate.controller.cadastro.ControleCadastroPerfil;
import br.com.sate.model.hibernate.persistense.Perfil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author SATE
 * 
 */
public class ListarPerfilAction extends ActionSupport {

    private static final long serialVersionUID = 6641411140402083094L;

    private static final String SUCESSO = "sucesso";

    private List<Perfil> perfilList;

    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    @Override
    public String execute() throws Exception {
        setPerfilList(ControleCadastroPerfil.recuperaTodosPerfil());
        return SUCESSO;
    }
}
