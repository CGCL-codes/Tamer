package org.databene.domain.person;

import org.databene.benerator.csv.LocalCSVGenerator;
import org.databene.commons.Encodings;
import java.util.Locale;

/**
 * Creates a quota of academic titles.<br/><br/>
 * Created: 26.06.2006 19:06:23
 * @since 0.1
 * @author Volker Bergmann
 */
public class AcademicTitleGenerator extends LocalCSVGenerator<String> {

    public AcademicTitleGenerator() {
        this(Locale.getDefault());
    }

    public AcademicTitleGenerator(Locale locale) {
        super(String.class, "/org/databene/domain/person/title", locale, ".csv", Encodings.UTF_8);
    }
}
