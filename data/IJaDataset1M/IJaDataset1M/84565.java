package com.technoetic.tornado.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import com.technoetic.tornado.ColumnMapping;
import com.technoetic.tornado.DatabaseContext;

public class TestTransientEventListener {

    private TransientEventListenerMapping eventListenerMapping;

    private ArrayList<Object> columnMappings;

    private MockObservable mockObservable;

    private MockObserver mockObserver;

    private MockObserver mockObserver2;

    private DatabaseContext mockDatabaseContext;

    private static class MockObservable {

        int id;

        String id2;

        public boolean addPropertyChangeListenerCalled;

        public PropertyChangeListener addPropertyChangeListenerListener;

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            addPropertyChangeListenerCalled = true;
            addPropertyChangeListenerListener = listener;
        }

        public boolean removePropertyChangeListenerCalled;

        public PropertyChangeListener removePropertyChangeListenerListener;

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            removePropertyChangeListenerCalled = true;
            removePropertyChangeListenerListener = listener;
        }
    }

    public static class MockObserver implements PropertyChangeListener {

        public boolean propertyChangeCalled;

        public PropertyChangeEvent propertyChangeEvent;

        public void propertyChange(PropertyChangeEvent event) {
            propertyChangeCalled = true;
            propertyChangeEvent = event;
        }
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        columnMappings = new ArrayList<Object>();
        columnMappings.add(new ColumnMapping("id", int.class, "id", java.sql.Types.INTEGER));
        mockObservable = new MockObservable();
        mockObservable.id = 1;
        mockObservable.id2 = "2";
        mockObserver = new MockObserver();
        mockObserver2 = new MockObserver();
        mockDatabaseContext = mock(DatabaseContext.class);
        stub(mockDatabaseContext.getObject(isA(Class.class), isA(Object[].class), anyBoolean())).toReturn(mockObserver);
    }

    @Test
    public void testInitializeForShared() throws Exception {
        ObserverFactory mockObserverFactory = mock(ObserverFactory.class);
        stub(mockObserverFactory.newInstance()).toReturn(mockObserver);
        eventListenerMapping = new TransientEventListenerMapping("propertyChange", mockObservable.getClass(), mockObserverFactory, true);
        eventListenerMapping.initialize(mockDatabaseContext, mockObservable, false);
        assertTrue("listener not added", mockObservable.addPropertyChangeListenerCalled);
        assertEquals("wrong listener type", MockObserver.class, mockObservable.addPropertyChangeListenerListener.getClass());
        MockObservable mockObservable2 = new MockObservable();
        eventListenerMapping.initialize(mockDatabaseContext, mockObservable2, false);
        assertTrue("listener not shared", mockObservable.addPropertyChangeListenerListener == mockObservable2.addPropertyChangeListenerListener);
    }

    public void testInitializeForNotShared() throws Exception {
        ObserverFactory mockObserverFactory = mock(ObserverFactory.class);
        stub(mockObserverFactory.newInstance()).toReturn(mockObserver);
        stub(mockObserverFactory.newInstance()).toReturn(mockObserver2);
        eventListenerMapping = new TransientEventListenerMapping("propertyChange", mockObservable.getClass(), mockObserverFactory, false);
        eventListenerMapping.initialize(mockDatabaseContext, mockObservable, false);
        assertTrue("listener not added", mockObservable.addPropertyChangeListenerCalled);
        assertEquals("wrong listener type", MockObserver.class, mockObservable.addPropertyChangeListenerListener.getClass());
        MockObservable mockObservable2 = new MockObservable();
        eventListenerMapping.initialize(mockDatabaseContext, mockObservable2, false);
        assertTrue("listener incorrectly shared", mockObservable.addPropertyChangeListenerListener != mockObservable2.addPropertyChangeListenerListener);
    }
}
