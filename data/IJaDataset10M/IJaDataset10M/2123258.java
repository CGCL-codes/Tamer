package com.ttporg.pe.servlet;

import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.ttporg.pe.bean.BaseBean;
import com.ttporg.pe.bean.Cliente;
import com.ttporg.pe.dao.AgenciaDao;
import com.ttporg.pe.dao.AsientoDao;
import com.ttporg.pe.dao.CalendarioDao;
import com.ttporg.pe.dao.ClienteDao;
import com.ttporg.pe.dao.ClientePagoDao;
import com.ttporg.pe.dao.DepartamentoDao;
import com.ttporg.pe.dao.EmpresaDao;
import com.ttporg.pe.dao.PagoDao;
import com.ttporg.pe.dao.SalidaDao;
import com.ttporg.pe.dao.ServicioDao;
import com.ttporg.pe.dao.TransaccionDao;
import com.ttporg.pe.dao.VehiculoDao;
import com.ttporg.pe.dao.impl.AgenciaDaoImpl;
import com.ttporg.pe.dao.impl.AsientoDaoImpl;
import com.ttporg.pe.dao.impl.CalendarioDaoImpl;
import com.ttporg.pe.dao.impl.ClienteDaoImpl;
import com.ttporg.pe.dao.impl.ClientePagoDaoImpl;
import com.ttporg.pe.dao.impl.DepartamentoDaoImpl;
import com.ttporg.pe.dao.impl.EmpresaDaoImpl;
import com.ttporg.pe.dao.impl.PagoDaoImpl;
import com.ttporg.pe.dao.impl.SalidaDaoImpl;
import com.ttporg.pe.dao.impl.ServicioDaoImpl;
import com.ttporg.pe.dao.impl.TransaccionDaoImpl;
import com.ttporg.pe.dao.impl.VehiculoDaoImpl;
import com.ttporg.pe.dto.BeanParametrosEmailDto;
import com.ttporg.pe.dto.BeanValidacionDto;
import com.ttporg.pe.util.UtilCalendario;
import com.ttporg.pe.util.UtilEmail;
import com.ttporg.pe.util.UtilEncriptacion;

/**
 * @author Cesar Ricardo.
 * @clase: ServletRegistroCliente.java  
 * @descripci�n descripci�n de la clase.
 * @author_web: http://frameworksjava2008.blogspot.com
                http://viviendoconjavaynomoririntentandolo.blogspot.com
 * @author_email: nombre del email del autor.
 * @author_company: nombre de la compa��a del autor.
 * @fecha_de_creaci�n: dd-mm-yyyy.
 * @fecha_de_ultima_actualizaci�n: dd-mm-yyyy.
 * @versi�n 1.0
 **/
public class ServletRegistroCliente extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 509943115648134836L;

    private ServletContext contexto = null;

    private RequestDispatcher despachador = null;

    private ClienteDao clienteDAO = null;

    private EmpresaDao empresaDAO = null;

    private DepartamentoDao departamentoDAO = null;

    private AgenciaDao agenciaDAO = null;

    private VehiculoDao vehiculoDAO = null;

    private ServicioDao servicioDAO = null;

    private AsientoDao asientoDAO = null;

    private SalidaDao salidaDAO = null;

    private CalendarioDao calendarioDAO = null;

    private PagoDao pagoDAO = null;

    private ClientePagoDao clientePagoDAO = null;

    private TransaccionDao transaccionDAO = null;

    private UtilCalendario utilCalendario = null;

    private UtilEmail utilEmail = null;

    private UtilEncriptacion utilEncriptacion = null;

    private BaseBean beanBase = null;

    private static String REDIRECCIONAMIENTO = "/jsp/RegistroCliente.jsp";

    {
        this.clienteDAO = new ClienteDaoImpl();
        this.empresaDAO = new EmpresaDaoImpl();
        this.departamentoDAO = new DepartamentoDaoImpl();
        this.agenciaDAO = new AgenciaDaoImpl();
        this.vehiculoDAO = new VehiculoDaoImpl();
        this.servicioDAO = new ServicioDaoImpl();
        this.asientoDAO = new AsientoDaoImpl();
        this.salidaDAO = new SalidaDaoImpl();
        this.calendarioDAO = new CalendarioDaoImpl();
        this.pagoDAO = new PagoDaoImpl();
        this.clientePagoDAO = new ClientePagoDaoImpl();
        this.transaccionDAO = new TransaccionDaoImpl();
        this.utilCalendario = new UtilCalendario();
        this.utilEmail = new UtilEmail();
        this.utilEncriptacion = new UtilEncriptacion();
        this.beanBase = new BaseBean();
    }

    /**
	 * init
	 * @param configuracion
	 **/
    public void init(ServletConfig configuracion) {
        this.imprimeLog("********* DENTRO DE 'init( ServletConfig config )' **********");
        try {
            super.init(configuracion);
            String servletName = (String) configuracion.getServletName();
            this.imprimeLog("ServletName: " + servletName);
            this.acceso_InitParam();
            this.acceso_ContextParam();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
	 * acceso_InitParam
	 **/
    public void acceso_InitParam() {
        this.imprimeLog("********** DENTRO DE 'acceso_InitParam' **********");
        ServletConfig servletConfig = this.getServletConfig();
        this.imprimeLog("=====> ServletConfig: " + servletConfig);
    }

    /**
	 * acceso_ContextParam
	 **/
    public void acceso_ContextParam() {
        this.imprimeLog("********** DENTRO DE 'acceso_ContextParam' **********");
        this.inicializaDAOs();
    }

    /**
	 * service
	 * @param request
	 * @param response
	 **/
    public void service(HttpServletRequest request, HttpServletResponse response) {
        this.imprimeLog("********* DENTRO DE service **********");
        BeanValidacionDto objValidacion = new BeanValidacionDto();
        boolean estadoValidacion = true;
        String MENSAJE_VALIDACION = " * Ingresar un valor en: ";
        try {
            String nombres = request.getParameter("txtNombres");
            String apellidos = request.getParameter("txtApellidos");
            String fechaNacimiento = request.getParameter("txtFechaNacimiento");
            String direccion = request.getParameter("txtDireccion");
            String email = request.getParameter("txtEmail");
            String usuario = request.getParameter("txtUsuario");
            String password = request.getParameter("txtPassword");
            String confirPassword = request.getParameter("txtConfirmPassword");
            String dni = request.getParameter("txtDni");
            this.imprimeLog("");
            this.imprimeLog("DATOS INGRESADOS DEL CLIENTE: ");
            this.imprimeLog("------------------------------");
            this.imprimeLog("Nombres:         " + nombres);
            this.imprimeLog("Apellidos:       " + apellidos);
            this.imprimeLog("FechaNacimiento: " + fechaNacimiento);
            this.imprimeLog("Direccion:       " + direccion);
            this.imprimeLog("Email:           " + email);
            this.imprimeLog("Dni:             " + dni);
            this.imprimeLog("Usuario:         " + usuario);
            this.imprimeLog("Password:        " + password);
            this.imprimeLog("Conf.Password:   " + confirPassword);
            this.imprimeLog("");
            if (nombres.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Nombres]");
                estadoValidacion = false;
            }
            if (apellidos.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Apellidos]");
                estadoValidacion = false;
            }
            if (fechaNacimiento.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Fecha Nacimiento]");
                estadoValidacion = false;
            }
            if (direccion.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Direccion]");
                estadoValidacion = false;
            }
            if (dni.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Dni]");
                estadoValidacion = false;
            }
            if (email.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Email]");
                estadoValidacion = false;
            }
            if (usuario.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Usuario]");
                estadoValidacion = false;
            }
            if (password.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Password]");
                estadoValidacion = false;
            }
            if (confirPassword.equals("")) {
                objValidacion.getMensajesNOK().add(MENSAJE_VALIDACION + "[Confirmacion de Password]");
                estadoValidacion = false;
            }
            if (!(password.equals(confirPassword))) {
                objValidacion.getMensajesNOK().add(" * Confirmacion de Password Incorrecta.");
                estadoValidacion = false;
            }
            if (estadoValidacion == true) {
                objValidacion.setMensajeOK("Proceso Exitoso");
                String[] arrayCadena = fechaNacimiento.split("-");
                String anio = arrayCadena[0];
                String mes = arrayCadena[1];
                String dia = arrayCadena[2];
                this.imprimeLog("Anio: " + anio);
                this.imprimeLog("Mes:  " + mes);
                this.imprimeLog("Dia:  " + dia);
                Date cumpleanos = this.utilCalendario.getFecha(Integer.parseInt(anio), Integer.parseInt(mes), Integer.parseInt(dia));
                String cadenaEncriptada = this.utilEncriptacion.encriptarCIPHER(password);
                Cliente objCliente = new Cliente(nombres, apellidos, cumpleanos, direccion, null, email, dni, usuario, cadenaEncriptada, "USUARIO", "true");
                boolean estado = this.clienteDAO.ingresarCliente(objCliente);
                this.imprimeLog("Estado INSERT: " + estado);
                String saltoLinea = "<br>";
                BeanParametrosEmailDto beanEmailDto = new BeanParametrosEmailDto();
                beanEmailDto.setUsuario("cesarricardo.guerra19@gmail.com");
                beanEmailDto.setPassword("41816133");
                beanEmailDto.setCuentaEmailRemitente("cesarricardo.guerra19@gmail.com");
                beanEmailDto.setCuentaEmailDestinatario(email);
                beanEmailDto.setCuentaEmailDestinatario_Cc("u201000125@upc.edu.pe");
                beanEmailDto.setCuentaEmailDestinatario_Bcc("i220051@cibertec.edu.pe");
                beanEmailDto.setServidor("smtp.gmail.com");
                beanEmailDto.setPuerto("587");
                beanEmailDto.setProtocolo("smtp");
                beanEmailDto.setContentType("text/html");
                beanEmailDto.setNombreAdjunto("PublicidaEmail.jpg");
                beanEmailDto.setAsuntoMensaje("peruvianportalterrestrialtransport [Publicidad]");
                beanEmailDto.setCuerpoMensaje(" Bienvenido Sr(a): <font color='red'> " + nombres + " " + apellidos + "</font>, su registro en el portal" + saltoLinea + " se ha realizado de forma exitosa. " + saltoLinea + saltoLinea + " Su usuario es: <font color='red'>" + usuario + "</font>" + saltoLinea + " Su password es: <font color='red'>" + password + "</font>" + saltoLinea + saltoLinea + " �Quieres Viajar? " + saltoLinea + " �No sabes donde? " + saltoLinea + " �No sabes como?  " + saltoLinea + " Ven con nosotros y te brindaremos toda la informaci�n que quieres y m�s...!!! " + saltoLinea);
                this.utilEmail.enviarEmailHtmlAdjunto(beanEmailDto);
            }
            request.setAttribute("estadoValidacion", estadoValidacion);
            request.setAttribute("objValidacion", objValidacion);
            this.contexto = this.getServletContext();
            this.despachador = this.contexto.getRequestDispatcher(this.REDIRECCIONAMIENTO);
            this.despachador.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * inicializaDAOs
	 **/
    public void inicializaDAOs() {
        ServletContext servletContext = this.getServletContext();
        this.imprimeLog("=====> servletContext: " + servletContext);
        WebApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.imprimeLog("=====> contexto: " + contexto);
        this.imprimeLog("****************** OBTENIENDO 'DAOS' [INICIO] ******************");
        this.clienteDAO = (ClienteDaoImpl) contexto.getBean("clienteDao");
        this.empresaDAO = (EmpresaDaoImpl) contexto.getBean("empresaDao");
        this.departamentoDAO = (DepartamentoDaoImpl) contexto.getBean("departamentoDao");
        this.agenciaDAO = (AgenciaDaoImpl) contexto.getBean("agenciaDao");
        this.vehiculoDAO = (VehiculoDaoImpl) contexto.getBean("vehiculoDao");
        this.servicioDAO = (ServicioDaoImpl) contexto.getBean("servicioDao");
        this.asientoDAO = (AsientoDaoImpl) contexto.getBean("asientoDao");
        this.salidaDAO = (SalidaDaoImpl) contexto.getBean("salidaDao");
        this.calendarioDAO = (CalendarioDaoImpl) contexto.getBean("calendarioDao");
        this.pagoDAO = (PagoDaoImpl) contexto.getBean("pagoDao");
        this.clientePagoDAO = (ClientePagoDaoImpl) contexto.getBean("clientePagoDao");
        this.transaccionDAO = (TransaccionDaoImpl) contexto.getBean("transaccionDao");
        this.imprimeLog("====> [clienteDAO]:      " + this.clienteDAO);
        this.imprimeLog("====> [empresaDAO]:      " + this.empresaDAO);
        this.imprimeLog("====> [departamentoDAO]: " + this.departamentoDAO);
        this.imprimeLog("====> [agenciaDAO]:      " + this.agenciaDAO);
        this.imprimeLog("====> [vehiculoDAO]:     " + this.vehiculoDAO);
        this.imprimeLog("====> [servicioDAO]:     " + this.servicioDAO);
        this.imprimeLog("====> [asientoDAO]:      " + this.asientoDAO);
        this.imprimeLog("====> [salidaDAO]:       " + this.salidaDAO);
        this.imprimeLog("====> [calendarioDAO]:   " + this.calendarioDAO);
        this.imprimeLog("====> [pagoDAO]:         " + this.pagoDAO);
        this.imprimeLog("====> [clientePagoDAO]:  " + this.clientePagoDAO);
        this.imprimeLog("====> [transaccionDAO]:  " + this.transaccionDAO);
        this.imprimeLog("******************* OBTENIENDO 'DAOS' [FIN] *******************");
    }

    /**
	 * imprimeLog
	 * @param mensaje
	 **/
    public void imprimeLog(String mensaje) {
        this.beanBase.imprimeLog(mensaje, this.getClass().toString());
    }
}
