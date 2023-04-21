package example.pGrid;

import peersim.core.*;
import peersim.config.Configuration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.*;

/**
 * @author Tomasz
 * 
 */
public class InitiateData implements Control {

    private static final String PAR_IDLENGTH = "idLength";

    private static final String PAR_PROT = "protocol";

    private int pid = 0;

    int idLength = 0;

    public InitiateData(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /**
 *initialisiert bei allen knoten dataID mit zufahligen zahlen 
 *mit angegebene bit lange
 * @throws FileNotFoundException 
 *
 * 
 */
    public boolean execute() {
        TestPGTree tree = new TestPGTree();
        String filename = "/home/tomasz/P2P/P-Grid/P-Grid/resources/PGridTree.ini";
        BufferedReader reader = null;
        try {
            reader = tree.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TreeNode mTreeRoot = tree.loadTreeFromFile(reader, filename);
        tree.createKeys("/home/tomasz/tomasztest", mTreeRoot);
        FileInputStream in = null;
        try {
            in = new FileInputStream("/home/tomasz/tomasztest");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(in));
        String[] args;
        for (int i = 0; i < Network.size(); i++) {
            Node node = (Node) Network.get(i);
            PGProtocol cp = (PGProtocol) node.getProtocol(pid);
            String prefix = null;
            try {
                prefix = reader2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            args = this.getKeyAndData(prefix);
            cp.data = args[0];
            cp.key = args[1];
        }
        for (int i = 0; i < Network.size(); i++) {
            Node node = (Node) Network.get(i);
            PGProtocol cp = (PGProtocol) node.getProtocol(pid);
            System.out.println("data " + cp.data);
            System.out.println(" key " + cp.key);
        }
        return false;
    }

    public String[] getKeyAndData(String line) {
        String[] a = line.split("\\p{Space}");
        return a;
    }
}
