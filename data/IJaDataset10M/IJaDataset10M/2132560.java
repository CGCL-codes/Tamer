package edu.cmu.ece.agora.futures.test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import edu.cmu.ece.agora.futures.FutureListener;
import edu.cmu.ece.agora.futures.FuturePackage;
import edu.cmu.ece.agora.futures.Futures;
import edu.cmu.ece.agora.futures.TaskLocal;

public class TaskLocalTestTwo {

    public static void main(String[] args) throws InterruptedException {
        Executor exec = Executors.newCachedThreadPool();
        final TaskLocal<String> tl = Futures.newTaskLocal("-");
        Futures.startTask();
        FuturePackage<Void> fp1 = Futures.newFuturePackage(exec);
        tl.set("1");
        fp1.getFuture().addListener(new FutureListener<Void>() {

            @Override
            public void onCancellation(Throwable cause) {
            }

            @Override
            public void onCompletion(Void result) {
                System.out.println("Future 1");
                printTaskLocal(tl);
            }
        });
        Futures.startTask();
        FuturePackage<Void> fp2 = Futures.newFuturePackage(exec);
        tl.set("2");
        fp2.getFuture().addListener(new FutureListener<Void>() {

            @Override
            public void onCancellation(Throwable cause) {
            }

            @Override
            public void onCompletion(Void result) {
                System.out.println("Future 2");
                printTaskLocal(tl);
            }
        });
        fp2.getManager().completeFuture(null);
        fp1.getManager().completeFuture(null);
    }

    private static void printTaskLocal(TaskLocal<?> tl) {
        System.out.println("[TID " + Thread.currentThread().getId() + "] " + tl.get());
    }
}
