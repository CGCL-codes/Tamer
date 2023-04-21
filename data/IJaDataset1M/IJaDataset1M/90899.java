package com.isdinvestments.cam.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "POSITION_TYPE")
public class PositionStatus extends BaseReferenceData {

    private static final long serialVersionUID = 1L;

    public PositionStatus() {
        super();
    }
}
