package org.sharp.android.autils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.sharp.exceptions.SdcardNotAvailableException;
import org.sharp.intf.Logger;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class AIOUtils {

    private static PrintWriter logfileWriter;

    public static void copyAssetFile(Context ctx, String srcFileName, String targetFilePath) {
        AssetManager assetManager = ctx.getAssets();
        try {
            InputStream is = assetManager.open(srcFileName);
            File out = new File(targetFilePath);
            if (!out.exists()) {
                out.getParentFile().mkdirs();
                out.createNewFile();
            }
            OutputStream os = new FileOutputStream(out);
            IOUtils.copy(is, os);
            is.close();
            os.close();
        } catch (IOException e) {
            AIOUtils.log("error when copyAssetFile", e);
        }
    }

    public static String storagePath(String path) {
        String state = Environment.getExternalStorageState();
        String storagePath = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            storagePath = FilenameUtils.concat(Environment.getExternalStorageDirectory().getPath(), path);
            return storagePath;
        } else {
            return null;
        }
    }

    public static File initLogFile(Context ctx, boolean addFileHandler) {
        File logFile = null;
        java.util.logging.FileHandler fh = null;
        try {
            String state = Environment.getExternalStorageState();
            String pattern = "";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                pattern = FilenameUtils.concat(Environment.getExternalStorageDirectory().getPath(), "logs/vocReader.log");
            } else {
                File di = ctx.getDir("logs", Context.MODE_PRIVATE);
                pattern = FilenameUtils.concat(di.getPath(), "vocReader.log");
            }
            File f = new File(pattern);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            logfileWriter = new PrintWriter(f);
        } catch (Exception e) {
            AIOUtils.log("", e);
        }
        return logFile;
    }

    public static void log(String msg) {
        if (logfileWriter != null) {
            logfileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "," + msg + "\n");
            if (logfileWriter.checkError()) {
                throw new RuntimeException("error occur when writing log file.");
            }
        }
    }

    public static void log(String msg, Throwable thrown) {
        Log.d("", msg, thrown);
        if (logfileWriter != null) {
            logfileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "," + msg + "\n");
            if (thrown != null) thrown.printStackTrace(logfileWriter);
            if (logfileWriter.checkError()) {
                throw new RuntimeException("error occur when writing log file.");
            }
        }
    }

    public static String exec(String logcatCmd, int maxLines) {
        try {
            Process process = Runtime.getRuntime().exec(logcatCmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            Stack<String> lines = new Stack<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lines.push(line);
            }
            for (int i = 0; i < maxLines && !lines.isEmpty(); i++) {
                log.append(lines.pop() + "\n");
            }
            return log.toString();
        } catch (IOException e) {
            AIOUtils.log("", e);
        }
        return null;
    }

    @Deprecated
    public static void writeExternalFile(String path, String str) throws IOException {
        String filepath = storagePath(path);
        FileUtils.writeStringToFile(new File(filepath), str, "utf-8");
    }

    @Deprecated
    public static String readExternalFile(String path) throws IOException {
        String filepath = storagePath(path);
        File file = new File(filepath);
        if (file.exists()) return FileUtils.readFileToString(file, "utf-8"); else return null;
    }

    public static void writeFile(String path, String str) throws IOException {
        FileUtils.writeStringToFile(new File(path), str, "utf-8");
    }

    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) return FileUtils.readFileToString(file, "utf-8"); else return null;
    }

    public static InputStream loadAssetResource(Context cx, String path) {
        AssetManager am = cx.getAssets();
        try {
            InputStream is = am.open(path);
            return is;
        } catch (IOException e) {
            AIOUtils.log("", e);
        }
        return null;
    }

    public static File newInternalFile(Context cx, String dir, String filename) {
        File di = cx.getDir(dir, Context.MODE_PRIVATE);
        log("dir " + dir + " created at internal storage.");
        File fi = new File(di, filename);
        try {
            if (fi.createNewFile()) log(fi.getPath() + " created.");
        } catch (IOException e) {
            AIOUtils.log("", e);
        }
        return fi;
    }

    public static File writeInternalFile(Context cx, URL url, String dir, String filename) {
        FileOutputStream fos = null;
        File fi = null;
        try {
            fi = newInternalFile(cx, dir, filename);
            fos = FileUtils.openOutputStream(fi);
            int length = IOUtils.copy(url.openStream(), fos);
            log(length + " bytes copyed.");
        } catch (IOException e) {
            AIOUtils.log("", e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                AIOUtils.log("", e);
            }
        }
        return fi;
    }

    public static byte[] readInternalFile(Context cx, String filename) {
        FileInputStream fis = null;
        try {
            fis = cx.openFileInput(filename);
            return IOUtils.toByteArray(fis);
        } catch (IOException e) {
            AIOUtils.log("", e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                AIOUtils.log("", e);
            }
        }
        return null;
    }
}
