package mobat.bonesa.dist;

import ibis.rmi.impl.RTS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.tree.DefaultMutableTreeNode;
import mobat.bonesa.Bonesa;
import mobat.bonesa.store.IBonesaAlgorithmDescription;
import mobat.bonesa.store.impl.RemoteAlgorithmDescription;
import org.apache.commons.io.FileUtils;
import mobat.bonesa.action.RemoteProgressStatus;
import mobat.bonesa.components.JSelectDirectoryDialog;
import mobat.tuning.store.IAlgorithmDescription;
import mobat.tuning.tasks.ParameterTuningTask;
import mobat.tuning.utils.Utils;

/**
 *
 * @author S.K. Smit
 * @institution: Vrije Universiteit Amsterdam
 */
public class Distribution {

    public static void create(File directory, IAlgorithmDescription[] descr, String host) {
        try {
            for (IAlgorithmDescription des : descr) {
                Bonesa.getApplication().getCurrentProject().setAlgorithmDescriptionRemote((IBonesaAlgorithmDescription) des, host);
            }
            FileUtils.deleteDirectory(directory);
            directory.mkdirs();
            createAutorunPropertyFile(directory, descr, host);
            copyFiles(directory, descr);
            zip(directory);
            Bonesa.getApplication().getMainView().startRemoteProgressMeter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createAutorunPropertyFile(File directory, IAlgorithmDescription[] descr, String host) throws FileNotFoundException, IOException {
        FileOutputStream fos = null;
        Properties autorun = new Properties();
        autorun.setProperty(mobat.satin.Launcher.BONESA_PROPERTY_HOSTNAME, host);
        autorun.setProperty(mobat.satin.Launcher.BONESA_PROPERTY_PROCESSORS, "MAX");
        autorun.setProperty(mobat.tuning.Launcher.PROPERTY_RUNDISTRIBUTED, "true");
        autorun.setProperty(mobat.tuning.Launcher.PROPERTY_PARAMETERTUNER, Bonesa.getParameterTuner().getCanonicalName());
        String algs = Utils.convertNameToSaveName(descr[0].getName()) + ".bda";
        for (int i = 1; i < descr.length; i++) {
            algs += "," + Utils.convertNameToSaveName(descr[i].getName()) + ".bda";
        }
        autorun.setProperty(mobat.tuning.Launcher.PROPERTY_ALGORITHMDESCRIPTION, algs);
        autorun.setProperty(mobat.tuning.Launcher.PROPERTY_SEED, "12345");
        if (descr.length > 1) {
            autorun.setProperty(mobat.tuning.Launcher.WAIT_WHEN_FINISHED, "false");
        } else {
            autorun.setProperty(mobat.tuning.Launcher.WAIT_WHEN_FINISHED, "true");
        }
        autorun.setProperty(RTS.SMARTSOCKETS_FORCE_CONNECTION, "false");
        for (IAlgorithmDescription des : descr) {
            for (String[] s : des.getCustomProperties()) {
                autorun.setProperty(ParameterTuningTask.CUSTOM_PROPERTY_PATH + des.getName() + "." + s[0], s[1]);
            }
        }
        fos = new FileOutputStream(directory.getCanonicalPath() + File.separator + "autorun.properties");
        autorun.store(fos, null);
        fos.close();
    }

    private static void copyFiles(File directory, IAlgorithmDescription[] descr) throws IOException {
        File programDirectory = JSelectDirectoryDialog.getProgramDirectory();
        for (IAlgorithmDescription des : descr) {
            if (des instanceof RemoteAlgorithmDescription) {
                Bonesa.saveToFile(((RemoteAlgorithmDescription) des).getRoot(), directory.getCanonicalPath() + File.separator + Utils.convertNameToSaveName(des.getName()) + ".bda");
            } else {
                Bonesa.saveToFile(des, directory.getCanonicalPath() + File.separator + Utils.convertNameToSaveName(des.getName()) + ".bda");
            }
        }
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "ParameterTuning.jar"), directory);
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "SATINDistribution.jar"), directory);
        FileUtils.copyFileToDirectory(Bonesa.getParameterTunerJar(), new File(directory.getCanonicalPath() + File.separator + "plugins"));
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "log4j.properties"), directory);
        FileUtils.copyDirectoryToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "lib"), directory);
        if (new File(programDirectory.getCanonicalPath() + File.separator + "runtime").exists()) {
            FileUtils.copyDirectoryToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "runtime"), directory);
        }
    }

    private static void zip(File d) throws FileNotFoundException, IOException {
        String[] entries = d.list();
        byte[] buffer = new byte[4096];
        int bytesRead;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(d.getParent() + File.separator + "dist.zip")));
        for (int i = 0; i < entries.length; i++) {
            File f = new File(d, entries[i]);
            if (f.isDirectory()) continue;
            FileInputStream in = new FileInputStream(f);
            int skipl = d.getCanonicalPath().length();
            ZipEntry entry = new ZipEntry(f.getPath().substring(skipl));
            out.putNextEntry(entry);
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
            in.close();
        }
        out.close();
        FileUtils.moveFile(new File(d.getParent() + File.separator + "dist.zip"), new File(d + File.separator + "dist.zip"));
    }
}
