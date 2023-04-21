package com.paulo.cursofacens.bean;

import com.paulo.cursofacens.bean.enums.Permissao;
import java.util.Set;

/**
 *
 * @author visitante
 */
public class Usuario {

    private String login;

    private String senha;

    private Set<Permissao> permissao;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Permissao> getPermissao() {
        return permissao;
    }

    public void setPermissao(Set<Permissao> permissao) {
        this.permissao = permissao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
