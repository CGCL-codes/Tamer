package biblioteca.etiquetas;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import biblioteca.dao.exceptions.BibliotecaDAOException;
import biblioteca.dao.impl.BibliotecaDAOImp;
import biblioteca.modelo.*;

public class ListadoTag extends TagSupport {

    private char tipo;

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        boolean esAdmin = false;
        JspWriter print = pageContext.getOut();
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            BibliotecaDAOImp dao = (BibliotecaDAOImp) request.getSession().getServletContext().getAttribute("dao");
            Usuario admin = (Usuario) request.getSession().getAttribute(AtributosConstantes.administrador.toString());
            if (admin != null) {
                esAdmin = true;
            }
            switch(tipo) {
                case 'a':
                    List<Autor> listaAutores = dao.obtenerAutores();
                    for (Autor autor : listaAutores) {
                        print.println(autor);
                        print.println("<hr align='left' width='30%'><br />");
                    }
                    break;
                case 'g':
                    List<Genero> listaGeneros = dao.obtenerGeneros();
                    for (Genero genero : listaGeneros) {
                        print.println(genero);
                        print.println("<hr width='30%'><br />");
                    }
                    break;
                case 'l':
                    List<Libro> listaLibros = dao.obtenerLibros();
                    for (Libro libro : listaLibros) {
                        print.println(libro);
                        if (esAdmin == true) {
                            print.println("<br/><a href=?accion=nuevo'>Nuevo</a>" + " - <a href='?ref=" + libro.getReferencia() + "&accion=modificar'>Modificar</a> - " + "<a href='?ref=" + libro.getReferencia() + "&accion=eliminar'>Eliminar</a></br>");
                        } else {
                            print.println("<br /><a href='#'>Solicitar prestamo</a><br />");
                        }
                        print.println("<hr align='left' width='30%'><br />");
                    }
                    break;
                case 'p':
                    List<Prestamo> listaPrestamos = dao.obtenerPrestamo();
                    for (Prestamo prestamo : listaPrestamos) {
                        print.println(prestamo);
                        print.println("<hr width='30%'><br />");
                    }
                    break;
                case 'u':
                    List<Usuario> listaUsuarios = dao.obtenerUsuarios();
                    for (Usuario usuario : listaUsuarios) {
                        print.println(usuario);
                        print.println("<hr width='30%'><br />");
                    }
                    break;
                default:
                    print.println("<h4>No se ha seleccionado un tipo v&aacute;lido</h4>");
                    break;
            }
        } catch (BibliotecaDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public void enlacesAdmin() throws IOException {
        JspWriter print = pageContext.getOut();
    }
}
