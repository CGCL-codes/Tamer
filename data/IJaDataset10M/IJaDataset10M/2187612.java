package org.jfeature.sample.helper.features;

import org.jfeature.Element;
import org.jfeature.feature.GetElement;

public interface F_ExperienceInYears {

    @Element("experienceInYears")
    @GetElement
    Integer getExperienceInYears();
}
