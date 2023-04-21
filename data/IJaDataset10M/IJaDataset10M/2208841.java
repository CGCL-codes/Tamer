package balmysundaycandy.marble.controller.snatch;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcachePutController extends Controller {

    @Override
    protected Navigation run() {
        MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
        memcacheService.put("test", "this is test", Expiration.byDeltaMillis(100000));
        return forward("/WEB-INF/jsp/snatch.jsp");
    }
}
