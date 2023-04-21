package twoadw.wicket.generic.informationpages;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.modelibra.wicket.util.LocalizedText;
import twoadw.generic.informationpage.InformationPage;
import twoadw.generic.informationpage.InformationPages;
import twoadw.website.customer.Customers;
import twoadw.wicket.app.twoadw.TwoAdwBasePage;
import wicket.contrib.tinymce.InPlaceEditComponent;
import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;

public class InformationPageView extends TwoAdwBasePage {

    private InformationPages infoPages;

    public InformationPageView(final String selectedCodePage) {
        infoPages = getInformationPages();
        final InformationPage infoPage = infoPages.getInformationPage("codePage", selectedCodePage);
        add(new Label("title", infoPage.getTitle()));
        add(new Label("page", infoPage.getPage()).setEscapeModelStrings(false));
        TinyMCESettings mceSettings = new TinyMCESettings(Theme.advanced);
        mceSettings.setToolbarLocation(TinyMCESettings.Location.top);
        mceSettings.setStatusbarLocation(TinyMCESettings.Location.bottom);
        final Form form = new Form("form");
        final TextArea textArea = new TextArea("richTextInput", new PropertyModel(infoPage, "page"));
        textArea.setEscapeModelStrings(false);
        textArea.add(new TinyMceBehavior(mceSettings));
        form.add(textArea);
        if (getTwoadwAppSession().getCustomer() != null) if (Integer.valueOf(getTwoadwAppSession().getCustomer().getSecurityRole()) >= 3) {
            form.add(new Button("modify") {

                @Override
                public void onSubmit() {
                    InformationPages informationPages = getInformationPages();
                    InformationPage tempPage = infoPage.copy();
                    tempPage.setPage(textArea.getModelObjectAsString());
                    if (informationPages.update(infoPage, tempPage)) {
                        try {
                            informationPages.update(infoPage, tempPage);
                            setResponsePage(new InformationPageView(selectedCodePage));
                        } catch (Exception e) {
                            error("AboutUs Page was not saved: " + e.getMessage());
                        }
                    } else {
                        List<String> errorKeys = informationPages.getErrors().getKeyList();
                        for (String errorKey : errorKeys) {
                            String errorMsg = LocalizedText.getErrorMessage(this, errorKey);
                            form.error(errorMsg);
                        }
                    }
                }
            });
        }
        form.setVisible(false);
        if (getTwoadwAppSession().getCustomer() != null) if (Integer.valueOf(getTwoadwAppSession().getCustomer().getSecurityRole()) >= 3) {
            form.setVisible(true);
        }
        add(form);
    }
}
