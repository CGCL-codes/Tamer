package net.sf.amemailchecker.app.model;

public interface UIPanelModel<VO> {

    void fill();

    VO acceptChanges();

    VO declineChanges();

    void fireModelUpdate(VO input);
}
