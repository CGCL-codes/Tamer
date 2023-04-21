package net.sf.poormans.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import net.sf.poormans.Constants;
import net.sf.poormans.tool.PropertiesTool;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Adopt the basic configuration task. 1) Reads the common and user's properties and copy it into the {@link MyStringProperties}
 * which will be inject in all objects which requires these these properties. 2) Initializing spring. 
 * 
 * @version $Id: BasicConfigurator.java 2134 2011-07-17 12:14:41Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class BasicConfigurator {

    public static final String PROPERTIES_NAME = "poormans.properties";

    private File dataDir;

    private AbstractApplicationContext context;

    private Properties props;

    public BasicConfigurator() {
        init();
    }

    public BasicConfigurator(File dataDir) {
        System.setProperty("data.dir", dataDir.getAbsolutePath());
        init();
    }

    private void init() {
        if (System.getProperty("data.dir") == null) throw new IllegalArgumentException("No data directory set!");
        dataDir = new File(System.getProperty("data.dir"));
        if (!dataDir.exists()) throw new IllegalArgumentException("No data directory found!");
        loadProperties();
        boolean renderingAvailable = StringUtils.isNotEmpty(props.getProperty("imagemagick.convert.command")) && StringUtils.isNotEmpty(props.getProperty("imagemagick.convert.parameters")) && StringUtils.isNotEmpty(props.getProperty("imagemagick.resolution.export")) && new File(props.getProperty("imagemagick.convert.command")).exists();
        props.setProperty("rendering.available", renderingAvailable ? "true" : "false");
        String baseUrl = String.format("http://%s:%s/", props.get("poormans.jetty.host"), props.get("poormans.jetty.port"));
        props.setProperty("baseurl", baseUrl);
        props.setProperty("data.dir", dataDir.getAbsolutePath());
        System.setProperty("content.types.user.table", new File(Constants.APPLICATION_DIR, "lib/content-types.properties").getAbsolutePath());
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(PropertiesTool.getProperties(props, "log4j"));
        Logger logger = Logger.getLogger(BasicConfigurator.class);
        logger.info("*** log4j initialized!");
        try {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.scan("net.sf.poormans");
            PropertyPlaceholderConfigurer config = new PropertyPlaceholderConfigurer();
            config.setProperties(props);
            config.postProcessBeanFactory(ctx.getDefaultListableBeanFactory());
            ctx.refresh();
            context = ctx;
            logger.info("*** Spring initialized!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getDataDir() {
        return dataDir;
    }

    public Properties getProps() {
        return props;
    }

    public AbstractApplicationContext getContext() {
        return context;
    }

    private void loadProperties() {
        props = new Properties();
        try {
            InputStream commonIn = new BufferedInputStream(BasicConfigurator.class.getResourceAsStream("common.properties"));
            File propsFile = new File(dataDir, PROPERTIES_NAME);
            InputStream usersIn = new BufferedInputStream(new FileInputStream(propsFile));
            props = PropertiesTool.loadProperties(commonIn, usersIn);
            for (Object key : props.keySet()) {
                String val = (String) props.get(key);
                if (val.contains("${datapath}")) {
                    val = val.replace("${datapath}", dataDir.getAbsolutePath());
                    props.setProperty((String) key, val.replace(File.separator, "/"));
                }
            }
        } catch (Exception e) {
            props.clear();
            throw new RuntimeException("Can't read common.properties or poormans.properties!", e);
        }
    }
}
