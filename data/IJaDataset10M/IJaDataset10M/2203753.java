package jir.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class DatasetEntry {

    public Vector<File> pendingFiles = new Vector<File>();

    public boolean isListed;

    private String key, segName;

    private File imageFile, groundTruthFile;

    private Vector<DatasetEntry> groundTruths = new Vector<DatasetEntry>();

    private Segmentation segmentation;

    private SegmentationPolygons segmentationPolygons;

    private boolean imageOnly;

    private boolean isolated;

    public DatasetEntry() {
        this("");
    }

    public DatasetEntry(String key) {
        this.key = key;
        pendingFiles.clear();
        this.imageOnly = false;
        this.isolated = false;
    }

    public DatasetEntry(String key, boolean imageOnly) {
        this.key = key;
        pendingFiles.clear();
        this.imageOnly = imageOnly;
        this.isolated = true;
    }

    public void processFiles(HashMap<String, DatasetEntry> listDataset, HashMap<String, File> listSeg, HashMap<String, File> listSegPol) {
        if (this.groundTruthFile != null) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(this.groundTruthFile));
                try {
                    String line;
                    while (true) {
                        line = in.readLine();
                        if (line == null) break;
                        if (line.equals(this.key)) continue;
                        if (listDataset.containsKey(line)) {
                            listDataset.get(line).reference();
                            this.groundTruths.add(listDataset.get(line));
                        }
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (listSeg.containsKey(this.key + "_" + this.segName)) {
            this.segmentation = processSegmentation(listSeg.get(this.key + "_" + this.segName));
        }
        if (listSeg.containsKey(this.key + "_" + this.segName)) {
            this.segmentationPolygons = processSegmentationPolygons(listSeg.get(this.key + "_" + this.segName));
        }
        for (File file : this.pendingFiles) {
            System.err.println("Unexpected file format: " + file);
        }
    }

    private SegmentationPolygons processSegmentationPolygons(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(this.groundTruthFile));
            try {
                in.close();
                return new SegmentationPolygons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Segmentation processSegmentation(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(this.groundTruthFile));
            try {
                in.close();
                return new Segmentation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSegmentName(String segName) {
        this.segName = segName;
    }

    public boolean setImage(File file) {
        if (this.imageFile != null) return false;
        this.imageFile = file;
        return true;
    }

    public boolean setGroundTruth(File file) {
        if (this.groundTruthFile != null) return false;
        this.groundTruthFile = file;
        return true;
    }

    public boolean isOkay() {
        boolean okayState = true;
        if (this.imageFile == null) {
            Logger.out.println("ERROR:	Image file is missing for entry " + this.key);
            okayState = false;
        }
        if (this.segmentation == null) {
            if (this.imageOnly) {
                if (this.isolated) Logger.out.println("NOTICE:	Dataset entry " + this.key + " is an isolated image.");
            } else {
                Logger.out.println("ERROR:	Segmentation data is missing for entry " + this.key);
                okayState = false;
            }
        }
        if (this.segmentationPolygons == null) {
            if (!this.imageOnly) {
                Logger.out.println("NOTICE:	Segmentation polygons data is missing for entry " + this.key);
            }
        }
        return okayState;
    }

    public boolean isIsolated() {
        return this.imageOnly && this.isolated;
    }

    public void reference() {
        this.isolated = false;
    }

    public String getKey() {
        return this.key;
    }
}
