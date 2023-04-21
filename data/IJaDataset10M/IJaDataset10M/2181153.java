package mac5855.app.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mac5855.app.dao.CategoriaDAO;
import mac5855.app.dao.DAOFactory;
import mac5855.app.dao.PerguntaDAO;
import mac5855.app.dao.UsuarioDAO;
import mac5855.app.models.Categoria;
import mac5855.app.models.GerenciadorDePerguntas;
import mac5855.app.models.Pergunta;
import mac5855.app.models.Usuario;

/**
 * Servlet implementation class Pergunta
 */
public class PerguntaController implements BusinessLogic {

    private static final long serialVersionUID = 1L;

    private String delimitador = "delimitador";

    private GerenciadorDePerguntas gerenciadorDePerguntas = GerenciadorDePerguntas.getInstancia();

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public PerguntaController() {
        super();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        String action = request.getParameter("action");
        System.out.println("action:" + action);
        if (action == null) {
            System.out.println("Executando a logica e redirecionando...");
            HttpSession session = ((HttpServletRequest) request).getSession();
            String userEmail = (String) session.getAttribute("User");
            System.out.println("Email user:" + userEmail);
            UsuarioDAO usuarioDAO = Controller.daoFactory.getUsuarioDAO();
            CategoriaDAO categoriaDAO = Controller.daoFactory.getCategoriaDAO();
            Usuario usuario = usuarioDAO.findByEmail(userEmail);
            System.out.println(usuario);
            Pergunta pergunta = new Pergunta();
            Categoria categoria = new Categoria();
            categoria.setId(1);
            if (request.getParameter("categoria_id") != null && request.getParameter("categoria_id").length() > 0) {
                categoria.setId(Integer.parseInt(request.getParameter("categoria_id")));
                categoria = categoriaDAO.find(categoria);
            }
            pergunta.setTitulo(request.getParameter("titulo"));
            pergunta.setData(new Date());
            pergunta.setUsuario(usuario);
            pergunta.setCategoria(categoria);
            PerguntaDAO dao = Controller.daoFactory.getPerguntaDAO();
            dao.create(pergunta);
            RequestDispatcher rd = request.getRequestDispatcher("/restricted/home.jsp");
            rd.forward(request, response);
            System.out.println("Alterando contato ..." + pergunta.getTitulo());
        } else if (action.equals("show")) {
            String id = request.getParameter("id");
            RequestDispatcher rd = request.getRequestDispatcher("/show_pergunta.jsp?id=" + id);
            rd.forward(request, response);
        } else if (action.equals("showAdmin")) {
            String id = request.getParameter("id");
            RequestDispatcher rd = request.getRequestDispatcher("/admin/show_pergunta.jsp?id=" + id);
            rd.forward(request, response);
        } else if (action.equals("search")) {
            String search = request.getParameter("search");
            RequestDispatcher rd = request.getRequestDispatcher("/busca.jsp?tipo=titulo&titulo=" + search);
            rd.forward(request, response);
        } else if (action.equals("show_category")) {
            int id = Integer.parseInt(request.getParameter("id"));
            RequestDispatcher rd = request.getRequestDispatcher("/busca.jsp?tipo=categoria&categoria_id=" + id);
            rd.forward(request, response);
        } else if (action.equals("vote_through")) {
            int id = Integer.parseInt(request.getParameter("id"));
            PerguntaDAO perguntaDAO = Controller.daoFactory.getPerguntaDAO();
            perguntaDAO.vote(id, true);
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else if (action.equals("vote_down")) {
            int id = Integer.parseInt(request.getParameter("id"));
            PerguntaDAO perguntaDAO = Controller.daoFactory.getPerguntaDAO();
            perguntaDAO.vote(id, false);
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else if (action.equals("add_favorite")) {
            int id = Integer.parseInt(request.getParameter("id"));
            HttpSession session = ((HttpServletRequest) request).getSession();
            String userEmail = (String) session.getAttribute("User");
            PerguntaDAO perguntaDAO = Controller.daoFactory.getPerguntaDAO();
            perguntaDAO.addFavorite(id, userEmail);
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else if (action.equals("del_Pergunta")) {
            int id = Integer.parseInt(request.getParameter("id"));
            PerguntaDAO perguntaDAO = Controller.daoFactory.getPerguntaDAO();
            perguntaDAO.delete(id);
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else if (action.equals("perguntasrecentes")) {
            iniciaMultipartResponse(response);
            Pergunta ultimaPergunta = null;
            PerguntaDAO dao = Controller.daoFactory.getPerguntaDAO();
            Pergunta[] perguntas = dao.findAll();
            for (Pergunta pergunta : perguntas) {
                enviarPergunta(pergunta, response);
                ultimaPergunta = pergunta;
            }
            while (true) {
                List<Pergunta> newPerguntas = gerenciadorDePerguntas.pegaEEsperaPorPerguntasMaisNovas(ultimaPergunta);
                for (Pergunta pergunta : newPerguntas) {
                    enviarPergunta(pergunta, response);
                    ultimaPergunta = pergunta;
                }
            }
        }
    }

    private void iniciaMultipartResponse(HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-type", "multipart/x-mixed-replace;boundary=\"" + delimitador + "\"");
        resp.getWriter().write("--" + delimitador + "\n");
    }

    private void enviarPergunta2(Pergunta pergunta, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("Content-type: text/xml\n\n");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<pergunta>\n");
        writer.write(pergunta.toXML() + "\n");
        writer.write("</pergunta>\n");
        resp.flushBuffer();
        writer.println("--" + delimitador);
        resp.flushBuffer();
    }

    private void enviarPergunta(Pergunta pergunta, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("Content-type: text/x-json\n\n");
        writer.write("{");
        writer.write("\"pergunta\":{\n");
        writer.write(pergunta.toJson() + "\n");
        writer.write("}}\n");
        resp.flushBuffer();
        writer.println("--" + delimitador);
        resp.flushBuffer();
    }
}
