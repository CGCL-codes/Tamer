package com.mgensystems.jarindexer.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocCollector;
import com.mgensystems.jarindexer.search.IndexSearch;
import com.mgensystems.jarindexer.search.NonInteractiveSearch;
import com.mgensystems.jarindexer.search.format.BasicFormat;
import com.mgensystems.jarindexer.search.format.FormattingException;
import com.mgensystems.jarindexer.search.format.MavenFormat;
import com.mgensystems.jarindexer.search.format.ResultFormat;

public class JarIndexSearch {

    protected static String index;

    protected static String field = "all:";

    protected static String queries = null;

    protected static String output = null;

    protected static String repo = null;

    private static class OneNormsReader extends FilterIndexReader {

        private String field;

        public OneNormsReader(IndexReader in, String field) {
            super(in);
            this.field = field;
        }

        public byte[] norms(String field) throws IOException {
            return in.norms(this.field);
        }
    }

    private void SearchFiles() {
    }

    /** Simple command-line based search demo. */
    public static void main(String[] args) throws Exception {
        CommandLineParser cli_parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        Options opts = buildOptions();
        String[] searchPaths;
        CommandLine cli = null;
        try {
            cli = cli_parser.parse(opts, args);
        } catch (ParseException e) {
            formatter.printHelp("JarIndexer", opts, true);
            System.exit(1);
        }
        index = cli.getOptionValue("i");
        queries = null;
        output = null;
        repo = null;
        if (cli.hasOption("i")) {
            field = cli.getOptionValue("f");
        }
        if (cli.hasOption("q")) {
            queries = cli.getOptionValue("q");
        }
        if (cli.hasOption("o")) {
            output = cli.getOptionValue("o");
        }
        if (cli.hasOption("l")) {
            repo = cli.getOptionValue("l");
        }
        nonInteractiveSearch();
    }

    public static void nonInteractiveSearch() {
        IndexSearch search = new NonInteractiveSearch();
        BufferedReader in = null;
        ResultFormat format;
        Map inputArgs = new HashMap();
        if (output != null) {
            format = new MavenFormat();
            inputArgs.put("repo", repo);
        } else {
            format = new BasicFormat();
            inputArgs.put("index", "");
        }
        format.setInputArgs(inputArgs);
        try {
            if (queries != null) {
                in = new BufferedReader(new FileReader(queries));
            } else {
                in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            }
            while (true) {
                if (queries == null) {
                    System.out.println("Enter query: ");
                }
                String line = in.readLine();
                if (line == null || line.length() == -1) {
                    break;
                }
                line = line.trim();
                if (line.length() == 0) {
                    break;
                }
                List<String> results = search.search(index, field, format, line, 5);
                for (String found : results) {
                    System.out.println(found);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * This method uses a custom HitCollector implementation which simply prints
	 * out the docId and score of every matching document.
	 * 
	 * This simulates the streaming search use case, where all hits are supposed
	 * to be processed, regardless of their relevance.
	 */
    public static void doStreamingSearch(final Searcher searcher, Query query) throws IOException {
        HitCollector streamingHitCollector = new HitCollector() {

            public void collect(int doc, float score) {
                System.out.println("doc=" + doc + " score=" + score);
            }
        };
        searcher.search(query, streamingHitCollector);
    }

    /**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
    public static void doPagingSearch(BufferedReader in, Searcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive, String output, String repo) throws IOException, FormattingException {
        TopDocCollector collector = new TopDocCollector(5 * hitsPerPage);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        int numTotalHits = collector.getTotalHits();
        System.out.println(numTotalHits + " total matching documents");
        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);
        while (true) {
            if (end > hits.length) {
                System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents collected.");
                System.out.println("Collect more (y/n) ?");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n') {
                    break;
                }
                collector = new TopDocCollector(numTotalHits);
                searcher.search(query, collector);
                hits = collector.topDocs().scoreDocs;
            }
            end = Math.min(hits.length, start + hitsPerPage);
            MavenFormat mf;
            BasicFormat bf;
            Map mfia;
            for (int i = start; i < end; i++) {
                if (raw) {
                    System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
                    continue;
                }
                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("path");
                if (path != null) {
                    if (output != null) {
                        mf = new MavenFormat();
                        mfia = new HashMap();
                        mfia.put("path", path);
                        mfia.put("repo", repo);
                        System.out.println(mf.format(mfia, doc));
                    } else {
                        mfia = new HashMap();
                        mfia.put("index", i + 1);
                        bf = new BasicFormat();
                        System.out.println(bf.format(mfia, doc));
                    }
                } else {
                    System.out.println((i + 1) + ". " + "No path for this document");
                }
            }
            if (!interactive) {
                break;
            }
            if (numTotalHits >= end) {
                boolean quit = false;
                while (true) {
                    System.out.print("Press ");
                    if (start - hitsPerPage >= 0) {
                        System.out.print("(p)revious page, ");
                    }
                    if (start + hitsPerPage < numTotalHits) {
                        System.out.print("(n)ext page, ");
                    }
                    System.out.println("(q)uit or enter number to jump to a page.");
                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0) == 'q') {
                        quit = true;
                        break;
                    }
                    if (line.charAt(0) == 'p') {
                        start = Math.max(0, start - hitsPerPage);
                        break;
                    } else if (line.charAt(0) == 'n') {
                        if (start + hitsPerPage < numTotalHits) {
                            start += hitsPerPage;
                        }
                        break;
                    } else {
                        int page = Integer.parseInt(line);
                        if ((page - 1) * hitsPerPage < numTotalHits) {
                            start = (page - 1) * hitsPerPage;
                            break;
                        } else {
                            System.out.println("No such page");
                        }
                    }
                }
                if (quit) break;
                end = Math.min(numTotalHits, start + hitsPerPage);
            }
        }
    }

    protected static Options buildOptions() {
        Options options = new Options();
        Option ip = OptionBuilder.withArgName("index").hasArg().isRequired().withLongOpt("index-path").withDescription("full path to the location of the index").create("i");
        Option f = OptionBuilder.withArgName("field").hasArg().withLongOpt("field-name").withDescription("sets the default field").create("f");
        Option q = OptionBuilder.withArgName("queries").hasArg().withLongOpt("path-to-file").withDescription("file containng multiple queries").create("q");
        Option o = OptionBuilder.withArgName("format").hasArg().withLongOpt("result-format").withDescription("set to 'm' for maven dependency").create("o");
        Option l = OptionBuilder.withArgName("repo").hasArg().withLongOpt("repo-location").withDescription("the base path of the maven repository for building the dep section").create("l");
        options.addOption(ip);
        options.addOption(f);
        options.addOption(q);
        options.addOption(o);
        options.addOption(l);
        return options;
    }
}
