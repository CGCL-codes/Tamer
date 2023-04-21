package com.simplefun;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONWriter;

@SuppressWarnings("serial")
public class ListAllServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            tryToRespond(resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void tryToRespond(HttpServletResponse resp) throws IOException, JSONException {
        resp.setContentType("text/plain");
        resp.getWriter().println("");
        JSONWriter jsonWriter = new JSONWriter(resp.getWriter());
        writeAllContacts(jsonWriter, new Contact("Mike", 6, false), new Contact("George", 3, true));
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    private void writeAllContacts(JSONWriter jsonWriter, Contact... contacts) throws JSONException {
        jsonWriter.array();
        for (Contact contact : contacts) {
            writeAsJson(contact, jsonWriter);
        }
        jsonWriter.endArray();
    }

    private void writeAsJson(Contact contact, JSONWriter jsonWriter) throws JSONException {
        jsonWriter.object();
        jsonWriter.key("name");
        jsonWriter.value(contact.getName());
        jsonWriter.key("closeness");
        jsonWriter.value(contact.getCloseness());
        jsonWriter.key("waitingForResponse");
        jsonWriter.value(contact.isWaitingForResponse());
        jsonWriter.endObject();
    }
}
