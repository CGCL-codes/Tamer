package honeycrm.server.profiling;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ReadTest {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;

    private long foo;

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public long getFoo() {
        return foo;
    }

    public void setFoo(long foo) {
        this.foo = foo;
    }
}
