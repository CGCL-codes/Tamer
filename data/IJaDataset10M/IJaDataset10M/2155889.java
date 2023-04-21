package org.bee.tl.ext;

import java.io.IOException;
import java.io.Writer;
import org.bee.tl.core.Context;
import org.bee.tl.core.Function;
import org.bee.tl.core.Template;

public class Println implements Function {

    @Override
    public String call(Object[] paras, Context ctx) {
        Object o = paras[0];
        Writer w = (Writer) ctx.getVar("__pw");
        Template t = (Template) ctx.getVar("__this");
        if (o != null) {
            try {
                w.write(o.toString());
                w.write(t.getCR());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
}
