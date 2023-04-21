package org.gamio.conf;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 19 $ $Date: 2008-09-26 19:00:58 -0400 (Fri, 26 Sep 2008) $
 */
public class BasicProps {

    private String id = null;

    private String name = null;

    public BasicProps() {
    }

    public BasicProps(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }
}
