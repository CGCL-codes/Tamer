package net.sf.signs.editors;

import net.sf.signs.iscas.ISCASCompiler;

public class ISCASEditor extends SignsEditor {

    public ISCASEditor() {
        super(new ISCASCompiler(), new ISCASScanner(), new String[] { "#", "" });
    }
}
