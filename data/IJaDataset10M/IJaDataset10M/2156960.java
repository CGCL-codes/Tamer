package br.com.sate.controller.struts2.action.inativar;

import br.com.sate.controller.cadastro.ControleCadastroGerente;
import br.com.sate.model.hibernate.persistense.Gerente;
import com.opensymphony.xwork2.ActionSupport;

public class InativarGerenteAction extends ActionSupport {

    private static final long serialVersionUID = -2028517256203469357L;

    public static final String SUCESSO = "sucesso";

    private Gerente gerente = new Gerente();

    private String matricula;

    private String mensagemErro;

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String execute() throws Exception {
        System.out.println(matricula);
        gerente = ControleCadastroGerente.recuperaPorMatricula(Integer.parseInt(matricula));
        if (gerente.getSituacao().equals("Ativo")) {
            gerente.setSituacao("Inativo");
            mensagemErro = ("Inativa��o do gerente " + gerente.getNome() + " realizada com sucesso!");
        } else {
            gerente.setSituacao("Ativo");
            mensagemErro = ("Ativa��o do gerente " + gerente.getNome() + " realizada com sucesso!");
        }
        ControleCadastroGerente.salvarGerente(gerente);
        return SUCESSO;
    }
}
