package es.eucm.eadventure.editor.control.controllers.metadata.lom;

import es.eucm.eadventure.editor.data.meta.lom.LOMTechnical;
import es.eucm.eadventure.editor.control.config.LOMConfigData;

public class LOMTechnicalDataControl {

    public static final String GROUP = "technical";

    private LOMTechnical data;

    public LOMTechnicalDataControl(LOMTechnical data) {
        this.data = data;
    }

    public LOMTextDataControl getMaximumVersionController() {
        return new LOMTextDataControl() {

            public String getText() {
                return data.getMaximumVersion();
            }

            public void setText(String text) {
                data.setMaximumVersion(text);
                LOMConfigData.storeData(GROUP, "maximumVersion", text);
            }
        };
    }

    public LOMTextDataControl getMinimumVersionController() {
        return new LOMTextDataControl() {

            public String getText() {
                return data.getMinimumVersion();
            }

            public void setText(String text) {
                data.setMinimumVersion(text);
                LOMConfigData.storeData(GROUP, "minimumVersion", text);
            }
        };
    }

    /**
     * @return the data
     */
    public LOMTechnical getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(LOMTechnical data) {
        this.data = data;
    }
}
