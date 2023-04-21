package app.controllers;

import org.javalite.activeweb.AppController;

/**
 * @author Igor Polevoy
 */
public class RequestParamsController extends AppController {

    public void index() {
        view("name", param("name"));
    }

    public void multi() {
        view("first_name", param("first_name"));
        view("last_name", param("last_name"));
    }

    public void multiValues() {
        view("states", params("states"));
    }

    public void singleMultiValues() {
        view("states", params("states"));
        view("name", param("name"));
    }
}
