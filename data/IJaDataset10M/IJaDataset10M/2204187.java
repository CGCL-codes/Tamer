package com.lb.db;

import java.io.File;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class CreateDatabase {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String[] a = new String[] { "--properties=/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/etc/hibernate.properties", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Profilo.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Utenti.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Allegati.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/StoricoTicket.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Tickets.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Evento.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Stati.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Progetti.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/TipoIntervento.hbm.xml", "/home/edoardo/Documents/workspace-setup/tracsetup/src/main/java/com/setup/trac/pojo/Societa.hbm.xml" };
        SchemaExport.main(a);
    }
}
