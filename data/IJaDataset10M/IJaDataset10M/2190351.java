package br.com.sate.controller.struts2.action.cadastro;

import br.com.sate.controller.cadastro.ControleCadastroTecnico;
import br.com.sate.model.hibernate.persistense.Tecnico;
import com.opensymphony.xwork2.ActionSupport;

public class detalharTecnicoAction extends ActionSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = -539379336822520251L;

    public static final String SUCESSO = "sucesso";

    public static final String FALHA = "falha";

    private Tecnico tecnico = new Tecnico();

    private String matricula;

    private String nome;

    private String CPF;

    private String telefone;

    private String email;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String cPF) {
        CPF = cPF;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String execute() throws Exception {
        tecnico = ControleCadastroTecnico.recuperaPorMatricula(Integer.parseInt(matricula));
        setMatricula(tecnico.getMatricula().toString());
        setCPF(tecnico.getCPF());
        setNome(tecnico.getNome());
        setTelefone(tecnico.getTelefone());
        setEmail(tecnico.getEmail());
        return SUCESSO;
    }
}
