package medimagesuite.utils.persistence;

import java.io.FileInputStream;
import java.util.Properties;
import medimagesuite.development.DevelopmentConstants;
import medimagesuite.util.SystemConstants;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * This program creates an empty database with the structure defined at the hibernate system
 * configuration files. Must be carefully used because the creation of the new database drops any
 * information previously stored. The main use is in the system test phase.
 * 
 * @author Daniel Fireman - danielfireman@yahoo.com.br
 * 
 * @see org.hibernate.cfg.Configuration
 * @see org.hibernate.tool.hbm2ddl.SchemaExport
 */
public class CreateDatabase {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Configuration configuration = new Configuration();
            Properties mainProperties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(SystemConstants.HIBERNATE_PROPERTIES);
            mainProperties.load(fileInputStream);
            configuration.setProperties(mainProperties);
            configuration.addInputStream(new FileInputStream(SystemConstants.PATIENT_BEAN_MAPPING));
            configuration.addInputStream(new FileInputStream(SystemConstants.EXAM_BEAN_MAPPING));
            configuration.addInputStream(new FileInputStream(SystemConstants.IMAGE_BEAN_MAPPING));
            SchemaExport export = new SchemaExport(configuration);
            export.drop(true, true);
            export.create(true, true);
            System.out.println("Database Created Successfully");
        } catch (Exception e) {
            if (DevelopmentConstants.DEBUG_ENABLED) e.printStackTrace();
        }
    }
}
