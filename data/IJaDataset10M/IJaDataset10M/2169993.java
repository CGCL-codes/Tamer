package com.rapidweb.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.directwebremoting.WebContextFactory;
import com.rapidweb.base.BaseAction;
import com.rapidweb.generator.core.ConfigurationManager;
import com.rapidweb.generator.core.WebItem;
import com.rapidweb.generator.engine.DatabaseProvider;
import com.rapidweb.generator.engine.FileOperate;
import com.rapidweb.generator.engine.GeneratorStrategyFactory;
import com.rapidweb.generator.engine.SqlGenerator;
import com.rapidweb.generator.engine.StringUtil;
import com.rapidweb.generator.engine.WebStrategy;

/**
 * @author yxli
 * 
 */
public class CreateItemAction extends BaseAction {

    private static final String RAPIDWEB_PROPERTIES = "WEB-INF/classes/resources/rapidweb.properties";

    private static final String BLANK_PROJECT_FOLDER = "WEB-INF/classes/blankproject/";

    public String prepare(String siteDirectory, String siteBase, String siteContextRoot, String databaseDialect) {
        StringBuffer errorMessages = new StringBuffer();
        String realPath = WebContextFactory.get().getServletContext().getRealPath("/") + BLANK_PROJECT_FOLDER;
        FileOperate fileOp = new FileOperate();
        String sStrictDirectory = StringUtil.getStrictFilePath(siteDirectory);
        fileOp.delFolder(sStrictDirectory);
        fileOp.copyFolder(realPath, sStrictDirectory);
        try {
            WebStrategy strategy = getStrategy();
            strategy.getConfig().setPropertyValue("OUTPUT_FOLDER", sStrictDirectory);
            strategy.getConfig().setPropertyValue("PAGES_BASE_PATH", siteBase + siteContextRoot);
            strategy.getConfig().setPropertyValue("CONTEXT_ROOT", siteContextRoot);
            strategy.getConfig().setPropertyValue("HIBERNATE_DIALECT", databaseDialect);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errorMessages.append("Initialization failed.");
        }
        return errorMessages.toString();
    }

    public String save(String formName, List<WebItem> list) {
        StringBuffer errorMessages = new StringBuffer();
        try {
            WebStrategy strategy = getStrategy();
            strategy.addForm(formName, list);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessages.append("Save failed.");
        }
        return errorMessages.toString();
    }

    public String flush() throws Exception {
        StringBuffer errorMessages = new StringBuffer();
        WebStrategy strategy = getStrategy();
        if (!strategy.getForms().isEmpty()) {
            SqlGenerator sqlgen = new SqlGenerator(strategy);
            sqlgen.process();
            strategy.clearForms();
        } else {
            errorMessages.append("No form generated.");
        }
        return errorMessages.toString();
    }

    public List<String> findForms() throws Exception {
        WebStrategy strategy = getStrategy();
        return strategy.getForms();
    }

    public String removeForm(String formName) throws Exception {
        StringBuffer errorMessages = new StringBuffer();
        WebStrategy strategy = getStrategy();
        List<WebItem> list = strategy.removeForm(formName);
        if (list == null) {
            errorMessages.append("No form deleted.");
        }
        return errorMessages.toString();
    }

    private WebStrategy getStrategy() throws FileNotFoundException {
        InputStream fis = new FileInputStream(WebContextFactory.get().getServletContext().getRealPath("/") + RAPIDWEB_PROPERTIES);
        ConfigurationManager config = ConfigurationManager.getInstance(fis);
        DatabaseProvider.getInstance().setConfigurationManager(config);
        WebStrategy strategy = (WebStrategy) GeneratorStrategyFactory.getGeneratorStrategy(config);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strategy;
    }

    public List<WebItem> getForm(String formName) throws Exception {
        WebStrategy strategy = getStrategy();
        return strategy.findForm(formName);
    }
}
