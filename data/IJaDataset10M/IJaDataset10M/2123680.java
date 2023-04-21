package edu.rice.cs.drjava.model.repl;

import junit.framework.*;
import edu.rice.cs.drjava.ui.*;
import edu.rice.cs.drjava.model.GlobalModel;
import edu.rice.cs.drjava.model.repl.InteractionsDocumentTest.TestBeep;
import edu.rice.cs.util.text.DocumentAdapterException;
import edu.rice.cs.util.swing.Utilities;

/**
 * Tests the functionality of the InteractionsDocumentAdapter.
 */
public final class InteractionsDocumentAdapterTest extends TestCase {

    protected InteractionsDocumentAdapter _adapter;

    protected InteractionsModel _model;

    protected InteractionsDocument _doc;

    protected InteractionsPane _pane;

    protected MainFrame mf;

    /**
   * Initialize fields for each test.
   */
    protected void setUp() {
        mf = new MainFrame();
        GlobalModel gm = mf.getModel();
        _model = gm.getInteractionsModel();
        _adapter = gm.getSwingInteractionsDocument();
        _doc = gm.getInteractionsDocument();
    }

    /**
   * Tests that the styles list is updated and reset properly
   */
    public void testStylesListContentAndReset() throws DocumentAdapterException {
        assertEquals("StylesList before insert should contain 2 pairs", 2, _adapter.getStylesList().size());
        _doc.insertText(_doc.getLength(), "5", InteractionsDocument.NUMBER_RETURN_STYLE);
        assertEquals("StylesList before reset should contain 3 pairs", 3, _adapter.getStylesList().size());
        assertEquals("The first element of StylesList before reset should be", "((21, 22), number.return.style)", _adapter.getStylesList().get(0).toString());
        assertEquals("The second element of StylesList before reset should be", "((19, 21), default)", _adapter.getStylesList().get(1).toString());
        assertEquals("The third element of StylesList before reset should be", "((0, 19), object.return.style)", _adapter.getStylesList().get(2).toString());
        synchronized (_model) {
            _model.setWaitingForFirstInterpreter(false);
            _model.resetInterpreter();
            Utilities.clearEventQueue();
            _model.interpreterResetting();
            Utilities.clearEventQueue();
            assertEquals("StylesList after reset should contain 1 pair", 1, _adapter.getStylesList().size());
            assertTrue("The only element of the StylesList after reset should be similar to ((48, 74), error)", _adapter.getStylesList().get(0).toString().matches("\\(\\(4\\d, 7\\d\\)\\, error\\)"));
        }
    }

    /**
   * Tests that a null style is not added to the list. Fix for bug #995719
   */
    public void testCannotAddNullStyleToList() throws DocumentAdapterException {
        assertEquals("StylesList before insert should contain 2 pairs", 2, _adapter.getStylesList().size());
        _doc.insertText(_doc.getLength(), "5", InteractionsDocument.NUMBER_RETURN_STYLE);
        assertEquals("StylesList should contain 3 pairs", 3, _adapter.getStylesList().size());
        _doc.insertText(_doc.getLength(), "6", null);
        assertEquals("StylesList should still contain 3 pairs - null string should not have been inserted", 3, _adapter.getStylesList().size());
    }
}
