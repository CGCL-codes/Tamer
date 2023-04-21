package javango.contrib.databrowse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javango.contrib.hibernate.HibernateUtil;
import javango.contrib.hibernate.Pagination;
import javango.db.ManagerException;
import javango.http.HttpRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

public class ObjectDetail {

    private static final Log log = LogFactory.getLog(ObjectDetail.class);

    protected List<String> property_list;

    protected List<String> pk_list;

    public void generate(List list, PersistentClass pc) {
        pk_list = new ArrayList<String>();
        Property pkProperty = pc.getIdentifierProperty();
        pk_list.add(pkProperty.getName());
        property_list = new ArrayList<String>();
        Iterator<Property> i = pc.getPropertyIterator();
        while (i.hasNext()) {
            Property p = i.next();
            property_list.add(p.getName());
        }
    }

    public String asTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("<thead><tr><th>id</th>");
        for (String property : pk_list) {
            builder.append("<th>");
            builder.append(property);
            builder.append("</th>");
        }
        builder.append("</tr></thead>");
        builder.append("<tbody>");
        return builder.toString();
    }

    public List<String> getProperty_list() {
        return property_list;
    }

    public List<String> getPk_list() {
        return pk_list;
    }
}
