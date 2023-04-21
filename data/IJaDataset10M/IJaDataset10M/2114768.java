package verinec.validation.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import verinec.VerinecException;
import verinec.gui.VerinecStudio;
import verinec.validation.gui.menu.actions.ValidateAction;

/**
 * The validator editor. Files of type <tt>TYPE_LOGFILE</tt> are not editable, <tt>TYPE_SCHEMA</tt> are. When a log file is openend, a schema linking pane (@see verinec.validation.gui.SchemaLinkingPane) is added to the tab.  
 * 
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class ValidatorEditorPanel extends JTextArea {

    private Logger jlogger;

    private File file;

    private int type;

    private VerinecStudio studio;

    private ValidationGui gui;

    /**
	 * Schema document type
	 */
    public static int TYPE_SCHEMA = 0;

    /**
	 * Logfile doucment type 
	 */
    public static int TYPE_LOGFILE = 1;

    /** Creates a new Editor Panel
	 * @param gui the validator gui 
	 * @param studio the verinec studio 
	 * @param file the file to be opened, can be null for a new, empty editor
	 * @param type document type (@see TYPE_SCHEMA) and (@see TYPE_LOGFILE)
	 * @throws VerinecException if the file could not be loaded (if file is not null)
	 */
    public ValidatorEditorPanel(ValidationGui gui, VerinecStudio studio, File file, int type) throws VerinecException {
        super();
        jlogger = Logger.getLogger(getClass().getName());
        this.file = file;
        this.type = type;
        this.studio = studio;
        this.gui = gui;
        if (file != null) {
            loadFile();
        }
        if (type == TYPE_LOGFILE) {
            setEditable(false);
        }
    }

    /** Set's up the gui. If the document is a logfile, a SchemaLinkingPane is added the bottom component. 
	 * @param studio the verinec studio
	 * @throws VerinecException if the doucment can't be parsed
	 */
    public void setupGUI(VerinecStudio studio) throws VerinecException {
        if (type == TYPE_LOGFILE) {
            SchemaLinkingPane panel = new SchemaLinkingPane(getElement(), this);
            JScrollPane pane = new JScrollPane(panel);
            studio.setBottomComponent(pane);
            ValidateAction.getInstance(gui).setEnabled(true);
        } else if (type == TYPE_SCHEMA) {
            studio.setBottomComponent(null);
            ValidateAction.getInstance(gui).setEnabled(false);
        }
    }

    /** parses the file edited with this panel and returns the root element
	 * @return the root element of the file edited by this panel
	 * @throws VerinecException if the file can't be parsed
	 */
    public Element getElement() throws VerinecException {
        SAXBuilder builder = new SAXBuilder(false);
        Element element = null;
        try {
            element = builder.build(file).getRootElement();
        } catch (IOException e) {
            jlogger.throwing(getClass().getName(), "init", e);
            JOptionPane.showMessageDialog(this, "error reading from file");
            throw new VerinecException("Error while building jdom tree", e);
        } catch (JDOMException e) {
            jlogger.throwing(getClass().getName(), "init", e);
            JOptionPane.showMessageDialog(this, "could not open logfile. Logfile contains invalid XML");
            throw new VerinecException("Error while building jdom tree", e);
        }
        return element;
    }

    /** returns if the content has been changed
	 * @return true if the content has changed since the last load or save, false otherwise
	 */
    public boolean hasChanged() {
        return false;
    }

    /**
	 * saves the document to the related file
	 */
    public void save() {
    }

    /**
	 * loads a file and sets this editor panels text to the files content
	 * @throws VerinecException if the file could not be loaded
	 */
    public void loadFile() throws VerinecException {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(file));
            String text = new String();
            String temp = in.readLine();
            while (temp != null) {
                text = text.concat(temp + "\n");
                temp = in.readLine();
            }
            setText(text);
            in.close();
            setupGUI(studio);
        } catch (Exception e) {
            throw new VerinecException("file could not be loaded", e);
        }
    }

    /** saves the content of the editor in a given file
	 * @param file the file to save this document
	 * @param type the file's type
	 */
    public void saveAs(File file, int type) {
        this.file = file;
        this.type = type;
        save();
    }

    /** saves an element in the file edited by this editor
	 * @param element element to be saved
	 * @throws FileNotFoundException if the file can't be found
	 * @throws IOException if the output can't be preformed
	 */
    public void save(Element element) throws FileNotFoundException, IOException {
        XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
        o.output(element.getDocument(), new FileOutputStream(file));
    }

    /** gets the file edited in this editor
	 * @return the file edited in this editor
	 */
    public File getFile() {
        return file;
    }
}
