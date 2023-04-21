package org.tridentproject.repository.vocab;

import java.io.File;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpStatus;

public class VocabClient {

    private static Logger log = Logger.getLogger(VocabClient.class);

    private String strVocabURL = null;

    private Configuration vocabConfig = null;

    public VocabClient() {
    }

    public VocabClient(URL configURL) {
        configure(configURL);
    }

    private void configure(URL configURL) {
        String log4j = System.getProperty("log4j.configuration");
        if (log4j != null) {
            PropertyConfigurator.configure(log4j);
        }
        log.info("VocabServlet:  init: begin");
        try {
            ConfigurationFactory factory = new ConfigurationFactory();
            factory.setConfigurationURL(configURL);
            Configuration config = factory.getConfiguration();
            vocabConfig = config.subset("trident.vocab");
            strVocabURL = vocabConfig.getString("queue.location");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void setConfiguration(Configuration config) {
        vocabConfig = config;
        try {
            strVocabURL = vocabConfig.getString("queue.location");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String processRequest(String strAction, String strItemIds, String strPriority) {
        String response = null;
        StringBuffer sbURL = new StringBuffer(strVocabURL + "?action=" + strAction);
        if (strItemIds != null) sbURL.append("&itemids=" + strItemIds);
        if (strPriority != null) sbURL.append("&priority=" + strPriority);
        String strURL = sbURL.toString();
        HttpClient client = new HttpClient();
        try {
            GetMethod method = new GetMethod(strURL);
            int statusCode = client.executeMethod(method);
            response = method.getResponseBodyAsString();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        String strAction = System.getProperty("vocabAction");
        String strItemIds = System.getProperty("vocabItemIds");
        String strPriority = System.getProperty("vocabPriority");
        if (strAction == null) {
            System.err.println("vocabAction is a required system property");
            System.exit(1);
        }
        String strConfigURL = System.getProperty("vocabConfig");
        if (strConfigURL == null) {
            System.err.println("config property must be set");
            System.exit(1);
        }
        try {
            File fConfig = new File(strConfigURL);
            VocabClient client = new VocabClient(fConfig.toURL());
            String output = client.processRequest(strAction, strItemIds, strPriority);
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
