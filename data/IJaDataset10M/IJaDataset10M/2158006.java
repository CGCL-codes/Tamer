package org.crap4j.crap4jeclipse.views;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class Crap4jViewAgitarTest extends AgitarTestCase {

    public Class getTargetClass() {
        return Crap4jView.class;
    }

    public void testNameSorterConstructor() throws Throwable {
        Crap4jView.NameSorter nameSorter = new Crap4jView().new NameSorter();
        assertNotNull("nameSorter.comparator", getPrivateField(nameSorter, "comparator"));
        assertEquals("nameSorter.getCollator().getDecomposition()", 0, nameSorter.getCollator().getDecomposition());
    }

    public void testViewContentProviderConstructor() throws Throwable {
        new Crap4jView().new ViewContentProvider();
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testViewLabelProviderConstructor() throws Throwable {
        Crap4jView.ViewLabelProvider viewLabelProvider = new Crap4jView().new ViewLabelProvider();
        assertNull("viewLabelProvider.listenerList", getPrivateField(viewLabelProvider, "listenerList"));
    }

    public void testSetFocusWithAggressiveMocks() throws Throwable {
        Crap4jView crap4jView = (Crap4jView) Mockingbird.getProxyObject(Crap4jView.class, true);
        TableViewer tableViewer = (TableViewer) Mockingbird.getProxyObject(TableViewer.class);
        Table table = (Table) Mockingbird.getProxyObject(Table.class);
        setPrivateField(crap4jView, "viewer", tableViewer);
        setPrivateField(tableViewer, "table", table);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, table, "setFocus", "()boolean", Boolean.FALSE, 1);
        Mockingbird.enterTestMode(Crap4jView.class);
        crap4jView.setFocus();
        assertNotNull("crap4jView.viewer.getTable()", ((TableViewer) getPrivateField(crap4jView, "viewer")).getTable());
    }

    public void testShowMessageWithAggressiveMocks() throws Throwable {
        Crap4jView crap4jView = (Crap4jView) Mockingbird.getProxyObject(Crap4jView.class, true);
        TableViewer tableViewer = (TableViewer) Mockingbird.getProxyObject(TableViewer.class);
        Table table = (Table) Mockingbird.getProxyObject(Table.class);
        setPrivateField(crap4jView, "viewer", tableViewer);
        setPrivateField(tableViewer, "table", table);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, table, "getShell", "()org.eclipse.swt.widgets.Shell", null, 1);
        MessageDialog.openInformation(null, "Crap4j View", "");
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.enterTestMode(Crap4jView.class);
        callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "showMessage", new Class[] { String.class }, crap4jView, new Object[] { "" });
        assertNotNull("crap4jView.viewer.getTable()", ((TableViewer) getPrivateField(crap4jView, "viewer")).getTable());
    }

    public void testViewContentProviderDispose() throws Throwable {
        new Crap4jView().new ViewContentProvider().dispose();
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testViewContentProviderGetElements() throws Throwable {
        String[] result = (String[]) new Crap4jView().new ViewContentProvider().getElements("");
        assertEquals("result.length", 3, result.length);
        assertEquals("(String[]) result[0]", "One", ((String[]) result)[0]);
    }

    public void testViewContentProviderInputChangedWithAggressiveMocks() throws Throwable {
        Crap4jView.ViewContentProvider viewContentProvider = (Crap4jView.ViewContentProvider) Mockingbird.getProxyObject(Crap4jView.ViewContentProvider.class, true);
        Mockingbird.enterTestMode(Crap4jView.ViewContentProvider.class);
        viewContentProvider.inputChanged(null, null, null);
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testViewLabelProviderGetColumnImageWithAggressiveMocks() throws Throwable {
        Crap4jView.ViewLabelProvider viewLabelProvider = (Crap4jView.ViewLabelProvider) Mockingbird.getProxyObject(Crap4jView.ViewLabelProvider.class, true);
        IWorkbench iWorkbench = (IWorkbench) Mockingbird.getProxyObject(IWorkbench.class);
        ISharedImages iSharedImages = (ISharedImages) Mockingbird.getProxyObject(ISharedImages.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(PlatformUI.getWorkbench(), iWorkbench);
        Mockingbird.setReturnValue(iWorkbench.getSharedImages(), iSharedImages);
        Mockingbird.setReturnValue(iSharedImages.getImage("IMG_OBJ_ELEMENTS"), null);
        Mockingbird.enterTestMode(Crap4jView.ViewLabelProvider.class);
        Image result = viewLabelProvider.getColumnImage(null, 0);
        assertNull("result", result);
    }

    public void testViewLabelProviderGetColumnText() throws Throwable {
        String result = new Crap4jView().new ViewLabelProvider().getColumnText("Gull", 100);
        assertEquals("result", "Gull", result);
    }

    public void testContributeToActionBarsThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "contributeToActionBars", new Class[] {}, crap4jView, new Object[] {});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Crap4jView.class, ex);
        }
    }

    public void testCreatePartControlThrowsSWTException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        Composite parent = (Composite) callPrivateMethod("org.eclipse.swt.widgets.Canvas", "<init>", new Class[] {}, null, new Object[] {});
        try {
            crap4jView.createPartControl(parent);
            fail("Expected SWTException to be thrown");
        } catch (SWTException ex) {
            assertEquals("ex.getMessage()", "Widget is disposed", ex.getMessage());
            assertThrownBy(SWT.class, ex);
            assertEquals("ex.code", 24, ex.code);
            assertNull("ex.throwable", ex.throwable);
            assertNull("crap4jView.viewer", getPrivateField(crap4jView, "viewer"));
            assertNull("crap4jView.action2", getPrivateField(crap4jView, "action2"));
            assertNull("crap4jView.doubleClickAction", getPrivateField(crap4jView, "doubleClickAction"));
            assertNull("crap4jView.action1", getPrivateField(crap4jView, "action1"));
        }
    }

    public void testFillLocalToolBarThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        IToolBarManager toolBarManager = new ToolBarManager(100);
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "fillLocalToolBar", new Class[] { IToolBarManager.class }, crap4jView, new Object[] { toolBarManager });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ActionContributionItem.class, ex);
            boolean actual = ((Boolean) callPrivateMethod("org.eclipse.jface.action.ToolBarManager", "toolBarExist", new Class[] {}, toolBarManager, new Object[] {})).booleanValue();
            assertFalse("(ToolBarManager) toolBarManager.toolBarExist()", actual);
        }
    }

    public void testHookContextMenuThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "hookContextMenu", new Class[] {}, crap4jView, new Object[] {});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Crap4jView.class, ex);
            assertNull("crap4jView.viewer", getPrivateField(crap4jView, "viewer"));
        }
    }

    public void testHookDoubleClickActionThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "hookDoubleClickAction", new Class[] {}, crap4jView, new Object[] {});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Crap4jView.class, ex);
            assertNull("crap4jView.viewer", getPrivateField(crap4jView, "viewer"));
        }
    }

    public void testMakeActionsThrowsIllegalStateException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "makeActions", new Class[] {}, crap4jView, new Object[] {});
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("crap4jView.action1.getStyle()", 1, ((Action) getPrivateField(crap4jView, "action1")).getStyle());
            assertEquals("ex.getMessage()", "Workbench has not been created yet.", ex.getMessage());
            assertThrownBy(PlatformUI.class, ex);
            assertNull("crap4jView.action2", getPrivateField(crap4jView, "action2"));
            assertNull("crap4jView.doubleClickAction", getPrivateField(crap4jView, "doubleClickAction"));
        }
    }

    public void testSetFocusThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            crap4jView.setFocus();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Crap4jView.class, ex);
            assertNull("crap4jView.viewer", getPrivateField(crap4jView, "viewer"));
        }
    }

    public void testShowMessageThrowsNullPointerException() throws Throwable {
        Crap4jView crap4jView = new Crap4jView();
        try {
            callPrivateMethod("org.crap4j.crap4jeclipse.views.Crap4jView", "showMessage", new Class[] { String.class }, crap4jView, new Object[] { "testCrap4jViewMessage" });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Crap4jView.class, ex);
            assertNull("crap4jView.viewer", getPrivateField(crap4jView, "viewer"));
        }
    }

    public void testViewLabelProviderGetColumnImageThrowsIllegalStateException() throws Throwable {
        try {
            new Crap4jView().new ViewLabelProvider().getColumnImage(new Integer(100), 100);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("ex.getMessage()", "Workbench has not been created yet.", ex.getMessage());
            assertThrownBy(PlatformUI.class, ex);
        }
    }

    public void testViewLabelProviderGetImageThrowsIllegalStateException() throws Throwable {
        try {
            new Crap4jView().new ViewLabelProvider().getImage("t");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("ex.getMessage()", "Workbench has not been created yet.", ex.getMessage());
            assertThrownBy(PlatformUI.class, ex);
        }
    }
}
