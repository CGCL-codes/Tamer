package mr3.actions;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import mr3.*;
import mr3.data.*;
import mr3.util.*;
import com.hp.hpl.mesa.rdf.jena.common.*;
import com.hp.hpl.mesa.rdf.jena.mem.*;
import com.hp.hpl.mesa.rdf.jena.model.*;

/**
 * @author takeshi morita
 *
 */
public abstract class AbstractActionFile extends MR3AbstractAction {

    public AbstractActionFile() {
    }

    public AbstractActionFile(MR3 mr3) {
        this.mr3 = mr3;
    }

    public AbstractActionFile(String name) {
        super(name);
    }

    public AbstractActionFile(MR3 mr3, String name) {
        super(name);
        this.mr3 = mr3;
    }

    public AbstractActionFile(MR3 mr3, String name, ImageIcon icon) {
        super(mr3, name, icon);
    }

    private void setNsPrefix(RDFWriter writer) {
        Set prefixNsInfoSet = mr3.getGraphManager().getPrefixNSInfoSet();
        for (Iterator i = prefixNsInfoSet.iterator(); i.hasNext(); ) {
            PrefixNSInfo info = (PrefixNSInfo) i.next();
            if (info.isAvailable()) {
                writer.setNsPrefix(info.getPrefix(), info.getNameSpace());
            }
        }
    }

    public Writer writeModel(Model model, Writer output, RDFWriter writer) {
        try {
            setNsPrefix(writer);
            String baseURI = mr3.getGraphManager().getBaseURI().replaceAll("#", "");
            writer.write(model, output, baseURI);
        } catch (RDFException e) {
            e.printStackTrace();
        }
        return output;
    }

    protected Model readModel(Reader r, String xmlbase) {
        if (r == null) {
            return null;
        }
        Model model = new ModelMem();
        try {
            RDFReader reader = new RDFReaderFImpl().getReader("RDF/XML");
            reader.read(model, r, xmlbase);
        } catch (RDFException e) {
            e.printStackTrace();
        }
        return model;
    }

    protected Reader getReader(String uri) {
        Component desktop = mr3.getDesktopPane();
        Preferences userPrefs = mr3.getUserPrefs();
        if (uri == null) {
            return null;
        }
        URL rdfURI = null;
        try {
            rdfURI = getURI(uri);
            String encoding = userPrefs.get(PrefConstants.InputEncoding, "SJIS");
            Reader reader = new InputStreamReader(rdfURI.openStream(), encoding);
            return reader;
        } catch (UnknownHostException uhe) {
            JOptionPane.showInternalMessageDialog(desktop, "Unknown Host(Proxy)", "Warning", JOptionPane.ERROR_MESSAGE);
        } catch (MalformedURLException uriex) {
            uriex.printStackTrace();
        } catch (IOException ioe) {
            JOptionPane.showInternalMessageDialog(desktop, "File Not Found.", "Warning", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    protected Reader getReader(String ext, String encoding) {
        Preferences userPrefs = mr3.getUserPrefs();
        File file = getFile(true, ext);
        if (file == null) {
            return null;
        }
        if (ext.equals("mr3")) {
            mr3.setCurrentProject(file);
        }
        try {
            if (encoding == null) {
                encoding = userPrefs.get(PrefConstants.InputEncoding, "SJIS");
            }
            Reader reader = new InputStreamReader(new FileInputStream(file), encoding);
            return reader;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static MR3FileFilter mr3FileFilter = new MR3FileFilter();

    private static RDFsFileFilter rdfsFileFilter = new RDFsFileFilter();

    private static NTripleFileFilter n3FileFilter = new NTripleFileFilter();

    protected File getFile(boolean isOpenFile, String extension) {
        Component desktop = mr3.getDesktopPane();
        Preferences userPrefs = mr3.getUserPrefs();
        JFileChooser jfc = new JFileChooser(userPrefs.get(PrefConstants.DefaultWorkDirectory, ""));
        if (extension.equals("mr3")) {
            jfc.setFileFilter(mr3FileFilter);
        } else if (extension.equals("n3")) {
            jfc.setFileFilter(n3FileFilter);
        } else {
            jfc.setFileFilter(rdfsFileFilter);
        }
        if (isOpenFile) {
            if (jfc.showOpenDialog(desktop) == JFileChooser.APPROVE_OPTION) {
                return jfc.getSelectedFile();
            } else {
                return null;
            }
        } else {
            if (jfc.showSaveDialog(desktop) == JFileChooser.APPROVE_OPTION) {
                String defaultPath = jfc.getSelectedFile().getAbsolutePath();
                if (extension.equals("mr3")) {
                    return new File(complementMR3Extension(defaultPath, extension));
                } else {
                    return new File(complementRDFsExtension(defaultPath, extension));
                }
            } else {
                return null;
            }
        }
    }

    private String complementMR3Extension(String tmp, String extension) {
        String ext = (extension != null) ? "." + extension.toLowerCase() : "";
        if (extension != null && !tmp.toLowerCase().endsWith(".mr3")) {
            tmp += ext;
        }
        return tmp;
    }

    private String complementRDFsExtension(String tmp, String extension) {
        String ext = (extension != null) ? "." + extension.toLowerCase() : "";
        if (extension != null && !tmp.toLowerCase().endsWith(".rdf") && !tmp.toLowerCase().endsWith(".rdfs") && !tmp.toLowerCase().endsWith(".n3")) {
            tmp += ext;
        }
        return tmp;
    }

    protected URL getURI(String uri) throws MalformedURLException, UnknownHostException {
        Preferences userPrefs = mr3.getUserPrefs();
        URL rdfURI = null;
        boolean isProxy = userPrefs.getBoolean("Proxy", false);
        if (isProxy) {
            String proxyURL = userPrefs.get(PrefConstants.ProxyHost, "http://localhost");
            int proxyPort = userPrefs.getInt(PrefConstants.ProxyPort, 8080);
            rdfURI = new URL("http", proxyURL, proxyPort, uri);
        } else {
            rdfURI = new URL(uri);
        }
        return rdfURI;
    }

    protected void saveProject(File file) {
        try {
            Model exportModel = mr3.getProjectModel();
            Writer output = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            RDFWriter writer = new RDFWriterFImpl().getWriter("RDF/XML");
            writeModel(exportModel, output, writer);
            mr3.setTitle("MR^3 - " + file.getAbsolutePath());
            mr3.setCurrentProject(file);
        } catch (RDFException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (UnsupportedEncodingException e3) {
            e3.printStackTrace();
        }
    }

    protected void saveProjectAs() {
        File file = getFile(false, "mr3");
        if (file == null) {
            return;
        }
        saveProject(file);
        mr3.setCurrentProject(file);
    }

    protected void exitProgram() {
        int messageType = confirmExitProject("Exit Program");
        if (messageType == JOptionPane.CANCEL_OPTION) {
            return;
        }
        saveWindows();
        System.exit(0);
    }

    private void saveWindowBounds(Preferences userPrefs) {
        Rectangle windowRect = mr3.getBounds();
        userPrefs.putInt(PrefConstants.WindowHeight, (int) windowRect.getHeight());
        userPrefs.putInt(PrefConstants.WindowWidth, (int) windowRect.getWidth());
        userPrefs.putInt(PrefConstants.WindowPositionX, (int) windowRect.getX());
        userPrefs.putInt(PrefConstants.WindowPositionY, (int) windowRect.getY());
    }

    private void saveRDFEditorBounds(Preferences userPrefs, JInternalFrame iFrame) {
        Rectangle rdfEditorRect = iFrame.getBounds();
        userPrefs.putInt(PrefConstants.RDFEditorHeight, (int) rdfEditorRect.getHeight());
        userPrefs.putInt(PrefConstants.RDFEditorWidth, (int) rdfEditorRect.getWidth());
        userPrefs.putInt(PrefConstants.RDFEditorPositionX, (int) rdfEditorRect.getX());
        userPrefs.putInt(PrefConstants.RDFEditorPositionY, (int) rdfEditorRect.getY());
    }

    private void saveClassEditorBounds(Preferences userPrefs, JInternalFrame iFrame) {
        Rectangle classEditorRect = iFrame.getBounds();
        userPrefs.putInt(PrefConstants.ClassEditorHeight, (int) classEditorRect.getHeight());
        userPrefs.putInt(PrefConstants.ClassEditorWidth, (int) classEditorRect.getWidth());
        userPrefs.putInt(PrefConstants.ClassEditorPositionX, (int) classEditorRect.getX());
        userPrefs.putInt(PrefConstants.ClassEditorPositionY, (int) classEditorRect.getY());
    }

    private void savePropertyEditorBounds(Preferences userPrefs, JInternalFrame iFrame) {
        Rectangle propertyEditorRect = iFrame.getBounds();
        userPrefs.putInt(PrefConstants.PropertyEditorHeight, (int) propertyEditorRect.getHeight());
        userPrefs.putInt(PrefConstants.PropertyEditorWidth, (int) propertyEditorRect.getWidth());
        userPrefs.putInt(PrefConstants.PropertyEditorPositionX, (int) propertyEditorRect.getX());
        userPrefs.putInt(PrefConstants.PropertyEditorPositionY, (int) propertyEditorRect.getY());
    }

    private void saveWindows() {
        Preferences userPrefs = mr3.getUserPrefs();
        JInternalFrame[] internalFrames = mr3.getInternalFrames();
        saveWindowBounds(userPrefs);
        saveRDFEditorBounds(userPrefs, internalFrames[0]);
        saveClassEditorBounds(userPrefs, internalFrames[1]);
        savePropertyEditorBounds(userPrefs, internalFrames[2]);
    }

    protected int confirmExitProject(String title) {
        JComponent desktop = mr3.getDesktopPane();
        int messageType = JOptionPane.showInternalConfirmDialog(desktop, "Save changes ?", "MR^3 - " + title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (messageType == JOptionPane.YES_OPTION) {
            saveProjectAs();
        }
        return messageType;
    }

    public void showSrcView() {
        JInternalFrame srcFrame = mr3.getSourceFrame();
        try {
            srcFrame.toFront();
            srcFrame.setVisible(true);
            srcFrame.setIcon(false);
            mr3.getShowSrcWindowBox().setState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
