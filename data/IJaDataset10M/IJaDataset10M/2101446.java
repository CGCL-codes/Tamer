package org.xteam.box2text.oaw;

import java.util.ArrayList;
import java.util.List;
import org.xteam.box2text.parser.ast.BoxFile;
import org.xteam.box2text.parser.ast.Rule;

public class BoxResourceImpl implements BoxResource {

    private String fqn;

    private BoxFile file;

    private BoxRule[] rules;

    public BoxResourceImpl(BoxFile file) {
        this.file = file;
    }

    @Override
    public BoxRule[] getRules() {
        if (rules == null) {
            List<BoxRule> r = new ArrayList<BoxRule>();
            for (Rule rule : file.getRules()) {
                r.add(new BoxRule(this, rule));
            }
            rules = r.toArray(new BoxRule[r.size()]);
        }
        return rules;
    }

    @Override
    public String getFullyQualifiedName() {
        return fqn;
    }

    @Override
    public void setFullyQualifiedName(String fqn) {
        this.fqn = fqn;
    }

    @Override
    public String[] getImportedExtensions() {
        return new String[0];
    }

    @Override
    public String[] getImportedNamespaces() {
        if (file.getImportName() != null) return new String[] { file.getImportName().getValue() };
        return null;
    }
}
