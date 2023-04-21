package com.webmotix.facade;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.util.QueryUtil;
import com.webmotix.util.XPathUtils;

public class MotixModuleComposeContentMoveDown extends MotixEmptyFacade {

    private static final Logger log = LoggerFactory.getLogger(MotixModuleComposeContentUpdate.class);

    @Override
    public void execute(final MotixParameter parameter) throws Throwable {
        final Session session = parameter.getSession();
        final Node node = session.getNodeByUUID(parameter.getMotixUUID());
        final long index = node.getProperty(MotixNodeTypes.NS_PROPERTY + ":index").getLong();
        final String query = XPathUtils.getXPath(node.getParent(), MotixNodeTypes.COMPOSECONTENT, "@mpt:index=" + (index + 1), "mpt:index");
        final NodeIterator nodes = QueryUtil.query(parameter.getSession(), query, Query.XPATH);
        if (nodes.hasNext()) {
            final Node nodeUp = nodes.nextNode();
            nodeUp.setProperty(MotixNodeTypes.NS_PROPERTY + ":index", index);
            node.setProperty(MotixNodeTypes.NS_PROPERTY + ":index", index + 1);
        }
    }
}
