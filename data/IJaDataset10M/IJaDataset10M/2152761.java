package persistent_heap.testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import persistent_heap.PersistentHeap;

public class PersistentHeapTest {

    public static void main(String[] args) {
        testHeapSort();
        testMerge();
        testPersistence();
        testChangeVal();
        testDeleteValShort();
        testDeleteValPerf();
    }

    private static void testDeleteValShort() {
        System.out.println("----------------------------------");
        System.out.println("-------- testDeleteValShort() ----------");
        System.out.println();
        PersistentHeap<Integer, Integer> myHeap = new PersistentHeap<Integer, Integer>();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 5;
        ArrayList<Integer> ints = new ArrayList<Integer>(total);
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            Integer newInt = randgen.nextInt(1000000);
            ints.add(newInt);
        }
        System.out.println("array insert done:\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            Integer val = ints.get(i);
            myHeap = myHeap.insert(val, val);
            System.out.println("inserted: " + val);
        }
        System.out.println("heap insert done:\t\t" + System.currentTimeMillis());
        Integer prev = myHeap.minPri();
        Integer curr = null;
        System.out.println("inserts done.");
        int i = 1;
        while (!myHeap.isEmpty()) {
            curr = myHeap.minPri();
            System.out.println(i + " min: " + curr);
            if (prev > curr) {
                throw new IllegalStateException("order constraint violation");
            }
            Integer val = ints.get(i - 1);
            System.out.println("deleting: " + val);
            if (randgen.nextInt(100) < 50) {
                myHeap = myHeap.lazyDelete(val, val);
            } else {
                myHeap = myHeap.eagerDelete(val, val);
            }
            prev = curr;
            i += 1;
        }
        if (i < total) {
            throw new IllegalStateException("some values lost: " + i + " < " + total);
        }
        System.out.println("deletes complete and ok:\t" + System.currentTimeMillis());
        System.out.println("-------- test complete -----------");
    }

    private static void testDeleteValPerf() {
        System.out.println("----------------------------------");
        System.out.println("-------- testDeleteValPerf() ----------");
        System.out.println();
        PersistentHeap<Integer, Integer> myHeap = new PersistentHeap<Integer, Integer>();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 200000;
        System.out.println("Test size: " + total + " values\t" + System.currentTimeMillis());
        ArrayList<Integer> ints = new ArrayList<Integer>(total);
        for (int i = 0; i < total; ++i) {
            Integer newInt = randgen.nextInt(1000000);
            ints.add(newInt);
        }
        for (int i = 0; i < total; ++i) {
            Integer val = ints.get(i);
            myHeap = myHeap.insert(val, val);
        }
        Integer prev = myHeap.minPri();
        Integer curr = null;
        System.out.println("starting random deletes:\t" + System.currentTimeMillis());
        int i = 1;
        while (!myHeap.isEmpty()) {
            curr = myHeap.minPri();
            if (prev > curr) {
                throw new IllegalStateException("order constraint violation");
            }
            Integer val = ints.get(i - 1);
            if (randgen.nextInt(100) < 50) {
                myHeap = myHeap.lazyDelete(val, val);
            } else {
                myHeap = myHeap.eagerDelete(val, val);
            }
            prev = curr;
            i += 1;
        }
        if (i < total) {
            throw new IllegalStateException("some values lost: " + i + " < " + total);
        }
        System.out.println("deletes complete and ok:\t" + System.currentTimeMillis());
        System.out.println("-------- test complete -----------");
    }

    private static void testChangeVal() {
        System.out.println("----------------------------------");
        System.out.println("-------- testChangeVal() ----------");
        System.out.println();
        PersistentHeap<Integer, Integer> myHeap = new PersistentHeap<Integer, Integer>();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 2000;
        ArrayList<Integer> ints = new ArrayList<Integer>(total);
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            Integer newInt = randgen.nextInt(1000000);
            ints.add(newInt);
        }
        System.out.println("array insert done:\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            int val = ints.get(i);
            myHeap = myHeap.insert(i, val);
        }
        System.out.println("heap insert done:\t\t" + System.currentTimeMillis());
        System.out.println("inserts done.");
        int changes = 5000;
        System.out.println("change values around for a while: " + changes + " changes");
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        for (int v = 0; v < changes; ++v) {
            Integer index = randgen.nextInt(total);
            Integer oldVal = ints.get(index);
            Integer newVal = randgen.nextInt(1000000);
            ints.set(index, newVal);
            myHeap = myHeap.changeVal(index, oldVal, newVal);
        }
        System.out.println("Changes Complete\t\t" + System.currentTimeMillis());
        Collections.sort(ints);
        System.out.println("Popping and checking everything.\t" + System.currentTimeMillis());
        Integer prev = myHeap.minPri();
        Integer curr = null;
        int i = 1;
        while (!myHeap.isEmpty()) {
            curr = myHeap.minPri();
            Integer val = ints.get(i - 1);
            if (curr.compareTo(val) != 0) {
                throw new IllegalStateException("holding wrong value. " + "violation at count: " + i + ", curr: " + curr + " not equal: " + val);
            }
            i += 1;
            if (prev > curr) {
                throw new IllegalStateException("order constraint " + "violation at count: " + i + ", prev: " + prev + ", curr: " + curr);
            }
            myHeap = myHeap.deleteMin();
            prev = curr;
        }
        if (i < total) {
            throw new IllegalStateException("some values lost: " + i + " < " + total);
        }
        System.out.println("deletes complete and ok:\t" + System.currentTimeMillis());
        System.out.println("-------- test complete -----------");
    }

    public static void testHeapSort() {
        System.out.println("----------------------------------");
        System.out.println("-------- testHeapSort() ----------");
        System.out.println();
        PersistentHeap<Integer, Integer> myHeap = new PersistentHeap<Integer, Integer>();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 200000;
        ArrayList<Integer> ints = new ArrayList<Integer>(total);
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            Integer newInt = randgen.nextInt(1000000);
            ints.add(newInt);
        }
        System.out.println("array insert done:\t\t" + System.currentTimeMillis());
        for (int i = 0; i < total; ++i) {
            Integer val = ints.get(i);
            myHeap = myHeap.insert(val, val);
        }
        System.out.println("heap insert done:\t\t" + System.currentTimeMillis());
        Integer prev = myHeap.minPri();
        Integer curr = null;
        System.out.println("inserts done.");
        int i = 1;
        while (!myHeap.isEmpty()) {
            curr = myHeap.minPri();
            i += 1;
            if (prev > curr) {
                throw new IllegalStateException("order constraint violation");
            }
            myHeap = myHeap.deleteMin();
            prev = curr;
        }
        if (i < total) {
            throw new IllegalStateException("some values lost: " + i + " < " + total);
        }
        System.out.println("deletes complete and ok:\t" + System.currentTimeMillis());
        Collections.sort(ints);
        System.out.println("destrictive sort done:\t\t" + System.currentTimeMillis());
        System.out.println("-------- test complete -----------");
    }

    public static void testMerge() {
        System.out.println("----------------------------------");
        System.out.println("-------- testMerge() -------------");
        System.out.println();
        PersistentHeap<Integer, Integer> myHeap1 = new PersistentHeap<Integer, Integer>();
        PersistentHeap<Integer, Integer> myHeap2 = new PersistentHeap<Integer, Integer>();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 200000;
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        Integer newInt = null;
        for (int i = 0; i < total; ++i) {
            newInt = randgen.nextInt(1000000);
            myHeap1 = myHeap1.insert(newInt, newInt);
            newInt = randgen.nextInt(1000000);
            myHeap2 = myHeap1.insert(newInt, newInt);
        }
        System.out.println("all inserts done:\t\t" + System.currentTimeMillis());
        myHeap1 = myHeap1.meld(myHeap2);
        System.out.println("meld complete:\t\t\t" + System.currentTimeMillis());
        Integer prev = myHeap1.minPri();
        Integer curr = null;
        int i = 1;
        while (!myHeap1.isEmpty()) {
            curr = myHeap1.minPri();
            i += 1;
            if (prev > curr) {
                throw new IllegalStateException("order constraint violation");
            }
            myHeap1 = myHeap1.deleteMin();
            prev = curr;
        }
        if (i < 2 * total) {
            throw new IllegalStateException("some values lost: " + i + " < " + total);
        }
        System.out.println("deletes complete and ok:\t" + System.currentTimeMillis());
        System.out.println("-------- test complete -----------");
    }

    private static void testPersistence() {
        System.out.println("----------------------------------");
        System.out.println("-------- testPersistence() -------");
        System.out.println();
        Random randgen = new Random(System.currentTimeMillis());
        int total = 200;
        ArrayList<Integer> ints = new ArrayList<Integer>(total);
        ArrayList<PersistentHeap<Integer, Integer>> heapList = new ArrayList<PersistentHeap<Integer, Integer>>(total);
        System.out.println("\t\t\t\t" + System.currentTimeMillis());
        Integer newInt = randgen.nextInt(1000000);
        ints.add(newInt);
        heapList.add((new PersistentHeap<Integer, Integer>()).insert(newInt, newInt));
        for (int i = 1; i < total; ++i) {
            newInt = randgen.nextInt(1000000);
            ints.add(newInt);
            heapList.add(heapList.get(i - 1).insert(newInt, newInt));
        }
        System.out.println("-- built array of heap states ----");
        for (int i = 0; i < total; ++i) {
            PersistentHeap<Integer, Integer> myHeap = heapList.get(i);
            Integer prev = myHeap.minPri();
            Integer curr = null;
            final int size = i + 1;
            int count = 1;
            while (!myHeap.isEmpty()) {
                curr = myHeap.minPri();
                count += 1;
                if (prev > curr) {
                    throw new IllegalStateException("order constraint violation");
                }
                myHeap = myHeap.deleteMin();
                prev = curr;
            }
            if (count < size) {
                throw new IllegalStateException("some values lost: " + i + " < " + total);
            }
        }
        System.out.println("-------- test complete -----------");
    }
}
