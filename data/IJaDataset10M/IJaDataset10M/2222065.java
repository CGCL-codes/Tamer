package samples;

import com.google.inject.AbstractModule;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.mycila.inject.scope.ExtraScopeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AdditionalScopeTest {

    @Test
    public void test() throws Exception {
        Jsr250Injector jsr250Injector = Jsr250.createInjector(new ExtraScopeModule(), new MyModule());
    }

    static final class MyModule extends AbstractModule {

        @Override
        protected void configure() {
        }
    }
}
