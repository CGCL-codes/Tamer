package benchmark.cute.original.tests.concurrent;

import benchmark.cute.original.concurrent.producer_consumer.Buffer;
import benchmark.cute.original.concurrent.producer_consumer.Consumer;
import benchmark.cute.original.concurrent.producer_consumer.CubbyHole;
import benchmark.cute.original.concurrent.producer_consumer.Producer;

/**
 *  .
 * User: Koushik Sen (ksen@cs.uiuc.edu)
 * Date: Nov 6, 2005
 * Time: 11:07:48 AM
 */
public class ProducerConsumerTest {

    public static void main(String[] args) {
        Buffer c = new CubbyHole();
        Producer p1 = new Producer(c, 1);
        Consumer c1 = new Consumer(c, 1);
        p1.start();
        c1.start();
    }
}
