package net.sf.buildbox.impl;

import net.sf.buildbox.api.VcsWebUi;

public class GoogleCodeSvnWebUI implements VcsWebUi {

    private final String projectUrl;

    private final String svnRepositoryName;

    public GoogleCodeSvnWebUI(String projectUrl) {
        this.projectUrl = projectUrl;
        final int n = projectUrl.lastIndexOf('/');
        svnRepositoryName = projectUrl.substring(n + 1);
    }

    public String revisionPage(String rev) {
        final StringBuilder sb = new StringBuilder(projectUrl);
        sb.append("/source/detail?r=");
        sb.append(rev);
        return sb.toString();
    }

    public String filePage(String path, String rev) {
        final StringBuilder sb = new StringBuilder(projectUrl);
        sb.append("/source/browse");
        sb.append(path);
        sb.append("?spec=svn");
        sb.append(rev);
        sb.append("&r=");
        sb.append(rev);
        return sb.toString();
    }

    public String downloadPage(String path, String rev) {
        final StringBuilder sb = new StringBuilder("http://");
        sb.append(svnRepositoryName);
        sb.append(".googlecode.com/svn-history/r");
        sb.append(rev);
        sb.append("/");
        sb.append(path);
        return sb.toString();
    }

    public String diffPage(String path, String previousRev, String thisRev) {
        final StringBuilder sb = new StringBuilder(projectUrl);
        sb.append("/source/diff?spec=svn");
        sb.append(thisRev);
        if (previousRev != null) {
            sb.append("&old=");
            sb.append(previousRev);
        }
        sb.append("&r=");
        sb.append(thisRev);
        sb.append("&path=");
        sb.append(path);
        return sb.toString();
    }
}
