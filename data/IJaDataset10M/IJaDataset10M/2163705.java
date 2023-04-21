package org.jmlspecs.samples.list.list3;

import org.jmlspecs.samples.list.node.OneWayNode;

/** Supply test data for the JML and JUnit based testing of 
 *  OneWayList.
 *
 *  <p>Test data is supplied by overriding methods in this class.  See
 *  the JML documentation and the comments below about how to do this.
 *
 *  <p>This class is also the place to override the <kbd>setUp()</kbd>
 *  and <kbd>tearDown()</kbd> methods if your testing needs some
 *  actions to be taken before and after each test is executed.
 *
 *  <p>This class is never rewritten by jmlunit.
 *
 */
public abstract class OneWayList_JML_TestData extends junit.framework.TestCase {

    /** Initialize this class. */
    public OneWayList_JML_TestData(java.lang.String name) {
        super(name);
    }

    public junit.framework.TestSuite overallTestSuite() {
        return new junit.framework.TestSuite("Overall tests for OneWayList");
    }

    public junit.framework.TestSuite emptyTestSuiteFor(java.lang.String methodName) {
        return new junit.framework.TestSuite(methodName);
    }

    protected org.jmlspecs.jmlunit.strategies.IndefiniteIterator vorg_jmlspecs_samples_list_list3_OneWayListIter(java.lang.String methodName, int loopsThisSurrounds) {
        return vorg_jmlspecs_samples_list_list3_OneWayListStrategy.iterator();
    }

    /** The strategy for generating test data of type
     * org.jmlspecs.samples.list.list3.OneWayList. */
    private org.jmlspecs.jmlunit.strategies.StrategyType vorg_jmlspecs_samples_list_list3_OneWayListStrategy = new org.jmlspecs.jmlunit.strategies.CloneableObjectAbstractStrategy() {

        protected java.lang.Object[] addData() {
            org.jmlspecs.samples.list.list3.OneWayList[] receivers = new org.jmlspecs.samples.list.list3.OneWayList[] { new OneWayList(), new OneWayList(), new OneWayList(), new OneWayList(), new OneWayList(), new OneWayList(), new OneWayList(), new OneWayList() };
            receivers[1].insertBeforeCursor("it");
            receivers[1].firstEntry();
            receivers[2].insertBeforeCursor("first");
            receivers[2].firstEntry();
            receivers[2].insertAfterCursor("second");
            receivers[2].incrementCursor();
            receivers[2].incrementCursor();
            receivers[3].insertBeforeCursor("first");
            receivers[3].insertBeforeCursor("second");
            receivers[3].insertBeforeCursor("third");
            receivers[3].insertBeforeCursor("fourth");
            receivers[3].firstEntry();
            receivers[3].incrementCursor();
            receivers[3].incrementCursor();
            receivers[4].insertBeforeCursor(new int[1]);
            receivers[4].insertBeforeCursor(new int[2]);
            receivers[4].insertBeforeCursor(new int[3]);
            receivers[4].insertBeforeCursor(new int[4]);
            receivers[4].firstEntry();
            receivers[5].insertBeforeCursor("it1");
            receivers[6].insertBeforeCursor("it2");
            return receivers;
        }

        protected Object cloneElement(java.lang.Object o$) {
            org.jmlspecs.samples.list.list3.OneWayList down$ = (org.jmlspecs.samples.list.list3.OneWayList) o$;
            return down$.clone();
        }
    };

    protected org.jmlspecs.jmlunit.strategies.IndefiniteIterator vjava_lang_ObjectIter(java.lang.String methodName, int loopsThisSurrounds) {
        return vjava_lang_ObjectStrategy.iterator();
    }

    /** The strategy for generating test data of type
     * java.lang.Object. */
    private org.jmlspecs.jmlunit.strategies.StrategyType vjava_lang_ObjectStrategy = new org.jmlspecs.jmlunit.strategies.CompositeStrategy(new org.jmlspecs.jmlunit.strategies.StrategyType[] { new org.jmlspecs.jmlunit.strategies.ObjectStrategy(), vorg_jmlspecs_samples_list_list3_OneWayListStrategy });
}
