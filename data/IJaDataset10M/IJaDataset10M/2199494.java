package html;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jhierrot
 */
public class Menu extends Container {

    private ArrayList MenCols = new ArrayList();

    public Menu() {
    }

    @Override
    public String ToHtml(HttpSession Sess) {
        String CadHtml = "<table cellpadding=\"2\" cellspacing=\"0\"  id=\"MenuLine\"><tr>";
        for (int i = 0; i < MenCols.size(); i++) {
            CadHtml += "<td width=\"100px\" ><div onMouseOver=\"hideAll(); showLayer('MCol" + i + "'); stopTime()\" onMouseOut=\"startTime();\">" + ((MenuCol) MenCols.get(i)).getText() + "</div></td>\n";
        }
        CadHtml += "</tr></table>";
        for (int i = 0; i < MenCols.size(); i++) {
            CadHtml += ((MenuCol) MenCols.get(i)).HtmlPropio();
        }
        return (CadHtml);
    }

    public void add(MenuCol NewMC) {
        MenCols.add(NewMC);
    }
}
