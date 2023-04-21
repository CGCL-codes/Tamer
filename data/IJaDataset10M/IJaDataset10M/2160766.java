package schemacrawler.tools.integration.freemarker;

/**
 * Main class that takes arguments for a database for crawling a schema.
 */
public final class Main {

    /**
   * Get connection parameters, and creates a connection, and crawls the
   * schema.
   * 
   * @param args
   *        Arguments passed into the program from the command line.
   * @throws Exception
   *         On an exception
   */
    public static void main(final String[] args) throws Exception {
        new FreeMarkerRenderer().main(args);
    }

    private Main() {
    }
}
