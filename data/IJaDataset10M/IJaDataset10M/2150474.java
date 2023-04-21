package experiments.realcore;

import org.apache.log4j.Logger;
import org.pfyshnet.core.Core;
import org.pfyshnet.core.UserKeywordUpload;

public class RCSearchStore implements UserKeywordUpload {

    private static Logger logger = Logger.getLogger(Core.class);

    private Object Data;

    private String KeyWord;

    private long MaxBackTime;

    private int MinDepth;

    public RCSearchStore(String keyword, Object data, long maxbacktime, int mindepth) {
        KeyWord = keyword;
        Data = data;
        MinDepth = mindepth;
        MaxBackTime = maxbacktime;
    }

    public void Fail(String msg) {
        logger.debug("keyword store failed! " + msg);
    }

    public Object getData() {
        return Data;
    }

    public String getKeyword() {
        return KeyWord;
    }

    public long getMaxBackTime() {
        return MaxBackTime;
    }

    public int getMinDepth() {
        return MinDepth;
    }
}
