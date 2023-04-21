package net.sf.webnzb.domain.impl;

import java.util.Arrays;
import java.util.List;
import net.sf.webnzb.domain.NNTPFile;
import net.sf.webnzb.domain.NNTPFileSegment;
import com.agical.rmock.extension.junit.RMockTestCase;

public class DefaultNNTPPostTest extends RMockTestCase {

    private DefaultNNTPPost post;

    private NNTPFile file;

    protected void setUp() {
        file = (NNTPFile) mock(NNTPFile.class);
        post = new DefaultNNTPPost();
    }

    public void testAddSingleFile() {
        file.calculateSize();
        modify().returnValue(100);
        startVerification();
        post.addFile(file);
        assertEquals(100, post.getSize());
        assertEquals(1, post.getFiles().size());
    }

    public void testAddMultipleFile() {
        file.calculateSize();
        modify().returnValue(100);
        NNTPFile secondFile = (NNTPFile) mock(NNTPFile.class);
        secondFile.calculateSize();
        modify().returnValue(9);
        startVerification();
        post.addFile(file);
        post.addFile(secondFile);
        assertEquals(109, post.getSize());
        assertEquals(2, post.getFiles().size());
    }

    public void testGetFilesIsUnmodifiable() {
        startVerification();
        try {
            post.getFiles().add(file);
            fail();
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testEqualsAndHashcodeSameObject() {
        file.calculateSize();
        modify().multiplicity(expect.exactly(2)).returnValue(100);
        startVerification();
        post.addFile(file);
        DefaultNNTPPost secondPost = new DefaultNNTPPost();
        secondPost.addFile(file);
        assertTrue(secondPost.equals(post));
        assertEquals(secondPost.hashCode(), post.hashCode());
    }

    public void testEqualsAndHashcodeDifferentObject() {
        file.calculateSize();
        modify().returnValue(100);
        startVerification();
        post.addFile(file);
        DefaultNNTPPost secondPost = new DefaultNNTPPost();
        assertFalse(secondPost.equals(post));
        assertFalse(secondPost.hashCode() == post.hashCode());
    }

    public void testDetermineSegmentsToDownloadMoreThanMaximumAvailable() {
        file.calculateSize();
        modify().returnValue(100);
        NNTPFileSegment segment = (NNTPFileSegment) mock(NNTPFileSegment.class);
        file.determineSegmentsToDownload(1);
        modify().returnValue(Arrays.asList(segment));
        startVerification();
        post.addFile(file);
        List<NNTPFileSegment> segments = post.determineSegmentsToDownload(1);
        assertEquals(1, segments.size());
        assertEquals(segment, segments.get(0));
    }

    public void testDetermineSegmentsToDownloadLessThanMaximumAvailable() {
        file.calculateSize();
        modify().returnValue(100);
        NNTPFileSegment segment = (NNTPFileSegment) mock(NNTPFileSegment.class);
        file.determineSegmentsToDownload(10);
        modify().returnValue(Arrays.asList(segment));
        startVerification();
        post.addFile(file);
        List<NNTPFileSegment> segments = post.determineSegmentsToDownload(10);
        assertEquals(1, segments.size());
        assertEquals(segment, segments.get(0));
    }
}
