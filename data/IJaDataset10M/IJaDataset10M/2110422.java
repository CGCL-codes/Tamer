package yatiAI.imexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Vector;

public class YatiDataImport {

    private static ArrayList<Vector<Double>> in = new ArrayList<Vector<Double>>();

    private static ArrayList<Vector<Double>> out = new ArrayList<Vector<Double>>();

    public static void importData(String filename) {
        File file = new File(filename);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            StreamTokenizer st = new StreamTokenizer(bf);
            st.resetSyntax();
            st.wordChars(45, 90);
            st.whitespaceChars((int) ';', (int) ';');
            int index = 0;
            while (st.nextToken() != st.TT_EOF) {
                Vector<Double> inData = new Vector<Double>();
                for (int a = 0; a < 11; ++a) {
                    inData.add(Double.parseDouble(((Integer) Integer.parseInt(st.sval)).toString()));
                    st.nextToken();
                }
                Vector<Double> outData = new Vector<Double>();
                for (int a = 0; a < 2; ++a) {
                    outData.add(Double.parseDouble(((Integer) Integer.parseInt(st.sval)).toString()));
                    st.nextToken();
                }
                in.add(inData);
                out.add(outData);
                System.out.println(index++);
            }
            System.out.println(in.size());
            System.out.println(out.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Vector<Double>> getIn() {
        return in;
    }

    public static ArrayList<Vector<Double>> getOut() {
        return out;
    }
}
