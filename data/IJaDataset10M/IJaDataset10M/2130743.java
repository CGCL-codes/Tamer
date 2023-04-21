package org.openXpertya.process;

import java.io.Serializable;
import javax.sql.RowSet;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class RemoteMergeDataVO implements Serializable {

    /** Descripción de Campos */
    public Boolean Test = Boolean.FALSE;

    /** Descripción de Campos */
    public String TableName = null;

    /** Descripción de Campos */
    public String Sql = null;

    /** Descripción de Campos */
    public String[] KeyColumns = null;

    /** Descripción de Campos */
    public RowSet CentralData = null;

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        return "RemoteNewDataVO[test=" + Test + "-" + TableName + "]";
    }
}
