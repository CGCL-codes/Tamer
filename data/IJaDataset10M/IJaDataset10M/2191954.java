package org.fgraph.io;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import org.progeeks.util.*;
import org.progeeks.util.TemplateExpressionProcessor.TemplateExpression;
import org.progeeks.util.log.Log;
import org.fgraph.*;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.mem.HashTripleStore;
import org.fgraph.tstore.sql.SqlStoreFactory;
import org.fgraph.tstore.sql.ProfileStats;
import org.fgraph.util.JdbcConnections;

/**
 *  Uses a supplied regex pattern to parse lines into
 *  edges.
 *
 *  @version   $Revision: 562 $
 *  @author    Paul Speed
 */
public class LinePatternLoader {

    private FGraph graph;

    private Pattern linePattern;

    private Map<String, TemplateExpression> tailMap = new HashMap<String, TemplateExpression>();

    private Map<String, TemplateExpression> headMap = new HashMap<String, TemplateExpression>();

    private TemplateExpression edgeType;

    private Map<String, Node> cache = new HashMap<String, Node>();

    public LinePatternLoader(FGraph graph) {
        this.graph = graph;
    }

    public void setLinePattern(String pattern) {
        linePattern = Pattern.compile(pattern);
    }

    public void setMapping(String key, String expression) {
        if (key.startsWith("tail.")) {
            tailMap.put(key.substring("tail.".length()), new TemplateExpression(expression));
        } else if (key.startsWith("head.")) {
            headMap.put(key.substring("head.".length()), new TemplateExpression(expression));
        } else if (key.startsWith("edge.")) {
        } else if (key.equals("edgeType")) {
            edgeType = new TemplateExpression(expression);
        } else {
            throw new RuntimeException("Invalid key[" + key + "], must start with 'tail.' or 'head.'");
        }
    }

    protected Node getNode(String key, String value) {
        String id = key + "=" + value;
        Node n = cache.get(id);
        if (n != null) return n;
        n = graph.newNode();
        n.add(key, value);
        cache.put(id, n);
        return n;
    }

    protected String eval(TemplateExpression exp) {
        Object o = TemplateExpressionProcessor.evaluateTemplateExpression(exp);
        if (o == null) return null;
        return String.valueOf(o);
    }

    public void load(Reader in) throws IOException {
        BufferedReader bIn;
        if (in instanceof BufferedReader) bIn = (BufferedReader) in; else bIn = new BufferedReader(in);
        String line = null;
        int count = 0;
        Map<String, String> entry = new HashMap<String, String>();
        while ((line = bIn.readLine()) != null) {
            Matcher m = linePattern.matcher(line);
            if (!m.matches()) {
                System.out.println("Skipping:" + line);
                continue;
            }
            System.out.print(count + "  -> ");
            for (int i = 1; i <= m.groupCount(); i++) {
                System.out.print("[" + m.group(i) + "]");
                entry.put(String.valueOf(i), m.group(i));
            }
            System.out.println();
            Node tail = null;
            if (tailMap.size() == 0) {
                tail = getNode("id", m.group(1));
            } else {
                throw new UnsupportedOperationException("Property mapping not implemented.");
            }
            Node head = null;
            if (headMap.size() == 0) {
                head = getNode("id", m.group(2));
            } else {
                throw new UnsupportedOperationException("Property mapping not implemented.");
            }
            String type = null;
            if (edgeType == null) type = "line"; else type = eval(edgeType);
            tail.addEdge(head, type);
            count++;
        }
    }

    public static void main(String... args) throws Exception {
        Log.initialize();
        if (args == null || args.length == 0) {
            System.out.println("Usage: LinePatternLoader <jdbc info> <user> <password> <pattern> [property mappings] <file>...");
            return;
        }
        List<String> argList = new ArrayList<String>(Arrays.asList(args));
        Connection conn = JdbcConnections.getConnection(argList);
        conn.setAutoCommit(false);
        FGraph g = DefaultGraph.create(new SqlStoreFactory(conn));
        NanoTimer timer = new NanoTimer();
        timer.start();
        LinePatternLoader loader = new LinePatternLoader(g);
        if (argList.size() == 0) {
            System.out.println("Missing <pattern> and file arguments.");
            return;
        }
        loader.setLinePattern(argList.remove(0));
        while (argList.size() > 0) {
            String s = argList.get(0);
            int split = s.indexOf('=');
            if (split < 0) break;
            argList.remove(0);
            loader.setMapping(s.substring(0, split), s.substring(split + 1));
        }
        for (String s : argList) {
            loader.load(new BufferedReader(new FileReader(s), 16384));
        }
        conn.commit();
        timer.stop();
        System.out.println("Total execution time:" + timer.getAccumulatedTimeMillis() + " ms");
        System.out.println("Break down:");
        ProfileStats.printStats();
        g.close();
    }
}
