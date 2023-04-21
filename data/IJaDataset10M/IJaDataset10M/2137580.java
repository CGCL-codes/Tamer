package com.webmotix.dao;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.core.MotixNodes;
import com.webmotix.data.MotixDataExport;
import com.webmotix.exception.DAONotFoundException;
import com.webmotix.facade.MotixParameter;
import com.webmotix.util.DAOUtil;
import com.webmotix.util.JcrUtils;
import com.webmotix.util.QueryUtil;
import com.webmotix.util.XmlResponse;
import com.webmotix.util.XmlUtils;

public abstract class MotixDAO {

    /**
	 * Logger
	 */
    protected static final Logger log = LoggerFactory.getLogger(MotixDAO.class);

    /**
	 * Node de refer�ncia utilizado para construir o DAO.
	 */
    protected Node node = null;

    /**
	 * Identificar UUID, utilizando quando criando objeto a partir de um Node.
	 */
    protected String UUID = null;

    /**
	 * Constru��o do objeto DAO atrav�s de um Node JCR.
	 * @param node Node do reposit�rio JCR.
	 */
    public MotixDAO(final Node node) {
        super();
        DAOUtil.nodeToDAO(this, node);
    }

    /**
	 * Constru��o do objeto DAO atrav�s de um Map.
	 * @param fields Campos contendo chave/atributo e valor/cont�do
	 */
    public MotixDAO(final Map<String, String> fields) {
        super();
        DAOUtil.mapToDAO(this, fields);
    }

    /**
	 * Constru��o padr�o do objeto.
	 */
    public MotixDAO() {
        super();
    }

    /**
	 * Retorn o UUID
	 * @return
	 */
    public String getUUID() {
        return UUID;
    }

    public void setUUID(final String UUID) {
        this.UUID = UUID;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(final Node node) {
        this.node = node;
    }

    /**
	 * Recupera o objeto DAO atrav�s do UUID de identifica��o.
	 * @param cls
	 * @param uuid
	 * @param session
	 * @see DAOUtil.findByUUID
	 * @return
	 */
    protected static MotixDAO findByUUID(final Class cls, final String uuid, final Session session) throws DAONotFoundException {
        return DAOUtil.findByUUID(cls, uuid, session);
    }

    /**
	 * Recupera o objeto DAO atrav�s do UUID de identifica��o.
	 * @param cls
	 * @param uuid
	 * @param session
	 * @return
	 */
    protected static MotixDAO findByPath(final Class cls, final String path, final Session session) throws DAONotFoundException {
        try {
            final Node node = (Node) session.getItem(path);
            final MotixDAO obj = (MotixDAO) cls.newInstance();
            DAOUtil.nodeToDAO(obj, node);
            return obj;
        } catch (final InstantiationException ex) {
            throw new DAONotFoundException("Falha ao criar inst�ncia de " + cls, ex);
        } catch (final IllegalAccessException ex) {
            throw new DAONotFoundException("Falha ao criar inst�ncia de " + cls, ex);
        } catch (final RepositoryException ex) {
            throw new DAONotFoundException("Falha ao criar inst�ncia de " + cls, ex);
        }
    }

    /**
	 * Recupera o objeto DAO atrav�s do UUID de identifica��o.
	 * @param cls
	 * @param uuid
	 * @param session
	 * @return
	 */
    protected static Document findByPathXML(final Class cls, final String path, final Session session) throws DAONotFoundException {
        try {
            final Node node = (Node) session.getItem(path);
            final XmlResponse doc = new XmlResponse();
            final Element record = XmlUtils.nodeToXml(node);
            doc.addRecord(record);
            doc.setTotal(1);
            return doc;
        } catch (final PathNotFoundException e) {
            throw new DAONotFoundException("N� n�o encontrado.", e);
        } catch (final RepositoryException e) {
            throw new DAONotFoundException("Ocorreu uma falha n�o identificada no reposit�rio.", e);
        }
    }

    /**
	 * Retorna todos os objetos Node com tratamento especial de pagina��o. 
	 * @param parameter Objeto auxiliar para parametriza��o. Exemplo:<br/> 
	 * {
	 * dir		= ASC<br/>
	 * limit	= 10<br/>
	 * sort		= name<br/>
	 * start	= 0<br/>
	 * search	= novo<br/>
	 * }
	 * @return
	 */
    protected static NodeIterator findAll(final MotixParameter parameter, final String nodeType) {
        if (parameter == null) {
            throw new IllegalArgumentException("O parametro MotixParameter deve ser passado para a consulta findAll.");
        }
        final String dir = QueryUtil.getSortDir(parameter.getValue("dir"));
        final String sort = parameter.getValue("sort");
        final String path = parameter.getValue("path");
        final int start = Integer.parseInt(StringUtils.defaultIfEmpty(parameter.getValue("start"), "0"));
        final String search = StringUtils.defaultIfEmpty(parameter.getValue("search"), parameter.getValue("query"));
        final StringBuffer query = new StringBuffer();
        if (!StringUtils.isEmpty(path)) {
            query.append(MotixNodes.XPATH_JCR_ROOT).append(JcrUtils.getXPathISO9075(path)).append("//element(*," + nodeType + ")");
        } else {
            query.append("//element(*," + nodeType + ")");
        }
        final StringBuffer where = new StringBuffer("");
        if (StringUtils.isNotEmpty(search)) {
            String[] searchs = search.toLowerCase().split(" ");
            for (int i = 0; i < searchs.length; i++) {
                where.append("(jcr:contains(.,\"" + searchs[i] + "*\")) and ");
            }
            query.append("[").append(StringUtils.chomp(where.toString(), " and ")).append("]");
        }
        if (StringUtils.isNotEmpty(sort)) {
            query.append(" order by @" + MotixNodeTypes.NS_PROPERTY + ":" + sort + " " + dir);
        }
        return QueryUtil.query(parameter.getSession(), query.toString(), Query.XPATH, start);
    }

    /**
	 * Retorna um documento XML JDOM para renderiza��o.
	 * @param parameter
	 * @return
	 */
    protected static Document findAllXml(final MotixParameter parameter, final String nodeType) {
        if (parameter == null) {
            throw new IllegalArgumentException("O parametro MotixParameter deve ser passado para a consulta findAllXml.");
        }
        final XmlResponse doc = new XmlResponse();
        try {
            final NodeIterator nodeList = findAll(parameter, nodeType);
            doc.setTotal(nodeList.getSize());
            final int limit = Integer.parseInt(StringUtils.defaultIfEmpty(parameter.getValue("limit"), "-1"));
            if (limit == -1) {
                while (nodeList.hasNext()) {
                    final Element record = XmlUtils.nodeToXml(nodeList.nextNode());
                    doc.addRecord(record);
                }
            } else {
                int c = 0;
                while (nodeList.hasNext() && (c++) < limit) {
                    final Element record = XmlUtils.nodeToXml(nodeList.nextNode());
                    doc.addRecord(record);
                }
            }
        } catch (final RepositoryException e) {
            log.error("Ocorreu um erro no reposit�rio.", e);
        }
        return doc;
    }

    /**
	 * Retorna uma cole��o contendo todos os objetos DAO de uma determinada 
	 * classe e identificado pela propriedade do Node.
	 * Este m�todo deve ser utilizado para recuperar cole��es de objetos 
	 * associados a um DAO.
	 * @param cls Tipo de classe MotixDAO.
	 * @param propertyName Nome da propriedade do Node. Exemplo: role ou 
	 * language. N�o � necess�rio passar o namespace.
	 * @return Cole��o de objetos DAO
	 */
    public Collection getReference(final Class cls, final String propertyName) {
        final Collection<MotixDAO> c = new ArrayList<MotixDAO>();
        try {
            final Property p = getNode().getProperty(MotixNodeTypes.NS_PROPERTY + ":" + propertyName);
            final Session session = getNode().getSession();
            final Value[] values = p.getValues();
            boolean commom = true;
            Method method = null;
            try {
                method = cls.getMethod("findByUUID", String.class, Session.class);
                commom = false;
            } catch (final NoSuchMethodException e) {
            }
            for (int i = 0; i < values.length; i++) {
                final Value value = values[i];
                final MotixDAO dao = (commom) ? DAOUtil.findByUUID(cls, value.getString(), session) : (MotixDAO) method.invoke(this, value.getString(), session);
                c.add(dao);
            }
        } catch (final PathNotFoundException ex) {
        } catch (final Exception ex) {
            log.error(MessageFormat.format("Falha ao criar cole��o de objetos do tipo {0} e propriedade {1}.", new Object[] { cls, propertyName }), ex);
        }
        return c;
    }

    public Collection getReference(final Class cls, final String[] values) {
        final Collection<MotixDAO> c = new ArrayList<MotixDAO>();
        if (values != null) {
            try {
                final Session session = getNode().getSession();
                for (int i = 0; i < values.length; i++) {
                    final String value = values[i];
                    final MotixDAO dao = DAOUtil.findByUUID(cls, value, session);
                    c.add(dao);
                }
            } catch (final Exception ex) {
                log.error(MessageFormat.format("Falha ao criar cole��o de objetos do tipo {0} e propriedade {1}.", new Object[] { cls, values }), ex);
            }
        }
        return c;
    }

    public static void exportAllJCR(final Node root, final HttpServletResponse response) throws IOException {
        MotixDataExport.exportAllJCR(root, response);
    }

    public static void exportAllXML(final Node root, final HttpServletResponse response) throws IOException {
        MotixDataExport.exportAllXML(root, response);
    }

    public static void exportAllCSV(final Node root, final HttpServletResponse response) throws IOException {
        MotixDataExport.exportAllCSV(root, response);
    }

    /**
	 * Transforma o objeto em uma String contendo os atributos e valores do objeto.
	 */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
