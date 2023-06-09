package hu.akarnokd.reactive4java.test;

import hu.akarnokd.reactive4java.base.Action0;
import hu.akarnokd.reactive4java.base.Action1;
import hu.akarnokd.reactive4java.base.Func1;
import hu.akarnokd.reactive4java.base.Functions;
import hu.akarnokd.reactive4java.reactive.Observable;
import hu.akarnokd.reactive4java.reactive.Reactive;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test Reactive operators, B.
 * @author akarnokd
 */
public final class TestB {

    /**
	 * Utility class.
	 */
    private TestB() {
    }

    /** 
	 * Run the observable with a print attached. 
	 * @param observable the source observable
	 * @throws InterruptedException when the current thread is interrupted while
	 * waiting on the observable completion
	 */
    static void run(Observable<?> observable) throws InterruptedException {
        Reactive.run(observable, Reactive.print());
    }

    /**
	 * @param args no arguments
	 * @throws Exception on error
	 */
    public static void main(String[] args) throws Exception {
        Reactive.run(Reactive.selectMany(Reactive.range(0, 10), new Func1<Integer, Observable<Integer>>() {

            @Override
            public Observable<Integer> invoke(Integer param1) {
                return Reactive.range(0, param1);
            }
        }), Reactive.println());
        run(Reactive.tick(0, 10, 1, TimeUnit.SECONDS));
        Observable<Observable<Long>> window = Reactive.window(Reactive.tick(0, 10, 1, TimeUnit.SECONDS), Functions.constant0(Reactive.tick(0, 2, 2, TimeUnit.SECONDS)));
        final CountDownLatch cdl = new CountDownLatch(1);
        window.register(Reactive.toObserver(new Action1<Observable<Long>>() {

            @Override
            public void invoke(Observable<Long> value) {
                System.out.println("New window");
                value.register(Reactive.println());
            }
        }, new Action1<Throwable>() {

            @Override
            public void invoke(Throwable value) {
                value.printStackTrace();
            }
        }, new Action0() {

            @Override
            public void invoke() {
                System.out.println("Finished");
                cdl.countDown();
            }
        }));
        cdl.await();
        System.out.printf("%nMain finished%n");
    }
}
