package com.egladius.exanet.web.servlets;

import com.egladius.exanet.controlBBDD.BBDDCalifs;
import com.egladius.exanet.gestion.Examen;
import com.egladius.exanet.gestion.Modelo;
import javax.servlet.http.*;
import java.io.*;
import javax.swing.*;

/**
 * <p>T�tulo: ServletHacerExamen</p>
 * <p>Descripci�n: Servlet q implementa la accion de hacer un examen.</p>
 * <strong>Exanet</strong>, ex�menes en red. Esta aplicaci�n es de software libre;
 * puedes redistribuirla y/o modificarla acorde a los t�rminos de GNU Lesser General
 * Public License tal como es publicada por Free Software Foundation.
 * Copyright   1989, 1991 Free Software Foundation, Inc..
 * <p>@author <a href="mailto:exanet@terra.es">Grupo de desarrollo Exanet </a></p>
 * <p><a href="mailto:borjabi@teleline.es">Borja Blanco Iglesias</a></p>
 * <p><a href="mailto:jldiego@gmail.com">Diego Jim&eacute;nez L&oacute;pez</a></p>
 * <p><a href="mailto:jbarbasanchez@wanadoo.es">Jorge Barba S&aacute;nchez</a></p>
 * <p><a href="mailto:victorsanchezalonso@wanadoo.es">V&iacute;ctor S&aacute;nchez Alonso</a></p>
 * <p><a href="mailto:fjperezdiezma@gmail.com">Francisco Javier P&eacute;rez Diezma </a>  </p>
 * @version 3.0
 */
public class ServletHacerExamen extends HttpServlet {

    /**
   * Metodo ejecutado al crearse el server en el servidor.
   * Mostramos un mensaje por consola a modo de control del servidor.
   */
    public void init() {
        System.out.println("Creando servlet");
    }

    /**
  * Metodo ejecutado al destruirse el server
  * Mostramos un mensaje por consola a modo de control del servidor.
  */
    public void destroy() {
        System.out.println("Cerrando Servlet");
    }

    /**
   * Metodo q atiende a las peticiones de los clientes.
   * @param request Peticion del cliente
   * @param response respuestas del server
   */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession sesion = request.getSession();
            response.setHeader("Pragma", "no-cache");
            Modelo modelo = (Modelo) sesion.getAttribute("modelo");
            if (modelo == null) {
                modelo = new Modelo();
                sesion.setAttribute("modelo", modelo);
            }
            String indice = request.getParameter("indice");
            int indiceInt = Integer.parseInt(indice);
            DefaultListModel examenesDisponibles = (DefaultListModel) sesion.getAttribute("examenesDisponibles");
            sesion.removeAttribute("examenesDisponibles");
            Examen examen = (Examen) examenesDisponibles.get(indiceInt);
            sesion.setAttribute("examen", examen);
            modelo.asignarExamen(examen);
            BBDDCalifs.getBBDDCalifs().nuevaCalificacion(modelo.getDniAlumno(), examen.getCodAsignatura(), 0, 0);
            try {
                response.setHeader("Pragma", "no-cache");
                response.sendRedirect("examen.jsp");
            } catch (IOException ex) {
                System.out.println("Se produjo un error en " + this.getClass() + ex.getMessage());
            }
        } catch (Exception ex1) {
            System.out.println("Error - ServletHacerExamen, Descripci�n:" + ex1.toString());
            try {
                response.sendRedirect("error.jsp?texto=Error - ServletHacerExamen");
            } catch (IOException ex) {
                System.out.println("Error - ServletHacerExamen, Descripci�n:" + ex);
            }
        }
    }
}
