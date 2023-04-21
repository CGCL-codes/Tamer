package netgest.bo.xwc.framework;

import java.io.Serializable;
import javax.faces.event.ActionEvent;
import netgest.bo.xwc.framework.components.XUIComponentBase;

public class XUIActionEvent extends ActionEvent implements Serializable {

    private static final long serialVersionUID = 2209230278658013420L;

    public XUIActionEvent(ActionEvent oEvent) {
        super(oEvent.getComponent());
        this.setPhaseId(oEvent.getPhaseId());
    }

    @Override
    public XUIComponentBase getComponent() {
        return (XUIComponentBase) super.getComponent();
    }
}
