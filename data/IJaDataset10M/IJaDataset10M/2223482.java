package org.lindenb.wikipedia.api;

public class Template extends Entry {

    public Template(String templateName) {
        super(templateName);
    }

    @Override
    public MWNamespace getNamespace() {
        return MWNamespace.Template;
    }
}
