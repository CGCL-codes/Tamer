package net.sourceforge.openwebarch.app.thetimeloop.client.category.data;

import java.io.Serializable;

public class CategoryDetail implements Serializable {

    public Long id;

    public String name;

    @Override
    public String toString() {
        return "[id: " + id + "; name: " + name + "]";
    }
}
