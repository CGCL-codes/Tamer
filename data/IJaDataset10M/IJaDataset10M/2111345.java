package bz.ziro.kanbe.controller.mng.log;

import java.util.Date;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import bz.ziro.kanbe.dao.LogDao;
import bz.ziro.kanbe.model.Log;

/**
 * ログの更新
 * @author Administrator
 */
public class UpdateController extends Controller {

    @Override
    public Navigation run() {
        String keyBuf = requestScope("logKey");
        Log log = LogDao.find(Long.valueOf(keyBuf));
        BeanUtil.copy(request, log);
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        log.setEditDate(new Date());
        log.setEditor(user);
        Datastore.put(log);
        return null;
    }
}
