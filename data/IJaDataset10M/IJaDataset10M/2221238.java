package org.wfp.rita.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.wfp.rita.pojo.base.IdentifiedByCode;
import org.wfp.rita.pojo.face.DescribedObject;
import org.wfp.rita.pojo.face.Insecurable;

@Entity
@Table(name = "request_cust_type")
public class RequestCustType extends IdentifiedByCode implements DescribedObject, Insecurable {

    public enum Code {

        C, R
    }

    @Id
    @Column(name = "code", length = 1, columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private Code code;

    @Column(name = "description", nullable = false)
    private String description;

    /**
     * @return Returns the code.
     */
    public Code getCode() {
        return code;
    }

    /**
     * @param code The code to set.
     */
    public void setCode(Code code) {
        this.code = code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
