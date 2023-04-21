package Controller;

import java.sql.*;
import java.util.ArrayList;
import Module.*;

public class ResourceController {

    private Database database = new Database();

    public boolean registerResource(Resource resource) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(database.getURL());
            String[] autogen = { "id" };
            PreparedStatement statement = conn.prepareStatement("Insert into resource values(DEFAULT,?,?)", autogen);
            statement.setString(1, resource.getName());
            statement.setInt(2, resource.getResourceTypeId());
            statement.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    public boolean editResource(Resource resource) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(database.getURL());
            PreparedStatement statement = conn.prepareStatement("Update resource " + "set resourceName=?, resourceTypeID=? where resourceId =?");
            statement.setString(1, resource.getName());
            statement.setInt(2, resource.getResourceTypeId());
            statement.setInt(3, resource.getId());
            statement.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    public ArrayList<Resource> getAllResources() {
        Connection conn = null;
        ArrayList<Resource> resources = new ArrayList<Resource>();
        try {
            conn = DriverManager.getConnection(database.getURL());
            PreparedStatement statement = conn.prepareStatement("select * from resource");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                resources.add(new Resource(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            conn.close();
            return resources;
        } catch (Exception e) {
            System.err.println(e);
        }
        return resources;
    }

    public ArrayList<Resource> getResourcesToProject(Project project) {
        Connection conn = null;
        ArrayList<Resource> resources = new ArrayList<Resource>();
        try {
            conn = DriverManager.getConnection(database.getURL());
            PreparedStatement statement = conn.prepareStatement("" + "select resource.resourceId, resource.resourceName, " + "resource.resourceTypeID from resource r" + "left join resourceProject rp on(resource.resourceI=resourceProject.resourceId)" + "left join project p on (project.projectId = resoruceProject.projectId)" + "where project.projectId=?");
            statement.setInt(1, project.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                resources.add(new Resource(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            conn.close();
            return resources;
        } catch (Exception e) {
            System.err.println(e);
        }
        return resources;
    }

    public boolean deleteResourcesToProject(Project project) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(database.getURL());
            PreparedStatement statement = conn.prepareStatement("delete from resourceProject where projectId = ?");
            statement.setInt(1, project.getId());
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }
}
