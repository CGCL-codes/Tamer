package bz.ziro.kanbe.controller.mng.listdata;

import java.util.List;
import java.util.logging.Logger;
import org.slim3.controller.Navigation;
import com.google.appengine.api.datastore.Key;
import bz.ziro.kanbe.bean.Pager;
import bz.ziro.kanbe.controller.mng.PagerController;
import bz.ziro.kanbe.dao.SiteListDao;
import bz.ziro.kanbe.dao.SiteListDataDao;
import bz.ziro.kanbe.logic.ListLogic;
import bz.ziro.kanbe.model.SiteList;
import bz.ziro.kanbe.model.SiteListData;
import bz.ziro.kanbe.util.KeyFactory;

/**
 * リストデータ検索
 * @author Administrator
 */
public class FindController extends PagerController {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(FindController.class.getName());

    @Override
    protected Integer getCount() {
        String keyBuf = requestScope("listKey");
        Long listKey = Long.valueOf(keyBuf);
        Key key = KeyFactory.createListKey(listKey);
        return SiteListDataDao.count(key);
    }

    @Override
    protected Integer getLimit() {
        return 10;
    }

    @Override
    protected Navigation runPager(Pager pager) {
        String keyBuf = param("listKey");
        Long listKey = Long.valueOf(keyBuf);
        SiteList siteList = SiteListDao.find(listKey);
        requestScope("siteList", siteList);
        List<SiteListData> siteDataList = SiteListDataDao.findPageList(listKey, pager);
        List<String> lineList = ListLogic.createLineList(siteDataList);
        requestScope("lineList", lineList);
        return forward("list.jsp");
    }
}
