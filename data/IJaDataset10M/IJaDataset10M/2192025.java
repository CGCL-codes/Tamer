package de.fbodewald.computersicherheit.client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import javax.swing.JFileChooser;
import de.fbodewald.computersicherheit.Utils;
import de.fbodewald.computersicherheit.model.Key;
import de.fbodewald.computersicherheit.model.Person;
import de.fbodewald.computersicherheit.model.Signature;

public class ClientController {

    private MainFrame mainFrame = null;

    private Person person;

    private String filename;

    private File file = null;

    private Signature signatureToTest = null;

    private File currentDir;

    public void addMainFrame(MainFrame mainFrame) {
        if (this.mainFrame == null) {
            this.mainFrame = mainFrame;
            mainFrame.setManager(this);
        }
    }

    public void hitWriteSignatureButton() {
        File signatureFile = new File(file.getParent(), "signature" + person.getPublicKey().getName() + ".sig");
        Utils.serialize(person.getSignature(), signatureFile);
        log("write signature successfully to disc: " + signatureFile);
        File textFile = new File(file.getParent(), "msg" + person.getPublicKey().getName() + ".txt");
        try {
            FileWriter writer = new FileWriter(textFile);
            writer.append(mainFrame.getMainTextField().getText());
            writer.flush();
            writer.close();
            log("write my text successfully to disc: " + textFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hitCreateSignatureButton() {
        person.setPublicKey((Key) Utils.deserialize(file));
        log("successfully deserialized file");
        person.createSignature(mainFrame.getMainTextField().getText());
        log("successfully create signature");
        mainFrame.getWriteSignatureButton().setEnabled(true);
    }

    public void hitWritePublicKeyButton() {
        JFileChooser writePKChooser = new JFileChooser(currentDir);
        writePKChooser.setDialogTitle("Choose destination of public key");
        if (JFileChooser.APPROVE_OPTION == writePKChooser.showSaveDialog(mainFrame)) {
            this.file = writePKChooser.getSelectedFile();
            currentDir = this.file.getParentFile();
            Utils.serialize(person.getPublicKey(), file);
            log("saved my public key successfully to: " + file);
        }
    }

    public void openFile() {
        JFileChooser openFileChooser = new JFileChooser(currentDir);
        if (JFileChooser.APPROVE_OPTION == openFileChooser.showOpenDialog(mainFrame)) {
            File selFile = openFileChooser.getSelectedFile();
            currentDir = selFile.getParentFile();
            this.filename = selFile.getPath();
            try {
                File newFile = new File(filename);
                FileReader in = new FileReader(newFile);
                mainFrame.getMainTextField().read(in, newFile);
                in.close();
                log("successfully load file from disc: " + newFile);
                mainFrame.getVerifyTextButton().setEnabled(true);
            } catch (Exception e) {
                log("Exception: OpenFile error");
                e.printStackTrace();
            }
        }
    }

    private boolean loadSignature() {
        JFileChooser loadSigChooser = new JFileChooser(currentDir);
        loadSigChooser.setDialogTitle("Open a signature file (*.sig)");
        if (JFileChooser.APPROVE_OPTION == loadSigChooser.showOpenDialog(mainFrame)) {
            File f = loadSigChooser.getSelectedFile();
            currentDir = f.getParentFile();
            this.signatureToTest = (Signature) Utils.deserialize(f);
            return true;
        } else return false;
    }

    public void hitVerifyTextButton() {
        if (this.signatureToTest == null) {
            if (!loadSignature()) {
                log("You must load a signature to verify the text!");
                return;
            }
        }
        if (person.compareSignatureHashWithTextHash(this.signatureToTest, mainFrame.getMainTextField().getText())) {
            log("text successfully verified");
        } else {
            log("text not succeefully verified");
            log("something gone wrong");
        }
    }

    public void hitVerifySignatureButton() {
        if (loadSignature()) {
            log("signature checking ....");
            JFileChooser keyChooser = new JFileChooser(currentDir);
            keyChooser.setDialogTitle("Choose attestors public key");
            if (JFileChooser.APPROVE_OPTION == keyChooser.showOpenDialog(mainFrame)) {
                log("Opening Attestor Public key");
                File keyFile = keyChooser.getSelectedFile();
                currentDir = keyFile.getParentFile();
                Key attestorPublicKey = (Key) Utils.deserialize(keyFile);
                if (person.checkIfDateExpired(signatureToTest)) {
                    log("signature is expired on " + signatureToTest.getPublicKey().getExpireDate().getTime());
                    log("signature is NOT valid");
                } else {
                    if (person.verifySignature(this.signatureToTest, attestorPublicKey)) {
                        log("signature is valid!");
                    } else {
                        log("signature is NOT valid");
                    }
                }
            }
        }
    }

    private void log(String msg) {
        if (mainFrame != null) {
            StringBuffer buf = new StringBuffer();
            buf.append(mainFrame.getLogArea().getText());
            buf.append(new Date() + ": " + msg + "\n");
            mainFrame.getLogArea().setText(buf.toString());
        }
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
