package fr.itris.glips.extension.jwidget.trends.runtime.view.component.curvesproperties;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import java.util.*;

/**
 * the class of the header panel 
 * @author ITRIS, Jordi SUC
 */
public class HeaderPanel extends JPanel {

    /**
	 * the curves properties component
	 */
    private CurvesPropertiesComponent curvesPropertiesComponent;

    /**
	 * the text field used to display the curve tag name
	 */
    private JTextField curveTextField;

    /**
	 * the icon for display the current curve color
	 */
    private JLabel iconLbl;

    /**
	 * the popup menu enabling to choose a curve tag name 
	 */
    private JPopupMenu popupMenu;

    /**
	 * the button used to show the popup menu
	 */
    private JButton popupButton;

    /**
	 * the popup button listener
	 */
    private ActionListener buttonListener;

    /**
	 * the array of the menu items
	 */
    private JMenuItem[] menuItems;

    /**
	 * the array of the action listeners
	 */
    private ActionListener[] actionListeners;

    /**
	 * the button used to reinitialize the values of the curve
	 */
    private JButton reinitializeButton;

    /**
	 * the reinitialize button listener
	 */
    private ActionListener reinitializeButtonListener;

    /**
	 * the array of the listeners to curve configuration changes
	 */
    private CurvesConfigurationChangeListener[] configListeners;

    /**
	 * the current trends curve configuration object
	 */
    private TrendsCurveConfiguration currentConfig = null;

    /**
	 * the constructor of the class
	 * @param curvesPropertiesComponent the curves properties component
	 */
    public HeaderPanel(CurvesPropertiesComponent curvesPropertiesComponent) {
        this.curvesPropertiesComponent = curvesPropertiesComponent;
        build();
    }

    /**
	 * builds the component
	 */
    protected void build() {
        setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        TrendsConfiguration configuration = curvesPropertiesComponent.getComponentsHandler().getView().getController().getConfiguration();
        String curveLabel = TrendsBundle.bundle.getString("CurveCombo");
        String reinitializeCurveValuesLabel = TrendsBundle.bundle.getString("ReinitializeCurveValues");
        JLabel curveJLabel = new JLabel(curveLabel + " : ");
        curveJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLbl = new JLabel();
        curveTextField = new JTextField(12);
        curveTextField.setOpaque(false);
        curveTextField.setEditable(false);
        popupMenu = new JPopupMenu();
        LinkedList<TrendsCurveConfiguration> configs = configuration.getCurvesConfigurationList();
        menuItems = new JMenuItem[configs.size()];
        actionListeners = new ActionListener[configs.size()];
        configListeners = new CurvesConfigurationChangeListener[configs.size()];
        for (int i = 0; i < configs.size(); i++) {
            final TrendsCurveConfiguration config = configs.get(i);
            if (config != null) {
                menuItems[i] = new JMenuItem();
                menuItems[i].setText(config.getLabel().equals("") ? config.getTagName() : config.getTagName());
                menuItems[i].setIcon(createIcon(config.getColor()));
                actionListeners[i] = new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        setCurrentConfig(config);
                    }
                };
                menuItems[i].addActionListener(actionListeners[i]);
                final int j = i;
                configListeners[i] = new CurvesConfigurationChangeListener() {

                    @Override
                    public void curveColorChanged() {
                        Icon icon = createIcon(config.getColor());
                        if (currentConfig != null && currentConfig.equals(config)) {
                            iconLbl.setIcon(icon);
                        }
                        menuItems[j].setIcon(icon);
                    }
                };
                config.addCurvesConfigurationChangeListener(configListeners[i]);
                popupMenu.add(menuItems[i]);
            }
        }
        popupButton = new JButton();
        popupButton.setIcon(TrendsIcons.getIcon("CurveChooser", false));
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                popupMenu.show(popupButton, popupButton.getWidth() / 2, popupButton.getHeight() / 2);
            }
        };
        popupButton.addActionListener(buttonListener);
        reinitializeButton = new JButton(TrendsIcons.getIcon("ReinitializeCurveValues", false));
        reinitializeButton.setDisabledIcon(TrendsIcons.getIcon("ReinitializeCurveValues", true));
        reinitializeButton.setToolTipText(reinitializeCurveValuesLabel);
        reinitializeButtonListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (currentConfig != null) {
                    currentConfig.reinitialize();
                }
            }
        };
        reinitializeButton.addActionListener(reinitializeButtonListener);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(curveJLabel);
        add(iconLbl);
        add(curveTextField);
        add(popupButton);
        add(reinitializeButton);
        if (configs.size() > 0) {
            setCurrentConfig(configs.get(0));
        }
    }

    /**
	 * sets the current trends curve configuration
	 * @param config a config object
	 */
    protected void setCurrentConfig(TrendsCurveConfiguration config) {
        curvesPropertiesComponent.changeCurve(config);
        curveTextField.setText(config.getLabel().equals("") ? config.getTagName() : config.getTagName());
        iconLbl.setIcon(createIcon(config.getColor()));
        currentConfig = config;
        reinitializeButton.setEnabled(true);
    }

    /**
	 * creates and returns the icon corresponding to 
	 * a rectangle painted with the given color
	 * @param color a color
	 * @return the icon corresponding to 
	 * a rectangle painted with the given color
	 */
    protected ImageIcon createIcon(Color color) {
        ImageIcon icon = null;
        Dimension size = new Dimension(12, 12);
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size.width, size.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, size.width - 1, size.height - 1);
        g.dispose();
        icon = new ImageIcon(image);
        return icon;
    }

    /**
	 * disposes the panel
	 */
    public void dispose() {
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i].removeActionListener(actionListeners[i]);
        }
        popupButton.removeActionListener(buttonListener);
        reinitializeButton.removeActionListener(reinitializeButtonListener);
        curvesPropertiesComponent = null;
        curveTextField = null;
        iconLbl = null;
        popupMenu = null;
        popupButton = null;
        buttonListener = null;
        menuItems = null;
        actionListeners = null;
        reinitializeButton = null;
        reinitializeButtonListener = null;
        configListeners = null;
        currentConfig = null;
    }
}
