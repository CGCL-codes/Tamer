package demo.output;

import java.io.Reader;
import java.io.StringReader;
import prest.core.annotations.Action;
import prest.core.annotations.Doc;
import prest.json.annotations.Json;
import prest.web.ContentType;
import prest.web.WebController;
import prest.web.annotations.View;
import demo.output.model.OutputData;
import demo.output.model.OutputObject;

/**
 * @author "Peter Rybar <peter.rybar@centaur.sk>"
 *
 */
@Doc("Output data handling.")
public class OutputController extends WebController {

    @Action(name = "output-string")
    @Doc("String output.")
    public String output() {
        setContentType(ContentType.TEXT_PLAIN_UTF8);
        return "Hello  I'm string.";
    }

    @Action(name = "output-reader")
    @Doc("Reader output.")
    public Reader reader() {
        setContentType(ContentType.TEXT_PLAIN_UTF8);
        Reader reader = new StringReader("Hello. I'm reader.");
        return reader;
    }

    @Action(name = "output-object-to-string")
    @Doc("Object toString() output.")
    public OutputObject object() {
        setContentType(ContentType.TEXT_PLAIN_UTF8);
        OutputObject object = new OutputObject("Hello.", "I'm reader.");
        return object;
    }

    @Action(name = "output-json")
    @Doc("Output data serialized to JSON format.")
    @Json
    public OutputData outputJson() {
        OutputData data = new OutputData();
        return data;
    }

    @Action(name = "output-view")
    @Doc("Output data model representation using JSP view.")
    @View(template = "output/output.jsp")
    public OutputData outputTeplate() {
        OutputData data = new OutputData();
        return data;
    }
}
