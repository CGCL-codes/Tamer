package com.ngenes.mapMaker.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import com.ngenes.mapMaker.Cache;
import com.ngenes.mapMaker.MapMaker;
import com.ngenes.mapMaker.PrimitivMapComparator;
import com.ngenes.mapMaker.Settings;
import com.ngenes.mapMaker.gui.panel.InfoPanel;
import com.ngenes.mapMaker.gui.panel.MapGraphicPanel;
import com.ngenes.mapMaker.gui.panel.MapTreePanel;
import com.ngenes.mapMaker.gui.panel.TileGraphicPanel;
import com.ngenes.mapMaker.gui.panel.ToolPanel;
import com.ngenes.mapMaker.gui.window.DialogFactory;
import com.ngenes.mapMaker.gui.window.MainWindow;
import com.ngenes.util.Tool;

public class MapMakerIO {

    private static MapMakerIO mInstance;

    private Cache mCache;

    private Settings mSettings;

    private XMLWriter mXMLWriter;

    private XMLReader mXMLReader;

    private MapMakerIO() {
        mCache = Cache.getInstance();
        mSettings = Settings.getInstance();
        mXMLWriter = new XMLWriter();
        mXMLReader = new XMLReader();
    }

    public static MapMakerIO getInstance() {
        if (mInstance == null) return mInstance = new MapMakerIO(); else return mInstance;
    }

    public void loadProject(File projectFolder) throws Exception {
        if (!projectFolder.exists()) {
            throw new Exception("projectfolder does not exist!");
        }
        if (!projectFolder.isDirectory()) {
            throw new Exception("projectfolder is no folder!");
        }
        File[] mapFiles = Tool.getFilesInDir(new File(projectFolder.toString() + "\\" + Settings.MAPDIR));
        if (mapFiles == null) {
            throw new IOException("Could not find any Files in projectFolder: \"" + projectFolder + "\"");
        }
        Vector<PrimitivMap> vec = new Vector<PrimitivMap>();
        for (int i = 0; i < mapFiles.length; i++) {
            vec.add(mXMLReader.getPrimitivMap(mapFiles[i]));
        }
        Collections.sort(vec, new PrimitivMapComparator());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(projectFolder.getName());
        addChildren(root, 0, vec);
        MapTreePanel.getInstance().setRoot(root, true);
        mCache.setProjectFolder(projectFolder);
        mCache.loadBgImages(projectFolder.toString() + "\\" + Settings.IMAGEDIR);
        mCache.loadTileSets(projectFolder.toString() + "\\" + Settings.TILESETDIR);
        MainWindow.getInstance().setTitle();
        String fileString = Tool.getStringFromFile(new File(projectFolder + Settings.PROJECTINIFILE), "lastMap", "=");
        fileString = projectFolder.toString() + "\\" + Settings.MAPDIR + "\\" + fileString;
        if (fileString != null && !fileString.equals("null")) {
            File mapFile = new File(fileString);
            if (mapFile.exists()) {
                loadMap(mapFile);
            } else if (MapMaker.isInstanced()) {
                MapMaker.getInstance().blankScreen();
            }
        } else if (MapMaker.isInstanced()) {
            MapMaker.getInstance().blankScreen();
        }
    }

    public void loadMap(File mapFile) throws Exception {
        if (mCache.getWorkFile() != null && mSettings.isMapChanged()) {
            int res = DialogFactory.saveDialog(null);
            if (res == JOptionPane.OK_OPTION) {
                saveProcess();
            }
        }
        TileGraphicPanel.getInstance().clear();
        MapGraphicPanel.getInstance().clear();
        MapGraphicPanel.getInstance().setStatusMessage("loading Map...");
        File doctype = new File(Settings.MAPDTDFILE);
        if (!doctype.exists()) {
            mXMLWriter.writeXML_MapDoctype(doctype);
        }
        mXMLReader.parseXmlFile(mapFile);
        mXMLReader.parseMapDocument(mapFile);
        mSettings.setEditable(true);
        TileGraphicPanel.getInstance().init();
        MapGraphicPanel.getInstance().enable();
        ToolPanel.getInstance().enableEditFunctions(true);
        InfoPanel.getInstance().enableMapInfo(true);
        MainWindow.getInstance().setTitle();
        mCache.clearTempLists();
        mCache.setWorkFile(mapFile);
        File stampFile = new File(mCache.getProjectFolder() + Settings.STAMPSFILE);
        if (stampFile.exists()) loadStampList(stampFile);
    }

    public void loadStampList(File stampFile) {
        File stampDTD = new File(Settings.STAMPDTDFILE);
        if (!stampDTD.exists()) {
            try {
                mXMLWriter.writeXML_StampListDoctype(stampDTD);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            mXMLReader.parseXmlFile(stampFile);
            mXMLReader.parseStampDocument();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File newFile(File save, int number) {
        if (save.getName().lastIndexOf(").") != -1) {
            save = new File(save.getParent() + "/" + save.getName().substring(0, save.getName().lastIndexOf("(")) + ".xml");
        }
        File file = new File(save.getParent() + "/" + save.getName().substring(0, save.getName().lastIndexOf(".")) + "(" + number + ").xml");
        if (file.exists()) file = newFile(save, ++number);
        return file;
    }

    /**
	 * stellt sicher, dass der abzuspeichernde File<br/>
	 * wirklich abgespeichert wird, auch wenn ein gleicher<br/>
	 * File existiert. Der abzuspeichernde File bekommt<br/>
	 * dann einfach einen Zusatz.<br/>
	 * Zum Beispiel:<br/>
	 * Es existiert der File <b>haus.xml</b> und man will <br/>
	 * noch einen File <b>haus.xml</b> im gleichen Ordner abspeichern.<br/>
	 * In diesem Fall wird der File als <b>haus(1).xml</b> gespeichert.
	 * @param save
	 * @param number
	 * @return File mit eventuell ver�ndertem Namen.
	 */
    public File newFile(File save) {
        if (save.exists()) return newFile(save, 1);
        return save;
    }

    /**
	 * versucht den angegebenen MapFile zu speichern.
	 * @param save 
	 */
    public boolean saveMap(File save) {
        try {
            mXMLWriter.writeMap(save);
            copyResources();
            mCache.setWorkFile(save);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (save.canWrite()) {
                save.delete();
            }
            DialogFactory.saveMapError(null);
            return false;
        }
    }

    /**
	 * kopiert alle in der Map benutzten Ressourcen in 
	 * die entsprechenden Ordner des Projekts
	 */
    private void copyResources() {
        File bgImageFile = mCache.getBgImageFile();
        File copyBgImageFile = createProjectImageFile(bgImageFile.getName());
        if (bgImageFile != null && bgImageFile.exists() && !bgImageFile.isDirectory()) {
            if (!copyBgImageFile.exists()) {
                Tool.copyfile(bgImageFile, copyBgImageFile);
            }
        }
        File tileSetFile = mCache.getTileSet();
        File copytileSetFile = createProjectTileSetFile(tileSetFile.getName());
        if (tileSetFile != null && tileSetFile.exists() && !tileSetFile.isDirectory()) {
            if (!copytileSetFile.exists()) {
                Tool.copyfile(tileSetFile, copytileSetFile);
            }
        }
    }

    /**
	 * erzeugt ein neues Projektverzeichnis und
	 * gibt dieses als File zur�ck.
	 * @return projectFile
	 */
    public File createProject() {
        String curDirString = Tool.getStringFromFile(MapMakerIO.getMapMakerFile(), "ProjectDir", "=");
        if (curDirString == null || curDirString.equals("null")) {
            curDirString = ".";
        }
        File curDir = new File(curDirString);
        if (!curDir.exists()) {
            curDir = new File(".");
        }
        JFileChooser dirChooser = new JFileChooser(curDir);
        dirChooser.setDialogTitle("Name directory to save Project");
        dirChooser.setApproveButtonText("Save");
        dirChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File arg0) {
                if (arg0.isDirectory()) return true; else return false;
            }

            @Override
            public String getDescription() {
                return "Project Folder";
            }
        });
        int result = dirChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File projectDir = new File(Tool.cleanFromSpaces(Tool.validateFileName(dirChooser.getSelectedFile().toString(), "")));
            File mapDir = new File(projectDir.getPath() + "/" + Settings.MAPDIR);
            File imgDir = new File(projectDir.getPath() + "/" + Settings.IMAGEDIR);
            File tileSetDir = new File(projectDir.getPath() + "/" + Settings.TILESETDIR);
            try {
                if (!projectDir.mkdir() || !mapDir.mkdir() || !imgDir.mkdir() || !tileSetDir.mkdir()) {
                    throw new IOException("Couldn't create Folders: \"" + projectDir + "\" and \"" + mapDir);
                }
                saveProjectFile(projectDir);
                mCache.setProjectFolder(projectDir);
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(projectDir.getName());
                MapTreePanel.getInstance().setRoot(root, true);
                MapTreePanel.getInstance().getTree().updateUI();
                MainWindow.getInstance().setTitle();
                return projectDir;
            } catch (IOException e1) {
                DialogFactory.createProjectError(null);
                return null;
            }
        }
        return null;
    }

    public static File createProjectImageFile(String imageName) {
        if (imageName == null) {
            return null;
        }
        return new File(Cache.getInstance().getProjectFolder().toString() + "\\" + Settings.IMAGEDIR + "\\" + imageName);
    }

    public static File createProjectTileSetFile(String tileSetName) {
        if (tileSetName == null) {
            return null;
        }
        return new File(Cache.getInstance().getProjectFolder().toString() + "\\" + Settings.TILESETDIR + "\\" + tileSetName);
    }

    private void addChildren(DefaultMutableTreeNode parent, int generation, Vector<PrimitivMap> vec) {
        DefaultMutableTreeNode child;
        Vector<DefaultMutableTreeNode> newGeneration = new Vector<DefaultMutableTreeNode>();
        Iterator<PrimitivMap> iter = vec.iterator();
        while (iter.hasNext()) {
            PrimitivMap m = iter.next();
            if (m.getParentID() == generation) {
                child = new DefaultMutableTreeNode(m);
                parent.add(child);
                newGeneration.add(child);
            }
        }
        Iterator<DefaultMutableTreeNode> iter2 = newGeneration.iterator();
        while (iter2.hasNext()) {
            child = iter2.next();
            PrimitivMap m = (PrimitivMap) child.getUserObject();
            vec.remove(m);
            addChildren(child, m.getID(), vec);
        }
        vec.clear();
    }

    public boolean saveProjectFile(File projectFolder) throws IOException {
        FileWriter fw = new FileWriter(new File(projectFolder.getPath() + Settings.PROJECTINIFILE));
        BufferedWriter bw = new BufferedWriter(fw);
        String lastMap = "null";
        if (mCache.getWorkFile() != null) {
            lastMap = mCache.getWorkFile().getName();
        }
        bw.write("lastMap=" + lastMap);
        bw.newLine();
        bw.close();
        saveMapMakerFile(projectFolder);
        return true;
    }

    public static File saveMapMakerFile(File projectFolder) throws IOException {
        File mapMakerFile = new File(Settings.INIFILE);
        FileWriter fw = new FileWriter(mapMakerFile);
        BufferedWriter bw = new BufferedWriter(fw);
        String projectFolderString = "";
        if (projectFolder != null && projectFolder.exists()) {
            projectFolderString = projectFolder.toString();
        }
        bw.write("ProjectDir=" + Tool.cleanFromSpaces(projectFolder.getParent()));
        bw.newLine();
        bw.write("HOME=" + Tool.cleanFromSpaces(new File(mapMakerFile.toURI().getPath()).getParent()));
        bw.newLine();
        bw.write("lastProject=" + projectFolderString);
        bw.newLine();
        bw.close();
        return mapMakerFile;
    }

    public void saveProcess() {
        if (mCache.getProjectFolder() == null) {
            if (createProject() == null) {
                DialogFactory.createProjectError(null);
                return;
            }
        }
        boolean overwrite = false;
        int res;
        if (mCache.getWorkFile() == null) overwrite = false; else {
            res = JOptionPane.OK_OPTION;
            if (res == JOptionPane.OK_OPTION) overwrite = true; else if (res == JOptionPane.NO_OPTION) return; else return;
        }
        if (!overwrite) {
            File curDir = mCache.getProjectFolder();
            if (mSettings.getMapName() != null) {
                File mapFile = new File(curDir.getPath() + "/" + Settings.getInstance().getMapName());
                saveMap(mapFile);
            }
        } else {
            saveMap(mCache.getWorkFile());
        }
        try {
            saveProjectFile(mCache.getProjectFolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveStampList();
        mSettings.setProjectChanged(false);
    }

    public void saveStampList() {
        File stampDTD = new File(Settings.STAMPDTDFILE);
        if (!stampDTD.exists()) {
            try {
                mXMLWriter.writeXML_StampListDoctype(stampDTD);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        File stampFile = new File(mCache.getProjectFolder() + Settings.STAMPSFILE);
        try {
            mXMLWriter.writeStampList(stampFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * returns the mapMaker.ini File. Creates a new
	 * mapMaker.ini File if it does not exist
	 * and also creates a new mapMaker.ini File
	 * if the HOME path within the mapMaker.ini File
	 * does not exist
	 * @return an existing and well-formed mapMaker.ini File
	 */
    public static File getMapMakerFile() {
        File homeFile = new File(Settings.INIFILE);
        if (!homeFile.exists()) {
            try {
                return saveMapMakerFile(new File("."));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String homeString = Tool.getStringFromFile(homeFile, "HOME", "=");
            File existTest;
            if (homeString != null && !homeString.equals("null")) {
                existTest = new File(homeString);
            } else {
                existTest = null;
            }
            if (existTest != null && existTest.exists()) {
                return homeFile;
            } else {
                try {
                    return saveMapMakerFile(new File("."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
