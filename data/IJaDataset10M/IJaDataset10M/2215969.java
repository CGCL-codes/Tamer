package jp.co.baka.gae.example.controller.view.bbs;

import java.util.List;
import jp.co.baka.gae.example.meta.BbsMessageMeta;
import jp.co.baka.gae.example.util.PerformanceChecker;
import jp.co.baka.gae.lib.MyController;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import com.google.appengine.api.datastore.Key;

public class AllDeleteController extends MyController {

    @Override
    public Navigation run() {
        String message = "";
        BbsMessageMeta BbsMeta = new BbsMessageMeta();
        PerformanceChecker timer = PerformanceChecker.get();
        int MAXLOOPCNT = 100;
        int loopCnt = 0;
        while (true) {
            loopCnt++;
            List<Key> keyList = Datastore.query(BbsMeta).limit(300).asKeyList();
            if (loopCnt > MAXLOOPCNT || keyList.isEmpty()) {
                break;
            }
            Datastore.delete(keyList);
        }
        message += "削除時間" + timer.getProcessTimeMills() + "ms<br/>";
        requestScope("gmnMessage", message);
        return forward("index.jsp");
    }
}
