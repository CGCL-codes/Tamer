package net.sf.fileexchange.api.snapshot;

import java.io.File;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class FileLinkSnapshot extends ResourceTreeSnapshot {

    private File target;

    @XmlAttribute(name = "target")
    @XmlJavaTypeAdapter(XmlFileAdapter.class)
    public File getTarget() {
        return target;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FileLinkSnapshot other = (FileLinkSnapshot) obj;
        if (target == null) {
            if (other.target != null) return false;
        } else if (!target.equals(other.target)) return false;
        return true;
    }
}
