package soma.rest.training.jerseySpringJDO.model;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.google.appengine.api.datastore.Key;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "key", "text", "value", "tag" })
@XmlRootElement(name = "sentense")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sentense implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String text;

    @Persistent
    private String value;

    @Persistent
    private String tag;

    public Sentense() {
        super();
    }

    public Sentense(String text, String value, String tag) {
        super();
        this.text = text;
        this.value = value;
        this.tag = tag;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Sentense [key = " + key + ", text = " + text + ", value = " + value + ", tag = " + tag + "]";
    }
}
