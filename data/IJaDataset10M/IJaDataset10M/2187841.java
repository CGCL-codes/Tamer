package org.openXpertya.db;

import java.util.ListResourceBundle;

/**
 *  Connection Resource Strings
 *
 *  @author Comunidad de Desarrollo openXpertya
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *     .
 *  @version    $Id: DBRes_ml.java,v 1.4 2005/03/11 20:29:00 jjanke Exp $
 */
public class DBRes_ml extends ListResourceBundle {

    /** Data */
    static final Object[][] contents = new String[][] { { "CConnectionDialog", "Libertya Connection" }, { "Name", "Name" }, { "AppsHost", "Application Host" }, { "AppsPort", "Application Port" }, { "TestApps", "Test Application" }, { "DBHost", "Database Host" }, { "DBPort", "Database Port" }, { "DBName", "Database Name" }, { "DBUidPwd", "User / Password" }, { "ViaFirewall", "via Firewall" }, { "FWHost", "Firewall Host" }, { "FWPort", "Firewall Port" }, { "TestConnection", "Test Database" }, { "Type", "Database Type" }, { "BequeathConnection", "Bequeath Connection" }, { "Overwrite", "Overwrite" }, { "ConnectionError", "Connection Error" }, { "ServerNotActive", "Server Not Active" } };

    /**
     * Get Contsnts
     * @return contents
     */
    public Object[][] getContents() {
        return contents;
    }
}
