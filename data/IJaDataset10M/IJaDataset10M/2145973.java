package dbmi_server.database;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Airport {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String icaoCode;

    @Persistent
    private String name;

    @Persistent
    private Country country;

    @Persistent
    private MetarInformation metarInformation;

    public Airport(String icaoCode, String name, MetarInformation metarInformation) {
        this.icaoCode = icaoCode;
        this.name = name;
        this.metarInformation = metarInformation;
    }

    public MetarInformation getMetarInformation() {
        return metarInformation;
    }

    public Key getKey() {
        return key;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }
}
