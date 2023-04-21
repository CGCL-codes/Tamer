package shellkk.qiq.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFactory {

    private Log log = LogFactory.getLog(SpringFactory.class);

    protected String configFile;

    public ApplicationContext createContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext(configFile);
        log.info("spring context create success!");
        return context;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
