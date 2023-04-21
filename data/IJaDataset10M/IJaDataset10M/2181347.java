package angry.courses.servlets;

import java.util.List;
import java.util.Arrays;

public abstract class HtmlFormatter {

    public String htmlTag() {
        StringBuffer buf = new StringBuffer();
        buf.append("<html>");
        buf.append("<head>");
        buf.append("<title>");
        buf.append("Angry Courses - " + title());
        buf.append("</title>");
        script(buf);
        buf.append("</head>");
        buf.append("<body>");
        body(buf);
        buf.append("</body>");
        buf.append("</html>");
        return buf.toString();
    }

    public abstract String title();

    public abstract void body(StringBuffer buf);

    public void script(StringBuffer buf) {
    }

    ;

    public String tr(String... tds) {
        StringBuffer buf = new StringBuffer();
        buf.append("<tr>");
        for (String td : tds) {
            buf.append("<td>");
            buf.append(td);
            buf.append("</td>");
        }
        buf.append("</tr>");
        return buf.toString();
    }

    public String tr(int width, String... tds) {
        StringBuffer buf = new StringBuffer();
        buf.append("<tr>");
        for (String td : tds) {
            buf.append("<td width=\"" + width + "\">");
            buf.append(td);
            buf.append("</td>");
        }
        buf.append("</tr>");
        return buf.toString();
    }

    public String th(String text) {
        return "<th>" + text + "</th>";
    }

    public String textbox(String id) {
        return "<input type=\"text\" name=\"" + id + "\"/>";
    }

    public String textbox(String id, String value) {
        return "<input type=\"text\" name=\"" + id + "\"" + "value=\"" + value + "\"/>";
    }

    public String hidden(String id, String value) {
        return "<input type=\"hidden\" name=\"" + id + "\"" + "value=\"" + value + "\"/>";
    }

    public String multiple(String id, int size, List<String> selected, List<String> options) {
        StringBuffer buf = new StringBuffer();
        buf.append("<select name=\"" + id + "\" multiple size=\"" + size + "\" >");
        for (String option : options) {
            if (selected.contains(option)) buf.append("<option selected>"); else buf.append("<option>");
            buf.append(option);
            buf.append("</option>");
        }
        buf.append("</select>");
        return buf.toString();
    }

    public String dropdown(String id, String selected, List<String> options) {
        StringBuffer buf = new StringBuffer();
        buf.append("<select name=\"" + id + "\" >");
        for (String option : options) {
            if (option.equals(selected)) buf.append("<option selected>"); else buf.append("<option>");
            buf.append(option);
            buf.append("</option>");
        }
        buf.append("</select>");
        return buf.toString();
    }

    public String dropdown(String id, String selected, String... options) {
        return dropdown(id, selected, Arrays.asList(options));
    }

    public String button(String text) {
        return "<input type=\"submit\" value=\"" + text + "\" />";
    }

    public String link(String text, String href) {
        return "<a href=\"" + href + "\" >" + text + "</a>";
    }

    public String heading(int n, String text) {
        return "<h" + n + ">" + text + "</h" + n + ">";
    }
}
