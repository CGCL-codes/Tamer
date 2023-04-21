package net.sf.buildbox.releasator.legacy;

import org.junit.Test;
import org.junit.Assert;

public class JReleasatorTest {

    @Test
    public void showCurrentTimeAsUTC() throws Exception {
        System.out.println("JReleasator.formatCurrentTime() = " + MyUtils.formatCurrentTime());
    }

    @Test
    public void mavenVersionTest() throws Exception {
        String v = MyUtils.getMavenVersion();
        System.out.println("v = " + v);
    }

    @Test
    public void getVcsPath() throws Exception {
        final ScmData scm = ScmData.getInstance("scm:svn:http://gappa.devlab.ad/svn/hp-soa/trunk/isobuild/iso-systinet-es");
        Assert.assertEquals("vcsId", "devlab.hp-soa", scm.getVcsId());
        Assert.assertEquals("vcsPath", "/trunk/isobuild/iso-systinet-es", scm.getVcsPath());
        final ScmData tag = scm.getTagScm("MY-TAG");
        Assert.assertEquals("tag", "scm:svn:http://gappa.devlab.ad/svn/hp-soa/tags/MY-TAG", tag.toString());
        Assert.assertEquals("tag.vcsPath", "/tags/MY-TAG", tag.getVcsPath());
    }
}
