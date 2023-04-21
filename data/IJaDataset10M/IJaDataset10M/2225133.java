package manager.gui.actions;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QDir;
import org.apache.log4j.Logger;
import manager.gui.widgets.TreeAndFileItem;
import manager.Manager;
import manager.exceptions.UnexpectedElementException;
import manager.model.Model;
import manager.util.FileSystem;
import manager.util.MetadataController;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: sbrummerloh
 * Date: 10.09.2008
 * Time: 13:44:32
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateFileItemAction extends QAction {

    private static Logger logger = Logger.getLogger("");

    TreeAndFileItem treeItem;

    public DuplicateFileItemAction(QObject parent, TreeAndFileItem treeItem) {
        super(parent);
        this.setText(tr("Duplicate"));
        this.triggered.connect(this, "on_DuplicateButton_clicked()");
        this.treeItem = treeItem;
    }

    /**
        * Wird ausgef�hrt, falls die Schaltfl�che zum Duplizieren eines Dateisystem-Elements angeklickt wurde.
        * Ergebnis: Es erscheint ein Dialog zum Ausw�hlen des Ziels und die Datei bzw. das Verzeichnis (inklusiv aller Dateien und Unterverzeichnisse) wird kopiert.
        */
    private void on_DuplicateButton_clicked() {
        logger.debug("");
        if (treeItem == null) {
            return;
        }
        TreeAndFileItem pathItem = treeItem;
        if (treeItem.parent() != null) {
            pathItem = (TreeAndFileItem) treeItem.parent();
        }
        String newDir = QFileDialog.getExistingDirectory(Manager.getMainWindow(), tr("Duplicate"), pathItem.getPath());
        if (newDir == null || newDir.equals("")) {
            return;
        }
        duplicate(treeItem, newDir);
    }

    /**
     * Dupliziert/kopiert ein Element
     *
     * @param item   das zu duplizierende Element
     * @param newDir der Pfad vom neuen Element
     * @return falls das Element erfolgreich dupliziert werden konnte: true, sonst: false
     */
    public static boolean duplicate(TreeAndFileItem item, String newDir) {
        logger.debug("");
        String path = item.getPath();
        String newPath = FileSystem.concatPath(newDir, item.getName());
        if (FileSystem.isExisting(newPath)) {
            return false;
        }
        if (item.isDirectory()) {
            FileSystem.copy(path, newDir);
            if (newDir.startsWith(Model.getRootPath())) {
                Model.getFileTree().addItemToTree(newDir, false);
                Model.getMetadataTree().addItemToTree(newDir, false);
            }
        } else if (MetadataController.checkForMetadataFile(path)) {
            try {
                ArrayList<String> gisFileList = MetadataController.getDataFiles(path);
                for (String oldGisPath : gisFileList) {
                    String newGisPath = FileSystem.calculateRelativePath(FileSystem.calculatePathWithoutFile(path), FileSystem.calculatePathWithoutFile(newPath));
                    newGisPath = FileSystem.concatPath(FileSystem.calculatePathWithoutFile(oldGisPath), newGisPath);
                    newGisPath = FileSystem.concatPath(newGisPath, FileSystem.calculateFileWithoutPath(oldGisPath));
                    FileSystem.copy(oldGisPath, newGisPath);
                }
            } catch (UnexpectedElementException e) {
                logger.error("", e);
            }
        } else {
            FileSystem.copy(path, newPath);
        }
        return true;
    }
}
