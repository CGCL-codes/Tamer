package org.metadsl.resolvers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RepositoryManager {

    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public RepositoryInfo getLocalRepository(final String localRepo) {
        try {
            if (debug) System.out.println(String.format("localRepo=%s", localRepo));
            if (localRepo != null && localRepo.trim().length() > 0) {
                final String value = new File(localRepo.trim()).toURI().toURL().toString();
                return new RepositoryInfoImpl("local", value);
            } else {
                for (final RepositoryInfo repo : probableLocalRepositories) {
                    if (new File(repo.getURL()).isDirectory()) {
                        final String name = repo.getName();
                        final String value = new File(repo.getURL().trim()).toURI().toURL().toString();
                        if (debug) System.out.println(String.format("%s=%s", name, value));
                        return new RepositoryInfoImpl(name, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Could not find a valid local repository");
    }

    public RepositoryInfo[] getRemoteRepositories(List<String> remoteRepoList) {
        try {
            if (remoteRepoList != null) {
                final List<RepositoryInfo> result = new ArrayList<RepositoryInfo>();
                if (debug) System.out.println(String.format("remoteRepoList=%s", remoteRepoList.toString()));
                for (int i = 0; i < remoteRepoList.size(); i++) {
                    String remoteRepo = remoteRepoList.get(i);
                    final String name = "remote".concat(String.valueOf(i));
                    final String value = remoteRepo.trim();
                    if (debug) System.out.println(String.format("%s=%s", name, value));
                    result.add(new RepositoryInfoImpl(name, value));
                }
                return result.toArray(new RepositoryInfo[result.size()]);
            } else {
                return famousPublicRepositories;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface RepositoryInfo {

        String getName();

        String getURL();
    }

    private class RepositoryInfoImpl implements RepositoryInfo {

        final String name;

        final String url;

        public RepositoryInfoImpl(final String name, final String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getURL() {
            return url;
        }
    }

    /**
     * List of "probable local M2 repositories"
     * <p>
     * Every entry contains a name and a URL, separated by a single space
     * <p>
     * Note: Maven supports only one local Maven2 repository.
     * So, in order to support lookups on multiple local repositories, some logic needs
     * to be added by application code.
     */
    private final RepositoryInfo[] probableLocalRepositories = new RepositoryInfo[] { new RepositoryInfoImpl("home", System.getProperty("user.home").concat("/.m2/repository")) };

    /**
     * List of "famous public Maven2 repositories"
     * <p>
     * Every entry contains a name and a URL, separated by a single space
     *
     * @see: http://www.mvnbrowser.com/repositories.html
     */
    private final RepositoryInfo[] famousPublicRepositories = new RepositoryInfo[] { new RepositoryInfoImpl("central", "http://repo1.maven.org/maven2"), new RepositoryInfoImpl("apache", "http://ftp.cica.es/mirrors/maven2"), new RepositoryInfoImpl("appfuse", "http://static.appfuse.org/repository"), new RepositoryInfoImpl("atlassian", "http://maven.atlassian.com/public"), new RepositoryInfoImpl("atlasian-contrib", "http://maven.atlassian.com/contrib"), new RepositoryInfoImpl("codehaus", "http://repository.codehaus.org"), new RepositoryInfoImpl("dojo", "http://download.dojotoolkit.org//maven2"), new RepositoryInfoImpl("dynamicjava", "http://maven.dynamicjava.org"), new RepositoryInfoImpl("ejb3unit", "http://ejb3unit.sourceforge.net/maven2"), new RepositoryInfoImpl("ebxml", "http://ebxmlrr.sourceforge.net/maven2/repository"), new RepositoryInfoImpl("freehep", "http://java.freehep.org/maven2"), new RepositoryInfoImpl("fuse", "http://repo.fusesource.com/maven2"), new RepositoryInfoImpl("geotools", "http://maven.geotools.fr/repository"), new RepositoryInfoImpl("glassfish", "http://download.java.net/maven/glassfish"), new RepositoryInfoImpl("google-maven", "http://google-maven-repository.googlecode.com/svn/repository"), new RepositoryInfoImpl("guiceyfruit", "http://guiceyfruit.googlecode.com/svn/repo/releases"), new RepositoryInfoImpl("hivedb", "http://www.hivedb.org/maven"), new RepositoryInfoImpl("ja-sig", "http://developer.ja-sig.org/maven2"), new RepositoryInfoImpl("java.net", "http://download.java.net/maven/2"), new RepositoryInfoImpl("jboss", "http://repository.jboss.org/maven2"), new RepositoryInfoImpl("objectweb", "http://maven.objectweb.org/maven2"), new RepositoryInfoImpl("restlet", "http://maven.restlet.org"), new RepositoryInfoImpl("scala-tools", "http://scala-tools.org//repo-releases"), new RepositoryInfoImpl("seasar", "http://maven.seasar.org/maven2"), new RepositoryInfoImpl("slick2d", "http://slick.cokeandcode.com/mavenrepo"), new RepositoryInfoImpl("sweetdev-ria", "http://sweetdev-ria.ideotechnologies.com/maven2/repository"), new RepositoryInfoImpl("wicket", "http://wicketstuff.org/maven/repository") };
}
