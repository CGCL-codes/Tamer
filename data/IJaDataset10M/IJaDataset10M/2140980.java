package com.egladius.exanet.controlBBDD;

import java.sql.ResultSet;
import java.util.List;
import javax.swing.table.*;
import java.util.Vector;

/**
 * <p>T�tulo: Practica 5: BBDDSoluciones</p>
 * <p>Descripci�n: Clase q implementa la comunicacion con la tablas soluciones de la BBDD</p>
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
public class BBDDSoluciones implements BBDDTabla {

    /**
     * estrategia a seguir
     */
    private strategyBBDDSoluciones strategy;

    /**
     * Unica Instancia de la clase;
     */
    private static BBDDSoluciones instancia = new BBDDSoluciones();

    /**
     * Metodo accedente a la instancia.
     * @return instancia
     */
    public static BBDDSoluciones getBBDDSoluciones() {
        return instancia;
    }

    /**
     * Constructor por defecto
     */
    private BBDDSoluciones() {
    }

    /**
     * <p>
     * Devuelve una lista de los codigos de las respuestas correctas para una
     * pregunta.
     * </p>
     * @return La lista de las respuestas correctas de una pregunta.
     * @param _pregunta Codigo de la pregunta
     */
    public List getCodigosPreguntasCorrectas(String _pregunta) {
        return strategy.getCodigosPreguntasCorrectas(_pregunta);
    }

    /**
     * Metodo accedente al defaultListModel de la tabla
     * @return DeafaultListModel con la tabla
     */
    public String getTabla() {
        return strategy.getTabla();
    }

    /**
     * Metodo que actualiza la tabla a partir de un defaultTableModel
     * @param _modelo Modelo q le pasamos para actualizar la tabla
     */
    public void actualizaTabla(DefaultTableModel _modelo) {
        strategy.actualizaTabla(_modelo);
    }

    /**
     * Metodo q elimina un registro
     * @param claves idp, idr
     */
    public void eliminaRegistro(Vector claves) {
        if (claves == null || claves.isEmpty()) {
            return;
        }
        strategy.eliminaRegistro(claves);
    }

    /**
     * Metodo que aniade un nuevo registro
     * @param _vector Vector con los datos del nuevo registro
     */
    public void aniadeRegistro(Vector _vector) {
        strategy.aniadeRegistro(_vector);
    }

    /**
     * Genera la interfaz de edici�n de registros de esta tabla
     * @return c�digo HTML de la interfaz
     * @param clave1 primera clave
     * @param clave2 segunda clave
     */
    public String getEditor(String clave1, String clave2) {
        return strategy.getEditor(clave1, clave2);
    }

    /**
     * M�todo que edita un registro
     * @param _claves de la tabla
     * @param _datos a actualizar
     */
    public void editarRegistro(Vector _claves, Vector _datos) {
        strategy.editarRegistro(_claves, _datos);
    }

    /**
     * Accedente al strategy
     * @return strategy
     */
    public strategyBBDDSoluciones getStrategy() {
        return strategy;
    }

    /**
     * Mutador del strategy
     * @param _strategy nuevo strategy
     */
    public void setStrategy(strategyBBDDSoluciones _strategy) {
        strategy = _strategy;
    }

    /**
    * Accedente al strategy
    * @return vector con las preguntas
    */
    public Vector getAllPreguntas() {
        return strategy.getAllPreguntas();
    }

    /**
    * Accedente al strategy
    * @return vector con las respuestas
    */
    public Vector getAllRespuestas() {
        return strategy.getAllRespuestas();
    }

    public ResultSet getResultSet(String sql) {
        return strategy.getResultSet(sql);
    }
}
