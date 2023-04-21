package objectif.lyon.designer.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.sun.awt.AWTUtilities;
import objectif.lyon.data.Destination;
import objectif.lyon.data.Segment;
import objectif.lyon.data.RouteColor;
import objectif.lyon.designer.gui.component.models.ColorComboBoxModel;
import objectif.lyon.designer.gui.component.models.DestinationComboBoxModel;
import objectif.lyon.designer.gui.component.renderers.ColorListCellRenderer;
import objectif.lyon.designer.gui.component.renderers.DestinationListCellRenderer;
import objectif.lyon.designer.gui.resources.ResourceManager;

public class SegmentPopupWindow extends Window implements WindowFocusListener {

    /**
	 * Num�ro de version pour s�rialisation
	 */
    private static final long serialVersionUID = -2242293860028877194L;

    /**
	 * Segment auquel la popup se rattache
	 */
    private Segment segment = null;

    /**
	 * Fen�tre parente
	 */
    private Plateau plateau = null;

    /**
	 * Police du texte
	 */
    private Font font = null;

    private JPanel contentPanel = null;

    private JPanel routePanel = null;

    private JLabel titleLabel = null;

    private JComboBox<Destination> destinationAComboBox = null;

    private JComboBox<Destination> destinationBComboBox = null;

    private JComboBox<RouteColor> colorComboBox = null;

    /**
	 * Construit la popup permettant de choisir les informations de la route
	 * @throws HeadlessException
	 */
    public SegmentPopupWindow(Plateau plateau, Segment segment) throws HeadlessException {
        super(null);
        InputStream is = ResourceManager.class.getResourceAsStream("calibri.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(11.0f);
        } catch (FontFormatException e) {
            e.printStackTrace();
            font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        } catch (IOException e) {
            e.printStackTrace();
            font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        }
        this.plateau = plateau;
        this.segment = segment;
        this.setSize(new Dimension(200, 100));
        this.setLocation(200, 200);
        this.setAlwaysOnTop(true);
        AWTUtilities.setWindowOpaque(this, false);
        this.add(getContentPanel());
        this.pack();
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createLineBorder(new Color(51, 153, 255)));
            contentPanel.add(getTitleLabel(), BorderLayout.NORTH);
            contentPanel.add(getRoutePanel(), BorderLayout.CENTER);
            contentPanel.setOpaque(false);
        }
        return contentPanel;
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel("ROUTE");
            titleLabel.setBackground(new Color(168, 209, 251));
            titleLabel.setOpaque(true);
            titleLabel.setFont(new Font(font.getFamily(), Font.BOLD, 14));
        }
        return titleLabel;
    }

    private JPanel getRoutePanel() {
        if (routePanel == null) {
            routePanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(1, 2, 1, 2);
            c.fill = GridBagConstraints.HORIZONTAL;
            routePanel.add(createLabel("Destination A"), c);
            c.gridy = 1;
            routePanel.add(createLabel("Destination B"), c);
            c.gridy = 2;
            routePanel.add(createLabel("Couleur"), c);
            c.gridx = 1;
            routePanel.add(getColorComboBox(), c);
            c.gridy = 0;
            routePanel.add(getDestinationAComboBox(), c);
            c.gridy = 1;
            routePanel.add(getDestinationBComboBox(), c);
            updateDestinationModel();
        }
        return routePanel;
    }

    private JComboBox<Destination> getDestinationAComboBox() {
        if (destinationAComboBox == null) {
            destinationAComboBox = new JComboBox<Destination>();
            destinationAComboBox.setRenderer(new DestinationListCellRenderer());
            destinationAComboBox.setFont(font);
            destinationAComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (destinationAComboBox.getSelectedItem() instanceof Destination) {
                        Destination destination = (Destination) destinationAComboBox.getSelectedItem();
                        segment.getRoute().setDestination(0, destination);
                        plateau.repaint();
                    }
                }
            });
        }
        return destinationAComboBox;
    }

    private JComboBox<Destination> getDestinationBComboBox() {
        if (destinationBComboBox == null) {
            destinationBComboBox = new JComboBox<Destination>();
            destinationBComboBox.setRenderer(new DestinationListCellRenderer());
            destinationBComboBox.setFont(font);
            destinationBComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (destinationBComboBox.getSelectedItem() instanceof Destination) {
                        Destination destination = (Destination) destinationBComboBox.getSelectedItem();
                        segment.getRoute().setDestination(1, destination);
                        plateau.repaint();
                    }
                }
            });
        }
        return destinationBComboBox;
    }

    private void updateDestinationModel() {
        List<Destination> destinations = new ArrayList<Destination>();
        for (GraphicalElement e : plateau.getGraphicalElements()) {
            if (e instanceof DestinationObject) {
                destinations.add(((DestinationObject) e).getDestination());
            }
        }
        Collections.sort(destinations);
        getDestinationAComboBox().setModel(new DestinationComboBoxModel(destinations));
        getDestinationBComboBox().setModel(new DestinationComboBoxModel(destinations));
    }

    private JComboBox<RouteColor> getColorComboBox() {
        if (colorComboBox == null) {
            colorComboBox = new JComboBox<RouteColor>(new ColorComboBoxModel());
            colorComboBox.setRenderer(new ColorListCellRenderer());
            colorComboBox.setFont(font);
            colorComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (colorComboBox.getSelectedItem() instanceof RouteColor) {
                        RouteColor segmentColor = (RouteColor) colorComboBox.getSelectedItem();
                        segment.getRoute().setCouleur(segmentColor);
                        plateau.repaint();
                    }
                }
            });
        }
        return colorComboBox;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(false);
        label.setFont(font);
        return label;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        java.awt.Component frame;
        for (frame = ((java.awt.Component) (plateau)); !(frame instanceof java.awt.Frame) && frame != null; frame = ((java.awt.Component) (frame.getParent()))) ;
        if (frame != null) {
            if (visible) {
                ((java.awt.Frame) frame).addWindowFocusListener(this);
            } else {
                ((java.awt.Frame) frame).removeWindowFocusListener(this);
            }
        }
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        super.setVisible(true);
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        super.setVisible(false);
    }
}
