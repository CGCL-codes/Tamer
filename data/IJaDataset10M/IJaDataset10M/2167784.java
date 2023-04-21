package lamao.soh.core;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import org.junit.Test;
import com.thoughtworks.xstream.XStream;

/**
 * @author lamao
 *
 */
public class SHPlayerInfoTest {

    @Test
    public void testXmlStorage() throws FileNotFoundException {
        SHPlayerInfo player = new SHPlayerInfo();
        player.setLives(5);
        player.setName("ugly player");
        SHEpochInfo epoch1 = new SHEpochInfo();
        epoch1.getLevels().add(new SHLevelInfo());
        epoch1.getLevels().add(new SHLevelInfo());
        SHEpochInfo epoch2 = new SHEpochInfo();
        epoch2.getLevels().add(new SHLevelInfo());
        epoch2.setName("epoch2");
        player.getEpochs().add(epoch1);
        player.getEpochs().add(epoch2);
        player.setCurrentEpoch(epoch2);
        XStream xstream = new XStream();
        xstream.processAnnotations(SHLevelInfo.class);
        xstream.processAnnotations(SHEpochInfo.class);
        xstream.processAnnotations(SHPlayerInfo.class);
        String xml = xstream.toXML(player);
        SHPlayerInfo player2 = (SHPlayerInfo) xstream.fromXML(xml);
        assertEquals(player.getName(), player2.getName());
        assertEquals(player.getLives(), player2.getLives());
        assertEquals(player.getCurrentEpoch().getName(), player2.getCurrentEpoch().getName());
        assertEquals(player.getEpochs().size(), player2.getEpochs().size());
        for (int i = 0; i < player.getEpochs().size(); i++) {
            assertEquals(player.getEpochs().get(i).getLevels().size(), player2.getEpochs().get(i).getLevels().size());
        }
    }
}
