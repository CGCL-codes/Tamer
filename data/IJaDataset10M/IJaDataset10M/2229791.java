package es.caib.regtel.plugincaib.persistence.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.model.ExcepcionRegistroWeb;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.regtel.plugincaib.persistence.dao.parametros.ParametrosDAO;
import es.caib.regtel.plugincaib.persistence.dao.parametros.ParametrosDAOFactory;
import es.caib.regtel.plugincaib.persistence.dao.registro.RegistroDAO;
import es.caib.regtel.plugincaib.persistence.dao.registro.RegistroDAOFactory;
import es.caib.regtel.plugincaib.persistence.util.Configuracion;
import es.caib.util.StringUtil;

/**
 * SessionBean de registro telem�tico 
 *
 * @ejb.bean
 *  name="regtel/plugincaib/persistence/RegistroWebEJB"
 *  jndi-name="es.caib.regtel.plugincaib.persistence.RegistroWeb"
 *  type="Stateless"
 *  view-type="remote"
 *  
 *  @ejb.env-entry name="jndiDatasource" type="java.lang.String" value="java:/es.caib.regweb.db"
 *  
 */
public abstract class RegistroWebEJB implements SessionBean {

    private static Log log = LogFactory.getLog(RegistroWebEJB.class);

    private String daoImpl = null;

    private String daoImplParametros = null;

    private String jndiDatasource = null;

    private String defaultUser = null;

    private String defaultPassword = null;

    private static String ENTRADA_IN_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private static String ENTRADA_OUT_DATE_FORMAT = "yyyyMMddHHmmss";

    private boolean validaOfRegEnt = true;

    private boolean validaOfRegSal = true;

    /**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     * 
     */
    public void ejbCreate() throws CreateException {
        log.info("ejbCreate: " + this.getClass());
        try {
            javax.naming.InitialContext initialContext = new javax.naming.InitialContext();
            jndiDatasource = (String) initialContext.lookup("java:comp/env/jndiDatasource");
            log.debug("jndiDatasource : [" + jndiDatasource + "]");
            daoImplParametros = Configuracion.getProperty("parametrosDAOImpl");
            log.debug("daoImplParametros : [" + daoImplParametros + "]");
            daoImpl = Configuracion.getProperty("registroDAOImpl");
            log.debug("daoImpl : [" + daoImpl + "]");
            defaultUser = Configuracion.getProperty("defaultUser");
            defaultPassword = Configuracion.getProperty("defaultPassword");
            String strValidaOfRegEnt = Configuracion.getProperty("validaOfRegEnt");
            validaOfRegEnt = Boolean.valueOf(strValidaOfRegEnt).booleanValue();
            log.debug("validaOfRegEnt : [" + validaOfRegEnt + "]");
            String strValidaOfRegSal = Configuracion.getProperty("validaOfRegSal");
            validaOfRegSal = Boolean.valueOf(strValidaOfRegSal).booleanValue();
            log.debug("validaOfRegSal : [" + validaOfRegSal + "]");
        } catch (Exception exc) {
            log.error(exc);
            throw new CreateException(exc.getLocalizedMessage());
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public es.caib.sistra.plugins.regtel.ResultadoRegistro registroEntrada(es.caib.regtel.plugincaib.model.DatosRegistroEntrada entrada) throws ExcepcionRegistroWeb {
        log.debug("Registro. Entrada: [" + entrada + "]");
        if (StringUtils.isEmpty(entrada.getUsuario())) {
            entrada.setUsuario(defaultUser);
            entrada.setPassword(defaultPassword);
        }
        String strFecha = getFecha();
        String strHora = getHorasMinutos();
        entrada.setDataentrada(strFecha);
        entrada.setData(strFecha);
        entrada.setHora(strHora);
        ResultadoRegistro resultado = new ResultadoRegistro();
        try {
            RegistroDAO daoRegistro = RegistroDAOFactory.getInstance().getRegistroDAO(this.daoImpl);
            entrada = daoRegistro.grabar(entrada, validaOfRegEnt);
            resultado.setNumeroRegistro(entrada.getOficina() + "/" + entrada.getNumeroEntrada() + "/" + entrada.getAnoEntrada());
            resultado.setFechaRegistro(StringUtil.cadenaAFecha(entrada.getDataentrada() + " " + entrada.getHora(), ENTRADA_IN_DATE_FORMAT));
            log.debug("Registro. Resultado: [" + resultado + "]");
            return resultado;
        } catch (Exception exc) {
            log.error("Error intentando efectuar registro de entrada", exc);
            throw new ExcepcionRegistroWeb("Error intentando efectuar registro de entrada", exc);
        }
    }

    /**
	 * @ejb.interface-method
     * @ejb.permission role-name = "${role.gestor}"
     * @ejb.permission role-name = "${role.auto}"
	 * @param salida
	 * @return
	 */
    public es.caib.sistra.plugins.regtel.ResultadoRegistro registroSalida(DatosRegistroSalida salida) throws ExcepcionRegistroWeb {
        if (StringUtils.isEmpty(salida.getUsuario())) {
            salida.setUsuario(defaultUser);
            salida.setPassword(defaultPassword);
        }
        String strFecha = getFecha();
        String strHora = getHorasMinutos();
        salida.setDataSalida(strFecha);
        salida.setData(strFecha);
        salida.setHora(strHora);
        ResultadoRegistro resultado = new ResultadoRegistro();
        try {
            RegistroDAO daoRegistro = RegistroDAOFactory.getInstance().getRegistroDAO(this.daoImpl);
            salida = daoRegistro.grabar(salida, validaOfRegSal);
            resultado.setNumeroRegistro(salida.getOficina() + "/" + salida.getNumeroSalida() + "/" + salida.getAnoSalida());
            resultado.setFechaRegistro(StringUtil.cadenaAFecha(salida.getDataSalida() + " " + salida.getHora(), ENTRADA_IN_DATE_FORMAT));
            log.debug("Registro salida. Resultado: [" + resultado + "]");
            return resultado;
        } catch (Exception exc) {
            log.error("Error intentando efectuar registro de salida", exc);
            throw new ExcepcionRegistroWeb("Error intentando efectuar registro de salida", exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public void anularRegistroEntrada(String usuario, String oficina, String numeroEntrada, String anyoEntrada) throws ExcepcionRegistroWeb {
        try {
            if (StringUtils.isEmpty(usuario)) {
                usuario = defaultUser;
            }
            RegistroDAO daoRegistro = RegistroDAOFactory.getInstance().getRegistroDAO(this.daoImpl);
            daoRegistro.anularRegistroEntrada(usuario, oficina, numeroEntrada, anyoEntrada, validaOfRegEnt);
        } catch (Exception exc) {
            log.error("Error intentando anular registro de entrada", exc);
            throw new ExcepcionRegistroWeb("Error intentando anular registro de entrada", exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public List obtenerOficinas(String usuario, String autorizacion) {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerOficinas(usuario, autorizacion);
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public List obtenerOficinas() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerOficinas();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public List obtenerServiciosDestino() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerServiciosDestino();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public List obtenerDocumentos() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerDocumentos();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public List obtenerIdiomas() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerIdiomas();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public List obtenerMunicipiosBaleares() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerMunicipiosBaleares();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public String obtenerDescripcionOficina(String codigoOficina) {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerDescripcionOficina(codigoOficina);
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public String obtenerFecha() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerFecha();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     */
    public String obtenerHorasMinutos() {
        try {
            ParametrosDAO daoParams = ParametrosDAOFactory.getInstance().getParametrosDAO(daoImplParametros);
            return daoParams.obtenerHorasMinutos();
        } catch (Exception exc) {
            log.error(exc);
            throw new EJBException(exc);
        }
    }

    private String getFecha() {
        DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaTest = new java.util.Date();
        return dateF.format(fechaTest);
    }

    private String getHorasMinutos() {
        DateFormat dateF = new SimpleDateFormat("HH:mm");
        java.util.Date fechaTest = new java.util.Date();
        return dateF.format(fechaTest);
    }
}
