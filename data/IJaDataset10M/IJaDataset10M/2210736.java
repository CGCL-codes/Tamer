package titancommon.tasks;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Filewriter acts similar as the Sink task but it writes the received data
 * into a file. The file is named like "20091012-161954.txt".
 * 
 * @author Benedikt Köppel <bkoeppel@ee.ethz.ch>
 * @author Jonas Huber <huberjo@ee.ethz.ch>
 */
public class TitanFileWriter extends Task {

    public static final String NAME = "titanfilewriter";

    public static final int TASKID = 34;

    protected String filename;

    public TitanFileWriter() {
    }

    public TitanFileWriter(TitanFileWriter f) {
        super(f);
        filename = f.filename;
    }

    public Object clone() {
        return new TitanFileWriter(this);
    }

    /**
         * This has to return the configuration
         * @param maxBytesPerMsg
         * @return
         */
    public short[][] getConfigBytes(int maxBytesPerMsg) {
        short[] config_file = new short[filename.length()];
        char[] filename_c = filename.toCharArray();
        for (int i = 0; i < filename.length(); i++) {
            config_file[i] = (short) filename_c[i];
        }
        short[][] config = { config_file };
        return config;
    }

    public int getID() {
        return TASKID;
    }

    public int getInPorts() {
        return 127;
    }

    public String getName() {
        return NAME.toLowerCase();
    }

    public int getOutPorts() {
        return 0;
    }

    public boolean setConfiguration(String[] strConfig) {
        if (strConfig == null || strConfig.length == 0) {
            System.out.println("No configuration for TitanFileWriter");
            Date now = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            filename = dateFormat.format(now) + ".txt";
            return true;
        } else if (strConfig.length > 1) {
            System.out.println("Wrong configuration for TitanFileWriter");
            return false;
        }
        filename = strConfig[0].toString();
        return true;
    }

    /**
	 * Getter for the filename.
	 */
    public String getFilename() {
        return filename;
    }
}
