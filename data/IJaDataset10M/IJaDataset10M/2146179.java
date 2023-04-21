package cx.ath.mancel01.dependencyshot.test.tck;

import cx.ath.mancel01.dependencyshot.DependencyShot;
import cx.ath.mancel01.dependencyshot.api.DSInjector;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.accessories.SpareTire;

/**
 * The test suite with TCK tests.
 * 
 * @author Mathieu ANCELIN
 */
public class TCKFluentStaticTest extends TestSuite {

    public static Test suite() {
        final DSInjector injector = DependencyShot.getInjector(new TCKFluentBinder());
        injector.injectStatics(Convertible.class);
        injector.injectStatics(Tire.class);
        injector.injectStatics(SpareTire.class);
        Car car = injector.getInstance(Car.class);
        return Tck.testsFor(car, true, true);
    }
}
