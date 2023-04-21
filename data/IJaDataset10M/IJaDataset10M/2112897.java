package net.sf.jabref.groups;

import java.awt.datatransfer.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import net.sf.jabref.BasePanel;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefFrame;
import net.sf.jabref.external.DroppedFileHandler;
import net.sf.jabref.external.ExternalFileType;
import net.sf.jabref.external.TransferableFileLinkSelection;
import net.sf.jabref.gui.MainTable;
import net.sf.jabref.gui.MainTableFormat;
import net.sf.jabref.imports.ImportMenuItem;
import net.sf.jabref.imports.OpenDatabaseAction;
import net.sf.jabref.imports.ParserResult;
import net.sf.jabref.net.URLDownload;
import spl.PdfImporter;
import spl.Tools;

public class EntryTableTransferHandler extends TransferHandler {

    protected final MainTable entryTable;

    protected JabRefFrame frame;

    private BasePanel panel;

    protected DataFlavor urlFlavor;

    protected DataFlavor stringFlavor;

    protected static boolean DROP_ALLOWED = true;

    /**
	 * Construct the transfer handler.
	 * 
	 * @param entryTable
	 *            The table this transfer handler should operate on. This
	 *            argument is allowed to equal
	 * @null, in which case the transfer handler can assume that it works for a
	 *        JabRef instance with no databases open, attached to the empty
	 *        tabbed pane.
	 * @param frame
	 *            The JabRefFrame instance.
	 * @param panel
	 *            The BasePanel this transferhandler works for.
	 */
    public EntryTableTransferHandler(MainTable entryTable, JabRefFrame frame, BasePanel panel) {
        this.entryTable = entryTable;
        this.frame = frame;
        this.panel = panel;
        stringFlavor = DataFlavor.stringFlavor;
        try {
            urlFlavor = new DataFlavor("application/x-java-url; class=java.net.URL");
        } catch (ClassNotFoundException e) {
            Globals.logger("Unable to configure drag and drop for main table");
            e.printStackTrace();
        }
    }

    /**
	 * Overriden to indicate which types of drags are supported (only LINK).
	 * 
	 * @override
	 */
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_LINK;
    }

    /**
	 * This method is called when dragging stuff *from* the table.
	 */
    public Transferable createTransferable(JComponent c) {
        if (!draggingFile) {
            return new TransferableEntrySelection(entryTable.getSelectedEntries());
        } else {
            draggingFile = false;
            return (new TransferableFileLinkSelection(panel, entryTable.getSelectedEntries()));
        }
    }

    /**
	 * This method is called when stuff is drag to the component.
	 * 
	 * Imports the dropped URL or plain text as a new entry in the current
	 * database.
	 * 
	 */
    public boolean importData(JComponent comp, Transferable t) {
        int dropRow = -1;
        if (comp instanceof JTable) {
            dropRow = ((JTable) comp).getSelectedRow();
        }
        try {
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                @SuppressWarnings("unchecked") List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                return handleDraggedFiles(l, dropRow);
            }
            if (t.isDataFlavorSupported(urlFlavor)) {
                URL dropLink = (URL) t.getTransferData(urlFlavor);
                return handleDropTransfer(dropLink, dropRow);
            }
            if (t.isDataFlavorSupported(stringFlavor)) {
                String dropStr = (String) t.getTransferData(stringFlavor);
                return handleDropTransfer(dropStr, dropRow);
            }
        } catch (IOException ioe) {
            System.err.println("failed to read dropped data: " + ioe.toString());
        } catch (UnsupportedFlavorException ufe) {
            System.err.println("drop type error: " + ufe.toString());
        }
        System.err.println("can't transfer input: ");
        DataFlavor inflavs[] = t.getTransferDataFlavors();
        for (int i = 0; i < inflavs.length; i++) {
            System.out.println("  " + inflavs[i].toString());
        }
        return false;
    }

    /**
	 * This method is called to query whether the transfer can be imported.
	 * 
	 * Will return true for urls, strings, javaFileLists
	 * 
	 * @override
	 */
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        if (!DROP_ALLOWED) return false;
        for (int i = 0; i < transferFlavors.length; i++) {
            DataFlavor inflav = transferFlavors[i];
            if (inflav.match(urlFlavor) || inflav.match(stringFlavor) || inflav.match(DataFlavor.javaFileListFlavor)) return true;
        }
        return false;
    }

    boolean draggingFile = false;

    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            int col = entryTable.columnAtPoint(me.getPoint());
            String[] res = entryTable.getIconTypeForColumn(col);
            if (res == null) {
                super.exportAsDrag(comp, e, DnDConstants.ACTION_LINK);
                return;
            }
            if (res == MainTableFormat.FILE) {
                System.out.println("dragging file");
                draggingFile = true;
            }
        }
        super.exportAsDrag(comp, e, DnDConstants.ACTION_LINK);
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        super.exportDone(source, data, action);
    }

    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        super.exportToClipboard(comp, clip, action);
    }

    protected boolean handleDropTransfer(String dropStr, final int dropRow) throws IOException {
        if (dropStr.startsWith("file:")) {
            if (handleDraggedFilenames(dropStr, dropRow)) return true;
        } else if (dropStr.startsWith("http:")) {
            URL url = new URL(dropStr);
            return handleDropTransfer(url, dropRow);
        }
        File tmpfile = java.io.File.createTempFile("jabrefimport", "");
        tmpfile.deleteOnExit();
        FileWriter fw = new FileWriter(tmpfile);
        fw.write(dropStr);
        fw.close();
        ImportMenuItem importer = new ImportMenuItem(frame, false);
        importer.automatedImport(new String[] { tmpfile.getAbsolutePath() });
        return true;
    }

    /**
	 * Translate a String describing a set of files or URLs dragged into JabRef
     * into a List of File objects, taking care of URL special characters.
	 *
	 * @param s
	 *            String describing a set of files or URLs dragged into JabRef
     * @return a List<File> containing the individual file objects.
     *
	 */
    public static List<File> getFilesFromDraggedFilesString(String s) {
        String[] lines = s.replaceAll("\r", "").split("\n");
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            File fl = null;
            try {
                URL url = new URL(line);
                fl = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (fl != null) line = fl.getPath(); else if (line.startsWith("file:")) line = line.substring(5); else continue;
            if (line.startsWith("//")) line = line.substring(2);
            File f = new File(line);
            if (f.exists()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
	 * Handle a String describing a set of files or URLs dragged into JabRef.
	 * 
	 * @param s
	 *            String describing a set of files or URLs dragged into JabRef
     * @param dropRow The row in the table where the files were dragged.
     * @return success status for the operation
     *
	 */
    private boolean handleDraggedFilenames(String s, final int dropRow) {
        return handleDraggedFiles(getFilesFromDraggedFilesString(s), dropRow);
    }

    /**
	 * Handle a List containing File objects for a set of files to import.
	 * 
	 * @param files
	 *            A List containing File instances pointing to files.
	 * @param dropRow @param dropRow The row in the table where the files were dragged.
     * @return success status for the operation
	 */
    private boolean handleDraggedFiles(List<File> files, final int dropRow) {
        final String[] fileNames = new String[files.size()];
        int i = 0;
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext(); ) {
            File file = iterator.next();
            fileNames[i] = file.getAbsolutePath();
            i++;
        }
        new Thread(new Runnable() {

            public void run() {
                final String[] newfileNames = new PdfImporter(frame, panel, entryTable, dropRow).importPdfFiles(fileNames, frame);
                if (newfileNames.length > 0) {
                    loadOrImportFiles(newfileNames, dropRow);
                }
            }
        }).start();
        return true;
    }

    /**
	 * Take a set of filenames. Those with names indicating bib files are opened
	 * as such if possible. All other files we will attempt to import into the
	 * current database.
	 * 
	 * @param fileNames
	 *            The names of the files to open.
	 * @param dropRow success status for the operation
	 */
    private void loadOrImportFiles(String[] fileNames, int dropRow) {
        OpenDatabaseAction openAction = new OpenDatabaseAction(frame, false);
        ArrayList<String> notBibFiles = new ArrayList<String>();
        String encoding = Globals.prefs.get("defaultEncoding");
        for (int i = 0; i < fileNames.length; i++) {
            String extension = "";
            ExternalFileType fileType = null;
            int index = fileNames[i].lastIndexOf('.');
            if ((index >= 0) && (index < fileNames[i].length())) {
                extension = fileNames[i].substring(index + 1).toLowerCase();
                fileType = Globals.prefs.getExternalFileTypeByExt(extension);
            }
            if (extension.equals("bib")) {
                File f = new File(fileNames[i]);
                try {
                    ParserResult pr = OpenDatabaseAction.loadDatabase(f, encoding);
                    if ((pr == null) || (pr == ParserResult.INVALID_FORMAT)) {
                        notBibFiles.add(fileNames[i]);
                    } else {
                        openAction.addNewDatabase(pr, f, true);
                        frame.getFileHistory().newFile(fileNames[i]);
                    }
                } catch (IOException e) {
                    notBibFiles.add(fileNames[i]);
                }
                continue;
            }
            if (fileType != null && dropRow >= 0) {
                boolean local = true;
                DroppedFileHandler dfh = new DroppedFileHandler(frame, panel);
                dfh.handleDroppedfile(fileNames[i], fileType, local, entryTable, dropRow);
                continue;
            }
            notBibFiles.add(fileNames[i]);
        }
        if (notBibFiles.size() > 0) {
            String[] toImport = new String[notBibFiles.size()];
            notBibFiles.toArray(toImport);
            ImportMenuItem importer = new ImportMenuItem(frame, (entryTable == null));
            importer.automatedImport(toImport);
        }
    }

    protected boolean handleDropTransfer(URL dropLink, int dropRow) throws IOException {
        File tmpfile = java.io.File.createTempFile("jabrefimport", "");
        tmpfile.deleteOnExit();
        new URLDownload(entryTable, dropLink, tmpfile).download();
        ImportMenuItem importer = new ImportMenuItem(frame, (entryTable == null));
        importer.automatedImport(new String[] { tmpfile.getAbsolutePath() });
        return true;
    }
}
