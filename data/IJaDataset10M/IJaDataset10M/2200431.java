package net.community.chest.svnkit;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import net.community.chest.CoVariantReturn;
import net.community.chest.svnkit.core.wc.SVNInfoCollection;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNPropertyHandler;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

public class SVNURLLocation extends SVNLocation {

    private final SVNURL _url;

    @Override
    public final SVNURL getURL() {
        return _url;
    }

    @Override
    @CoVariantReturn
    public final SVNURL getLocationObject() {
        return getURL();
    }

    @Override
    public final File getFile() {
        return null;
    }

    public SVNURLLocation(SVNURL u) throws IllegalStateException {
        super(SVNLocationType.URL);
        if ((_url = u) == null) throw new IllegalStateException("No URL instance provided");
    }

    @Override
    @CoVariantReturn
    public SVNURLLocation appendSubPath(String subPath) throws SVNException, IllegalStateException {
        if ((subPath == null) || (subPath.length() <= 0)) throw new IllegalStateException("No sub path value provided");
        final SVNURL baseURL = getURL();
        final String fullURL = baseURL.toString() + "/" + subPath;
        return new SVNURLLocation(SVNURL.parseURIEncoded(fullURL));
    }

    @Override
    public String getName() {
        final SVNURL url = getURL();
        final String path = (url == null) ? null : url.getPath();
        final int pLen = (path == null) ? 0 : path.length(), lPos = (pLen <= 3) ? (-1) : path.lastIndexOf('/');
        if ((lPos <= 0) || (lPos >= (pLen - 1))) return null;
        return path.substring(lPos + 1);
    }

    @Override
    public SVNInfo getInfo(final SVNWCClient wcc) throws SVNException, IOException {
        if (wcc == null) throw new EOFException("No WC client instance provided");
        return wcc.doInfo(getURL(), SVNRevision.UNDEFINED, SVNRevision.HEAD);
    }

    @Override
    @CoVariantReturn
    public List<SVNURLLocation> listFiles(final SVNWCClient wcc, final SVNLocationFilter filter) throws SVNException, IOException {
        if (wcc == null) throw new EOFException("No WC client instance provided");
        final SVNURL url = getURL();
        final SVNInfoCollection children = new SVNInfoCollection();
        wcc.doInfo(url, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.IMMEDIATES, children);
        final int numChildren = children.size();
        if (numChildren <= 0) return null;
        final List<SVNURLLocation> locSet = new ArrayList<SVNURLLocation>(Math.max(5, numChildren));
        final String myName = getName();
        boolean skippedMyName = false;
        for (final SVNInfo i : children) {
            final String subPath = i.getPath();
            if ((!skippedMyName) && myName.equals(subPath)) {
                skippedMyName = true;
                continue;
            }
            final SVNURLLocation loc = appendSubPath(subPath);
            if ((filter != null) && (!filter.accept(loc))) continue;
            if (!locSet.add(loc)) continue;
        }
        return locSet;
    }

    @Override
    public void copyTo(final SVNWCClient wcc, final OutputStream out) throws SVNException, IOException {
        if (wcc == null) throw new EOFException("No WC client instance provided");
        if (out == null) throw new EOFException("No output stream instance provided");
        wcc.doGetFileContents(getURL(), SVNRevision.UNDEFINED, SVNRevision.HEAD, true, out);
    }

    @Override
    public OutputStream createOutputStream(final SVNWCClient wcc) throws SVNException, IOException {
        if (wcc == null) throw new EOFException("No WC client instance provided");
        throw new StreamCorruptedException("createOutputStream(" + getLocationType() + ") N/A");
    }

    @Override
    public <H extends ISVNPropertyHandler> H getProperties(SVNWCClient wcc, H handler) throws SVNException, IOException {
        if (wcc == null) throw new EOFException("No WC client instance provided");
        wcc.doGetProperty(getURL(), null, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.EMPTY, handler);
        return handler;
    }

    @Override
    public void mergeTo(SVNDiffClient dfc, SVNLocation target) throws SVNException, IOException {
        if (dfc == null) throw new EOFException("No DIFF client provided");
        if (target == null) throw new StreamCorruptedException("No merge target provided");
        final SVNURL srcURL = getURL(), tgtURL = target.getURL();
        if (tgtURL == null) {
            final File tgtFile = target.getFile();
            if (tgtFile == null) throw new StreamCorruptedException("No merge target file available");
            dfc.doMerge(srcURL, SVNRevision.BASE, srcURL, SVNRevision.HEAD, tgtFile, SVNDepth.EMPTY, false, false, false, false);
        } else throw new IOException("Merge to URL target N/A");
    }
}
