package com.genia.toolbox.web.gwt.form.client.form.impl;

import com.genia.toolbox.web.gwt.form.client.form.PasswordBoxItem;
import com.genia.toolbox.web.gwt.form.client.visitor.FormVisitor;

/**
 * implementation of interface PasswordBoxItem.
 */
public class PasswordBoxItemImpl extends AbstractBasicItem implements PasswordBoxItem {

    /**
   * Make class visitable by visitor.
   * 
   * @param visitor
   *          is of type FormVisitor.
   */
    public void accept(final FormVisitor visitor) {
        visitor.visitPasswordBoxItem(this);
    }

    /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return obj instanceof PasswordBoxItemImpl;
    }
}
