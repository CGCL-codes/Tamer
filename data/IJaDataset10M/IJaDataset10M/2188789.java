package com.vektorsoft.acapulco.gui.menu.edit.extensions;

import com.vektorsoft.acapulco.gui.menu.edit.service.ClipboardData;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

/**
 * <p>
 *  Provides test cases for {@link CopyAction} class.
 * </p>
 *
 * @author Vladimir Djurovic
 */
public class CopyActionTest {

    private CopyAction copyAction;

    @Before
    public void setUp() {
        copyAction = new CopyAction();
    }

    /**
     * <p>
     *  Verifies correct behavior of {@link CopyAction#actionPerformed(java.awt.event.ActionEvent) } method. When invoked,
     * this method will place available data on clipboard.
     * </p>
     * <p>
     *  Steps taken in this test:
     * </p>
     * <p>
     *  <ul>
     *      <li>create mock data for clipboard</li>
     *      <li>place transfer data to model</li>
     *      <li>Invoke the method and verify that data is available in clipboard</li>
     *  </ul>
     * </p>
     */
    @Test
    public void testCopyData() throws Exception {
        ClipboardData data = Mockito.mock(ClipboardData.class);
        DataFlavor flavor = Mockito.mock(DataFlavor.class);
        Mockito.when(data.getTransferData(flavor)).thenReturn("mock data");
        Field field = copyAction.getClass().getDeclaredField("clipboardData");
        field.setAccessible(true);
        field.set(copyAction, data);
        field.setAccessible(false);
        copyAction.actionPerformed(Mockito.mock(ActionEvent.class));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertNotNull("Clipboard content is null", clipboard.getContents(new Object()));
        assertEquals("Invalid clipboard content data type", "mock data", clipboard.getContents(this).getTransferData(flavor));
    }
}
