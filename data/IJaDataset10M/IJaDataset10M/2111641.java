package edu.unc.safegui.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Myroslav Sypa
 */
public class StoredData {

    private Color p01Color = Color.RED;

    private Color p015Color = Color.GREEN;

    private Color p02Color = Color.BLUE;

    private Color pOtherColor = Color.BLACK;

    private Color plotColor = Color.WHITE;

    private Color plotLineColor = Color.RED;

    private Color plotShadedAreaColorLeft = Color.RED;

    private Color plotShadedAreaColorRight = Color.GREEN;

    private Color plotShadedAreaColorOne = Color.GRAY;

    private double p01 = 0.1;

    private double p015 = 0.15;

    private double p02 = 0.2;

    private boolean isRandom = true;

    private int seed = 12345;

    private String cat = "";

    private String selectedRow;

    private String delimiter = " ,";

    private TreeMap<String, Integer> platforms;

    private static StoredData instance = null;

    public static StoredData getInstance() {
        if (instance == null) instance = new StoredData();
        return instance;
    }

    public Color getP015Color() {
        return p015Color;
    }

    public void setP015Color(Color p015Color) {
        try {
            this.p015Color = p015Color;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getP01Color() {
        return p01Color;
    }

    public void setP01Color(Color p01Color) {
        try {
            this.p01Color = p01Color;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getP02Color() {
        return p02Color;
    }

    public void setP02Color(Color p02Color) {
        try {
            this.p02Color = p02Color;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getPOtherColor() {
        return pOtherColor;
    }

    public void setPOtherColor(Color pOtherColor) {
        try {
            this.pOtherColor = pOtherColor;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getPlotColor() {
        return plotColor;
    }

    public void setPlotColor(Color plotColor) {
        try {
            this.plotColor = plotColor;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPlotLineColor(Color bgColor) {
        try {
            this.plotLineColor = bgColor;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getPlotLineColor() {
        return this.plotLineColor;
    }

    public Color getPlotShadedAreaColorLeft() {
        return plotShadedAreaColorLeft;
    }

    public void setPlotShadedAreaColorLeft(Color plotShadedAreaColorLeft) {
        try {
            this.plotShadedAreaColorLeft = plotShadedAreaColorLeft;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Color getPlotShadedAreaColorRight() {
        return plotShadedAreaColorRight;
    }

    public Color getPlotShadedAreaColorOne() {
        return plotShadedAreaColorOne;
    }

    public void setPlotShadedAreaColorRight(Color plotShadedAreaColorRight) {
        try {
            this.plotShadedAreaColorRight = plotShadedAreaColorRight;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPlotShadedAreaColorOne(Color plotShadedAreaColorRight) {
        try {
            this.plotShadedAreaColorOne = plotShadedAreaColorRight;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getP01() {
        return p01;
    }

    public void setP01(double p01) {
        try {
            this.p01 = p01;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getP015() {
        return p015;
    }

    public void setP015(double p015) {
        try {
            this.p015 = p015;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getP02() {
        return p02;
    }

    public void setP02(double p02) {
        try {
            this.p02 = p02;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isIsRandom() {
        return isRandom;
    }

    public void setIsRandom(boolean isRandom) {
        try {
            this.isRandom = isRandom;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        try {
            this.seed = seed;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(String selectedRow) {
        this.selectedRow = selectedRow;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        try {
            this.delimiter = delimiter;
            storeData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeData() throws FileNotFoundException {
        BufferedWriter output = null;
        try {
            File aFile = new File("utils/userData.dat");
            if (!aFile.exists()) {
                aFile.createNewFile();
            }
            if (!aFile.canWrite()) {
                throw new IllegalArgumentException("File cannot be written: " + aFile);
            }
            output = new BufferedWriter(new FileWriter(aFile));
            try {
                output.write(new Integer(getP01Color().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getP015Color().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getP02Color().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPOtherColor().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPlotColor().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPlotLineColor().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPlotShadedAreaColorLeft().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPlotShadedAreaColorRight().getRGB()).toString());
                output.newLine();
                output.write(new Integer(getPlotShadedAreaColorOne().getRGB()).toString());
                output.newLine();
                output.write(new Double(getP01()).toString());
                output.newLine();
                output.write(new Double(getP015()).toString());
                output.newLine();
                output.write(new Double(getP02()).toString());
                output.newLine();
                output.write(new Boolean(isIsRandom()).toString());
                output.newLine();
                output.write(new Integer(getSeed()).toString());
                output.newLine();
                output.write(getDelimiter());
                output.newLine();
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void readUserDataFromFile() {
        try {
            if (new File("utils/userData.dat").length() > 0) {
                FileReader fr = new FileReader("utils/userData.dat");
                BufferedReader input = new BufferedReader(fr);
                try {
                    setP01Color(new Color(new Integer(input.readLine())));
                    setP015Color(new Color(new Integer(input.readLine())));
                    setP02Color(new Color(new Integer(input.readLine())));
                    setPOtherColor(new Color(new Integer(input.readLine())));
                    setPlotColor(new Color(new Integer(input.readLine())));
                    setPlotLineColor(new Color(new Integer(input.readLine())));
                    setPlotShadedAreaColorLeft(new Color(new Integer(input.readLine())));
                    setPlotShadedAreaColorRight(new Color(new Integer(input.readLine())));
                    setPlotShadedAreaColorOne(new Color(new Integer(input.readLine())));
                    setP01(new Double(input.readLine()).doubleValue());
                    setP015(new Double(input.readLine()).doubleValue());
                    setP02(new Double(input.readLine()).doubleValue());
                    setIsRandom(new Boolean(input.readLine()).booleanValue());
                    setSeed(new Integer(input.readLine()));
                    setDelimiter(input.readLine());
                } finally {
                    input.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void storePlatform(String platform) throws FileNotFoundException {
        BufferedWriter output = null;
        try {
            File aFile = new File("utils/platformsData.dat");
            if (!aFile.exists()) {
                aFile.createNewFile();
            }
            if (!aFile.canWrite()) {
                throw new IllegalArgumentException("File cannot be written: " + aFile);
            }
            output = new BufferedWriter(new FileWriter(aFile));
            try {
                if (platforms.containsKey(platform)) {
                    Integer ii = (Integer) platforms.get(platform);
                    ii++;
                    platforms.put(platform, ii);
                } else platforms.put(platform, 1);
                for (int i = 0; i < platforms.size(); i++) {
                    String s = (String) platforms.keySet().toArray()[i];
                    s += "=";
                    s += ((Integer) platforms.values().toArray()[i]).toString();
                    output.write(s);
                    output.newLine();
                }
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(StoredData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void readPlatforms() {
        try {
            if (new File("utils/platformsData.dat").length() > 0) {
                FileReader fr = new FileReader("utils/platformsData.dat");
                BufferedReader input = new BufferedReader(fr);
                try {
                    String s = "";
                    platforms = new TreeMap<String, Integer>();
                    while ((s = input.readLine()) != null) {
                        platforms.put(s.substring(0, s.indexOf("=")), new Integer(s.substring(s.indexOf("=") + 1, s.length())));
                    }
                    input.readLine();
                } finally {
                    input.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String[] getPlatforms() {
        String[] res = new String[5];
        TreeSet set = new TreeSet(new Comparator() {

            public int compare(Object obj, Object obj1) {
                int vcomp = ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
                if (vcomp != 0) {
                    return vcomp;
                } else {
                    return ((Comparable) ((Map.Entry) obj1).getKey()).compareTo(((Map.Entry) obj).getKey());
                }
            }
        });
        set.addAll(platforms.entrySet());
        int ii = 0;
        for (Iterator i = set.iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            res[ii] = (String) entry.getKey();
            ii++;
            if (ii == 5) {
                break;
            }
        }
        return res;
    }
}
