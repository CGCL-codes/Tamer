package cz.cacek.ebook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

/**
 * Main (MIDlet) class of Ebook J2ME application.
 *
 * @author Tom� Darmovzal [tomas.darmovzal (at) seznam.cz]
 * @author Josef Cacek [josef.cacek (at) atlas.cz]
 * @author $Author: kwart $
 * @version $Revision: 1.17 $
 * @created $Date: 2007/08/30 11:25:22 $
 */
public class EBookMIDlet extends MIDlet {

    /**
	 * <code>canvas</code> is basic controller for EBookME
	 */
    protected EBookCanvas canvas;

    private SettingBean setting;

    private Library library;

    public EBookMIDlet() {
        super();
        this.setting = SettingBean.getIntance();
    }

    public void startApp() {
        if (Utils.INFO) {
            Utils.info("EBookMIDlet.startApp() ...");
        }
        if (canvas == null) {
            try {
                Display.getDisplay(this).setCurrent(new EBookIntro(this));
                final long tmpTime = System.currentTimeMillis();
                try {
                    loadConfig();
                } catch (Exception e) {
                    if (Utils.ERROR) {
                        Utils.error("load() failed: " + e.getMessage(), e);
                    }
                }
                canvas = new EBookCanvas(this);
                canvas.begin(tmpTime);
                if (Utils.INFO) {
                    Utils.info("EBookMIDlet.startApp() done.");
                }
            } catch (Exception e) {
                if (Utils.ERROR) {
                    Utils.error("EBookMIDlet.startApp() failed - " + e.getMessage(), e);
                }
            }
        } else {
            if (Utils.WARN) {
                Utils.warn("EBookMIDlet.startApp() - " + " skipped.");
            }
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (Utils.INFO) {
            Utils.info(" EBookMIDlet.destroyApp(" + unconditional + ") ...");
        }
        Library library = getLibrary();
        library.savePosition(canvas.getViewPosition());
        this.getSetting().setBookIdx(library.getBookIdx());
        try {
            saveConfig();
        } catch (Exception e) {
            if (Utils.ERROR) {
                Utils.error(" saveConfig failed: " + e.getMessage(), e);
            }
        }
        notifyDestroyed();
        if (Utils.INFO) {
            Utils.info(" EBookMIDlet.destroyApp(" + unconditional + ") done.");
        }
    }

    public Library getLibrary() {
        return library;
    }

    /**
	 * Loads configuration of this ebook application (fonts, colors, active
	 * book, ...)
	 *
	 * @throws Exception
	 */
    private void loadConfig() throws Exception {
        if (Utils.INFO) {
            Utils.info("EBookMIDlet.load() ...");
        }
        RecordStore tmpRS = null;
        try {
            tmpRS = RecordStore.openRecordStore(Utils.STORE_CONFIG, true);
            if (tmpRS != null && tmpRS.getNumRecords() > 0) {
                byte[] bytes = tmpRS.getRecord(1);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                DataInputStream dis = new DataInputStream(bais);
                loadConfig(dis);
                dis.close();
            }
            final SettingBean settingBean = this.getSetting();
            if (Utils.INFO) {
                Utils.info(settingBean.toString());
            }
            InputStream tmpIS = Library.class.getResourceAsStream("/" + Utils.DATA_FOLDER + "/" + Utils.LIBRARY_FILE);
            LibraryInfo libInfo = null;
            try {
                if (null != tmpIS) {
                    libInfo = LibraryInfo.read(tmpIS);
                }
            } finally {
                if (null != tmpIS) {
                    tmpIS.close();
                }
            }
            if (libInfo == null) {
                libInfo = new LibraryInfo();
            }
            this.library = new Library(libInfo);
            library.setBookIdx(settingBean.getBookIdx());
            if (Utils.INFO) {
                Utils.info("curLibrary:" + library);
            }
        } finally {
            if (tmpRS != null) tmpRS.closeRecordStore();
        }
        if (Utils.INFO) {
            Utils.info("EBookMIDlet.load() done.");
        }
    }

    /**
	 * Loads configuration from given DataInput
	 *
	 * @param aDis
	 * @throws Exception
	 */
    private void loadConfig(DataInput aDis) throws Exception {
        setting.loadSetting(aDis);
    }

    /**
	 * Saves configuration of this ebook application (fonts, colors, active
	 * book, ...)
	 *
	 * @throws Exception
	 */
    private void saveConfig() throws Exception {
        if (Utils.INFO) {
            Utils.info(this.getSetting().toString());
        }
        RecordStore tmpRS = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            saveConfig(dos);
            dos.flush();
            byte[] bytes = baos.toByteArray();
            tmpRS = RecordStore.openRecordStore(Utils.STORE_CONFIG, true);
            if (tmpRS.getNumRecords() == 0) {
                tmpRS.addRecord(bytes, 0, bytes.length);
            } else {
                tmpRS.setRecord(1, bytes, 0, bytes.length);
            }
            dos.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (tmpRS != null) {
                tmpRS.closeRecordStore();
            }
        }
    }

    /**
	 * Saves configuration to given DataOutput
	 *
	 * @param aDos
	 * @throws Exception
	 */
    private void saveConfig(DataOutput aDos) throws Exception {
        if (this.setting != null) {
            this.setting.saveSetting(aDos);
        }
    }

    public SettingBean getSetting() {
        return setting;
    }
}
