package org.sinaxe.components;

import org.sinaxe.*;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.Component;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import org.sinaxe.eventdom.DOMChangeEventDocumentFactory;

public class SinaxeFile implements SinaxeComponent, SinaxeDataTypes {

    private SinaxeRuntime runtime;

    private boolean create = false;

    private boolean noerror = false;

    private boolean noempty = false;

    private JFileChooser fileChooser = new JFileChooser();

    private DocumentFactory factory = new DOMChangeEventDocumentFactory();

    private SAXReader reader = new SAXReader(factory);

    private File file;

    private Component parentComponent = null;

    private Element newTemplate = null;

    private SinaxeVariable nameVariable;

    private SinaxeVariable directoryVariable;

    private SinaxeVariable documentVariable;

    private SinaxeQuery filenameQuery;

    private SinaxeQuery relativeQuery;

    private SinaxeInPort loadInPort;

    private SinaxeInPort reloadInPort;

    private SinaxeInPort newInPort;

    private SinaxeInPort saveInPort;

    private SinaxeInPort saveasInPort;

    private SinaxeInPort closeInPort;

    private SinaxeOutPort loadedOutPort;

    public SinaxeFile() {
    }

    protected File askFile(boolean open) {
        int retVal;
        if (open) retVal = fileChooser.showOpenDialog(parentComponent); else retVal = fileChooser.showSaveDialog(parentComponent);
        if (retVal == JFileChooser.APPROVE_OPTION) return fileChooser.getSelectedFile(); else return null;
    }

    protected void saveas() {
        File file = askFile(false);
        int retVal;
        if (file.exists()) {
            retVal = JOptionPane.showConfirmDialog(parentComponent, "Overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (retVal != JOptionPane.YES_OPTION) return;
        } else {
            try {
                file.createNewFile();
            } catch (Exception e) {
                retVal = JOptionPane.showConfirmDialog(parentComponent, "Create Failed.  Retry?", "Retry?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (retVal == JOptionPane.YES_OPTION) saveas();
                return;
            }
        }
        nameVariable.setValue(file.toString());
        directoryVariable.setValue(file.getAbsoluteFile().getParent());
        save();
    }

    protected void save() {
        File file = null;
        if (filenameQuery != null) {
            String filename = filenameQuery.getString(null);
            if (relativeQuery != null) {
                File relative = new File(relativeQuery.getString(null));
                if (!relative.isDirectory()) relative = relative.getAbsoluteFile().getParentFile();
                try {
                    filename = relative.getCanonicalPath() + File.separatorChar + filename;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!filename.equals("")) nameVariable.setValue(filename);
        }
        directoryVariable.setValue(null);
        if (nameVariable.getValue() == null) file = askFile(false); else file = new File((String) nameVariable.getValue());
        if (file == null) return;
        directoryVariable.setValue(file.getAbsoluteFile().getParent());
        try {
            if (documentVariable.getValue() == null || ((Document) documentVariable.getValue()).getRootElement() == null) {
                file.delete();
                if (!noempty) file.createNewFile();
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                XMLWriter writer;
                OutputFormat formater = OutputFormat.createCompactFormat();
                formater.setTrimText(false);
                writer = new XMLWriter(fileOutputStream, formater);
                writer.write((Document) documentVariable.getValue());
            }
            nameVariable.setValue(file.getCanonicalPath());
        } catch (Exception e) {
            runtime.issueWarning("Failed to save file '" + file + "'!!!", e);
            int retVal = JOptionPane.showConfirmDialog(parentComponent, "Save Failed.  Retry?", "Retry?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (retVal == JOptionPane.YES_OPTION) saveas();
        }
    }

    protected void reload() {
        if (file != null) {
            try {
                if (file.length() > 0) documentVariable.setValue(reader.read(file)); else documentVariable.setValue(factory.createDocument());
                nameVariable.setValue(file.toString());
                directoryVariable.setValue(file.getAbsoluteFile().getParent());
                loadedOutPort.send(documentVariable.getValue());
                return;
            } catch (Exception e) {
                if (!noerror) JOptionPane.showMessageDialog(parentComponent, "Error reading '" + file + "'!");
                runtime.issueWarning("Error reading file'" + file + "'!", e);
            }
        }
        file = null;
        documentVariable.setValue(null);
        nameVariable.setValue(null);
        directoryVariable.setValue(null);
        loadedOutPort.send(null);
    }

    protected void load(Object context) {
        file = null;
        if (filenameQuery != null) {
            String filename = filenameQuery.getString(context);
            if (filename != null) {
                file = new File(filename);
                if (relativeQuery != null && !file.isAbsolute()) {
                    String relativeName = relativeQuery.getString(context);
                    if (relativeName != null) {
                        File relative = new File(relativeName);
                        if (!relative.isDirectory()) {
                            relativeName = relative.getAbsolutePath();
                            relativeName = relativeName.substring(0, relativeName.lastIndexOf(File.separator));
                        }
                        file = new File(relativeName + File.separator + filename);
                    }
                }
            }
        } else file = askFile(true);
        if (file == null) return;
        if (!file.exists() && create) {
            boolean created = false;
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                created = false;
            }
            if (created) {
                nameVariable.setValue(file.toString());
                directoryVariable.setValue(file.getAbsoluteFile().getParent());
                documentVariable.setValue(factory.createDocument());
                loadedOutPort.send(documentVariable.getValue());
            } else {
                if (!noerror) JOptionPane.showMessageDialog(parentComponent, "Failed to create file '" + file + "'!");
                return;
            }
        }
        if (!file.exists()) {
            if (!noerror) JOptionPane.showMessageDialog(parentComponent, "File '" + file + "' does not exist!");
        } else {
            if (!file.isFile()) {
                if (!noerror) JOptionPane.showMessageDialog(parentComponent, "'" + file + "' is not a regular file!");
            } else {
                reload();
                return;
            }
        }
        file = null;
        documentVariable.setValue(null);
        nameVariable.setValue(null);
        directoryVariable.setValue(null);
        loadedOutPort.send(null);
    }

    protected void createNew() {
        Document newDoc = factory.createDocument();
        if (newTemplate != null) {
            Element copy = newTemplate.createCopy();
            newDoc.setRootElement(copy);
        }
        documentVariable.setValue(newDoc);
        nameVariable.setValue(null);
        directoryVariable.setValue(null);
        loadedOutPort.send(newDoc);
    }

    protected void close() {
        documentVariable.setValue(null);
        nameVariable.setValue(null);
        directoryVariable.setValue(null);
        loadedOutPort.send(null);
    }

    public void sinaxeInit(SinaxeRuntime runtime, SinaxeProperty properties) throws SinaxeException {
        this.runtime = runtime;
        SinaxeProperty parentProp = properties.get("parent");
        if (parentProp != null) parentComponent = runtime.getNeighborGraphic(parentProp);
        create = properties.has("create");
        noerror = properties.has("noerror");
        noempty = properties.has("noempty");
        if (properties.has("newtemplate")) {
            SinaxeProperty prop = properties.get("newtemplate");
            newTemplate = prop.getXML();
        }
        nameVariable = runtime.registerVariable("name");
        directoryVariable = runtime.registerVariable("directory");
        documentVariable = runtime.registerVariable("document");
        filenameQuery = runtime.getQuery("filename");
        relativeQuery = runtime.getQuery("relative");
        loadedOutPort = runtime.registerOutPort("loaded", DT_NODE | DT_NULL);
        loadInPort = runtime.registerInPort("load", DT_CONTEXT | DT_NULL);
        loadInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                load(data);
            }
        });
        reloadInPort = runtime.registerInPort("reload", DT_CONTEXT | DT_NULL);
        reloadInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                reload();
            }
        });
        newInPort = runtime.registerInPort("new", DT_ANY);
        newInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                createNew();
            }
        });
        saveInPort = runtime.registerInPort("save", DT_ANY);
        saveInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                save();
            }
        });
        saveasInPort = runtime.registerInPort("saveas", DT_ANY);
        saveasInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                saveas();
            }
        });
        closeInPort = runtime.registerInPort("close", DT_ANY);
        closeInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                close();
            }
        });
    }

    public void sinaxeExit() throws SinaxeException {
    }

    public Component sinaxeGetComponent() {
        return null;
    }

    public String sinaxeGetDocumentation() {
        return null;
    }

    public Object sinaxePointToContext(java.awt.Point source) {
        return null;
    }
}
