package com.sourceforge.jartorrent.ivy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.resolve.ResolveData;
import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.core.search.ModuleEntry;
import org.apache.ivy.core.search.OrganisationEntry;
import org.apache.ivy.core.search.RevisionEntry;
import org.apache.ivy.plugins.repository.Repository;
import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.plugins.resolver.CacheResolver;
import org.apache.ivy.plugins.resolver.RepositoryResolver;
import org.apache.ivy.plugins.resolver.util.ResolvedResource;
import org.apache.ivy.util.Message;
import com.sourceforge.jartorrent.Downloader;

public class CopyOfTorrentResolver extends RepositoryResolver {

    private CacheResolver delegate;

    private String root;

    private String torrentPattern;

    private final TorrentRepository repository;

    public CopyOfTorrentResolver() {
        super();
        System.out.println("--------- HELLO JARTORRENT FOR IVY ! ------------- ");
        repository = new TorrentRepository();
        repository.setName("torrentRepository");
        setRepository(repository);
        setName("torrent");
    }

    private void initDelegate() {
        delegate = new CacheResolver();
        delegate.setRepositoryCacheManager(getRepositoryCacheManager());
        delegate.setSettings(getSettings());
        delegate.setName("cache");
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        if (root == null) {
            throw new NullPointerException("root must not be null");
        }
        if (!root.endsWith("/")) {
            this.root = root + "/";
        } else {
            this.root = root;
        }
    }

    public String getTorrentPattern() {
        return torrentPattern;
    }

    /**
	 * torrentPattern can be for example: [organisation]-[module]-[revision].torrent
	 * 
	 */
    public void setTorrentPattern(String torrentPattern) {
        this.torrentPattern = torrentPattern;
    }

    public String getTypeName() {
        return "torrent";
    }

    public OrganisationEntry[] listOrganisations() {
        System.out.println(" ------------ listOrganisations ------------");
        return super.listOrganisations();
    }

    public ModuleEntry[] listModules(OrganisationEntry org) {
        System.out.println(" ------------ listModules ------------");
        return super.listModules(org);
    }

    public RevisionEntry[] listRevisions(ModuleEntry mod) {
        System.out.println(" ------------ listRevisions ------------");
        return super.listRevisions(mod);
    }

    protected ResolvedResource[] listResources(Repository repository, ModuleRevisionId mrid, String pattern, Artifact artifact) {
        System.out.println(" ------------ listResources ------------");
        return super.listResources(repository, mrid, pattern, artifact);
    }

    public ResolvedModuleRevision getDependency(DependencyDescriptor dde, ResolveData data) throws ParseException {
        System.out.println(" ------------ getDependency ------------");
        final ResolvedModuleRevision revision = super.getDependency(dde, data);
        final boolean isCached = (revision == null) ? false : true;
        if (!isCached) {
            final ModuleRevisionId mrid = dde.getDependencyRevisionId();
            Message.info("\t" + getName() + ": now trying torrent download for " + mrid);
            final String metadataLocation = IvyPatternHelper.substitute(root + torrentPattern, mrid);
            final Resource metadata;
            try {
                metadata = repository.getResource(metadataLocation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (metadata.exists()) {
                Message.verbose("\tinitializing torrent download with: " + metadata);
                final Downloader downloader = new Downloader();
                final String filename = IvyPatternHelper.substitute(torrentPattern, mrid);
                downloader.setFilename(filename);
                downloader.setDestination(getIvyCacheDir());
                final File downloadDirectory = downloader.download(metadataLocation);
                System.out.println("downloadDirectory:" + downloadDirectory.getAbsolutePath());
                initDelegate();
                final ResolvedModuleRevision recentlyDownloadeRevision = super.getDependency(dde, data);
                return recentlyDownloadeRevision;
            } else {
                Message.verbose("\tunable to find .torrent file: " + metadataLocation);
            }
        }
        return revision;
    }

    /**
     * ivy.home + /cache
     */
    private String getIvyCacheDir() {
        final String ivyCacheDir = getSettings().getVariable("ivy.cache.dir");
        System.out.println("ivy.cache.dir: " + ivyCacheDir);
        return ivyCacheDir;
    }
}
