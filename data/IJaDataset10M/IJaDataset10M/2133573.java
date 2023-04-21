package kr.pe.javarss.manager;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import de.nava.informa.core.ItemIF;

/**
 * 페이스북 javarss 페이지 담벼락에 새로운 아이템 게시.
 *
 */
public class FacebookPublisher {

    private static Log logger = LogFactory.getLog(FacebookPublisher.class);

    /**
     * 한번에 게시할 아이템 수
     */
    private static final int BULK_SIZE = 10;

    private String accessToken;

    private String connection;

    private FacebookClient facebookClient;

    private List<ItemIF> recentItems = new ArrayList<ItemIF>();

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void init() {
        facebookClient = new DefaultFacebookClient(accessToken);
        if (logger.isInfoEnabled()) {
            logger.info("Init Success : " + facebookClient);
        }
    }

    /**
     * 담벼락에 아이템 정보를 게시한다.
     * @param newItem
     */
    public synchronized void publishItem(ItemIF item) {
        recentItems.add(item);
        if (recentItems.size() < BULK_SIZE) {
            return;
        }
        publishBulkItems();
        recentItems.clear();
    }

    public String publish(String message) {
        FacebookType response = null;
        try {
            response = facebookClient.publish(connection, FacebookType.class, Parameter.with("message", message));
            String id = response.getId();
            if (logger.isInfoEnabled()) {
                logger.info("A new message published to Facebook : " + connection + " => " + id);
            }
            return id;
        } catch (Throwable e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Facebook publish error : " + connection + " => " + e.getMessage());
            }
        }
        return null;
    }

    private void publishBulkItems() {
        String message = "";
        int count = 0;
        for (ItemIF item : recentItems) {
            if (count > BULK_SIZE) {
                break;
            }
            message += "● " + item.getTitle() + " [ " + item.getChannel().getTitle() + " ]\n" + item.getLink().toString() + "\n\n";
            count++;
        }
        publish(message);
    }
}
