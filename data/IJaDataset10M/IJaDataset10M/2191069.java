package de.sonivis.tool.core.tests.datamodel;

import junit.framework.TestCase;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.ContextRelation;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;
import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;
import de.sonivis.tool.core.datamodel.proxy.IContextRelation;
import de.sonivis.tool.core.tests.datamodel.hibernate.ContentElementTestImpl;
import de.sonivis.tool.core.tests.datamodel.hibernate.ContextRelationTestImpl;

/**
 * Test case for {@link ContextRelation} class of the SONIVIS:Data Model.
 * <p>
 * The class tests the public methods of the {@link ContextRelation} class. The abstract
 * {@link ContextRelation} is realized by an instance of {@link RevisionOfPage} in all the tests.
 * </p>
 * 
 * @author Andreas Erber
 * @version $Revision: 1626 $, $Date: 2010-04-07 15:28:53 -0400 (Wed, 07 Apr 2010) $
 */
public class TestContextRelation extends TestCase {

    /**
	 * A test {@link InfoSpace} for the {@link ContextRelation} to exist in.
	 */
    private InfoSpace infoSpace = null;

    /**
	 * An example {@link ContentElement}.
	 */
    private ContentElement ce1 = null;

    /**
	 * An example {@link ContentElement}
	 */
    private ContentElement ce2 = null;

    /**
	 * Representative of the class under test.
	 */
    private ContextRelation<? extends IContentElement, ? extends IContentElement> ctx = null;

    /**
	 * {@inheritDoc}
	 * 
	 * @see TestCase#setUp()
	 */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        this.infoSpace = new InfoSpace("TestContextRelation", TestContextRelation.class.getCanonicalName());
        this.ce1 = new RevisionElement(this.infoSpace, null, "Some Rev Title", "Some ContentElement", null);
        this.ce2 = new ContentElementTestImpl(this.infoSpace, "Some other ContentElement", null);
        this.ctx = new ContextRelationTestImpl<IContentElement, IContentElement>(this.infoSpace, this.ce1, this.ce2);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 *             in case {@link TestCase#tearDown()} does.
	 * 
	 * @see jTestCase#tearDown()
	 */
    @Override
    protected final void tearDown() throws Exception {
        this.ctx = null;
        this.ce2 = null;
        this.ce1 = null;
        this.infoSpace = null;
        super.tearDown();
    }

    /**
	 * Test method for {@link ContextRelation#hashCode()}.
	 */
    public final void testHashCode() {
        final ContextRelation<? extends IContentElement, ? extends IContentElement> other = new ContextRelationTestImpl<IContentElement, IContentElement>(this.infoSpace, this.ce1, this.ce2);
        assertEquals("Found non-equal hashCodes for Contexts created from same arguments.", other.hashCode(), this.ctx.hashCode());
    }

    /**
	 * Test method for {@link ContextRelation#equals(Object)}.
	 */
    public final void testEqualsObject() {
        final IContextRelation<? extends IContentElement, ? extends IContentElement> other = new ContextRelationTestImpl<IContentElement, IContentElement>(this.infoSpace, this.ce1, this.ce2);
        assertTrue(this.ctx.equals(other));
    }

    /**
	 * Test method for {@link ContextRelation#getSource()}.
	 */
    public final void testGetSource() {
        final IContentElement retrievedCE = this.ctx.getSource();
        assertEquals(this.ce1, retrievedCE);
    }

    /**
	 * Test method for {@link ContextRelation#getTarget()}.
	 */
    public final void testGetTarget() {
        final IContentElement retrievedCE = this.ctx.getTarget();
        assertEquals(this.ce2, retrievedCE);
    }

    /**
	 * Test method for {@link ContextRelation#getType()}.
	 */
    public void testGetType() {
        assertEquals(ContextRelation.class, this.ctx.getType());
    }

    /**
	 * Testing constructors for exceptions.
	 */
    public final void testForException() {
        @SuppressWarnings("unused") ContextRelation<? extends IContentElement, ? extends IContentElement> ctx = null;
        try {
            ctx = new ContextRelationTestImpl<IContentElement, IContentElement>(this.infoSpace, this.ce1, null);
            fail("Expected DataModelInstantiationException to be raised on target argument being null.");
        } catch (final DataModelInstantiationException dmie) {
            ctx = null;
        }
        try {
            ctx = new ContextRelationTestImpl<IContentElement, IContentElement>(this.infoSpace, null, this.ce2);
            fail("Expected DataModelInstantiationException to be raised on source argument being null.");
        } catch (final DataModelInstantiationException dmie) {
            ctx = null;
        }
    }
}
