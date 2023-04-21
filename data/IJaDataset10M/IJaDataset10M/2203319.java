package au.gov.naa.digipres.xena.plugin.multipage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.view.XenaView;
import au.gov.naa.digipres.xena.kernel.view.XmlDivertor;
import au.gov.naa.digipres.xena.util.ChunkedView;
import au.gov.naa.digipres.xena.util.XmlContentHandlerSplitter;

/**
 * Display a Xena multipage instance page by page with First, Prev, Next and
 * Last buttons.
 *
 */
public class MultiPageView extends ChunkedView {

    BorderLayout borderLayout1 = new BorderLayout();

    Component currentPage;

    private JPanel displayPanel = new JPanel();

    private BorderLayout borderLayout2 = new BorderLayout();

    public MultiPageView() {
        try {
            jbInit2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ContentHandler getContentHandler() throws XenaException {
        final XenaView oldview = getSubView(displayPanel);
        ContentHandler ch = new XmlDivertor(this, displayPanel) {

            StringBuffer buf = null;

            int p = 0;

            @Override
            public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
                if (qName.equals("multipage:page")) {
                    if (p == currentChunk) {
                        setDivertNextTag();
                    }
                    p++;
                }
                super.startElement(namespaceURI, localName, qName, atts);
            }

            @Override
            public void endDocument() {
                setTotalChunks(p);
                XenaView newview = getSubView(displayPanel);
                if (oldview != null) {
                    copyAttributes(oldview, newview);
                }
            }
        };
        if (getTmpFile() != null) {
            return ch;
        }
        XmlContentHandlerSplitter splitter = new XmlContentHandlerSplitter();
        splitter.addContentHandler(ch);
        splitter.addContentHandler(getTmpFileContentHandler());
        return splitter;
    }

    @Override
    public String getViewName() {
        return "Multi-Page View";
    }

    @Override
    public boolean canShowTag(String tag) throws XenaException {
        return tag.equals(viewManager.getPluginManager().getTypeManager().lookupXenaFileType(XenaMultiPageFileType.class).getTag());
    }

    @Override
    public void initListeners() {
    }

    void jbInit2() throws Exception {
        setLayout(borderLayout1);
        pageLabel.setText(" Page: ");
        displayPanel.setLayout(borderLayout2);
        this.add(displayPanel, BorderLayout.CENTER);
    }

    /**
	 * We can for example retain the same zoom factor between images using this
	 * function. Functions that start with "getXenaExternal" are treated
	 * specially and we copy these special attributes from the old to the new
	 * view.
	 * @param oldv
	 * @param newv
	 */
    void copyAttributes(XenaView oldv, XenaView newv) {
        if (oldv != null) {
            Method[] methods = oldv.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.startsWith("getXenaExternal")) {
                    String rest = name.substring("getXenaExternal".length());
                    String setterName = "setXenaExternal" + rest;
                    try {
                        Method setter = newv.getClass().getMethod(setterName, new Class[] { method.getReturnType() });
                        try {
                            Object res = method.invoke(oldv, new Class[] {});
                            setter.invoke(newv, new Object[] { res });
                        } catch (Exception x) {
                            JOptionPane.showMessageDialog(this, x);
                        }
                    } catch (SecurityException x) {
                        JOptionPane.showMessageDialog(this, x);
                    } catch (NoSuchMethodException x) {
                    }
                }
            }
            Iterator it = newv.getSubViews().iterator();
            while (it.hasNext()) {
                XenaView view = (XenaView) it.next();
                copyAttributes(oldv, view);
            }
            it = oldv.getSubViews().iterator();
            while (it.hasNext()) {
                XenaView view = (XenaView) it.next();
                copyAttributes(view, newv);
            }
        }
    }

    @Override
    public boolean displayChunkPanel() {
        return true;
    }
}
