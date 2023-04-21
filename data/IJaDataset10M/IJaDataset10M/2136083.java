package EA.Graph;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import EA.LocalSearch.Genetic.Solution;

@SuppressWarnings("serial")
public class Graph extends ArrayList<Node> {

    private static final int NUMBER_OF_NODES = 500;

    public Graph() {
        ;
    }

    /**
	 * Constructor
	 * Load Graph information from file
	 */
    @SuppressWarnings("deprecation")
    public Graph(File f) {
        super();
        System.out.println("> Reading graph from file: " + f.getName());
        this.ensureCapacity(Graph.NUMBER_OF_NODES);
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            for (int i = 0; i < Graph.NUMBER_OF_NODES; i++) this.add(new Node(i));
            while (dis.available() != 0) {
                StringTokenizer strTok = new StringTokenizer(dis.readLine(), " ");
                int currentId = (Integer.parseInt(strTok.nextToken()) - 1);
                strTok.nextToken();
                int neighbourCount = Integer.parseInt(strTok.nextToken());
                Node currentNode = this.get(currentId);
                for (int i = 0; i < neighbourCount; i++) {
                    int neighbourId = (Integer.parseInt(strTok.nextToken()) - 1);
                    currentNode.addNeighbour(this.get(neighbourId));
                }
            }
        } catch (IOException e) {
            System.err.print(e.toString());
            Thread.currentThread().stop();
        }
        System.out.println("> Done. " + this.size() + " nodes added.");
    }

    /**
	 * Create a bit representation of the node partitioning.
	 * 0 = Partition Alpha
	 * 1 = Partition Beta.
	 * @return byte[] 
	 */
    public Solution createBitRepresentation() {
        Solution sol = new Solution();
        sol.representation = new int[this.size()];
        for (int i = 0; i < sol.representation.length; i++) {
            sol.representation[i] = (this.get(i).getPartition() == Partition.Alpha) ? 0 : 1;
        }
        PartitionBucket temp = this.getPartitionBucket(Partition.Alpha);
        sol.cutSize = temp.getCutSize();
        return sol;
    }

    public void setSolution(Solution sol) {
        for (int i = 0; i < sol.representation.length; i++) {
            if (sol.representation[i] == 0) this.get(i).setPartition(Partition.Alpha); else this.get(i).setPartition(Partition.Beta);
        }
    }

    public void setSolution(PartitionBucket a, PartitionBucket b) {
        for (Node node : a) this.get(this.indexOf(node)).setPartition(Partition.Alpha);
        for (Node node : b) this.get(this.indexOf(node)).setPartition(Partition.Beta);
    }

    /**
	 * Gets a bucket of Nodes from the same partition
	 */
    public PartitionBucket getPartitionBucket(Partition partition) {
        PartitionBucket pb = new PartitionBucket();
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getPartition() == partition) {
                pb.add(this.get(i));
            }
        }
        pb.sort();
        return pb;
    }

    public void calculateGains() {
        for (Node node : this) node.calculateGain();
    }
}
