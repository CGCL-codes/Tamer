package net.sf.buildbox.releasator.legacy;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.io.File;
import net.sf.buildbox.util.CommandLineExec;

class ScmSvnData extends ScmData {

    private static final Pattern SVN_SOURCEFORGE = Pattern.compile("^https?://(.*)\\.svn\\.sourceforge\\.net/svnroot/([^/]+)(/.*)$");

    private static final Pattern SVN_GOOGLECODE = Pattern.compile("^https?://(.*)\\.googlecode\\.com/svn(/.*)$");

    private static final Pattern SVN_MANY = Pattern.compile("^[\\w-]+:.*\\.([\\w-]+)\\.[\\w-]+/svn/([\\w-]+)(/.*)$");

    private String repoRoot;

    private String vcsId;

    ScmSvnData(String scm) {
        super(scm);
    }

    /**
     * fills vcsId and repoRoot
     */
    public void analyzeValidUrl() {
        if (repoRoot == null) {
            final String svnUrl = scm.substring(SCM_SVN_PREFIX.length());
            try {
                repoRoot = SCM_SVN_PREFIX + svnInfoGet(svnUrl, "Repository Root");
            } catch (IOException e) {
                throw new IllegalArgumentException(svnUrl);
            } catch (InterruptedException e) {
                Thread.interrupted();
                throw new RuntimeException(e);
            }
        }
        if (vcsId == null) {
            final String svnUrl = scm.substring(SCM_SVN_PREFIX.length());
            if (svnUrl.startsWith("file://")) {
                vcsId = "local.svn";
                return;
            }
            final Matcher sfMatcher = SVN_SOURCEFORGE.matcher(svnUrl);
            if (sfMatcher.matches()) {
                vcsId = "sf." + sfMatcher.group(1);
                return;
            }
            final Matcher gcMatcher = SVN_GOOGLECODE.matcher(svnUrl);
            if (gcMatcher.matches()) {
                vcsId = "googlecode." + sfMatcher.group(1);
                return;
            }
            final Matcher manyMatcher = SVN_MANY.matcher(svnUrl);
            if (manyMatcher.matches()) {
                vcsId = manyMatcher.group(1) + "." + manyMatcher.group(2);
                return;
            }
            throw new IllegalArgumentException("Unknown SVN repository: " + svnUrl);
        }
    }

    public ScmData getTagScm(String scmTag) throws IOException, InterruptedException {
        int n = scm.indexOf("/trunk/");
        if (n < 0) {
            n = scm.indexOf("/branches/");
        }
        if (n < 0) {
            n = scm.indexOf("/tags/");
        }
        if (n < 0) {
            throw new UnsupportedOperationException("releasing from path in a non-standard directory layout is not implemented yet");
        }
        final String scmBaseUrl = scm.substring(0, n);
        final ScmSvnData tag = (ScmSvnData) ScmData.getInstance(scmBaseUrl + "/tags/" + scmTag);
        analyzeValidUrl();
        tag.repoRoot = repoRoot;
        tag.vcsId = vcsId;
        return tag;
    }

    private String getRepoUrl() {
        analyzeValidUrl();
        return repoRoot;
    }

    public String getVcsPath() {
        return scm.substring(getRepoUrl().length());
    }

    public String getVcsId() {
        analyzeValidUrl();
        return vcsId;
    }

    private static String svnInfoGet(String svnUrl, String info) throws IOException, InterruptedException {
        final String prefix = info + ": ";
        final List<String> result = new ArrayList<String>(1);
        final CommandLineExec cle = new CommandLineExec("svn", "info", svnUrl) {

            @Override
            protected void logEnv(Map<String, String> stringStringMap) {
            }

            @Override
            protected void stdout(String s) {
                if (s.startsWith(prefix)) {
                    result.add(s.substring(prefix.length()));
                }
            }
        };
        cle.setFailOnError(true);
        cle.call();
        if (result.isEmpty()) {
            throw new IOException("No such svn info found: " + info);
        }
        return result.get(0);
    }

    public void checkout(File dest, File log) throws IOException, InterruptedException {
        final String svnUrl = scm.substring(SCM_SVN_PREFIX.length());
        dest.getParentFile().mkdirs();
        MyUtils.loggedCmd(log, null, "svn", "checkout", "--ignore-externals", svnUrl, dest.getAbsolutePath());
    }
}
