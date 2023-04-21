package org.dmd.mvw.tools.mvwgenerator.extended.menus;

import org.dmd.dms.ClassDefinition;
import org.dmd.mvw.tools.mvwgenerator.generated.dmo.MenuDMO;
import org.dmd.mvw.tools.mvwgenerator.generated.dmw.MenuDMW;

public abstract class Menu extends MenuDMW {

    public Menu() {
    }

    protected Menu(MenuDMO dmo, ClassDefinition cd) {
        super(dmo, cd);
    }
}
