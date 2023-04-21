package toxtree.plugins.kroes.categories;

import toxTree.tree.DefaultCategory;

public class RequireCompoundSpecificToxicityData extends DefaultCategory {

    public RequireCompoundSpecificToxicityData() {
        this("Risk assessment requires compound-specific toxicity data", 3);
    }

    public RequireCompoundSpecificToxicityData(String name, int id) {
        super(name, id);
    }

    private static final long serialVersionUID = 0xf8e2d8d0ba9a765fL;
}
