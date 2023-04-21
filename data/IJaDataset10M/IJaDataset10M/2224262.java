package modelo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.logica.Aluno;
import modelo.logica.Endereco;
import modelo.logica.Estado;
import modelo.logica.Funcionario;
import modelo.logica.Grupo;
import modelo.logica.Responsavel;

/**
 * Servlet implementation class cadastroServlet
 */
public class cadastroFuncServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public cadastroFuncServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelo.bd.Acess.inicializaBD();
        String nome = request.getParameter("nome");
        int end = Integer.parseInt(request.getParameter("endereco"));
        String telefone = request.getParameter("telefone");
        String dataNasc = request.getParameter("dataNasc");
        char sexo = request.getParameter("sexo").toCharArray()[0];
        String nacionalidade = request.getParameter("nacionalidade");
        String naturalidade = request.getParameter("naturalidade");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        int nivel = Integer.parseInt(request.getParameter("nivel"));
        String obs = request.getParameter("obs");
        String funcao = request.getParameter("funcao");
        String matrSiape = request.getParameter("matrsiape");
        String estadoCivil = request.getParameter("estadocivil");
        String pis = request.getParameter("pis");
        String identidade = request.getParameter("identidade");
        String orgaoExp = request.getParameter("orgao");
        String cpf = request.getParameter("cpf");
        String admissao = request.getParameter("admissoa");
        String habilitacao = request.getParameter("habilitacao");
        modelo.bd.Acess.inicializaBD();
        Funcionario func = new Funcionario();
        func.setEndereco(modelo.bd.Acess.getEndereco(end));
        Endereco endereco = modelo.bd.Acess.getEndereco(end);
        func.setNome(nome);
        func.setTelefone(telefone);
        func.setDataNasc(dataNasc);
        func.setSexo(sexo);
        func.setNacionalidade(nacionalidade);
        func.setNaturalidade(naturalidade);
        func.setLogin(login);
        func.setSenha(senha);
        func.setNivel(nivel);
        func.setObs(obs);
        func.setFuncao(funcao);
        func.setMatrSiape(matrSiape);
        func.setEstadoCivil(estadoCivil);
        func.setPis(pis);
        func.setIdentidade(identidade);
        func.setOrgaoExp(orgaoExp);
        func.setAdmissao(admissao);
        func.setHabilitacao(habilitacao);
        int sucesso = modelo.bd.Acess.addFuncionario(func);
        if (sucesso != -1) response.sendRedirect("cadfuncionario.jsp");
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
