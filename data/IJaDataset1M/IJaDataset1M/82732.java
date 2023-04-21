package org.mcisb.beacon.ui.pedro.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.mcisb.beacon.ui.pedro.NodeUtils;
import org.mcisb.beacon.ui.pedro.PedroUtils;
import org.mcisb.util.io.FileUtils;
import pedro.model.RecordModelFactory;
import pedro.view.NavigationTreeNode;

/**
 * 
 * @author Neil Swainston
 */
public class FileExporter {

    /**
	 * 
	 */
    private final FileUtils fileUtils = new FileUtils();

    /**
	 * 
	 */
    private final NodeUtils nodeUtils = new NodeUtils();

    /**
	 * 
	 * @param experimentDir
	 * @param recordModelFactory
	 * @param node
	 * @param writeXml
	 * @param writeLSM
	 * @param writeExcel
	 * @throws IOException
	 */
    public void exportExperiment(File experimentDir, RecordModelFactory recordModelFactory, NavigationTreeNode node, boolean writeXml, boolean writeLSM, boolean writeExcel) throws IOException {
        OutputStream os = null;
        try {
            copyFile(experimentDir, node, "ASSAY", "equipment_file_name");
            if (writeXml) {
                os = new FileOutputStream(new File(experimentDir, "Experiment.xml"));
                new PedroUtils().exportXml(recordModelFactory, node.getRecordModel(), os);
                copyFile(experimentDir, new File(recordModelFactory.getSchemaName()));
            }
            final List dishes = nodeUtils.getChildNodes(nodeUtils.getChildNodes(node, "ASSAY"), "DISH");
            for (Iterator iterator = dishes.iterator(); iterator.hasNext(); ) {
                final NavigationTreeNode dishNode = (NavigationTreeNode) iterator.next();
                final File dishDir = fileUtils.mkdir(experimentDir, fileUtils.convertCharacters(dishNode.getDisplayName()));
                final List spots = nodeUtils.getChildNodes(dishNode, "SPOTREADING");
                for (Iterator spotIterator = spots.iterator(); spotIterator.hasNext(); ) {
                    final NavigationTreeNode spotNode = (NavigationTreeNode) spotIterator.next();
                    final File spotDir = fileUtils.mkdir(dishDir, fileUtils.convertCharacters(spotNode.getDisplayName()));
                    exportSpot(spotDir, spotNode, writeLSM, writeExcel);
                }
            }
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
	 * 
	 *
	 * @param spotDir
	 * @param node
	 * @param writeLSM
	 * @param writeExcel
	 * @throws IOException
	 */
    public void exportSpot(File spotDir, NavigationTreeNode node, boolean writeLSM, boolean writeExcel) throws IOException {
        if (writeLSM) {
            copyFile(spotDir, node, "RESULT", "results_file");
        }
        if (writeExcel) {
            new SpreadSheetGenerator(spotDir, node).createExcelFile();
        }
    }

    /**
     *
     * @param parentDir
     * @param node
     * @param displayName
     * @param fieldName
     * @return File
     * @throws IOException
     */
    private File copyFile(File parentDir, NavigationTreeNode node, String displayName, String fieldName) throws IOException {
        final List resultNodes = nodeUtils.getChildNodes(node, displayName);
        for (Iterator iterator = resultNodes.iterator(); iterator.hasNext(); ) {
            final File originalFile = new File(nodeUtils.getStringFieldValue((NavigationTreeNode) iterator.next(), fieldName));
            return copyFile(parentDir, originalFile);
        }
        return null;
    }

    /**
     * 
     * @param parentDir
     * @param originalFile
     * @return File
     * @throws IOException
     */
    private File copyFile(File parentDir, File originalFile) throws IOException {
        final File copiedFile = new File(parentDir, originalFile.getName());
        fileUtils.fileCopy(originalFile.getAbsolutePath(), copiedFile.getAbsolutePath());
        return copiedFile;
    }
}
