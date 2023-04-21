package jcd2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import jcd2.model.Activity;
import jcd2.model.Cyclist;
import jcd2.model.LogEntry;
import org.apache.xerces.parsers.DOMParser;
import org.jdesktop.application.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author manfred
 */
public class OpenTask extends Task<Object, Void> {

    MainView _view = null;

    Cyclist _cyclist = null;

    JFileChooser jFC = null;

    String selectedFile = null;

    int progressCounter = 0;

    int activitiesCount = 1;

    OpenTask(MainView view, org.jdesktop.application.Application app, String file) {
        super(app);
        _view = view;
        _cyclist = _view.getCyclist();
        jFC = _view.getFileChooser();
        if (file != null) {
            selectedFile = file;
        } else {
            if (jFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = jFC.getSelectedFile().getAbsolutePath();
            }
        }
    }

    @Override
    protected Object doInBackground() {
        if (selectedFile != null) {
            try {
                setMessage("Open " + selectedFile);
                read(selectedFile);
            } catch (IOException ex) {
                selectedFile = null;
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return selectedFile;
    }

    @Override
    protected void succeeded(Object result) {
        _view.setFile(selectedFile);
        _view.updateUI();
    }

    /**
     *  setup Cyclist from an XML file
     *
     * @param  uri              location of the XML file
     * @exception  IOException  Description of Exception
     */
    void read(final String uri) throws IOException {
        FileInputStream is = new java.io.FileInputStream(uri);
        Document doc = null;
        DOMParser parser = null;
        _cyclist.reset();
        _cyclist.notifyPause();
        try {
            parser = new DOMParser();
            parser.parse(new org.xml.sax.InputSource(is));
            doc = parser.getDocument();
            readFromDOM(doc.getDocumentElement());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        } catch (org.xml.sax.SAXParseException err) {
            Exception x = err.getException();
            if (x == null) {
                err.printStackTrace();
            } else {
                x.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, err.getLineNumber() + "\n " + err.getSystemId() + "\n" + err.getMessage());
        } catch (org.xml.sax.SAXException e) {
            Exception x = e.getException();
            if (x == null) {
                e.printStackTrace();
            } else {
                x.printStackTrace();
            }
        } finally {
            _cyclist.notifyResume(false);
            _cyclist.setCurrentYear(new Date());
        }
    }

    /**
     *  Process DOM and set values for Cyclist accordingly
     *
     * @param  root  root node of DOM
     */
    private void readFromDOM(Element root) {
        NodeList list;
        setProgress(0.0f);
        list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            processSubNodes(list.item(i));
        }
    }

    private void processSubNodes(Node nd) {
        org.w3c.dom.NamedNodeMap attrMap;
        NodeList lst;
        Node node;
        String ndName;
        Locale currLocale = Locale.getDefault();
        if (nd.getNodeType() != Node.ELEMENT_NODE) {
            return;
        } else {
            ndName = nd.getNodeName();
            if (ndName.equals("locale")) {
                String country = "";
                String language = "";
                attrMap = nd.getAttributes();
                if (attrMap != null) {
                    node = attrMap.getNamedItem("country");
                    if (node != null) {
                        country = node.getNodeValue();
                    }
                    node = attrMap.getNamedItem("language");
                    if (node != null) {
                        language = node.getNodeValue();
                    }
                    if (country.equals("")) {
                        country = Locale.getDefault().getCountry();
                    }
                    if (language.equals("")) {
                        language = Locale.getDefault().getLanguage();
                    }
                }
                Locale.setDefault(new Locale(country, language));
            } else if (ndName.equals("Cyclist")) {
                attrMap = nd.getAttributes();
                if (attrMap != null) {
                    node = attrMap.getNamedItem(Cyclist.DISTANCE_UNIT);
                    if (node != null) {
                        _cyclist.setUnit(new Boolean(node.getNodeValue()));
                    }
                    node = attrMap.getNamedItem("NumberOfActivities");
                    if (node != null) {
                        activitiesCount = new Integer(node.getNodeValue());
                        setProgress(progressCounter / activitiesCount);
                    }
                }
            } else if (ndName.equals("Name")) {
                node = nd.getFirstChild();
                if (node == null) {
                    _cyclist.setName(Cyclist.INIT_NAME);
                    JOptionPane.showMessageDialog(null, "Name not found");
                } else {
                    if (node.getNodeType() != Node.TEXT_NODE) {
                        _cyclist.setName(Cyclist.INIT_NAME);
                        JOptionPane.showMessageDialog(null, "Name not found");
                    } else {
                        _cyclist.setName(node.getNodeValue());
                    }
                }
            } else if (ndName.equals("YearOfBirth")) {
                node = nd.getFirstChild();
                if (node == null) {
                    _cyclist.setYearOfBirth(Cyclist.INIT_YOB);
                    JOptionPane.showMessageDialog(null, "Year of Birth missing");
                } else {
                    _cyclist.setYearOfBirth(node.getNodeValue());
                }
            } else if (ndName.equals("Season")) {
                attrMap = nd.getAttributes();
                if (attrMap != null) {
                    node = attrMap.getNamedItem("Year");
                    _cyclist.setCurrentYear(new Integer(node.getNodeValue()));
                }
            } else if (ndName.equals("Ride")) {
                LogEntry logEntry = Activity.factory(ndName, _cyclist);
                logEntry.readFromDOM(nd);
                ++progressCounter;
                setProgress(progressCounter / activitiesCount);
            } else if ((ndName.equals("LogEntry")) || (ndName.equals("Activity"))) {
                attrMap = nd.getAttributes();
                if (attrMap != null) {
                    node = attrMap.getNamedItem("type");
                    try {
                        LogEntry act = Activity.factory(node.getNodeValue(), _cyclist);
                        act.readFromDOM(nd);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                    ++progressCounter;
                    setProgress((float) progressCounter / activitiesCount);
                }
            }
            lst = nd.getChildNodes();
            for (int i = 0; i < lst.getLength(); i++) {
                processSubNodes(lst.item(i));
            }
            Locale.setDefault(currLocale);
        }
    }
}
