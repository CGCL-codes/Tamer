package org.efs.openreports.util;

import java.util.Locale;
import org.displaytag.decorator.TableDecorator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;

public class HRefColumnDecorator extends TableDecorator implements LocaleProvider {

    private final transient TextProvider textProvider;

    public HRefColumnDecorator() {
        textProvider = new TextProviderFactory().createInstance(getClass(), this);
    }

    public Locale getLocale() {
        return ActionContext.getContext().getLocale();
    }

    public Object getRemoveLink() {
        return textProvider.getText(LocalStrings.LINK_DELETE);
    }

    public Object getUpdateLink() {
        return textProvider.getText(LocalStrings.LINK_UPDATE);
    }

    public Object getAddToGroupLink() {
        return textProvider.getText(LocalStrings.LINK_GROUPS);
    }

    public Object getUsersLink() {
        return textProvider.getText(LocalStrings.LINK_USERS);
    }
}
