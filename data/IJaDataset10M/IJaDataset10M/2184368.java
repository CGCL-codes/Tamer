package com.egladius.exanet.idioma;

import java.util.TreeMap;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.Serializable;
import com.egladius.exanet.web.servlets.InicializacionListener;

/**
 * <p>T�tulo: Ingles</p>
 * <p>Descripci�n: Clase q implementa el diccionario de ingles</p>
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
public class Ingles implements Idioma, Serializable {

    /**
     * Mapa q contiene los terminos del lenguaje.
     */
    private TreeMap mapa = new TreeMap();

    /**
     * Constructor por defecto
     */
    public Ingles() {
        String camino = null;
        try {
            camino = InicializacionListener.getInstancia().getRuta();
            camino = camino + "WEB-INF" + File.separator + "config" + File.separator + "ing.len";
            ObjectInputStream ficheroEntrada = new ObjectInputStream(new FileInputStream(camino));
            mapa = (TreeMap) ficheroEntrada.readObject();
        } catch (ClassNotFoundException ex1) {
        } catch (IOException ex1) {
        }
    }

    /**
     * metodo q dada una claver devuelve
     * @param _codigo Codigo del texto a buscar
     * @return El valor de la clave pasada como parametro
     */
    public String getValor(String _codigo) {
        return (String) mapa.get(_codigo);
    }
}
