package server.shortestPathBetweenness;

import algorithms.centralityAlgorithms.betweenness.brandes.GroupAPI;
import algorithms.centralityAlgorithms.betweenness.brandes.preprocessing.DataWorkshop;
import javolution.util.FastList;
import javolution.util.Index;
import server.common.DataBase;

public class GroupController {

    public static final String ALIAS = "Group";

    /** Creates a Group instance with the given candidate vertices and the given dataworkshop.
	 * @param dataworkshop index
	 * @param array of candidates
	 * @return group index in the database */
    public int create(int dwID, Object[] candidatesObj) {
        FastList<Index> candidates = new FastList<Index>(candidatesObj.length);
        for (int i = 0; i < candidatesObj.length; i++) candidates.add(Index.valueOf(((Integer) candidatesObj[i]).intValue()));
        GroupAPI group = new GroupAPI((DataWorkshop) DataBase.getAlgorithm(dwID), candidates);
        return DataBase.putAlgorithm(group);
    }

    /** Returns the group members of the given group.
	 * @param group index
	 * @return array of vertices */
    public Object[] getMembers(int algID) {
        return ((GroupAPI) DataBase.getAlgorithm(algID)).getMembers();
    }

    /** Returns the betweenness value of the given vertex in the given group.
	 * @param group index
	 * @param vertex
	 * @return betweenness value */
    public double getBetweenness(int algID, int v) {
        return ((GroupAPI) DataBase.getAlgorithm(algID)).getBetweenness(v);
    }

    /** Adds the given vertex to the given group.
	 * @param group index
	 * @param vertex */
    public void add(int algID, int v) {
        ((GroupAPI) DataBase.getAlgorithm(algID)).add(v);
    }

    /** Adds the given vertices to the given group.
	 * @param group index
	 * @param array of vertices */
    public void add(int algID, int[] vertices) {
        ((GroupAPI) DataBase.getAlgorithm(algID)).add(vertices);
    }

    /** Returns the group betweenness value of all vertices in the given group.
	 * @param group index
	 * @return betweenness value */
    public double getBetweenness(int algID) {
        return ((GroupAPI) DataBase.getAlgorithm(algID)).getBetweenness();
    }

    /** Returns k vertices with the highest betweenness values in the given group.
	 * @param group index
	 * @param k
	 * @return array of k vertices */
    public Object[] getTopK(int algID, int k) {
        return ((GroupAPI) DataBase.getAlgorithm(algID)).getTopK(k);
    }

    /** Removes the given group from the Database maps.
	 * @param group index
	 * @return 0 */
    public void close(int algID) {
        ((GroupAPI) DataBase.getAlgorithm(algID)).close();
        DataBase.releaseAlgorithm(algID);
    }
}
