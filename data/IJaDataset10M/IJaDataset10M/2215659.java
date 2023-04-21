package at.riemers.zero.base.controller;

/**
 *
 * @author tobias
 */
public class PopupInfo {

    private String popupcaption;

    private int popuptype;

    private String alignId;

    private String align1;

    private String align2;

    private String cancelAction;

    public PopupInfo() {
    }

    public PopupInfo(String popupcaption, int popuptype, String alignId, String align1, String align2) {
        this(popupcaption, popuptype, alignId, align1, align2, "");
    }

    public PopupInfo(String popupcaption, int popuptype, String alignId, String align1, String align2, String cancelAction) {
        this.popupcaption = popupcaption;
        this.popuptype = popuptype;
        this.align1 = align1;
        this.align2 = align2;
        this.alignId = alignId;
        this.cancelAction = cancelAction;
    }

    public String getAlign1() {
        return align1;
    }

    public void setAlign1(String align1) {
        this.align1 = align1;
    }

    public String getAlign2() {
        return align2;
    }

    public void setAlign2(String align2) {
        this.align2 = align2;
    }

    public String getPopupcaption() {
        return popupcaption;
    }

    public void setPopupcaption(String popupcaption) {
        this.popupcaption = popupcaption;
    }

    public int getPopuptype() {
        return popuptype;
    }

    public void setPopuptype(int popuptype) {
        this.popuptype = popuptype;
    }

    public String getAlignId() {
        return alignId;
    }

    public void setAlignId(String alignId) {
        this.alignId = alignId;
    }

    public String getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(String cancelAction) {
        this.cancelAction = cancelAction;
    }
}
