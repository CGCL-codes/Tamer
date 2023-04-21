package br.com.sysconstruct.controle;

import br.com.sysconstruct.aplicacao.IAplCadastrarPais;
import br.com.sysconstruct.dominio.Pais;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;

/**
 *
 * @author Anderson Zanichelli
 */
@Model
public class CtrCadastrarPais {

    @EJB
    private IAplCadastrarPais aplCadastrarPais;

    private Pais pais = new Pais();

    public CtrCadastrarPais() {
    }

    public Pais getPais() {
        return pais;
    }

    public List<Pais> getListagem() {
        return aplCadastrarPais.list();
    }

    public void atualizarListagem() {
        aplCadastrarPais.atualizarListagem();
    }

    public String list() {
        return "/pais/list.xhtml";
    }

    public String adicionarPais() {
        return "/pais/add.xhtml";
    }

    public String salvar() {
        aplCadastrarPais.insert(pais);
        return "/pais/list.xhtml";
    }
}
