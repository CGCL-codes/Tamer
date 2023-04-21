package de.ipkgatersleben.agbi.uploader.gui.panels.parameter.selection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;
import de.ipkgatersleben.agbi.uploader.gui.bannerPanel.BannerPanel;
import de.ipkgatersleben.agbi.uploader.gui.bannerPanel.IBannelPanel;
import de.ipkgatersleben.agbi.uploader.model.UplParameter;

public class ParametersInputGUI extends JPanel implements IParametersInput {

    private static final long serialVersionUID = 1L;

    private BannerPanel bannerPanel = null;

    private JPanel content = null;

    private List<UplParameter> parameters;

    /**
	 * This is the default constructor
	 */
    public ParametersInputGUI() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.fill = GridBagConstraints.BOTH;
        gridBagConstraints2.insets = new Insets(0, 12, 12, 12);
        gridBagConstraints2.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 12, 0);
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.gridy = 0;
        this.setBorder(IBannelPanel.BANNERBODER);
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add(getBannerPanel(), gridBagConstraints);
        this.add(getContent(), gridBagConstraints2);
    }

    /**
	 * This method initializes bannerPanel	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.util.BannerPanel	
	 */
    private BannerPanel getBannerPanel() {
        if (bannerPanel == null) {
            bannerPanel = new BannerPanel();
            bannerPanel.setTitle("Parameter");
        }
        return bannerPanel;
    }

    /**
	 * This method initializes content	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getContent() {
        if (content == null) {
            content = new JPanel();
            content.setLayout(new GridBagLayout());
        }
        return content;
    }

    public List<UplParameter> getParameters() {
        return parameters;
    }

    private void buildContendPanel(Collection<UplParameter> parameters) {
        getContent().removeAll();
        int count = 0;
        for (UplParameter parameter : parameters) {
            if (parameter.isParameterVisible()) {
                getContent().add(new ParameterInputGUI(parameter));
                count++;
            }
        }
        getContent().setLayout(new GridLayout(count, 1, 12, 12));
        getContent().setVisible(false);
        getContent().setVisible(true);
    }

    public void setParameters(List<UplParameter> parameters) {
        this.parameters = parameters;
        buildContendPanel(parameters);
    }
}
