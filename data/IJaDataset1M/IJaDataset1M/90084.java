package com.egladius.exanet.web.servlets;

import com.egladius.exanet.gestion.Modelo;
import javax.servlet.http.*;
import java.io.*;
import javax.swing.*;

/**
 * <p>T�tulo: ServletTerminarExamen</p>
 * <p>Descripci�n: Servlet ejecutado al terminar un examen</p>
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
public class ServletTerminarExamen extends HttpServlet {

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
            Boolean hecho = (Boolean) sesion.getAttribute("hecho");
            if (hecho != null) {
                if (hecho.booleanValue()) {
                    System.out.println("Error - TerminarExamen, Descripci�n:" + "atrasAlante");
                    try {
                        response.sendRedirect("error.jsp?texto=Error - TerminarExamen");
                    } catch (IOException ex) {
                        System.out.println("Error - TerminarExamen, Descripci�n:" + ex);
                    }
                }
            }
            double resultado = 0;
            Modelo modelo = (Modelo) sesion.getAttribute("modelo");
            if (modelo == null) {
                modelo = new Modelo();
                sesion.setAttribute("modelo", modelo);
            }
            String tiempo = request.getParameter("cronometro");
            if (tiempo.length() < 8) tiempo = "00:" + tiempo;
            System.out.println("Tiempo transcurrido: " + tiempo);
            int tiempoInt;
            try {
                String horas = tiempo.substring(0, 2);
                String minutos = tiempo.substring(3, 5);
                String segundos = tiempo.substring(6, 8);
                int horasInt = Integer.parseInt(horas);
                int minutosInt = Integer.parseInt(minutos);
                int segundosInt = Integer.parseInt(segundos);
                if (segundosInt > 0) {
                    minutosInt++;
                }
                tiempoInt = (horasInt * 60) + minutosInt;
                modelo.setTiempoExamen(tiempoInt);
                resultado = modelo.terminarExamen();
                sesion.setAttribute("hecho", new Boolean(true));
                response.sendRedirect("examenFinalizado.jsp?nota=" + String.valueOf(resultado));
            } catch (IOException ex) {
                System.out.println("Se produjo un error en " + this.getClass() + ex.getMessage());
            } catch (Exception ex2) {
                System.out.println(ex2);
                modelo.setTiempoExamen(0);
                try {
                    response.sendRedirect("examenFinalizado.jsp?nota=" + String.valueOf(resultado));
                } catch (IOException ex1) {
                }
            }
        } catch (Exception ex1) {
            System.out.println("Error - ServletTerminarExamen, Descripci�n:" + ex1.toString());
            try {
                response.sendRedirect("error.jsp?texto=Error - ServletTerminarExamen");
            } catch (IOException ex) {
                System.out.println("Error - ServletTerminarExamen, Descripci�n:" + ex);
            }
        }
    }

    /**
     * Metodo para atender a las peticiones del tipo post
     * @param request Peticion del cliente
     * @param response respuestas del server
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }
}
