package org.cleartk.classifier.mallet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import org.cleartk.classifier.encoder.features.NameNumber;
import org.cleartk.classifier.jar.JarStreams;
import org.cleartk.classifier.jar.SequenceClassifierBuilder_ImplBase;
import cc.mallet.fst.SimpleTagger;
import cc.mallet.fst.Transducer;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philip Ogren
 * 
 */
public class MalletCRFClassifierBuilder extends SequenceClassifierBuilder_ImplBase<MalletCRFClassifier, List<NameNumber>, String, String> {

    private static final String MODEL_NAME = "model.malletcrf";

    public File getTrainingDataFile(File dir) {
        return new File(dir, "training-data.malletcrf");
    }

    public void trainClassifier(File dir, String... args) throws Exception {
        String[] malletArgs = new String[args.length + 5];
        System.arraycopy(args, 0, malletArgs, 0, args.length);
        malletArgs[malletArgs.length - 5] = "--train";
        malletArgs[malletArgs.length - 4] = "true";
        malletArgs[malletArgs.length - 3] = "--model-file";
        malletArgs[malletArgs.length - 2] = new File(dir, MODEL_NAME).getPath();
        malletArgs[malletArgs.length - 1] = getTrainingDataFile(dir).getPath();
        String leaveMyLoggingAloneMallet = "java.util.logging.config.file";
        String propValue = System.getProperty(leaveMyLoggingAloneMallet);
        System.setProperty(leaveMyLoggingAloneMallet, "anything-but-null");
        try {
            SimpleTagger.main(malletArgs);
        } finally {
            System.getProperties().remove(leaveMyLoggingAloneMallet);
            if (propValue != null) {
                System.setProperty(leaveMyLoggingAloneMallet, propValue);
            }
        }
    }

    @Override
    protected void packageClassifier(File dir, JarOutputStream modelStream) throws IOException {
        super.packageClassifier(dir, modelStream);
        JarStreams.putNextJarEntry(modelStream, MODEL_NAME, new File(dir, MODEL_NAME));
    }

    protected Transducer transducer;

    @Override
    protected void unpackageClassifier(JarInputStream modelStream) throws IOException {
        super.unpackageClassifier(modelStream);
        JarStreams.getNextJarEntry(modelStream, MODEL_NAME);
        ObjectInputStream objectStream = new ObjectInputStream(modelStream);
        try {
            this.transducer = (Transducer) objectStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected MalletCRFClassifier newClassifier() {
        return new MalletCRFClassifier(this.featuresEncoder, this.outcomeEncoder, this.transducer);
    }
}
