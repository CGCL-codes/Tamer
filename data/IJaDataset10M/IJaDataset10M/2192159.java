package de.schwarzrot.burn.service;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.schwarzrot.app.config.BurnerConfig;
import de.schwarzrot.app.support.StatusUpdater;
import de.schwarzrot.burn.domain.MediaInfo;
import de.schwarzrot.concurrent.TaskPerThreadExecutor;
import de.schwarzrot.concurrent.VContextTask;
import de.schwarzrot.system.CommandReader;

/**
 * wrapper class for linux command "growisofs".
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class LinuxDVDBurner implements DVDBurner {

    private static final String ID_EYECATCH = "INQUIRY:";

    private static final String ERROR_EYECATCH = ":-(";

    private static final String STATUS_EYECATCH = " Disc status:";

    private static final String SPACE_EYECATCH = " Free Blocks:";

    private static final String MEDIA_EYECATCH = " Mounted Media:";

    private static final Pattern patIdBurn = Pattern.compile("^INQUIRY:\\s+\\[([^\\]]+)\\]\\[([^\\]]+)\\]\\[([^\\]]+)\\]");

    private static final Pattern patStatus = Pattern.compile("^\\s+Disc\\s+status:\\s+(.+)$");

    private static final Pattern patSpace = Pattern.compile("^\\s+Free\\s+Blocks:\\s+(\\d+)\\*(\\d+)(.+)$");

    private static final Pattern patMedia = Pattern.compile("^\\s+Mounted Media:\\s+([^,]+),\\s+(.+)$");

    public LinuxDVDBurner() {
    }

    public LinuxDVDBurner(BurnerConfig cfg) {
        config = cfg;
    }

    public LinuxDVDBurner(Component parent, BurnerConfig cfg) {
        this.parent = parent;
        config = cfg;
    }

    public void burnISO(File isoFile) {
        if (isoFile.exists() && isoFile.length() > 0 && isoFile.isFile() && isoFile.canRead()) {
            MediaInfo info = scanMedia();
            burnISO(isoFile, info);
        }
    }

    @Override
    public void burnISO(File isoFile, MediaInfo info) {
        burnISO(isoFile, info, false);
    }

    @Override
    public void burnISO(File isoFile, MediaInfo info, boolean ejectMedia) {
        if (info != null && info.getMediaStatus().equals("blank") && info.getMediaFreeSpace() >= isoFile.length()) {
            File dev = new File(info.getDeviceName());
            File burner = config.getBurnCommand();
            if (burner.exists() && burner.isFile() && burner.canExecute() && dev.exists() && dev.canWrite()) {
                VContextTask vct = new VContextTask() {

                    @Override
                    public void run() {
                        File helper = (File) getContext("Burner");
                        File device = (File) getContext("Device");
                        File source = (File) getContext("Source");
                        Boolean eject = (Boolean) getContext("Eject");
                        List<String> cmdArgs = new ArrayList<String>();
                        cmdArgs.add(helper.getAbsolutePath());
                        cmdArgs.add("-dvd-compat");
                        cmdArgs.add("-speed=1");
                        cmdArgs.add("-Z");
                        cmdArgs.add(device.getAbsolutePath() + "=" + source.getAbsolutePath());
                        try {
                            CommandReader cr = new CommandReader(cmdArgs);
                            Process proc = cr.start();
                            BufferedReader br = cr.startReader(proc);
                            String line;
                            if (statusUpdater != null) statusUpdater.begin();
                            while ((line = br.readLine()) != null) statusUpdater.updateStatus(line);
                            if (eject) eject(device);
                        } catch (IOException ie) {
                            System.err.println("Error on burning ISO-File: " + source.getAbsolutePath());
                            ie.printStackTrace();
                        } finally {
                            if (statusUpdater != null) statusUpdater.end();
                        }
                    }
                };
                vct.addContext("Burner", burner);
                vct.addContext("Device", dev);
                vct.addContext("Source", isoFile);
                vct.addContext("Eject", ejectMedia);
                new TaskPerThreadExecutor().execute(vct);
            }
        }
    }

    public void burnISO(String isoFileName) {
        burnISO(new File(isoFileName));
    }

    @Override
    public void eject(File device) {
        File cmd = config.getEjectCommand();
        if (cmd.exists() && cmd.canExecute()) {
            try {
                Runtime.getRuntime().exec(new String[] { cmd.getAbsolutePath(), device.getAbsolutePath() });
            } catch (Exception e) {
            }
        }
    }

    public final Component getParent() {
        return parent;
    }

    public final StatusUpdater getStatusUpdater() {
        return statusUpdater;
    }

    @Override
    public MediaInfo scanMedia() {
        File device = config.getBurnDevice();
        File scanner = config.getMediaInfoPath();
        MediaInfo rv = null;
        if (scanner.exists() && scanner.canExecute() && device.exists() && device.canRead()) {
            List<String> cmdArgs = new ArrayList<String>();
            cmdArgs.add(scanner.getAbsolutePath());
            cmdArgs.add(device.getAbsolutePath());
            try {
                CommandReader cr = new CommandReader(cmdArgs);
                Process proc = cr.start();
                BufferedReader br = cr.startReader(proc);
                String line;
                Matcher m;
                rv = new MediaInfo();
                rv.setDeviceName(device.getAbsolutePath());
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(ERROR_EYECATCH)) {
                        rv.setMediaID(ERROR_EYECATCH);
                        rv.setMediaType(line);
                    } else if (line.startsWith(ID_EYECATCH)) {
                        m = patIdBurn.matcher(line);
                        if (m.matches()) {
                            rv.setDeviceManufactorer(m.group(1));
                            rv.setDeviceModel(m.group(2));
                            rv.setDeviceBIOS(m.group(3));
                        }
                    } else if (line.startsWith(STATUS_EYECATCH)) {
                        m = patStatus.matcher(line);
                        if (m.matches()) rv.setMediaStatus(m.group(1));
                    } else if (line.startsWith(MEDIA_EYECATCH)) {
                        m = patMedia.matcher(line);
                        if (m.matches()) rv.setMediaType(m.group(2));
                    } else if (line.startsWith(SPACE_EYECATCH)) {
                        m = patSpace.matcher(line);
                        if (m.matches()) {
                            long blocks = Long.valueOf(m.group(1));
                            long blockSize = Long.valueOf(m.group(2));
                            String unit = m.group(3);
                            if (unit.equals("KB")) {
                                rv.setMediaFreeSpace(blocks * blockSize * 1024);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                getLogger().fatal("Error on check media: ", e);
            }
        }
        return rv;
    }

    @Override
    public final void setConfig(BurnerConfig cfg) {
        this.config = cfg;
    }

    @Override
    public final void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public final void setStatusUpdater(StatusUpdater statusUpdater) {
        this.statusUpdater = statusUpdater;
    }

    protected final Log getLogger() {
        return LogFactory.getLog(LinuxDVDBurner.class);
    }

    protected StatusUpdater statusUpdater;

    private Component parent;

    private BurnerConfig config;
}
