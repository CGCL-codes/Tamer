package org.openXpertya.install;

import java.util.ListResourceBundle;

/**
 * DescripciÃ¯Â¿Â½n de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya
 */
public class SetupRes_pl extends ListResourceBundle {

    /** Descripción de Campo */
    static final Object[][] contents = new String[][] { { "InstalarServidorOXP", "Konfiguracja serwera OpenXpertya" }, { "Ok", "Ok" }, { "File", "Plik" }, { "Exit", "Wyjście" }, { "Help", "Pomoc" }, { "PleaseCheck", "Proszę sprawdzić" }, { "UnableToConnect", "Nie można połączyć się ze stroną OpenXpertya w celu uzyskania pomocy" }, { "OXPHomeInfo", "Folder OpenXpertya jest folderem głównym" }, { "OXPHome", "Folder OpenXpertya" }, { "WebPortInfo", "Web (HTML) Port" }, { "WebPort", "Web Port" }, { "AppsServerInfo", "Nazwa serwera aplikacji" }, { "AppsServer", "Serwer bazy danych" }, { "DatabaseTypeInfo", "Typ bazy danych" }, { "DatabaseType", "Typ bazy danych" }, { "DatabaseNameInfo", "Nazwa bazy danych " }, { "DatabaseName", "Nazwa bazy danych (SID)" }, { "DatabasePortInfo", "Port listenera bazy danych" }, { "DatabasePort", "Port bazy danych" }, { "DatabaseUserInfo", "Użytkownik OpenXpertya w bazie danych" }, { "DatabaseUser", "Użytkownik bazy" }, { "DatabasePasswordInfo", "Hasło użytkownika OpenXpertya" }, { "DatabasePassword", "Hasło użytkownika" }, { "TNSNameInfo", "TNS lub Globalna Nazwa Bazy (dla Oracle)" }, { "TNSName", "Nazwa TNS" }, { "SystemPasswordInfo", "Hasło dla użytkownika System w bazie danych" }, { "SystemPassword", "Hasło System" }, { "MailServerInfo", "Serwer pocztowy" }, { "MailServer", "Serwer pocztowy" }, { "AdminEMailInfo", "Adres email administartora OpenXpertya" }, { "AdminEMail", "EMail administ." }, { "DatabaseServerInfo", "Nazwa serwera bazy danych" }, { "DatabaseServer", "Serwer bazy danych" }, { "JavaHomeInfo", "Folder Javy" }, { "JavaHome", "Folder Javy" }, { "JNPPortInfo", "Application Server JNP Port" }, { "JNPPort", "JNP Port" }, { "MailUserInfo", "Użytkownik poczty dla celów administracyjnych OpenXpertya" }, { "MailUser", "Użytkownik poczty" }, { "MailPasswordInfo", "Hasło dla konta pocztowego OpenXpertya" }, { "MailPassword", "Hasło poczty" }, { "KeyStorePassword", "Key Store Password" }, { "KeyStorePasswordInfo", "Password for SSL Key Store" }, { "JavaType", "Java VM" }, { "JavaTypeInfo", "Java VM Vendor" }, { "AppsType", "Server Type" }, { "AppsTypeInfo", "J2EE Application Server Type" }, { "DeployDir", "Deployment" }, { "DeployDirInfo", "J2EE Deployment Directory" }, { "ErrorDeployDir", "Error Deployment Directory" }, { "TestInfo", "Sprawdź ustawienia" }, { "Test", "Testuj" }, { "SaveInfo", "Zapisz ustawienia" }, { "Save", "Zapisz" }, { "HelpInfo", "Pomoc" }, { "ServerError", "Błędne ustawienia" }, { "ErrorJavaHome", "Niepoprawny folder Javy" }, { "ErrorOXPHome", "Nie stwierdzono zainstalowanego systemu OpenXpertya w miescu wskazanym jako Folder OpenXpertya" }, { "ErrorAppsServer", "Niepoprawny serwer aplikacji (nie może być localhost)" }, { "ErrorWebPort", "Niepoprawny port WWW (być może inna aplikacja używa już tego portu)" }, { "ErrorJNPPort", "Niepoprawny port JNP (być może inna aplikacja używa już tego portu)" }, { "ErrorDatabaseServer", "Niepoprawny serwer bazy (nie może być localhost)" }, { "ErrorDatabasePort", "Niepoprawny port serwer bazy" }, { "ErrorJDBC", "Wystąpił błąd przy próbie połącznia się z bazą danych" }, { "ErrorTNS", "Wystąpił błąd przy próbie połącznia się z bazą danych poprzez TNS" }, { "ErrorMailServer", "Niepoprawny serwer pocztowy (nie może być localhost)" }, { "ErrorMail", "Błąd poczty" }, { "ErrorSave", "Błąd przy zapisywaniu konfiguracji" }, { "EnvironmentSaved", "Ustawienia zapisany\nMusisz ponownie uruchomić serwer." }, { "RMIoverHTTP", "Tunelowanie RMI over HTTP" }, { "RMIoverHTTPInfo", "Tunelowanie RMI over HTTP pozwala używać OpenXpertya przez firewall" } };

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
