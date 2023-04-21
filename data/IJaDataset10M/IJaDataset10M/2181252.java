package org.merak.core.text.json.jsonfiers;

import java.util.Date;
import org.merak.core.text.json.JsonContext;

public class DateJsonfier extends Jsonfier {

    @Override
    public void render(Object object, JsonContext context) throws Exception {
        if (object == null) {
            context.getBuffer().append("null");
        } else {
            context.getBuffer().append(((Date) object).getTime());
        }
    }
}
