package net.sf.buildbox.devmodel.fs.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.xml.transform.stream.StreamResult;
import net.sf.buildbox.devmodel.Commit;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CommitLogTest {

    @Test
    public void overallTest() throws IOException, SAXException {
        final CommitLog commitLog = new CommitLog(new InputSource(getClass().getResourceAsStream("/sample-commit-log.xml")));
        final List<Commit> commits = commitLog.allCommitsByTimestamp();
        for (Commit commit : commits) {
            System.out.println(commit);
        }
        Assert.assertEquals("commits.count", 10, commits.size());
        {
            final Commit firstCommit = commitLog.findByRevision("869");
            Assert.assertNotNull("first:rev", firstCommit);
            Assert.assertEquals("first:author", "pkozelka", firstCommit.getAuthor());
            Assert.assertEquals("first:commitMsg", "no longer needed", firstCommit.getMsg());
            Assert.assertEquals("first:time", "2008-04-10T22:20:13.118", CommitLog.VCS_TIME_FORMAT.get().format(firstCommit.getTime()));
        }
        {
            final Commit lastCommit = commitLog.findByRevision("575");
            Assert.assertNotNull("last:rev", lastCommit);
            Assert.assertEquals("last:author", "pkozelka", lastCommit.getAuthor());
            Assert.assertEquals("last:commitMsg", "little cleanup - method killThreadBrutally moved to an utils class", lastCommit.getMsg());
            Assert.assertEquals("last:time", "2008-02-07T09:29:55.843", CommitLog.VCS_TIME_FORMAT.get().format(lastCommit.getTime()));
        }
        final CommitLog commitLog2 = new CommitLog(new InputSource(getClass().getResourceAsStream("/sample-commit-log-2.xml")));
        final Collection<Commit> newCommits = commitLog2.getCommits();
        Assert.assertEquals("newCommits.count", 5, newCommits.size());
        Assert.assertNull("commits.r979", commitLog.findByRevision("979"));
        final Collection<Commit> overlap = commitLog.addAll(newCommits);
        Assert.assertEquals("overlap.size", 3, overlap.size());
        Assert.assertEquals("merged.count", 12, commitLog.getCommits().size());
        Assert.assertNotNull("commits.r979", commitLog.findByRevision("979"));
        commitLog.save(new StreamResult(System.out));
        Assert.assertTrue(true);
    }
}
