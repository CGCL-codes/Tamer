package org.ictclas4j.segment;

import java.util.ArrayList;
import org.ictclas4j.bean.Queue;
import org.ictclas4j.bean.QueueNode;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.utility.Utility;

/**
 * N-���·��
 * 
 * @author sinboy
 * @since 2007.5.17 updated
 */
public class NShortPath {

    private int pathCount;

    private SegGraph biSegGraph;

    private double[][] pathWeight;

    private Queue[] parent;

    private int vertex;

    public NShortPath(SegGraph bsg, int pathCount) {
        this.biSegGraph = bsg;
        this.pathCount = pathCount;
        if (bsg != null && bsg.getSize() > 0 && pathCount > 0) {
            vertex = bsg.getMaxCol() + 1;
            if (bsg.getMaxRow() + 1 > vertex) vertex = bsg.getMaxRow() + 1;
            parent = new Queue[vertex];
            pathWeight = new double[vertex][pathCount];
            for (int i = 0; i < pathWeight.length; i++) for (int j = 0; j < pathWeight[i].length; j++) pathWeight[i][j] = Utility.INFINITE_VALUE;
            for (int i = 0; i < vertex; i++) {
                parent[i] = new Queue();
            }
        }
    }

    /**
	 * ���б���ͼ�?����ÿһ����Ȩ����С��ȡ������
	 * 
	 */
    private void shortPath() {
        int preNode = -1;
        double weight = 0;
        if (biSegGraph != null) {
            for (int cur = 1; cur < vertex; cur++) {
                ArrayList<SegNode> colSgs = biSegGraph.getNodes(cur, true);
                if (colSgs == null || colSgs.size() == 0) return;
                Queue queWork = new Queue();
                for (SegNode seg : colSgs) {
                    preNode = seg.getRow();
                    weight = seg.getValue();
                    if (preNode == 0) {
                        queWork.push(new QueueNode(preNode, 0, weight));
                    } else {
                        if (pathWeight[preNode][0] != Utility.INFINITE_VALUE) queWork.push(new QueueNode(preNode, 0, weight + pathWeight[preNode][0]));
                    }
                }
                QueueNode minNode = null;
                int pathIndex = 0;
                while ((minNode = queWork.pop()) != null && pathIndex < pathCount) {
                    pathWeight[cur][pathIndex] = minNode.getWeight();
                    parent[cur].push(minNode);
                    pathIndex++;
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getPaths() {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> onePath = null;
        Queue queResult = null;
        int curNode, curIndex = 0;
        int pathIndex = 0;
        shortPath();
        if (vertex > 0) {
            queResult = new Queue();
            queResult.push(new QueueNode(vertex - 1, 0, 0));
            curNode = vertex - 1;
            curIndex = 0;
            while (!queResult.isEmpty()) {
                while (curNode > 0) {
                    QueueNode qn = parent[curNode].pop(false);
                    if (qn == null) qn = parent[curNode].top();
                    if (qn != null) {
                        curNode = qn.getParent();
                        curIndex = qn.getIndex();
                    }
                    if (curNode > 0) queResult.push(new QueueNode(curNode, curIndex, 0));
                }
                if (curNode == 0) {
                    QueueNode qn = null;
                    onePath = new ArrayList<Integer>();
                    onePath.add(curNode);
                    while ((qn = queResult.pop(false)) != null) onePath.add(qn.getParent());
                    result.add(onePath);
                    queResult.resetIndex();
                    pathIndex++;
                    if (pathIndex == pathCount) break;
                    while ((qn = queResult.pop()) != null) {
                        curNode = qn.getParent();
                        QueueNode next = parent[curNode].pop(false);
                        if (next != null) {
                            curNode = next.getParent();
                            next.setWeight(0);
                            queResult.push(qn);
                            queResult.push(next);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public int[] getPaths(int index) {
        int[] rs = null;
        ArrayList<ArrayList<Integer>> result = getPaths();
        if (result != null && index < result.size()) {
            rs = new int[result.get(index).size()];
            int i = 0;
            for (int p : result.get(index)) rs[i++] = p;
        }
        return rs;
    }

    public void printPath(ArrayList<ArrayList<Integer>> paths) {
        if (paths != null) {
            for (int i = 0; i < paths.size(); i++) {
                String result = "path[" + i + "]:";
                for (int j : paths.get(i)) {
                    result += j + ",";
                }
            }
        }
    }

    public int getPathCount() {
        return pathCount;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }
}
