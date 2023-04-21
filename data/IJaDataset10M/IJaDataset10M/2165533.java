package fi.arcusys.commons.j2ee.resolver.util;

/**
 * @author mikko
 * @version 1.0 $Rev$
 */
public class DefaultDN<T> extends AbstractDN<T> {

    private static final long serialVersionUID = 1L;

    public DefaultDN() {
    }

    @Override
    public String getDisplayId() {
        return getEntity().toString();
    }

    @Override
    public void init() {
    }
}
