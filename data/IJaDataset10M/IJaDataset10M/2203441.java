package br.org.eteg.curso.javaoo.capitulo11.sql;

import java.io.IOException;

public class HSQLDBConsole {

    private static final String STARTUP = "java org.hsqldb.Server";

    private static final String SHUTDOWN = "java -jar path/to/hsqldb.jar --noinput --sql shutdown localhost-sa";

    public void iniciar() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(STARTUP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
