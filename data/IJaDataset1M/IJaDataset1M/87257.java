package uk.ac.ebi.intact.core.batch.reader;

import org.springframework.batch.item.database.JpaPagingItemReader;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: InteractionReader.java 17244 2011-09-30 14:31:53Z mdumousseau@yahoo.com $
 */
public class InteractionReader extends JpaPagingItemReader {

    private boolean excludeNegative = false;

    public InteractionReader() {
        super();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String query = "select i from InteractionImpl i";
        if (isExcludeNegative()) {
            query = query + " where i.ac not in " + "(select i2.ac from InteractionImpl i2 join i2.annotations as annot where annot.cvTopic.shortLabel = 'negative') order by i.ac";
        } else {
            query += " order by i.ac";
        }
        setQueryString(query);
        super.afterPropertiesSet();
    }

    public boolean isExcludeNegative() {
        return excludeNegative;
    }

    public void setExcludeNegative(boolean excludeNegative) {
        this.excludeNegative = excludeNegative;
    }
}
