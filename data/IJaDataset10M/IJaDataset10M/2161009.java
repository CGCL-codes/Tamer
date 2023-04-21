package org.kommando.core;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import java.util.Arrays;
import javax.swing.text.JTextComponent;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.kommando.core.KommandoWindow.Selector;
import org.kommando.core.action.Action;
import org.kommando.core.action.ActionRepository;
import org.kommando.core.action.DefaultActionRepository;
import org.kommando.core.catalog.Catalog;
import org.kommando.core.catalog.DefaultCatalog;
import org.kommando.core.catalog.ObjectSource;
import org.kommando.core.selector.CommandSelector;
import org.kommando.core.support.TestAction;
import org.kommando.core.support.TestObject;
import org.kommando.core.support.TestObjectSource;

/**
 * {@link TestCase} for the {@link CommandSelector}
 * 
 * @author Peter De Bruycker
 */
public class CommandSelectorTest {

    private Catalog catalog;

    private ActionRepository actionRepository;

    @Before
    public void createObjectsAndActions() {
        catalog = new DefaultCatalog(Arrays.<ObjectSource>asList(new TestObjectSource(new TestObject("object1"), new TestObject("object2"))));
        actionRepository = new DefaultActionRepository(Arrays.<Action>asList(new TestAction("action1"), new TestAction("action2")));
    }

    @Test
    public void setWindowAndShow() {
        HistoryTracker historyTracker = new HistoryTracker();
        CommandSelector commandSelector = new CommandSelector(catalog, actionRepository, historyTracker);
        KommandoWindow window = createMock(KommandoWindow.class);
        window.setTextComponent(eq(Selector.DIRECT), (JTextComponent) anyObject());
        window.setTextComponent(eq(Selector.INDIRECT), (JTextComponent) anyObject());
        window.shrink();
        window.setSelection(Selector.DIRECT, null);
        window.setSelection(Selector.ACTION, null);
        window.setSelection(Selector.INDIRECT, null);
        window.setActiveSelector(Selector.DIRECT);
        window.show();
        window.dispose();
        replay(window);
        commandSelector.setWindow(window);
        commandSelector.show(true);
        KommandoWindow window2 = createMock(KommandoWindow.class);
        window2.setTextComponent(eq(Selector.DIRECT), (JTextComponent) anyObject());
        window2.setTextComponent(eq(Selector.INDIRECT), (JTextComponent) anyObject());
        window2.shrink();
        replay(window2);
        commandSelector.setWindow(window2);
        verify(window, window2);
    }
}
