package keypair;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import guiLogics.*;

/**
 * Save/load public/private key to/from the file.
 * 
 * @author Petri Tuononen
 * @since 2.2.2009
 */
public class OpenSave {

    private EncodeDecode encDec;

    private LoadSaveKey loadSave;

    private RsaPublicKey pubKey;

    private RsaPrivateKey privKey;

    private File file;

    private JFrame frame;

    byte[] encoded;

    /**
	 * Constructor.
	 * @param frame
	 */
    public OpenSave(JFrame frame) {
        this.frame = frame;
    }

    /**
	 * Returns the user selected file.
	 * File to load.
	 * @return file File to load.
	 */
    public File chooseFileToLoad() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new RsaKeyFileFilter());
        int result = fc.showOpenDialog(frame);
        switch(result) {
            case JFileChooser.APPROVE_OPTION:
                if (fc.getSelectedFile() != null) {
                    file = fc.getSelectedFile();
                } else {
                    JOptionPane.showMessageDialog(frame, "No file was selected", "File selection info", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                JOptionPane.showMessageDialog(frame, "An error occured while selecting a file to load", "File selection error", JOptionPane.ERROR_MESSAGE);
                break;
        }
        return file;
    }

    /**
	 * Returns the user selected file.
	 * @return file File to save.
	 */
    public File chooseFileToSave() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new RsaKeyFileFilter());
        int result = fc.showSaveDialog(frame);
        switch(result) {
            case JFileChooser.APPROVE_OPTION:
                if (fc.getSelectedFile() != null) {
                    file = fc.getSelectedFile();
                } else {
                    JOptionPane.showMessageDialog(frame, "No file was selected", "File selection info", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                JOptionPane.showMessageDialog(frame, "An error occured while selecting a file to save", "File selection error", JOptionPane.ERROR_MESSAGE);
                break;
        }
        return file;
    }

    /**
	 * Saves public key in encoded format to the file.
	 * @param publicKey Public key to be saved.
	 */
    public void savePublicKey(RsaPublicKey publicKey) {
        encDec = new EncodeDecode();
        encoded = encDec.encPublicKey(publicKey);
        file = chooseFileToSave();
        if (file != null) {
            String[] splits = file.getName().split("\\.");
            String extension = splits[splits.length - 1];
            if (!extension.equals("pub")) {
                String newFileName = file.getName() + ".pub";
                int length = file.getPath().length() - file.getName().length();
                String newFilePath = file.getPath().substring(0, length) + newFileName;
                file = new File(newFilePath);
            }
            loadSave = new LoadSaveKey(frame);
            loadSave.saveKeyToFile(encoded, file);
        }
    }

    /**
	 * Saves private key in encoded format to the file.
	 * @param privateKey Private key to be saved.
	 */
    public void savePrivateKey(RsaPrivateKey privateKey) {
        encDec = new EncodeDecode();
        encoded = encDec.encPrivateKey(privateKey);
        file = chooseFileToSave();
        if (file != null) {
            String[] splits = file.getName().split("\\.");
            String extension = splits[splits.length - 1];
            if (!extension.equals("priv")) {
                String newFileName = file.getName() + ".priv";
                int length = file.getPath().length() - file.getName().length();
                String newFilePath = file.getPath().substring(0, length) + newFileName;
                file = new File(newFilePath);
            }
            loadSave = new LoadSaveKey(frame);
            loadSave.saveKeyToFile(encoded, file);
        }
    }

    /**
	 * Loads public key from the file to instance.
	 * Encoded byte array from the file is first decoded.
	 * 
	 * @return Public key instance.
	 */
    public RsaPublicKey loadPublicKey() {
        file = chooseFileToLoad();
        if (file == null) {
            pubKey = null;
        } else {
            loadSave = new LoadSaveKey(frame);
            encoded = loadSave.loadKeyFromFile(file);
            encDec = new EncodeDecode();
            pubKey = encDec.decPublicKey(encoded);
        }
        return pubKey;
    }

    /**
	 * Loads private key from the file to instance.
	 * Encoded byte array from the file is first decoded.
	 * 
	 * @return Private key instance.
	 */
    public RsaPrivateKey loadPrivateKey() {
        file = chooseFileToLoad();
        if (file == null) {
            privKey = null;
        } else {
            loadSave = new LoadSaveKey(frame);
            encoded = loadSave.loadKeyFromFile(file);
            encDec = new EncodeDecode();
            privKey = encDec.decPrivateKey(encoded);
        }
        return privKey;
    }
}
