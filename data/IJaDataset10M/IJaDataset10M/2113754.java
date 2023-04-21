package org.hironico.gui.table.export;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Cette classe permet de réaliser l'export des données contenues dans
 * la property tableToExport dans un Thread séparé du programme principal
 * pour permettre le rafraichissement de la GUI.
 * Cette classe utilise les interfaces New IO du JDK 1.4.x.
 * Note : utiliser un log level mis à DEBUG <b>ralentit</b> considérablement
 * les performances.
 * @version $Rev: 1.2 $
 * @author $Author: hironico $
 * @since 1.5.3
 */
public class TableExporterThread extends Thread {

    /**
     * Construire le Thread en initialisant les références vers l'interface
     * graphique et les données à exporter.
     * 
     */
    public TableExporterThread(TableExporterPanel panel, JTable tableToExport) {
        super();
        this.tableExporterPanel = panel;
        this.tableToExport = tableToExport;
        if (!logger.getAllAppenders().hasMoreElements()) {
            PatternLayout layout = new PatternLayout("%-5p [%t]: %m%n");
            ConsoleAppender appender = new ConsoleAppender(layout);
            logger.addAppender(appender);
        }
    }

    /**
     * Cette méthode va executer l'export en paralélle du programme principal.
     * On va exporter suivant les paramétres saisis dans le TableExporterPanel.
     * @since 0.0.1
     */
    @Override
    public void run() {
        currentTask = 0;
        tableExporterPanel.setProgress(0);
        tableExporterPanel.exportLaunched();
        if (tableExporterPanel.isExportToClipboard() && !tableExporterPanel.isCancelAsked()) {
            exportToClipboard();
        }
        if (tableExporterPanel.isExportToFile() && !tableExporterPanel.isCancelAsked()) {
            if (tableExporterPanel.isExportToExcel()) {
                exportToExcelFile();
            } else {
                exportToFile();
            }
        }
        tableExporterPanel.setProgress(0);
        tableExporterPanel.exportEnded();
    }

    /**
     * Cette classe permet d'exporter les données dans le clipboard systéme.
     * Pour cela, il suffit juste de transformer le contenu de la table é
     * exporter en String et de la coller dans le clipboard.
     * 
     */
    protected void exportToClipboard() {
        String cellSeparator = tableExporterPanel.getCellSeparator();
        String rowSeparator = tableExporterPanel.getRowSeparator();
        String carriageReturn = "\n";
        boolean useCellSeparator = tableExporterPanel.isCellSeparated();
        boolean useRowSeparator = tableExporterPanel.isRowSeparated();
        boolean newLineForEachCell = tableExporterPanel.isNewLineForEachCell();
        boolean newLineForEachRow = tableExporterPanel.isNewLineForEachRow();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringBuffer stringToCopy = new StringBuffer("");
        int[] selectedRows;
        if (tableExporterPanel.isOnlySelectedRows()) {
            selectedRows = tableToExport.getSelectedRows();
        } else {
            selectedRows = new int[tableToExport.getRowCount()];
            for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                selectedRows[cptRow] = cptRow;
            }
        }
        if (tableExporterPanel.isIncludeHeaders()) {
            int columnCount = tableToExport.getColumnCount();
            for (int cptCol = 0; cptCol < columnCount; cptCol++) {
                stringToCopy.append(tableToExport.getColumnName(cptCol));
                if (useCellSeparator) {
                    stringToCopy.append(cellSeparator);
                }
                if (newLineForEachCell) {
                    stringToCopy.append(carriageReturn);
                }
            }
            if (useRowSeparator) {
                stringToCopy.append(rowSeparator);
            }
            if (newLineForEachRow) {
                stringToCopy.append(carriageReturn);
            }
        }
        for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
            if (tableExporterPanel.isCancelAsked()) {
                logger.warn("Cancel asked while exporting table to clipboard. Stopped at row #" + cptRow);
                break;
            }
            for (int cptCol = 0; cptCol < tableToExport.getColumnCount(); cptCol++) {
                Object value = tableToExport.getValueAt(selectedRows[cptRow], cptCol);
                String valueStr = "NULL";
                if (value != null) {
                    valueStr = value.toString();
                }
                stringToCopy.append(valueStr);
                if (useCellSeparator) {
                    stringToCopy.append(cellSeparator);
                }
                if (newLineForEachCell) {
                    stringToCopy.append(carriageReturn);
                }
                tableExporterPanel.setProgress(++currentTask);
            }
            if (useRowSeparator) {
                stringToCopy.append(rowSeparator);
            }
            if (newLineForEachRow) {
                stringToCopy.append(carriageReturn);
            }
        }
        logger.info(stringToCopy.length() + " caracters copied into clipboard !");
        StringSelection selection = new StringSelection(stringToCopy.toString());
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(tableExporterPanel, stringToCopy.length() + " caracters copied into clipboard !", "Yeah...", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Permet d'exporter les données de la table vers le fichier spécifié
     * dans l'interface graphique.
     * 
     */
    protected void exportToFile() {
        try {
            File fichier = new File(tableExporterPanel.getExportFileName());
            FileOutputStream fos = new FileOutputStream(fichier);
            FileChannel fc = fos.getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 1024);
            String charsetName = tableExporterPanel.getCharsetName();
            logger.debug("Using charset : " + charsetName);
            byte[] cellSeparator = tableExporterPanel.getCellSeparator().getBytes(charsetName);
            byte[] rowSeparator = tableExporterPanel.getRowSeparator().getBytes(charsetName);
            byte[] carriageReturn = "\n".getBytes(charsetName);
            boolean useCellSeparator = tableExporterPanel.isCellSeparated();
            boolean useRowSeparator = tableExporterPanel.isRowSeparated();
            boolean newLineForEachCell = tableExporterPanel.isNewLineForEachCell();
            boolean newLineForEachRow = tableExporterPanel.isNewLineForEachRow();
            int[] selectedRows;
            if (tableExporterPanel.isOnlySelectedRows()) {
                selectedRows = tableToExport.getSelectedRows();
            } else {
                selectedRows = new int[tableToExport.getRowCount()];
                for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                    selectedRows[cptRow] = cptRow;
                }
            }
            fc.position(0);
            for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                if (tableExporterPanel.isCancelAsked()) {
                    logger.warn("Cancel asked while exporting to file. Stoped at row #" + cptRow + " out of " + selectedRows.length);
                    break;
                }
                bb.clear();
                Object value = null;
                String valueStr = null;
                if (tableExporterPanel.isIncludeHeaders()) {
                    int columnCount = tableToExport.getColumnCount();
                    for (int cptCol = 0; cptCol < columnCount; cptCol++) {
                        bb.put(tableToExport.getColumnName(cptCol).getBytes(charsetName));
                        if (useCellSeparator) {
                            bb.put(cellSeparator);
                        }
                        if (newLineForEachCell) {
                            bb.put(carriageReturn);
                        }
                    }
                    if (useRowSeparator) {
                        bb.put(rowSeparator);
                    }
                    if (newLineForEachRow) {
                        bb.put(carriageReturn);
                    }
                }
                bb.flip();
                fc.write(bb);
                bb.clear();
                for (int cptCol = 0; cptCol < tableToExport.getColumnCount(); cptCol++) {
                    value = tableToExport.getValueAt(selectedRows[cptRow], cptCol);
                    if (value != null) {
                        valueStr = value.toString();
                    } else {
                        valueStr = "null";
                    }
                    bb.put(valueStr.getBytes(charsetName));
                    if (useCellSeparator) {
                        bb.put(cellSeparator);
                    }
                    if (newLineForEachCell) {
                        bb.put(carriageReturn);
                    }
                    bb.flip();
                    fc.write(bb);
                    bb.clear();
                    tableExporterPanel.setProgress(++currentTask);
                }
                if (useRowSeparator) {
                    bb.put(rowSeparator);
                }
                if (newLineForEachRow) {
                    bb.put(carriageReturn);
                }
                bb.flip();
                fc.write(bb);
                bb.clear();
            }
            fc.close();
            fos.close();
            JOptionPane.showMessageDialog(tableExporterPanel, "Exported " + selectedRows.length + " rows", "Yeah...", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioe) {
            Component parent = null;
            if (tableExporterPanel.getParentInternalFrame() != null) {
                parent = tableExporterPanel.getParentInternalFrame().getDesktopPane();
            }
            if (tableExporterPanel.getParentFrame() != null) {
                parent = tableExporterPanel.getParentFrame();
            } else {
                parent = tableExporterPanel.getParent();
            }
            JOptionPane.showInternalMessageDialog(parent, "IOException : " + ioe.getMessage(), "Ohoh...", JOptionPane.ERROR_MESSAGE);
            logger.error("Cannot export to file.", ioe);
        }
    }

    protected void exportToExcelFile() {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(tableExporterPanel.getExportFileName()));
            WritableSheet sheet = workbook.createSheet("Hironico Db tool copy", 0);
            int[] selectedRows;
            if (tableExporterPanel.isOnlySelectedRows()) {
                selectedRows = tableToExport.getSelectedRows();
            } else {
                selectedRows = new int[tableToExport.getRowCount()];
                for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                    selectedRows[cptRow] = cptRow;
                }
            }
            int columnCount = tableToExport.getColumnCount();
            Label label;
            currentTask = 0;
            int currentRow = 0;
            if (tableExporterPanel.isIncludeHeaders()) {
                for (int cptCol = 0; cptCol < columnCount; cptCol++) {
                    label = new Label(cptCol, 0, tableToExport.getColumnName(cptCol));
                    sheet.addCell(label);
                }
                currentRow++;
            }
            for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                for (int col = 0; col < columnCount; col++) {
                    label = new Label(col, currentRow, (String) tableToExport.getValueAt(selectedRows[cptRow], col));
                    sheet.addCell(label);
                    tableExporterPanel.setProgress(++currentTask);
                }
                currentRow++;
            }
            workbook.write();
            workbook.close();
            JOptionPane.showMessageDialog(tableExporterPanel, "Exported " + selectedRows.length + " rows to Excel file.", "Yeah...", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioe) {
            logger.error("Cannot write to Excel workbook.", ioe);
            JOptionPane.showMessageDialog(tableExporterPanel, "Cannot write to Excel workbook:\n" + ioe.getMessage(), "Ohoh...", JOptionPane.ERROR_MESSAGE);
        } catch (WriteException we) {
            logger.error("Cannot write to excel workbook", we);
            JOptionPane.showMessageDialog(tableExporterPanel, "Cannot write to Excel workbook:\n" + we.getMessage(), "Ohoh...", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** le panel de paramétrage. Utilisé pour mettre à jour la gui pendant l'exécution. */
    private TableExporterPanel tableExporterPanel;

    /** la table dont il faut exporter les données. */
    private JTable tableToExport;

    /** le no courant de cellule exportée. c'est un simple compteur. */
    private int currentTask;

    /** log4j logger */
    private static Logger logger = Logger.getLogger("org.hironico.gui");
}
