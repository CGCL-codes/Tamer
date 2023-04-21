package net.sourceforge.jsjavacomm.examples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import net.sourceforge.jsjavacomm.applet.JSApplet;
import net.sourceforge.jsjavacomm.dom.JSDOMImplementation;
import net.sourceforge.jsjavacomm.dom.views.JSWindow;
import net.sourceforge.jsjavacomm.domImpl.JSDOMImplementationImpl;
import org.w3c.dom.*;

/**
 * An example of how to modify the DOM of the document in the browser
 * from an applet using <em>JSJavaComm</em>.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 3 March 2009
 */
public class DOMModification extends JSApplet implements ActionListener {

    /**
	 * Required in case this class is serialized.
	 */
    private static final long serialVersionUID = 1L;

    /** 
	 * The text pane for this applet.
	 */
    private JEditorPane jep;

    /**
	 * The document which is currently being displayed by the browser. 
	 */
    private Document doc;

    /**
	 * Initialises the Java components.
	 */
    @Override
    public void init() {
        super.init();
        JButton jbut = new JButton("Add paragraph");
        jbut.addActionListener(this);
        jep = new JEditorPane("text/plain", "This paragraph of text can be added to the document by pressing the 'Add paragraph' button.");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(jep);
        getContentPane().add(jbut);
    }

    /**
	 * Gets the document which is currently being displayed by the browser.
	 */
    @Override
    public void start() {
        JSDOMImplementation domImpl = new JSDOMImplementationImpl();
        JSWindow window = domImpl.getWindow(this);
        doc = window.getDocument();
    }

    /**
	 * Called when the user presses the button. Creates a new Text node,
	 * wraps it in a p element and adds it to the document as a child
	 * of the element who's id is "container".
	 */
    public void actionPerformed(ActionEvent e) {
        Text newText = doc.createTextNode(jep.getText());
        Element newParagraph = doc.createElement("p");
        newParagraph.appendChild(newText);
        Element container = doc.getElementById("container");
        container.appendChild(newParagraph);
    }
}
