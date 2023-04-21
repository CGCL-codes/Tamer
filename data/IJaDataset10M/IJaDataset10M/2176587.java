package com.softsizo.explorer.svnclient;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import com.softsizo.data.Repository;
import com.softsizo.data.RepositoryFilter;
import com.softsizo.explorer.handlers.SvnAnnotateHandler;
import com.softsizo.explorer.handlers.SvnLogEntryHandler;

public interface SvnClientProvider {

    public void authenticate(String user, String password);

    public Repository getRepository(SVNURL url) throws SVNException;

    public void doLog(SVNURL url, long fromRevision, long toRevision, SvnLogEntryHandler logEntryHandler) throws SVNException;

    public void doAnnotate(SVNURL url, String filename, long revision, SvnAnnotateHandler annotateHandler) throws SVNException;

    public void doDiff(SVNURL url, long revisionA, SVNURL url2, long revisionB, ByteArrayOutputStream baos1) throws SVNException;

    public Collection<String> listFilesFromRevision(SVNURL url, long revision, RepositoryFilter filter) throws SVNException;
}
