package br.uniriotec.np2tec.propid.dao;

import br.uniriotec.np2tec.propid.shared.interfaces.IUserStore;

/**
 * Classe POJO que implementa a interface IUserStore, para ser utilizada pelo
 * ServiceExporte e pelo bean Remoto (VendasService) como local para armazenar
 * a chave do usuário passada no request.
 *
 * @author Felipe
 */
public class UserStore implements IUserStore {

    private String chaveUsuario;

    public String getChaveUsuario() {
        return chaveUsuario;
    }

    public void setChaveUsuario(String chaveUsuario) {
        this.chaveUsuario = chaveUsuario;
    }
}
