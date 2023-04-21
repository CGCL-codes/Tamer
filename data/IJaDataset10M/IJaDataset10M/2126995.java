package org.form4G.gui.component.text;

import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ObjectView;
import org.form4G.script.DHTMLDocument;

public class MSEditorKit extends HTMLEditorKit {

    private static final long serialVersionUID = 5613567618806497616L;

    private boolean userScript;

    public MSEditorKit(boolean userScript) {
        super();
        this.userScript = userScript;
        this.setAutoFormSubmission(false);
    }

    public ViewFactory getViewFactory() {
        return new MyViewFactory();
    }

    public Document createDefaultDocument() {
        Document rr = super.createDefaultDocument();
        if ((rr instanceof HTMLDocument) && (userScript)) rr = new DHTMLDocument(getStyleSheet());
        return rr;
    }

    public class MyViewFactory extends HTMLFactory {

        public MyViewFactory() {
            super();
        }

        public View create(Element elem) {
            View rr = super.create(elem);
            if (rr instanceof FormView) rr = new MSFormView(elem); else if ((rr instanceof ObjectView) || elem.getName().equalsIgnoreCase("applet")) rr = new MSObjectView(elem); else if (elem.getName().equalsIgnoreCase("iframe")) rr = new IFrameView(elem); else if (elem.getName().equalsIgnoreCase("frame")) rr = new MSFrameView(elem);
            if (elem.getDocument() instanceof DHTMLDocument) {
                ((DHTMLDocument) elem.getDocument()).nitializerRootView(rr);
            }
            return rr;
        }
    }
}
