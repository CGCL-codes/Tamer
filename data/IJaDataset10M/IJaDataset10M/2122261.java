package ch.ethz.mxquery.test.dmcq;

import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class StartTests extends XQueryTestBase {

    public void testStartOneStart1() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(2);
        assertEquals("<seq>3</seq><seq>4</seq><seq>5</seq><seq>6</seq><seq>7</seq><seq>8</seq>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStart2() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(4);
        assertEquals("<q4>3.5</q4><q4>4.5</q4><q4>5.5</q4><q4>6.5</q4><q4>7.5</q4>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStart3() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(4);
        assertEquals("<q4>5.5</q4><q4>6.5</q4><q4>7.5</q4>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartTwoStarts() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item2);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(2);
        assertEquals("<seq>3</seq><seq>4</seq><seq>5</seq><seq>6</seq><seq>7</seq><seq>8</seq>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartFor1() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(7);
        assertEquals("<item><seq>3</seq><seq>4</seq></item><item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartFor3() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(7);
        assertEquals("<item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartFor4() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.injectStartElement();
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(7);
        assertEquals("", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartFor2() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(7);
        assertEquals("<item><seq>1</seq><seq>2</seq></item><item><seq>3</seq><seq>4</seq></item><item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartForseq1() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(8);
        assertEquals("<item><seq>3</seq><seq>4</seq></item><item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartForseq3() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(8);
        assertEquals("<item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartForseq4() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.injectStartElement();
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(8);
        assertEquals("", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }

    public void testStartOneStartForseq2() throws Exception {
        TestPreparator tp = new TestPreparator();
        tp.pop.injectStartElement();
        tp.pop.addSequence(tp.item1);
        tp.pop.addSequence(tp.item2);
        tp.pop.addSequence(tp.item3);
        tp.pop.addSequence(tp.item4);
        tp.pop.closeStream();
        tp.q.setInitialActiveVersion(8);
        assertEquals("<item><seq>1</seq><seq>2</seq></item><item><seq>3</seq><seq>4</seq></item><item><seq>5</seq><seq>6</seq></item><item><seq>7</seq><seq>8</seq></item>", tp.ip.eventsToXML(tp.q.getDataIterator()));
    }
}
