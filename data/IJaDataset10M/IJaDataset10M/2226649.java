package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 日计划定值单关系
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class DdDayplanfixvaluerelDto implements Serializable {

    public DdDayplanfixvaluerelDto() {
    }

    private String frelid;

    private int version;

    private com.techstar.dmis.dto.DdDoutageplanDto zdddayplanfixvaluerel2;

    private com.techstar.dmis.dto.BhFixedvaluebillDto zdddayplanfixvaluerel4;

    /**
     * getters and setters
     */
    public void setFrelid(String frelid) {
        this.frelid = frelid;
    }

    public String getFrelid() {
        return frelid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZdddayplanfixvaluerel2(com.techstar.dmis.dto.DdDoutageplanDto zdddayplanfixvaluerel2) {
        this.zdddayplanfixvaluerel2 = zdddayplanfixvaluerel2;
    }

    public com.techstar.dmis.dto.DdDoutageplanDto getZdddayplanfixvaluerel2() {
        return zdddayplanfixvaluerel2;
    }

    public void setZdddayplanfixvaluerel4(com.techstar.dmis.dto.BhFixedvaluebillDto zdddayplanfixvaluerel4) {
        this.zdddayplanfixvaluerel4 = zdddayplanfixvaluerel4;
    }

    public com.techstar.dmis.dto.BhFixedvaluebillDto getZdddayplanfixvaluerel4() {
        return zdddayplanfixvaluerel4;
    }
}
