package uk.co.lakesidetech.springxmldb.examples.springxmldbmanage.web.spring;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import uk.co.lakesidetech.springxmldb.examples.springxmldbmanage.domain.logic.ISpringXMLDBManage;

/**
 * @author Stuart Eccles
 */
public class NewCollectionController extends AbstractController {

    private ISpringXMLDBManage xmldbManage;

    /**
	 * @param xmldbManage The xmldbManage to set.
	 */
    public void setXmldbManage(ISpringXMLDBManage xmldbManage) {
        this.xmldbManage = xmldbManage;
    }

    /**
	 * 
	 */
    public NewCollectionController() {
        super();
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String collectionPath = request.getParameter("collectionPath");
        String newCollectionName = request.getParameter("newCollectionName");
        xmldbManage.newCollection(collectionPath, newCollectionName);
        Map model = new HashMap();
        model.put("collectionPath", collectionPath);
        return new ModelAndView(new RedirectView("showCollection.htm"), model);
    }
}
