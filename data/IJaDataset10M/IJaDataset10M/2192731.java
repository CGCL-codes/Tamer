package controllers.bluehat.backbone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utils.exceptions.ValidationException;
import utils.json.data.AbstractJson;
import utils.json.data.backbone.FocusJson;
import utils.table.DataTable;
import utils.table.DataTable.DataTablesParameters;
import models.Focus;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.data.validation.Error;
import controllers.bluehat.BlueHatBaseController;

public class FocusController extends BlueHatBaseController {

    public static void index() {
        render("bluehat/backbone/index.html");
    }

    public static void list() {
        final DataTablesParameters dtp = new DataTable.DataTablesParameters(request);
        DataTable<Focus> table = new DataTable<Focus>(Focus.all().filter("owner", getCurrentUser()), dtp);
        List<FocusJson> jsonList = new ArrayList<FocusJson>();
        for (Focus f : table.getRows()) {
            jsonList.add(new FocusJson(f));
        }
        table.setRows(jsonList);
        renderJSON(table);
    }

    public static void save(String body) throws ValidationException {
        JsonObject json = new JsonParser().parse(body).getAsJsonObject();
        Long id = json.get("id") != null ? json.get("id").getAsLong() : null;
        String title = json.get("title").getAsString() != null ? json.get("title").getAsString() : null;
        String description = json.get("description") != null ? json.get("description").getAsString() : null;
        Focus focus = null;
        if (id != null) focus = Focus.getById(id, Focus.findByOwner(getCurrentUser())); else focus = new Focus();
        focus.title = title;
        focus.description = description;
        focus.owner = getCurrentUser();
        validation.valid(focus);
        if (validation.hasErrors()) {
            ErrorJson errors = new ErrorJson(validation.errors());
            throw new ValidationException("Validation failed!!", errors);
        } else {
            focus.save();
            renderJSON(new FocusJson(focus));
        }
    }

    public static void detail(Long id) {
        Focus focus = Focus.getById(id, Focus.findByOwner(getCurrentUser()));
        renderJSON(focus);
    }

    public static void delete(Long id) {
        Focus focus = Focus.getById(id, Focus.findByOwner(getCurrentUser()));
        focus.delete();
    }

    public static void edit(String body) {
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        Long id = jsonObject.get("id").getAsLong();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        Focus focus = Focus.getById(id, Focus.findByOwner(getCurrentUser()));
        focus.title = title;
        focus.description = description;
        focus.save();
        renderJSON(focus);
    }

    public static class ErrorJson extends HashMap<String, Object> {

        public ErrorJson() {
            put("message", "Generic Error");
        }

        public ErrorJson(List<Error> errors) {
            for (Error error : errors) {
                put(error.getKey(), error.message());
            }
        }

        public String toJson() {
            StringBuffer json = new StringBuffer("{\"error\": {");
            for (String key : keySet()) {
                json.append("\"").append(key).append("\": \"").append(get(key)).append("\",");
            }
            json.deleteCharAt(json.length() - 1);
            json.append("}}");
            return json.toString();
        }
    }
}
