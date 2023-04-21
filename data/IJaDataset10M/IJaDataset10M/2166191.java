package fr.armida.refloker.example;

import static fr.armida.refloker.Reflector.declaredIn;
import static fr.armida.refloker.Reflector.execute;
import static fr.armida.refloker.Reflector.executeAndReturnValue;
import static fr.armida.refloker.Reflector.on;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class NoArgMethodInvocationExampleTest {

    public static class Example {

        public boolean publicVoidMethodInvoked;

        public boolean privateVoidMethodInvoked;

        public void voidMethod() {
            publicVoidMethodInvoked = true;
        }

        public String stringMethod() {
            return "invoked";
        }

        private void privateVoidMethod() {
            privateVoidMethodInvoked = true;
        }
    }

    /**
	 * this class only inherits of {@link Example} and nothing else.
	 * 
	 * @author Gr�gory
	 * 
	 */
    public static class ExampleSubclass extends Example {
    }

    private Example example;

    @Before
    public void setUpExample() {
        example = new Example();
    }

    @Test
    public void shouldInvokeVisibleVoidMethod() {
        execute(on(example).invokeMethod("voidMethod"));
        assertThat(example.publicVoidMethodInvoked, is(true));
    }

    @Test
    public void shouldInvokeVisibleStringMethod() {
        String value = (String) executeAndReturnValue(on(example).invokeMethod("stringMethod"));
        assertThat(value, is("invoked"));
    }

    @Test
    public void shouldInvokeHiddenVoidMethod() {
        execute(on(example).invokeHiddenMethod("privateVoidMethod"));
        assertThat(example.privateVoidMethodInvoked, is(true));
    }

    @Test
    public void shouldInvokeMethodDeclaredInSuperclass() {
        ExampleSubclass subExample = new ExampleSubclass();
        execute(on(subExample).invokeMethod("voidMethod", declaredIn(Example.class)));
        assertThat(subExample.publicVoidMethodInvoked, is(true));
    }

    @Test
    public void shouldInvokeHiddenMethodDeclaredInSuperclass() {
        ExampleSubclass subExample = new ExampleSubclass();
        execute(on(subExample).invokeHiddenMethod("privateVoidMethod", declaredIn(Example.class)));
        assertThat(subExample.privateVoidMethodInvoked, is(true));
    }
}
