package ebadat.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Sonia
 */
public class DAOFactory {

    static final Logger logger = Logger.getLogger(DAOFactory.class);

    private static BeanFactory factory;

    private static DAOFactory daoFactory;

    private static Properties props;

    static {
        String iniFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "conf.ini";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(iniFile))));
            logger.info("archivo ini:" + iniFile);
            props = new Properties();
            String linea = "";
            while ((linea = br.readLine()) != null) if (linea.startsWith("driver=")) props.setProperty("driver", linea.substring("driver=".length())); else if (linea.startsWith("url=")) props.setProperty("url", linea.substring("url=".length())); else if (linea.startsWith("user=")) props.setProperty("username", linea.substring("user=".length())); else if (linea.startsWith("password=")) props.setProperty("password", linea.substring("password=".length()));
            br.close();
        } catch (IOException ex) {
            logger.warn(ex, ex);
        }
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/ebadat/resources/spring.xml" });
        factory = (BeanFactory) context;
        logger.info("BeanFactory cargado:" + factory);
    }

    public static UsuariosDAO getAplicacionDAO() {
        return (UsuariosDAO) factory.getBean("UsuariosDAO");
    }

    public static DepartamentosDAO getDepartamentosDAO() {
        return (DepartamentosDAO) factory.getBean("DepartamentosDAO");
    }

    public static ProvinciasDAO getProvinciasDAO() {
        return (ProvinciasDAO) factory.getBean("ProvinciasDAO");
    }

    public static DistritosDAO getDistritosDAO() {
        return (DistritosDAO) factory.getBean("DistritosDAO");
    }

    public static CentroPobladosDAO getCentrosPobladosDAO() {
        return (CentroPobladosDAO) factory.getBean("CentroPobladosDAO");
    }

    public static ComunesDAO getComunesDAO() {
        return (ComunesDAO) factory.getBean("ComunesDAO");
    }

    public static PersonasDAO getPersonasDAO() {
        return (PersonasDAO) factory.getBean("PersonasDAO");
    }

    public static EstudiantesDAO getEstudiantesDAO() {
        return (EstudiantesDAO) factory.getBean("EstudiantesDAO");
    }

    public static CentrosDAO getCentrosDAO() {
        return (CentrosDAO) factory.getBean("CentrosDAO");
    }

    public static PerifericosDAO getPerifericosDAO() {
        return (PerifericosDAO) factory.getBean("PerifericosDAO");
    }

    public static FichasDAO getFichasDAO() {
        return (FichasDAO) factory.getBean("FichasDAO");
    }

    public static EvaluacionesDAO getEvaluacionesDAO() {
        return (EvaluacionesDAO) factory.getBean("EvaluacionesDAO");
    }

    public static HorariosDAO getHorariosDAO() {
        return (HorariosDAO) factory.getBean("HorariosDAO");
    }

    public static CodigosDAO getCodigosDAO() {
        return (CodigosDAO) factory.getBean("CodigosDAO");
    }

    public static Properties getConnectionProperties() {
        return props;
    }

    public static DatosDAO getDatosDAO() {
        return (DatosDAO) factory.getBean("DatosDAO");
    }

    public static PeriodosDAO getPeriodosDAO() {
        return (PeriodosDAO) factory.getBean("PeriodosDAO");
    }

    public static InventariosDAO getInventariosDAO() {
        return (InventariosDAO) factory.getBean("InventariosDAO");
    }

    public static ContactosDAO getContactosDAO() {
        return (ContactosDAO) factory.getBean("ContactosDAO");
    }
}
