package parser;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import core.Item;

public abstract class AbstractBuilderAndHandler<T extends Item> extends AbstractInternalBuilder<T> implements IHandler<T> {

    private Logger logger = Logger.getLogger(AbstractBuilderAndHandler.class);

    protected AbstractBuilderAndHandler<T> successor;

    public Set<T> processRequest(String rawUrls) {
        logger.info("processRequest() starts");
        rawUrls = replaceHtmlCharsByRealChars(rawUrls);
        boolean found = this.find(rawUrls);
        Set<T> builtUrls = null;
        if (found) {
            builtUrls = this.build(rawUrls);
        } else if (this.successor != null) {
            logger.debug("processRequest() call successor's processRequest() ");
            builtUrls = this.successor.processRequest(rawUrls);
        }
        if (builtUrls == null) {
            logger.debug("no more remaingin builder to try to build urls");
            builtUrls = new HashSet<T>();
        }
        logger.info("processRequest() returns " + builtUrls.size() + " built urls :" + builtUrls);
        return builtUrls;
    }

    private String replaceHtmlCharsByRealChars(String rawUrls) {
        rawUrls = rawUrls.replace("\t", " ").replace("\r", "");
        logger.debug("replaceHtmlCharsByRealChars()\nrawUrls in Output=" + rawUrls);
        return rawUrls;
    }

    public void setSuccessor(AbstractBuilderAndHandler<T> successor) {
        this.successor = successor;
    }
}
