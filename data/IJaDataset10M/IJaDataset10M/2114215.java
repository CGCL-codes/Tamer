package org.gerhardb.lib.dirtree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

/**
 * Pairs with DTNReader
 */
public class DTNWriter {

    DTNReaderWriter myBase;

    DTNWriter(DTNReaderWriter base) {
        this.myBase = base;
    }

    public static String getNameOfDrive(String path) {
        javax.swing.filechooser.FileSystemView view = javax.swing.filechooser.FileSystemView.getFileSystemView();
        File dir = new File(path);
        String name = view.getSystemDisplayName(dir);
        if (name == null) {
            return null;
        }
        name = name.trim();
        if (name == null || name.length() < 1) {
            return null;
        }
        int index = name.lastIndexOf(" (");
        if (index > 0) {
            name = name.substring(0, index);
        }
        return name;
    }

    /**
    * If directoryToStoreIn is null, will store in directory of root node.
	 */
    public void store(DirectoryTreeNode rootNode) throws Exception {
        store(rootNode, 0);
    }

    /**
    * If directoryToStoreIn is null, will store in directory of root node.
    * usableSpace is set if this was opened by ExplorerBox.
	 */
    public void store(DirectoryTreeNode rootNode, long usableSpace) throws Exception {
        if (rootNode == null || rootNode.getDirectory() == null) {
            return;
        }
        String dir = rootNode.getDirectory().toString();
        File directoryToStoreIn = this.myBase.myFileFromPathManagerStartingJibsFile;
        if (directoryToStoreIn == null) {
            String fileName = dir + "/" + this.myBase.getFileName();
            directoryToStoreIn = new File(fileName);
        }
        System.out.println("DTNWriter storing into: " + directoryToStoreIn);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(directoryToStoreIn)));
        if (this.myBase.iCountTargetFiles) {
            writer.print(DTNReaderWriter.VERSION_TOKEN_TARGET_COUNTED);
        } else {
            writer.print(DTNReaderWriter.VERSION_TOKEN_NO_TARGET_COUNT);
        }
        writer.println(this.myBase.getFileName());
        writer.print(DTNReaderWriter.DATE_MARKER);
        Date now = new Date();
        writer.println(DTNReaderWriter.DATE_FORMAT.format(now));
        writer.print(DTNReaderWriter.ROOT_PATH_MARKER);
        File treeRootDir = rootNode.getDirectory();
        writer.println(treeRootDir.getAbsolutePath());
        writer.print(DTNReaderWriter.ROOT_USABLE_SPACE);
        writer.println(usableSpace);
        writer.println("# depth path directories fileCount fileSize imageCount imageSize");
        int skip = dir.length();
        storeAllNodes(writer, rootNode, 0, skip);
        writer.flush();
        writer.close();
    }

    private void storeAllNodes(PrintWriter writer, DirectoryTreeNode node, int depth, int skip) {
        storeANode(writer, node, depth, skip);
        Enumeration<?> kids = node.children();
        while (kids.hasMoreElements()) {
            DirectoryTreeNode childNode = (DirectoryTreeNode) kids.nextElement();
            storeAllNodes(writer, childNode, depth + 1, skip);
        }
    }

    private static void storeANode(PrintWriter writer, DirectoryTreeNode node, int depth, int skip) {
        String path = node.getDirectory().getAbsolutePath();
        writer.print(depth);
        writer.print("\t*");
        if (skip < path.length()) {
            writer.print(path.substring(skip));
        } else {
            writer.print(File.separator);
        }
        writer.print("\t");
        writer.print(node.myNodeDirCount);
        writer.print("\t");
        writer.print(node.myNodeAllFileCount);
        writer.print("\t");
        writer.print(node.myNodeAllFileSize);
        writer.print("\t");
        writer.print(node.myNodeTargetFileCount);
        writer.print("\t");
        writer.print(node.myNodeTargetFileSize);
        writer.println("");
    }
}
