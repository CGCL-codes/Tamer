package org.openscience.cdk.io.listener;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Properties;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.ReaderEvent;
import org.openscience.cdk.io.setting.IOSetting;

/**
 * Answers the questions by looking up the values in a Properties
 * object. The question names match the property field names.
 * If no answer is found in the Property object, or if the value
 * is invalid, then the default is taken.
 *
 * <p>For the GaussianInputWriter the properties file might look like:
 * <pre>
 * Basis=6-31g
 * Method=b3lyp
 * Command=geometry optimization
 * </pre>
 *
 * @cdk.module io
 * @cdk.githash
 *
 * @author Egon Willighagen <egonw@sci.kun.nl>
 */
public class PropertiesListener implements IReaderListener, IWriterListener {

    private Properties props;

    private PrintWriter out;

    public PropertiesListener(Properties props) {
        this.props = props;
        this.setOutputWriter(new OutputStreamWriter(System.out));
    }

    /**
     * Overwrites the default writer to which the output is directed.
     */
    public void setOutputWriter(Writer writer) {
        if (writer instanceof PrintWriter) {
            this.out = (PrintWriter) writer;
        } else if (writer == null) {
            this.out = null;
        } else {
            this.out = new PrintWriter(writer);
        }
    }

    public void frameRead(ReaderEvent event) {
    }

    /**
     * Processes the IOSettings by listing the question, giving the options
     * and asking the user to provide their choice.
     *
     * <p>Note: if the input reader is <code>null</code>, then the method
     * does not wait for an answer, and takes the default.
     */
    public void processIOSettingQuestion(IOSetting setting) {
        String questionName = setting.getName();
        if (props != null) {
            String propValue = props.getProperty(questionName, setting.getSetting());
            try {
                setting.setSetting(propValue);
            } catch (CDKException exception) {
                String message = "Submitted Value (" + propValue + ") is not valid!";
                out.println(message);
            }
        }
    }
}
