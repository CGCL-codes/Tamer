package com.mainatom.af;

public class ModuleCore extends AModule {

    public void prepareDef(ADef def) {
        def.addPackage("com.mainatom.af");
        def.addPackage("com.mainatom.tab");
        def.addPackage("com.mainatom.db");
        def.addPackage("com.mainatom.dh");
        def.addPackage("com.mainatom.gen");
        def.addPackage("com.mainatom.script");
    }
}
