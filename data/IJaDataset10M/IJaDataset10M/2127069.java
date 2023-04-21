package workday.client;

import workday.client.core.Configurator;

/**
 *
 */
public class Main {

    private static Configurator configurator;

    public static void main(String[] args) {
        try {
            configurator = Configurator.getInstance();
            configurator.application_checkDependencies();
            configurator.application_initialize();
        } catch (Exception exception) {
            System.err.println(exception.getLocalizedMessage());
        }
    }
}
