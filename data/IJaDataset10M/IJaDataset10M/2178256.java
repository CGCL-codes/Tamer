package src.gui;

import src.backend.*;
import javax.swing.JFrame;

public class Model {

    public Model() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private FlagsFrame jf;

    private MapData md;

    private String currentFile;

    private String rdMapFileErr = "Problem reading map file";

    public Model(FlagsFrame jf) {
        this.jf = jf;
    }

    public boolean newMapFile() {
        md = null;
        md = new MapData();
        md.addLevel(new Level(md));
        return true;
    }

    public boolean openMapFile(String openFile) {
        try {
            currentFile = openFile;
            md = null;
            md = new MapData(openFile);
            if (!md.getMapWriter().getFileLoaded()) return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public MapData getMapData() {
        try {
            return md;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean[] loadGametypeCheckboxInfo(int levelIndex) {
        try {
            boolean[] flagValues = new boolean[8];
            if (!md.getMapWriter().getFileLoaded() || !md.getIsValidMap()) {
                for (int i = 0; i < 8; i++) flagValues[i] = false;
                return flagValues;
            }
            short flags = md.getLevel(levelIndex).getFlags();
            for (int j = 0; j < 8; j++) {
                flagValues[j] = ((flags & (1 << j)) != 0);
            }
            return flagValues;
        } catch (Exception e) {
            System.out.println("Couldn't load gametype info - Level " + levelIndex);
            e.printStackTrace();
            return null;
        }
    }

    public boolean[] loadMissionCheckboxInfo(int levelIndex) {
        try {
            boolean[] flagValues = new boolean[5];
            if (!md.getMapWriter().getFileLoaded() || !md.getIsValidMap()) {
                for (int i = 0; i < 5; i++) flagValues[i] = false;
                return flagValues;
            }
            short flags = ((Level) md.getLevels().get(levelIndex)).getMinfChunk().getMission();
            for (int i = 0; i < 5; i++) {
                flagValues[i] = ((flags & (1 << i)) != 0);
            }
            return flagValues;
        } catch (Exception e) {
            System.out.println("Couldn't load mission info - Level " + levelIndex);
            return null;
        }
    }

    public boolean[] loadEnvCheckboxInfo(int levelIndex) {
        try {
            boolean[] flagValues = new boolean[4];
            if (!md.getMapWriter().getFileLoaded() || !md.getIsValidMap()) {
                for (int i = 0; i < 4; i++) flagValues[i] = false;
                return flagValues;
            }
            short flags = ((Level) md.getLevels().get(levelIndex)).getMinfChunk().getEnvironmentFlags();
            for (int i = 0; i < 4; i++) {
                flagValues[i] = ((flags & (1 << i)) != 0);
            }
            return flagValues;
        } catch (Exception e) {
            System.out.println("Couldn't load environment info - Level " + levelIndex);
            return null;
        }
    }

    public void saveGametypeCheckboxInfo(int levelIndex, boolean[] flagValues) {
        int[] flagTypes = { 0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80 };
        int flags = 0;
        for (int j = 0; j < 8; j++) {
            if (flagValues[j]) {
                flags = flags | flagTypes[j];
            }
        }
        try {
            md.getLevel(levelIndex).setFlags((short) flags);
            md.getLevel(levelIndex).getMinfChunk().setEntry(flags);
        } catch (NullPointerException e) {
            System.out.println("Couldn't save gametype info - Level " + levelIndex);
            return;
        }
    }

    public void saveMissionCheckboxInfo(int levelIndex, boolean[] missionValues) {
        int[] flagTypes = { 0x1, 0x2, 0x4, 0x8, 0x10 };
        int flags = 0;
        for (int i = 0; i < 5; i++) {
            if (missionValues[i]) {
                flags = flags | flagTypes[i];
            }
        }
        try {
            md.getLevel(levelIndex).getMinfChunk().setMission((short) flags);
        } catch (NullPointerException e) {
            System.out.println("Couldn't save mission info - Level " + levelIndex);
            return;
        }
    }

    public void saveEnvironmentCheckboxInfo(int levelIndex, boolean[] envValues) {
        int[] flagTypes = { 0x1, 0x2, 0x4, 0x8 };
        int flags = 0;
        for (int i = 0; i < 4; i++) {
            if (envValues[i]) {
                flags = flags | flagTypes[i];
            }
        }
        try {
            md.getLevel(levelIndex).getMinfChunk().setEnvironmentFlags((short) flags);
        } catch (NullPointerException e) {
            System.out.println("Couldn't save environment info - Level " + levelIndex);
            return;
        }
    }

    public void writeData() {
        if (currentFile == null) {
            System.out.println("No file to write to");
            return;
        }
        try {
            md.write();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed writing data");
            return;
        }
    }

    public void writeNew(String saveFile) {
        if (saveFile == null) {
            System.out.println("No file to write to");
            return;
        }
        try {
            md.writeNew(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed writing data");
            return;
        }
    }

    public PlacEntry getPlacItem(int levelIndex, int itemIndex) {
        try {
            return md.getLevel(levelIndex).getPlacChunk().getEntry(itemIndex);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public int getSelectedLevelIndex() {
        try {
            return jf.getLevelIndex();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setLevelSelectorNames() {
        try {
            jf.fillLevelSelector();
        } catch (Exception e) {
            return;
        }
    }

    public String getLevelSelectName() {
        try {
            return md.getLevel(getSelectedLevelIndex()).getlevelSelectName();
        } catch (NullPointerException e) {
            System.out.println(rdMapFileErr);
            return null;
        }
    }

    public String getLevelSelectName(int index) {
        try {
            if (md.getLevel(index).getlevelSelectName().equals("")) return md.getLevel(index).getMinfChunk().getName(); else return md.getLevel(index).getlevelSelectName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setLevelSelectName(String name, int index) {
        try {
            md.getLevel(index).setLevelSelectName(name);
        } catch (NullPointerException e) {
            return;
        }
    }

    public String getLevelOverHeadName(int index) {
        try {
            return md.getLevel(index).getMinfChunk().getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setLevelOverHeadName(String name, int index) {
        try {
            md.getLevel(index).getMinfChunk().setName(name);
        } catch (NullPointerException e) {
            return;
        }
    }

    public short getLandscape(int levelIndex) {
        try {
            return md.getLevel(levelIndex).getMinfChunk().getLandscape();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public short getEnvironment(int levelIndex) {
        try {
            return md.getLevel(levelIndex).getMinfChunk().getEnvironmentCode();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public void setLandscape(int levelIndex, short newLandscape) {
        try {
            md.getLevel(levelIndex).getMinfChunk().setLandscape(newLandscape);
        } catch (Exception e) {
        }
    }

    public short getNumLevels() {
        try {
            return md.getNumLevels();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public void addShapesPatch(int levelIndex, String path) throws NotShPaException, TooLongException {
        try {
            ShPaChunk c = new ShPaChunk(path);
            md.getLevel(levelIndex).addChunk(c);
        } catch (NotShPaException notShPa) {
            MessageBox m = new MessageBox("This is not a valid shapes patch");
            throw new NotShPaException();
        } catch (TooLongException tooLong) {
            MessageBox m = new MessageBox("The shapes file is too large to add");
            throw new TooLongException();
        } catch (Exception e) {
            return;
        }
    }

    public void merge(String path, String outputPath) {
        try {
            Merge merge = new Merge(path, outputPath);
        } catch (Exception e) {
        }
    }

    public String getComment(int level) {
        try {
            return md.getLevel(level).getJuceChunk().getComment();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public void setComment(int level, String msg) {
        try {
            md.getLevel(level).getJuceChunk().setComment(msg);
        } catch (NullPointerException e) {
            try {
                Level mapLevel = md.getLevel(level);
                JuceChunk juceChunk = new JuceChunk(msg);
                mapLevel.addChunk(juceChunk);
            } catch (NullPointerException ex) {
                return;
            }
        }
    }

    public PlacChunk getPlacChunk(int level) {
        try {
            return md.getLevel(level).getPlacChunk();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public ObjsChunk getObjsChunk(int level) {
        try {
            return md.getLevel(level).getObjsChunk();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public PolyEntry getPoly(int level, int num) {
        try {
            return md.getLevel(level).getPolyChunk().getEntry(num);
        } catch (NullPointerException e) {
            return new PolyEntry(new PolyChunk());
        }
    }

    public int getPolyNumber(int level) {
        try {
            return md.getLevel(level).getPolyChunk().getNumEntries();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public double getPolygonCenterX(int level, int poly) {
        try {
            return md.getLevel(level).getPolyChunk().getEntry(poly).getX();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public double getPolygonCenterY(int level, int poly) {
        try {
            return md.getLevel(level).getPolyChunk().getEntry(poly).getY();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public int getNumObjs(int level) {
        try {
            return md.getLevel(level).getObjsChunk().getNumEntries();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public ObjsEntry getObjsItem(int level, int num) {
        try {
            return md.getLevel(level).getObjsChunk().getEntry(num);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public ObjsEntry addObject(int level) {
        try {
            return md.getLevel(level).getObjsChunk().addEntry();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void addObject(int level, ObjsEntry entry) {
        try {
            md.getLevel(level).getObjsChunk().addEntry(entry);
        } catch (NullPointerException e) {
            return;
        }
    }

    public void removeEntry(int level, ObjsEntry gone) {
        try {
            md.getLevel(level).getObjsChunk().removeEntry(gone);
        } catch (NullPointerException e) {
            return;
        }
    }

    public int getLinsNumber(int level) {
        try {
            return md.getLevel(level).getLinsChunk().getNumEntries();
        } catch (Exception e) {
            return 0;
        }
    }

    public LinsEntry getLin(int level, int index) {
        try {
            return md.getLevel(level).getLinsChunk().getEntry(index);
        } catch (NullPointerException e) {
            return new LinsEntry(new LinsChunk());
        }
    }

    public int getSidsNumber(int level) {
        try {
            return md.getLevel(level).getSidsChunk().getNumEntries();
        } catch (Exception e) {
            return 0;
        }
    }

    public SidsEntry getSide(int level, int index) {
        try {
            return md.getLevel(level).getSidsChunk().getEntry(index);
        } catch (Exception e) {
            return new SidsEntry(new SidsChunk());
        }
    }

    public boolean isOptimized() {
        try {
            return md.isOptimized();
        } catch (Exception e) {
            return false;
        }
    }

    public void convertTextures(int level, int col1, int bmp1, int xfer1, int cp1, int col2, int bmp2, int xfer2, int cp2) {
        try {
            md.getLevel(level).convertLevelTextures(col1, bmp1, xfer1, cp1, col2, bmp2, xfer2, cp2);
        } catch (Exception e) {
            return;
        }
    }

    public void convertTextures(int level, int col1, int bmp1, int xfer1, int col2, int bmp2, int xfer2) {
        try {
            md.getLevel(level).convertLevelTextures(col1, bmp1, xfer1, col2, bmp2, xfer2);
        } catch (Exception e) {
            return;
        }
    }

    public void convertTextures(int level, int col1, int bmp1, int col2, int bmp2) {
        try {
            md.getLevel(level).convertLevelTextures(col1, bmp1, col2, bmp2);
        } catch (Exception e) {
            return;
        }
    }

    public void convertTextures(int level) {
        try {
            md.getLevel(level).convertLevelTextures();
        } catch (Exception e) {
            return;
        }
    }

    public void removeChildFrame(JFrame window) {
        try {
            jf.removeChildFrame(window);
        } catch (Exception e) {
            return;
        }
    }

    public void addChildFrame(JFrame window) {
        try {
            jf.addChildFrame(window);
        } catch (Exception e) {
            return;
        }
    }

    public void turnProgOff() {
        try {
            jf.turnProgOff();
        } catch (Exception e) {
            return;
        }
    }

    public void initLoad() {
        try {
            jf.initLoad();
        } catch (Exception e) {
            return;
        }
    }

    public boolean getIsMerged() {
        try {
            return md.isMergedMapFile();
        } catch (Exception e) {
            return false;
        }
    }

    private void jbInit() throws Exception {
    }
}
