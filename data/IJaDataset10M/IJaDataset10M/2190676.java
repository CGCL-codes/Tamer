package org.openremote.beehive.rest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.openremote.beehive.api.dto.CodeDTO;

/**
 * In order to let rest service to serialize list of Codes User: allenwei Date: 2009-2-10 Time: 13:57:29
 */
@XmlRootElement(name = "codes")
public class CodeListing {

    private List<CodeDTO> codes = new ArrayList<CodeDTO>();

    public CodeListing() {
    }

    public CodeListing(List<CodeDTO> codes) {
        this.codes = codes;
    }

    @XmlElement(name = "code")
    public List<CodeDTO> getCodes() {
        return codes;
    }

    public void setCodes(List<CodeDTO> codes) {
        this.codes = codes;
    }
}
