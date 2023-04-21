package net.sf.buildbox.devmodel.ui.client.model;

import java.util.*;
import java.io.Serializable;

/**
 * <p>VCS Domain usually represents whole infrastructure of a company, or its division.</p>
 * <p>This domain has a lot of things in common, like multiple vcs repositories, other infrastructure, working policies etc.</p>
 * <p>Those technically important for integration can be expressed through the various specialized lists, or with generic {@link #properties}</p>
 */
public final class VcsDomain implements Serializable {

    static final long serialVersionUID = 20090905;

    public static final String SUBVERSION = "SVN";

    public static final String CVS = "CVS";

    public static final String BAZAAR = "BZR";

    public static final String GIT = "GIT";

    /**
     * Domain identification; must consist of alphanum chars only.
     */
    public final String domainId;

    /**
     * <p>Is the repository list complete ?</p>
     * <p>Value of "true" is useful to describe public vcs providers where we do not (and do not want) to list all repositories available.</p>
     * <p>Value of "false" should be used to describe local infrastructure, where we know all repositories and there is quite a small number of them.</p>
     */
    public final boolean isOpen;

    public final String vcsType;

    private final List<String> connectionsInternal = new ArrayList<String>();

    /**
     * <p>Connections - list of all connection strings usable to login and checkout from contained repositories - no commits and changes are allowed with these.</p>
     */
    public final transient List<String> connections = Collections.unmodifiableList(connectionsInternal);

    private final List<String> develConnectionsInternal = new ArrayList<String>();

    /**
     * <p>Devel connections - list of all connection strings usable to login, checkout and commit to repositories.</p>
     */
    public final transient List<String> develConnections = Collections.unmodifiableList(develConnectionsInternal);

    private final List<WebUiSpec> webInterfacesInternal = new ArrayList<WebUiSpec>();

    /**
     * <p>Web interfaces - list of pointers to software rendering the repository history and contents.</p>
     */
    public final transient List<WebUiSpec> webInterfaces = Collections.unmodifiableList(webInterfacesInternal);

    /**
     * <p>List of well-known repositories in the domain.</p>
     */
    private final List<VcsRepo> repositoriesInternal = new ArrayList<VcsRepo>();

    public final transient List<VcsRepo> repositories = Collections.unmodifiableList(repositoriesInternal);

    /**
     * <p>Domain properties</p>
     * <p>Contains all important information about the infrastructure in this domain, in order to make integration with other systems possible.</p>
     */
    private final Map<String, String> propertiesInternal = new HashMap<String, String>();

    public final transient Map<String, String> properties = Collections.unmodifiableMap(propertiesInternal);

    public VcsDomain(String domainId, boolean open) {
        this.domainId = domainId;
        this.isOpen = open;
        this.vcsType = SUBVERSION;
    }

    public void setupKnownRepository(VcsRepo repo) {
        repositoriesInternal.add(repo);
    }

    public void addConnection(String connectionString) {
        connectionsInternal.add(connectionString);
    }

    public void addDevelConnection(String connectionString) {
        develConnectionsInternal.add(connectionString);
    }

    public void addWebInterface(WebUiSpec webUiSpec) {
        webInterfacesInternal.add(webUiSpec);
    }

    public void setProperty(String name, String value) {
        if (value == null) {
            propertiesInternal.remove(name);
        } else {
            propertiesInternal.put(name, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VcsDomain vcsDomain = (VcsDomain) o;
        return domainId.equals(vcsDomain.domainId);
    }

    @Override
    public int hashCode() {
        return domainId.hashCode();
    }

    @Override
    public String toString() {
        return "VcsDomain{" + "domainId='" + domainId + '\'' + '}';
    }
}
