package gndzh.som.summer.action;

import com.opensymphony.xwork2.ActionSupport;
import java.io.PrintStream;
import java.util.Map;
import pub.ext.getJsondata.datafromtab;

public class GridStore extends ActionSupport {

    private String result;

    private Map<String, String> map;

    public String execute() throws Exception {
        datafromtab df = new datafromtab();
        df.setsql((String) this.map.get("sql"));
        String s = df.getjson("");
        System.out.println(s);
        this.result = s;
        return "ajax";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return this.map;
    }
}
