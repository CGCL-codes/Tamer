package eu.fbk.hlt.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import eu.fbk.hlt.common.conffile.Configuration;
import eu.fbk.hlt.common.conffile.Configurations;
import eu.fbk.hlt.common.conffile.Option;
import eu.fbk.hlt.common.conffile.Type;
import eu.fbk.hlt.common.module.OptionInfo;
import eu.fbk.hlt.edits.EDITS;

/**
 * @author Milen Kouylekov
 */
public class SerializationManager {

    public static final String DEFAILT_ID = "default";

    public static final String MODELPATH = "MODEL_PATH";

    public static void copy(String inputFile, String outputFile) throws EDITSException {
        try {
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception e) {
            throw new EDITSException("Could not copy " + inputFile + " into " + outputFile + " because:\n" + e.getMessage());
        }
    }

    public static String copyFile(String path, String file, Map<String, String> renames) throws EDITSException {
        if (renames.containsKey(file)) return renames.get(file);
        String newfile = "" + renames.size() + new File(file).getName();
        copy(file, path + newfile);
        newfile = "${" + MODELPATH + "}" + newfile;
        renames.put(file, newfile);
        return newfile;
    }

    public static void createModelZip(String filename, String tempdir) throws EDITSException {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(filename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            int BUFFER = 2048;
            byte data[] = new byte[BUFFER];
            File f = new File(tempdir);
            for (File fs : f.listFiles()) {
                FileInputStream fi = new FileInputStream(fs.getAbsolutePath());
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(fs.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) out.write(data, 0, count);
                out.closeEntry();
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            throw new EDITSException("Can not create a model in file " + filename + " from folder " + tempdir);
        }
    }

    public static void delete(File f) {
        for (File ff : f.listFiles()) {
            if (ff.isDirectory()) delete(ff); else ff.delete();
        }
        f.delete();
    }

    public static void export(String filename, Configurable c) throws EDITSException {
        String date = getDate() + "/";
        String tempdir = EDITS.system().tempdir() + date;
        new File(tempdir).mkdir();
        Configuration cc = SerializationManager.migrateConfiguration(c.configuration(), tempdir, new HashMap<String, String>());
        Configurations ss = new Configurations();
        ss.getModule().add(cc);
        SerializationManager.saveConfiguration(tempdir + "conf.xml", ss, false);
        c.export(tempdir, DEFAILT_ID);
        createModelZip(filename, tempdir);
        SerializationManager.delete(new File(tempdir));
    }

    public static String getDate() {
        Calendar xcal = Calendar.getInstance();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(xcal.getTime());
    }

    public static String getDateFormatted() {
        Calendar xcal = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(xcal.getTime());
    }

    public static Configurable load(String filename, String type) throws EDITSException {
        return load(filename, type, null);
    }

    public static Configurable load(String filename, String type, String tempdir) throws EDITSException {
        File file = new File(filename);
        if (!file.exists()) throw new EDITSException("The file " + filename + " does not exist!");
        if (!file.canRead()) throw new EDITSException("The file " + filename + " can not be read.");
        if (tempdir == null) tempdir = EDITS.system().tempdir();
        if (new File(tempdir).exists()) new File(tempdir).mkdir();
        tempdir = tempdir + SerializationManager.getDate() + "/";
        new File(tempdir).mkdir();
        SerializationManager.unzipModel(filename, tempdir);
        String confFile = tempdir + "/conf.xml";
        Configurations conf = loadConfigurations(confFile);
        ConfigurationLoader loader = new ConfigurationLoader(MODELPATH, tempdir);
        loader.processConfiguration(conf);
        Configurable c = ModuleLoader.loadModule(conf, type);
        if (c == null) throw new EDITSException("The file " + filename + " does not contain " + type + "!");
        c.read(tempdir, DEFAILT_ID);
        SerializationManager.delete(new File(tempdir));
        return c;
    }

    public static Configuration loadConfiguration(String filename) throws EDITSException {
        Object o = FileTools.loadObject(filename, "eu.fbk.hlt.common.conffile");
        if (o instanceof Configuration) return (Configuration) o;
        throw new EDITSException("The file " + filename + " is not in the correct format!");
    }

    public static Configurations loadConfigurations(String filename) throws EDITSException {
        Object o = FileTools.loadObject(filename, "eu.fbk.hlt.common.conffile");
        if (o instanceof Configurations) return (Configurations) o;
        throw new EDITSException("The file " + filename + " is not in the correct format!");
    }

    public static Configuration migrateConfiguration(Configuration conf, String path, Map<String, String> renames) throws EDITSException {
        Configuration out = new Configuration();
        Configurable c = (Configurable) ModuleLoader.initialize(conf.getClassName());
        Map<String, OptionInfo> options = new HashMap<String, OptionInfo>();
        for (OptionInfo s : c.info().options()) options.put(s.name(), s);
        out.setClassName(conf.getClassName());
        out.setType(conf.getType());
        out.setId(conf.getId());
        for (Option a : conf.getOption()) {
            Option na = new Option();
            OptionInfo t = options.get(a.getName());
            if (t.type().equals(Type.FILE)) {
                na.setValue(copyFile(path, a.getValue(), renames));
            } else na.setValue(a.getValue());
            na.setId(a.getId());
            na.setName(a.getName());
            out.getOption().add(na);
        }
        for (Configuration cdd : conf.getModule()) out.getModule().add(migrateConfiguration(cdd, path, renames));
        return out;
    }

    public static void saveConfiguration(String filename, Configurations doc, boolean overwrite) throws EDITSException {
        try {
            Marshaller marshaller = FileTools.header(filename, "eu.fbk.hlt.common.conffile", overwrite);
            FileOutputStream fos = new FileOutputStream(filename);
            marshaller.marshal(new eu.fbk.hlt.common.conffile.ObjectFactory().createConf(doc), fos);
            fos.close();
        } catch (JAXBException e) {
            throw new EDITSException("The system can not write in " + filename + "!");
        } catch (IOException e) {
            throw new EDITSException("The system can not write in " + filename + "!");
        }
    }

    public static void unzipModel(String filename, String tempdir) throws EDITSException {
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(filename);
            int BUFFER = 2048;
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(tempdir + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) dest.write(data, 0, count);
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            throw new EDITSException("Can not expand model in \"" + tempdir + "\" because:\n" + e.getMessage());
        }
    }
}
