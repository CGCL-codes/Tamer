package br.biofoco.p2p.utils;

import br.biofoco.p2p.guice.ProductionModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceHelper {

    private static Injector injector;

    private GuiceHelper() {
    }

    public static synchronized Injector getInjector() {
        if (injector == null) {
            createInjector();
        }
        return injector;
    }

    private static void createInjector() {
        injector = Guice.createInjector(new ProductionModule());
    }
}
