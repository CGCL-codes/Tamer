package org.tolven.ctom.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A non-measurable lesion is a lesion that is not determined as measurable with
 * convestional techniques, or non-measurable by nature such as bone lesions,
 * ascites, cystic lesions, etc.
 * @version 1.0
 * @created 27-Sep-2006 9:56:24 AM
 */
@Entity
@DiscriminatorValue("NML")
public class NonMeasurable extends LesionEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Code to follow the response of a specific non-measurable lesion, as specified
	 * at the time of each evaluation. Values include: N - New; R - Resolved; D -
	 * Decreasing; I - Increasing; S - Stable; B - Baseline.
	 */
    @Column
    private String code;

    public NonMeasurable() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
