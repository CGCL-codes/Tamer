package sketch.experiment.complexdatastructure;

import binoheap.BinomialHeap;
import sketch.ounit.Observer;
import sketch.ounit.Values;
import sketch.specs.annotation.TestSketch;
import junit.framework.TestCase;

/**
 * The sketch for bino heap
 * */
public class Z_Sketch_BinoHeap extends TestCase {

    @TestSketch
    public void testBinoHeap() {
        BinomialHeap heap = new BinomialHeap();
        Integer[] ints = Values.exhaust(1, 3, 2, 4);
        for (int i : ints) {
            heap.insert(i);
            heap.insert(-1);
            heap.decreaseKeyVariable(-1, 2);
            heap.decreaseKeyVariable(i, 2 * i - 10);
        }
        if (heap.extractMin() != -1) {
            Observer.observeExpr(heap.findMinimum());
        }
        Observer.observeExpr(heap.extractMin());
        heap.delete(-1);
        heap.delete(2 * 2 - 10);
        Observer.observeExpr(heap.extractMin());
        heap.delete(1 * 2 - 10);
        Observer.observeExpr(heap.extractMin());
        heap.delete(3 * 2 - 10);
        Observer.observeExpr(heap.extractMin());
        heap.delete(4 * 2 - 10);
        Observer.observeExpr(heap.extractMin());
    }
}
