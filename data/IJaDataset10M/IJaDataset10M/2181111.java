package bz.ziro.kanbe.controller.mng.template;

import java.util.Date;
import java.util.logging.Logger;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;
import bz.ziro.kanbe.model.Template;
import bz.ziro.kanbe.util.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * テンプレートの追加
 * @author Administrator
 */
public class AddController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AddController.class.getName());

    @Override
    public Navigation run() {
        String siteKeyBuf = sessionScope("siteKey");
        Template template = new Template();
        Key templateKey = KeyFactory.allocateTemplateKey(Long.valueOf(siteKeyBuf));
        BeanUtil.copy(request, template);
        Key siteKey = KeyFactory.createSiteKey(Long.valueOf(siteKeyBuf));
        template.setSiteKey(siteKey);
        template.setKey(templateKey);
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        template.setCreateDate(new Date());
        template.setCreator(user);
        Datastore.put(template);
        return null;
    }
}
