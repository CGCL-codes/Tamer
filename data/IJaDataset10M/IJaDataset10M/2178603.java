package vademecum.visualiser.umatrix.colors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColorMapChooser {

    /**
	 * Logger instance
	 */
    private static final Log log = LogFactory.getLog(ColorMapChooser.class);

    private final File directory = new File("data" + File.separator + "colors");

    private Vector<ColorMap> myColors = new Vector<ColorMap>();

    FileReader fr;

    BufferedReader br;

    private int actMap = 0;

    public ColorMapChooser() {
        if (!directory.exists()) {
            System.out.println("Couldn't initialise ColorMapChooser-Object because directory \"" + directory.getPath() + "\" doesn't exist!");
            return;
        }
        String[] tempFileList = directory.list();
        Vector<String> fileList = new Vector<String>();
        System.out.println("Searching *.rgb-Files in:");
        System.out.println(directory.getAbsolutePath());
        System.out.println();
        for (String s : tempFileList) {
            if (s.endsWith(".rgb")) {
                System.out.println(s);
                fileList.add(s);
            }
        }
        System.out.println();
        System.out.println(String.valueOf(fileList.size()) + " files found.");
        for (String s : fileList) {
            try {
                this.myColors.add(new ColorMap(s.substring(0, s.length() - 4)));
                log.debug(">" + myColors.lastElement().getName() + "<");
                fr = new FileReader(this.directory.getPath() + File.separator + s);
                br = new BufferedReader(fr);
                String line = br.readLine();
                while (line != null) {
                    StringTokenizer buffer = new StringTokenizer(line);
                    if (buffer.countTokens() == 3) {
                        int red = Integer.parseInt(buffer.nextToken());
                        int green = Integer.parseInt(buffer.nextToken());
                        int blue = Integer.parseInt(buffer.nextToken());
                        myColors.get(myColors.size() - 1).addColor(red, green, blue);
                    } else {
                        System.out.println("Corrupted color!!!");
                    }
                    line = br.readLine();
                }
                System.out.println(myColors.get(myColors.size() - 1).toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean setColorMap(String name) {
        int index = 0;
        for (int i = 0; i < this.myColors.size(); i++) {
            log.debug("checking \"" + this.myColors.get(i).getName() + "\"");
            if (this.myColors.get(i).getName().equals(name)) {
                this.actMap = i;
                log.debug("ColorMap set to " + this.myColors.get(i).getName());
                return true;
            }
        }
        return false;
    }

    public Vector<Integer> getRGBValues(int index) {
        return this.myColors.get(this.actMap).getColor(index);
    }

    public int numOfValues() {
        return this.myColors.get(this.actMap).numOfValues();
    }

    public Vector<Float> getFloatRGBValues(int index) {
        return this.myColors.get(this.actMap).getColorAsFloat(index);
    }

    public Vector<String> getColorMapNames() {
        Vector<String> erg = new Vector<String>();
        for (ColorMap map : this.myColors) {
            erg.add(map.getName());
        }
        return erg;
    }
}
