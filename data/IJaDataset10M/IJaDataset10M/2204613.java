package ftog.language_elements;

import org.apache.log4j.Logger;

public class Import {

    public String path;

    public String className;

    public boolean used;

    private Logger log = Logger.getLogger(Import.class);

    public Import() {
    }

    public Import(String im) {
        addImport(im);
    }

    public void addImport(String im) {
        int pos = im.lastIndexOf('.');
        className = im.substring(pos + 1, im.length());
        path = im.substring(0, pos);
        log.debug("Classname:" + className);
        log.debug("path:" + path);
    }

    public void checkForUse(String className) {
        if (className.equals(this.className) || className.equals(getFullyQualifiedClassName())) {
            used = true;
            return;
        }
    }

    public String getFullyQualifiedClassName() {
        return new StringBuilder(path).append('.').append(className).toString();
    }

    public int hashCode() {
        return getFullyQualifiedClassName().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Import)) return false;
        return getFullyQualifiedClassName().equals(((Import) o).getFullyQualifiedClassName());
    }
}
