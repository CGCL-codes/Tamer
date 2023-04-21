package gcr.rdf;

import gcr.mmm2.model.StringFormatException;
import gcr.mmm2.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Simon King
 *
 */
public class AnnotationWrapper {

    private Date m_timestamp;

    private List m_props;

    private Map m_propMap;

    private String m_classLabel, m_indURI, m_subjectURI, m_creatorURI, m_editSubjURI;

    private boolean m_hidden;

    public AnnotationWrapper(List props, String timestamp, String indURI, String subjectURI, String creatorURI, String editSubjURI, String hidden, Map propMap) {
        m_props = props;
        m_propMap = propMap;
        m_indURI = indURI;
        m_subjectURI = subjectURI;
        m_creatorURI = creatorURI;
        m_editSubjURI = editSubjURI;
        m_hidden = "true".equalsIgnoreCase(hidden);
        m_classLabel = RDFManipulator.getInstanceClassLabel(indURI);
        try {
            m_timestamp = StringUtils.parseXMLDate(timestamp);
        } catch (StringFormatException e) {
            m_timestamp = new Date();
        }
    }

    public List getFullPropList() {
        return (List) MMMModelFactory.getInstance().getClassMap().get(getPropMap().get("CLASS_URI"));
    }

    public Map getPropMap() {
        return m_propMap;
    }

    public Date getTimestamp() {
        return m_timestamp;
    }

    public List getPropList() {
        return m_props;
    }

    public String getClassLabel() {
        return m_classLabel;
    }

    public String getURI() {
        return m_indURI;
    }

    public String getSubjectURI() {
        return m_subjectURI;
    }

    public String getCreatorURI() {
        return m_creatorURI;
    }

    public String getEditSubjectURI() {
        return m_editSubjURI;
    }

    public boolean getHidden() {
        return m_hidden;
    }
}
