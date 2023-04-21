package uk.ac.ebi.intact.util.sanityChecker.model;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id: AnnotationBean.java 4217 2005-07-28 16:14:25Z catherineleroy $
 */
public class AnnotationBean extends IntactBean {

    private String ac;

    private String description;

    private String topic_ac;

    public AnnotationBean() {
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic_ac() {
        return topic_ac;
    }

    public void setTopic_ac(String topic_ac) {
        this.topic_ac = topic_ac;
    }
}
