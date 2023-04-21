package org.compiere.apps;

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ALoginRes_ro.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public final class ALoginRes_ro extends ListResourceBundle {

    /** Translation Content     */
    static final Object[][] contents = new String[][] { { "Connection", "Conexiune" }, { "Defaults", "Valori implicite" }, { "Login", "Autentificare" }, { "File", "Aplicaţie" }, { "Exit", "Ieşire" }, { "Help", "Ajutor" }, { "About", "Despre..." }, { "Host", "Server" }, { "Database", "Bază de date" }, { "User", "Utilizator" }, { "EnterUser", "Introduceţi identificatorul utilizatorului" }, { "Password", "Parolă" }, { "EnterPassword", "Introduceţi parola" }, { "Language", "Limbă" }, { "SelectLanguage", "Alegeţi limba dumneavoastră" }, { "Role", "Rol" }, { "Client", "Titular" }, { "Organization", "Organizaţie" }, { "Date", "Dată" }, { "Warehouse", "Depozit" }, { "Printer", "Imprimantă" }, { "Connected", "Conectat" }, { "NotConnected", "Neconectat" }, { "DatabaseNotFound", "Baza de date nu a fost găsită" }, { "UserPwdError", "Parola nu se potriveşte cu utilizatorul" }, { "RoleNotFound", "Rolul nu a fost găsit sau este incomplet" }, { "Authorized", "Autorizat" }, { "Ok", "OK" }, { "Cancel", "Anulare" }, { "VersionConflict", "Conflict de versiune:" }, { "VersionInfo", "server <> client" }, { "PleaseUpgrade", "Rulaţi programul de actualizare" } };

    /**
	 *  Get Contents
	 *  @return context
	 */
    public Object[][] getContents() {
        return contents;
    }
}
