package phex.gui.dialogs.configwizard;

import javax.swing.JPanel;
import phex.gui.common.HTMLMultiLinePanel;
import phex.gui.tabs.library.LibraryTreePane;
import phex.utils.Localizer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SharingPanel extends JPanel {

    private ConfigurationWizardDialog parent;

    public SharingPanel(ConfigurationWizardDialog parent) {
        this.parent = parent;
        prepareComponent();
    }

    private void prepareComponent() {
        FormLayout layout = new FormLayout("10dlu, d, 2dlu, d, right:d:grow", "p, 3dlu, p, 8dlu, fill:p:grow");
        setLayout(layout);
        PanelBuilder builder = new PanelBuilder(layout, this);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator(Localizer.getString("ConfigWizard_SharingHeader"), cc.xywh(1, 1, 5, 1));
        HTMLMultiLinePanel welcomeLines = new HTMLMultiLinePanel(Localizer.getString("ConfigWizard_SharingText"));
        welcomeLines.setBorder(null);
        builder.add(welcomeLines, cc.xywh(2, 3, 4, 1));
        LibraryTreePane libraryTree = new LibraryTreePane(this);
        builder.add(libraryTree, cc.xywh(2, 5, 4, 1));
    }
}
