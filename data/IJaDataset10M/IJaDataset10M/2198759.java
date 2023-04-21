package org.ijg.jpeg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class IJGLib {

    private static final class RotateImageThread extends Thread {

        private String rotation = "90";

        private final String outPath;

        private final String inpath;

        private String jpegtran = Activator.getJpegTranPath();

        private int result = -1;

        private String outOption;

        public RotateImageThread(String outPath, String inpath) {
            super("IJG Rotate Thread");
            this.outPath = outPath;
            this.inpath = inpath;
        }

        public void run() {
            outOption = "-perfect  -rotate " + rotation;
            result = IJGLib.exec(jpegtran, outOption, inpath, outPath);
        }

        public int getResult() {
            return result;
        }

        /**
		 * @param rotation
		 *            from 90,180,270
		 */
        public void setRotation(String scaleFactor) {
            this.rotation = scaleFactor;
        }
    }

    private static final class ResizeImageThread extends Thread {

        private String scaleFactor = "1/8";

        private String quality = "80";

        private String minimize = " -scale 1/2";

        private final String outPath;

        private final String inpath;

        private String tmpPath;

        private String djepg = Activator.getDjpegPath();

        private String cjepg = Activator.getCjpegPath();

        private String inOption = "-scale ";

        private String outOption = "-quality ";

        private int result = -1;

        public ResizeImageThread(String outPath, String inpath) {
            super("IJG Resize Thread");
            this.outPath = outPath;
            this.inpath = inpath;
            tmpPath = outPath + ".bmp";
        }

        public void run() {
            if (scaleFactor.equals("1/16")) {
                scaleFactor = "1/8";
                quality += minimize;
            }
            inOption = "-scale " + scaleFactor + " -bmp";
            outOption = "-quality " + quality;
            IJGLib.exec(djepg, inOption, inpath, tmpPath);
            result = IJGLib.exec(cjepg, outOption, tmpPath, outPath);
            deleteTmp();
        }

        public void deleteTmp() {
            if (tmpPath != null) {
                File f = new File(tmpPath);
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        }

        public int getResult() {
            return result;
        }

        /**
		 * @param scaleFactor
		 *            from 1/2,1/4,1/8 to 1/16
		 */
        public void setScaleFactor(String scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }
    }

    /**
	 * @param inpath
	 * @param outPath
	 * @param scaleFactor 1/2 to 1/16 or null(default 1/8)
	 * @param quality e.g. 60 to 90
	 * @return
	 */
    public static int createFastResizeImage(final String inpath, final String outPath, String scaleFactor, String quality) {
        if (invalid()) {
            return -1;
        }
        if (inpath != null && outPath != null) {
            ResizeImageThread t = new ResizeImageThread(outPath, inpath);
            t.setQuality(quality);
            if (scaleFactor != null) {
                t.setScaleFactor(scaleFactor);
            }
            t.start();
            try {
                t.join();
                return t.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t = null;
        }
        return -1;
    }

    public static boolean invalid() {
        if (Activator.getCjpegPath() == null) {
            Activator.init();
            if (Activator.getCjpegPath() == null) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @param inpath
	 * @param outPath
	 * @param degree 90(right),180,270(left)
	 * @return
	 */
    public static int rotateImage(final String inpath, final String outPath, String degree) {
        if (invalid()) {
            return -1;
        }
        if (inpath != null && outPath != null) {
            RotateImageThread t = new RotateImageThread(outPath, inpath);
            t.setRotation(degree);
            t.start();
            try {
                t.join();
                return t.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t = null;
        }
        return -1;
    }

    public static int exec(String exe, String options, String inPath, String outPath) {
        if (inPath != null && outPath != null && exe != null) {
            outPath = getPath(outPath);
            inPath = getPath(inPath);
            String command = exe + " " + options + " " + inPath + " " + outPath;
            return runProcess(command);
        }
        return -1;
    }

    public static int runProcess(String command) {
        try {
            Process currentProcess = Runtime.getRuntime().exec(command);
            try {
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                throw new IOException("process interrupted");
            }
            int exitValue = currentProcess.exitValue();
            currentProcess.destroy();
            return exitValue;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    public static int runWithInputStream(String command) {
        try {
            Process currentProcess = Runtime.getRuntime().exec(command);
            InputStream errorStream = currentProcess.getErrorStream();
            InputStream inputStream = currentProcess.getInputStream();
            InputHandler errorHandler = new InputHandler(errorStream, "Error Stream");
            InputHandler inputHandler = new InputHandler(inputStream, "Output Stream");
            errorHandler.start();
            inputHandler.start();
            try {
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                throw new IOException("process interrupted");
            }
            try {
                errorHandler.join();
                inputHandler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            errorStream.close();
            inputStream.close();
            int exitValue = currentProcess.exitValue();
            currentProcess.destroy();
            return exitValue;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    public static String getPath(String path) {
        if (path == null) {
            return "";
        }
        path = path.trim();
        path = "\"" + path + "\"";
        return path;
    }
}
