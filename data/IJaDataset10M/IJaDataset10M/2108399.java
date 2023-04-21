package org.rbx.sims.model.vo.collection;

import org.rbx.sims.model.vo.TermVO;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: jperez
 */
@XmlRootElement(name = "terms")
public class TermCollectionVO {

    private List<TermVO> terms;

    public TermCollectionVO() {
    }

    public TermCollectionVO(List<TermVO> collection) {
        this.terms = collection;
    }

    @XmlElement(name = "term")
    public List<TermVO> getTerms() {
        return terms;
    }

    public void setTerms(List<TermVO> terms) {
        this.terms = terms;
    }
}
