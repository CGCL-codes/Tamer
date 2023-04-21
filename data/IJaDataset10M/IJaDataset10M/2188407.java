package org.compiere.install;

import java.util.*;

/**
 *	Setup Resources
 *
 * 	@author 	Jordi Luna
 * 	@version 	$Id: SetupRes_es.java,v 1.3 2006/07/30 00:57:42 jjanke Exp $
 */
public class SetupRes_es extends ListResourceBundle {

    /**	Translation Info	*/
    static final Object[][] contents = new String[][] { { "AdempiereServerSetup", "Configuración Servidor Adempiere" }, { "Ok", "Aceptar" }, { "File", "Fichero" }, { "Exit", "Salir" }, { "Help", "Ayuda" }, { "PleaseCheck", "Por favor Compruebe" }, { "UnableToConnect", "No se ha podido obtener ayuda de la web de Adempiere" }, { "AdempiereHomeInfo", "Adempiere Home es la Carpeta Principal" }, { "AdempiereHome", "Adempiere Home" }, { "WebPortInfo", "Puerto Web (HTML)" }, { "WebPort", "Puerto Web" }, { "AppsServerInfo", "Nombre Servidor Aplicación" }, { "AppsServer", "Servidor Aplicación" }, { "DatabaseTypeInfo", "Tipo Base de Datos" }, { "DatabaseType", "Tipo Base de Datos" }, { "DatabaseNameInfo", "Nombre Base de Datos (Servicio)" }, { "DatabaseName", "Nombre Base de Datos" }, { "DatabasePortInfo", "Puerto Escucha Base de Datos" }, { "DatabasePort", "Puerto Base de Datos" }, { "DatabaseUserInfo", "ID Usuario Base de Datos Adempiere" }, { "DatabaseUser", "Usuario Base de Datos" }, { "DatabasePasswordInfo", "Contraseña Usuario Base de Datos Adempiere" }, { "DatabasePassword", "Contraseña Base de Datos" }, { "TNSNameInfo", "Bases de Datos Encontradas" }, { "TNSName", "Buscar Bases de Datos" }, { "SystemPasswordInfo", "Contraseña Usuario System" }, { "SystemPassword", "Contraseña System" }, { "MailServerInfo", "Servidor Correo" }, { "MailServer", "Servidor Correo" }, { "AdminEMailInfo", "Email Administrador Adempiere" }, { "AdminEMail", "Email Admin" }, { "DatabaseServerInfo", "Nombre Servidor Base de Datos" }, { "DatabaseServer", "Servidor Base de Datos" }, { "JavaHomeInfo", "Carpeta Java Home" }, { "JavaHome", "Java Home" }, { "JNPPortInfo", "Puerto JNP Servidor Aplicación" }, { "JNPPort", "Puerto JNP" }, { "MailUserInfo", "Usuario Correo Adempiere" }, { "MailUser", "Usuario Correo" }, { "MailPasswordInfo", "Contraseña Usuario Correo Adempiere" }, { "MailPassword", "Contraseña Correo" }, { "KeyStorePassword", "Contraseña Key Store" }, { "KeyStorePasswordInfo", "Contraseña para SSL Key Store" }, { "JavaType", "Java VM" }, { "JavaTypeInfo", "Proveedor Java VM" }, { "AppsType", "Tipo Servidor" }, { "AppsTypeInfo", "Tipo Servidor Aplicaciones J2EE" }, { "DeployDir", "Despliegue" }, { "DeployDirInfo", "Directorio Despliegue J2EE" }, { "ErrorDeployDir", "Error Directorio Despliegue" }, { "TestInfo", "Probar Configuración" }, { "Test", "Probar" }, { "SaveInfo", "Guardar Configuración" }, { "Save", "Guardar" }, { "HelpInfo", "Obtener Ayuda" }, { "ServerError", "Error Configuración Servidor" }, { "ErrorJavaHome", "Error Java Home" }, { "ErrorAdempiereHome", "Error Adempiere Home" }, { "ErrorAppsServer", "Error Servidor Aplicación (no utilizar localhost)" }, { "ErrorWebPort", "Error Puerto Web" }, { "ErrorJNPPort", "Error Puerto JNP" }, { "ErrorDatabaseServer", "Error Servidor Base de Datos (no utilizar localhost)" }, { "ErrorDatabasePort", "Error Puerto Base de Datos" }, { "ErrorJDBC", "Error Connexión JDBC" }, { "ErrorTNS", "Error Connexión TNS" }, { "ErrorMailServer", "Error Servidor Correo (no utilizar localhost)" }, { "ErrorMail", "Error Correo" }, { "ErrorSave", "Error Guardando Ficheros" }, { "EnvironmentSaved", "Archivo de Entorno guardado .... empezando Despliegue\n" + "Puede volver a arrancar el Servidor de la Aplicación cuando el programa finalice.\n" + "Por favor compruebe el archivo de errores.\n" } };

    /**
	 * 	Get Content
	 * 	@return content array
	 */
    public Object[][] getContents() {
        return contents;
    }
}
