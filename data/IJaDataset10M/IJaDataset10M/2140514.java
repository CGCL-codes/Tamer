package com.mycila.junit.rule;

import com.mycila.junit.concurrent.ConcurrentException;
import com.mycila.junit.concurrent.ConcurrentRunnerScheduler;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestRepeater implements TestRule {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                Times annotation = description.getAnnotation(Times.class);
                if (annotation == null) base.evaluate(); else {
                    int times = Math.max(0, annotation.value());
                    if (times > 0) {
                        if (!annotation.concurrent()) {
                            while (times-- > 0) {
                                base.evaluate();
                            }
                        } else {
                            ConcurrentRunnerScheduler scheduler = new ConcurrentRunnerScheduler(description.getTestClass().getSimpleName() + (description.isTest() ? "#" + description.getMethodName() : ""), times);
                            final CountDownLatch go = new CountDownLatch(1);
                            Runnable runnable = new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        go.await();
                                        base.evaluate();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    } catch (Throwable throwable) {
                                        throw ConcurrentException.wrap(throwable);
                                    }
                                }
                            };
                            while (times-- > 0) scheduler.schedule(runnable);
                            go.countDown();
                            try {
                                scheduler.finished();
                            } catch (ConcurrentException e) {
                                throw e.unwrap();
                            }
                        }
                    }
                }
            }
        };
    }
}
