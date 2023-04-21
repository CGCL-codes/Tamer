package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

public class FilterGenerator extends AbstractGenerator {

    private static final String TEMPLATE = "filter.ftl";

    public void generate(Environment env, VariablesBuilder pb) {
        Template template = env.getTemplate(TEMPLATE);
        env.addJavaSource((String) pb.getParameters().get("pattern_package"), (String) pb.getParameters().get("filter_name"), template, pb.getParameters());
    }

    public boolean existsArtifact(Environment env, VariablesBuilder pb) {
        return false;
    }

    public FilterGenerator() {
        name = "modelPattern";
    }

    public String getDisplayName() {
        return "ModelPattern Class";
    }

    public String getDescription() {
        return "Produces a ModelPattern implementation for the model class.";
    }
}
