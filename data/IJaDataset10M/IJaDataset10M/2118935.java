package edu.cibertec.action;

import java.io.Reader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class TipoUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SqlMapClient sqlMap;
        try {
            String resource = "SqlMapConfigApplication.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error cargando configuracion en TipoUsuarioAction Cause: " + e);
        }
        List listaTipoUsuario = sqlMap.queryForList("getTipoUsuarios");
        if (listaTipoUsuario.size() > 0) {
            HttpSession session = request.getSession();
            session.setAttribute("listaTipoUsuario", listaTipoUsuario);
            return mapping.findForward("success");
        } else {
            return null;
        }
    }
}
