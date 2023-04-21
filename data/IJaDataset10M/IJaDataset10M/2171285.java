package work;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SubRegions {

    private Vector<String> generatedKeywords;

    private Vector<String> instanceList;

    private Vector<Integer> containedByInstanceID;

    private ResultSet resultSet;

    private int instanceID;

    private int classID;

    private int subClassID;

    private boolean subRegionProperty = true;

    private boolean inverseOf_subRegionProperty;

    private int index = 1;

    protected void findAllSubRegions(Statement statement, String keyword, int instID, int clsID) {
        instanceID = instID;
        classID = clsID;
        generatedKeywords = new Vector<String>();
        generatedKeywords.add(keyword);
        instanceList = new Vector<String>();
        instanceList.add(keyword);
        containedByInstanceID = new Vector<Integer>();
        containedByInstanceID.add(null);
        try {
            do {
                if (subRegionProperty) {
                    resultSet = statement.executeQuery("SELECT ClassID_B " + "from subRegion " + "WHERE ClassID_A=" + classID);
                    while (resultSet.next()) {
                        subClassID = resultSet.getInt("ClassID_B");
                    }
                    resultSet = statement.executeQuery("SELECT * " + "from instances " + "WHERE ContainedIn_instanceID=" + instanceID + " AND ClassID=" + subClassID);
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        generatedKeywords.add(name);
                        instanceList.add(name);
                        int id = resultSet.getInt("ClassID");
                        int id2 = resultSet.getInt("ContainedIn_instanceID");
                        containedByInstanceID.add(id2);
                    }
                }
                if (instanceList.size() != index) {
                    String word = instanceList.elementAt(index);
                    if (word.contains("'")) {
                        word = word.replace("'", " ");
                    }
                    resultSet = statement.executeQuery("SELECT * " + "from instances " + "WHERE name=\'" + word + "\'" + "AND containedIn_instanceID=" + containedByInstanceID.elementAt(index));
                    while (resultSet.next()) {
                        instanceID = resultSet.getInt("instanceID");
                        classID = resultSet.getInt("classID");
                        String str = resultSet.getString("name");
                    }
                    resultSet = statement.executeQuery("SELECT * " + "from owlClass " + "WHERE owlclassID=" + classID);
                    while (resultSet.next()) {
                        subRegionProperty = resultSet.getBoolean("subRegion");
                        inverseOf_subRegionProperty = resultSet.getBoolean("inverseOf_subregion");
                        classID = resultSet.getInt("owlclassID");
                        String owlClass = resultSet.getString("name");
                    }
                    index++;
                }
            } while (instanceList.size() != index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getGeneratedKeywords() {
        return generatedKeywords;
    }
}
