package teamwebsite.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class News extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "pages/news.jsp";
    }
}
