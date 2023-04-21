package no.ugland.utransprod.gui.model;

import java.beans.PropertyChangeListener;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.ProcentDone;
import no.ugland.utransprod.util.Util;
import com.jgoodies.binding.PresentationModel;

public class ProcentDoneModel extends AbstractModel<ProcentDone, ProcentDoneModel> {

    public static final String PROPERTY_PROCENT_DONE_YEAR = "procentDoneYear";

    public static final String PROPERTY_PROCENT_DONE_WEEK = "procentDoneWeek";

    public static final String PROPERTY_PROCENT_STRING = "procentString";

    public static final String PROPERTY_PROCENT_DONE_COMMENT = "procentDoneComment";

    public ProcentDoneModel(ProcentDone object) {
        super(object);
    }

    public Integer getProcentDoneId() {
        return object.getProcentDoneId();
    }

    public Order getOrder() {
        return object.getOrder();
    }

    public Integer getProcentDoneYear() {
        if (object.getProcentDoneYear() != null) {
            return object.getProcentDoneYear();
        }
        return 0;
    }

    public void setProcentDoneYear(Integer aYear) {
        Integer oldYear = getProcentDoneYear();
        object.setProcentDoneYear(aYear);
        firePropertyChange(PROPERTY_PROCENT_DONE_YEAR, oldYear, aYear);
    }

    public Integer getProcentDoneWeek() {
        return object.getProcentDoneWeek();
    }

    public void setProcentDoneWeek(Integer aWeek) {
        Integer oldWeek = getProcentDoneWeek();
        object.setProcentDoneWeek(aWeek);
        firePropertyChange(PROPERTY_PROCENT_DONE_WEEK, oldWeek, aWeek);
    }

    public String getProcentString() {
        return Util.convertIntegerToString(object.getProcent());
    }

    public void setProcentString(String aProcent) {
        String oldProcent = getProcentString();
        object.setProcent(Util.convertStringToInteger(aProcent));
        firePropertyChange(PROPERTY_PROCENT_STRING, oldProcent, aProcent);
    }

    public String getProcentDoneComment() {
        return object.getProcentDoneComment();
    }

    public void setProcentDoneComment(String aComment) {
        String oldComment = getProcentDoneComment();
        object.setProcentDoneComment(aComment);
        firePropertyChange(PROPERTY_PROCENT_DONE_COMMENT, oldComment, aComment);
    }

    public Integer getProcent() {
        return object.getProcent();
    }

    @Override
    public void addBufferChangeListener(PropertyChangeListener listener, PresentationModel presentationModel) {
        presentationModel.getBufferedModel(PROPERTY_PROCENT_DONE_YEAR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PROCENT_DONE_WEEK).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PROCENT_STRING).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PROCENT_DONE_COMMENT).addValueChangeListener(listener);
    }

    @Override
    public ProcentDoneModel getBufferedObjectModel(PresentationModel presentationModel) {
        ProcentDoneModel procentDoneModel = new ProcentDoneModel(new ProcentDone());
        procentDoneModel.setProcentDoneYear((Integer) presentationModel.getBufferedValue(PROPERTY_PROCENT_DONE_YEAR));
        procentDoneModel.setProcentDoneWeek((Integer) presentationModel.getBufferedValue(PROPERTY_PROCENT_DONE_WEEK));
        procentDoneModel.setProcentString((String) presentationModel.getBufferedValue(PROPERTY_PROCENT_STRING));
        procentDoneModel.setProcentDoneComment((String) presentationModel.getBufferedValue(PROPERTY_PROCENT_DONE_COMMENT));
        return procentDoneModel;
    }
}
