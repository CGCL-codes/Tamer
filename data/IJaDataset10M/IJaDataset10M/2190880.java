package net.jtools.ant.directorystructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

public class DirectoryStructure extends DataType {

    public static final String VAR_PREFIX = "*[";

    public static final String VAR_SUFFIX = "]";

    public static class Alias {

        private String alias;

        public void addText(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }
    }

    public static interface Item {

        void execute(Properties dest, String root);
    }

    public static interface DirectoryItem extends Item {

        Collection getChilds();
    }

    public static class File implements Item {

        private String name;

        private String var;

        private List<String> alias = new ArrayList<String>();

        protected String getName(boolean required) {
            if (name != null) return name;
            if (var != null) return VAR_PREFIX + var + VAR_SUFFIX;
            if (required) throw new NullPointerException("either name or var have to be specified.");
            return null;
        }

        public void setAlias(String name) {
            for (StringTokenizer st = new StringTokenizer(name, ";,"); st.hasMoreTokens(); ) alias.add(st.nextToken().trim());
        }

        public void addConfiguredAlias(Alias alias) {
            this.alias.add(alias.getAlias());
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setVar(String name) {
            this.var = name;
        }

        public void execute(Properties dest, String root) {
            for (Iterator i = this.alias.iterator(); i.hasNext(); ) {
                String alias = (String) i.next();
                dest.setProperty(alias, (root == null ? "" : (root + "/")) + getName(true));
            }
        }
    }

    public static class Directory extends File implements DirectoryItem {

        private List<Item> childs = new ArrayList<Item>();

        public Collection<Item> getChilds() {
            return childs;
        }

        public void addChilds(Collection<Item> childs) {
            this.childs.addAll(childs);
        }

        public void addConfiguredDir(Directory item) {
            childs.add(item);
        }

        public void addConfiguredFile(File item) {
            childs.add(item);
        }

        public void execute(Properties dest, String root) {
            super.execute(dest, root);
            String newRoot = (root == null) ? getName(false) : (root + "/" + getName(true));
            for (Item child : childs) child.execute(dest, newRoot);
        }
    }

    private final Directory root = new Directory();

    private Properties alias = null;

    public DirectoryStructure() {
    }

    public void addConfiguredDir(Directory item) {
        alias = null;
        root.addConfiguredDir(item);
    }

    public void addConfiguredFile(File item) {
        alias = null;
        root.addConfiguredFile(item);
    }

    public void addConfiguredFileSystem(DirectoryStructure other) {
        alias = null;
        this.root.addChilds(other.root.getChilds());
    }

    public void addConfiguredRefId(Reference ref) {
        addConfiguredFileSystem((DirectoryStructure) ref.getReferencedObject());
    }

    public String getDirectory(String alias) {
        if (this.alias == null) {
            this.alias = new Properties();
            this.root.execute(this.alias, null);
        }
        return this.alias.getProperty(alias);
    }
}
