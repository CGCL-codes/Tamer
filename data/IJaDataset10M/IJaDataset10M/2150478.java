package test.base.jdbs.cryptography.symmetric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import test.base.jdbs.AllTests;
import base.jdbs.cryptography.symmetric.DESSymmetricCipher;
import base.jdbs.cryptography.symmetric.SymmetricKey;
import base.util.FileUtil;

public class DESSymmetricCipherTest extends TestCase {

    private DESSymmetricCipher DESCipher;

    private File plainFile = new File(AllTests.REPOSITORY_PATH + File.separatorChar + "strings.txt");

    private File encFile = new File(AllTests.REPOSITORY_PATH + File.separatorChar + "strings_enc.txt");

    private File decFile = new File(AllTests.REPOSITORY_PATH + File.separatorChar + "strings_dec.txt");

    private static final boolean RUN_SILENTLY = true;

    private static final transient Logger logger = Logger.getLogger(DESSymmetricCipherTest.class.getName());

    public void setUp() throws IOException {
        DOMConfigurator.configure("log4jConfiguration.xml");
        DESCipher = new DESSymmetricCipher(new SymmetricKey("pippo"));
        if (RUN_SILENTLY) {
            plainFile.getParentFile().mkdirs();
            plainFile.createNewFile();
            encFile.createNewFile();
            decFile.createNewFile();
            FileWriter fileWriter = new FileWriter(plainFile);
            for (int k = 0; k < 100; k++) fileWriter.append("Line " + k + " : THIS IS A SIMPLE TEST FILE CONTENT!");
            fileWriter.close();
        } else {
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Select the file to be encrypted...");
            chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
            chooser.setApproveButtonText("Select");
            if (chooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) plainFile = new File(chooser.getSelectedFile().getAbsolutePath());
            chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Select the encryption output file...");
            chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
            chooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
            chooser.setApproveButtonText("Select");
            if (chooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) encFile = new File(chooser.getSelectedFile().getAbsolutePath());
            chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Select the decryption output file...");
            chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
            chooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
            chooser.setApproveButtonText("Select");
            if (chooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) decFile = new File(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void test() throws IOException {
        if (plainFile != null && encFile != null) {
            DESCipher.encrypt(plainFile, encFile);
        }
        if (encFile != null && decFile != null) {
            DESCipher.decrypt(encFile, decFile);
        }
        if (RUN_SILENTLY && plainFile.exists() && encFile.exists() && decFile.exists()) {
            logger.info("PlainFile     " + plainFile + "     Size: " + FileUtil.fileSizeInByte(plainFile) + " Length: " + FileUtil.fileContent(plainFile).length());
            logger.info("EncryptedFile " + encFile + " Size: " + FileUtil.fileSizeInByte(encFile) + " Length: " + FileUtil.fileContent(encFile).length());
            logger.info("DecryptedFile " + decFile + " Size: " + FileUtil.fileSizeInByte(decFile) + " Length: " + FileUtil.fileContent(decFile).length());
            assertTrue(FileUtil.fileContent(plainFile).length() == FileUtil.fileContent(decFile).length());
            assertEquals(FileUtil.fileContent(plainFile), FileUtil.fileContent(decFile));
            assertTrue(FileUtil.fileSizeInByte(plainFile) == FileUtil.fileSizeInByte(decFile));
        }
    }
}
