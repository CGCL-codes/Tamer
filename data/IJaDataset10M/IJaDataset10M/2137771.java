package org.formaria.pojo;

import java.awt.Container;
import javax.swing.JComponent;
import org.formaria.xml.XmlElement;
import org.formaria.swing.Page;
import org.formaria.aria.Project;
import org.formaria.aria.validation.Validator;

/**
 *  A validtor for validation of pojo field values
 * <p>Copyright (c) Formaria Ltd., 2008</p>
 * <p>License: see license.txt</p>
 * @author luano
 */
public class PojoFieldValidator extends PojoValidator {

    protected boolean doSave;

    private Page page;

    /**
   * Create a new POJO validator
   * @param project the owner project
   */
    public PojoFieldValidator(Project project) {
        super(project);
    }

    public void validate(Object c, boolean forceMandatory) throws Exception {
        if (doSave) doSave((JComponent) c);
        getPojo();
        formattedMessage = "";
        errorLevel = validateProperties((JComponent) c, forceMandatory);
        if (errorLevel > Validator.LEVEL_IGNORE) throw new Exception(formattedMessage);
    }

    protected void doSave(Object comp) {
        Container cont = (Container) comp;
        if (page == null) {
            while (!(cont instanceof Page)) cont = cont.getParent();
            page = (Page) cont;
        }
        if (page != null) page.getBinding(comp).set();
    }

    /**
   * Set the validation parameters
   * @param ruleConfig the validator parameters as declared in the validations rules file
   * @param instanceConfig the validator instance parameters as declared in the page file
   */
    public void setup(XmlElement ruleConfig, XmlElement instanceConfig) {
        super.setup(ruleConfig, instanceConfig);
        try {
            doSave = "true".equals(instanceConfig.getAttribute("doSave"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
