package org.jawara.modules;

import org.jawara.lang.Context;

public abstract class LoadableModule {

    public abstract void onLoad(Context ctxt);

    public void load(Context ctxt) {
        onLoad(ctxt);
    }
}
