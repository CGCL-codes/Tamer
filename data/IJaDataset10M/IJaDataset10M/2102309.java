package subsearch.index;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import subsearch.graph.Graph;
import subsearch.index.features.extractor.ConnectivityExtractor;
import subsearch.index.util.LabelCounter;

public class ConnectivityIndexFile extends FileIndex {

    public ConnectivityIndexFile(String fileName) {
        super(null, fileName);
    }

    public void addGraph(int id, Graph g) {
        try {
            if (indexFileOOS == null) {
                indexFileOOS = createIndexOOS(true);
            }
            ConnectivityExtractor ce = new ConnectivityExtractor(g, 5);
            ce.extractFeatures();
            indexFileOOS.writeObject(ce.getResult());
            indexFileOOS.writeInt(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LinkedList<Integer> findCandidates(Graph g) {
        LinkedList<Integer> candidates = new LinkedList<Integer>();
        try {
            ConnectivityExtractor ce = new ConnectivityExtractor(g, 5);
            ce.extractFeatures();
            LabelCounter[] plc = ce.getResult();
            ObjectInputStream ois = createIndexOIS();
            int c = 0;
            try {
                while (true) {
                    c++;
                    LabelCounter[] hlc = (LabelCounter[]) ois.readObject();
                    int id = (int) ois.readInt();
                    boolean compatible = true;
                    for (int i = 0; i < 5; i++) {
                        if (!hlc[i].contains(plc[i])) {
                            compatible = false;
                            break;
                        }
                    }
                    if (compatible) {
                        candidates.add(id);
                    }
                }
            } catch (EOFException e) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return candidates;
    }

    public String toString() {
        return "Connectivity File";
    }
}
