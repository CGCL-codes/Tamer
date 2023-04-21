package com.l2fprod.skinbuilder.synth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.l2fprod.common.util.LoadFailureException;
import com.l2fprod.common.util.SaveFailureException;

/**
 * Load/Save SynthConfig. <br>
 * 
 */
public class SynthConfigPersistence {

    public SynthConfig load(File configLocation) throws LoadFailureException {
        SynthConfig config;
        File serialized = new File(configLocation.getAbsolutePath());
        try {
            FileInputStream input = new FileInputStream(serialized);
            ObjectInputStream oi = new ObjectInputStream(input);
            config = (SynthConfig) oi.readObject();
            oi.close();
            input.close();
            return config;
        } catch (Throwable th) {
            throw new LoadFailureException(th);
        }
    }

    public void save(SynthConfig library, File file) throws SaveFailureException {
        try {
            File serialized = new File(file.getAbsolutePath());
            FileOutputStream output = new FileOutputStream(serialized);
            ObjectOutputStream ou = new ObjectOutputStream(output);
            ou.writeObject(library);
            ou.flush();
            ou.close();
            output.flush();
            output.close();
        } catch (Throwable th) {
            throw new SaveFailureException(th);
        }
    }
}
