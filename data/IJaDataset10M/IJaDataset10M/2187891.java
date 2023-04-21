package org.jprovocateur.basis.objectmodel.session;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.jprovocateur.serializer.XStream;
import org.jprovocateur.serializer.io.json.JsonHierarchicalStreamDriver;

/**
 * <p/>
 *
 * @author Michael Pitsounis 
 */
@Entity
@Table(name = "web_properties")
@SuppressWarnings("serial")
public class SessionDB {

    @Transient
    private static Logger logger = Logger.getLogger(SessionDB.class.getName());

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basis_gen")
    @SequenceGenerator(name = "basis_gen", sequenceName = "basis_seq")
    private Long id;

    @Column
    private String userid;

    @Column(name = "property_name")
    private String name;

    @Column(name = "property_value")
    private String value;

    @Transient
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String transformOutput(List obj) {
        try {
            XStream xstream = null;
            xstream = new XStream(new JsonHierarchicalStreamDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.alias("bcObject", SessionDB.class);
            xstream.omitField(SessionDB.class, "id");
            xstream.omitField(SessionDB.class, "appname");
            String returnStr = xstream.toXML(obj);
            returnStr = returnStr.replace("{\"list\":", "");
            returnStr = returnStr.substring(0, returnStr.length() - 1);
            return returnStr;
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }
}
