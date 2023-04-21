package DE.FhG.IGD.semoa.bin.starter;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;

/**
 * This component allows users to select {@link SemoaScriptParameters}.
 * This can be accomplished by either directly using the component's text
 * input field or by means of the provided file browser.
 *
 * @author Matthias Pressfreund
 * @version "$Id$"
 */
public class SemoaScriptParametersChooser extends InputFileChooser {

    /**
     * Create a <code>SemoaScriptParametersChooser</code>.
     */
    public SemoaScriptParametersChooser() {
        super();
        TitledBorder border;
        border = BorderFactory.createTitledBorder("Script Parameters");
        border.setTitleJustification(TitledBorder.CENTER);
        setBorder(border);
        setConverter(new Converter() {

            public Object convert(Object o) throws ConverterException {
                SemoaScriptParameters sparams;
                String str;
                try {
                    str = ConverterPanel.DEFAULT_CONVERTER_.convert(o).toString();
                    sparams = str.length() > 0 ? SemoaScriptParameters.create(str) : null;
                } catch (IllegalArgumentException e) {
                    throw new ConverterException("Invalid Script Parameters", "The parameter input is invalid");
                }
                return sparams;
            }
        });
        setSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    /**
     * Concatenates the object chosen by means of the file
     * browser to the textfield content.
     */
    protected void processChoice(Object o) throws ConverterException {
        super.processChoice(o != null ? textField_.getText() + SemoaScriptParameters.DELIMITER + o.toString() : null);
    }

    protected String getTextFieldHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Script Parameters Input Field</b></p>" + "</center>" + "<p>Enter your custom <i>SeMoA</i> script parameters.<br>" + "Input of any kind (of course also files and<br>" + "directories) will be accepted. Multiple<br>" + "parameters must be separated by commas.</p>" + super.getTextFieldHelp() + "</html>");
    }

    protected String getBrowseButtonHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Script Parameters Browser</b></p>" + "</center>" + "<p>Browse the file system for files and directories to<br>" + "be used as <i>SeMoA</i> Script parameters. Selected<br>" + "elements will be appended to the current input,<br>" + "separated by a comma.</p>" + "</html>");
    }
}
