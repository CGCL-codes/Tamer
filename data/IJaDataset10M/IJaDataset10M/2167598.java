package oxygen.forum.view;

import oxygen.web.GenericWebAction;
import oxygen.web.TemplateHandler;
import oxygen.web.WebLocal;

public class BasicForumAction extends GenericWebAction {

    public int render() throws Exception {
        TemplateHandler thdlr = WebLocal.getTemplateHandler();
        thdlr.render();
        return super.render();
    }
}
