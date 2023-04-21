package com.eidansoft.formularioHTML.impl;

import com.eidansoft.formularioHTML.IParametro;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alejandro Lorente
 * @author eidansoft ARROBA gmail PUNTITO com
 * @version 1.0
 * Es un objeto BEAN con dos parametros de texto
 * que son NOMBRE y VALOR y que forman el binomio de una variable.
 * 
 */
public class ParametroImpl implements IParametro, Comparable {

    /** Nombre de la variable */
    private String nombre;

    /** Valor de la variable */
    private String valor;

    /** Constructor pasandole como parámetro el nombre de la variable, el nombre debe estar compuesto
     *  por caracteres alfanuméricos exclusivamente, ya que no será convertido a UTF-8
     */
    public ParametroImpl(String n) throws ErrorFormularioException {
        this(n, "");
    }

    /** Constructor pasando como parámetro el nombre y el valor de la variable, el nombre debe estar compuesto
     *  por caracteres alfanuméricos exclusivamente, ya que no será convertido a UTF-8
     */
    public ParametroImpl(String n, String v) throws ErrorFormularioException {
        try {
            if (n.equals(URLEncoder.encode(n, "UTF-8"))) {
                nombre = n;
                valor = v;
            } else {
                throw new ErrorFormularioException("El nombre del parámetro contiene caracteres no válidos o no alfanuméricos");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new ErrorFormularioException("El nombre del parámetro contiene caracteres no válidos o no alfanuméricos");
        }
    }

    /** Obtener el nombre de la variable
     */
    public String getNombre() {
        return nombre;
    }

    /** Obtener el valor de la variable
     */
    public String getValor() {
        return valor;
    }

    /** Obtiene el binomio nombre=valor
     */
    public String getParametro() {
        String res = "";
        try {
            res = nombre + "=" + URLEncoder.encode(valor, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ParametroImpl.class.getName()).log(Level.SEVERE, "Error de codificación de los atributos", ex);
        }
        return res;
    }

    /** Método de comparacion por defecto, dos elementos son iguales si tienen el mismo nombre de la variable
     */
    public int compareTo(Object obj) {
        ParametroImpl tmp = (ParametroImpl) obj;
        return this.getNombre().compareTo(tmp.getNombre());
    }

    /** Modificar el valor de la variable
     */
    public void setValor(String v) {
        valor = v;
    }
}
