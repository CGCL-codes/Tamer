package br.uerj.eng.geomatica.interoperability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import br.uerj.eng.geomatica.interoperability.exception.ObjectNotValidException;
import br.uerj.eng.geomatica.interoperability.exception.XMLObjectNotFoundException;
import br.uerj.eng.geomatica.interoperability.exception.XMLStoreException;
import br.uerj.eng.geomatica.interoperability.model.DataSource;
import br.uerj.eng.geomatica.interoperability.model.Domain;
import br.uerj.eng.geomatica.interoperability.model.GeographicMap;
import br.uerj.eng.geomatica.interoperability.model.WFSDataSource;

/**
 * Configuration service to ontology-based interoperability.
 * 
 * @author Victor Azevedo (reference Azevedo, V. H. M.)
 */
public class ConfigurationService extends XMLStore implements Configurable {

    /**
	 * Collection of available domains of interest.
	 */
    private Collection<Domain> domains;

    /**
	 * Collection of available spatial data sources (WFS).
	 */
    private Collection<DataSource> dataSources;

    /**
	 * Collection of available maps.
	 */
    private Collection<GeographicMap> maps;

    /**
	 * @see Configurable#restoreDataSources()
	 */
    public Collection<DataSource> restoreDataSources() {
        try {
            if (dataSources == null) dataSources = (ArrayList<DataSource>) this.xml2Object(Configurable.DATASOURCES_FILE_NAME);
        } catch (XMLStoreException e) {
            e.printStackTrace();
        } catch (XMLObjectNotFoundException e) {
            e.printStackTrace();
        }
        if (dataSources == null) dataSources = new ArrayList<DataSource>();
        return dataSources;
    }

    /**
	 * @see Configurable#restoreDomains()
	 */
    public Collection<Domain> restoreDomains() {
        try {
            if (domains == null) domains = (ArrayList<Domain>) this.xml2Object(Configurable.DOMAINS_FILE_NAME);
        } catch (XMLStoreException e) {
            e.printStackTrace();
        } catch (XMLObjectNotFoundException e) {
            e.printStackTrace();
        }
        if (domains == null) domains = new ArrayList<Domain>();
        return domains;
    }

    /**
	 * Save the state of spatial interoperability configuration storing data
	 * sources and domains.
	 */
    public void save() {
        storeDomains(domains);
        storeDataSources(dataSources);
        storeMaps(maps);
    }

    /**
	 * Initialize the spatial interoperability configuration restoring data
	 * sources and domains.
	 */
    public void startup() {
        restoreDomains();
        restoreDataSources();
        restoreMaps();
    }

    /**
	 * @see Configurable#storeDataSources(Collection)
	 */
    public void storeDataSources(Collection col) {
        try {
            object2XML(col, Configurable.DATASOURCES_FILE_NAME);
            this.dataSources = col;
        } catch (XMLStoreException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @see Configurable#storeDomains(Collection)
	 */
    public void storeDomains(Collection<Domain> col) {
        try {
            object2XML(col, Configurable.DOMAINS_FILE_NAME);
            this.domains = col;
        } catch (XMLStoreException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Update the configuration with a new collection of data sources.
	 * 
	 * @param col
	 *            collection of data sources;
	 * @return nothing
	 * @throws ObjectNotValidException
	 *             If any object into collection is different from DataSource.
	 */
    public void setDataSources(Collection col) throws ObjectNotValidException {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            if (!(it.next() instanceof WFSDataSource)) {
                throw new ObjectNotValidException("There is objects different from 'DataSource' into Collection.");
            }
        }
        this.dataSources = col;
    }

    /**
	 * Update the configuration with a new collection of domains.
	 * 
	 * @param col
	 *            collection of domains;
	 * @return nothing
	 * @throws ObjectNotValidException
	 *             If any object into collection is different from Domain.
	 */
    public void setDomains(Collection<Domain> col) throws ObjectNotValidException {
        Iterator<Domain> it = col.iterator();
        while (it.hasNext()) {
            if (!(it.next() instanceof Domain)) {
                throw new ObjectNotValidException("There is objects different from 'Domain' into Collection.");
            }
        }
        this.domains = col;
    }

    /**
	 * Search for a single domain by a identifier code.
	 * 
	 * @param code
	 *            identifier of domain;
	 * @return Domain The Domain found into configuration collection
	 */
    public Domain getDomain(int code) {
        Domain domain = null;
        if (domains != null && !domains.isEmpty()) {
            Iterator<Domain> it = domains.iterator();
            while (it.hasNext()) {
                Domain d = it.next();
                if (d.getCode() == code) {
                    domain = d;
                    break;
                }
            }
        }
        return domain;
    }

    /**
	 * Search for a single data source by a identifier code.
	 * 
	 * @param code
	 *            identifier of data source;
	 * @return DataSource The data source found into configuration collection
	 */
    public WFSDataSource getDataSource(int code) {
        WFSDataSource ds = null;
        if (dataSources != null && !dataSources.isEmpty()) {
            Iterator it = dataSources.iterator();
            while (it.hasNext()) {
                WFSDataSource f = (WFSDataSource) it.next();
                if (f.getCode() == code) {
                    ds = f;
                    break;
                }
            }
        }
        return ds;
    }

    /**
	 * Search for data sources from a specific domain.
	 * 
	 * @param domain
	 *            domain of interest;
	 * @return DataSource Collection
	 */
    public Collection<DataSource> getDataSources(Domain domain) {
        Collection<DataSource> dsCol = new ArrayList<DataSource>();
        restoreDataSources();
        if (dataSources != null && !dataSources.isEmpty()) {
            Iterator it = dataSources.iterator();
            while (it.hasNext()) {
                WFSDataSource ds = (WFSDataSource) it.next();
                if (ds.getDomain() != null && ds.getDomain().equals(domain)) {
                    dsCol.add(ds);
                }
            }
        }
        return dsCol;
    }

    public Collection<GeographicMap> restoreMaps() {
        try {
            if (maps == null) maps = (ArrayList<GeographicMap>) this.xml2Object(Configurable.MAPS_FILE_NAME);
        } catch (XMLStoreException e) {
            e.printStackTrace();
        } catch (XMLObjectNotFoundException e) {
            e.printStackTrace();
        }
        if (maps == null) maps = new ArrayList<GeographicMap>();
        return maps;
    }

    public void storeMaps(Collection<GeographicMap> col) {
        try {
            object2XML(col, Configurable.MAPS_FILE_NAME);
            this.maps = col;
        } catch (XMLStoreException e) {
            e.printStackTrace();
        }
    }
}
