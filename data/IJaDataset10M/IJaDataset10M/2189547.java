package org.formaria.data.swing;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import org.formaria.debug.DebugLogger;
import org.formaria.swing.Edit;
import org.formaria.aria.Project;
import org.formaria.aria.data.TextBinding;

/**
 * A data binding for date fields
 * <p> Copyright (c) Formaria Ltd., 2001-2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * @author luano
 */
public class DoubleBinding extends TextBinding {

    private boolean useObject;

    /**
   * Construct a new data binding
   */
    public DoubleBinding() {
        super();
        useObject = false;
    }

    /**
   * Setup and configure the binding instance. The binding is configured via the
   * XML setup registered for the particular binding type and then, subsequently
   * by attibitional attributes of the binding instance specified in the page
   * declaration, for the individual binding instance. The binding may also
   * obtain configuration or reference information from the component and the 
   * project.
   * @param project the owning project
   * @param c the component being bound
   * @param bindingConfig the XML element which contains the binding configuration
   * @param instanceConfig the XML element which contains the setup attributes of the binding instance
   */
    public void setup(Project project, Object c, Hashtable bindingConfig, Hashtable instanceConfig) {
        super.setup(project, c, bindingConfig, instanceConfig);
        useObject = "true".equals((String) instanceConfig.get("useObject"));
    }

    /**
   * Updates the data model with the value retrieved from the TextComponent.
   */
    public void set() {
        try {
            Edit edit = (Edit) component;
            JFormattedTextField.AbstractFormatter format = edit.getFormatter();
            if (format == null) format = new NumberFormatter(DecimalFormat.getNumberInstance(Locale.getDefault()));
            Double d = (Double) format.stringToValue(edit.getText());
            setModelValue(d);
        } catch (ParseException pe) {
            DebugLogger.log("Unable to parse double value");
        }
    }
}
