package der.ponto.backBeans;

import util.jsf.BackBeanViewPage;

public class LoginFuncionarioBean extends BackBeanViewPage {

    private boolean funcionario = false;

    private String matricula = "";

    public LoginFuncionarioBean() {
    }

    public static LoginFuncionarioBean getInstance() {
        return (LoginFuncionarioBean) evalEL("#{loginFuncionarioBean}");
    }

    public boolean isFuncionario() {
        return funcionario;
    }

    public void setFuncionario(boolean funcionario) {
        this.funcionario = funcionario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
