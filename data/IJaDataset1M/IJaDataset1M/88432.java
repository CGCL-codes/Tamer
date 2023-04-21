package org.smth.search.fetcher;

import com.google.inject.Injector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import org.smth.search.Starter;
import org.smth.search.indexer.IndexManager;
import org.smth.search.indexer.impl.EnumAllAnalyzer;
import org.smth.search.indexer.impl.EnumAllTokenStream;
import org.smth.search.indexer.impl.FlagAnalyzer;
import org.smth.search.persistence.impl.JpaCallback;
import org.smth.search.persistence.impl.JpaDao;
import org.smth.search.query.QueryParser;
import org.smth.search.query.impl.FieldQueryParser;
import org.junit.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Test2 {

    @Test
    public void test01() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smths", "root", "123457");
        System.out.println(conn);
        conn.close();
    }

    @Test
    public void test02() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("smth");
        System.out.println(factory);
    }

    @Test
    public void test03() throws Exception {
        System.out.println(this.getClass().getClassLoader().getResource("c3p0.properties"));
        System.out.println(this.getClass().getClassLoader().getResource("/c3p0.properties"));
    }

    @Test
    public void test04() throws Exception {
        long n = 1187544877;
        Date date = new Date(n * 1000);
        System.out.println(date);
    }

    @Test
    public void test05() throws Exception {
        int[] DefaultSeps = EnumAllTokenStream.DefaultSeps;
        System.out.println(DefaultSeps.length);
        Arrays.sort(DefaultSeps);
        for (int i = 0; i < DefaultSeps.length; i++) {
            if (i > 0 && DefaultSeps[i] == DefaultSeps[i - 1]) {
                System.out.println("why?");
            }
        }
    }

    @Test
    public void test06() throws Exception {
        Analyzer analyzer;
        if (System.currentTimeMillis() < 1) {
            analyzer = new EnumAllAnalyzer();
        } else {
            analyzer = new FlagAnalyzer();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()).length() > 0) {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
            Token token;
            while ((token = stream.next()) != null) {
                System.out.println(token);
            }
            stream.close();
        }
    }

    @Test
    public void test07() throws Exception {
        IndexReader reader = IndexReader.open("D:\\workg\\index");
        System.out.println(reader);
        int num = reader.numDocs();
        System.out.println(num);
        System.out.println(reader.document(num - 1).get("postId"));
        reader.close();
    }

    @Test
    public void test08() throws Exception {
        File file = new File("D:\\workg\\index");
        File lock = new File(file, "write.lock");
        if (lock.exists()) {
            boolean b = lock.delete();
            System.out.println(b);
        }
    }

    public void mergeIndex(String dir, int level) throws Exception {
        IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(), false);
        Method method = writer.getClass().getDeclaredMethod("maybeMergeSegments", Integer.TYPE);
        method.setAccessible(true);
        method.invoke(writer, level);
        writer.close();
    }

    @Test
    public void test09() throws Exception {
        mergeIndex("D:\\workg\\index", 340);
    }

    @Test
    public void test10() throws Exception {
        IndexWriter writer = new IndexWriter("D:\\workg\\index", new StandardAnalyzer(), false);
        writer.optimize();
        writer.close();
    }

    @Test
    public void test11() throws Exception {
        IndexWriter writer = new IndexWriter("D:\\workg\\index", new StandardAnalyzer(), false);
        Field field = writer.getClass().getDeclaredField("segmentInfos");
        field.setAccessible(true);
        Object infos = field.get(writer);
        Method[] methods = writer.getClass().getDeclaredMethods();
        Method target = null;
        for (Method method : methods) {
            if ("mergeSegments".equals(method.getName())) {
                target = method;
                break;
            }
        }
        target.setAccessible(true);
        target.invoke(writer, infos, Integer.parseInt(Test4.reader.readLine()), Integer.parseInt(Test4.reader.readLine()));
        writer.close();
    }

    @Test
    public void test12() throws Exception {
        Injector injector = Starter.getInstance().getInjector();
        IndexManager manager = injector.getInstance(IndexManager.class);
        manager.start();
        Thread.sleep(30000);
        manager.close();
    }

    @Test
    public void test13() throws Exception {
        FieldQueryParser parser = new FieldQueryParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (!(line = reader.readLine()).equals("exit")) {
            List<String> list = parser.parseTokens(line);
            System.out.printf("共有%d个token\n", list.size());
            for (String s : list) {
                System.out.println(s);
            }
        }
    }

    @Test
    public void test14() throws Exception {
        QueryParser parser = Starter.getInstance().getInjector().getInstance(QueryParser.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (!(line = reader.readLine()).equals("exit")) {
            Query query = parser.parseQuery(line).getQuery();
            System.out.println(query);
        }
    }

    @Test
    public void test15() throws Exception {
        JpaDao dao = null;
        dao.updateCall(new JpaCallback<Object>() {

            public Object call(EntityManager manager) {
                return null;
            }
        });
    }

    @Test
    public void test16() throws Exception {
        System.out.println(String.valueOf(new Date()));
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(format.format(new Date()));
    }
}
