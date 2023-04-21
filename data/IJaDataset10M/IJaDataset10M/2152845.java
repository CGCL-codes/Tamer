package net.sf.magicmap.client.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import net.sf.magicmap.artifact.ArtifactComparator;
import net.sf.magicmap.artifact.IArtifact;
import net.sf.magicmap.artifact.IVendor;
import net.sf.magicmap.artifact.Version;
import net.sf.magicmap.client.plugin.util.OperatingSystemInfo;

/**
 * Beschreibt ein Plugin. Eine immutable Klasse.
 * @author jan_fride
 * @version 1.0
 * @since heute
 */
public class SimplePluginDescriptor implements IPluginDescriptor {

    private final IVendor vendor;

    private final String groupId;

    private final String artifactId;

    private final String description;

    private final String name;

    private final Version version;

    private static final ArtifactComparator cmp = new ArtifactComparator();

    private final Collection<String[]> dependencies = new LinkedList<String[]>();

    private final Collection<OperatingSystemInfo> supportedOperatingSystems = new ArrayList<OperatingSystemInfo>();

    /**
	 * Konstruktor.
	 *
	 * @param vendor der Hersteller/Author des Plugins
	 * @param groupId die Id der Gruppe des Plugins
	 * @param artifactId die ID des Plugins in seiner Gruppe.
	 * @param description eine Beschreibung des Plugins
	 * @param name der Name des Plugins
	 * @param version die Version des Plugins.
	 * @param dependencies eine Liste der Abh�ngikeiten
	 * @param os a regular expression for the supported operating systems.
	 */
    public SimplePluginDescriptor(IVendor vendor, String groupId, String artifactId, String description, String name, String version, Collection<String[]> dependencies, OperatingSystemInfo... os) {
        this.vendor = vendor;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.description = description;
        this.name = name;
        this.version = new Version(version);
        this.dependencies.addAll(dependencies);
        Collections.addAll(this.supportedOperatingSystems, os);
    }

    public SimplePluginDescriptor(String vendorName, String vendorEmail, String vendorHomepage, String groupId, String artifactId, String description, String name, String version, Collection<String[]> dependencies, OperatingSystemInfo... os) {
        this(new SimpleVendor(vendorName, vendorEmail, vendorHomepage), groupId, artifactId, description, name, version, dependencies, os);
    }

    /**
	 * Der Hersteller des Plugins
	 *
	 * @return die Beschreibung des Herstellers.
	 */
    public IVendor getVendor() {
        return this.vendor;
    }

    /**
	 * Eine f�r Menschen verst&auml;ndliche Beschreibung des Plugins
	 * (also auch f�r nicht Informatiker).
	 *
	 * @return die Beschreibung des Plugins.
	 */
    public String getDescription() {
        return this.description;
    }

    /**
	 * der Name des Plugins
	 * @return
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Die Version des Plugins
	 * @return
	 */
    public Version getVersion() {
        return this.version;
    }

    /**
	 *
	 * @return die Liste der Abh�ngigkeiten.
	 */
    public Collection<String[]> getDependencies() {
        return this.dependencies;
    }

    /**
	 * @see net.sf.magicmap.artifact.IUniqueIdentity
	 * @return die ID des Artifacts (Plugins).
	 */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
	 * @see net.sf.magicmap.artifact.IUniqueIdentity
	 * @return die ID der Gruppe (Plugins).
	 */
    public String getGroupId() {
        return this.groupId;
    }

    /**
	 *
	 * @param artifact another unique id.
	 * @return 0 if artifact and groupdid are equal. -1 or 1 else.
	 */
    public int compareTo(IArtifact artifact) {
        return SimplePluginDescriptor.cmp.compare(this, artifact);
    }

    /**
	 *
	 * @return true if the plugin runs on the current system.
	 */
    public boolean isOsSupported() {
        for (OperatingSystemInfo info : supportedOperatingSystems) {
            if (info.isCurrentOsSupported()) return true;
        }
        return false;
    }

    /**
	 * The same hash as any artifact.
	 * @return
	 */
    @Override
    public int hashCode() {
        int hash = 31;
        int hashFactor = 17;
        hash += getArtifactId() == null ? 0 : getArtifactId().hashCode() * 17;
        hash += getGroupId() == null ? 0 : getGroupId().hashCode() * 17;
        hash += getVersion() == null ? 0 : getVersion().hashCode() * 17;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IArtifact) {
            IArtifact iArtifact = (IArtifact) obj;
            return getArtifactId().equals(iArtifact.getArtifactId()) && getGroupId().equals(iArtifact.getGroupId()) && getVersion().equals(iArtifact.getVersion());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "SimplePluginDescriptor{" + "vendor=" + vendor + ", groupId='" + groupId + '\'' + ", artifactId='" + artifactId + '\'' + ", description='" + description + '\'' + ", name='" + name + '\'' + ", version='" + version.toString() + '\'' + ", dependencies=" + (dependencies == null ? null : Arrays.asList(dependencies)) + ", supportedOperatingSystems=" + supportedOperatingSystems + '}';
    }
}
