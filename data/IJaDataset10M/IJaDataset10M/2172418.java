package KFrameWork1.Server;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Administrator
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultSetClass", propOrder = { "session_id", "status", "desc", "start", "lengh", "column_names", "column_types", "records" })
public class ResultSetClass {

    @XmlElement(required = true)
    String session_id;

    @XmlElement(required = true)
    String status;

    @XmlElement(required = true)
    String desc;

    @XmlElement(required = true)
    int start = 0;

    @XmlElement(required = true)
    int lengh = 0;

    @XmlElement(required = true)
    ArrayList<String> column_names;

    @XmlElement(required = true)
    ArrayList<String> column_types;

    @XmlElement(required = true)
    ArrayList<RecordClass> records;

    public ResultSetClass() {
        status = "";
        desc = "";
        session_id = "";
        start = 0;
        lengh = 0;
        column_names = new ArrayList<String>();
        column_types = new ArrayList<String>();
        records = new ArrayList<RecordClass>();
    }
}
