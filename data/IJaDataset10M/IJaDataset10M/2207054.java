package actions.professor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import listagem.ListagemInfo;
import servicos.interfaces.professor.IAreaCurso;
import constantes.EjbNames;
import actions.DefaultListagemAction;

public class AreaCursoListagemAction extends DefaultListagemAction {

    private static final long serialVersionUID = 1L;

    private String nome = "";

    public AreaCursoListagemAction() {
        this.listagem = new ListagemInfo();
        this.listagem.setPaginaAtual(1);
        this.listagem.setTamanhoPagina(3);
        this.listagem.setOrdenacao(null);
        this.listagem.setCrescente(true);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ListagemInfo buscarLista() throws NamingException {
        InitialContext ic = new InitialContext();
        IAreaCurso bean = (IAreaCurso) ic.lookup(EjbNames.AREACURSO);
        return bean.buscarAreaCursos(this.getNome(), this.listagem);
    }
}
