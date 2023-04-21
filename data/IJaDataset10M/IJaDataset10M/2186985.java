package thread.folkJoinIssue.divideAndConquer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * User: webserg
 * Date: 09.12.11
 */
public class Lancher {

    public static void main(String[] args) {
        long[] anArray = new long[100000];
        for (int i = 0; i < anArray.length - 1; i++) anArray[i] = i;
        RecursiveAction mainTask = new IncrementTask(anArray, 0, anArray.length);
        ForkJoinPool mainPool = new ForkJoinPool();
        mainPool.invoke(mainTask);
    }
}
