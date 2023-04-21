package fr.soleil.TangoArchiving.ArchivingTools.Diary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;

public abstract class AbstractLogger implements ILogger {

    protected int traceLevel;

    protected PrintWriter writer;

    protected Hashtable levels;

    protected Hashtable reverseLevels;

    public int getTraceLevel() {
        return traceLevel;
    }

    public void setTraceLevel(int level) {
        this.traceLevel = level;
    }

    public void setTraceLevel(java.lang.String _level) {
        int level = ILogger.LEVEL_DEBUG;
        try {
            Integer _levelI = (Integer) reverseLevels.get(_level);
            level = _levelI.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.traceLevel = level;
    }

    public void initDiaryWriter(String path, String archiver) throws IOException {
        String refactoredArchiverName = this.refactorArchiverName(archiver);
        levels = new Hashtable(5);
        levels.put(new Integer(LEVEL_CRITIC), CRITIC);
        levels.put(new Integer(LEVEL_ERROR), ERROR);
        levels.put(new Integer(LEVEL_WARNING), WARNING);
        levels.put(new Integer(LEVEL_INFO), INFO);
        levels.put(new Integer(LEVEL_DEBUG), DEBUG);
        reverseLevels = new Hashtable(5);
        reverseLevels.put(CRITIC, new Integer(LEVEL_CRITIC));
        reverseLevels.put(ERROR, new Integer(LEVEL_ERROR));
        reverseLevels.put(WARNING, new Integer(LEVEL_WARNING));
        reverseLevels.put(INFO, new Integer(LEVEL_INFO));
        reverseLevels.put(DEBUG, new Integer(LEVEL_DEBUG));
        String absp = path;
        System.out.println("absp|" + absp + "|");
        File f = new File(absp);
        if (!f.canWrite()) {
            System.out.println("! canWrite");
            f.mkdir();
        } else {
            System.out.println("canWrite");
        }
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String date = today.toString();
        absp += "/diary_" + refactoredArchiverName + "_" + date;
        absp += ".log";
        writer = new PrintWriter(new FileWriter(absp, true));
    }

    private String refactorArchiverName(String archiver) {
        StringTokenizer st = new StringTokenizer(archiver, "/");
        String ret = "";
        while (st.hasMoreTokens()) {
            ret = st.nextToken();
        }
        return ret;
    }

    public int getTraceLevel(String level_s) {
        Integer lev = (Integer) reverseLevels.get(level_s);
        if (lev == null) {
            return LEVEL_DEBUG;
        }
        try {
            int ret = lev.intValue();
            return ret;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return LEVEL_DEBUG;
        }
    }

    public void trace(int level, Object o) {
        if (level <= this.traceLevel) {
            try {
                this.changeDiaryFileIfNecessary();
                this.addToDiary(level, o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (o instanceof String) {
                String msg = (String) o;
                msg = this.getDecoratedLog(msg, level);
                this.log(msg);
            }
        }
    }

    protected abstract void changeDiaryFileIfNecessary() throws IOException;

    /**
     * @param msg
     * @param level
     * @return 8 juil. 2005
     */
    protected abstract String getDecoratedLog(String msg, int level);

    /**
     * @param level
     * @param o
     * @throws Exception 8 juil. 2005
     */
    protected abstract void addToDiary(int level, Object o) throws Exception;

    /**
     * @param msg 8 juil. 2005
     */
    protected abstract void log(String msg);

    public abstract void close();
}
