package prefwork.method;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.PropertyConfigurator;
import prefwork.datasource.BasicDataSource;

public class ILPBridge implements InductiveMethod {

    String path = "c:\\install\\ilp\\";

    private void getAttributes(BasicDataSource trainingDataset, Integer user, BufferedWriter out) {
        trainingDataset.setFixedUserId(user);
        trainingDataset.restart();
        String[] attributeNames = trainingDataset.getAttributesNames();
        @SuppressWarnings("unused") int size = attributeNames.length;
        List<Object> rec;
        try {
            while ((rec = trainingDataset.getRecord()) != null) {
                for (int i = 2; i < rec.size(); i++) {
                    out.write(attributeNames[i] + "(" + rec.get(1) + ", " + rec.get(i) + ").\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeHeader(BasicDataSource trainingDataset, Integer user) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(path + trainingDataset.getName()));
            out.write("");
            out.write(":- set(i,2).\n");
            out.write(":- set(clauselength,10).\n");
            out.write(":- set(minacc,0.6).\n");
            out.write(":- set(noise,3).\n");
            out.write(":- set(minscore,3).\n");
            out.write(":- set(minpos,3).\n");
            out.write(":- set(nodes,5000).\n");
            out.write(":- set(explore,true).\n");
            out.write(":- set(max_features,10).\n");
            out.write(":- set(test_pos,notytest_pos).\n");
            out.write(":- set(test_neg,notytest_neg).\n");
            out.write(":- modeh(1,rating(+object,#rating)).\n");
            for (String attrName : trainingDataset.getAttributesNames()) {
                out.write(":- modeb(1,+" + attrName + "(+object,#" + attrName + ")).\n");
            }
            out.write("\n");
            for (String attrName : trainingDataset.getAttributesNames()) {
                out.write(":- determination(rating/2,+" + attrName + "/2).\n");
            }
            getAttributes(trainingDataset, user, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int buildModel(BasicDataSource trainingDataset, Integer user) {
        writeHeader(trainingDataset, user);
        return 0;
    }

    private String getResult(BufferedReader stdOut, BufferedReader stdErr) throws IOException {
        String line = null;
        while (true) {
            if (stdOut.ready()) {
                line = stdOut.readLine();
                System.out.print(line + "\n");
                break;
            } else if (stdErr.ready()) {
                line = stdErr.readLine();
                System.out.print(line + "\n");
                break;
            }
        }
        return line;
    }

    private void read(BufferedReader stdOut, BufferedReader stdErr, String endLine) throws IOException {
        while (true) {
            char[] cbuf = new char[1024];
            if (stdOut.ready()) {
                stdOut.read(cbuf);
            } else if (stdErr.ready()) {
                stdErr.read(cbuf);
            }
            if (String.copyValueOf(cbuf).contains(endLine)) {
                break;
            }
        }
    }

    public Double classifyRecord(List<Object> record, Integer targetAttribute) {
        try {
            Process p = Runtime.getRuntime().exec("c:\\F\\devel\\ilp\\SWI\\bin\\plcon.exe", null, new File("c:\\install\\ilp\\"));
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            PrintStream stdIn = new PrintStream(new BufferedOutputStream(p.getOutputStream()), true);
            stdIn.write("consult('c:/install/ilp/aleph.pl').\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true.");
            stdIn.write("read_all(train).\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true");
            stdIn.write(".\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "");
            stdIn.write("induce.\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true.");
            stdIn.write("eastbound(west7).\n".getBytes());
            stdIn.flush();
            @SuppressWarnings("unused") String res = getResult(stdOut, stdErr);
            return 0.0D;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0D;
    }

    public void configClassifier(XMLConfiguration config, String section) {
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        ILPBridge b = new ILPBridge();
        b.classifyRecord(null, 2);
    }
}
