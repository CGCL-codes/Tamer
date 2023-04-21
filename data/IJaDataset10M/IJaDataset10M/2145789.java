package de.uni_leipzig.lots.webfrontend.views;

import org.jetbrains.annotations.NotNull;
import java.util.Locale;

/**
 * @author Alexander Kiel
 * @version $Id: PageDataSupport.java,v 1.8 2007/10/23 06:30:31 mai99bxd Exp $
 */
public class PageDataSupport implements PageData {

    @NotNull
    protected final Locale locale;

    public PageDataSupport(@NotNull Locale locale) {
        this.locale = locale;
    }
}
