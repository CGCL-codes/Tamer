package mswing.print;

import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import mswing.MBeanUtils;
import mswing.MParameters;
import mswing.MUtilities;
import mswing.patterns.MDetailDialog;
import mswing.table.MBasicTable;
import mswing.table.MUtilitiesTable;

/**
 * Classe d'impression et d'export. <BR>
 * Cette classe s'utilise avec la m�thode statique printOrExport. <BR>
 * Les instances (Imprimante, Fichiers html, csv ...) param�tr�es dans MParametersPrint doivent
 * impl�menter les m�thodes print(JTable,OutputStream), getFile(JTable), getName(), getIcon() et
 * isOpenCheckBoxEnabled().
 *
 * @author Emeric Vernat
 */
public abstract class MPrintCenter {

    /**
	 * Constructeur.
	 */
    protected MPrintCenter() {
        super();
    }

    /**
	 * M�thode principale d'impression et d'export d'une JTable. <BR>
	 * Elle affiche une bo�te de dialogue pour choisir et ordonner les colonnes � imprimer, et pour
	 * choisir le type de sortie. <BR>
	 * La pr�s�lection de colonnes en param�tre mise � jour et retourn�e permettra �ventuellement
	 * aux �crans de se souvenir des s�lections.
	 *
	 * @return mswing.patterns.MPrintParameters
	 * @param table javax.swing.JTable
	 * @param parameters mswing.patterns.MPrintParameters
	 */
    public static MPrintParameters printOrExport(JTable table, MPrintParameters parameters) {
        MPrintParameters params = parameters;
        params = MPrintDialog.chooseParameters(table, parameters);
        if (params == null) {
            return parameters;
        }
        try {
            params.getPrinter().print(table, params);
        } catch (final Exception e) {
            MUtilities.handleException(e);
        }
        return params;
    }

    /**
	 * Construit une JTable � partir d'un tableModel et d'une liste de tableColumns.
	 *
	 * @return javax.swing.JTable
	 * @param table javax.swing.JTable
	 * @param selectedColumns java.util.List
	 */
    protected JTable buildTable(JTable table, List selectedColumns) {
        final MBasicTable returnTable = new MBasicTable();
        TableColumn column;
        int modelIndex;
        for (final Iterator it = selectedColumns.iterator(); it.hasNext(); ) {
            column = (TableColumn) it.next();
            modelIndex = column.getModelIndex();
            returnTable.addColumn(column);
            column.setModelIndex(modelIndex);
        }
        returnTable.setAutoCreateColumnsFromModel(false);
        returnTable.setModel(table.getModel());
        final String title = buildTitle(table);
        returnTable.setName(title);
        return returnTable;
    }

    /**
	 * Construit le titre � inclure dans l'impression/export.
	 *
	 * @param component java.awt.Component
	 * @return String
	 */
    protected String buildTitle(Component component) {
        Component current = component;
        final StringBuffer title = new StringBuffer();
        String localTitle;
        while (current != null) {
            if (current instanceof MPrintInformations) {
                localTitle = ((MPrintInformations) current).getPrintTitle();
                if (localTitle != null && localTitle.length() != 0) {
                    if (title.length() != 0) {
                        title.insert(0, " - ");
                    }
                    title.insert(0, localTitle);
                }
            }
            if (current instanceof MDetailDialog) {
                current = ((MDetailDialog) current).getMasterDetailPanel();
            } else {
                current = current.getParent();
            }
        }
        return title.length() != 0 ? title.toString() : null;
    }

    /**
	 * Renvoie le texte de la cellule de cette JTable � la ligne et la colonne sp�cifi�e.
	 *
	 * @return String
	 * @param table javax.swing.JTable
	 * @param row int
	 * @param column int
	 */
    protected String getTextAt(JTable table, int row, int column) {
        String text = MUtilitiesTable.getTextAt(table, row, column);
        if (MParameters.IS_JAVA14_MIN && text != null && text.startsWith("<html>")) {
            text = text.replaceFirst("<html>", "");
            text = text.replaceFirst("<center>", "");
            text = text.replaceAll("<br>", "\n");
            text = text.replaceFirst("<font color='#[0-9]*+'>", "");
        }
        return text;
    }

    /**
	 * Renvoie la valeur de la cellule de cette JTable � la ligne et la colonne sp�cifi�e.
	 *
	 * @return String
	 * @param table javax.swing.JTable
	 * @param row int
	 * @param column int
	 */
    protected Object getValueAt(JTable table, int row, int column) {
        return table.getModel().getValueAt(row, column);
    }

    /**
	 * Renvoie la bo�te de dialogue swing de choix du fichier d'export. (Initialis�e pour s'ouvrir
	 * sur le r�pertoire courant user.dir).
	 *
	 * @return javax.swing.JFileChooser
	 */
    protected JFileChooser getFileChooser() {
        return MUtilities.getFileChooser();
    }

    /**
	 * Choix du fichier pour un export.
	 *
	 * @return java.io.File
	 * @param table javax.swing.JTable
	 * @param extension String
	 * @throws java.io.IOException Erreur disque
	 */
    protected File chooseFile(JTable table, String extension) throws IOException {
        final JFileChooser fileChooser = getFileChooser();
        final MExtensionFileFilter filter = new MExtensionFileFilter(extension);
        fileChooser.addChoosableFileFilter(filter);
        if (MParametersPrint.USE_PRINT_TITLE_AS_DEFAULT_FILE) {
            String title = buildTitle(table);
            if (title != null) {
                final String notAllowed = "\\/:*?\"<>|";
                final int notAllowedLength = notAllowed.length();
                for (int i = 0; i < notAllowedLength; i++) {
                    title = title.replace(notAllowed.charAt(i), ' ');
                }
                fileChooser.setSelectedFile(new File(title));
            }
        }
        try {
            final Component parent = table.getParent() != null ? table.getParent() : table;
            if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getCanonicalPath();
                if (!fileName.endsWith('.' + extension)) {
                    fileName += '.' + extension;
                }
                return new File(fileName);
            } else {
                return null;
            }
        } finally {
            fileChooser.removeChoosableFileFilter(filter);
        }
    }

    /**
	 * Retourne le fichier de sortie, par d�faut on ouvre la bo�te de dialogue de choix du fichier.
	 *
	 * @return java.io.File
	 * @param table javax.swing.JTable
	 * @throws java.io.IOException Erreur disque
	 */
    protected File getFile(JTable table) throws IOException {
        return chooseFile(table, getFileExtension());
    }

    /**
	 * M�thode abstraite assurant le polymorphisme des instances.
	 *
	 * @param table javax.swing.JTable
	 * @param outputStream java.io.OutputStream
	 * @throws java.io.IOException Erreur disque
	 */
    public abstract void print(JTable table, OutputStream outputStream) throws IOException;

    /**
	 * M�thode abstraite : les instances doivent renvoyer leur nom.
	 *
	 * @return String
	 */
    public abstract String getName();

    /**
	 * M�thode abstraite : les instances doivent renvoyer l'extension du fichier export�.
	 *
	 * @return String
	 */
    public abstract String getFileExtension();

    /**
	 * M�thode abstraite : les instances doivent renvoyer true ou false.
	 *
	 * @return boolean
	 */
    public abstract boolean isOpenCheckBoxEnabled();

    /**
	 * M�thode abstraite : les instances doivent renvoyer l'ic�ne repr�sentant le type.
	 *
	 * @return javax.swing.Icon
	 */
    public abstract Icon getIcon();

    /**
	 * Impression/export de la table avec les param�tres sp�cifi�s
	 *
	 * @param table javax.swing.JTable
	 * @param parameters mswing.print.MPrintParameters
	 * @throws java.io.IOException Erreur disque
	 */
    protected void print(JTable table, MPrintParameters parameters) throws IOException {
        OutputStream outputStream = null;
        try {
            final List selectionColumns = parameters.getSelectionColumns();
            final boolean printExportedFile = parameters.isPrintExportedFile() && !MParametersPrint.JAVA_PRINTER.equals(getName()) && MParameters.IS_JAVA16_MIN;
            final boolean openExportedFile = parameters.isOpenExportedFile() || MParametersPrint.HTML_PRINTER.equals(getName());
            final JTable builtTable = buildTable(table, selectionColumns);
            final File file = printExportedFile ? createTempFile(getFileExtension()) : getFile(table);
            if (file != null) {
                outputStream = new BufferedOutputStream(new FileOutputStream(file));
                print(builtTable, outputStream);
                outputStream.close();
                postProcess(file);
                if (printExportedFile) {
                    try {
                        final java.lang.reflect.Method getDesktopMethod = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]);
                        final Object desktop = MBeanUtils.invokeMethod(getDesktopMethod, null, null);
                        final java.lang.reflect.Method printMethod = desktop.getClass().getMethod("print", new Class[] { File.class });
                        MBeanUtils.invokeMethod(printMethod, desktop, new Object[] { file });
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (openExportedFile) {
                    showDocument(file);
                }
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException e) {
                    MUtilities.handleError(e);
                }
            }
        }
    }

    /**
	 * Cr�e un fichier temporaire selon l'extension en param�tre.
	 *
	 * @param extension String
	 * @return java.io.File
	 * @throws java.io.IOException Erreur disque
	 */
    protected File createTempFile(String extension) throws IOException {
        final File tempFile = File.createTempFile("tmp", extension != null ? '.' + extension : null);
        tempFile.deleteOnExit();
        return tempFile;
    }

    /**
	 * Effectue le post-processing. <br>
	 * Les impl�mentations peuvent surcharger cette m�thode si un post-processing est requis (par
	 * exemple si un script natif doit �tre ex�cut� apr�s l'export)
	 *
	 * @param targetFile java.io.File
	 * @throws java.io.IOException Erreur disque
	 */
    protected void postProcess(File targetFile) throws IOException {
    }

    /**
	 * Affiche le document. <br>
	 * Les impl�mentations peuvent surcharger cette m�thode.
	 *
	 * @param targetFile java.io.File
	 * @throws java.io.IOException Erreur disque
	 */
    protected void showDocument(File targetFile) throws IOException {
        MUtilities.showDocument("file:///" + targetFile.getCanonicalPath());
    }

    /**
	 * Retourne le nom pour tooltip dans la comboBox.
	 *
	 * @return String
	 */
    public String toString() {
        return getName();
    }
}
