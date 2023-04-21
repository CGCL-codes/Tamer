package transmissions.responses;

import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user_id_repsonse_failed")
public class UserIDResponse_Failed extends Response {

    @XmlElement(required = true, name = "user_name")
    protected String userName;

    @XmlElement(required = true)
    protected String reason;

    protected UserIDResponse_Failed() {
    }

    public UserIDResponse_Failed(UUID uuid) {
        super(uuid);
    }

    public UserIDResponse_Failed(UUID uuid, String userName, String reason) {
        super(uuid);
        this.userName = userName;
        this.reason = reason;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getUserName() {
        return userName;
    }
}
