package br.absolut.apresentacao.mantemusuario;

import java.util.List;
import javax.faces.event.ActionEvent;
import br.absolut.apresentacao.seguranca.Seguranca;

public class ActConsultaUsuario extends Seguranca {

    private static final long serialVersionUID = -8430673951211159444L;

    private CtlMantemUsuario ctlUsuario;

    private List<DtoConsultaUsuario> listaUsuario;

    private String codUsuario;

    private int scrollerPage;

    public ActConsultaUsuario() throws Exception {
        super(Seguranca.PERFIL_ACESSO_GERENTE);
    }

    public String alterar() {
        if (codUsuario != null && !codUsuario.isEmpty()) return "atualizaUsuario";
        return "";
    }

    public void excluir(ActionEvent evt) {
        if (codUsuario != null && !codUsuario.isEmpty()) {
            ctlUsuario.excluiUsuario(new Long(codUsuario));
            codUsuario = null;
            addMsgInfo("Usu�rio exclu�do com sucesso!");
        }
    }

    public String atualizaResultado() throws Exception {
        listaUsuario = ctlUsuario.recuperaListaUsuario();
        return "";
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public List<DtoConsultaUsuario> getListaUsuario() {
        if (listaUsuario == null || listaUsuario.isEmpty()) listaUsuario = ctlUsuario.recuperaListaUsuario();
        return listaUsuario;
    }

    public void setListaUsuario(List<DtoConsultaUsuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public int getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

    public CtlMantemUsuario getCtlUsuario() {
        return ctlUsuario;
    }

    public void setCtlUsuario(CtlMantemUsuario ctlUsuario) {
        this.ctlUsuario = ctlUsuario;
    }
}
