package edu.udo.cs.ai.nemoz.plugins.restinterface.freemarker;

import edu.udo.cs.ai.nemoz.model.entities.Aspect;
import edu.udo.cs.ai.nemoz.model.entities.Category;
import edu.udo.cs.ai.nemoz.model.entities.Item;
import edu.udo.cs.ai.trude.core.Database;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author akaspari
 *
 */
public class NemozObjectWrapper extends DefaultObjectWrapper {

    @Override
    public TemplateModel wrap(final Object object) throws TemplateModelException {
        if (object instanceof Item) return new ItemTemplateModel((Item) object);
        if (object instanceof Category) return new CategoryTemplateModel((Category) object);
        if (object instanceof Aspect) return new AspectTemplateModel((Aspect) object);
        if (object instanceof Database) return new DatabaseTemplateModel((Database) object);
        return super.wrap(object);
    }
}
