package fildiv.jremcntl.tools.mplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import fildiv.jremcntl.tools.common.ToolsRuntimeException;
import fildiv.jremcntl.tools.common.PipeInputStreamToOutputStream;

public class MPlayerFE {

    private String jremHome;

    private String mPlayerCmdLine;

    private String cmdFilePath;

    private String semFilePath;

    private InputStream cmdFileStream;

    private boolean ownerSemFile;

    public MPlayerFE(Properties prop) {
        if (prop == null) throw new IllegalArgumentException();
        this.jremHome = prop.getProperty("jrem.home");
        if (this.jremHome == null || this.jremHome.equals("")) this.jremHome = System.getProperty("JREMHOME");
        if (this.jremHome == null || this.jremHome.equals("")) throw new IllegalArgumentException("jrem.home is missing");
        this.mPlayerCmdLine = prop.getProperty("mplayer.cmdline");
        if (mPlayerCmdLine == null || mPlayerCmdLine.equals("")) throw new IllegalArgumentException("mplayer.cmdline is missing");
        mPlayerCmdLine += " -slave";
        this.cmdFilePath = prop.getProperty("mplayer.cmdFilePath");
        if (cmdFilePath == null || cmdFilePath.equals("")) cmdFilePath = this.jremHome + File.separator + "work" + File.separator + "mplcmd";
        this.semFilePath = this.jremHome + File.separator + "work" + File.separator + "mplayerFE.sem";
        System.out.println("JRem Home : " + this.jremHome);
        System.out.println("MPlayer cmd-line : " + this.mPlayerCmdLine);
        System.out.println("Command file : " + this.cmdFilePath);
        checkConcurrencyInstance();
        createCmdStream();
    }

    private void checkConcurrencyInstance() {
        File semFile = new File(semFilePath);
        if (semFile.exists()) throw new ToolsRuntimeException("Command file already exists," + "check if another mplayer instance is running or try to " + "delete the file " + semFile.getAbsolutePath());
        try {
            semFile.createNewFile();
        } catch (IOException e) {
            throw new ToolsRuntimeException("Unable to create lock file", e);
        }
        ownerSemFile = true;
    }

    private void deleteFile(String filePath) {
        File cmdFile = new File(filePath);
        cmdFile.delete();
    }

    private void createCmdStream() {
        File cmdFile = new File(cmdFilePath);
        cmdFile.delete();
        try {
            cmdFile.createNewFile();
            cmdFileStream = new FileInputStream(cmdFile);
        } catch (IOException e) {
            throw new ToolsRuntimeException(e);
        }
    }

    protected int runMPlayer() {
        try {
            final Process p = Runtime.getRuntime().exec(mPlayerCmdLine);
            new Thread(new PipeInputStreamToOutputStream(p.getInputStream(), System.out)).start();
            new Thread(new PipeInputStreamToOutputStream(p.getErrorStream(), System.err)).start();
            new Thread(new PipeInputStreamToOutputStream(cmdFileStream, p.getOutputStream())) {

                public void run() {
                    followRead(p.getOutputStream());
                }
            }.start();
            return p.waitFor();
        } catch (Exception e) {
            throw new ToolsRuntimeException(e);
        }
    }

    protected void followRead(OutputStream os) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(cmdFileStream);
            br = new BufferedReader(isr);
            while (true) {
                String line = br.readLine();
                if (line != null && line.length() >= 0) {
                    String fixedLine = line + "\n";
                    byte bytes[] = fixedLine.getBytes();
                    os.write(bytes, 0, bytes.length);
                    os.flush();
                }
                synchronized (cmdFileStream) {
                    cmdFileStream.wait(100);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (isr != null) isr.close();
                if (br != null) br.close();
            } catch (IOException e) {
            }
        }
    }

    public int start() {
        int retCode = -1;
        try {
            retCode = runMPlayer();
            return retCode;
        } catch (Exception e) {
            throw new ToolsRuntimeException(e);
        } finally {
            freeResources();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        freeResources();
    }

    private void freeResources() {
        try {
            if (cmdFileStream != null) cmdFileStream.close();
            deleteFile(cmdFilePath);
            if (ownerSemFile) deleteFile(semFilePath);
        } catch (IOException e) {
        }
    }

    protected static Properties getProperties(String fileName) {
        java.util.Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            p.load(is);
            return p;
        } catch (Exception e) {
            throw new ToolsRuntimeException("Unable to load : " + fileName, e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: MPlayerFE <Property File>");
            System.exit(1);
        }
        String fileName = args[0];
        Properties p = getProperties(fileName);
        MPlayerFE mpfe = new MPlayerFE(p);
        int retCode = mpfe.start();
        System.exit(retCode);
    }
}
