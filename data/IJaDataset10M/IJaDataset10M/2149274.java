package org.atricore.idbus.kernel.main.authn;

/**
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Id: JOSSO11BindingRouteTest.java 1077 2009-03-20 22:27:50Z ajadzinsky $
 */
public class SecurityTokenImpl<T> implements SecurityToken<T> {

    private String id;

    private T content;

    private String nameIdentifier;

    public SecurityTokenImpl(String id, T content) {
        this(id, null, content);
    }

    public SecurityTokenImpl(String id, String nameIdentifier, T content) {
        this.id = id;
        this.nameIdentifier = nameIdentifier;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public T getContent() {
        return content;
    }

    public String getNameIdentifier() {
        return nameIdentifier;
    }
}
