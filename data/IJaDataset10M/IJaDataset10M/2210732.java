package com.chuidiang.editores.comun;

/**
 * Interface comun para todos los Component que permite editar un dato.
 */
public interface InterfaceEdicionDatos<Tipo> {

    /**
    * Recoge el dato que se le pasa, comprueba su validez y lo muestra en
    * pantalla.<br>
    *
    * @param dato Dato que se quiere mostrar en el Component.<br>
    */
    public void setDato(Tipo dato);

    /**
    * Recoge el dato del Component, verifica su validez y lo devuelve.<br>
    * Si se pasa un dato como parametro, lo rellena y es el mismo dato que devuelve.<br>
    * Si se pasa null como parametro, crea un dato nuevo y lo devuelve.<br>
    *
    * @param dato Dato donde se quiere que el editor deje los resultados. Puede
    *        ser null.<br>
    *
    * @return El mismo dato que se pasa como parametro con los datos del
    *         editor. Un dato nuevo si el parametro es null.<br>
    */
    public Tipo getDato();

    /**
    * Hace que el editor sea o no editable.<br>
    *
    * @param editable Indica si se quiere que el editor sea editable.<br>
    */
    public void hazEditable(boolean editable);

    /**
    * Comprueba que los datos mostrados en el Component son correctos.<br>
    * Devuelve true si es asi, false en caso contrario.<br>
    *
    * @param error Devuelve un texto con el error en caso de producirse.<br>
    *
    * @return true si los datos en el Component son correctos, false en caso
    *         contrario.<br>
    */
    public boolean validaDato(StringBuffer error);
}
