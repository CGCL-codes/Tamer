package com.iver.andami.messages;

import java.util.EventListener;

/**
 * Interfaz implementada por las extensiones que quieran informar del estado de
 * su ejecuci�n. Por ejemplo un extension que tarda mucho en ejecutarse puede
 * implementar esta interfaz y a�adirse como ProgressListener de la
 * aplicacion. De esta manera la aplicaci�n le pedir� su estado a intervalos
 * regulares de tiempo y mostrar� esta informaci�n en la barra de estado
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface ProgressListener extends EventListener {

    /**
     * M�todo invocado a intervalos regulares por la aplicaci�n y en el que se
     * debe devolver el estado de una supuesta ejecuci�n
     *
     * @return Cadena que se env�a a la aplicaci�n
     */
    public String getProgress();

    /**
     * Devuelve un porcentaje que indica el progreso de la tarea
     *
     * @return n�mero del 0 al 100
     */
    public int getProgressValue();
}
