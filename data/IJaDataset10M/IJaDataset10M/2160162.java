package org.compiere.db;

import java.util.*;

/**
 *  Norwegian Connection Resource Strings
 *
 *  @author     Olaf Slazak L�ken
 *  @version    $Id: DBRes_no.java,v 1.2 2006/07/30 00:55:13 jjanke Exp $
 */
public class DBRes_no extends ListResourceBundle {

    /** Data        */
    static final Object[][] contents = new String[][] { { "CConnectionDialog", "Adempiere Forbindelse" }, { "Name", "Navn" }, { "AppsHost", "Applikasjon  Maskine" }, { "AppsPort", "Applikasjon  Port" }, { "TestApps", "Test Applikasjon " }, { "DBHost", "Database Maskin" }, { "DBPort", "Database Port" }, { "DBName", "Database Navn" }, { "DBUidPwd", "Bruker /Passord" }, { "ViaFirewall", "Gjennom Brannmur" }, { "FWHost", "Brannmur Maskin" }, { "FWPort", "Brannmur Port" }, { "TestConnection", "Test Database" }, { "Type", "Database Type" }, { "BequeathConnection", "Bequeath Forbindelse" }, { "Overwrite", "Overskriv" }, { "ConnectionProfile", "Connection" }, { "LAN", "LAN" }, { "TerminalServer", "Terminal Server" }, { "VPN", "VPN" }, { "WAN", "WAN" }, { "ConnectionError", "Feil ved Oppkobling" }, { "ServerNotActive", "Server Ikke Aktivert" } };

    /**
	 * Get Contsnts
	 * @return contents
	 */
    public Object[][] getContents() {
        return contents;
    }
}
