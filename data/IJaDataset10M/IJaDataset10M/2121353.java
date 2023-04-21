package sample.empty;

import com.google.inject.*;
import org.guicebox.*;

/**
 * A simple, empty GuiceBox application that simply starts up and does nothing.
 * 
 * @author willhains
 */
public class EmptyStandalone {

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new CommandLineModule(args));
        final GuiceBox guicebox = injector.getInstance(GuiceBox.class);
        guicebox.start();
    }
}
