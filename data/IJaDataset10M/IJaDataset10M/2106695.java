package org.proteored.miapeapi.validation.ge;

import java.util.ArrayList;
import java.util.List;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.validation.AbstractMiapeValidator;
import org.proteored.miapeapi.validation.MiapeSection;
import org.proteored.miapeapi.validation.MiapeValidator;
import org.proteored.miapeapi.validation.ValidationReport;

public class MiapeGEValidator extends AbstractMiapeValidator implements MiapeValidator<MiapeGEDocument> {

    private static MiapeGEValidator instance;

    private MiapeGEValidator() {
    }

    public static MiapeGEValidator getInstance() {
        if (instance == null) {
            instance = new MiapeGEValidator();
        }
        return instance;
    }

    @Override
    public ValidationReport getReport(MiapeGEDocument miape) {
        List<MiapeSection> missingSections = new ArrayList<MiapeSection>();
        List<MiapeSection> validSections = new ArrayList<MiapeSection>();
        boolean isCompliant = validateMiape(miape, missingSections, validSections, MiapeGESection.values());
        ValidationReport result = new ValidationReport(isCompliant, missingSections, validSections);
        return result;
    }
}
