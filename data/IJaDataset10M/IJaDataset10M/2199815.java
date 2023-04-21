package wickettree.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sven Meier
 */
public class Foo {

    private static final long serialVersionUID = 1L;

    private String id;

    private String bar;

    private String baz;

    private boolean quux;

    private boolean loaded;

    private Foo parent;

    private List<Foo> foos = new ArrayList<Foo>();

    public Foo(String id) {
        this.id = id;
        this.bar = id.toLowerCase() + "Bar";
        this.baz = id.toLowerCase() + "Baz";
    }

    public Foo(Foo parent, String name) {
        this(name);
        this.parent = parent;
        this.parent.foos.add(this);
    }

    public Foo getParent() {
        return parent;
    }

    public String getId() {
        return id;
    }

    public String getBar() {
        return bar;
    }

    public String getBaz() {
        return baz;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public void setBaz(String baz) {
        this.baz = baz;
    }

    public void setQuux(boolean quux) {
        this.quux = quux;
        if (quux) {
            for (Foo foo : foos) {
                foo.setQuux(true);
            }
        } else {
            if (parent != null) {
                parent.setQuux(false);
            }
        }
    }

    public boolean getQuux() {
        return quux;
    }

    public List<Foo> getFoos() {
        return Collections.unmodifiableList(foos);
    }

    @Override
    public String toString() {
        return id;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
