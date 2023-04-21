package org.verus.ngl.sl.bprocess.technicalprocessing;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.lucene.search.IndexSearcher;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Element;
import org.verus.ngl.sl.objectmodel.technicalprocessing.SEARCHABLE_CATALOGUERECORD;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLXMLUtility;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.verus.ngl.sl.objectmodel.technicalprocessing.CUSTOM_INDEX;

/**
 *
 * @author Edukondalu
 */
public class SearchCatalogueImpl implements SearchCatalogue {

    private IndexSearcher indexSearcher = null;

    @Override
    public String bm_getIndexValues(String xmlReq, String dbId) {
        String resp = "";
        Element response = new Element("Response");
        Element status = new Element("Status");
        try {
            response.addContent(status);
            Element type = new Element("Type");
            type.setText("getResults");
            response.addContent(type);
            Element customIndex = new Element("CustomIndex");
            response.addContent(customIndex);
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(dbId);
            Element root = NGLXMLUtility.getInstance().getRootElementFromXML(xmlReq);
            String libId = root.getChildText("LibraryId");
            Integer libid = new Integer(libId);
            Query query = session.getNamedQuery("CUSTOM_INDEX.findByLibraryId");
            query.setParameter("libraryId", libid);
            List<CUSTOM_INDEX> list = query.list();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    CUSTOM_INDEX custom_INDEX = list.get(i);
                    String statusVal = custom_INDEX.getStatus();
                    String typeVal = custom_INDEX.getCustomType();
                    boolean statboo = statusVal.trim().equalsIgnoreCase("A");
                    boolean typeboo = (typeVal.trim().equalsIgnoreCase("A") || typeVal.trim().equalsIgnoreCase("C"));
                    if (statboo && typeboo) {
                        Element value = new Element("Value");
                        customIndex.addContent(value);
                        Element customId = new Element("Custom_id");
                        customId.setText(custom_INDEX.getCustomId().toString());
                        value.addContent(customId);
                        Element indexName = new Element("IndexName");
                        indexName.setText(custom_INDEX.getIndexName());
                        value.addContent(indexName);
                        Element indexdef = new Element("IndexDefinition");
                        indexdef.setText(custom_INDEX.getIndexDefinition());
                        value.addContent(indexdef);
                        Element indexdesc = new Element("IndexDescription");
                        indexdesc.setText(custom_INDEX.getIndexDescription());
                        value.addContent(indexdesc);
                        Element lib_Id = new Element("LibId");
                        lib_Id.setText(custom_INDEX.getLibraryId().toString());
                        value.addContent(lib_Id);
                    }
                }
            }
            session.close();
            status.setText("yes");
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("no");
        }
        resp = NGLXMLUtility.getInstance().generateXML(response);
        return resp;
    }

    @Override
    public String bm_searchCatalogue(String reqXml, String dataBaseId, Integer libraryId) {
        System.out.println("................... bm_searchCatalogue .................");
        String respXml = "";
        try {
            org.jdom.Element root = NGLXMLUtility.getInstance().getRootElementFromXML(reqXml);
            String totalResult = "";
            String from = root.getAttributeValue("From");
            String to = root.getAttributeValue("To");
            Vector indexVector = new Vector();
            List queryList = root.getChildren("Query");
            String phraseArr[] = new String[queryList.size()];
            String searchTermaArr[] = new String[queryList.size()];
            String booleanArr[] = new String[queryList.size()];
            String indexPhraseValue = "";
            String searchTerm = "";
            String booleanValue = "";
            for (int q = 0; q < queryList.size(); q++) {
                org.jdom.Element indexEle = (org.jdom.Element) queryList.get(q);
                indexPhraseValue = indexEle.getAttributeValue("Phrase");
                searchTerm = indexEle.getAttributeValue("Term");
                booleanValue = indexEle.getAttributeValue("Boolean");
                phraseArr[q] = indexPhraseValue;
                searchTermaArr[q] = searchTerm;
                booleanArr[q] = booleanValue;
                List indexList = indexEle.getChildren("Index");
                Vector vec = new Vector();
                for (int p = 0; p < indexList.size(); p++) {
                    org.jdom.Element ele = (org.jdom.Element) indexList.get(p);
                    List fieldsList = ele.getChildren("Field");
                    for (int f = 0; f < fieldsList.size(); f++) {
                        Element fldsEle = (Element) fieldsList.get(f);
                        String tag1 = fldsEle.getAttributeValue("tag");
                        vec.add(tag1);
                    }
                }
                indexVector.add(vec);
            }
            Vector vectorCatalogue1 = new Vector();
            String queryStr = "".trim();
            System.out.println("...............total rows: " + indexVector.size());
            for (int v = 0; v < indexVector.size(); v++) {
                Vector objVect = (Vector) indexVector.get(v);
                System.out.println(".....................v: " + v);
                String field[] = new String[objVect.size()];
                objVect.toArray(field);
                for (int f = 0; f < field.length; f++) {
                    String fields = field[f];
                    if ((v != booleanArr.length - 1) || (v == 0)) {
                        if (booleanArr[v].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                                }
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            if (v == 0) {
                                if (f == 0) {
                                    queryStr = fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                            if (v >= 1) {
                                if (f == 0) {
                                    queryStr = queryStr + booleanArr[v] + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                                if (f >= 1) {
                                    queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                                }
                            }
                        }
                    } else {
                        if (booleanArr[v - 1].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("AND") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v].equalsIgnoreCase("OR") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("Any of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("All of these")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + searchTermaArr[v] + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "AND" + " " + fields + ":" + searchTermaArr[v] + " ";
                            }
                        }
                        if (booleanArr[v - 1].equalsIgnoreCase("NOT") && phraseArr[v].equalsIgnoreCase("As a Phase")) {
                            String str = "";
                            if (v == booleanArr.length - 1) {
                                str = queryStr + booleanArr[v - 1] + " ";
                            }
                            if (f == 0) {
                                queryStr = str + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            } else if (v >= 1 || f >= 1) {
                                queryStr = queryStr + "OR" + " " + fields + ":" + "\"" + searchTermaArr[v] + "\"" + " ";
                            }
                        }
                    }
                }
            }
            System.out.println(".....................queryStr: " + queryStr);
            SolrServer server = new CommonsHttpSolrServer("http://localhost:8080/apache-solr1.3.0");
            SolrQuery query = new SolrQuery(queryStr);
            query.setRows(Integer.parseInt(to));
            System.out.println("*********** Final  Query: " + query);
            QueryRequest req = new QueryRequest(query);
            QueryResponse rsp = req.process(server);
            System.out.println("....................Hits: " + rsp.getResults().getNumFound());
            System.out.println("..... From: " + from + "  .............. To: " + to);
            for (int h = Integer.parseInt(from) - 1; h < Integer.parseInt(to) && h < rsp.getResults().getNumFound(); h++) {
                SolrDocument doc = rsp.getResults().get(h);
                Hashtable ht = new Hashtable();
                Object catRecIdobj = doc.getFieldValue("CatalogueRecordId");
                Object libId = doc.getFieldValue("OwnerLibraryId");
                ht.put("catRecId", catRecIdobj.toString());
                ht.put("libId", libId.toString());
                vectorCatalogue1.addElement(ht);
            }
            totalResult = String.valueOf(rsp.getResults().getNumFound());
            System.out.println("............. Vector: " + vectorCatalogue1);
            String type = "A";
            Element root1 = new Element("Root");
            Element type1 = new Element("Type");
            type1.setText("catalogueRecordWholeXmls");
            root1.addContent(type1);
            Element totalHits = new Element("TotalResults");
            totalHits.setText(totalResult);
            root1.addContent(totalHits);
            if (vectorCatalogue1 != null) {
                Element subRoot = bm_getSearchCatalogueRecords(vectorCatalogue1, dataBaseId, type, libraryId);
                List list = subRoot.getChildren("CatalogueRecord");
                try {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Element catEle = (Element) list.get(i);
                            Element copyEle = (Element) catEle.clone();
                            root1.addContent(copyEle);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
            respXml = NGLXMLUtility.getInstance().generateXML(root1);
        } catch (Exception e) {
            e.printStackTrace();
            Element root1 = new Element("Root");
            Element type1 = new Element("Type");
            type1.setText("catalogueRecordWholeXmls");
            root1.addContent(type1);
            Element totalHits = new Element("TotalResults");
            totalHits.setText("0");
            root1.addContent(totalHits);
            respXml = NGLXMLUtility.getInstance().generateXML(root1);
            return respXml;
        }
        return respXml;
    }

    @Override
    public Element bm_getSearchCatalogueRecords(Vector vectorCatalogue, String dataBaseId, String type1, Integer libraryId) {
        System.out.println("................. bm_getSearchCatalogueRecords ..........");
        Element root = new Element("Root");
        if (vectorCatalogue != null) {
            try {
                Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
                Session session = connections.getSession(dataBaseId);
                for (int i = 0; i < vectorCatalogue.size(); i++) {
                    Hashtable ht = (Hashtable) vectorCatalogue.get(i);
                    String catId = (String) ht.get("catRecId");
                    String ownerLibId = (String) ht.get("libId");
                    Query query = session.getNamedQuery("SEARCHABLE_CATALOGUERECORD.findByCataloguerecordidOwnerLibraryId");
                    query.setParameter("cataloguerecordid", Integer.parseInt(catId));
                    query.setParameter("ownerLibraryId", Integer.parseInt(ownerLibId));
                    SEARCHABLE_CATALOGUERECORD catRec = (SEARCHABLE_CATALOGUERECORD) query.uniqueResult();
                    if (type1.equalsIgnoreCase("A")) {
                        String xmlWhole = catRec.getXmlWholerecord();
                        String idVal = String.valueOf(catRec.getSEARCHABLE_CATALOGUERECORDPK().getCataloguerecordid());
                        String libId = String.valueOf(catRec.getSEARCHABLE_CATALOGUERECORDPK().getOwnerLibraryId());
                        Element catRecordEle = new Element("CatalogueRecord");
                        Element wholeXml = new Element("WholeXml");
                        wholeXml.setText(xmlWhole);
                        catRecordEle.addContent(wholeXml);
                        Element id = new Element("Id");
                        id.setText(idVal);
                        catRecordEle.addContent(id);
                        Element libIdEle = new Element("LibId");
                        libIdEle.setText(libId);
                        catRecordEle.addContent(libIdEle);
                        root.addContent(catRecordEle);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("The Error in search1 is ###########" + e);
            }
        }
        return root;
    }
}
