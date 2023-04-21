package ufolib.fontizer;

import ufolib.convert.*;
import gnu.getopt.*;
import java.io.*;
import waba.applet.*;

/**
 * Main application
 *
 * functions here are: union of GUI requirements and CmdLine requirements.
 */
public class Controller {

    static final int DEBUGLEVEL = 5;

    private static final int UI_GUI = 1;

    private static final int UI_CMD = 2;

    /**
     * desired UI
     * -1 is: not set
     */
    private int m_usedUi = UI_GUI;

    /**
     * the registered UI
     */
    private UserInterface m_ui;

    /**
     * params
     */
    private String[] m_args;

    private boolean m_quiet = false;

    private String m_paramSrcFile = null;

    private String m_paramProFile = null;

    private String m_paramUffFamily = null;

    private String m_paramUffSize = null;

    private String m_paramUffShape = null;

    private String m_paramBatchFile = null;

    private boolean m_saveAndExit = false;

    /**
     * null if none loaded
     */
    private FontSource m_fontsource = null;

    private int m_requestSize = 0;

    private FontRange m_fontsourceRange = null;

    private ProfileManager m_profile = null;

    private String m_profileFilename = null;

    private UFFWriter m_uffwriter = null;

    public static Controller m_controller;

    /**
     * main
     */
    public static void main(String[] args) {
        m_controller = new Controller(args);
        m_controller.go();
    }

    public Controller(String[] args) {
        m_args = args;
    }

    /**
     * Main work.
     */
    public void go() {
        JavaBridge.setNonGUIApp();
        processArgs();
        if (m_usedUi == UI_GUI) {
            m_ui = new GUI_Application();
        } else if (m_usedUi == UI_CMD) {
            m_ui = new CmdLineInterface();
        }
        m_ui.preInit(this);
        instanceOwnFields();
        m_ui.postInit();
        if (m_saveAndExit) {
            writeUff();
            friendlyExit();
        } else {
            if (m_usedUi == UI_GUI) {
            } else if (m_usedUi == UI_CMD) {
                if (m_paramBatchFile == null) {
                    ((CmdLineInterface) m_ui).startLoop();
                } else {
                    ((CmdLineInterface) m_ui).startBatch(m_paramBatchFile);
                }
            }
        }
    }

    public void friendlyExit() {
        m_ui.exit();
        System.exit(0);
    }

    public void unfriendlyExit() {
        m_ui.exit();
        System.exit(1);
    }

    private void processArgs() {
        Getopt g = new Getopt("Fontizer Controller", m_args, "-f:p:u:v:w:icb:qx");
        int c;
        String arg;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'f':
                    m_paramSrcFile = (g.getOptarg()).trim();
                    break;
                case 'p':
                    m_paramProFile = (g.getOptarg()).trim();
                    break;
                case 'u':
                    m_paramUffFamily = (g.getOptarg()).trim();
                    break;
                case 'v':
                    m_paramUffSize = (g.getOptarg()).trim();
                    break;
                case 'w':
                    m_paramUffShape = (g.getOptarg()).trim();
                    break;
                case 'q':
                    m_quiet = true;
                    break;
                case 'i':
                    break;
                case 'c':
                    m_usedUi = UI_CMD;
                    break;
                case 'b':
                    m_paramBatchFile = (g.getOptarg()).trim();
                    break;
                case 'x':
                    m_saveAndExit = true;
                    break;
                case '?':
                    System.err.println("-i          start interactive gui (this is the default), vs. -c/-b \n" + "-c <file>   start command line interface, vs. -i/-b\n" + "-b <file>   start batch job, commands given in <file>, vs. -i/-c\n" + "-f <file>   load this source font on startup\n" + "-p <file>   load this profile on startup\n" + "-u <fam>    use this as UFF family name\n" + "-v <size>   use this as UFF pixelsize\n" + "-w <nbi>    use this as UFF shape\n" + "-x          save UFF and exit, use with -f -p -u -v -w \n" + "-q          be quiet\n");
                    break;
                default:
                    System.out.print("getopt() returned " + c + "\n");
            }
        }
        if (m_paramBatchFile != null) {
            m_usedUi = UI_CMD;
        }
    }

    private void instanceOwnFields() {
        if (m_paramSrcFile != null) {
            setSrcFile(m_paramSrcFile);
        }
        m_profile = new ProfileManager();
        if (m_paramProFile != null) {
            setProFile(m_paramProFile);
        }
        m_uffwriter = new UFFWriter();
        if (m_paramUffFamily != null) {
            setUffDataFamily(m_paramUffFamily);
        }
        if (m_paramUffSize != null) {
            try {
                int i = Integer.parseInt(m_paramUffSize);
                setUffDataSize(i);
            } catch (NumberFormatException e) {
            }
        }
        if (m_paramUffShape != null) {
            setUffDataShape(m_paramUffShape);
        }
    }

    /**
     * @return false, if it fails
     */
    private boolean setSrcFile(String filename) {
        FontSource fs = null;
        boolean success = false;
        File fsf = new File(filename);
        mess(2, "open font file: " + filename);
        if (fsf.canRead()) {
            mess(2, "open font file readable");
            if (filename.endsWith(".bdf") || filename.endsWith(".BDF")) {
                mess(2, "detected as BDF file");
                fs = new BdfFont();
            }
            if (fs != null) {
                mess(2, "font source open ...");
                success = fs.openFile(filename);
                if (success) {
                    mess(2, "font source open ok");
                    m_fontsource = fs;
                    fsCalcRange();
                    m_requestSize = m_fontsource.fontCoreSize();
                }
            }
        }
        mess(2, "finish open src is " + success);
        return success;
    }

    /**
     * the FontSource's range is not managed by the FontRange, we do it here.
     */
    private boolean fsCalcRange() {
        int oldEnc, curEnc, startV, endV;
        m_fontsourceRange = new FontRange();
        int glyphslength = m_fontsource.fontNumberOfChars();
        Controller.mess(2, "Controller, fontsourceRange: scan for range, number of chars " + glyphslength + ", start");
        if (glyphslength > 0) {
            oldEnc = m_fontsource.fontGlyphDataByIndex(0).glyphEncoding();
            startV = oldEnc;
            for (int index = 1; index < glyphslength; index++) {
                curEnc = m_fontsource.fontGlyphDataByIndex(index).glyphEncoding();
                if ((oldEnc + 1) != curEnc) {
                    endV = oldEnc;
                    Controller.mess(9, "Controller, fontsourceRange: middle (" + startV + "-" + endV + ") ...");
                    m_fontsourceRange.addRange(startV, endV);
                    Controller.mess(8, "Controller, fontsourceRange: middle (" + startV + "-" + endV + ") done");
                    startV = curEnc;
                }
                oldEnc = curEnc;
            }
            endV = oldEnc;
            Controller.mess(9, "Controller, fontsourceRange: last (" + startV + "-" + endV + ") ...");
            m_fontsourceRange.addRange(startV, endV);
            Controller.mess(8, "Controller, fontsourceRange: last (" + startV + "-" + endV + ") done");
        }
        Controller.mess(2, "Controller, fontsourceRange: total " + m_fontsourceRange.size() + ", finished");
        return true;
    }

    /**
     * 
     * @return false, if it fails
     */
    private boolean setProFile(String filename) {
        boolean success = false;
        File fsf = new File(filename);
        if (fsf.canRead()) {
            success = m_profile.openFile(filename);
            if (success) {
            }
        }
        return success;
    }

    /**
     * open font source
     *
     * @return false, if it fails
     */
    public boolean openSrc(String filename) {
        mess(1, "Src file open " + filename);
        return setSrcFile(filename);
    }

    public String getSrcDataCoreFamily() {
        if (m_fontsource != null) {
            return m_fontsource.fontCoreFamily();
        }
        return null;
    }

    public int getSrcDataCoreSize() {
        if (m_fontsource != null) {
            return m_fontsource.fontCoreSize();
        }
        return 0;
    }

    public int getSrcDataRequestSize() {
        return m_requestSize;
    }

    public String getSrcDataCoreShape() {
        if (m_fontsource != null) {
            return m_fontsource.fontCoreShape();
        }
        return null;
    }

    public String getSrcDataFilename() {
        if (m_fontsource != null) {
            return m_fontsource.fontFilename();
        }
        return null;
    }

    public String getSrcDataType() {
        if (m_fontsource != null) {
            return m_fontsource.fontType();
        }
        return null;
    }

    public String getSrcDataComment() {
        if (m_fontsource != null) {
            return m_fontsource.fontComment();
        }
        return null;
    }

    public boolean getSrcDataIsFixedSize() {
        if (m_fontsource != null) {
            return m_fontsource.fontIsFixedSize();
        }
        return true;
    }

    /**
     * Number of Ranges
     */
    public int getSrcDataRanges() {
        if (m_fontsourceRange != null) {
            return m_fontsourceRange.size();
        }
        return 0;
    }

    /**
     * Ranges
     */
    public FontRange getSrcDataRange() {
        if (m_fontsourceRange != null) {
            return m_fontsourceRange;
        }
        return null;
    }

    /**
     * Total number of chars
     */
    public int getSrcDataChars() {
        if (m_fontsourceRange != null) {
            return m_fontsource.fontNumberOfChars();
        }
        return 0;
    }

    public int getSrcDataRangeStart(int index) {
        if (m_fontsourceRange != null) {
            return m_fontsourceRange.getRangeStart(index);
        }
        return 0;
    }

    public int getSrcDataRangeEnd(int index) {
        if (m_fontsourceRange != null) {
            return m_fontsourceRange.getRangeEnd(index);
        }
        return 0;
    }

    /**
     * Set request size, in case of vector font
     *
     * @return actual set size
     */
    public int setSrcDataRequestSize(int rq) {
        if (m_fontsource != null) {
            if (m_fontsource.fontIsFixedSize()) {
                m_requestSize = m_fontsource.fontCoreSize();
            } else {
                m_requestSize = rq;
                m_fontsource.fontSetPixelSize(m_requestSize);
            }
        }
        return m_requestSize;
    }

    /**
     * Set encoding at font source
     *
     * @return null, if default
     */
    public boolean setSrcDataEncoding(String enc) {
        if (m_fontsource != null) {
            if (m_fontsource.fontSetEncoding(enc)) {
                return fsCalcRange();
            }
        }
        return false;
    }

    /**
     * Get encoding from font source
     *
     * @return FontSource returns null, if default
     */
    public String getSrcDataEncoding() {
        if (m_fontsource != null) {
            return m_fontsource.fontEncoding();
        }
        return null;
    }

    /**
     * Get list of converter-encodings
     */
    public String[] getCharConverterList() {
        return CharConvert.providedConverters();
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean openProfile(String filename) {
        if ((filename != null) && (m_profile != null)) {
            m_profileFilename = filename;
            return m_profile.openFile(m_profileFilename);
        }
        return false;
    }

    /**
     * 
     * @param filename save this file; null if openProfile's filename should be used
     * @return false, if it fails
     */
    public boolean saveProfile(String filename) {
        String ufilename = (filename == null ? m_profileFilename : filename);
        if ((m_profile != null) && (ufilename != null)) {
            return m_profile.saveFile(ufilename);
        }
        return false;
    }

    public String getProfileDataCoreFamily() {
        return null;
    }

    public int getProfileDataCoreSize() {
        return 0;
    }

    public String getProfileDataCoreShape() {
        return null;
    }

    public String getProfileDataComment() {
        if (m_profile != null) {
            return m_profile.getProfileComment();
        }
        return null;
    }

    public void setProfileDataComment(String comm) {
        if (m_profile != null) {
            mess(3, "to profile: setting profile comment: " + comm);
            m_profile.setProfileComment(comm);
        }
    }

    /**
     * 
     */
    public FontRange getProfileDataRange() {
        if (m_profile != null) {
            return m_profile.range();
        }
        return null;
    }

    public boolean setProfileRangesByUff(String filename) {
        return false;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean writeUff() {
        if ((m_uffwriter != null) && (m_fontsource != null) && (m_profile != null)) {
            return m_uffwriter.writeUff(m_fontsource, m_fontsourceRange, m_profile.range());
        }
        return false;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean calculateUffRanges() {
        mess(6, "Contoller calculateUffRange: " + m_uffwriter + " : " + m_fontsource + " : " + m_fontsourceRange + " : " + m_profile + ".");
        if ((m_uffwriter != null) && (m_fontsource != null) && (m_fontsourceRange != null) && (m_profile != null)) {
            if (m_fontsourceRange.size() > 0) {
                return m_uffwriter.calculateUffRanges(m_fontsource, m_fontsourceRange, m_profile.range());
            } else {
                mess(2, "calculateUffRanges, but Font Source range is empty");
                return true;
            }
        }
        return true;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean setUffDataFamily(String name) {
        if (m_uffwriter != null) {
            return m_uffwriter.setFamily(name);
        }
        return false;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean setUffDataSize(int size) {
        if (m_uffwriter != null) {
            return m_uffwriter.setSize(size);
        }
        return false;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean setUffDataShape(String shape) {
        if (m_uffwriter != null) {
            return m_uffwriter.setShape(shape);
        }
        return false;
    }

    /**
     * 
     *
     */
    public String getUffDataFamily() {
        if (m_uffwriter != null) {
            return m_uffwriter.getFamily();
        }
        return null;
    }

    /**
     * 
     *
     */
    public int getUffDataSize() {
        if (m_uffwriter != null) {
            return m_uffwriter.getSize();
        }
        return 0;
    }

    /**
     * 
     *
     */
    public String getUffDataShape() {
        if (m_uffwriter != null) {
            return m_uffwriter.getShape();
        }
        return null;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean setUffDataExtraWS(int v) {
        if (m_uffwriter != null) {
            return m_uffwriter.setExtraWS(v);
        }
        return false;
    }

    /**
     * 
     *
     */
    public int getUffDataExtraWS() {
        if (m_uffwriter != null) {
            return m_uffwriter.getExtraWS();
        }
        return 0;
    }

    /**
     * 
     *
     * @return false, if it fails
     */
    public boolean setUffDataExtraLeading(int v) {
        if (m_uffwriter != null) {
            return m_uffwriter.setExtraLeading(v);
        }
        return false;
    }

    /**
     * 
     *
     */
    public int getUffDataExtraLeading() {
        if (m_uffwriter != null) {
            return m_uffwriter.getExtraLeading();
        }
        return 0;
    }

    /**
     * aka. radio button "use schema name"
     *
     * @return false, if it fails
     */
    public boolean setUffDataSchemaName() {
        if (m_uffwriter != null) {
            return m_uffwriter.setSchemaName();
        }
        return false;
    }

    /**
     * aka. radio button "custom font name"
     *
     * @return false, if it fails
     */
    public boolean setUffDataCustomName(String fontname) {
        if (m_uffwriter != null) {
            return m_uffwriter.setCustomName(fontname);
        }
        return false;
    }

    public String getUffDataSchemaName() {
        if (m_uffwriter != null) {
            return m_uffwriter.getSchemaName();
        }
        return null;
    }

    public String getUffDataCustomName() {
        if (m_uffwriter != null) {
            return m_uffwriter.getCustomName();
        }
        return null;
    }

    public String getUffDataSCName() {
        if (m_uffwriter != null) {
            return m_uffwriter.getSCName();
        }
        return null;
    }

    /**
     * Is Schema or custom name the current
     */
    public boolean getUffDataIsSchemaName() {
        if (m_uffwriter != null) {
            return m_uffwriter.isSchemaName();
        }
        return true;
    }

    /**
     *  When written to PDB there will be one more (the MCS).
     */
    public FontRange getUffDataRange() {
        if (m_uffwriter != null) {
            return m_uffwriter.range();
        }
        return null;
    }

    /**
     * if an error happens, get the description of the last error here.
     *
     * @return a string; null if no error
     */
    public String errorMess() {
        return null;
    }

    static void mess(int level, String s) {
        if (level <= DEBUGLEVEL) {
            System.err.println("(via) fontizer.Controller: " + s);
        }
    }
}
