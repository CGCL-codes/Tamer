package fhj.itm05.seminarswe.web;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fhj.itm05.seminarswe.database.CategoryDAOHsqldb;
import fhj.itm05.seminarswe.domain.*;

/**
 * Handles user inputs
 * @author Mike Ulm / Little Help From DirtyHarry
 * @version 1.0
 */
public class CategoryAnalysisController implements Controller {

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
        Map<String, Object> categories = new HashMap<String, Object>();
        String action = request.getParameter("action");
        String description = request.getParameter("description");
        String name = request.getParameter("name");
        String user = request.getParameter("user");
        String idText = request.getParameter("id");
        int id = -1;
        System.out.println("action:" + action + " name:" + name + " desc:" + description + " user:" + user);
        if ((checkNull(action, name, description, user) == true) && (!action.equals("delete"))) {
            action = "error.null";
        }
        if (action.equals("edit") || action.equals("delete")) {
            id = checkID(idText);
            if (id == -1) {
                action = "error.id.category";
            }
        }
        if (action.equals("insert")) {
            Category category = new Category(user, name, description);
            if (CategoryDAOHsqldb.getInstance().insertCategory(category) == true) {
                action = "category successfully inserted";
            } else {
                action = "error.sql";
            }
        } else if (action.equals("edit")) {
            Category category = new Category(id, user, name, description);
            if (CategoryDAOHsqldb.getInstance().editCategory(category) == true) {
                action = "category successfully edited";
            } else {
                action = "error.sql";
            }
        } else if (action.equals("delete")) {
            if (CategoryDAOHsqldb.getInstance().deleteCategory(id) == true) {
                action = "category successfully deleted";
            } else {
                action = "error.sql";
            }
        }
        categories.put("result", getError(action));
        return categories;
    }

    /**
	 * Checks if something is null or empty
	 * @param action
	 * @param name
	 * @param description
	 * @param user
	 * @return isNull true if one ore more fields are null or empty
	 */
    private boolean checkNull(String action, String name, String description, String user) {
        boolean isNull;
        if ((action == null || action.trim().isEmpty()) || (name == null || name.trim().isEmpty()) || (description == null || description.trim().isEmpty()) || (user == null || user.trim().isEmpty())) {
            isNull = true;
        } else {
            isNull = false;
        }
        return isNull;
    }

    /**
	 * Checks and converts IDs
	 * @param idText
	 * @return id
	 */
    private int checkID(String idText) {
        int id = -1;
        if (idText != null) {
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException nfe) {
                System.out.println("ERROR: ID format invalid");
            }
        }
        return id;
    }

    /**
	 * Returns a suitable message
	 * @param error
	 * @return message
	 */
    private String getError(String error) {
        if (error.equals("error.null")) return "One or more fields were empty. Fill in all fields."; else if (error.equals("error.id.category")) return "Category id is not valid. Don't touch the url!"; else if (error.equals("error.sql")) return "Database transaction failed. Database corrupt?"; else return error;
    }
}
