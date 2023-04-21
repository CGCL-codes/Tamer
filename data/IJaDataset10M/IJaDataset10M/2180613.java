package org.odlabs.wiquery.core.options;

import java.util.ArrayList;

/**
 * Array of IListItemOptions
 * 
 * @author Julien Roche
 * 
 */
public class ArrayItemOptions<E extends IListItemOption> extends ArrayList<E> implements ICollectionItemOptions {

    /** Constant of serialization */
    private static final long serialVersionUID = 1779802328333735627L;

    public CharSequence getJavascriptOption() {
        StringBuffer javascript = new StringBuffer();
        javascript.append("[");
        if (!isEmpty()) {
            for (IListItemOption itemOption : this) {
                javascript.append(itemOption.getJavascriptOption());
                javascript.append(",");
            }
            javascript.replace(javascript.length() - 1, javascript.length(), "");
        }
        javascript.append("]");
        return javascript;
    }

    public IListItemOption[] values() {
        return this.toArray(new IListItemOption[this.size()]);
    }
}
