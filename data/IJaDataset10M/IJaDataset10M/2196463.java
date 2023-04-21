package be.kuleuven.peno3.mobiletoledo.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/AnnouncementHandler")
public class AnnouncementServer {

    protected DatabaseManager manager = DatabaseManager.getInstance();

    @GET
    @Path("/getAnnouncements")
    @Produces("application/json")
    public String getCourses(@QueryParam("course_id") int course_id) {
        String query = "SELECT * FROM Announcements";
        query += " WHERE course_id= '" + course_id + "'";
        System.out.println(query);
        String result = queryForCourses(query);
        manager.disconnect();
        return result;
    }

    @GET
    @Path("/getAnnouncement")
    @Produces("application/json")
    public String getCourse(@QueryParam("announcement_id") int announcement_id) {
        String query = "SELECT * FROM Announcements";
        query += " WHERE id= '" + announcement_id + "'";
        System.out.println(query);
        String result = queryForCourses(query);
        manager.disconnect();
        return result;
    }

    private String queryForCourses(String query) {
        JsonArray announcements = new JsonArray();
        ResultSet rs = manager.query(query);
        Gson gson = new Gson();
        try {
            while (rs.next()) {
                JsonObject announcement = (JsonObject) gson.toJsonTree(DatabaseManager.getColumnValues(rs));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String asString = announcements.toString();
        return asString;
    }
}
