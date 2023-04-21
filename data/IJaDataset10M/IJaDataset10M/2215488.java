package com.dyuproject.protostuff.codegen;

import java.io.Writer;
import java.util.ArrayList;
import org.apache.velocity.VelocityContext;
import com.dyuproject.protostuff.model.Model;

/**
 * Generates gwt overlays.
 * 
 * @author David Yu
 * @created Oct 17, 2009
 */
public class GwtJsonGenerator extends VelocityCodeGenerator {

    public static final String ID = "gwt_json";

    public GwtJsonGenerator() {
        this(ID);
    }

    public GwtJsonGenerator(String id) {
        super(id);
    }

    @Override
    protected void generateFrom(Module module, ArrayList<Model<?>> models) throws Exception {
        for (Model<?> model : models) {
            String outputClassname = model.getModelMeta().getMessageClass().getSimpleName();
            VelocityContext context = newVelocityContext();
            context.put("module", module);
            context.put("model", model);
            Writer writer = newWriter(module, outputClassname + ".java");
            try {
                ENGINE.mergeTemplate(getTemplateResource(), module.getEncoding(), context, writer);
            } finally {
                writer.close();
            }
        }
    }
}
