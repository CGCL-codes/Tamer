package org.merak.core.text.json.jsonfiers;

import java.util.Collection;
import java.util.Iterator;
import org.merak.core.text.json.JsonContext;

public class CollectionJsonfier extends Jsonfier {

    @Override
    public void render(Object object, JsonContext context) throws Exception {
        Collection<?> collection = (Collection<?>) object;
        StringBuffer json = context.getBuffer();
        if (collection.isEmpty()) {
            json.append("[]");
            return;
        }
        Iterator<?> iter = collection.iterator();
        Object item = iter.next();
        Jsonfier jsonfier = context.getJsonfier(item);
        json.append('[');
        jsonfier.render(item, context);
        while (iter.hasNext()) {
            json.append(',');
            jsonfier.render(iter.next(), context);
        }
        json.append(']');
    }
}
