package corner.orm.tapestry.jasper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import corner.demo.model.one.A;
import corner.util.BaseModelTestCase;

/**
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision: 4121 $
 * @since 2.3.7
 */
public class UnZip extends BaseModelTestCase {

    String fileName = System.getProperty("user.dir") + "\\src\\test\\resources\\jasper\\Test.zip";

    String tmpdir = System.getProperty("java.io.tmpdir");

    /**
	 * @see com.bjmaxinfo.piano.model.BaseModelTestCase#getMappingDirectoryLocations()
	 */
    @Override
    protected String[] getMappingDirectoryLocations() {
        return new String[] { "classpath:/corner/demo/model/one" };
    }

    static final int BUFFER = 2048;

    /**
	 * 
	 */
    public void testUnZipBlobSingleFile() {
        A main = null;
        main = (A) this.load(A.class, "282828c1154a53a501154a54c4780001");
        InputStream bos = new ByteArrayInputStream(main.getBlobData());
        ZipInputStream zis = new ZipInputStream(bos);
        BufferedOutputStream dest = null;
        ZipEntry entry = null;
        String fileName = "Atest.jasper".toLowerCase();
        try {
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName().toLowerCase();
                if (entryName.endsWith(fileName)) {
                    System.out.println("Extracting: " + entry.getName() + "\t" + entry.getSize() + "\t" + entry.getCompressedSize());
                    int count;
                    byte data[] = new byte[BUFFER];
                    ByteArrayOutputStream fos = new ByteArrayOutputStream((int) entry.getSize());
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    byte[] b = fos.toByteArray();
                    System.out.println("length  " + b.length);
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testUnZipBlob() {
        A main = null;
        main = (A) this.load(A.class, "282828c1154a53a501154a54c4780001");
        InputStream bos = new ByteArrayInputStream(main.getBlobData());
        ZipInputStream zis = new ZipInputStream(bos);
        BufferedOutputStream dest = null;
        ZipEntry entry = null;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(tmpdir + entry.getName()).mkdirs();
                    continue;
                }
                System.out.println("Extracting: " + entry.getName() + "\t" + entry.getSize() + "\t" + entry.getCompressedSize());
                int count;
                byte data[] = new byte[BUFFER];
                File file = new File(tmpdir + entry.getName());
                FileOutputStream fos = new FileOutputStream(file);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testUnZip() {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.UnZipAction(zipFile);
    }

    /**
	 * 
	 * 处理需要解压缩的文件
	 */
    private void UnZipAction(ZipFile zipFile) {
        try {
            Enumeration emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) emu.nextElement();
                if (entry.isDirectory()) {
                    new File(tmpdir + entry.getName()).mkdirs();
                    continue;
                }
                System.out.println("Extracting: " + entry.getName() + "\t" + entry.getSize() + "\t" + entry.getCompressedSize());
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                File file = new File(tmpdir + entry.getName());
                File parent = file.getParentFile();
                if (parent != null && (!parent.exists())) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                int count;
                byte data[] = new byte[BUFFER];
                while ((count = bis.read(data, 0, BUFFER)) != -1) {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
            zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
