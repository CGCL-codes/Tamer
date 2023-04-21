package pe.gob.mef.logistica.catalogo.web.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jlis.core.bean.ErrorMessage;
import org.jlis.core.bean.Message;
import org.jlis.core.util.Constantes;
import org.jlis.core.util.HashUtil;
import org.jlis.jdbc.logic.JdbcLogicImpl;
import org.jlis.web.mvc.GenericBaseController;
import org.jlis.web.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import pe.gob.mef.logistica.security.bean.UsuarioLogistica;

public class CatalogoController extends GenericBaseController {

    private String ciListaSicon;

    private String ciEmpleado;

    public void setCiListaSicon(String ciListaSicon) {
        this.ciListaSicon = ciListaSicon;
    }

    public void setCiEmpleado(String ciEmpleado) {
        this.ciEmpleado = ciEmpleado;
    }

    public ModelAndView ciListaSicon(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UsuarioLogistica usuario = (UsuarioLogistica) session.getAttribute(Constantes.USUARIO);
        HashUtil datos = RequestUtil.getParameter(request);
        HashUtil filter = new HashUtil();
        filter.put("1", datos.getString("departamento"));
        if (datos.getString("departamento").equals("")) {
            filter.put("[2]", "all");
        }
        request.setAttribute("filter", filter);
        return new ModelAndView(ciListaSicon);
    }

    public ModelAndView ciEmpleado(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        HashUtil datos = RequestUtil.getParameter(request);
        HashUtil filter = new HashUtil();
        String codigoEmpleado = datos.getString("codigoEmpleado");
        filter.put("1", codigoEmpleado);
        HashUtil element = JdbcLogicImpl.getJdbcLogic().loadElement("adm.creditos.empleado.element", filter).addPrefix("EMPLOYEES");
        HashUtil elementComp = JdbcLogicImpl.getJdbcLogic().loadElement("adm.creditos.empleadoComp.element", filter).addPrefix("COMPLEMENTO");
        element.add(elementComp);
        request.setAttribute("filter", filter);
        request.setAttribute("codigoEmpleado", datos.getString("codigoEmpleado"));
        RequestUtil.setAttributes(request, element);
        return new ModelAndView(ciEmpleado);
    }
}
