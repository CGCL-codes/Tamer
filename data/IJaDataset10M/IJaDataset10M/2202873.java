package sourceagile.administration.client.project;

import sourceagile.client.InternationalizationConstants;
import sourceagile.shared.entities.project.Project;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ListBox;

public class LocaleList extends ListBox {

    private static InternationalizationConstants internationalizationConstants = GWT.create(InternationalizationConstants.class);

    public LocaleList(String projectLocale) {
        this.setWidth("100px");
        this.addItem(internationalizationConstants.english(), Project.LOCALE_ENGLISH);
        this.addItem(internationalizationConstants.portugues(), Project.LOCALE_PORTUGUESE);
        this.addItem(internationalizationConstants.espanol(), Project.LOCALE_SPANISH);
        if (Project.LOCALE_ENGLISH.equals(projectLocale)) {
            this.setSelectedIndex(0);
        } else if (Project.LOCALE_PORTUGUESE.equals(projectLocale)) {
            this.setSelectedIndex(1);
        } else if (Project.LOCALE_SPANISH.equals(projectLocale)) {
            this.setSelectedIndex(2);
        }
    }
}
