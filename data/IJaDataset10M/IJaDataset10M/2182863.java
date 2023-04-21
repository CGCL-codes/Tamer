package com.greentea.relaxation.jnmf.gui.components.project.data.transformation.clasterization;

import com.greentea.relaxation.jnmf.gui.components.project.data.transformation.AbstractDataVisualizationComponent;
import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.parameters.annotations.Configurable;
import com.greentea.relaxation.jnmf.parameters.annotations.NotParameter;
import com.greentea.relaxation.jnmf.util.data.LearningData;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.localization.Localizer;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 22.02.2009 Time: 17:17:45 To
 * change this template use File | Settings | File Templates.
 */
@Configurable
public class DataClasterizationComponent extends AbstractDataVisualizationComponent {

    @NotParameter
    private Art2mComponent art2mComponent;

    public DataClasterizationComponent() {
        super(Localizer.getString(StringId.CLUSTERIZATION), null);
        setConfigurable(this);
        setEnabled(false);
        setIcon(new ImageIcon(MainFrame.ICONS_DIR + "pfeil2.gif"));
        art2mComponent = new Art2mComponent();
        addChild(art2mComponent);
    }

    public LearningData clasterizeData(LearningData data) {
        if (isEnabled()) {
            data = art2mComponent.clasterizeData(data);
        }
        updateDataTable(data);
        return data;
    }
}
