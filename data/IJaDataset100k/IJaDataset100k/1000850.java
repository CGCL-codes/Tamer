package dk.mirasola.systemtraining.bridgewidgets.client.ui.distributionfiltereditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import dk.mirasola.systemtraining.bridgewidgets.client.ui.editor.GroupNameEditor;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.DistributionFilterGroup;

public class DistributionFilterGroupEditor extends Composite implements Editor<DistributionFilterGroup> {

    interface DistributionFilterGroupEditorUIBinder extends UiBinder<Widget, DistributionFilterGroupEditor> {
    }

    private static DistributionFilterGroupEditorUIBinder uiBinder = GWT.create(DistributionFilterGroupEditorUIBinder.class);

    @UiField
    GroupNameEditor nameEditor;

    @UiField
    CheckBox invertedEditor;

    public DistributionFilterGroupEditor() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
