package com.stromberglabs.visual.search;

import java.io.Serializable;
import java.util.List;
import com.stromberglabs.cluster.Clusterable;
import com.stromberglabs.tree.KMeansTree;
import com.stromberglabs.util.SizedPriorityQueue;

public interface ImageWordIndex extends Serializable {

    public SizedPriorityQueue<Score> findClosest(List<Clusterable> points);

    public SizedPriorityQueue<Score> findClosest(List<Clusterable> points, int maxNumScore);

    public int numWords();

    public KMeansTree getTree();
}
