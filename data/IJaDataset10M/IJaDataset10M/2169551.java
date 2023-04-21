package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;

public class ContextModel extends AbstractModelSortedModel {

    public ContextModel(boolean firstNull) {
        this.initialize(firstNull);
    }

    private void initialize(boolean firstNull) {
        if (firstNull) this.addElement(null);
        List<Context> contexts = ContextFactory.getInstance().getList();
        for (Context context : contexts) this.addElement(context);
        ContextFactory.getInstance().addListChangeListener(this);
        ContextFactory.getInstance().addPropertyChangeListener(this);
    }
}
