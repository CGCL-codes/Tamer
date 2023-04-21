package pe.com.bn.sach.mantenimiento.controller;

import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import pe.com.bn.sach.common.Util;
import pe.com.bn.sach.controller.BaseController;
import pe.com.bn.sach.domain.Bnchf07Item;
import pe.com.bn.sach.domain.Bnchf16Programa;
import pe.com.bn.sach.mantenimiento.form.ProgramaForm;
import pe.com.bn.sach.service.MantenimientoService;
import pe.com.bn.sach.service.SelectService;

/**
 * @author ce_dpcreditos04
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class ProgramaController extends BaseController {

    private String listar;

    Bnchf16Programa bnchf16Programa = new Bnchf16Programa();

    private MantenimientoService servicioMantenimiento;

    private SelectService servicioSelect;

    private String commandName;

    private Class commandClass;

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public ProgramaController() {
        ParameterMethodNameResolver resolver = new ParameterMethodNameResolver();
        resolver.setParamName("method");
        resolver.setDefaultMethodName("cargaListaPrograma");
        setMethodNameResolver(resolver);
    }

    public Bnchf16Programa formBuscarInit(HttpServletRequest request) {
        Bnchf16Programa bnchf16Programa = new Bnchf16Programa();
        bnchf16Programa.setF16IdPrograma(Util.copyLongc("0"));
        bnchf16Programa.setF16DescPrograma(null);
        return bnchf16Programa;
    }

    public Bnchf16Programa formBuscar(HttpServletRequest request) {
        Bnchf16Programa bnchf16Programa = new Bnchf16Programa();
        bnchf16Programa.setF16IdPrograma(Util.copyLongc(request.getParameter("txtCodigoPrograma")));
        bnchf16Programa.setF16DescPrograma("" + request.getParameter("txtDescPrograma"));
        return bnchf16Programa;
    }

    public ModelAndView cargaListaPrograma(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
        bnchf16Programa = formBuscarInit(request);
        try {
            mav.addObject("listarPrograma", servicioMantenimiento.listarPrograma(bnchf16Programa));
            Bnchf07Item bnchf07Item = new Bnchf07Item();
            bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("4"));
            mav.addObject("ListTipoDocumento", servicioSelect.listItem(bnchf07Item));
        } catch (Exception e) {
            mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
            request.setAttribute("ERROR_MSG", (String) " Error al cargar  lista programa " + e);
        }
        return mav;
    }

    public ModelAndView cargaListaProgramaAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_BuscarProgramaAjax");
        try {
            bnchf16Programa = formBuscar(request);
            mav.addObject("listarPrograma", servicioMantenimiento.listarPrograma(bnchf16Programa));
        } catch (Exception e) {
            bnchf16Programa = formBuscarInit(request);
            mav.addObject("listarPrograma", servicioMantenimiento.listarPrograma(bnchf16Programa));
        }
        return mav;
    }

    public ModelAndView nuevo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_RegistrarPrograma");
        mav = select(mav);
        return mav;
    }

    public ModelAndView select(ModelAndView mav) throws Exception {
        Bnchf07Item bnchf07Item = new Bnchf07Item();
        mav.addObject("ListDepartamento", servicioSelect.listUbigeoDepartamento());
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("4"));
        mav.addObject("ListTipoDocumento", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("7"));
        mav.addObject("ListZona", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("21"));
        mav.addObject("ListVia", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("22"));
        mav.addObject("ListExterior", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("23"));
        mav.addObject("ListInterior", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("7"));
        mav.addObject("ListUbicacion", servicioSelect.listItem(bnchf07Item));
        bnchf07Item.getId().getBnchf06Tabla().setF06IdTabla(Util.copyLong("33"));
        mav.addObject("ListEstado", servicioSelect.listItem(bnchf07Item));
        return mav;
    }

    public ModelAndView encontrarPrograma(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (request.getParameter("txtOpcion").equals("bajar")) mav = new ModelAndView("mantenimiento/programa/pro_BajaPrograma");
        if (request.getParameter("txtOpcion").equals("consultar")) mav = new ModelAndView("mantenimiento/programa/pro_ConsultaPrograma");
        if (request.getParameter("txtOpcion").equals("modificar")) mav = new ModelAndView("mantenimiento/programa/pro_ModificarPrograma");
        Bnchf16Programa bnchf16Programa = new Bnchf16Programa();
        try {
            bnchf16Programa.setF16IdPrograma(Util.copyLong("" + "" + request.getParameter("optSeleccion")));
            mav.addObject("Programa", servicioMantenimiento.encontrarPrograma(bnchf16Programa));
            mav = select(mav);
        } catch (Exception e) {
            mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
            request.setAttribute("ERROR_MSG", (String) " Error al cargar  lista programa " + e);
        }
        return mav;
    }

    Bnchf16Programa getForm(ProgramaForm institucionForm) {
        Bnchf16Programa bnchf16Programa = new Bnchf16Programa();
        bnchf16Programa.setF16IdPrograma(Util.copyLong("" + institucionForm.getTxtIdPrograma()));
        bnchf16Programa.setF16DescPrograma(Util.copyString("" + institucionForm.getTxtDescPrograma()));
        bnchf16Programa.setF16IdUsuaCrea("1");
        return bnchf16Programa;
    }

    public ModelAndView guardarPrograma(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ProgramaForm institucionForm = new ProgramaForm();
        institucionForm = (ProgramaForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
        try {
            bnchf16Programa = new Bnchf16Programa();
            bnchf16Programa = getForm(institucionForm);
            Connection cnx = null;
            try {
                cnx = getPoolConnection();
                mav.addObject("Msj", servicioMantenimiento.guardarPrograma(bnchf16Programa, cnx));
                if (cnx != null) cnx.commit();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (cnx != null) cnx.commit();
                if (cnx != null) freePoolConnection(cnx);
            }
        } catch (Exception e) {
            mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
            request.setAttribute("ERROR_MSG", (String) " Error al guardar programa " + e);
        }
        return cargaListaPrograma(request, response);
    }

    public ModelAndView modificarPrograma(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ProgramaForm institucionForm = new ProgramaForm();
        institucionForm = (ProgramaForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
        try {
            bnchf16Programa = new Bnchf16Programa();
            bnchf16Programa = getForm(institucionForm);
            Connection cnx = null;
            try {
                cnx = getPoolConnection();
                mav.addObject("Msj", servicioMantenimiento.modificarPrograma(bnchf16Programa, cnx));
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
            }
        } catch (Exception e) {
            mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
            request.setAttribute("ERROR_MSG", (String) " Error al modificar programa " + e);
        }
        return cargaListaPrograma(request, response);
    }

    public ModelAndView bajarPrograma(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ProgramaForm institucionForm = new ProgramaForm();
        institucionForm = (ProgramaForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
        try {
            bnchf16Programa = new Bnchf16Programa();
            bnchf16Programa = getForm(institucionForm);
            Connection cnx = null;
            try {
                cnx = getPoolConnection();
                mav.addObject("Msj", servicioMantenimiento.bajarPrograma(bnchf16Programa, cnx));
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mav = new ModelAndView("mantenimiento/programa/pro_BuscarPrograma");
            request.setAttribute("ERROR_MSG", (String) " Error al bajar programa " + e);
        }
        return cargaListaPrograma(request, response);
    }

    /**
	 * @return Devuelve commandName.
	 */
    public String getCommandName() {
        return commandName;
    }

    /**
	 * @param commandName El commandName a establecer.
	 */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
	 * @return Devuelve commandClass.
	 */
    public Class getCommandClass() {
        return commandClass;
    }

    /**
	 * @param commandClass El commandClass a establecer.
	 */
    public void setCommandClass(Class commandClass) {
        this.commandClass = commandClass;
    }

    /**
	 * @return Devuelve servicioMantenimiento.
	 */
    public MantenimientoService getServicioMantenimiento() {
        return servicioMantenimiento;
    }

    /**
	 * @param servicioMantenimiento El servicioMantenimiento a establecer.
	 */
    public void setServicioMantenimiento(MantenimientoService servicioMantenimiento) {
        this.servicioMantenimiento = servicioMantenimiento;
    }

    /**
	 * @return Devuelve servicioSelect.
	 */
    public SelectService getServicioSelect() {
        return servicioSelect;
    }

    /**
	 * @param servicioSelect El servicioSelect a establecer.
	 */
    public void setServicioSelect(SelectService servicioSelect) {
        this.servicioSelect = servicioSelect;
    }

    public Object getCommandObject(HttpServletRequest request) throws Exception {
        Object command = formBackingObject(request);
        bind(request, command);
        return command;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return commandClass.newInstance();
    }
}
