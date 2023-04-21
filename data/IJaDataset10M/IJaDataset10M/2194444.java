package ggc.core.db;

import com.atech.db.hibernate.HibernateConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GGCDbConfig 
 *  Description:  This is configuration file for GGC Database. This class is used for 
 *                all external and internal handling of database. It contains main 
 *                methods for accessing Hibernate Framework.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */
public class GGCDbConfig extends HibernateConfiguration {

    private static Log log = LogFactory.getLog(GGCDbConfig.class);

    private String[] db_files = { "GGC_Main.hbm.xml", "GGC_Nutrition.hbm.xml", "GGC_Other.hbm.xml", "GGC_Pump.hbm.xml", "GGC_CGM.hbm.xml" };

    /**
     * Constructor
     * 
     * @param val
     */
    public GGCDbConfig(boolean val) {
        super(val);
    }

    /**
     * Get Db Name
     * 
     * @return
     */
    public String getDbName() {
        return "GGCDb";
    }

    /**
     * Get Configuration File
     */
    @Override
    public String getConfigurationFile() {
        return "../data/GGC_Config.properties";
    }

    /**
     * Get Resource Files
     */
    @Override
    public String[] getResourceFiles() {
        return db_files;
    }

    /**
     * Load Default Database
     * 
     * @param config_found
     */
    @Override
    public void loadDefaultDatabase(boolean config_found) {
        db_num = 0;
        db_conn_name = "Internal Db (H2)";
        if (!config_found) log.info("GGCDb: Database configuration not found. Using default database.");
        log.info("GGCDb: Loading Db Configuration #" + db_num + ": " + db_conn_name);
        db_hib_dialect = "org.hibernate.dialect.H2Dialect";
        db_driver_class = "org.h2.Driver";
        db_conn_url = "jdbc:h2:../data/db/ggc_db";
        db_conn_username = "sa";
        db_conn_password = "";
    }

    /**
     * Is Check Enabled
     * 
     * @see com.atech.db.hibernate.check.DbCheckInterface#isCheckEnabled()
     */
    @Override
    public boolean isCheckEnabled() {
        return true;
    }

    /**
     * Get DbInfo Configuration
     * 
     * @see com.atech.db.hibernate.check.DbCheckInterface#getDbInfoConfiguration()
     */
    public String getDbInfoReportFilename() {
        return "../data/db_info.txt";
    }

    /**
     * Get Number Of Sessions
     * 
     * @see com.atech.db.hibernate.HibernateConfiguration#getNumberOfSessions()
     */
    @Override
    public int getNumberOfSessions() {
        return 2;
    }
}
