package ensino2.filtro;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DisciplinaFiltro implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2) throws IOException, ServletException {
        RequestDispatcher red = request.getRequestDispatcher("controle");
        request.setAttribute("classAction", "DisciplinaControl");
        red.forward(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void destroy() {
    }
}
