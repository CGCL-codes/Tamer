package be.lassi.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.awt.Rectangle;
import java.io.Reader;
import java.io.StringReader;
import org.testng.annotations.Test;
import be.lassi.base.Dirty;
import be.lassi.base.DirtyStub;
import be.lassi.cues.Cue;
import be.lassi.cues.CueChannelLevel;
import be.lassi.cues.CueSubmasterLevel;
import be.lassi.cues.LightCueDetail;
import be.lassi.domain.Channel;
import be.lassi.domain.Dimmer;
import be.lassi.domain.FrameProperties;
import be.lassi.domain.Group;
import be.lassi.domain.Groups;
import be.lassi.domain.Level;
import be.lassi.domain.Show;
import be.lassi.domain.Submaster;
import be.lassi.domain.Submasters;

/**
 * Tests class <code>XmlShowParser</code>.
 */
public class XmlShowReaderTestCase {

    private final Dirty dirty = new DirtyStub();

    @Test
    public void parseShow() {
        String xml = "<show name=\"Example\"/>";
        Show show = toShow(xml);
        assertEquals(show.getName(), "Example");
    }

    @Test
    public void parseChannel() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "  </fixtures>\n" + "</show>\n";
        Show show = toShow(xml);
        Channel channel1 = show.getChannels().get(0);
        Channel channel2 = show.getChannels().get(1);
        assertEquals(channel1.getName(), "Channel 1");
        assertEquals(channel2.getName(), "Channel 2");
        assertEquals(channel1.getId(), 0);
        assertEquals(channel2.getId(), 1);
        assertTrue(channel1.getDirty() == dirty);
        assertTrue(channel2.getDirty() == dirty);
    }

    @Test
    public void parseDimmer() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <patch-lines>\n" + "    <patch-line id=\"1\" name=\"Dimmer 1\" fixture-id=\"2\"/>\n" + "    <patch-line id=\"2\" name=\"Dimmer 2\" fixture-id=\"1\"/>\n" + "    <patch-line id=\"3\" name=\"Dimmer 3\" fixture-id=\"1\"/>\n" + "    <patch-line id=\"4\" name=\"Dimmer 4\"/>\n" + "  </patch-lines>\n" + "</show>\n";
        Show show = toShow(xml);
        Channel channel1 = show.getChannels().get(0);
        Channel channel2 = show.getChannels().get(1);
        Channel channel3 = show.getChannels().get(2);
        Dimmer dimmer1 = show.getDimmers().get(0);
        Dimmer dimmer2 = show.getDimmers().get(1);
        Dimmer dimmer3 = show.getDimmers().get(2);
        Dimmer dimmer4 = show.getDimmers().get(3);
        assertEquals(dimmer1.getName(), "Dimmer 1");
        assertEquals(dimmer2.getName(), "Dimmer 2");
        assertEquals(dimmer3.getName(), "Dimmer 3");
        assertEquals(dimmer4.getName(), "Dimmer 4");
        assertEquals(dimmer1.getId(), 0);
        assertEquals(dimmer2.getId(), 1);
        assertEquals(dimmer3.getId(), 2);
        assertEquals(dimmer4.getId(), 3);
        assertTrue(dimmer1.getDirty() == dirty);
        assertTrue(dimmer2.getDirty() == dirty);
        assertTrue(dimmer3.getDirty() == dirty);
        assertTrue(dimmer4.getDirty() == dirty);
        assertEquals(dimmer1.getChannelId(), 1);
        assertEquals(dimmer2.getChannelId(), 0);
        assertEquals(dimmer3.getChannelId(), 0);
        assertEquals(dimmer4.getChannelId(), -1);
        assertTrue(channel1.isPatched());
        assertTrue(channel2.isPatched());
        assertFalse(channel3.isPatched());
    }

    @Test
    public void parseGroup() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <groups>\n" + "    <group name=\"Group 1\"/>\n" + "    <group name=\"Group 2\">\n" + "      <comment>\n" + "        <line text=\"comment line 1\"/>\n" + "        <line text=\"comment line 2\"/>\n" + "      </comment>\n" + "      <fixtures>\n" + "        <fixture id=\"1\"/>\n" + "      </fixtures>\n" + "    </group>\n" + "    <group name=\"Group 3\">\n" + "      <fixtures>\n" + "        <fixture id=\"1\"/>\n" + "        <fixture id=\"2\"/>\n" + "        <fixture id=\"3\"/>\n" + "      </fixtures>\n" + "    </group>\n" + "  </groups>\n" + "</show>\n";
        Show show = toShow(xml);
        Groups groups = show.getGroups();
        assertEquals(groups.size(), 3);
        Group group1 = groups.get(0);
        Group group2 = groups.get(1);
        Group group3 = groups.get(2);
        assertEquals(group1.getName(), "Group 1");
        assertEquals(group2.getName(), "Group 2");
        assertEquals(group3.getName(), "Group 3");
        assertEquals(group2.getComment(), "comment line 1\ncomment line 2");
        assertTrue(group1.getDirty() == dirty);
        assertTrue(group2.getDirty() == dirty);
        assertTrue(group3.getDirty() == dirty);
        Channel[] channels1 = group1.getChannels();
        Channel[] channels2 = group2.getChannels();
        Channel[] channels3 = group3.getChannels();
        assertEquals(channels1.length, 0);
        assertEquals(channels2.length, 1);
        assertEquals(channels3.length, 3);
        assertEquals(channels2[0].getName(), "Channel 1");
        assertEquals(channels3[0].getName(), "Channel 1");
        assertEquals(channels3[1].getName(), "Channel 2");
        assertEquals(channels3[2].getName(), "Channel 3");
    }

    @Test
    public void parseGroupWithUnknownFixture() {
        String xml = "<show name=\"test\">\n" + "  <groups>\n" + "    <group name=\"Group\">\n" + "      <fixtures>\n" + "        <fixture id=\"1\"/>\n" + "      </fixtures>\n" + "    </group>\n" + "  </groups>\n" + "</show>\n";
        assertError(xml, "ERROR at line 5, column 26: Unknown fixture with id \"1\"\n");
    }

    @Test
    public void parseCueList() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <submasters>\n" + "    <submaster id=\"1\" name=\"Submaster 1\"/>\n" + "    <submaster id=\"2\" name=\"Submaster 2\">\n" + "      <scene>\n" + "        <set fixture-id=\"1\" attribute=\"Intensity\" value=\"100\"/>\n" + "        <set fixture-id=\"2\" attribute=\"Intensity\" value=\"50\"/>\n" + "      </scene>\n" + "    </submaster>\n" + "  </submasters>\n" + "  <cue-lists>\n" + "    <cue-list id=\"1\" name=\"Cue list 1\">\n" + "      <comment>\n" + "        <line text=\"cuelist comment line 1\"/>\n" + "        <line text=\"cuelist comment line 2\"/>\n" + "      </comment>\n" + "      <cue number=\"1.1\" page=\"page\" prompt=\"prompt\" description=\"L 2\">\n" + "        <comment>\n" + "          <line text=\"cue comment line 1\"/>\n" + "          <line text=\"cue comment line 2\"/>\n" + "        </comment>\n" + "        <submasters>\n" + "          <submaster id=\"1\" value=\"derived\"/>\n" + "          <submaster id=\"2\" value=\"77\"/>\n" + "        </submasters>\n" + "        <scene>\n" + "          <set fixture-id=\"1\" attribute=\"Intensity\" value=\"50\"/>\n" + "          <set fixture-id=\"2\" attribute=\"Intensity\" value=\"derived\"/>\n" + "        </scene>\n" + "      </cue>\n" + "    </cue-list>\n" + "  </cue-lists>\n" + "</show>\n";
        Show show = toShow(xml);
        assertEquals(show.getCues().size(), 1);
        Cue cue = show.getCues().get(0);
        assertTrue(cue.getDirty() == dirty);
        assertEquals(cue.getNumber(), "1.1");
        assertEquals(cue.getPage(), "page");
        assertEquals(cue.getPrompt(), "cue comment line 1\ncue comment line 2");
        assertEquals(cue.getDescription(), "L 2");
        LightCueDetail detail = (LightCueDetail) cue.getDetail();
        CueSubmasterLevel submasterLevel1 = detail.getSubmasterLevel(0);
        CueSubmasterLevel submasterLevel2 = detail.getSubmasterLevel(1);
        assertTrue(submasterLevel1.isActive());
        assertTrue(submasterLevel1.isDerived());
        assertTrue(submasterLevel2.isActive());
        assertFalse(submasterLevel2.isDerived());
        assertEquals(submasterLevel2.getIntValue(), 77);
        CueChannelLevel channelLevel1 = detail.getChannelLevel(0);
        CueChannelLevel channelLevel2 = detail.getChannelLevel(1);
        CueChannelLevel channelLevel3 = detail.getChannelLevel(2);
        assertTrue(channelLevel1.isActive());
        assertTrue(channelLevel2.isActive());
        assertFalse(channelLevel3.isActive());
        assertFalse(channelLevel1.isDerived());
        assertTrue(channelLevel2.isDerived());
        assertFalse(channelLevel3.isDerived());
        assertEquals(channelLevel1.getChannelIntValue(), 50);
        assertEquals(channelLevel2.getChannelIntValue(), 0);
        assertEquals(channelLevel3.getChannelIntValue(), 0);
    }

    @Test
    public void parseSubmasterWithUnknownFixture() {
        String xml = "<show name=\"test\">\n" + "  <submasters>\n" + "    <submaster id=\"1\" name=\"Submaster 1\">\n" + "      <scene>\n" + "        <set fixture-id=\"1\" attribute=\"Intensity\" value=\"100\"/>\n" + "      </scene>\n" + "    </submaster>\n" + "  </submasters>\n" + "</show>\n";
        assertError(xml, "ERROR at line 5, column 64: Unknown fixture with id \"1\"\n");
    }

    @Test
    public void parseSubmaster() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "  </fixtures>\n" + "  <submasters>\n" + "    <submaster id=\"1\" name=\"Submaster 1\"/>\n" + "    <submaster id=\"2\" name=\"Submaster 2\">\n" + "      <comment>\n" + "        <line text=\"line 1\"/>\n" + "        <line text=\"line 2\"/>\n" + "      </comment>\n" + "      <scene>\n" + "        <set fixture-id=\"1\" attribute=\"Intensity\" value=\"100\"/>\n" + "        <set fixture-id=\"2\" attribute=\"Intensity\" value=\"50\"/>\n" + "      </scene>\n" + "    </submaster>\n" + "  </submasters>\n" + "</show>\n";
        Show show = toShow(xml);
        Submasters submasters = show.getSubmasters();
        assertEquals(submasters.size(), 2);
        Submaster submaster1 = submasters.get(0);
        Submaster submaster2 = submasters.get(1);
        assertEquals(submaster1.getId(), 0);
        assertEquals(submaster2.getId(), 1);
        assertEquals(submaster1.getName(), "Submaster 1");
        assertEquals(submaster2.getName(), "Submaster 2");
        assertTrue(submaster1.getDirty() == dirty);
        assertTrue(submaster2.getDirty() == dirty);
        assertEquals(submaster1.getNumberOfLevels(), 2);
        assertEquals(submaster2.getNumberOfLevels(), 2);
        Level level11 = submaster1.getLevel(0);
        Level level12 = submaster1.getLevel(1);
        Level level21 = submaster2.getLevel(0);
        Level level22 = submaster2.getLevel(1);
        assertFalse(level11.isActive());
        assertFalse(level12.isActive());
        assertTrue(level21.isActive());
        assertTrue(level22.isActive());
        assertEquals(level21.getIntValue(), 100);
        assertEquals(level22.getIntValue(), 50);
    }

    @Test
    public void parseWindow() {
        String xml = "<show name=\"test\">\n" + "  <windows>\n" + "    <window id=\"window1\" x=\"11\" y=\"12\" width=\"13\" height=\"14\" visible=\"true\"/>\n" + "    <window id=\"window2\" x=\"21\" y=\"22\" width=\"23\" height=\"24\" visible=\"false\"/>\n" + "  </windows>\n" + "</show>\n";
        Rectangle bounds1 = new Rectangle(11, 12, 13, 14);
        Rectangle bounds2 = new Rectangle(21, 22, 23, 24);
        FrameProperties expected1 = new FrameProperties("window1", bounds1, true);
        FrameProperties expected2 = new FrameProperties("window2", bounds2, false);
        Show show = toShow(xml);
        assertEquals(show.getFrameProperties("window1"), expected1);
        assertEquals(show.getFrameProperties("window2"), expected2);
    }

    private Show toShow(final String xml) {
        Reader reader = new StringReader(xml);
        XmlShowReader showReader = new XmlShowReader(dirty, reader);
        String message = showReader.parse();
        if (message.length() > 0) {
            fail(message);
        }
        return showReader.getShow();
    }

    private void assertError(final String xml, final String expected) {
        Reader reader = new StringReader(xml);
        XmlShowReader showReader = new XmlShowReader(dirty, reader);
        String message = showReader.parse();
        assertEquals(message, expected);
    }
}
