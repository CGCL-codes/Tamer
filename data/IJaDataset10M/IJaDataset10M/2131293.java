package org.awelements.table.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collection;

public class HiddenVerticalListFormat extends Format {

    private boolean mOrdered;

    private String mTitle = "Items";

    public boolean isOrdered() {
        return mOrdered;
    }

    public void setOrdered(boolean ordered) {
        mOrdered = ordered;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj != null) {
            if (obj instanceof Collection) write((Collection) obj, toAppendTo); else if (obj instanceof Object[]) write(Arrays.asList((Object[]) obj), toAppendTo); else toAppendTo.append(obj.toString());
        }
        return toAppendTo;
    }

    private void write(Collection collection, StringBuffer toAppendTo) {
        if (collection.isEmpty()) return;
        toAppendTo.append("<a id=\"huhu\" href=\"javascript:alert($(this).id)\">");
        toAppendTo.append(collection.size() + " " + mTitle);
        toAppendTo.append(mOrdered ? "<ol>" : "<ul>");
        for (Object obj : collection) {
            if (obj != null) {
                toAppendTo.append("<li>");
                toAppendTo.append(obj.toString());
                toAppendTo.append("</li>");
            }
        }
        toAppendTo.append(mOrdered ? "</ol>" : "</ul>");
        toAppendTo.append("</a>");
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }
}
