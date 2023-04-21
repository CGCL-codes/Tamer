package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Rob Harrop
 */
public class JasperReportsHtmlViewTests extends AbstractJasperReportsViewTests {

    protected AbstractJasperReportsView getViewImplementation() {
        return new JasperReportsHtmlView();
    }

    protected String getDesiredContentType() {
        return "text/html";
    }

    public void testConfigureExporterParametersWithPropertiesFile() {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        BeanDefinitionReader reader = new PropertiesBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions(new ClassPathResource("view.properties", getClass()));
        AbstractJasperReportsView view = (AbstractJasperReportsView) applicationContext.getBean("report");
        view.convertExporterParameters();
        String encoding = (String) view.getConvertedExporterParameters().get(JRHtmlExporterParameter.CHARACTER_ENCODING);
        assertEquals("UTF-8", encoding);
    }
}
