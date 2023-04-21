package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageResolutionUnit extends ControlVocabularySet {

    private static ImageResolutionUnit instance;

    public static ImageResolutionUnit getInstance(ControlVocabularyManager cvManager) {
        if (instance == null) instance = new ImageResolutionUnit(cvManager);
        return instance;
    }

    private ImageResolutionUnit(ControlVocabularyManager cvManager) {
        super(cvManager);
        this.miapeSection = 31;
        String[] explicitAccessionsTMP = { "UO:0000242", "UO:0000243", "UO:0000240", "UO:0000017" };
        this.explicitAccessions = explicitAccessionsTMP;
    }
}
