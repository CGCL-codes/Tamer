package com.hp.hpl.guess.ui;

import java.awt.Rectangle;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.python.core.PyInstance;
import org.python.core.PyJavaInstance;
import org.python.util.PythonInterpreter;
import com.hp.hpl.guess.Edge;
import com.hp.hpl.guess.Node;

public class LabNotebook extends JFrame {

    private static final long serialVersionUID = -832422695400260099L;

    JTextPane pane = null;

    HTMLDocument doc = null;

    private PythonInterpreter jython = null;

    private static LabNotebook singleton = null;

    private JScrollPane jsp = null;

    public static LabNotebook createNotebook(PythonInterpreter j) {
        if (singleton == null) {
            singleton = new LabNotebook(j);
        }
        return (singleton);
    }

    public static LabNotebook getNotebook() {
        return (singleton);
    }

    public Element getElementByTag(Element parent, HTML.Tag tag) {
        if (parent == null || tag == null) return null;
        for (int k = 0; k < parent.getElementCount(); k++) {
            Element child = parent.getElement(k);
            if (child.getAttributes().getAttribute(StyleConstants.NameAttribute).equals(tag)) return child;
            Element e = getElementByTag(child, tag);
            if (e != null) return e;
        }
        return null;
    }

    private LabNotebook(PythonInterpreter jython) {
        this.jython = jython;
        pane = new JTextPane();
        pane.setEditable(false);
        pane.setEditorKit(new HTMLEditorKit());
        pane.setText("<HTML><BODY><H1>Lab Notebook</H1><TABLE><TR ID=1><TD>&nbsp;</TD><TD>&nbsp;</TD></TR><DIV></TABLE></BODY></HTML>");
        doc = (HTMLDocument) pane.getStyledDocument();
        StyleSheet styleSheet = doc.getStyleSheet();
        styleSheet.addRule("body {font-family: Dialog}");
        SimpleLinkListener sll = new SimpleLinkListener(pane);
        pane.addHyperlinkListener(sll);
        jsp = new JScrollPane(pane);
        setContentPane(jsp);
        setSize(400, 500);
        setVisible(true);
    }

    private int counter = 1;

    private int lineNum = 0;

    boolean color = false;

    public void addText(String command, Object result) {
        try {
            Element body = doc.getElement("" + counter);
            counter++;
            String cl = "#FFFFFF";
            if (!color) {
                cl = "#CCFFFF";
            }
            color = !color;
            lineNum++;
            StringBuffer toInsert = new StringBuffer("<TR ID=" + counter + " BGCOLOR=" + cl + "><TD VALIGN=TOP><B>" + lineNum + "</B></TD><TD VALIGN=TOP><PRE>" + command + "</PRE></TD></TR>\n");
            String res = null;
            if (result instanceof PyJavaInstance) {
                Object o = (Object) ((PyInstance) result).__tojava__(Object.class);
                if (o instanceof Node) {
                    String name = ((Node) o).getName();
                    res = "<A HREF=\"http://" + name + "\">" + name + "</A>";
                } else if (o instanceof Edge) {
                    String name = ((Edge) o).toString();
                    res = "<A HREF=\"http://" + name + "\">" + name + "</A>";
                }
            }
            if (res != null) {
                counter++;
                toInsert.append("<TR ID=" + counter + "><TD>&nbsp;<TD BGCOLOR=" + cl + ">" + res + "</TD></TR>\n");
            }
            doc.insertAfterEnd(body, toInsert.toString());
        } catch (Exception ex) {
            ExceptionWindow.getExceptionWindow(ex);
        }
        jsp.scrollRectToVisible(bottom);
    }

    private static final Rectangle bottom = new Rectangle(0, Integer.MAX_VALUE, 0, 0);

    public void addImage(String url, int width, int height) {
        try {
            Element body = doc.getElement("" + counter);
            double scale = Math.min((double) getHeight() / (double) height, (double) getWidth() / (double) width) * .8;
            height = (int) (height * scale);
            width = (int) (width * scale);
            counter++;
            url = (new java.io.File(url)).toURI().toURL().toString();
            StringBuffer toInsert = new StringBuffer("<TR ID=" + counter + "><TD VALIGN=TOP>&nbsp;" + "</TD><TD VALIGN=TOP>" + "<IMG SRC=\"" + url + "\" HEIGHT=" + height + " WIDTH=" + width + "></TD></TR>\n");
            doc.insertAfterEnd(body, toInsert.toString());
        } catch (Exception ex) {
            ExceptionWindow.getExceptionWindow(ex);
        }
        jsp.scrollRectToVisible(bottom);
    }

    class SimpleLinkListener implements HyperlinkListener {

        public SimpleLinkListener(JEditorPane jep) {
        }

        public void hyperlinkUpdate(HyperlinkEvent he) {
            HyperlinkEvent.EventType type = he.getEventType();
            java.net.URL testURL = he.getURL();
            if (testURL == null) {
                return;
            }
            String host = testURL.getHost();
            if (type == HyperlinkEvent.EventType.ENTERED) {
                System.out.println("over: " + host);
            } else if (type == HyperlinkEvent.EventType.EXITED) {
            } else if (type == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    jython.exec("center(" + host + ")");
                } catch (Exception e) {
                }
            }
        }
    }
}
