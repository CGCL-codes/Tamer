package org.charvolant.tmsnet.model;

import org.junit.Assert;
import org.charvolant.tmsnet.resources.ResourceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SystemInformation}
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class SystemInformationTest {

    private PVRState state;

    private SystemDescription desc;

    private SystemInformation info;

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        this.state = new PVRState();
        this.state.setLocator(new ResourceLocator());
        this.desc = new SystemDescription();
        this.desc.setAudLanguage(Language.ARABIC);
        this.desc.setFitting(DisplayFitting.LETTERBOX);
        this.desc.setHdOutputMode(HDOutputMode.COMPONENT);
        this.desc.setHdVideoFormat(VideoStandard.VS_1080I60);
        this.desc.setIBoxPos(70);
        this.desc.setIBoxTime(100);
        this.desc.setOsdAlpha(55);
        this.desc.setOsdAnimation(true);
        this.desc.setOsdLanguage(Language.DANISH);
        this.desc.setOsdTextScroll(TextScrollMode.PART);
        this.desc.setScartType(SCARTType.EXTERNAL);
        this.desc.setSoundMode(SoundMode.LEFT);
        this.desc.setSubLanguage(Language.SWEDISH);
        this.desc.setTimeshift(TimeShiftMode.TS_3HOUR);
        this.desc.setTimeshiftStorage(1000);
        this.desc.setTvAspectRatio(AspectRatio.RATIO_4_3);
        this.desc.setTvType(TVType.NTSC);
        this.desc.setUhfChannel(65);
        this.desc.setUhfType(5);
        this.desc.setVolume(12);
        this.info = new SystemInformation(this.state, this.desc);
    }

    /**
   * @throws java.lang.Exception
   */
    @After
    public void tearDown() throws Exception {
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#SystemInformation(org.charvolant.tmsnet.model.PVRState, org.charvolant.tmsnet.model.SystemDescription)}.
   */
    @Test
    public void testSystemInformationPVRStateSystemDescription() {
        Assert.assertEquals(ResourceLocator.RECORDER_URI + "unknown", this.info.getResource().getURI());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getVolume()}.
   */
    @Test
    public void testGetVolume() {
        Assert.assertEquals(12, this.info.getVolume());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getOsdLanguage()}.
   */
    @Test
    public void testGetOsdLanguage() {
        Assert.assertEquals("Danish", this.info.getOsdLanguage());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getSubLanguage()}.
   */
    @Test
    public void testGetSubLanguage() {
        Assert.assertEquals("Swedish", this.info.getSubLanguage());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getAudLanguage()}.
   */
    @Test
    public void testGetAudLanguage() {
        Assert.assertEquals("Arabic", this.info.getAudLanguage());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getTvType()}.
   */
    @Test
    public void testGetTvType() {
        Assert.assertEquals("4:3", this.info.getTvAspectRatio());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getHdOutputMode()}.
   */
    @Test
    public void testGetHdOutputMode() {
        Assert.assertEquals("Component", this.info.getHdOutputMode());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getHdVideoFormat()}.
   */
    @Test
    public void testGetHdVideoFormat() {
        Assert.assertEquals("1080i (60Hz)", this.info.getHdVideoFormat());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#isScartOut()}.
   */
    @Test
    public void testIsScartOut() {
        Assert.assertEquals(false, this.info.isScartOut());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getScartType()}.
   */
    @Test
    public void testGetScartType() {
        Assert.assertEquals("Passthrough", this.info.getScartType());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getTvAspectRatio()}.
   */
    @Test
    public void testGetTvAspectRatio() {
        Assert.assertEquals("4:3", this.info.getTvAspectRatio());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getFitting()}.
   */
    @Test
    public void testGetFitting() {
        Assert.assertEquals("Letterbox", this.info.getFitting());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getSoundMode()}.
   */
    @Test
    public void testGetSoundMode() {
        Assert.assertEquals("Left", this.info.getSoundMode());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getUhfType()}.
   */
    @Test
    public void testGetUhfType() {
        Assert.assertEquals(5, this.info.getUhfType());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getUhfChannel()}.
   */
    @Test
    public void testGetUhfChannel() {
        Assert.assertEquals(65, this.info.getUhfChannel());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getOsdAlpha()}.
   */
    @Test
    public void testGetOsdAlpha() {
        Assert.assertEquals(55, this.info.getOsdAlpha());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getIBoxTime()}.
   */
    @Test
    public void testGetIBoxTime() {
        Assert.assertEquals(100, this.info.getIBoxTime());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getIBoxPos()}.
   */
    @Test
    public void testGetIBoxPos() {
        Assert.assertEquals(70, this.info.getIBoxPos());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getTimeshift()}.
   */
    @Test
    public void testGetTimeshift() {
        Assert.assertEquals("Timeshift 3 hours", this.info.getTimeshift());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getTimeshiftStorage()}.
   */
    @Test
    public void testGetTimeshiftStorage() {
        Assert.assertEquals(1000, this.info.getTimeshiftStorage());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#isOsdAnimation()}.
   */
    @Test
    public void testIsOsdAnimation() {
        Assert.assertEquals(true, this.info.isOsdAnimation());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#getOsdTextScroll()}.
   */
    @Test
    public void testGetOsdTextScroll() {
        Assert.assertEquals("Partial", this.info.getOsdTextScroll());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.SystemInformation#toDescription()}.
   */
    @Test
    public void testToDescription() {
        SystemDescription d = this.info.toDescription();
        Assert.assertEquals(this.desc.getAudLanguage(), d.getAudLanguage());
        Assert.assertEquals(this.desc.getFitting(), d.getFitting());
        Assert.assertEquals(this.desc.getHdOutputMode(), d.getHdOutputMode());
        Assert.assertEquals(this.desc.getHdVideoFormat(), d.getHdVideoFormat());
        Assert.assertEquals(this.desc.getIBoxPos(), d.getIBoxPos());
        Assert.assertEquals(this.desc.getIBoxTime(), d.getIBoxTime());
        Assert.assertEquals(this.desc.getOsdAlpha(), d.getOsdAlpha());
        Assert.assertEquals(this.desc.getOsdLanguage(), d.getOsdLanguage());
        Assert.assertEquals(this.desc.getOsdTextScroll(), d.getOsdTextScroll());
        Assert.assertEquals(this.desc.getScartType(), d.getScartType());
        Assert.assertEquals(this.desc.getSoundMode(), d.getSoundMode());
        Assert.assertEquals(this.desc.getSubLanguage(), d.getSubLanguage());
        Assert.assertEquals(this.desc.getTimeshift(), d.getTimeshift());
        Assert.assertEquals(this.desc.getTimeshiftStorage(), d.getTimeshiftStorage());
        Assert.assertEquals(this.desc.getTvAspectRatio(), d.getTvAspectRatio());
        Assert.assertEquals(this.desc.getTvType(), d.getTvType());
        Assert.assertEquals(this.desc.getUhfChannel(), d.getUhfChannel());
        Assert.assertEquals(this.desc.getUhfType(), d.getUhfType());
        Assert.assertEquals(this.desc.getVolume(), d.getVolume());
        Assert.assertEquals(this.desc.isOsdAnimation(), d.isOsdAnimation());
        Assert.assertEquals(this.desc.isScartOut(), d.isScartOut());
    }
}
