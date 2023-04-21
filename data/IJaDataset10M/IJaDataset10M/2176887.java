package test.net.hawk.digiextractor.digic;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import net.hawk.digiextractor.digic.AbstractNameEntry;
import net.hawk.digiextractor.digic.FileSystemVersion;
import net.hawk.digiextractor.digic.NameListFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class NamesTest.
 * Test whether different name lists are parsed correctly.
 */
public class NamesTest {

    /** The size of the ByteBuffer. */
    private static final int BUFFER_SIZE = 0xFFFF;

    /** contains the required values for the recording numbers for the
	 * TSD_V1 entries. */
    private static final int[] REQ_NUMBERS_TSDV1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 26, 27, 28 };

    /** The Constant REQ_NAME_TSDV1. */
    private static final String[] REQ_NAME_TSDV1 = { "Malcolm mittendrin", "Malcolm mittendrin", "Malcolm mittendrin", "Findet Nemo", "extra 3", "Staatsanwalt Posch ermittelt", "Malcolm mittendrin", "Malcolm mittendrin", "Shrek - Der tollk�hne Held", "Malcolm mittendrin", "Staatsanwalt Posch ermittelt", "Malcolm mittendrin", "Staatsanwalt Posch ermittelt", "Shrek 2 - Der tollk�hne Held kehrt zu", "Malcolm mittendrin", "Staatsanwalt Posch ermittelt", "Malcolm mittendrin", "Schwiegertochter gesucht", "Malcolm mittendrin", "Malcolm mittendrin", "Malcolm mittendrin", "Extra 3", "Extra 3", "Rockpalast", "die story", "Was liest du?", "Was liest du?", "Der Soldat James Ryan" };

    /** The Constant REQ_NAMES_TSDV2. */
    private static final String[] REQ_NAME_TSDV2 = { "NoName", "Im Zeichen der Jungfrau", "Flipper & Lopaka", "Freitag Nacht News - Reloaded", "Flipper & Lopaka", "Chicago", "Wickie und die starken M�nner", "Flipper & Lopaka", "Wickie und die starken M�nner", "Die Sendung mit der Maus", "Wickie und die starken M�nner", "Marvi H�mmer", "Der kleine Eisb�r", "H�rspielserie", "Wickie und die starken M�nner", "Von der Lust besessen", "Flipper & Lopaka", "Quarks & Co", "heute-journal", "Von der Lust besessen", "Flipper & Lopaka", "Wickie und die starken M�nner", "Rendezvous der Sinne, Teil 1", "Der Zauberer von OZ", "Army go home", "Ihre zweite Chance", "Nuhr vom Feinsten", "Quarks & Co" };

    /** The Constant REQ_NAMES_TSDV2P. */
    private static final String[] REQ_NAME_TSDV2P = { "Miami Vice", "Spiegel TV Magazin", "Zwischen Klapperstorch und Retorte", "ZDF Olympia live", "ZDF Olympia live" };

    /** contains the required values for the recording numbers for the
	 * TSD_V2 entries. */
    private static final int[] REQ_NUMBERS_TSDV2 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 14, 15, 17, 19, 20, 22, 23, 24, 25, 26, 31, 32, 37, 40, 53, 55 };

    /** contains the required values for the recording numbers for the
	 * TSD_V2+ entries. */
    private static final int[] REQ_NUMBERS_TSDV2P = { 0, 1, 2, 4, 5 };

    /** contains the required values for the recording numbers for the
	 * new HD8+ update entries. */
    private static final int[] REQ_NUMBERS_UPD1578 = { 0, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

    /** contains the required names for the recording numbers for the
	 * new HD8+ update entries. */
    private static final String[] REQ_NAME_UPD1578 = { "extra 3", "Elvis: The Great Performances", "Ein Mann und sein Hund", "Bubba Ho-tep", "Birth of Rock", "Elvis: The Great Performances", "Elvis: The Great Performances", "Elvis '56", "Classic Albums", "Elvis by the Presleys", "Acapulco", "Elvis in Las Vegas", "Jailhouse Rock", "Elvis: '68 Comeback Special" };

    /** Byte Buffer containing TSDV1 data. */
    private static ByteBuffer namesTSDV1 = ByteBuffer.allocate(BUFFER_SIZE);

    /** Byte Buffer containing TSDV2 data. */
    private static ByteBuffer namesTSDV2 = ByteBuffer.allocate(BUFFER_SIZE);

    /** Byte Buffer containing TSDV2+ data. */
    private static ByteBuffer namesTSDV2p = ByteBuffer.allocate(BUFFER_SIZE);

    /** Byte Buffer containing TSDV2+ data (new Version) data. */
    private static ByteBuffer namesUpd1578 = ByteBuffer.allocate(BUFFER_SIZE);

    /**
	 * Sets the up before class.
	 * 
	 * @throws Exception the exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        FileInputStream fs = new FileInputStream(new File("./testdata/names_t1.dat"));
        fs.getChannel().read(namesTSDV1);
        namesTSDV1.rewind();
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_s2.dat"));
        fs.getChannel().read(namesTSDV2);
        namesTSDV2.rewind();
        namesTSDV2.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_hd8+.dat"));
        fs.getChannel().read(namesTSDV2p);
        namesTSDV2p.rewind();
        namesTSDV2p.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_upd1578.dat"));
        fs.getChannel().read(namesUpd1578);
        namesUpd1578.rewind();
        namesUpd1578.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
    }

    /**
	 * Test parsing the TSDV1 name list.
	 * @throws IOException in case of a read error.
	 */
    @Test
    public final void testParseTSDV1Names() throws IOException {
        List<AbstractNameEntry> names = NameListFactory.parseNameList(namesTSDV1, FileSystemVersion.TSD_V1);
        assertEquals(REQ_NAME_TSDV1.length, names.size());
        for (int i = 0; i < names.size(); ++i) {
            assertEquals(REQ_NUMBERS_TSDV1[i], names.get(i).getNumber());
            assertEquals(REQ_NAME_TSDV1[i], names.get(i).getShortName());
        }
    }

    /**
	 * Test parsing the TSDV2 name list.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
    public final void testParseTSDV2Names() throws IOException {
        List<AbstractNameEntry> names = NameListFactory.parseNameList(namesTSDV2, FileSystemVersion.TSD_V2);
        assertEquals(REQ_NAME_TSDV2.length, names.size());
        for (int i = 0; i < names.size(); ++i) {
            assertEquals(REQ_NUMBERS_TSDV2[i], names.get(i).getNumber());
            assertEquals(REQ_NAME_TSDV2[i], names.get(i).getShortName());
        }
    }

    /**
	 * Test parsing the TSDV2+ name list.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
    public final void testParseTSDV2pNames() throws IOException {
        List<AbstractNameEntry> names = NameListFactory.parseNameList(namesTSDV2p, FileSystemVersion.TSD_V2);
        assertEquals(REQ_NAME_TSDV2P.length, names.size());
        for (int i = 0; i < names.size(); ++i) {
            assertEquals(REQ_NUMBERS_TSDV2P[i], names.get(i).getNumber());
            assertEquals(REQ_NAME_TSDV2P[i], names.get(i).getShortName());
        }
    }

    /**
	 * Test parsing the TSDV2+ name list (new SW-Version).
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
    public final void testParseUpd1578Names() throws IOException {
        List<AbstractNameEntry> names = NameListFactory.parseNameList(namesUpd1578, FileSystemVersion.TSD_V2);
        assertEquals(REQ_NAME_UPD1578.length, names.size());
        for (int i = 0; i < names.size(); ++i) {
            assertEquals(REQ_NUMBERS_UPD1578[i], names.get(i).getNumber());
            assertEquals(REQ_NAME_UPD1578[i], names.get(i).getShortName());
        }
    }
}
