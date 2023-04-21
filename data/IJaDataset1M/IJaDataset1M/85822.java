package org.openXpertya.util;

import java.util.ListResourceBundle;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya
 */
public class IniRes_es extends ListResourceBundle {

    /** Descripción de Campos */
    static final Object[][] contents = new String[][] { { "Licencia_OXP", "T" + "é" + "rminos de la licencia p" + "ú" + "blica de utilizaci" + "ó" + "n" }, { "Do_you_accept", "¿" + " Acepta la licencia ?" }, { "No", "No" }, { "Yes_I_Understand", "SI, Entiendo y acepto" }, { "license_htm", "org/openXpertya/install/Licencia.html" }, { "License_rejected", "Licencia rehusada o no encontrada." } };

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public Object[][] getContents() {
        return contents;
    }
}
