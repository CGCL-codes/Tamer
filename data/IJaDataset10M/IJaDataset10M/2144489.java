package org.plazmaforge.bsolution.base.common.beans;

import org.plazmaforge.framework.core.data.Dictionary;

/**
 * @author Oleh Hapon Date: 17.09.2004 Time: 7:35:08 $Id: Language.java,v 1.3 2010/05/17 10:39:24 ohapon Exp $
 */
public class Language extends Dictionary {

    private String code3;

    private String numericCode;

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getAlphaCode2() {
        return getCode();
    }

    public void setAlphaCode2(String code) {
        setCode(code);
    }

    public String getAlphaCode3() {
        return getCode3();
    }

    public void setAlphaCode3(String code) {
        setCode3(code);
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }
}
