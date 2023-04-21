package com.leohart.buildwhisperer.adapt;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.leohart.buildwhisperer.BuildStatus;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author Leo Hart
 */
public class TitlePartSyndEntryImplBuildStatusExtractorTest {

    TitlePartSyndEntryImplBuildStatusExtractor extractor;

    SyndEntryImpl entry;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        this.entry = createMock(SyndEntryImpl.class);
        this.extractor = new TitlePartSyndEntryImplBuildStatusExtractor(" ", "(SUCCESS)", 2);
    }

    /**
	 * Test method for
	 * {@link com.leohart.buildwhisperer.adapt.TitlePartSyndEntryImplBuildStatusExtractor#getBuildStatus(com.sun.syndication.feed.synd.SyndEntryImpl)}.
	 */
    @Test
    public void testGetBuildStatusSuccess() {
        expect(this.entry.getTitle()).andReturn("name #1235 (SUCCESS)");
        replay(this.entry);
        Assert.assertEquals("BuildStatus should have been Success: ", BuildStatus.Success, this.extractor.getBuildStatus(this.entry));
    }

    /**
	 * Test method for
	 * {@link com.leohart.buildwhisperer.adapt.TitlePartSyndEntryImplBuildStatusExtractor#getBuildStatus(com.sun.syndication.feed.synd.SyndEntryImpl)}.
	 */
    @Test
    public void testGetBuildStatusFailure() {
        expect(this.entry.getTitle()).andReturn("name #1235 (FAILUREORANYTHINGELSE)");
        replay(this.entry);
        Assert.assertEquals("BuildStatus should have been Success: ", BuildStatus.Failure, this.extractor.getBuildStatus(this.entry));
    }
}
