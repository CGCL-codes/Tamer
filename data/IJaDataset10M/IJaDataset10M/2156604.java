package teamwebsite.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class About extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "pages/about.jsp";
    }
}
