package org.mariella.adapters.glue;

import org.mariella.glue.service.Context;
import org.mariella.rcp.adapters.DefaultAdapterContext;
import org.mariella.rcp.databinding.VBindingContext;

public class DefaultGlueAdapterContext extends DefaultAdapterContext implements GlueAdapterContext {

    Context glueContext;

    public DefaultGlueAdapterContext(VBindingContext bindingContext, Context glueContext) {
        super(bindingContext);
        this.glueContext = glueContext;
    }

    public Context getGlueContext() {
        return glueContext;
    }

    public void setGlueContext(Context glueContext) {
        this.glueContext = glueContext;
    }
}
