package com.wisc.csvParser.vocabProviders;

import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JPanel;
import org.jdom.Element;

/**
 *
 * @author lawinslow
 */
public class HoboVocabProvider implements IVocabProvider {

    public HashSet<String> getVocab(String vocabType) {
        HashSet<String> vocab = new HashSet<String>();
        if (vocabType.compareToIgnoreCase("sites") == 0) {
            vocab.add("crystal bog n");
            vocab.add("crystal bog s");
            vocab.add("crystal bog e");
            vocab.add("crystal bog w");
            vocab.add("crystal bog center");
        } else if (vocabType.compareToIgnoreCase("offsettypes") == 0) {
            vocab.add("depth");
        } else if (vocabType.compareToIgnoreCase("sources") == 0) {
            vocab.add("cfl");
            vocab.add("DNR-TLS");
        } else if (vocabType.compareToIgnoreCase("aggmethods") == 0) {
            vocab.add("inst");
            vocab.add("sum");
            vocab.add("mean");
        } else if (vocabType.compareToIgnoreCase("units") == 0) {
            vocab.add("m");
            vocab.add("mm");
            vocab.add("C");
            vocab.add("millisiemens");
        } else if (vocabType.compareToIgnoreCase("variables") == 0) {
            vocab.add("precipitation");
            vocab.add("water_temp");
            vocab.add("conductivity");
            vocab.add("depth");
        }
        return vocab;
    }

    public HashSet<String> getVocabTypes() {
        HashSet<String> vocab = new HashSet<String>();
        vocab.add("sites");
        vocab.add("offsettypes");
        vocab.add("sources");
        vocab.add("aggmethods");
        vocab.add("units");
        vocab.add("variables");
        return vocab;
    }

    public void updateLocalVocab() {
    }

    public JPanel getSettingsJPanel() {
        return new JPanel();
    }

    public String updateVocab(String vocabType, String currentVocab) {
        if (vocabType.compareToIgnoreCase("variables") == 0 && currentVocab.compareToIgnoreCase("precip") == 0) {
            return "precipitation";
        }
        return currentVocab;
    }

    public Element getSettingsXml() {
        Element e = new Element(IVocabProvider.VOCAB_PROVIDER_TAG);
        e.setAttribute("type", HoboVocabProvider.class.getName());
        return e;
    }

    public void configure(Element e) {
    }
}
