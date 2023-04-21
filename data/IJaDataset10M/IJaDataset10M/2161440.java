package net.sf.warpcore.cms.webfrontend.editor.atom.view;

import net.sf.wedgetarian.*;
import net.sf.warpcore.cms.webfrontend.editor.atom.*;
import net.sf.warpcore.cms.webfrontend.editor.editor.*;
import java.util.*;

public class NullView extends View {

    public NullView(AtomManager atomManager) {
        super(atomManager);
    }

    public NullView() {
    }

    public Wedget getEditElement(Atom atom) {
        if (atom.getChildCount() != 0 && !super.tree.isExpanded(atom)) {
            Enumeration ch = atom.children();
            while (ch.hasMoreElements()) {
                Atom tmpAtom = (Atom) ch.nextElement();
                if (tmpAtom.getControler().getAttrAsBoolean("useAsTitle")) {
                    tmpAtom.setInit(true);
                    atom.setInit(true);
                    return tmpAtom.getView().getEditWedget(tmpAtom);
                }
            }
            return new Label("");
        } else return new Label("");
    }
}
