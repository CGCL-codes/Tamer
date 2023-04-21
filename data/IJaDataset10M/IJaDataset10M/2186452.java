package interfaces;

import java.util.LinkedList;
import java.util.List;
import logic.nodes.nodeSettings.Settings;
import org.fenggui.Container;
import org.fenggui.Label;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.RowLayout;
import org.fenggui.render.Font;
import fileHandling.language.LanguageLoader;

public class InfoContainer extends Container {

    private List<InfoLabelItem> labelMap;

    private Container westPanel, eastPanel;

    private Settings settings;

    private Font font;

    private List<String> exceptions;

    public InfoContainer(Settings settings, List<String> exceptions, Font font) {
        super(new BorderLayout());
        this.settings = settings;
        this.exceptions = exceptions;
        this.font = font;
        if (exceptions == null) this.exceptions = new LinkedList<String>();
        initPanels();
        if (settings != null) initLabels();
    }

    private void initPanels() {
        westPanel = new Container(new RowLayout(false));
        westPanel.setLayoutData(BorderLayoutData.WEST);
        eastPanel = new Container(new RowLayout(false));
        eastPanel.setLayoutData(BorderLayoutData.EAST);
        addWidget(westPanel);
        addWidget(eastPanel);
        layout();
    }

    private void initLabels() {
        labelMap = new LinkedList<InfoLabelItem>();
        for (String key : settings.getKeys()) {
            if (exceptions.contains(key)) continue;
            Label descrLabel = new Label(LanguageLoader.get(key));
            descrLabel.getAppearance().setFont(font);
            westPanel.addWidget(descrLabel);
            Label updateLabel = new Label("               ");
            updateLabel.getAppearance().setFont(font);
            eastPanel.addWidget(updateLabel);
            InfoLabelItem labelItem = new InfoLabelItem(descrLabel, updateLabel, key);
            labelMap.add(labelItem);
        }
        layout();
    }

    public void updateLabels(Settings settings) {
        this.settings = settings;
        if (labelMap == null) initLabels();
        for (InfoLabelItem labelItem : labelMap) {
            for (String key : settings.getKeys()) {
                if (key.equals(labelItem.getDescription())) {
                    labelItem.setValueLabelText(settings.getValueOf(key));
                    break;
                }
            }
        }
        layout();
    }
}
