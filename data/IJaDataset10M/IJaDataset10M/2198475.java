package bz.ziro.kanbe.controller.mng.listdata;

import java.util.Date;
import java.util.logging.Logger;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import bz.ziro.kanbe.dao.SiteListDataDao;
import bz.ziro.kanbe.model.SiteListData;

/**
 * リストデータ更新
 * @author Administrator
 */
public class UpdateController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(UpdateController.class.getName());

    @Override
    public Navigation run() {
        String listKeyBuf = param("ownerKey");
        String keyBuf = param("listDataKey");
        SiteListData data = SiteListDataDao.find(Long.valueOf(listKeyBuf), Long.valueOf(keyBuf));
        data.setData((String) requestScope("data"));
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        data.setEditDate(new Date());
        data.setEditor(user);
        Datastore.put(data);
        return null;
    }
}
