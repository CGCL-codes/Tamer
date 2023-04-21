package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

/**
 * This class represents the prefix name.
 *
 * @author Jiri Schejbal
 */
public class SchemaPrefix {

    private boolean isDefault;

    private String prefixName;

    public SchemaPrefix() {
        prefixName = null;
        isDefault = true;
    }

    public SchemaPrefix(String prefixName) {
        this.prefixName = prefixName;
        isDefault = false;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public String getPrefixName() {
        return prefixName;
    }
}
