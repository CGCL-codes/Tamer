package genomancer.ucsc.das2.modelimpl;

import genomancer.trellis.das2.model.Das2SourceI;
import genomancer.vine.das2.client.modelimpl.Das2Source;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UcscSource extends Das2Source implements Das2SourceI {

    String name;

    String id;

    String clade;

    int priority;

    UcscSourcesCapability ucsc_sources_cap;

    /**
     *  Assumes ResultSet contains all columns in hgcentral genomeClade table ("genome", "clade", "priority");
     *    (can have others, for example from a join)
     */
    public UcscSource(URI base_uri, ResultSet rs, UcscSourcesCapability ucsc_sources_cap) throws SQLException {
        super(base_uri, rs.getString("genome").replace(' ', '_'), rs.getString("genome"), null, null);
        name = rs.getString("genome");
        id = name.replace(' ', '_');
        clade = rs.getString("clade");
        priority = rs.getInt("priority");
        this.ucsc_sources_cap = ucsc_sources_cap;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClade() {
        return clade;
    }

    public int getPriority() {
        return priority;
    }

    public UcscSourcesCapability getSourcesCapability() {
        return ucsc_sources_cap;
    }
}
