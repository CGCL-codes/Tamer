package com.proyectobloj.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

public class DatosUsuario {

    private HttpSession session;

    private String ip;

    private HttpServletRequest request;

    public DatosUsuario() {
        session = ServletActionContext.getRequest().getSession();
        request = ServletActionContext.getRequest();
        conocerIp();
    }

    public DatosUsuario(HttpServletRequest request, HttpSession session) {
        this.session = session;
        this.request = request;
        conocerIp();
    }

    private void conocerIp() {
        try {
            ip = request.getRemoteAddr();
        } catch (Exception ex) {
            ex.getMessage();
            this.ip = "desconocida";
        }
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
