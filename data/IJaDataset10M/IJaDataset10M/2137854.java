package org.databene.dbsanity.parser;

import java.util.Set;
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.xml.XMLUtil;
import org.databene.dbsanity.model.ForeignKeyStrategy;
import org.databene.dbsanity.model.StrategyBasedCheck;
import org.databene.dbsanity.model.query.DefectQueryStrategy;
import org.w3c.dom.Element;

/**
 * Parses the &lt;foreignKey&gt; part of a &lt;check&gt; and creates a SQL defect query.<br/><br/>
 * Created: 26.05.2011 11:39:27
 * @since 0.8
 * @author Volker Bergmann
 */
public class ForeignKeyParser extends AbstractDbSanityXMLParser {

    private static final String DEFAULT_DEFECT_TYPE = "foreignKey";

    private static final Set<String> REQUIRED_ATTRIBUTES = CollectionUtil.toSet("refererColumns", "refereeTable", "refereeColumns");

    private static final Set<String> OPTIONAL_ATTRIBUTES = CollectionUtil.toSet(REPORT_COLUMNS);

    public ForeignKeyParser() {
        super("foreignKey", REQUIRED_ATTRIBUTES, OPTIONAL_ATTRIBUTES, StrategyBasedCheck.class);
    }

    @Override
    public Object parse(Element element, Object[] parentPath, DbSanityParseContext context) {
        String xml = XMLUtil.format(element);
        StrategyBasedCheck parent = (StrategyBasedCheck) this.parent(parentPath);
        String refererTableName = getTableName(parent, xml);
        String[] refererColumns = getRequiredAttribute("refererColumns", element).split(",");
        StringUtil.trimAll(refererColumns);
        String refereeTableName = getRequiredAttribute("refereeTable", element);
        String refereeColumnsSpec = getRequiredAttribute("refereeColumns", element);
        String reportColumnsSpec = getReportColumnsSpec(element);
        DefectQueryStrategy strategy = new ForeignKeyStrategy(refererTableName, refererColumns, refereeTableName, refereeColumnsSpec, reportColumnsSpec);
        parent.setStrategy(strategy);
        if (parent.getDefectType() == null) parent.setDefectType(DEFAULT_DEFECT_TYPE);
        return strategy;
    }
}
