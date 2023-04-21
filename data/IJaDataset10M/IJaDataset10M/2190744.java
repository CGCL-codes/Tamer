package samples;

import java.util.List;
import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(AccessType.FIELD)
public class FieldTest {

    public FieldTest(String s) {
        p1 = s;
    }

    public FieldTest() {
    }

    private String p1;

    protected String getP1() {
        return p1;
    }
}
