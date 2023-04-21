package ces.platform.bbs.proxy;

import ces.platform.bbs.ForumThread;
import ces.platform.bbs.Authorization;
import ces.platform.bbs.Permissions;
import java.util.Iterator;

/**
 * Title:        ForumThreadIteratorProxy
 * Description:  ThreadIterator's Proxy
 * Copyright:    Copyright (c) 2003
 * Company:      Cnjsp.com
 * @author:      Sager
 * @version      1.0
 */
public class ForumThreadIteratorProxy extends IteratorProxy {

    public ForumThreadIteratorProxy(Iterator iterator, Authorization authorization, Permissions permissions) {
        super(iterator, authorization, permissions);
    }

    public Object next() throws java.util.NoSuchElementException {
        ForumThread thread = (ForumThread) iterator.next();
        return new ForumThreadProxy(thread, authorization, permissions);
    }
}
