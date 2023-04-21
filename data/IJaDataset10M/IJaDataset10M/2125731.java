package net.sf.buildbox.releasator.ng;

import java.io.File;

public class ScmGitAdapterFactory implements ScmAdapterFactory {

    private String tagFormatString = "%2$s-%s";

    public ScmAdapter create(String scmUrl) {
        final File jobDir = new File(ScmUtils.workDir(), ScmUtils.scmUrlAsFileName(scmUrl));
        return new ScmGitAdapter(this, scmUrl, jobDir);
    }

    public String getTagFormatString() {
        return tagFormatString;
    }
}
