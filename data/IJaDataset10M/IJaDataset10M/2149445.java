package org.wat.wcy.isi.mmazur.bp.io.xpdl.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.wat.wcy.isi.mmazur.bp.io.model.IdDesc;
import org.wat.wcy.isi.mmazur.bp.io.model.Node;
import org.wat.wcy.isi.mmazur.bp.io.model.Transition;
import org.wat.wcy.isi.mmazur.bp.io.model.enums.NodeType;
import org.wat.wcy.isi.mmazur.bp.io.model.enums.TransitionType;
import org.wat.wcy.isi.mmazur.bp.io.model.transport.BusinessProcessMatrix;

/**
 * @author mma
 *
 */
public class XPDLGeneratorTest {

    private IdGenerator idGen;

    public static class StringGenerator {

        public static String generateString(int n) {
            char[] pw = new char[n];
            int c = 'A';
            int r1 = 0;
            for (int i = 0; i < n; i++) {
                r1 = (int) (Math.random() * 3);
                switch(r1) {
                    case 0:
                        c = '0' + (int) (Math.random() * 10);
                        break;
                    case 1:
                        c = 'a' + (int) (Math.random() * 26);
                        break;
                    case 2:
                        c = 'A' + (int) (Math.random() * 26);
                        break;
                }
                pw[i] = (char) c;
            }
            return new String(pw);
        }
    }

    public XPDLGeneratorTest() {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[1]);
        new XPDLGeneratorTest().generate(args[0], n);
        System.out.println("Wygenerowano");
    }

    private void generate(String fileName, int n) {
        System.out.println("Rozpocz�to genreowanie danych testowych");
        System.out.println("n = " + n);
        System.out.println("plik: " + fileName);
        idGen = new IdGenerator();
        Random rand = new Random();
        List<Node> nodes = new ArrayList<Node>();
        Node pool = new Node();
        pool.setId(new IdDesc(idGen.next()));
        pool.setName("TestProces");
        pool.setLane(new Node());
        pool.setType(NodeType.POOL);
        nodes.add(pool);
        for (int i = 1; i < n; i++) {
            Node node = generateNode();
            node.setLane(new Node());
            node.getLane().setId(new IdDesc(pool.getId().toString()));
            nodes.add(node);
        }
        Transition transitions[][] = new Transition[n][n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                Transition tr = new Transition();
                if (rand.nextInt(30) == 3) {
                }
                transitions[i][j] = tr;
            }
        }
        BusinessProcessMatrix bpm = new BusinessProcessMatrix();
        bpm.setNodesList(nodes);
        bpm.setNodesMatrix(transitions);
        XPDLGenerator gen = new XPDLGenerator();
        String str = gen.generateXml("TestProcess", bpm);
        try {
            IOUtils.write(str, new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node generateNode() {
        Node node = new Node();
        node.setId(new IdDesc(idGen.next()));
        node.setName(StringGenerator.generateString(10));
        Random rand = new Random();
        int t = rand.nextInt(NodeType.values().length - 2);
        node.setType(NodeType.values()[t]);
        return node;
    }
}
