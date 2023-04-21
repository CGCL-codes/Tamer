package bz.ziro.kanbe.controller.mng.text;

import java.util.logging.Logger;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import bz.ziro.kanbe.dao.SiteTextDao;
import bz.ziro.kanbe.model.SiteText;

/**
 * テキストの検索
 * @author Administrator
 */
public class FindController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(FindController.class.getName());

    @Override
    public Navigation run() {
        String keyBuf = param("textKey");
        SiteText text = SiteTextDao.find(Long.valueOf(keyBuf));
        requestScope("text", text);
        return forward("find.jsp");
    }
}
