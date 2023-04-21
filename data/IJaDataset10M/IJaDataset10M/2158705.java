package edu.unibi.agbi.webservice.client.service.dawismd.objects;

import edu.unibi.agbi.webservice.client.service.xml.ResourceLibrary;

public class Data {

    private final String identifier;

    private final Domains domain;

    private final DataSource data_source;

    private String org = ResourceLibrary.geServiceResource("name.na");

    /**
	 * @param identifier
	 * @param domain
	 * @param data_source
	 */
    public Data(String identifier, Domains domain, DataSource data_source) {
        this.identifier = identifier;
        this.domain = domain;
        this.data_source = data_source;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Domains getDomain() {
        return domain;
    }

    public DataSource getDataSource() {
        return data_source;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data_source == null) ? 0 : data_source.hashCode());
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Data other = (Data) obj;
        if (data_source != other.data_source) return false;
        if (domain != other.domain) return false;
        if (identifier == null) {
            if (other.identifier != null) return false;
        } else if (!identifier.equals(other.identifier)) return false;
        return true;
    }
}
