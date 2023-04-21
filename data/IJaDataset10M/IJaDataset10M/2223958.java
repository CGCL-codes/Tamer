package com.habitton.filter;

import java.io.IOException;
import java.util.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.habitton.action.BaseAction;
import com.habitton.filter.Registro;
import com.habitton.utils.Constantes;
import com.habitton.filter.Estadisticas;

public class HabittonFilter extends BaseAction implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        Registro registro = new Registro();
        long now = System.currentTimeMillis();
        registro.setUrl(hreq.getRequestURL().toString());
        if (hreq.getSession().getAttribute(Constantes.SESSION_ID) != null) {
            registro.setUserId((String) hreq.getSession().getAttribute(Constantes.SESSION_ID));
            System.out.println("- 1. nombre usuario: " + (String) hreq.getSession().getAttribute(Constantes.SESSION_ID));
        }
        registro.setSessionId((String) hreq.getSession().getId());
        System.out.println("- 1. nombre Session: " + (String) hreq.getSession().getId());
        String idioma = hreq.getLocale().toString();
        System.out.println("- 2. idioma: " + idioma);
        Enumeration eParametros = hreq.getParameterNames();
        List<Parametro> listaParametros = null;
        if (eParametros != null) {
            String nombreParametro = null;
            listaParametros = new ArrayList<Parametro>();
            Parametro parametro = null;
            while (eParametros.hasMoreElements()) {
                nombreParametro = (String) eParametros.nextElement();
                parametro = new Parametro();
                parametro.setNombreParametro(nombreParametro);
                parametro.setValorParametro((String) req.getParameter(nombreParametro));
                System.out.println("- 3. parametro: " + nombreParametro);
                System.out.println("- 3. valor: " + req.getParameter(nombreParametro));
                listaParametros.add(parametro);
            }
            registro.setParametros(listaParametros);
        }
        Enumeration eAtributos = req.getAttributeNames();
        List<Atributo> listaAtributos = null;
        if (eAtributos != null) {
            String nombreAtributo = null;
            listaAtributos = new ArrayList<Atributo>();
            Atributo atributo = null;
            while (eAtributos.hasMoreElements()) {
                nombreAtributo = (String) eAtributos.nextElement();
                atributo = new Atributo();
                atributo.setNombreAtributo(nombreAtributo);
                atributo.setValorAtributo((String) req.getAttribute(nombreAtributo));
                System.out.println("- 4. atributo: " + nombreAtributo);
                System.out.println("- 4. valor del atributo: " + (String) req.getAttribute(nombreAtributo));
                listaAtributos.add(atributo);
            }
            registro.setAtributos(listaAtributos);
        }
        registro.setUrl(hreq.getRequestURL().toString());
        System.out.println("5. Request url: " + hreq.getRequestURL());
        System.out.println("date: " + new Date());
        registro.setDate(new Date());
        chain.doFilter(req, res);
        registro.setDuration(System.currentTimeMillis() - now);
        addEntry(registro);
    }

    private void addEntry(Registro registro) {
        System.out.println("ENTRADAS SIZE: " + Estadisticas.getListadoFiltro().size());
        Estadisticas.getListadoFiltro().add(registro);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
