package menfis.view.mbean;

import java.util.List;
import menfis.model.bean.Funcionario;
import smi.view.util.FacesUtils;

/**
 *
 * @author lpelegrini
 */
public class FuncionarioMBean extends BaseMBean {

    private int chapa;

    private String nome;

    private String email;

    private String loginUsuario;

    private String senha;

    private int ramal;

    private boolean gerente;

    private boolean diretor;

    private boolean ativo;

    private int setor;

    private List funcionarios;

    /** Creates a new instance of FuncionarioManagedBean */
    public FuncionarioMBean() {
        super();
    }

    public String doLogin() {
        if (!validateLogin()) {
            return Constants.FAIL;
        }
        Funcionario func = this.serviceLocator.getFuncionarioService().doLogin(loginUsuario, senha);
        if (func == null) {
            FacesUtils.addErrorMessage("Login ou senha inv�lida, verifique!");
            return Constants.FAIL;
        } else {
            SessionMBean se = FacesUtils.getSessionBean();
            se.setFuncionario(func);
            return "success";
        }
    }

    private boolean validateLogin() {
        boolean valid = true;
        if (loginUsuario == null || loginUsuario.equals("")) {
            FacesUtils.addErrorMessage("Login deve ser preenchido");
            valid = false;
        }
        if (senha == null || senha.equals("")) {
            FacesUtils.addErrorMessage("Senha deve ser preenchida");
            valid = false;
        }
        return valid;
    }

    public List getFuncionarios() {
        funcionarios = this.serviceLocator.getFuncionarioService().listAll();
        return funcionarios;
    }

    public void setFuncionarios(List funcionarios) {
        this.funcionarios = funcionarios;
    }

    public String getNome() {
        return nome;
    }

    public int getChapa() {
        return chapa;
    }

    public void setChapa(int chapa) {
        this.chapa = chapa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getRamal() {
        return ramal;
    }

    public void setRamal(int ramal) {
        this.ramal = ramal;
    }

    public boolean isGerente() {
        return gerente;
    }

    public void setGerente(boolean gerente) {
        this.gerente = gerente;
    }

    public boolean isDiretor() {
        return diretor;
    }

    public void setDiretor(boolean diretor) {
        this.diretor = diretor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getSetor() {
        return setor;
    }

    public void setSetor(int setor) {
        this.setor = setor;
    }
}
