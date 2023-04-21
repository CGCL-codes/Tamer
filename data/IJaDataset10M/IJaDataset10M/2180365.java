package upm.fi.oeg.test.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

public class QueryLoader {

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(QueryLoader.class.getName());

    private static File cfgFile;

    private static final String PROP_QUERY_DIR = "query.directory";

    private static final String PROP_DQP_SERVER = "dqp.server";

    private static Properties props = new Properties();

    /**
     * Private constructor for reading the configuration settings from a file.
     *  
     * @param fileName the file containing the configuration settings.
     * @throws IOException if an error occurred when reading from the file.
     */
    private QueryLoader(String fileName) throws IOException {
        QueryLoader.cfgFile = new File(fileName).getAbsoluteFile();
        QueryLoader.props.load(new FileReader(QueryLoader.cfgFile));
        LOG.info("loaded configuration from " + cfgFile);
    }

    /**
     * Load configurations setting from a file.
     * 
     * @param configFile the file containing the configuration settings.
     * @return instantiation of the configuration settings
     * @throws IOException if an error occurred when reading from the file.
     */
    public static QueryLoader load(String configFile) throws IOException {
        return new QueryLoader(configFile);
    }

    public static Iterator<Query> loadQueries() throws Exception {
        return new Iterator<Query>() {

            private List<File> files = getQueryList();

            @Override
            public boolean hasNext() {
                return files.size() != 0;
            }

            @Override
            public Query next() {
                if (files.size() == 0) throw new IllegalStateException("no more query files");
                File file = files.remove(0);
                try {
                    String query = readQuery(file);
                    LOG.debug(file.toString());
                    String name = file.getName();
                    int pos = name.lastIndexOf(".");
                    if (pos > 0) name = name.substring(0, pos);
                    return new Query(name, query);
                } catch (IOException e) {
                    throw new RuntimeException("can not load query " + file, e);
                }
            }

            @Override
            public void remove() {
            }

            /**
             * Returns the list of matching query files in the specified
             * directory.
             * 
             * @return list of query files.
             * @throws ConfigurationException
             *             if an error occurs during query reading.
             */
            private List<File> getQueryList() throws Exception {
                String queryDir = props.getProperty(PROP_QUERY_DIR);
                if (queryDir == null) throw new Exception("missing query dir setting '" + PROP_QUERY_DIR + "' in " + cfgFile);
                String[] queryDirs = queryDir.split(";");
                List<File> queries = new ArrayList<File>();
                for (String qDir : queryDirs) {
                    File dir = new File(cfgFile.toURI().resolve(qDir)).getAbsoluteFile();
                    if (!dir.isDirectory() || !dir.canRead()) LOG.warn("cannot read query directory: " + dir);
                    for (File file : dir.listFiles()) {
                        if (file.isFile() && file.getName().endsWith(".rq")) {
                            queries.add(file);
                        }
                    }
                }
                if (queries.size() == 0) LOG.error("found no matching queries");
                Collections.sort(queries);
                return queries;
            }

            /**
             * Reads a SPARQL query from a file.
             * 
             * @param file
             *            the file to read.
             * @return the query.
             * @throws IOException
             *             if an error occurs during query reading.
             */
            private String readQuery(File query) throws IOException {
                StringBuffer buffer = new StringBuffer();
                BufferedReader r = new BufferedReader(new FileReader(query));
                String input;
                while ((input = r.readLine()) != null) {
                    buffer.append(input).append("\n");
                }
                return buffer.toString();
            }
        };
    }

    public static String getServer() {
        return props.getProperty(PROP_DQP_SERVER);
    }
}
