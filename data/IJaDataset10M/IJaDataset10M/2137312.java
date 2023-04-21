package org.crazydays.gameplan;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.*;
import static org.junit.Assert.*;
import org.crazydays.gameplan.controller.ActionController;
import org.crazydays.gameplan.db.swing.DatabaseTreeMouseListener;
import org.crazydays.gameplan.db.swing.JDatabaseTreePane;
import org.crazydays.gameplan.map.swing.CreateMapDialogFactory;
import org.crazydays.gameplan.map.swing.JMapContextMenu;
import org.crazydays.gameplan.map.swing.MapFrameFactory;
import org.crazydays.gameplan.map.swing.MapMouseListener;
import org.crazydays.gameplan.model.Model;
import org.crazydays.gameplan.view.EditMenu;
import org.crazydays.gameplan.view.FileMenu;
import org.crazydays.gameplan.view.HelpMenu;
import org.crazydays.gameplan.view.View;
import org.crazydays.gameplan.view.ViewMenu;
import org.crazydays.gameplan.view.paint.JControlsPanel;
import org.crazydays.gameplan.view.paint.JPaintPane;
import org.crazydays.gameplan.view.paint.action.SelectPaintBrush;

/**
 * SpringFrameworkUnitTest
 */
public class SpringFrameworkUnitTest {

    /** context */
    protected ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "gameplan.ioc.xml" });

    /**
     * Test Model.
     */
    @Test
    public void testModel() {
        Model model = context.getBean("model", Model.class);
        assertNotNull("model", model);
    }

    /**
     * Test View.
     */
    @Test
    public void testView() {
        View view = context.getBean("view", View.class);
        assertNotNull("view", view);
        assertNotNull("view.menuBar", view.getJMenuBar());
    }

    /**
     * Test FileMenu.
     */
    @Test
    public void testFileMenu() {
        FileMenu component = context.getBean("view.fileMenu", FileMenu.class);
        assertNotNull("component == null", component);
        assertEquals("count", 10, component.getMenuComponentCount());
    }

    /**
     * Test EditMenu.
     */
    @Test
    public void testEditMenu() {
        EditMenu component = context.getBean("view.editMenu", EditMenu.class);
        assertNotNull("component == null", component);
        assertEquals("count", 0, component.getMenuComponentCount());
    }

    /**
     * Test ViewMenu.
     */
    @Test
    public void testViewMenu() {
        ViewMenu component = context.getBean("view.viewMenu", ViewMenu.class);
        assertNotNull("component == null", component);
        assertEquals("count", 1, component.getMenuComponentCount());
    }

    /**
     * Test HelpMenu.
     */
    @Test
    public void testHelpMenu() {
        HelpMenu component = context.getBean("view.helpMenu", HelpMenu.class);
        assertNotNull("component == null", component);
        assertEquals("count", 1, component.getMenuComponentCount());
    }

    /**
     * Test DatabaseTreePane.
     */
    @Test
    public void testDatabaseTreePane() {
        JDatabaseTreePane component = context.getBean("view.databaseTreePane", JDatabaseTreePane.class);
        assertNotNull("component == null", component);
    }

    /**
     * Test PaintPane.
     */
    @Test
    public void testPaintPane() {
        JPaintPane component = context.getBean("view.paintPane", JPaintPane.class);
        assertNotNull("component == null", component);
    }

    /**
     * Test ControlsPanel.
     */
    @Test
    public void testControlsPanel() {
        JControlsPanel component = context.getBean("view.controlsPanel", JControlsPanel.class);
        assertNotNull("component == null", component);
    }

    /**
     * Test DatabaseTreeMouseListener.
     */
    @Test
    public void testDatabaseTreeMouseListener() {
        DatabaseTreeMouseListener listener = context.getBean("view.databaseTreeMouseListener", DatabaseTreeMouseListener.class);
        assertNotNull("listener == null", listener);
    }

    /**
     * Test MapFrameFactory.
     */
    @Test
    public void testMapFrameFactory() {
        MapFrameFactory factory = context.getBean("view.mapFrameFactory", MapFrameFactory.class);
        assertNotNull("factory == null", factory);
    }

    /**
     * Test MapMouseListener.
     */
    @Test
    public void testMapMouseListener() {
        MapMouseListener listener = context.getBean("view.mapMouseListener", MapMouseListener.class);
        assertNotNull("listener == null", listener);
    }

    /**
     * Test MapContextMenu.
     */
    @Test
    public void testMapContextMenu() {
        JMapContextMenu component = context.getBean("view.mapContextMenu", JMapContextMenu.class);
        assertNotNull("component == null", component);
    }

    /**
     * Test CreateMapDialogFactory.
     */
    @Test
    public void testCreateMapDialogFactory() {
        CreateMapDialogFactory factory = context.getBean("view.createMapDialogFactory", CreateMapDialogFactory.class);
        assertNotNull("factory == null", factory);
    }

    /**
     * Test ActionController.
     */
    @Test
    public void testActionController() {
        ActionController actionController = context.getBean("actionController", ActionController.class);
        assertNotNull("actionController", actionController);
    }

    /**
     * Test SelectPaintBrush.
     */
    @Test
    public void testSelectPaintBrush() {
        SelectPaintBrush action = context.getBean("action.selectPaintBrush", SelectPaintBrush.class);
        assertNotNull("action", action);
    }
}
