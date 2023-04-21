package Entities;

import GUI.Browser.VersionBrowser.VersionPanel;
import XML.XMLBuilder;
import java.util.Vector;

/**
 * Contains a set of different versions of a quality requirement.
 * @author pontuslp
 */
public class QualityHistory implements XMLRepresentation {

    Vector<Quality> versions;

    public QualityHistory() {
        versions = new Vector<Quality>();
    }

    public QualityHistory(Vector<Quality> versions) {
        this.versions = versions;
    }

    /**
	 * Initializes the objects as empty and adds the single Quality given
	 * as parameter.
	 */
    public QualityHistory(Quality quality) {
        this.versions = new Vector<Quality>();
        versions.add(quality);
    }

    public void addVersion(Quality q) {
        versions.add(q);
    }

    public String getId() {
        if (versions.size() > 0) {
            return versions.lastElement().getId();
        }
        throw new UnsupportedOperationException("Attempted to get id of the " + "quality, but there are no versions");
    }

    public String getDefinition() {
        if (versions.size() > 0) {
            return versions.lastElement().getDefinition();
        }
        throw new UnsupportedOperationException("Attempted to get the " + "definition of the quality, but there are no versions");
    }

    public Quality getCurrent() {
        return versions.lastElement();
    }

    public Quality getVersion(int version) {
        return versions.elementAt(version);
    }

    public int getVersionCount() {
        return versions.size();
    }

    public Vector<VersionPanel> getVersionPanels() {
        Vector<VersionPanel> versionPanels = new Vector<VersionPanel>();
        for (int i = 0; i < versions.size(); i++) {
            versionPanels.add(new VersionPanel(versions.elementAt(i), i + 1));
        }
        return versionPanels;
    }

    @Override
    public QualityHistory clone() throws CloneNotSupportedException {
        QualityHistory clone = new QualityHistory();
        for (Quality version : versions) {
            clone.addVersion(version.clone());
        }
        return clone;
    }

    public boolean equals(QualityHistory qh) {
        boolean equals = versions.size() == qh.versions.size();
        if (!equals) return false;
        for (int i = 0; i < versions.size(); i++) {
            equals = equals && versions.elementAt(i).equals(qh.versions.elementAt(i));
        }
        return equals;
    }

    @Override
    public XMLBuilder toXML(XMLBuilder xb) {
        xb.append("<QualityHistory>");
        xb.append("<sequence id=\"qualityVersions\" >");
        for (Quality q : versions) {
            q.toXML(xb);
        }
        xb.append("</sequence >");
        xb.append("</QualityHistory>");
        return xb;
    }
}
