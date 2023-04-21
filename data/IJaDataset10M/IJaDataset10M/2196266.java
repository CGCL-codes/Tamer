package influx.dtc.test.data.impl;

import influx.dtc.collection.IEntityKeyListDTC;
import influx.dtc.collection.IKeyListCompositeDTC;
import influx.dtc.event.impl.AbstractDTCEvent;
import influx.dtc.impl.EntityDTC;
import influx.dtc.listener.IDTCListener;
import influx.dtc.phase.impl.DTCPhase;
import influx.dtc.test.data.impl.EntityTestData.TestEntityWithAbstractEntityExtension;
import influx.model.IAlias;
import influx.model.impl.AbstractAlias;
import java.lang.ref.WeakReference;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Test data class that provides data to all test classes
 * 
 * @author sapatel
 */
public final class DTCTestData {

    private static Logger LOG = new WeakReference<Logger>(LoggerFactory.getLogger(DTCTestData.class)).get();

    /**
	 * private constructor
	 */
    private DTCTestData() {
    }

    /**
	 * TestListener
	 */
    public static final class TestListener implements IDTCListener<EntityDTC<TestEntityWithAbstractEntityExtension, TestEntityWithAbstractEntityExtension>, DTCPhase> {

        /**
		 * process test
		 * 
		 * @param event
		 *            the event
		 */
        public final void processTest(final TestEvent event) {
            LOG.info("PROCESSING EVENT... ");
        }
    }

    /**
	 * Test composite listener
	 */
    public static final class TestCompositeListener implements IDTCListener<IKeyListCompositeDTC<String>, DTCPhase> {

        public static final String SEARCH_DTC_TEST_NAME = "sEaRchDtC_nAmE";

        /**
		 * process test
		 * 
		 * @param event
		 *            the event
		 */
        public final void processTest(final TestCompositeEvent event) {
            LOG.info("PROCESSING EVENT... ");
            final IKeyListCompositeDTC<String> cDTC = event.getSource();
            final List<IEntityKeyListDTC<String, TestEntityWithAbstractEntityExtension>> tpDTCs = cDTC.getKeyListDTCsByClassTypes(String.class, TestEntityWithAbstractEntityExtension.class);
            final IEntityKeyListDTC<String, TestEntityWithAbstractEntityExtension> tpDTC = cDTC.getKeyListDTCByName(SEARCH_DTC_TEST_NAME);
            Assert.assertNotNull(tpDTCs);
            Assert.assertFalse(tpDTCs.isEmpty());
            Assert.assertNotNull(tpDTC);
        }
    }

    /**
	 * TestEvent
	 */
    public static final class TestEvent extends AbstractDTCEvent<EntityDTC<TestEntityWithAbstractEntityExtension, TestEntityWithAbstractEntityExtension>, DTCPhase> {

        private static final long serialVersionUID = 1L;

        /**
		 * TestEvent
		 * 
		 * @param source
		 *            source
		 * @param phase
		 *            phase
		 * @param queueEvent
		 *            queueEvent
		 */
        public TestEvent(final EntityDTC<TestEntityWithAbstractEntityExtension, TestEntityWithAbstractEntityExtension> source, final DTCPhase phase, final boolean queueEvent) {
            super(source, phase, queueEvent);
        }

        /**
		 * {@inheritDoc}
		 */
        public final boolean isAppropriateListener(final IDTCListener<?, ?> listener) {
            return true;
        }

        /**
		 * {@inheritDoc}
		 */
        public final void processIfAppropriateListener(final IDTCListener<?, ?> listener) {
            if (listener instanceof TestListener) {
                ((TestListener) listener).processTest(this);
            }
        }
    }

    /**
	 * Test composite event
	 */
    public static final class TestCompositeEvent extends AbstractDTCEvent<IKeyListCompositeDTC<String>, DTCPhase> {

        private static final long serialVersionUID = 1L;

        /**
		 * TestCompositeEvent
		 * 
		 * @param source
		 *            source
		 * @param phase
		 *            phase
		 * @param queueEvent
		 *            queueEvent
		 */
        public TestCompositeEvent(final IKeyListCompositeDTC<String> source, final DTCPhase phase, final boolean queueEvent) {
            super(source, phase, queueEvent);
        }

        /**
		 * {@inheritDoc}
		 */
        public final boolean isAppropriateListener(final IDTCListener<?, ?> listener) {
            return true;
        }

        /**
		 * {@inheritDoc}
		 */
        public final void processIfAppropriateListener(final IDTCListener<?, ?> listener) {
            if (listener instanceof TestCompositeListener) {
                ((TestCompositeListener) listener).processTest(this);
            }
        }
    }

    /**
	 * The Class TestAbstractAlias.
	 */
    @SuppressWarnings("serial")
    public static final class TestAbstractAlias extends AbstractAlias implements IAlias {

        public static final String QUALIFIED_NAME = "someQF";

        public static final String ALIAS = "someAlias";

        /**
		 * Instantiates a new test abstract alias.
		 * 
		 * @param qualifiedName
		 *            the qualified name
		 * @param aliasForQualifiedName
		 *            the alias for qualified name
		 */
        public TestAbstractAlias(final String qualifiedName, final String aliasForQualifiedName) {
            super(qualifiedName, aliasForQualifiedName);
            LOG.debug("Set qualified name: {} and alias for qualified name: {}", qualifiedName, aliasForQualifiedName);
        }

        /**
		 * Instantiates a new test abstract alias.
		 * 
		 * @param qualifiedName
		 *            the qualified name
		 */
        public TestAbstractAlias(final String qualifiedName) {
            super(qualifiedName);
            LOG.debug("Set qualified name: {}", qualifiedName);
        }
    }
}
