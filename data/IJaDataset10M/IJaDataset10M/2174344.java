package br.org.rfccj.siap.web.struts2.actions;

import java.util.Collection;
import java.util.Vector;
import br.org.rfccj.siap.Endereco;

public class PesquisaDeEnderecosAction extends PrevaylerAction {

    private static final long serialVersionUID = 1L;

    private String descricaoParcial;

    private Collection<Endereco> enderecos;

    public void setDescricaoParcial(String descricaoParcial) {
        this.descricaoParcial = descricaoParcial.toLowerCase();
    }

    public Collection<Endereco> getPacientes() {
        return enderecos;
    }

    public String execute() throws Exception {
        enderecos = new Vector<Endereco>();
        for (Endereco endereco : contexto().enderecos().ordenadosPorNome()) if (endereco.getDescricao().toLowerCase().contains(descricaoParcial)) enderecos.add(endereco);
        return SUCCESS;
    }
}
