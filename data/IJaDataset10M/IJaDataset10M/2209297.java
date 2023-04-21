package com.ouroboroswiki.core.content.svnkit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;
import com.ouroboroswiki.core.AbstractContent;
import com.ouroboroswiki.core.Content;
import com.ouroboroswiki.core.ContentException;

public class SVNKitContent extends AbstractContent {

    private SVNRepository repository;

    private long revision;

    private String path;

    public SVNKitContent(SVNRepository repository, long revision, String path, String name, String repoName) {
        this.repository = repository;
        this.revision = revision;
        this.setUniqueName(name);
        this.setRepositoryName(repoName);
        this.path = path;
    }

    @Override
    public Map<String, Content> getChildContent() {
        return null;
    }

    @Override
    public void write(OutputStream outs) throws IOException, ContentException {
        try {
            repository.getFile(this.path, this.revision, null, outs);
        } catch (SVNException ex) {
            throw new ContentException("unable to read " + this.getUniqueName() + " from " + this.getRepositoryName(), ex);
        }
    }
}
