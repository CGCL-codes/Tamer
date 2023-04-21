package net.sourceforge.greenvine.generator.impl.java.dao.springjpa;

import net.sourceforge.greenvine.generator.Generator;
import net.sourceforge.greenvine.generator.helper.java.JavaHelper;
import net.sourceforge.greenvine.generator.helper.model.ModelHelper;
import net.sourceforge.greenvine.generator.task.TemplateTaskQueue;
import net.sourceforge.greenvine.generator.template.Template;
import net.sourceforge.greenvine.generator.template.TemplateContext;
import net.sourceforge.greenvine.model.Model;

public class SpringJpaDaoContextGenerator implements Generator {

    private final String appContextFileName = "application-context-jpa-dao.xml";

    private String daoSuffix = "Dao";

    public void generate(Model model, Template template, TemplateTaskQueue queue) throws Exception {
        JavaHelper javaHelper = new JavaHelper();
        ModelHelper modelHelper = new ModelHelper(model);
        String daoPackageName = model.getNamespace() + "." + model.getData().getNamespace() + ".daos";
        TemplateContext context = new TemplateContext();
        context.put("daoPackageName", daoPackageName);
        context.put("daoSuffix", daoSuffix);
        context.put("model", model);
        context.put("modelHelper", modelHelper);
        context.put("javaHelper", javaHelper);
        queue.enqueue(template, context, null, appContextFileName);
    }

    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }
}
