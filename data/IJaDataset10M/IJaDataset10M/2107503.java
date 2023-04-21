package gui;

import junit.framework.TestCase;
import logic.DrawingFrameLogic;

/**
 * Testklasse fuer das Zeichenfenster
 * @author Christian
 *
 */
public class DrawingFrameTest extends TestCase {

    /**
	 * Instanz des Zeichenfensters
	 */
    private DrawingFrame df = new DrawingFrame();

    /**
	 * Instanz der Zeichenfensterlogik
	 */
    private DrawingFrameLogic dfl = new DrawingFrameLogic();

    /**
	 * Array fuer Testnachrichten
	 */
    private String[] testMessages;

    /**
	 * Standardkonstruktor
	 * @param name name
	 */
    public DrawingFrameTest(String name) {
        super(name);
    }

    /**
	 * Tests vorbereiten
	 */
    protected void setUp() {
    }

    /**
	 * Tests nachbereiten
	 */
    protected void tearDown() {
    }

    /**
	 * Test: Aufruf des Zeichenfensters inkl. Initialisierung der Oberflaeche
	 * und Anbindung der Logik
	 *
	 */
    public void testDrawingFrameDrawingFrameLogicString() {
    }

    /**
	 * Test: Aufruf des Zeichenfensters ueber leeren Konstruktor um Tests
	 * durchfuehren zu koennen
	 *
	 */
    public void testDrawingFrame() {
    }

    /**
	 * Test: Logik anbinden
	 *
	 */
    public void testInitLogic() {
        assertNull(df.getLogic());
        df.initLogic(dfl);
        assertEquals(df.getLogic(), dfl);
    }

    /**
	 * Test: Logik entfernen
	 *
	 */
    public void testReleaselogic() {
        assertNull(df.getLogic());
        df.initLogic(dfl);
        assertNotNull(df.getLogic());
        df.releaselogic();
        assertNull(df.getLogic());
    }

    /**
	 * Test: Getter fuer die Logik
	 *
	 */
    public void testGetLogic() {
        assertNull(df.getLogic());
        df.initLogic(dfl);
        assertEquals(df.getLogic(), dfl);
    }

    /**
	 * Test: Linie zeichnen
	 *
	 */
    public void testDrawLine() {
    }

    /**
	 * Test: Zeichenfeld loeschen
	 *
	 */
    public void testClearScreen() {
    }

    /**
	 * Test: Text aus Eingabefeld einlesen
	 *
	 */
    public void testGetMessage() {
        assertEquals(df.getMessage(), "");
        df.setITFText("test1");
        assertEquals(df.getMessage(), "test1");
    }

    /**
	 * Test: Nachrichten ins Chatfenster einfuegen
	 *
	 */
    public void testAddMessage() {
        testMessages = new String[] { "test1" };
        assertEquals(df.getCTAText(), "");
        df.addMessage(testMessages, testMessages.length);
        assertEquals(df.getCTAText(), "test1\n");
    }

    /**
	 * Test: Text ins Eingabefeld schreiben
	 *
	 */
    public void testSetITFText() {
        assertEquals(df.getMessage(), "");
        df.setITFText("test1");
        assertEquals(df.getMessage(), "test1");
    }

    /**
	 * Test: Text aus Chatfenster auslesen
	 *
	 */
    public void testGetCTAText() {
        testMessages = new String[] { "test1" };
        assertEquals(df.getCTAText(), "");
        df.addMessage(testMessages, testMessages.length);
        assertEquals(df.getCTAText(), "test1\n");
    }

    /**
	 * Test: Chat loeschen
	 *
	 */
    public void testClearChat() {
        testMessages = new String[] { "test1" };
        df.addMessage(testMessages, testMessages.length);
        assertEquals(df.getCTAText(), "test1\n");
        df.clearChat();
        assertEquals(df.getCTAText(), "");
    }

    /**
	 * Test: Zeichentimer Wert zuweisen
	 *
	 */
    public void testSetDrawingTimerValue() {
        assertEquals(df.getDrawingTimerValue(), 0);
        df.setDrawingTimerValue(6);
        assertEquals(df.getDrawingTimerValue(), 6);
    }

    /**
	 * Test: Getter fuer Wert des Zeichentimers
	 *
	 */
    public void testGetDrawingTimerValue() {
        assertEquals(df.getDrawingTimerValue(), 0);
        df.setDrawingTimerValue(6);
        assertEquals(df.getDrawingTimerValue(), 6);
    }

    /**
	 * Test: Abfrage - ist Eingabefeld Quelle
	 *
	 */
    public void testIsInput() {
        assertFalse(df.isInput(this));
        assertTrue(df.isInput(df.getITF()));
    }

    /**
	 * Test: Getter fuer das Eingabefeld
	 *
	 */
    public void testGetITF() {
    }

    /**
	 * Test: Fenster schliessen
	 *
	 */
    public void testCloseFrame() {
    }

    /**
	 * Test: Chat aktivieren
	 *
	 */
    public void testActivateChat() {
        df.activateChat();
        assertTrue(df.getChatStatus());
    }

    /**
	 * Test: Chat deaktivieren
	 *
	 */
    public void testDeactivateChat() {
        df.deactivateChat();
        assertFalse(df.getChatStatus());
    }

    /**
	 * Test: Abfrage - Status vom Chat
	 *
	 */
    public void testGetChatStatus() {
        df.activateChat();
        assertTrue(df.getChatStatus());
        df.deactivateChat();
        assertFalse(df.getChatStatus());
    }

    /**
	 * Test: Zeichenfeld aktivieren
	 *
	 */
    public void testActivateDrawingPanel() {
    }

    /**
	 * Test: Zeichenfeld deaktivieren
	 *
	 */
    public void testDeactivateDrawingPanel() {
    }

    /**
	 * Test: Text des Ergebnislabels setzen
	 * 
	 */
    public void testSetResultLabelText() {
        assertEquals(df.getResultLabelText(), "");
        df.setResultLabelText("test");
        assertEquals(df.getResultLabelText(), "test");
    }

    /**
	 * Test: Abfrage - Text des Ergebnislabels
	 *
	 */
    public void testGetResultLabelText() {
        assertEquals(df.getResultLabelText(), "");
        df.setResultLabelText("test");
        assertEquals(df.getResultLabelText(), "test");
    }
}
