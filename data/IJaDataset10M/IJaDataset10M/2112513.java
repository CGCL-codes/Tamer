package lia.ch1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.in4j.core.common.TimeLogger;
import com.sohospace.lucene.analysis.xanalyzer.XFactory;
import com.sun.corba.se.spi.orbutil.fsm.Input;

/**
 *
 * @author yangfan
 * @version 1.0 2007-6-7 上午01:55:35,创建
 */
public class Searcher {

    private final String indexDir;

    public Searcher(String indexDir) {
        this.indexDir = indexDir;
    }

    private void execute(String queryString) throws Exception {
        TimeLogger timeLogger = new TimeLogger();
        IndexSearcher searcher = new IndexSearcher(indexDir);
        QueryParser parser = new QueryParser("filename", getAnalyzer());
        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);
        final int count = hits.length();
        for (int i = 0; i < count; i++) {
            Document doc = hits.doc(i);
            print(doc);
        }
        System.out.println("搜索到 " + count + " 条结果,耗时:" + timeLogger.getElapsedTime() + "ms");
    }

    private void print(Document doc) throws IOException {
        String filename = doc.get("filename");
        byte[] bytes = doc.getBinaryValue("text");
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
        int i = reader.read();
        char[] buf = new char[4096];
        StringWriter sw = new StringWriter();
        while ((i = reader.read(buf)) != -1) {
            sw.write(buf);
        }
        System.out.println();
        System.out.println("\t文件:" + filename);
        System.out.println(sw.toString());
        System.out.println("----------------------------------------------------------------------");
    }

    private Analyzer getAnalyzer() {
        return XFactory.getQueryAnalyzer();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new Exception("使用方法: java " + Searcher.class.getName() + " <index dir> <query>");
        Searcher searcher = new Searcher(args[0]);
        searcher.execute(args[1]);
    }
}
