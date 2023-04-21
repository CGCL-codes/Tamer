package fi.iki.juri.units.generator;

import java.util.ArrayList;
import java.util.Collection;

public class ParsedTranslations {

    public class Translation {

        private final String unit;

        private final String translation;

        public Translation(final String unit, final String translation) {
            this.unit = unit;
            this.translation = translation;
        }

        public String getTranslation() {
            return translation;
        }

        public String getUnit() {
            return unit;
        }
    }

    private final Collection<Translation> translations;

    private final String locale;

    public ParsedTranslations(final String locale) {
        this.locale = locale;
        this.translations = new ArrayList<Translation>();
    }

    public Iterable<Translation> getTranslations() {
        return translations;
    }

    public void addTranslation(final String unit, final String translation) {
        translations.add(new Translation(unit, translation));
    }

    public String getLocale() {
        return locale;
    }
}
