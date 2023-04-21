package jp.co.baka.gae.example.controller.view.error;

import java.util.Enumeration;
import jp.co.baka.gae.lib.MyController;
import org.slim3.controller.Navigation;

public class IndexController extends MyController {

    @Override
    public Navigation run() {
        Enumeration<String> enu = request.getAttributeNames();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement();
            Object obj = requestScope(key);
            System.out.println("key=" + key + " value=" + obj);
        }
        return forward("index.jsp");
    }
}
