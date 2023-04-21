package cn.adamkts.mail;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

/**
 * 邮件抽象,表示一个具体的邮件内容
 * @author Atomic
 */
public class MailEntity implements Serializable {

    private static final long serialVersionUID = -5476694965258044689L;

    Logger logger = Logger.getLogger("~~com.iiapk.entitydebugger.MAILEntity~~");

    private int id;

    private Date sendDate;

    private String target;

    private HashMap<String, Object> data = new HashMap<String, Object>(5);

    public MailEntity() {
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void putData(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
