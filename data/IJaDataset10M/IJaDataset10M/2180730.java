package com.calipso.reportgenerator.common.jasperdefinition;

import com.calipso.reportgenerator.common.IJasperDefinition;
import com.calipso.reportgenerator.common.LanguageTraslator;
import com.calipso.reportgenerator.common.exception.InfoException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 */
public class ExternalJasperDefinition implements IJasperDefinition {

    private JasperDesign jasperDesign;

    public JasperDesign getJasperDefinition(boolean isLandscape) throws JRException {
        return jasperDesign;
    }

    public ExternalJasperDefinition(String externalDefinitionFile) throws InfoException {
        try {
            System.out.println("LayoutDesign_FileName:" + externalDefinitionFile);
            jasperDesign = JRXmlLoader.load(externalDefinitionFile);
        } catch (JRException e) {
            throw new InfoException(LanguageTraslator.traslate("257"));
        }
    }
}
