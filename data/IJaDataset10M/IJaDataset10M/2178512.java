package medieveniti.object;

import java.util.Date;

/**
 * POJO für ein Captcha.
 * @author Hans Kirchner
 */
public class Captcha {

    private Integer id;

    private String code;

    private Date createTime;

    private Boolean imageCreated;

    public Captcha() {
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean isImageCreated() {
        return imageCreated;
    }

    public void setImageCreated(Boolean imageCreated) {
        this.imageCreated = imageCreated;
    }
}
