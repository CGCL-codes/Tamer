package geovista.touchgraph;

import junit.framework.TestCase;
import geovista.geoviz.Exerciser;

public class LinkGraphTest extends TestCase {

    public void testTouchGraph() {
        LinkGraph comp = new LinkGraph();
        Exerciser exer = new Exerciser();
        exer.testGUIAndEvents(comp);
    }
}
