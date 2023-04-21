package com.iprojectmanager.businessclass;

import java.io.*;
import javax.servlet.*;
import com.iprojectmanager.managers.*;
import com.iprojectmanager.entities.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author JKA
 */
public class LiderControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opcion = null;
        String operation = request.getParameter("operacionesintegrante");
        String usuario = (String) request.getAttribute("usuario");
        if ((opcion = request.getParameter("operacionesintegrante")) != null) {
            if (opcion.compareTo("Agregar Integrantes") == 0) {
                request.getRequestDispatcher("VistaAgregarIntegrante").forward(request, response);
            } else if (opcion.compareTo("Quitar Integrantes") == 0) {
                quitarIntegrantes(request, response);
                request.getRequestDispatcher("VistaLider").forward(request, response);
            } else if (opcion.compareTo("Modificar Datos") == 0) {
                request.getRequestDispatcher("VistaOperacionUsuario").forward(request, response);
            } else if (opcion.compareTo("Asignar Roles") == 0) {
                request.getRequestDispatcher("VistaOperacionUsuario").forward(request, response);
            } else if (operation.compareToIgnoreCase("Evaluar Material") == 0) {
                System.out.println("usuarioD:" + usuario);
                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("VistaEvaluarMaterial").forward(request, response);
            }
        } else if ((opcion = request.getParameter("operacioneactividades")) != null) {
        } else if (request.getParameter("Aceptar") != null && request.getParameter("Aceptar").compareTo("agregar") == 0) {
            agregarIntegrantes(request, response);
            request.getRequestDispatcher("VistaLider").forward(request, response);
        }
    }

    public void agregarIntegrantes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager um = new UserManager();
        HttpSession session = request.getSession();
        LiderProyecto lider = (LiderProyecto) session.getAttribute("user");
        Proyecto proyecto = lider.getProyecto();
        String[] usuarios = request.getParameterValues("usuario");
        ArrayList<IntegranteProyecto> todos = um.getIntegrantes();
        if (usuarios != null && proyecto != null) {
            for (String idusuario : usuarios) {
                for (IntegranteProyecto integrante : todos) {
                    if (Long.parseLong(idusuario) == integrante.getId()) {
                        integrante.setProyecto(proyecto);
                        um.exec(integrante, UserManager.UPDATE);
                        break;
                    }
                }
            }
        }
    }

    public void quitarIntegrantes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager um = new UserManager();
        String[] usuarios = request.getParameterValues("usuario");
        ArrayList<IntegranteProyecto> todos = um.getIntegrantes();
        ActivitiesManager manager = new ActivitiesManager();
        if (usuarios != null) {
            for (String idusuario : usuarios) {
                for (IntegranteProyecto integrante : todos) {
                    if (Long.parseLong(idusuario) == integrante.getId()) {
                        for (Actividad actividad : integrante.getActividades()) {
                            actividad.setResponsable(null);
                            manager.exec(actividad, IProjectManager.UPDATE);
                        }
                        integrante.setProyecto(null);
                        um.exec(integrante, UserManager.UPDATE);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Control de operaciones para el Lider de Proyecto";
    }
}
