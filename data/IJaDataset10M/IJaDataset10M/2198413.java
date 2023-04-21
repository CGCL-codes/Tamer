package neoAtlantis.utilidades.ctrlAcceso.utils;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Validador de session Web que valida la existencia del usuario en sesi&oacute;n.
 * <br><br>
 * Para realizar la validac&oacute;n revisa que en sesi&oacute;n existe una atributo nombrado 'usuario'.
 * En caso de no existe redirecionar&aacute; hacia donde se tenga configurado.
 * <br><br>
 * Para poder utilizar este validador, se debe de configurar el <i>Filter</i> en el '<i>web.xml</i>' de la aplicaci&oacute;n web.
 * Las lineas a agregar al archivo serian las siguientes:
 * <pre>
 * <b>&lt;filter&gt;</b>
 *     <b>&lt;filter-name>Filtro de sesion<b>&lt;/filter-name&gt;</b>
 *     <b>&lt;filter-class>neoAtlantis.utilidades.ctrlAcceso.utils.ValidadorSesionSimple<b>&lt;/filter-class&gt;</b>
 *     <b>&lt;init-param&gt;</b>
 *         <b>&lt;param-name&gt;</b>excepciones<b>&lt;/param-name&gt;</b>
 *         <b>&lt;param-value&gt;</b><i>paginas_que_no_ser&aacute;n_validadas(estas deben de estar separadas por coma)</i><b>&lt;/param-value&gt;</b>
 *     <b>&lt;/init-param&gt;</b>
 *     <b>&lt;init-param&gt;</b>
 *         <b>&lt;param-name&gt;</b>redireccion<b>&lt;/param-name&gt;</b>
 *         <b>&lt;param-value&gt;</b><i>pagina_a_la_que_se_redereccionara(default index.html)</i><b>&lt;/param-value&gt;</b>
 *     <b>&lt;/init-param&gt;</b>
 *     <b>&lt;init-param&gt;</b>
 *         <b>&lt;param-name&gt;</b>errorNombre<b>&lt;/param-name&gt;</b>
 *         <b>&lt;param-value&gt;</b><i>nombre_del_atributo_de_request_donde_se_alojara_el_mensaje_de_error(default errorGeneral)</i><b>&lt;/param-value&gt;</b>
 *     <b>&lt;/init-param&gt;</b>
 *     <b>&lt;init-param&gt;</b>
 *         <b>&lt;param-name&gt;</b>errorTexto<b>&lt;/param-name&gt;</b>
 *         <b>&lt;param-value&gt;</b><i>mensaje_de_error</i><b>&lt;/param-value&gt;</b>
 *     <b>&lt;/init-param&gt;</b>
 *     <b>&lt;init-param&gt;</b>
 *         <b>&lt;param-name&gt;</b>debug<b>&lt;/param-name&gt;</b>
 *         <b>&lt;param-value&gt;</b><i>true_si_se_desea_activar_el_debug(default false)</i><b>&lt;/param-value&gt;</b>
 *     <b>&lt;/init-param&gt;</b>
 * <b>&lt;/filter&gt;</b>
 * <b>&lt;filter-mapping&gt;</b>
 *     <b>&lt;filter-name&gt;</b>Filtro de sesion<b>&lt;/filter-name&gt;</b>
 *     <b>&lt;url-pattern&gt;</b><i>patron_de_los_recursos_a_validar(ej: *.jsp)</i><b>&lt;/url-pattern&gt;</b>
 * <b>&lt;/filter-mapping&gt;</b>
 * </pre>
 * @version 1.0
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ValidadorSesionSimple implements Filter {

    protected FilterConfig filterConfig;

    protected String[] pags = null;

    protected String redir;

    protected String errorNombre;

    protected String errorTexto;

    protected boolean debug = false;

    protected String nombreVal = "ValidadorSesionSimple";

    /**
     * Inicializa el Validador de session
     * @param filterConfig Configuraci&oacute;n del <i>Filter</i>
     * @throws javax.servlet.ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        try {
            this.cargaEntorno();
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Intercepta la llamada a un recurso Web.
     * @param request Request de la petici&oacute;n
     * @param response Response de la petici&oacute;n
     * @param chain Chain del <i>Filter</i>
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean conectado = this.validaConexion((HttpServletRequest) request);
        boolean valida = this.validaRecurso(((HttpServletRequest) request).getServletPath());
        if (valida && !conectado) {
            this.sessionCaducada(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destruye el Validador de session
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Obtiene las paginas que seran sometidas a validaci&oacute;n.
     * @return Arreglo con las paginas.
     */
    protected String[] obtieneExcepciones() {
        try {
            return this.filterConfig.getInitParameter("excepciones").split(",");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Obtiene si esta activo el debug.
     * @return true si esta activo
     */
    protected boolean obtieneDebug() {
        try {
            return (new Boolean(this.filterConfig.getInitParameter("debug"))).booleanValue();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Obtiene la pagina de redireccionamiento.
     * @return Pagina de redirecionamiento
     */
    protected String obtieneRedirecionamiento() {
        try {
            return (this.filterConfig.getInitParameter("redireccion") != null ? this.filterConfig.getInitParameter("redireccion") : "index.html").trim();
        } catch (Exception ex) {
            return "index.html";
        }
    }

    /**
     * Obtiene el nombre de la variable en request donde se alojar&aacute; el mensaje de error.
     * @return Nombre de la variable
     */
    protected String obtieneErrorNombre() {
        try {
            return (this.filterConfig.getInitParameter("errorNombre") != null ? this.filterConfig.getInitParameter("errorNombre") : "errorGeneral").trim();
        } catch (Exception ex) {
            return "errorGeneral";
        }
    }

    /**
     * Obtiene el mensaje de error que se mostrar&aacute; al momento de redirecionar.
     * @return Mensaje de error
     */
    protected String obtieneErrorTexto() {
        try {
            return (this.filterConfig.getInitParameter("errorTexto") != null ? this.filterConfig.getInitParameter("errorTexto") : "Su session ha expirado.").trim();
        } catch (Exception ex) {
            return "Su session ha expirado.";
        }
    }

    /**
     * Prepara todo el entorno para que trabaje el validador.
     */
    protected void cargaEntorno() throws Exception {
        this.pags = this.obtieneExcepciones();
        this.debug = this.obtieneDebug();
        this.redir = this.obtieneRedirecionamiento();
        this.errorNombre = this.obtieneErrorNombre();
        this.errorTexto = this.obtieneErrorTexto();
        if (debug) {
            System.out.print("INFO: " + this.nombreVal + ": excepciones -> [");
            for (int i = 0; this.pags != null && i < this.pags.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(this.pags[i]);
            }
            System.out.println("]");
            System.out.println("INFO: " + this.nombreVal + ": redireccion -> " + redir);
            System.out.println("INFO: " + this.nombreVal + ": errorNombre -> " + errorNombre);
            System.out.println("INFO: " + this.nombreVal + ": errorTexto -> " + errorTexto);
            System.out.println("INFO: " + this.nombreVal + ": Inicia la validación de sesion.");
        }
    }

    /**
     * Revisa si existe un usuario activo.
     * @param request Resquest de la peticion web
     * @return true si existe el usuario activo
     */
    protected boolean validaConexion(HttpServletRequest request) {
        try {
            return (request.getSession().getAttribute("usuario") != null);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Revisa si el recurso se debe de validar.
     * @param request Resquest de la peticion web
     * @return true si se debe de validar
     */
    protected boolean validaRecurso(String recurso) {
        if (this.debug) {
            System.out.println("INFO: " + this.nombreVal + ": Recurso a validar: " + recurso);
        }
        if (recurso.equals(this.redir)) {
            if (this.debug) {
                System.out.println("INFO: " + this.nombreVal + ": Omite el recurso por ser el redirecionamiento.");
            }
            return false;
        }
        for (int i = 0; pags != null && i < this.pags.length; i++) {
            if (recurso.equals(this.pags[i].trim())) {
                if (this.debug) {
                    System.out.println("INFO: " + this.nombreVal + ": Omite el recurso por ser una excepcion.");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Redireciona el proceso en caso de no ser valida la sesion.
     * @param request Resquest de la peticion web
     * @param response Response de la peticion web
     * @throws java.lang.Exception
     */
    protected void sessionCaducada(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher;
        request.setAttribute(this.errorNombre, this.errorTexto);
        if (this.debug) {
            System.out.println("INFO: " + this.nombreVal + ": Session caducada, redirecionando ...");
            System.out.println("INFO: " + this.nombreVal + ": " + this.errorNombre + "=" + request.getAttribute(this.errorNombre));
        }
        dispatcher = this.filterConfig.getServletContext().getRequestDispatcher(this.redir);
        dispatcher.forward(request, response);
    }
}
