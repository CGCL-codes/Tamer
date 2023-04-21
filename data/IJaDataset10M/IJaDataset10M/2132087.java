package samples.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import net.sf.asyncobjects.AsyncAction;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.When;
import net.sf.asyncobjects.util.Wait;

/**
 * A sample that demonstrate finallyDo functianlity
 * 
 * @author const
 */
public class WhenSample3 {

    /**
   * Application entry point
   * 
   * @param args
   *          application arguments
   */
    public static void main(String[] args) {
        try {
            Object v[] = new AsyncAction<Object[]>() {

                @Override
                public Promise<Object[]> run() throws Throwable {
                    Promise<Object> fp = new Promise<Object>();
                    fp.resolver().smash(new NullPointerException("Expected"));
                    Promise<Object> sp = new Promise<Object>();
                    sp.resolver().resolve("value");
                    class FinallyFailingWhen extends When<Object, Object> {

                        FinallyFailingWhen(Promise<Object> p) {
                            super(p);
                        }

                        @Override
                        protected Promise<Object> resolved(Object value) throws Throwable {
                            return new Promise<Object>(value);
                        }

                        @Override
                        protected Promise<?> finallyDo() throws Throwable {
                            throw new Exception("finally exception");
                        }
                    }
                    Promise<Object> r1 = new FinallyFailingWhen(sp).promise();
                    Promise<Object> r2 = new FinallyFailingWhen(fp).promise();
                    return Wait.all(WhenSample2.expectFailure(r1, Exception.class), WhenSample2.expectFailure(r2, Exception.class));
                }
            }.doInCurrentThread();
            System.out.println("Action finished successfully: " + Arrays.asList(v));
        } catch (InvocationTargetException ex) {
            System.out.println("Exception during action execution.");
            ex.getTargetException().printStackTrace(System.out);
        }
    }
}
