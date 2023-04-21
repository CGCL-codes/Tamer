package stixar.graph.attr;

/**
   Interface for attribute managers to communicate
   with edge attribute data.
 */
public interface EdgeData {

    /**
       Ensure the attribute set has capacity for storing attributes
       for <tt>cap</tt> edges.
       @param cap the new capacity for the set, which is greater than
       the capacity specified in the last call to either this method
       or {@link #shrink}.
     */
    public void grow(int cap);

    /**
       Shrink the capacity to size <tt>cap</tt>, and 
       compact the attributes according to a given permutation.
       @param cap the new capacity.
       @param fillPerm a permutation of the node ids,  with the following
       properties:
       <ol>
       <li>If there is an edge with id <tt>i</tt>, then <tt>fillPerm[i] &gt;= 0</tt>.
       </li>
       <li>If there is no edge with id <tt>i</tt> then <tt>fillPerm[i] = -1</tt>. </li>
       <li>Monotonicity: <tt>fillPerm[j] &gt; fillPerm[i] &lt;=&gt; j &gt; i</tt> 
       for every <tt>i</tt> and <tt>j</tt> such that there is an edge with id <tt>i</tT>
       and there is an edge with id <tt>j</tt>.</li>
       </ol>
     */
    public void shrink(int cap, int[] fillPerm);

    /**
       Clear all the entries in the map.
     */
    public void clear();
}
