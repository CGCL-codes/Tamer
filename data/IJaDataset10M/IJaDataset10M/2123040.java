package de.uni_hamburg.golem.target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import de.uni_hamburg.golem.model.GDevice;
import de.uni_hamburg.golem.model.GEnterprisePackage;
import de.uni_hamburg.golem.model.GWriterDevice;

public class FileDevice extends GDevice implements GWriterDevice {

    String directory;

    String filename;

    boolean appendUnique;

    boolean zipped;

    /**
	 *
	 */
    public FileDevice() {
        super();
    }

    /**
	 * @param directory
	 * @param filename
	 * @param appendUnique
	 * @param zipped
	 */
    public FileDevice(String directory, String filename, boolean appendUnique, boolean zipped) {
        super();
        this.directory = directory;
        this.filename = filename;
        this.appendUnique = appendUnique;
        this.zipped = zipped;
    }

    /**
	 * @return the directory
	 */
    public String getDirectory() {
        return directory;
    }

    /**
	 * @param directory the directory to set
	 */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
	 * @return the filename
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
	 * @return the appendUnique
	 */
    public boolean isAppendUnique() {
        return appendUnique;
    }

    /**
	 * @param appendUnique the appendUnique to set
	 */
    public void setAppendUnique(boolean appendUnique) {
        this.appendUnique = appendUnique;
    }

    /**
	 * @return the zipped
	 */
    public boolean isZipped() {
        return zipped;
    }

    /**
	 * @param zipped the zipped to set
	 */
    public void setZipped(boolean zipped) {
        this.zipped = zipped;
    }

    @Override
    public String getID() {
        return getDirectory() + File.separator + getFilename();
    }

    public GEnterprisePackage write(GEnterprisePackage pkg) throws Exception {
        String suffix = "_ims.xml";
        if (zipped) {
            suffix = "_ims.zip";
        }
        File f = null;
        if (appendUnique) {
            f = File.createTempFile(filename, suffix, new File(this.directory));
        } else {
            f = new File(this.directory + File.separator + filename + suffix);
        }
        if (zipped) {
            FileOutputStream bout = new FileOutputStream(f);
            ZipOutputStream zipout = new ZipOutputStream(bout);
            ZipEntry ze = new ZipEntry(filename + "_ims.xml");
            zipout.putNextEntry(ze);
            pkg.write(zipout);
            zipout.closeEntry();
            zipout.close();
        } else {
            FileOutputStream out = new FileOutputStream(f);
            pkg.write(out);
            out.close();
        }
        return new GEnterprisePackage();
    }
}
