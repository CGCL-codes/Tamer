package bufferings.lab.inmemory;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import com.google.appengine.api.datastore.Key;

@Model
public class Hoge {

    @Attribute(primaryKey = true)
    private Key key;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
