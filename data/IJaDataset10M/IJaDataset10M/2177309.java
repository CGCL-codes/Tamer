package net.sf.xpontus.actions.impl;

import com.ibm.icu.text.CharsetDetector;
import com.vlsolutions.swing.docking.Dockable;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.controllers.impl.XPontusUndoManager;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.modules.gui.components.XPontusEditorUI;
import net.sf.xpontus.utils.FileHistoryList;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Action to save a document under a new name
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SaveAsActionImpl extends SimpleDocumentAwareActionImpl {

    private static final long serialVersionUID = 1652205025693847700L;

    public static final String BEAN_ALIAS = "action.saveas";

    private static final Pattern ENCODING_PATTERN = Pattern.compile("(?<=encoding=\")[^\"]*(?=\")");

    private JFileChooser chooser;

    /** Creates a new instance of SaveAsActionImpl */
    public SaveAsActionImpl() {
    }

    /**
     *
     * Save the document under a new name
     */
    public void execute() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }
        DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer();
        chooser.setDialogTitle("Save " + dtc.getCurrentDockable().getDockKey().getName());
        doSave();
    }

    public void doSave() {
        int answer = chooser.showSaveDialog(XPontusComponentsUtils.getTopComponent().getDisplayComponent());
        if (answer == JFileChooser.APPROVE_OPTION) {
            try {
                File f = chooser.getSelectedFile();
                FileObject dest = VFS.getManager().toFileObject(f);
                if (dest.exists()) {
                    Toolkit.getDefaultToolkit().beep();
                    int rep = JOptionPane.showConfirmDialog(chooser, "Erase the file?", "The file exists!", JOptionPane.YES_NO_OPTION);
                    if (rep == JOptionPane.YES_OPTION) {
                        save(dest);
                    } else {
                        doSave();
                    }
                } else {
                    save(dest);
                }
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
    }

    private String getEncoding(String xml) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(xml.getBytes())));
            String prolog = reader.readLine();
            Matcher matcher = ENCODING_PATTERN.matcher(prolog);
            return matcher.find() ? matcher.group() : "";
        } finally {
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    /**
     *
     * @param fo
     * @throws java.lang.Exception
     */
    public void save(final FileObject fo) throws Exception {
        IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentDockable();
        final JTextComponent editor = container.getEditorComponent();
        OutputStream bos = null;
        if (fo instanceof LocalFile) {
            bos = new FileOutputStream(fo.getName().getPath());
        } else {
            fo.getContent().getOutputStream();
        }
        FileHistoryList.addFile(fo.getName().getURI());
        String encoding = "UTF-8";
        String m_encoding = getEncoding(editor.getText());
        if (!m_encoding.trim().equals("")) {
            encoding = m_encoding;
        }
        OutputStreamWriter m_writer = new OutputStreamWriter(bos, encoding);
        m_writer.write(editor.getText());
        m_writer.flush();
        m_writer.close();
        bos.flush();
        bos.close();
        String m_ext = FilenameUtils.getExtension(fo.getName().getBaseName());
        String mm = MimeTypesProvider.getInstance().getMimeType(fo.getName().getBaseName());
        final Object currentMime = editor.getClientProperty(XPontusConstantsIF.CONTENT_TYPE);
        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);
        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);
        final Dockable dc = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentDockable();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    dc.getDockKey().setTooltip(fo.getURL().toExternalForm());
                    dc.getDockKey().setName(fo.getName().getBaseName());
                } catch (Exception exe) {
                    getLogger().error(exe.getMessage(), exe);
                }
            }
        });
        ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
        handler.setModified(false);
        if (currentMime != null) {
            if (!currentMime.equals(mm)) {
                System.out.println("Mime type changed");
                System.out.println("extension is :" + m_ext);
                editor.setUI(new XPontusEditorUI(editor, fo.getName().getBaseName()));
                CharsetDetector detector = new CharsetDetector();
                detector.setText(editor.getText().getBytes());
                if (m_ext != null) {
                    Document doc = editor.getDocument();
                    if (m_ext.toLowerCase().endsWith("xsl") || m_ext.toLowerCase().endsWith("xslt")) {
                        doc.putProperty("BUILTIN_COMPLETION", "XSL");
                    } else if (m_ext.toLowerCase().endsWith("xsd")) {
                        doc.putProperty("BUILTIN_COMPLETION", "XSD");
                    } else if (m_ext.toLowerCase().endsWith("html") || m_ext.endsWith("htm")) {
                        doc.putProperty("BUILTIN_COMPLETION", "HTML");
                    } else if (m_ext.toLowerCase().endsWith("rng")) {
                        doc.putProperty("BUILTIN_COMPLETION", "RELAXNG");
                    }
                }
                handler = new ModificationHandler(container);
                editor.read(detector.detect().getReader(), null);
                handler.setModified(false);
                XPontusUndoManager _undo = new XPontusUndoManager();
                editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);
                editor.getDocument().addUndoableEditListener(new UndoableEditListener() {

                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((XPontusUndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                    }
                });
            }
        }
    }
}
