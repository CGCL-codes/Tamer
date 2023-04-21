package com.selcukcihan.xfacej.xface;

import com.selcukcihan.xfacej.xengine.Entity;
import com.selcukcihan.xfacej.xengine.MorphChannel;
import com.selcukcihan.xfacej.xengine.MorphController;

public class ExpressionChannel extends MorphChannel {

    protected boolean updateResult(Entity result, final Entity rest) {
        return MorphController.getInstance().getBlender().blend(result, m_result);
    }

    public ExpressionChannel(final String name) {
        super(name);
    }
}
