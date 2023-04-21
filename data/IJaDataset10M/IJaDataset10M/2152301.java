package flexmud.engine.context;

import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "context_group")
public class ContextGroup {

    public static final String ID_PROPERTY = "id";

    public static final String CONTEXT_PROPERTY = "context";

    public static final String CHILD_CONTEXTS_PROPERTY = "childContexts";

    private long id;

    private Context context;

    private Set<Context> childContexts = new HashSet<Context>();

    @Id
    @GeneratedValue
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToOne(mappedBy = Context.CHILD_GROUP_PROPERTY)
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @OneToMany(mappedBy = Context.PARENT_GROUP_PROPERTY, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Context> getChildContexts() {
        return childContexts;
    }

    public void setChildContexts(Set<Context> childContexts) {
        this.childContexts = childContexts;
    }
}
