package genomemap.cef.pui;

import cef.pui.EventConstructionException;
import cef.pui.EventImpl;
import cef.pui.pob.BuilderException;
import genomemap.data.OrganismData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import commons.util.PropertyUtil;
import commons.util.Util;
import genomemap.cef.event.ClassifyGenesEvent;
import genomemap.cef.pui.pob.OrganismDataBuilder;

/**
 * @version 1.0 Nov 21, 2010
 * @author Susanta Tewari
 */
public final class ClassifyGenesEventImpl extends EventImpl implements ClassifyGenesEvent {

    public static final String GENES_DATA_URL_PROP = "genes-data-url";

    private OrganismData organismData;

    private OrganismDataBuilder organismDataBuilder;

    private Set<String> genesClassified;

    public ClassifyGenesEventImpl(Properties props) throws EventConstructionException {
        super(props);
        String genesDataURLPropKey = getPropertySuffix() + EventImpl.PROP_DELIMETER + GENES_DATA_URL_PROP;
        PropertyUtil.validateProperty(genesDataURLPropKey, props.getProperty(genesDataURLPropKey));
        try {
            organismDataBuilder = new OrganismDataBuilder(props);
            organismData = organismDataBuilder.build();
        } catch (BuilderException ex) {
            throw new EventConstructionException("Error getting OrganismData for ClassifyGenesEventImpl", ex);
        }
        try {
            genesClassified = new HashSet<String>(Util.IO.readLines(new File(props.getProperty(genesDataURLPropKey))));
        } catch (FileNotFoundException ex) {
            throw new EventConstructionException("Error getting input genes data for ClassifyGenesEventImpl", ex);
        } catch (IOException ex) {
            throw new EventConstructionException("Error getting input genes data for ClassifyGenesEventImpl", ex);
        }
    }

    /**
     *
     * @return getGenesToBeClassified to be classified into chromosomes
     */
    @Override
    public Set<String> getGenesToBeClassified() {
        return genesClassified;
    }

    /**
     *
     * @return input data used in analysis
     */
    @Override
    public OrganismData getOrganismDataUsed() {
        return organismData;
    }

    @Override
    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder(super.getDescription() + "\n");
        stringBuilder.append("Input genes:").append(" URL: " + props.getProperty(GENES_DATA_URL_PROP)).append(" Genes #: " + genesClassified.size() + "\n");
        stringBuilder.append("Organism data used: ").append(organismDataBuilder.getDescription());
        return stringBuilder.toString();
    }

    @Override
    protected String getPrpertyPrefix() {
        return ClassifyGenesEventImpl.getPropertySuffix();
    }

    public static String getPropertySuffix() {
        return ClassifyGenesEventImpl.class.getName();
    }
}
