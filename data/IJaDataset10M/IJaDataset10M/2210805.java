package br.com.sysconstruct.dominio;

import br.com.sysconstruct.enumeration.Funcao;
import br.com.sysconstruct.utils.CriptoMD5;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anderson Zanichelli
 */
@Entity
public class Usuario extends Pessoa {

    @NotNull
    @Size(min = 4, max = 8)
    @Column(name = "CLOGIN", unique = true)
    private String cLogin;

    @NotNull
    @Size(min = 1)
    private String cPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Funcao> funcoes;

    public Set<Funcao> getFuncoes() {
        return funcoes;
    }

    public void setFuncoes(Set<Funcao> funcoes) {
        this.funcoes = funcoes;
    }

    public String getcLogin() {
        return cLogin;
    }

    public void setcLogin(String cLogin) {
        this.cLogin = cLogin;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }
}
