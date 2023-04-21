package com.io_software.catools.search.test;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import com.io_software.catools.search.*;
import com.io_software.catools.search.index.*;
import com.io_software.catools.search.index.IndexableSearchable;
import com.io_software.catools.search.capability.Production;
import java.util.Set;
import java.util.Date;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.MarshalledObject;
import java.rmi.activation.ActivationID;

/** The class {@link RemoteSearchableImpl} uses an instance of {@link
    RemoteImplementationClassSelector} or a subclass thereof to map a passed
    {@link Searchable} instance to a suitable RMI implementation class that is
    a subclass of or identical with {@link RemoteSearchableImpl}. The mapping
    implemented in the default class is based on a naming convention. This
    mapping is tested by this test case.

    @author Axel Uhl
    @version $Id: TestRemoteImplClassMapping.java,v 1.3 2002/04/22 10:51:16 uhl Exp $
*/
public class TestRemoteImplClassMapping extends TestCase {

    /** forwards to the superclass constructor

	@param name the selector telling the method to be executed in
	this test case
    */
    public TestRemoteImplClassMapping(String name) {
        super(name);
    }

    /** Creates an {@link IndexSearchable} object and checks that it will
	receive a {@link IndexRemoteSearchableImpl} implementation object.
      */
    public void testIndexSearchable() throws Throwable {
        RemoteImplementationClassSelector selector = new RemoteImplementationClassSelector();
        Searchable s = new TestIndexableSearchable();
        String className = selector.selectRemoteImplementationClass(s);
        assertEquals("com.io_software.catools.search.index.IndexableRemoteSearchableImpl", className);
    }

    /** makes sure that even if a {@link Searchable} subinterface is
	implemented, but it doesn't have a corresponding remote class
	structure, that then {@link RemoteSearchableImpl} is used as default.
    */
    public void testDefault() throws Throwable {
        RemoteImplementationClassSelector selector = new RemoteImplementationClassSelector();
        Searchable s = new X();
        String className = selector.selectRemoteImplementationClass(s);
        assertEquals("com.io_software.catools.search.RemoteSearchableImpl", className);
    }

    /** Creates a {@link Y} object and checks that it will
	receive a {@link IndexRemoteSearchableImpl} implementation object. The
	tricky part is that {@link Y} implements two {@link Searchable}
	subinterfaces, but only one has a corresponding remote class
	infrastructure.
      */
    public void testDoubleSearchable() throws Throwable {
        RemoteImplementationClassSelector selector = new RemoteImplementationClassSelector();
        Searchable s = new Y();
        String className = selector.selectRemoteImplementationClass(s);
        assertEquals("com.io_software.catools.search.index.IndexableRemoteSearchableImpl", className);
    }

    /** Creates a {@link Z} object and checks that an exception will occur when
	trying to retrieve the corresponding remote implementation class. This
	is because {@link Z} implements two {@link Searchable} subinterfaces
	which both have a corresponding remote class infrastructure.
      */
    public void testAmbiguouslyRemoteSearchable() throws Throwable {
        RemoteImplementationClassSelector selector = new RemoteImplementationClassSelector();
        Searchable s = new Z();
        try {
            String className = selector.selectRemoteImplementationClass(s);
            fail("Expected exception because of ambiguous Searchables");
        } catch (ClassCastException cce) {
        }
    }

    /** Creates a {@link Q} object which implements two {@link Searchable}
	subinterfaces ({@link QSearchable} and {@link ZSearchable}) that in
	turn have an inheritance relation and both have a remote class
	structure. It is assumed that the most derived remote implementation
	class, in this case {@link QSearchable}, results.
      */
    public void testMultiLevelInheritance() throws Throwable {
        RemoteImplementationClassSelector selector = new RemoteImplementationClassSelector();
        Searchable s = new Q();
        String className = selector.selectRemoteImplementationClass(s);
        assertEquals("com.io_software.catools.search.test.TestRemoteImplClassMapping$QRemoteSearchableImpl", className);
    }

    /** inner class that implements the {@link IndexableSearchable} interface
	and is used for testing only. All method implementations are empty.

	@author Axel Uhl
	@version $Id: TestRemoteImplClassMapping.java,v 1.3 2002/04/22 10:51:16 uhl Exp $
    */
    public static class TestIndexableSearchable extends AbstractSearchable implements IndexableSearchable {

        public Production getSupportedQueryTypes() {
            return null;
        }

        public Set search(Query q, Requestor r) {
            return null;
        }

        public IndexDataUpdate getIndexDataUpdate(Date changesSince) {
            return null;
        }

        public IndexData getIndexData() {
            return null;
        }
    }

    /** test interface for which no remote structure exists */
    public static interface TestSearchable extends Searchable {
    }

    /** does only implement a {@link Searchable} subinterface that doesn't
	provide a corresponding remote class structure. Thus the corresponding
	remote implementation class should default to {@link
	RemoteSearchableImpl}.
    */
    public static class X extends AbstractSearchable implements TestSearchable {

        public Production getSupportedQueryTypes() {
            return null;
        }

        public Set search(Query q, Requestor r) {
            return null;
        }
    }

    /** This inner class implements two different {@link Searchable}
	subinterfaces, namely {@link TestSearchable} and {@link
	IndexableSearchable}. But only one has a corresponding remote class
	strukture ({@link IndexableSearchable}). Therefore, the corresponding
	remote implementation class should be {@link
	IndexableRemoteSearchableImpl}.
    */
    public static class Y extends TestIndexableSearchable implements TestSearchable {
    }

    /** Implements two {@link Searchable} subinterfaces that both have a remote
	class infrastructure but that don't have a inheritance
	relationship. This should lead to an exception when trying to find the
	corresponding remote implementation class.
    */
    public static class Z extends Y implements ZSearchable {
    }

    public static interface ZSearchable extends Searchable {
    }

    /** remote interface for {@link ZSearchable} */
    public static interface ZRemoteSearchable extends RemoteSearchable, ZSearchable {
    }

    /** remote implementation class for {@link ZSearchable} */
    public static class ZRemoteSearchableImpl extends RemoteSearchableImpl implements ZRemoteSearchable {

        public ZRemoteSearchableImpl(ActivationID id, MarshalledObject data) throws RemoteException, IOException, ClassNotFoundException {
            super(id, data);
        }
    }

    /** A subinterface to {@link ZSearchable} that also defines a corresponding
	remote class structure. It is assumed that the mapping algorithm will
	pick the corresponding implementation subclass and not the {@link
	ZRemoteSearchableImpl} class.
    */
    public static interface QSearchable extends ZSearchable {
    }

    /** remote interface for {@link ZSearchable} */
    public static interface QRemoteSearchable extends ZRemoteSearchable, QSearchable {
    }

    /** remote implementation class for {@link ZSearchable} */
    public static class QRemoteSearchableImpl extends ZRemoteSearchableImpl implements QRemoteSearchable {

        public QRemoteSearchableImpl(ActivationID id, MarshalledObject data) throws RemoteException, IOException, ClassNotFoundException {
            super(id, data);
        }
    }

    /** searchable class that implement the {@link QSearchable} interface that
	in turn is a subinterface of {@link ZSearchable}. It is assumed that
	for instances of this class the corresponding {@link
	QRemoteSearchableImpl} implementation class will be chosen.
    */
    public static class Q extends AbstractSearchable implements QSearchable {

        public Production getSupportedQueryTypes() {
            return null;
        }

        public Set search(Query q, Requestor r) {
            return null;
        }
    }
}
