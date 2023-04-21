package util.swing;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import fseditor.ui.SwingFsEditor;

public class FileTransferHandler extends TransferHandler {

    public boolean importData(TransferSupport support) {
        Transferable t = support.getTransferable();
        Component source = support.getComponent();
        Component parent = source.getParent().getParent().getParent();
        if (parent instanceof SwingFsEditor) {
            SwingFsEditor fsEditor = (SwingFsEditor) parent;
            File dir = fsEditor.getCurrentDir();
            try {
                List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : files) {
                    file.renameTo(new File(dir, file.getName()));
                }
                fsEditor.setCurrentDir(dir);
            } catch (IOException ex) {
            } catch (UnsupportedFlavorException ex) {
            }
        }
        return false;
    }

    /**
	 * Create a Transferable to use as the source for a data transfer.
	 *
	 * @param c  The component holding the data to be transfered.  This
	 *  argument is provided to enable sharing of TransferHandlers by
	 *  multiple components.
	 * @return  The representation of the data to be transfered. 
	 *  
	 */
    protected Transferable createTransferable(JComponent c) {
        Object[] values = null;
        if (c instanceof JList) {
            values = ((JList) c).getSelectedValues();
        } else if (c instanceof JTable) {
            JTable table = (JTable) c;
            int[] rows = table.getSelectedRows();
            if (rows != null) {
                values = new Object[rows.length];
                for (int i = 0; i < rows.length; i++) {
                    values[i] = table.getValueAt(rows[i], 0);
                }
            }
        }
        if (values == null || values.length == 0) {
            return null;
        }
        StringBuffer plainBuf = new StringBuffer();
        StringBuffer htmlBuf = new StringBuffer();
        htmlBuf.append("<html>\n<body>\n<ul>\n");
        for (int i = 0; i < values.length; i++) {
            Object obj = values[i];
            String val = ((obj == null) ? "" : obj.toString());
            plainBuf.append(val + "\n");
            htmlBuf.append("  <li>" + val + "\n");
        }
        plainBuf.deleteCharAt(plainBuf.length() - 1);
        htmlBuf.append("</ul>\n</body>\n</html>");
        return new FileTransferable(plainBuf.toString(), htmlBuf.toString(), values);
    }

    public int getSourceActions(JComponent c) {
        return COPY | MOVE;
    }

    static class FileTransferable extends BasicTransferable {

        Object[] fileData;

        FileTransferable(String plainData, String htmlData, Object[] fileData) {
            super(plainData, htmlData);
            this.fileData = fileData;
        }

        /** 
		 * Best format of the file chooser is DataFlavor.javaFileListFlavor.
		 */
        protected DataFlavor[] getRicherFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.javaFileListFlavor;
            return flavors;
        }

        /**
		 * The only richer format supported is the file list flavor
		 */
        protected Object getRicherData(DataFlavor flavor) {
            if (DataFlavor.javaFileListFlavor.equals(flavor)) {
                ArrayList files = new ArrayList();
                for (int i = 0; i < fileData.length; i++) {
                    files.add(fileData[i]);
                }
                return files;
            }
            return null;
        }
    }
}
