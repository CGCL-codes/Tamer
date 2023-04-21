package br.ufpr.biblioteca.util;

import org.displaytag.decorator.TableDecorator;
import br.ufpr.biblioteca.bean.AutorBean;
import br.ufpr.biblioteca.bean.EditoraBean;
import br.ufpr.biblioteca.bean.ExemplarBean;
import br.ufpr.biblioteca.bean.FornecedorBean;
import br.ufpr.biblioteca.bean.FuncionarioBean;
import br.ufpr.biblioteca.bean.PerfilBean;
import br.ufpr.biblioteca.bean.PessoaBean;
import br.ufpr.biblioteca.bean.StatusBean;
import br.ufpr.biblioteca.bean.TipoBean;
import br.ufpr.biblioteca.bean.UsuarioBean;

public class Decorador extends TableDecorator {

    public String getLinkPessoa() {
        PessoaBean pb = (PessoaBean) getCurrentRowObject();
        int pbId = pb.getId();
        return "<a href=\"controladora?idPessoa=" + pbId + "&redirecionador=EmprestimoControl&comando=Pesquisar\">Selecionar</a>";
    }

    public String getLinkEmprestimo() {
        ExemplarBean eb = (ExemplarBean) getCurrentRowObject();
        int ebCodigo = eb.getCodigoExemplar();
        if (eb.getStatus().equals("Dispon�vel")) {
            return "<a href=\"controladora?id=" + ebCodigo + "&redirecionador=EmprestimoControl&comando=Remover\">Remover</a>";
        } else {
            return "-----------";
        }
    }

    public String getLinkExemplar() {
        ExemplarBean eb = (ExemplarBean) getCurrentRowObject();
        int ebCodigo = eb.getCodigoExemplar();
        if (eb.getStatus().equals("Dispon�vel")) {
            return "<a href=\"controladora?id=" + ebCodigo + "&redirecionador=EmprestimoControl&comando=Selecionar\">Selecionar</a>";
        } else {
            return "-----------";
        }
    }

    public String getLinkStatus() {
        StatusBean fb = (StatusBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"status.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"status.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_status.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkTipo() {
        TipoBean ub = (TipoBean) getCurrentRowObject();
        int ubId = ub.getId();
        return "<a href=\"tipo.jsp?id=" + ubId + "&flag=exibir\">Exibir</a> | " + "<a href=\"tipo.jsp?id=" + ubId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_tipos.jsp?id=" + ubId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkAutor() {
        AutorBean fb = (AutorBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"autor.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"autor.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_autores.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkEditora() {
        EditoraBean fb = (EditoraBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"editora.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"editora.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_editoras.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkFuncionario() {
        FuncionarioBean fb = (FuncionarioBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"funcionario.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"funcionario.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_funcionarios.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkPerfil() {
        PerfilBean fb = (PerfilBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"perfil.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"perfil.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_perfis.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkFornecedor() {
        FornecedorBean fb = (FornecedorBean) getCurrentRowObject();
        int fbId = fb.getId();
        return "<a href=\"fornecedor.jsp?id=" + fbId + "&flag=exibir\">Exibir</a> | " + "<a href=\"fornecedor.jsp?id=" + fbId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_fornecedores.jsp?id=" + fbId + "&flag=excluir\">Excluir</a>";
    }

    public String getLinkUsuario() {
        UsuarioBean ub = (UsuarioBean) getCurrentRowObject();
        int ubId = ub.getId();
        return "<a href=\"usuario.jsp?id=" + ubId + "&flag=exibir\">Exibir</a> | " + "<a href=\"usuario.jsp?id=" + ubId + "&flag=alterar\">Alterar</a> | " + "<a href=\"lista_usuarios.jsp?id=" + ubId + "&flag=excluir\">Excluir</a>";
    }
}
