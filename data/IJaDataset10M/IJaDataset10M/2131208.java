package org.ccnx.ccn.impl.security.crypto;

import static org.ccnx.ccn.impl.support.Serial.readObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptedObjectFileHelper {

    public static boolean writeEncryptedObject(File outputFile, Serializable objectToWrite, PublicKey keyToEncryptFor) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
        try {
            oos.writeObject(objectToWrite);
        } finally {
            oos.close();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T readEncryptedObject(File inputFile, PrivateKey decryptionKey) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(inputFile);
        ObjectInputStream input = new ObjectInputStream(fis);
        try {
            return (T) readObject(input);
        } finally {
            input.close();
        }
    }
}
