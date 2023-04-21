package lius.index.JCR;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import lius.Exception.LiusException;
import lius.Lucene.LuceneActions;
import lius.config.LiusConfig;
import lius.config.LiusField;
import lius.index.Indexer;
import lius.index.IndexerFactory;
import net.sf.archimede.util.JcrUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

public class JCRIndexer extends Indexer {

    private static Log log = LogFactory.getLog(JCRIndexer.class);

    private IndexWriter iw = null;

    public int getType() {
        return 2;
    }

    public boolean isConfigured() {
        boolean ef = false;
        if (getLiusConfig().getJcrFields() != null) return ef = true;
        return ef;
    }

    public Collection getConfigurationFields() {
        List ls = new ArrayList();
        ls.add(getLiusConfig().getJcrFields());
        return ls;
    }

    public String getContent() {
        return null;
    }

    public Collection getPopulatedLiusFields() {
        return null;
    }

    private void openIndex(Directory indexDir, LiusConfig lc) {
        if (iw == null) {
            try {
                iw = LuceneActions.getSingletonInstance().openIndex(indexDir, lc);
            } catch (LiusException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void closeIndex() {
        try {
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public synchronized void index(Directory indexDir) {
        try {
            openIndex(indexDir, getLiusConfig());
            if (iw == null) {
                throw new NullPointerException("IndexWriter is null");
            }
            Node jcrNode = getJcrNode();
            QueryManager qm = jcrNode.getSession().getWorkspace().getQueryManager();
            Set s = getLiusConfig().getJcrFields().entrySet();
            for (Iterator nodeIt = s.iterator(); nodeIt.hasNext(); ) {
                Map.Entry nodeEntry = (Map.Entry) nodeIt.next();
                String nodeSelect = (String) nodeEntry.getKey();
                String xPathQuery = "/jcr:root" + JcrUtil.xmlEncodePath(jcrNode) + nodeSelect;
                log.info(xPathQuery);
                Query nodeQuery = qm.createQuery(xPathQuery, Query.XPATH);
                QueryResult nodeQueryResults = nodeQuery.execute();
                for (NodeIterator nodeResultsIt = nodeQueryResults.getNodes(); nodeResultsIt.hasNext(); ) {
                    Node selectNode = nodeResultsIt.nextNode();
                    Collection toIndex = (Collection) nodeEntry.getValue();
                    Collection toIndex2 = new ArrayList();
                    for (Iterator fieldsIt = toIndex.iterator(); fieldsIt.hasNext(); ) {
                        LiusField lf = (LiusField) fieldsIt.next();
                        String fieldXPathQuery = "/jcr:root" + JcrUtil.xmlEncodePath(selectNode) + "/" + lf.getXpathSelect();
                        log.info(fieldXPathQuery);
                        Query fieldQuery = qm.createQuery(fieldXPathQuery, Query.XPATH);
                        QueryResult fieldQueryResults = fieldQuery.execute();
                        boolean found = false;
                        for (NodeIterator nodesToIndexIterator = fieldQueryResults.getNodes(); nodesToIndexIterator.hasNext(); ) {
                            Node nodeToIndex = nodesToIndexIterator.nextNode();
                            if (nodeToIndex.getPrimaryNodeType().getName().equals("nt:file")) {
                                Node jcrContent = nodeToIndex.getNode("jcr:content");
                                Property prop = jcrContent.getProperty("jcr:mimeType");
                                String mimeType = prop.getString();
                                Property content = jcrContent.getProperty("jcr:data");
                                InputStream is = content.getStream();
                                Indexer indexer = IndexerFactory.getIndexer(is, mimeType, getLiusConfig());
                                if (indexer == null) {
                                    System.out.println(prop.getString());
                                    System.out.println(nodeToIndex.getName());
                                } else {
                                    lf.setValue(indexer.getContent());
                                    LiusField newLiusField = new LiusField();
                                    BeanUtils.copyProperties(newLiusField, lf);
                                    toIndex2.add(newLiusField);
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            for (RowIterator propertiesToIndexIterator = fieldQueryResults.getRows(); propertiesToIndexIterator.hasNext(); ) {
                                Row row = propertiesToIndexIterator.nextRow();
                                String[] columnNames = fieldQueryResults.getColumnNames();
                                for (int i = 0; i < columnNames.length; i++) {
                                    if ((columnNames[i].equals("jcr:path") && columnNames.length != 2) || columnNames[i].equals("jcr:score")) {
                                        continue;
                                    }
                                    Value currentVal = row.getValue(columnNames[i]);
                                    if (currentVal != null) {
                                        lf.setValue(currentVal.getString());
                                    } else {
                                        lf.setValue("");
                                    }
                                    LiusField newLiusField = new LiusField();
                                    BeanUtils.copyProperties(newLiusField, lf);
                                    toIndex2.add(newLiusField);
                                }
                            }
                        }
                    }
                    if (!toIndex2.isEmpty()) {
                        Document document = LuceneActions.getSingletonInstance().populateLuceneDoc(toIndex2);
                        LuceneActions.getSingletonInstance().save(document, iw, getLiusConfig());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
