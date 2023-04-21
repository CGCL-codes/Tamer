package edu.fudan.cse.medlab.event.extraction;

import java.util.ArrayList;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.apache.log4j.Logger;

/**
 * ���� rmq �����ҵ����ڵ����С��������
 * 
 * @author yiminghe
 * 
 */
public class LCA {

    static Logger logger = Logger.getLogger(LCA.class);

    private int[][] M;

    /**
	 * �ڵ��������˳��
	 */
    private ArrayList<Node> seqence = new ArrayList<Node>();

    /**
	 * ��Ӧ����˳��Ĳ����Ϣ
	 */
    private ArrayList<Integer> seqenceLevel = new ArrayList<Integer>();

    public LCA(Node root) {
        preprocessForLca(root, 0);
        processForRmq();
    }

    /**
	 * ��������������������������Ϣ:������˳��ͽ�����.
	 * 
	 * @param d
	 * @param level
	 */
    private void preprocessForLca(Node d, int level) {
        seqence.add(d);
        seqenceLevel.add(level);
        NodeList nl = d.getChildren();
        if (nl != null) for (int i = 0; i < nl.size(); i++) {
            if (nl.elementAt(i) == null) continue;
            preprocessForLca(nl.elementAt(i), level + 1);
            seqence.add(d);
            seqenceLevel.add(level);
        }
    }

    /**
	 * ����RMQ��ST�㷨
	 * <p/>
	 * Sparse Table (ST) algorithm A better approach is to preprocess RMQ for
	 * sub arrays of length 2^k using dynamic programming. We will keep an array
	 * M[0, N-1][0, logN] where M[i][j] is the index of the minimum value in the
	 * sub array starting at i having length 2^j.
	 */
    private void processForRmq() {
        int col = (int) Math.ceil(Math.log(this.seqence.size()) / Math.log(2));
        M = new int[this.seqence.size()][col];
        int i, j;
        for (i = 0; i < this.seqence.size(); i++) M[i][0] = i;
        for (j = 1; (1 << j) <= this.seqence.size(); j++) {
            for (i = 0; i + (1 << j) - 1 < this.seqence.size(); i++) if (seqenceLevel.get(M[i][j - 1]) < seqenceLevel.get(M[i + (1 << (j - 1))][j - 1])) M[i][j] = M[i][j - 1]; else M[i][j] = M[i + (1 << (j - 1))][j - 1];
        }
    }

    /**
	 * LCA && RMQ algorithm
	 * 
	 * @param n1
	 *            �ڵ�1
	 * @param n2
	 *            �ڵ�2
	 * @return ��С��������
	 */
    public Node getMinumParent(Node n1, Node n2) {
        if (n1 == n2) return n1;
        if (n1.getParent() == n2) return n2;
        if (n2.getParent() == n1) return n1;
        int nIntIndex1 = -1;
        int nIntIndex2 = -1;
        for (int i = 0; i < this.seqence.size(); i++) {
            if (this.seqence.get(i) == n1 && nIntIndex1 == -1) nIntIndex1 = i;
            if (this.seqence.get(i) == n2 && nIntIndex2 == -1) nIntIndex2 = i;
        }
        if (nIntIndex1 > nIntIndex2) {
            int t = nIntIndex1;
            nIntIndex1 = nIntIndex2;
            nIntIndex2 = t;
        }
        int k = (int) Math.ceil(Math.log(Math.abs(nIntIndex2 - nIntIndex1)) / Math.log(2)) - 1;
        if (this.seqenceLevel.get(this.M[nIntIndex1][k]) > this.seqenceLevel.get(this.M[nIntIndex2 - (1 << k)][k])) return this.seqence.get(this.M[nIntIndex2 - (1 << k)][k]); else return this.seqence.get(this.M[nIntIndex1][k]);
    }
}
