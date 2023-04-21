package UniKrak.Search;

import UniKrak.Graph.*;

public class SearchRoom {

    public String city;

    public String address;

    public String building;

    public String number;

    public int index;

    public SearchRoom(String city, String address, String building, String number, int index) {
        this.city = city;
        this.address = address;
        this.building = building;
        this.number = number;
        this.index = (index < 0) ? -1 : index;
    }

    public static SearchRoom[] roomSearch(String query, Graph g, boolean perfectOnly) {
        SearchRoom[] results;
        if (g == null) return null;
        int[] resIndex = g.findNodes(query, 1, perfectOnly);
        if (resIndex == null) return new SearchRoom[0];
        results = new SearchRoom[resIndex.length];
        for (int i = 0; i < resIndex.length; i++) {
            results[i] = new SearchRoom("9220 Aalborg &Oslash;", g.buildingNames[g.nodeList[resIndex[i]].buildingId], "A", g.nodeList[resIndex[i]].description, resIndex[i]);
        }
        return results;
    }
}
