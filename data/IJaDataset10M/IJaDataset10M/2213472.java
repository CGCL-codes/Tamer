package de.t5book.pages;

import java.util.List;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import de.t5book.entities.Language;

public class PaletteExample {

    @Inject
    private Messages messages;

    @Persist
    private List<Language> selectedLanguages;

    public SelectModel getLanguagesModel() {
        return new EnumSelectModel(Language.class, messages);
    }

    public ValueEncoder getLanguageEncoder() {
        return new EnumValueEncoder(Language.class);
    }

    void onSubmit() {
        for (Language language : selectedLanguages) {
            System.out.println(language.name());
        }
    }

    public List<Language> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<Language> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }
}
