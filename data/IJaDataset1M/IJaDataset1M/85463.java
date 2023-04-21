package org.iptc.ines.factory.catalog;

import java.net.URI;
import java.net.URISyntaxException;
import org.iptc.ines.NewsMLContext;
import org.iptc.ines.factory.type.LabelTypeFactory;
import org.iptc.nar.core.datatype.LabelType;
import org.iptc.nar.core.datatype.SchemaDeclarationType;
import org.iptc.nar.core.model.Catalog;

public class CatalogFactory {

    private Catalog m_catalog = new Catalog();

    private NewsMLContext m_context;

    public CatalogFactory(NewsMLContext context) {
        m_context = context;
    }

    /**
	 * Build catalog with new entry
	 * 
	 * @param alias
	 * @param scheme
	 * @throws URISyntaxException
	 */
    public void addEntry(String alias, String scheme) throws URISyntaxException {
        SchemaDeclarationType schemaDeclarationType = new SchemaDeclarationType();
        schemaDeclarationType.setSchemeAlias(alias);
        schemaDeclarationType.setSchemeURI(new URI(scheme));
        m_catalog.addSchemeDeclaration(schemaDeclarationType);
        if (m_context.isControledVocabularies()) m_context.getCatalogContext().registerSchemaDeclarationType(schemaDeclarationType);
    }

    public void addTitle(LabelType title) {
        m_catalog.addTitle(title);
    }

    public void addSimpleTitle(String title) {
        LabelType label = LabelTypeFactory.INSTANCE.createLabel(title);
        m_catalog.addTitle(label);
    }

    public Catalog getCatalog() {
        return m_catalog;
    }
}
