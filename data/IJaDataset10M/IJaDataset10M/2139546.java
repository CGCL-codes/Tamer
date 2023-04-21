package com.egladius.exanet.controlBBDD;

import javax.swing.table.*;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.DefaultListModel;

/**
 * <p>T�tulo: BBDDAsignaturas</p>
 * <p>Descripci�n: Clase q implementa la comunicacion con a tabla Asignaturas de la BBDD</p>
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
public class BBDDAsignaturas implements BBDDTabla {

    /**
     * Estrategia a seguir
     */
    private strategyBBDDAsignaturas strategy;

    /**
     * Unica Instancia de la clase;
     */
    private static BBDDAsignaturas instancia = new BBDDAsignaturas();

    /**
     * Metodo accedente a la instancia.
     * @return instancia
     */
    public static BBDDAsignaturas getBBDDAsignaturas() {
        return instancia;
    }

    /**
     * Constructor por defecto
     */
    private BBDDAsignaturas() {
    }

    /**
     * <p>
     * Devuelve el nombre de la asigntura con coda, consultita sql powa....
     * </p>
     * @param _coda Codigo de la asignatura
     * @return El nombre de la asignatura con codigo _coda.
     */
    public String getNombreAsignatura(String _coda) {
        return strategy.getNombreAsignatura(_coda);
    }

    /**
     * Metodo accedente al defaultListModel de la tabla
     * @return DeafaultListModel con la tabla
     */
    public String getTabla() {
        return strategy.getTabla();
    }

    public DefaultListModel getListaAsignaturas() {
        return strategy.getListaAsignaturas();
    }

    /**
     * Genera la interfaz de edici�n de registros de esta tabla
     * @param clave1 primera clave
     * @param clave2 segunda clave
     * @return c�digo HTML de la interfaz
     */
    public String getEditor(String clave1, String clave2) {
        return strategy.getEditor(clave1, clave2);
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
     * @param claves vector con el codigo de la asignatura
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
     * M�todo que editar el registro de la tabla
     * @param _claves de la tabla
     * @param _datos a actualizar
     */
    public void editarRegistro(Vector _claves, Vector _datos) {
        strategy.editarRegistro(_claves, _datos);
    }

    /**
     * M�todo que devuelve el c�digo num�rico de la asignatura
     * @param _coda string de la asignatura
     * @return el CODA num�rico de la asignatura
     */
    public String getCoda(String _coda) {
        return strategy.getCoda(_coda);
    }

    /**
     * Accedente al strategy
     * @return strategy
     */
    public strategyBBDDAsignaturas getStrategy() {
        return strategy;
    }

    /**
     * Mutador del strategy
     * @param _strategy nuevo strategy
     */
    public void setStrategy(strategyBBDDAsignaturas _strategy) {
        strategy = _strategy;
    }

    public Vector getAllCodigos() {
        return strategy.getAllCodigos();
    }

    public Vector getAllCursos() {
        return strategy.getAllCursos();
    }

    public Vector getAllCreditos() {
        return strategy.getAllCreditos();
    }

    /**
     * M�todo que devuelve el curso dependiendo del codigo
     * @param coda codigo de la asignatura
     * @return el curso
     */
    public String getCurso(String coda) {
        return strategy.getCurso(coda);
    }

    /**
     * M�todo que devuelve los creditos de la asignatura
     * @param coda de la asignatura
     * @return creditos
     */
    public String getCreditos(String coda) {
        return strategy.getCreditos(coda);
    }

    public ResultSet getResultSet(String sql) {
        return strategy.getResultSet(sql);
    }
}
