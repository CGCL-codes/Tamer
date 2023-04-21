package net.sf.kpex.gui.builtins;

import net.sf.kpex.gui.JinniContainer;
import net.sf.kpex.gui.JinniImagePanel;
import net.sf.kpex.prolog.Const;
import net.sf.kpex.prolog.FunBuiltin;
import net.sf.kpex.prolog.Int;
import net.sf.kpex.prolog.JavaObject;
import net.sf.kpex.prolog.Prog;

@Deprecated
public class NewImage extends FunBuiltin {

    public NewImage() {
        super("new_image", 5);
    }

    @Override
    public int exec(Prog p) {
        JinniContainer C = (JinniContainer) ((JavaObject) getArg(0)).toObject();
        String src = ((Const) getArg(1)).getName();
        int width = (int) ((Int) getArg(2)).getValue();
        int height = (int) ((Int) getArg(3)).getValue();
        JinniImagePanel P = new JinniImagePanel(src, width, height);
        C.add_it(P);
        JavaObject JP = new JavaObject(P);
        return putArg(4, JP, p);
    }
}
