package accpt;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.mtp.pounder.Player;
import com.mtp.gui.WindowWatcher;

public class TestEditPreferences extends AcceptanceTest {

    public TestEditPreferences(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestEditPreferences.class);
    }

    /** Should not be able to open preferences while recording. **/
    public void testCannotEditWhenRecording() throws Exception {
        Player player = PlayerFactory.buildSpeedy("pounder/accpt/TestEditPreferences - open prefs.pnd");
        player.setDisposeWindows(false);
        player.play();
        assertEquals(null, windowWatcher.getWindowByName("PounderPrefsDialog"));
        assertEquals(2, windowWatcher.getWindowCount());
    }
}
