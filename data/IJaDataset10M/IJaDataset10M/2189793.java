package com.webmotix.facade;

import java.util.Iterator;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.core.MotixNodes;
import com.webmotix.core.PathCache;
import com.webmotix.core.SystemParameter;
import com.webmotix.dao.LogDAO;
import com.webmotix.util.QueryUtil;

/**
 * Regra de neg�cio para atualizar as vari�veis de configura��o.
 * 
 * @author wsouza
 */
public class MotixParameterUpdate extends MotixEmptyFacade {

    private static final Logger log = LoggerFactory.getLogger(MotixParameterUpdate.class);

    /**
	 * Criptografa a senha antes de inserir o usuario e verifica se j� existe um
	 * usu�rio com mesmo login.
	 */
    @Override
    public void execute(final MotixParameter param) throws Throwable {
        final Session session = param.getSession();
        final Iterator<String> names = param.getActiveFields().keySet().iterator();
        final Node root = session.getRootNode();
        final Node site = root.getNode(MotixNodes.SITE);
        final Node parameters = site.getNode("parameters");
        while (names.hasNext()) {
            final String name = names.next();
            final String value = param.getValue(name);
            if (name.equals(SystemParameter.WEBMOTIX_IMAGE_LOGIN) || name.equals(SystemParameter.WEBMOTIX_IMAGE_TOP)) {
                continue;
            }
            final String xpathQuery = "//element(*,mtx:parameter)[@mpt:name=\"" + name + "\"]";
            final NodeIterator nodeList = QueryUtil.query(session, xpathQuery, Query.XPATH);
            if (nodeList.hasNext()) {
                final Node node = nodeList.nextNode();
                node.setProperty(MotixNodeTypes.NS_PROPERTY + ":value", value);
            } else {
                final Node node = parameters.addNode(MotixNodes.PARAMETER, MotixNodeTypes.PARAMETER);
                node.setProperty(MotixNodeTypes.NS_PROPERTY + ":name", name);
                node.setProperty(MotixNodeTypes.NS_PROPERTY + ":value", value);
            }
            SystemParameter.reload(name, value);
        }
        if (StringUtils.isNotEmpty(param.getValue(SystemParameter.WEBMOTIX_IMAGE_LOGIN))) {
            final FileItem item = (FileItem) param.getActiveFields().get(SystemParameter.WEBMOTIX_IMAGE_LOGIN);
            final String xpathQuery = "//element(*,mtx:parameter)[@mpt:name=\"" + SystemParameter.WEBMOTIX_IMAGE_LOGIN + "\"]";
            final NodeIterator nodeList = QueryUtil.query(session, xpathQuery, Query.XPATH);
            if (nodeList.hasNext()) {
                final Node node = nodeList.nextNode();
                node.setProperty(MotixNodeTypes.NS_PROPERTY + ":value", item.getInputStream());
                SystemParameter.reload(SystemParameter.WEBMOTIX_IMAGE_LOGIN, node.getUUID());
            }
        }
        if (StringUtils.isNotEmpty(param.getValue(SystemParameter.WEBMOTIX_IMAGE_TOP))) {
            final FileItem item = (FileItem) param.getActiveFields().get(SystemParameter.WEBMOTIX_IMAGE_TOP);
            final String xpathQuery = "//element(*,mtx:parameter)[@mpt:name=\"" + SystemParameter.WEBMOTIX_IMAGE_TOP + "\"]";
            final NodeIterator nodeList = QueryUtil.query(session, xpathQuery, Query.XPATH);
            if (nodeList.hasNext()) {
                final Node node = nodeList.nextNode();
                node.setProperty(MotixNodeTypes.NS_PROPERTY + ":value", item.getInputStream());
                SystemParameter.reload(SystemParameter.WEBMOTIX_IMAGE_TOP, node.getUUID());
            }
        }
        LogDAO.clear();
        PathCache.clear();
    }
}
