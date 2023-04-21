package com.qallme.framework.tools.presentation;

import com.qallme.framework.QAObject;
import com.qallme.framework.QallmeModule;
import com.qallme.framework.QallmeSetModule;

public class InstructionPresenter extends QallmeSetModule {

    @Override
    public boolean process(QAObject o) {
        String presenter = (String) o.getParams().get("presenter");
        System.out.println("Presenter: " + presenter);
        return get(presenter).process(o);
    }

    public QallmeModule get(String id) {
        QallmeModule defaultP = null;
        for (QallmeModule m : getModules()) {
            System.out.println(m.id());
            if (m.id().endsWith(".SimplePresenter")) defaultP = m;
            if (m.id().endsWith("." + id)) return m;
        }
        return defaultP;
    }
}
