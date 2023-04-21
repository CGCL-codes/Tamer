package net.sf.sail.emf.bridge;

import net.sf.sail.core.entity.IAnnotation;
import net.sf.sail.emf.sailuserdata.EAnnotation;

/**
 * @author scott
 *
 */
public class EMFAnnotation implements IAnnotation {

    EAnnotation eAnnotation;

    Object sailEntity;

    public EMFAnnotation(EAnnotation eAnnotation, Object sailEntity) {
        this.eAnnotation = eAnnotation;
        this.sailEntity = sailEntity;
    }

    public String getContentType() {
        return eAnnotation.getContentType();
    }

    public String getContents() {
        return eAnnotation.getContents();
    }

    public Object getSailEntity() {
        return sailEntity;
    }

    public String getSource() {
        return eAnnotation.getSource();
    }

    public void setContentType(String contentType) {
        eAnnotation.setContentType(contentType);
    }

    public void setContents(String contents) {
        eAnnotation.setContents(contents);
    }

    public void setSailEntity(Object entity) {
        throw new UnsupportedOperationException();
    }

    public void setSource(String source) {
        eAnnotation.setSource(source);
    }
}
