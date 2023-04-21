package za.co.data.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author darryl
 */
public class StringUtils {

    public static final String NO_PREVIEW_AVAILABLE = "<html><body><h3><center>No Preview Available</center></h3></body></html>";

    public static String HIBERNATE_TEMPLATE = "<?xml version='1.0' encoding='utf-8'?>\n" + "<!DOCTYPE hibernate-configuration PUBLIC\n" + "\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"\n" + "\"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\n" + "<hibernate-configuration>\n" + "<session-factory>\n" + "<!-- Database connection settings -->\n" + "<property name=\"connection.driver_class\">org.postgresql.Driver</property>\n" + "<property name=\"connection.url\"></property>\n" + "<property name=\"connection.username\">postgres</property>\n" + "<property name=\"connection.password\">postgres</property>\n" + "<property name=\"connection.url\"></property>\n" + "<property name=\"connection.username\"></property>\n" + "<property name=\"connection.password\"></property-->\n" + "<!-- End Database connection settings -->\n" + "<!-- JDBC connection pool (use the built-in) -->\n" + "<property name=\"connection.pool_size\">1</property>\n" + "<!-- SQL dialect -->\n" + "<property name=\"dialect\"></property>\n" + "<!-- Enable Hibernate's automatic session context management -->\n" + "<property name=\"current_session_context_class\">thread</property>\n" + "<!-- Disable the second-level cache -->\n" + "<property name=\"cache.provider_class\">org.hibernate.cache.NoCacheProvider</property>\n" + "<!-- Echo all executed SQL to stdout -->\n" + "<property name=\"show_sql\">false</property>\n" + "<!-- Drop and re-create the database schema on startup -->\n" + "<property name=\"hbm2ddl.auto\">update</property>\n" + "<!-- FASTTRACK MODEL DEF START SECTION -->\n" + "<!-- FASTTRACK MODEL DEF END SECTION -->\n" + "</session-factory>\n" + "</hibernate-configuration>\n";

    public static String camelize(String input, boolean startWithLowerCase) {
        StringBuffer sb = new StringBuffer();
        String[] str = input.split(" ");
        for (String temp : str) {
            if (temp.isEmpty() || temp == null) {
                break;
            }
            temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
            sb.append(temp);
        }
        if (startWithLowerCase) sb.replace(0, 1, "" + Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    public static List getShortName(Collection<String> CLASSES) {
        List<String> shortNames = new ArrayList<String>();
        for (String name : CLASSES) {
            try {
                shortNames.add(ClassUtils.loadClass(name).getSimpleName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return shortNames;
    }

    /**
     * Ensure the field name starts with a lower case
     *
     * @param name
     * @return
     */
    public static String fieldName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
