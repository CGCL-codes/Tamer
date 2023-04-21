package be.kuleuven.peno3.mobiletoledo.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/ScheduleHandler")
public class ScheduleServer {

    protected DatabaseManager manager = DatabaseManager.getInstance();

    @GET
    @Path("/getCourseActivities")
    @Produces("application/json")
    public String getCourseActivities(@QueryParam("beginDate") String beginDate, @QueryParam("endDate") String endDate, @QueryParam("group") String group, @QueryParam("course") int course, @QueryParam("study_programme") String study_programme) {
        String query = "SELECT * FROM CourseActivities JOIN Courses ON Courses.id=CourseActivities.course_id";
        query += " WHERE date_begin>='" + beginDate + "' and date_end<='" + endDate + "'";
        query += " and study_programme='" + study_programme + "'";
        if (!group.equals("none")) query += " and `group` ='" + group + "'";
        if (course != 0) query += " and course_id ='" + course + "'";
        query += " ORDER BY date_begin ASC";
        System.out.println(query);
        String result = queryForCourseActivities(query);
        manager.disconnect();
        System.out.println(result);
        return result;
    }

    @SuppressWarnings("static-access")
    private String queryForCourseActivities(String query) {
        JsonArray courseActivities = new JsonArray();
        System.out.println("testqueryForCourseActivities1");
        ResultSet rs = manager.query(query);
        System.out.println(rs);
        Gson gson = new Gson();
        try {
            while (rs.next()) {
                JsonObject courseActivity = (JsonObject) gson.toJsonTree(manager.getColumnValues(rs));
                addTableRows(courseActivity, "course_id", "Courses", "course");
                addTableRows(courseActivity, "poi_id", "Pois", "poi");
                JsonObject poi = courseActivity.get("poi").getAsJsonObject();
                addTableRows(poi, "location_id", "Locations", "location");
                courseActivities.add(courseActivity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String asString = courseActivities.toString();
        return asString;
    }

    private void addTableRows(JsonObject jsonObject, String jsonElementNameIn, String tableName, String jsonElementNameOut) {
        JsonElement jsonElement = jsonObject.get("" + jsonElementNameIn);
        System.out.println(jsonElement.isJsonNull());
        if (!jsonElement.isJsonNull()) {
            int id = jsonElement.getAsInt();
            String query = "SELECT * FROM " + tableName + " WHERE id='" + id + "'";
            System.out.println(query);
            JsonArray result = querySimpleTable(query);
            if (result.size() > 0) {
                jsonObject.add(jsonElementNameOut, result.get(0));
            } else {
                jsonObject.add(tableName, null);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
    private JsonArray querySimpleTable(String query) {
        Vector tableRows = new Vector();
        ResultSet rs = manager.query(query);
        Gson gson = new Gson();
        try {
            while (rs.next()) {
                tableRows.add(manager.getColumnValues(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (JsonArray) gson.toJsonTree(tableRows);
    }
}
