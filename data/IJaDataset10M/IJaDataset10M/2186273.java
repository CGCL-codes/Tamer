package tikara.gui.shared;

import tikara.gui.main.Context;
import tikara.gui.utilities.Language;

public class TikFileFilter extends SimpleFileFilter {

    public TikFileFilter() {
        super("tik");
    }

    @Override
    public String getDescription() {
        Language lang = Context.getInstance().getTikaraModel().getCurrentLanguage();
        return lang.getTikFileFilterDescription();
    }
}
