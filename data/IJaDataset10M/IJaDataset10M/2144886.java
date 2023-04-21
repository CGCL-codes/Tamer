package hu.ihash.common.test;

import hu.ihash.common.model.file.FileWalker;
import hu.ihash.common.model.file.ImageFilter;
import hu.ihash.common.model.storage.ImageHashElem;
import hu.ihash.hashing.methods.FileHashUtils;
import hu.ihash.hashing.methods.ImageHashingMethods;
import hu.ihash.hashing.methods.versions.IHash;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;

public class ImageHashTest {

    @Ignore
    @Test
    public void testHashing() throws IOException {
        File file1 = new File("testimages/same/beach3.jpg");
        byte[] md51 = FileHashUtils.md5(file1);
        IHash hash1 = ImageHashingMethods.imageHash(file1);
        ImageHashElem ihe1 = new ImageHashElem(file1.getAbsolutePath(), hash1, md51);
        File file2 = new File("testimages/same/beach3_JPG.jpg");
        byte[] md52 = FileHashUtils.md5(file2);
        IHash hash2 = ImageHashingMethods.imageHash(file2);
        ImageHashElem ihe2 = new ImageHashElem(file2.getAbsolutePath(), hash2, md52);
        File file3 = new File("testimages/same/beach3_COLOR.jpg");
        byte[] md53 = FileHashUtils.md5(file3);
        IHash hash3 = ImageHashingMethods.imageHash(file3);
        ImageHashElem ihe3 = new ImageHashElem(file3.getAbsolutePath(), hash3, md53);
        System.out.println(ihe1.filepath + " <> " + ihe2.filepath);
        hash1.compareShow(hash2);
        System.out.println(ihe1.filepath + " <> " + ihe3.filepath);
        hash1.compareShow(hash3);
        System.out.println(ihe1.hashCode() + " " + ihe2.hashCode() + " " + ihe3.hashCode());
    }

    @Ignore
    @Test
    public void testHashFunction() throws IOException {
        ArrayList<ImageHashElem> ihes = new ArrayList<ImageHashElem>();
        FileWalker fw = new FileWalker(ImageFilter.File);
        fw.walk(new String[] { "testimages" });
        for (File file : fw) {
            IHash hash = ImageHashingMethods.imageHash(file);
            byte[] md5 = FileHashUtils.md5(file);
            ImageHashElem ihe = new ImageHashElem(file.getAbsolutePath(), hash, md5);
            ihes.add(ihe);
        }
        for (int i = 0; i < ihes.size(); i++) {
            ImageHashElem ihe1 = ihes.get(i);
            for (int j = i + 1; j < ihes.size(); j++) {
                ImageHashElem ihe2 = ihes.get(j);
                boolean eq = ihe1.equals(ihe2);
                boolean heq = ihe1.hashCode() == ihe2.hashCode();
                if (eq && !heq) {
                    System.err.println("ERROR: " + ihe1.filepath + " == " + ihe2.filepath + "\n HASH MISMATCH: " + ihe1.hashCode() + " != " + ihe2.hashCode());
                }
                if (!eq && heq) {
                    System.err.println("WARNING: " + ihe1.filepath + " == " + ihe2.filepath + "\n HASH COLLISION");
                }
            }
        }
    }
}
