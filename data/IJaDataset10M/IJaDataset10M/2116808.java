package br.com.ita.metadata.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import br.com.ita.metadata.exception.ConfigException;
import br.com.ita.metadata.factory.MetadataFactory;
import br.com.ita.metadata.util.ResourceUtils;

public class ConfigDescriptor {

    private static final String CONFIG_FILE = "sharing-config.xml";

    private MetadataFactory metadataFactory;

    private final Set<Class<?>> beanClasses = new HashSet<Class<?>>();

    private final Map<Class<?>, Map<String, String>> beanParameters = new HashMap<Class<?>, Map<String, String>>();

    private static ConfigDescriptor instance = null;

    public static ConfigDescriptor getInstance() throws ConfigException {
        if (instance == null) {
            instance = new ConfigDescriptor();
            instance.loadConfigurations();
        }
        return instance;
    }

    private ConfigDescriptor() {
    }

    private void loadConfigurations() throws ConfigException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            InputStream stream = ResourceUtils.getResourceAsStream(CONFIG_FILE);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(stream, new ConfigXMLHandler(this));
            stream.close();
        } catch (ParserConfigurationException e) {
            throw new ConfigException("Erro durante a leitura do arquivo: " + CONFIG_FILE, e);
        } catch (SAXException e) {
            throw new ConfigException("Erro durante a leitura do arquivo: " + CONFIG_FILE, e);
        } catch (IOException e) {
            throw new ConfigException("Erro durante a leitura do arquivo: " + CONFIG_FILE, e);
        }
    }

    public MetadataFactory getMetadataFactory() {
        return metadataFactory;
    }

    void setMetadataFactory(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    public void addBeanClass(Class<?> beanClass) {
        beanClasses.add(beanClass);
    }

    public Set<Class<?>> getBeanClasses() {
        return beanClasses;
    }

    public void addBeanParameter(Class<?> beanClass, String propertyName, String propertyValue) {
        Map<String, String> attributes = beanParameters.get(beanClass);
        if (attributes == null) {
            attributes = new HashMap<String, String>();
            beanParameters.put(beanClass, attributes);
        }
        attributes.put(propertyName, propertyValue);
    }

    public Map<String, String> getBeanParameter(Class<?> beanClass) {
        return beanParameters.get(beanClass);
    }
}
