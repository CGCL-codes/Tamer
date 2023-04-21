package DE.FhG.IGD.semoa.bin.starter;

import java.io.File;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;

/**
 * This component allows users to select a {@link JavaClassPath}.
 * This can be accomplished by either directly using the component's text
 * input field or by means of the provided file browser.
 *
 * @author Matthias Pressfreund
 * @version "$Id$"
 */
public class JavaClassPathChooser extends InputFileChooser {

    /**
     * Create a <code>JavaClassPathChooser</code>.
     */
    public JavaClassPathChooser() {
        super();
        TitledBorder border;
        border = BorderFactory.createTitledBorder("Java Class Path");
        border.setTitleJustification(TitledBorder.CENTER);
        setBorder(border);
        setConverter(new Converter() {

            public Object convert(Object o) throws ConverterException {
                JavaClassPath cp;
                String str;
                try {
                    str = ConverterPanel.DEFAULT_CONVERTER_.convert(o).toString();
                    cp = str.length() > 0 ? JavaClassPath.create(str) : null;
                } catch (Exception e) {
                    throw new ConverterException("Invalid Java Class Path", "The latest changes on the" + "\nclasspath were invalid");
                }
                return cp;
            }
        });
        setSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    /**
     * Concatenates the path element chosen by means of the file
     * browser to the textfield content using the system path separator.
     */
    protected void processChoice(Object o) throws ConverterException {
        super.processChoice(o != null ? textField_.getText() + File.pathSeparator + o.toString() : null);
    }

    protected String getTextFieldHelp() {
        return ("<html>" + "<center>" + "<p><b>The Java Classpath Input Field</b></p>" + "</center>" + "<p>Enter your custom Java classpath. Multiple<br>" + "path elements can be appended by means<br>" + "of the appropriate path separator char:<br>" + "<i>colon</i> (:) for UNIX, or <i>semicolon</i> (;)<br>" + "for Windows based systems.</p>" + super.getTextFieldHelp() + "</html>");
    }

    protected String getBrowseButtonHelp() {
        return ("<html>" + "<center>" + "<p><b>The Java Classpath Browser</b></p>" + "</center>" + "<p>Browse the file system for Java classpath elements.<br>" + "Selected elements will be appended to the current<br>" + "path separated by the appropriate path separator char.</p>" + "</html>");
    }
}
