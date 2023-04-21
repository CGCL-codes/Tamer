package org.webcockpit.builder.config;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.webcockpit.builder.BuilderCockpit;

/**
 */
public class WebCockpit extends BuilderCockpit implements Configurable {

    public static final String ELEMENT_WEBCOCKPIT = "webcockpit";

    /**
	 * Initialize the instance from the XML Document. The 
	 * child instances of this instance are initialized 
	 * recursively until the entire instance is initialized.  
	 * 
	 * @param config The document containing all configuration information
	 * @param element The element
	 */
    public void init(Document config, Element element) throws ConfigurationException {
        Element ctxElement = (Element) element.selectSingleNode(Context.ELEMENT_CONTEXT);
        Context ctx = new Context();
        ctx.init(config, ctxElement);
        setContext(ctx);
        List qList = element.selectNodes(Query.ELEMENT_QUERY);
        List qs = new ArrayList();
        for (int i = 0; i < qList.size(); i++) {
            Element qElement = (Element) qList.get(i);
            Query q = new Query();
            q.init(config, qElement);
            qs.add(q);
        }
        setQueries(qs);
        List cList = element.selectNodes(ChartLookAndFeel.ELEMENT_CHARTLOOKANDFEEL);
        List cs = new ArrayList();
        for (int i = 0; i < cList.size(); i++) {
            Element cElement = (Element) cList.get(i);
            ChartLookAndFeel c = new ChartLookAndFeel();
            c.init(config, cElement);
            cs.add(c);
        }
        setChartlookandfeels(cs);
        List pList = element.selectNodes(Page.ELEMENT_PAGE);
        List ps = new ArrayList();
        for (int i = 0; i < pList.size(); i++) {
            Element pElement = (Element) pList.get(i);
            Page p = new Page();
            p.init(config, pElement);
            ps.add(p);
        }
        setPages(ps);
        check(config, element);
    }

    private void check(Document config, Element element) throws ConfigurationException {
        List bindvarNames = new ArrayList();
        for (int i = 0; getQueries() != null && i < getQueries().size(); i++) {
            Query q1 = (Query) getQueries().get(i);
            List partialBindVarNames = new ArrayList();
            for (int j = 0; getQueries() != null && j < getQueries().size(); j++) {
                if (i == j) continue;
                Query q2 = (Query) getQueries().get(j);
                if (q1.getId().equals(q2.getId())) {
                    throw new ConfigurationException("Query id's are not unique: " + q1.getId());
                }
            }
            if (q1.getDatasourceid() == null || q1.getDatasourceid().length() == 0) {
                if (getContext().getDatasources() != null && getContext().getDatasources().size() == 1) {
                    Datasource ds = (Datasource) getContext().getDatasources().get(0);
                    q1.setDatasourceid(ds.getId());
                } else {
                    throw new ConfigurationException("DatasourceId is optional only when there is only one datasource defined: " + q1.getId());
                }
            }
            for (int j = 0; q1.getBindvars() != null && j < q1.getBindvars().size(); j++) {
                BindVar bv = (BindVar) q1.getBindvars().get(j);
                if (bindvarNames.contains(bv.getId())) {
                    throw new ConfigurationException("Bindvar name: " + bv.getId() + " is used by another query.");
                }
                partialBindVarNames.add(bv.getId());
            }
            bindvarNames.addAll(partialBindVarNames);
        }
        for (int i = 0; getPages() != null && i < getPages().size(); i++) {
            Page p = (Page) getPages().get(i);
            for (int j = 0; p.getItems() != null && j < p.getItems().size(); j++) {
                Item item = (Item) p.getItems().get(j);
                if (item.getChart() != null && item.getChart().getLookandfeelid() != null) {
                    String laf = item.getChart().getLookandfeelid();
                    if (!"".equals(laf) && null == lookupLookAndFeelByName(laf)) {
                        throw new ConfigurationException("lookandfeelid: " + laf + " is not defined in item " + item.getId());
                    }
                    if ("".equals(laf)) {
                        if (getChartlookandfeels() != null && getChartlookandfeels().size() == 1) {
                            ChartLookAndFeel l = (ChartLookAndFeel) getChartlookandfeels().get(0);
                            item.getChart().setLookandfeelid(l.getId());
                        }
                    }
                }
                if (item.getTable() != null) {
                    if (item.getTable().getColumns() == null) {
                        throw new ConfigurationException("no columns defined for table in item " + item.getId());
                    }
                    List cols = item.getTable().getColumns();
                    for (int col = 0; col < cols.size(); col++) {
                        Column column = (Column) cols.get(col);
                        if (column.getTitle() == null || "".equals(column.getTitle())) {
                            throw new ConfigurationException("column needs title for table in item " + item.getId());
                        }
                        if (column.getAlias() == null || "".equals(column.getAlias())) {
                            throw new ConfigurationException("column needs an alias in table in item " + item.getId());
                        }
                    }
                }
            }
        }
    }

    /**
	 * Save the configurable item to an XML Document. The item
	 * adds elements to the parent element for itself and all
	 * it's children.
	 * 
	 * @param config The document being created
	 * @param element The parent element
	 */
    public void save(Document config, Element element) throws ConfigurationException {
        Element webcockpit = config.addElement(ELEMENT_WEBCOCKPIT);
        if (getContext() != null) {
            ((Context) getContext()).save(config, webcockpit);
        }
        for (int i = 0; getQueries() != null && i < getQueries().size(); i++) {
            Query q = (Query) getQueries().get(i);
            q.save(config, webcockpit);
        }
        for (int i = 0; getChartlookandfeels() != null && i < getChartlookandfeels().size(); i++) {
            ChartLookAndFeel c = (ChartLookAndFeel) getChartlookandfeels().get(i);
            c.save(config, webcockpit);
        }
        for (int i = 0; getPages() != null && i < getPages().size(); i++) {
            Page p = (Page) getPages().get(i);
            p.save(config, webcockpit);
        }
    }

    public Document serialize() throws Exception {
        Document document = DocumentHelper.createDocument();
        save(document, null);
        return document;
    }

    public void deserialize(Document config) throws Exception {
        Element webcockpit = config.getRootElement();
        init(config, webcockpit);
    }
}
