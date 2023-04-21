package wicket.in.action.chapter08.locales;

import wicket.in.action.AbstractBasePage;

public class CurrentLocaleLabelPage extends AbstractBasePage {

    public CurrentLocaleLabelPage() {
        add(new CurrentLocaleLabel("label"));
    }
}
