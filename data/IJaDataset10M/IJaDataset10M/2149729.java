package rsparql;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

public class SparqlQuery {

    private String[] colNames = new String[0];

    private ArrayList<QuerySolution> rows = new ArrayList<QuerySolution>();

    public void execute(String sparql, String uri) throws IOException {
        sparql = loadQuery(sparql);
        File file = new File(uri);
        if (file.exists()) {
            Model model = ModelFactory.createDefaultModel();
            FileManager.get().readModel(model, uri);
            Query query = QueryFactory.create(sparql);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            try {
                processResult(qexec.execSelect());
            } finally {
                qexec.close();
            }
        } else {
            String urlParameters = "query=" + URLEncoder.encode(sparql, "UTF-8");
            URL endPoint = new URL(uri);
            InputStream inputStream = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) endPoint.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept", "application/sparql-results+xml");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                inputStream = connection.getInputStream();
                if (connection.getResponseCode() != 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    String decodedString;
                    while ((decodedString = in.readLine()) != null) {
                        System.out.println(decodedString);
                    }
                } else {
                    ResultSet resultSet = ResultSetFactory.fromXML(inputStream);
                    processResult(resultSet);
                }
            } finally {
                if (inputStream != null) inputStream.close();
            }
        }
    }

    private String loadQuery(String sparql) throws IOException {
        File file = new File(sparql);
        if (file.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                return Charset.defaultCharset().decode(bb).toString();
            } finally {
                if (stream != null) stream.close();
            }
        } else {
            return sparql;
        }
    }

    private void processResult(ResultSet results) {
        List<String> cols = new ArrayList<String>();
        rows = new ArrayList<QuerySolution>();
        boolean readingColumns = true;
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            rows.add(solution);
            if (readingColumns) {
                Iterator<String> varNames = solution.varNames();
                while (varNames.hasNext()) {
                    String column = varNames.next();
                    cols.add(column);
                }
                readingColumns = false;
            }
        }
        colNames = cols.toArray(new String[cols.size()]);
    }

    public int rows() {
        return rows.size();
    }

    public int cols() {
        return colNames.length;
    }

    public String[] colNames() {
        return colNames;
    }

    public Object cell(int row, int column) {
        String colName = colNames[column];
        QuerySolution rowData = rows.get(row);
        RDFNode node = rowData.get(colName);
        if (node.isLiteral()) {
            return node.asLiteral().getLexicalForm();
        } else if (node.isResource()) {
            return node.asResource().getURI();
        } else {
            return null;
        }
    }

    public void clear() {
        rows.clear();
        rows = null;
        colNames = null;
    }
}
