package org.fudaa.fudaa.meshviewer.profile;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.memoire.bu.BuResource;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ebli.animation.EbliAnimationAdapterInterface;
import org.fudaa.ebli.controle.BSelecteurListTimeTarget;

/**
 * @author fred deniger
 * @version $Id: MvProfileCourbeTime.java,v 1.6 2007-05-04 13:59:49 deniger Exp $
 */
public class MvProfileCourbeTime extends MvProfileCourbe implements BSelecteurListTimeTarget, ListSelectionListener, ListDataListener, EbliAnimationAdapterInterface {

    final ListSelectionModel selection_ = new DefaultListSelectionModel();

    ListModel time_;

    public MvProfileCourbeTime(final MvProfileCourbeGroup _m, final MVProfileCourbeModel _model, final ListModel _time) {
        super(_m, _model);
        time_ = _time;
        time_.addListDataListener(this);
        selection_.setSelectionInterval(_model.getTime(), _model.getTime());
        selection_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selection_.addListSelectionListener(this);
    }

    MvProfileCourbe duplicate() {
        final MvProfileCourbeTime res = new MvProfileCourbeTime((MvProfileCourbeGroup) getParent(), getM().duplicate(), time_);
        res.setAspectContour(getAspectContour());
        res.getM().setTitle(getTitle() + CtuluLibString.ESPACE + BuResource.BU.getString("Copie"));
        return res;
    }

    public void contentsChanged(final ListDataEvent _e) {
        updateTitle();
    }

    public int getNbTimeStep() {
        return time_.getSize();
    }

    public String getTimeStep(final int _idx) {
        return time_.getElementAt(_idx).toString();
    }

    public void setTimeStep(final int _idx) {
        if (_idx >= 0 && _idx < getNbTimeStep()) {
            selection_.setSelectionInterval(_idx, _idx);
        }
    }

    public ListModel getTimeListModel() {
        return time_;
    }

    public ListSelectionModel getTimeListSelectionModel() {
        return selection_;
    }

    public void intervalAdded(final ListDataEvent _e) {
    }

    public void intervalRemoved(final ListDataEvent _e) {
    }

    public void valueChanged(final ListSelectionEvent _e) {
        getM().setTime(selection_.getMaxSelectionIndex());
        updateTitle();
    }

    private void updateTitle() {
        setTitle(getM().getVariable().getName() + CtuluLibString.ESPACE + time_.getElementAt(getM().getTime()));
        fireCourbeContentChanged();
    }

    public void setVar(final CtuluVariable _v, final boolean _contentChanged) {
        getM().setVariable(_v, _contentChanged);
        if (_v != getM().getVariable()) {
            updateTitle();
        }
    }
}
