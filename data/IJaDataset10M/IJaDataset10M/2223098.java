package com.odontosis.acesso.filtro;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.odontosis.entidade.GrupoUsuario;
import com.odontosis.entidade.Usuario;

/**
 * Classe responsvel pela autenticao e controle de acesso
 * 
 * @author pablo
 * 
 */
public class PermissaoAcesso implements javax.servlet.Filter {

    public void destroy() {
        System.out.println("destroy");
    }

    public static Usuario getUsuarioLogado(HttpServletRequest request) throws IOException, ServletException {
        return (Usuario) request.getSession().getAttribute("usuario");
    }

    public static GrupoUsuario getGrupoUsuarioLogado(HttpServletRequest request) throws IOException, ServletException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        return usuario.getGrupo();
    }

    public static boolean verificarAcesso(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, GrupoUsuario... gruposComAcesso) throws Exception {
        boolean temAcesso = false;
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        for (GrupoUsuario grupo : gruposComAcesso) {
            if (usuario.getGrupo() == grupo) {
                temAcesso = true;
            }
        }
        if (!temAcesso) {
            throw new AcessoNegadoException();
        }
        return temAcesso;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = null;
        HttpServletResponse resp = null;
        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
        }
        if (response instanceof HttpServletResponse) {
            resp = (HttpServletResponse) response;
        }
        String path = req.getServletPath();
        if (path.indexOf(".do") >= 0) {
            if (req.getSession().getAttribute("usuario") == null) {
                RequestDispatcher rd = request.getRequestDispatcher("/login.do");
                rd.forward(request, resp);
                filterChain.doFilter(request, response);
            } else {
                Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
                if (path.indexOf("inclusaoUsuario") >= 0 && usuario.getGrupo() != GrupoUsuario.ADMIN) {
                    RequestDispatcher rd = request.getRequestDispatcher("/acessonegado.do");
                    rd.forward(request, resp);
                    filterChain.doFilter(request, response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("init");
    }
}
