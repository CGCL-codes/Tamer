package ch.kerbtier.malurus.components.defaultrenderers;

import ch.kerbtier.malurus.components.SingleSelect;
import ch.kerbtier.malurus.updaters.JavaComponentUpdater;

public class SIngleSelectUpdater extends JavaComponentUpdater<SingleSelect> {

    @Override
    public void update() {
        String sValue = getRequest().getParameter(getSubject().getClientId());
        if (sValue != null) {
            try {
                int pos = Integer.parseInt(sValue);
                Object value = getSubject().getModel().get(pos);
                getSubject().getSelectionModel().setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
