package org.freeform.form;

import java.io.IOException;
import org.freeform.Pkg;
import org.freeform.Root;

public class FormPkg extends Pkg {

    public static final String FF = "use f = ff.func;" + "use e = ff.exp;" + "use fl = ff.func.lang;" + "exp embed = {'@' formatter>f}-> <fl> (obj) => (...a) => ff.prim.string.cat(...ff.prim.list.toArray(obj.f(...a))); " + "exp arg = '_' -> <fl> (l) => (e) => e; " + "exp id = e.path -> <fl> (path) => processId(##, path);" + "exp formatter = embed|arg|id|f.func; " + "exp format = formatter* -> <fl> (list) => processFormat(list);" + "exp form = {'form' id>name '=' format ';'} -> <fl> (obj) => f.name(obj.name, obj.format);" + "name lang = form;";

    public FormPkg(Root root) throws IOException {
        super(root, root.stdExp());
        add("processFormat", new ProcessFormat());
        add("processId", new ProcessId());
        feed(FF);
    }
}
