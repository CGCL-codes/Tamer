package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DeviceModel extends ControlVocabularySet {

    private static DeviceModel instance;

    public static DeviceModel getInstance(ControlVocabularyManager cvManager) {
        if (instance == null) instance = new DeviceModel(cvManager);
        return instance;
    }

    private DeviceModel(ControlVocabularyManager cvManager) {
        super(cvManager);
        String[] parentAccessionsTMP = { "sep:00075" };
        this.parentAccessions = parentAccessionsTMP;
        this.miapeSection = 52;
    }
}
