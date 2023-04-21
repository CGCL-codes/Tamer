package org.norecess.noparagraph.tags;

import static org.junit.Assert.assertEquals;
import static org.norecess.noparagraph.frontend.ParagraphInserterLexer.TAG;
import java.io.IOException;
import org.antlr.runtime.CommonToken;
import org.junit.Before;
import org.junit.Test;
import org.norecess.noparagraph.statemachine.ParagraphInserterStateMachine.State;

public class TableOpenTagTest {

    private Helper myHelper;

    @Before
    public void setUp() {
        myHelper = new Helper(new TableOpenTag(new CommonToken(TAG, "<table>")));
    }

    @Test
    public void testOneNewline() throws IOException {
        assertEquals(State.IN_TABLE, myHelper.getToken().oneNewline(myHelper.getFullState()).getState());
        assertEquals("</p>", myHelper.getWritten());
    }
}
