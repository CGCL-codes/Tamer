package com.meleva.bll;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.meleva.dao.UsuarioDao;
import com.meleva.vo.Usuario;

@WebServlet("/EditarUsuario")
public class EditarUsuario extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = new Usuario();
        UsuarioServices usuarioServices = new UsuarioServices();
        usuario = usuarioServices.instanciaUsuario(request);
        try {
            HttpSession session = request.getSession(true);
            usuario.setCodUsuario((Integer) (session.getAttribute("codUsuario")));
            usuarioDao.editarUsuario(usuario);
            request.setAttribute("mensagem", "O seu cadastro foi atualizado com sucesso");
            getServletContext().getRequestDispatcher("/FormMeusDados.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", e.toString());
            getServletContext().getRequestDispatcher("/FormMeusDados.jsp").forward(request, response);
        }
    }
}
