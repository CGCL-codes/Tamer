package handins;

import java.util.Arrays;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import edu.princeton.cs.stdlib.StdOut;
import edu.princeton.cs.stdlib.StdStats;
import edu.princeton.cs.stdlib.Stopwatch;

/**
 * Test program for random queue. Code by Thore Husfeldt.
 */
public class TestRandomQueue {

    public static void main(String args[]) {
        System.out.println("Benchmarking RandomQueue and RandomQueueArray");
        RandomQueue<Integer> RQ = new RandomQueue<Integer>();
        RandomQueueArray<Integer> RQA = new RandomQueueArray<Integer>();
        for (int i = 1; i < 7; ++i) {
            RQ.enqueue(i);
            RQA.enqueue(i);
        }
        StdOut.print("Some die rolls: \n");
        String rqRoll = "";
        String rqaRoll = "";
        for (int i = 1; i < 30; ++i) {
            rqRoll += RQ.sample() + " ";
            rqaRoll += RQA.sample() + " ";
        }
        StdOut.println("RandomQueue:" + rqRoll);
        StdOut.println("RandomQueueArray:" + rqaRoll);
        int[] rqRolls = new int[100000];
        int[] rqaRolls = new int[100000];
        for (int i = 0; i < 100000; ++i) {
            rqRolls[i] = RQ.sample();
            rqaRolls[i] = RQA.sample();
        }
        StdOut.printf("RandomQueue Mean (should be around 3.5): %5.4f\n", StdStats.mean(rqRolls));
        StdOut.printf("RandomQueue Standard deviation (should be around 1.7): %5.4f\n", StdStats.stddev(rqRolls));
        StdOut.printf("RandomQueueArray Mean (should be around 3.5): %5.4f\n", StdStats.mean(rqRolls));
        StdOut.printf("RandomQueueArray Standard deviation (should be around 1.7): %5.4f\n", StdStats.stddev(rqRolls));
    }

    @Test
    public void TestRandomQueueIterator() {
        System.out.println("Testing RandomQueue Iterator");
        RandomQueue<String> C = new RandomQueue<String>();
        C.enqueue("red");
        C.enqueue("blue");
        C.enqueue("green");
        C.enqueue("yellow");
        Iterator<String> I = C.iterator();
        Iterator<String> J = C.iterator();
        StdOut.print("RandomQueue: Two colours from first shuffle: ");
        StdOut.print(I.next() + " ");
        StdOut.print(I.next() + " ");
        StdOut.print("\nRandomQueueArray Entire second shuffle: ");
        while (J.hasNext()) StdOut.print(J.next() + " ");
        StdOut.print("\nRandomQueue Remaining two colours from first shuffle: ");
        StdOut.print(I.next() + " ");
        StdOut.println(I.next());
        StdOut.println("\nDoing iteration with a large array ");
        int size = 200000;
        RandomQueue<Integer> rq = new RandomQueue<Integer>();
        int[] queue = new int[size];
        for (int i = 0; i < size; ++i) {
            rq.enqueue(i);
            queue[i] = i;
        }
        Iterator<Integer> it1 = rq.iterator();
        Iterator<Integer> it2 = rq.iterator();
        int[] queue2 = new int[size];
        int[] queue3 = new int[size];
        int i = 0;
        while (it1.hasNext()) {
            queue2[i] = it1.next();
            i++;
        }
        i = 0;
        while (it2.hasNext()) {
            queue3[i] = it2.next();
            i++;
        }
        String q2 = "";
        String q3 = "";
        int max = size > 10 ? 10 : size;
        for (int k = 0; k < max; k++) {
            q2 += queue2[k] + ",";
            q3 += queue3[k] + ",";
        }
        q2 += "...";
        q3 += "...";
        System.out.println("Look at the first 10 elements");
        System.out.println(q2);
        System.out.println(q3);
    }

    @Test
    public void TestRandomQueueArrayIterator() {
        System.out.println("Testing RandomQueueArray Iterator");
        RandomQueueArray<String> C = new RandomQueueArray<String>();
        C.enqueue("red");
        C.enqueue("blue");
        C.enqueue("green");
        C.enqueue("yellow");
        Iterator<String> I = C.iterator();
        Iterator<String> J = C.iterator();
        StdOut.print("RandomQueue: Two colours from first shuffle: ");
        StdOut.print(I.next() + " ");
        StdOut.print(I.next() + " ");
        StdOut.print("\nRandomQueueArray Entire second shuffle: ");
        while (J.hasNext()) StdOut.print(J.next() + " ");
        StdOut.print("\nRandomQueue Remaining two colours from first shuffle: ");
        StdOut.print(I.next() + " ");
        StdOut.println(I.next());
        StdOut.println("\nDoing iteration with a large array ");
        int size = 200000;
        RandomQueueArray<Integer> rq = new RandomQueueArray<Integer>();
        int[] queue = new int[size];
        for (int i = 0; i < size; ++i) {
            rq.enqueue(i);
            queue[i] = i;
        }
        Iterator<Integer> it1 = rq.iterator();
        Iterator<Integer> it2 = rq.iterator();
        int[] queue2 = new int[size];
        int[] queue3 = new int[size];
        int i = 0;
        while (it1.hasNext()) {
            queue2[i] = it1.next();
            i++;
        }
        i = 0;
        while (it2.hasNext()) {
            queue3[i] = it2.next();
            i++;
        }
        String q2 = "";
        String q3 = "";
        int max = size > 10 ? 10 : size;
        for (int k = 0; k < max; k++) {
            q2 += queue2[k] + ",";
            q3 += queue3[k] + ",";
        }
        q2 += "...";
        q3 += "...";
        System.out.println("Look at the first 10 elements");
        System.out.println(q2);
        System.out.println(q3);
    }

    @Test
    public void TestRandomQueueDequeue() {
        int size = 10;
        System.out.println("Testing RandomQueue Dequeue");
        RandomQueue<Integer> rq = new RandomQueue<Integer>();
        int[] queue = new int[size];
        for (int i = 0; i < size; ++i) {
            rq.enqueue(i);
            queue[i] = i;
        }
        int[] dequeue = new int[size];
        for (int i = 0; i < size; ++i) {
            dequeue[i] = rq.dequeue();
        }
        String q2 = "";
        String q3 = "";
        int max = size > 10 ? 10 : size;
        for (int k = 0; k < max; k++) {
            q2 += queue[k] + ",";
            q3 += dequeue[k] + ",";
        }
        System.out.println("Look at the first 10 elements");
        System.out.println(q2);
        System.out.println(q3);
        int[] dequeueClone = dequeue.clone();
        Arrays.sort(dequeueClone);
        Assert.assertArrayEquals(queue, dequeueClone);
        System.out.println("Testing random dequeue exponentially growing array");
        for (int i = 1; i <= 16; i++) {
            RandomQueue<Integer> rq2 = new RandomQueue<Integer>();
            int elements = (int) Math.pow(2, i);
            for (int k = 0; k < elements; k++) {
                rq2.enqueue(k);
            }
            Stopwatch w = new Stopwatch();
            for (int k = 0; k < elements; k++) {
                rq2.dequeue();
            }
            System.out.printf("%s;%s\n", elements, w.elapsedTime());
        }
    }

    @Test
    public void TestRandomQueueArrayDequeue() {
        int size = 10;
        System.out.println("Testing RandomQueueArray Dequeue");
        RandomQueue<Integer> rq = new RandomQueue<Integer>();
        int[] queue = new int[size];
        for (int i = 0; i < size; ++i) {
            rq.enqueue(i);
            queue[i] = i;
        }
        int[] dequeue = new int[size];
        for (int i = 0; i < size; ++i) {
            dequeue[i] = rq.dequeue();
        }
        String q2 = "";
        String q3 = "";
        int max = size > 10 ? 10 : size;
        for (int k = 0; k < max; k++) {
            q2 += queue[k] + ",";
            q3 += dequeue[k] + ",";
        }
        System.out.println("Look at the first 10 elements");
        System.out.println(q2);
        System.out.println(q3);
        int[] dequeueClone = dequeue.clone();
        Arrays.sort(dequeueClone);
        Assert.assertArrayEquals(queue, dequeueClone);
        System.out.println("Testing random dequeue exponentially growing array");
        for (int i = 1; i <= 16; i++) {
            RandomQueue<Integer> rq2 = new RandomQueue<Integer>();
            int elements = (int) Math.pow(2, i);
            for (int k = 0; k < elements; k++) {
                rq2.enqueue(k);
            }
            Stopwatch w = new Stopwatch();
            for (int k = 0; k < elements; k++) {
                rq2.dequeue();
            }
            System.out.printf("%s;%s\n", elements, w.elapsedTime());
        }
    }

    @Test
    public void TestSamplingRandomQueue() {
        System.out.println("Testing RandomQueue Sampling");
        RandomQueue<Integer> q = new RandomQueue<Integer>();
        for (int i = 0; i < Math.pow(2, 16); i++) {
            q.enqueue(i);
        }
        int T = 16;
        Stopwatch w = new Stopwatch();
        for (int k = 1; k <= T; k++) {
            int ceil = (int) Math.pow(2, k);
            for (int i = 0; i < ceil; i++) {
                q.sample();
            }
            System.out.printf("Sampling %s times: %s\n", Math.pow(2, k), w.elapsedTime());
        }
    }

    @Test
    public void TestSamplingRandomQueueArray() {
        System.out.println("Testing RandomQueueArray Sampling");
        RandomQueueArray<Integer> q = new RandomQueueArray<Integer>();
        for (int i = 0; i < Math.pow(2, 16); i++) {
            q.enqueue(i);
        }
        int T = 16;
        Stopwatch w = new Stopwatch();
        for (int k = 1; k <= T; k++) {
            int ceil = (int) Math.pow(2, k);
            for (int i = 0; i < ceil; i++) {
                q.sample();
            }
            System.out.printf("Sampling %s times: %s\n", Math.pow(2, k), w.elapsedTime());
        }
    }
}
