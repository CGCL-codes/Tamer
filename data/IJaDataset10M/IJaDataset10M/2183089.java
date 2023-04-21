package be.vds.jtbdive.client.view.core.divesite;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.actions.OpenGoogleMapAction;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.MapView;
import be.vds.jtbdive.client.view.core.document.DocumentsSlideWalker;
import be.vds.jtbdive.client.view.core.slideshow.SlideShowDialog;
import be.vds.jtbdive.client.view.events.LogBookEventAdapter;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveSiteDetailPanel extends DetailPanel implements Observer {

    private static final long serialVersionUID = 7292990007158689241L;

    protected static final Syslog LOGGER = Syslog.getLogger(DiveSiteDetailPanel.class);

    private JLabel nameLabel;

    private JLabel depthLabel;

    private JLabel depthUnitLabel;

    private CardLayout cardLayout;

    private JLabel altitudeLabel;

    private JLabel altitudeUnitLabel;

    private JLabel coordinatesLabel;

    private DiveSite currentDiveSite;

    private I18nButton showMapButton;

    private I18nButton showGoogleMapButton;

    private OpenGoogleMapAction openGoogleMapAction;

    private DiveSiteManagerFacade diveSiteManagerFacade;

    private JButton documentSlideShowButton;

    public DiveSiteDetailPanel(DiveSiteManagerFacade diveSiteManagerFacade) {
        this.diveSiteManagerFacade = diveSiteManagerFacade;
        initListerners();
        init();
    }

    private void initListerners() {
        LogBookEventAdapter adapter = new LogBookEventAdapter() {

            public void diveSiteSelected(Component source, DiveSite diveSite) {
                new Thread(new DiveSiteRunnable(diveSite)).start();
            }
        };
        LogBookUiEventHandler.getInstance().addEventListener(adapter);
        UnitsAgent.getInstance().addObserver(this);
    }

    private void init() {
        JPanel centralPanel = new JPanel(new GridBagLayout());
        centralPanel.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 3, 3, 3);
        int y = 0;
        GridBagLayoutManager.addComponent(centralPanel, createNameComponent(), gc, 0, y, 1, 1, 1, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(centralPanel, createDepthComponent(), gc, 0, ++y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(centralPanel, createAltitudeComponent(), gc, 0, ++y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gc.insets = new Insets(10, 3, 3, 3);
        GridBagLayoutManager.addComponent(centralPanel, createCoordinatesComponent(), gc, 0, ++y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gc.insets = new Insets(3, 3, 3, 3);
        GridBagLayoutManager.addComponent(centralPanel, createCoordinatesButton(), gc, 0, ++y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(centralPanel, createDocumentComponent(), gc, 0, ++y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(centralPanel, Box.createVerticalGlue(), gc, 0, ++y, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        JPanel defaultPanel = new JPanel();
        defaultPanel.add(new I18nLabel("no.divesite"));
        defaultPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(centralPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(UIAgent.VERTICAL_UNIT_SCROLL);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        this.add(defaultPanel, "default");
        this.add(scroll, "divesite");
    }

    private Component createDocumentComponent() {
        documentSlideShowButton = new JButton(new AbstractAction(null, UIAgent.getInstance().getIcon(UIAgent.ICON_SLIDESHOW)) {

            private static final long serialVersionUID = -3206791766297484824L;

            @Override
            public void actionPerformed(ActionEvent e) {
                SlideShowDialog dlg = new SlideShowDialog(new DocumentsSlideWalker(currentDiveSite.getDocuments(), diveSiteManagerFacade));
                dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
            }
        });
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setOpaque(false);
        p.add(new I18nLabel("documents"));
        p.add(documentSlideShowButton);
        return p;
    }

    private Component createCoordinatesButton() {
        showMapButton = new I18nButton(new AbstractAction() {

            private static final long serialVersionUID = 4898769084447788132L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMapAccessible()) {
                    MapView v = new MapView();
                    Coordinates c = currentDiveSite.getCoordinates();
                    v.setCoordinates(c.getLatitude(), c.getLongitude());
                    v.drawOnDialog(WindowUtils.getTopLevelWindow(DiveSiteDetailPanel.this));
                } else {
                    JOptionPane.showMessageDialog(DiveSiteDetailPanel.this, "Maps are currently not available from internet. Please verify your connection.");
                }
            }

            private boolean isMapAccessible() {
                try {
                    URL url = new URL("http://tile.openstreetmap.org");
                    url.getContent();
                    return true;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
                return false;
            }
        });
        showMapButton.setTooltipTextBundleKey("map.show");
        showMapButton.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_MAP_16));
        openGoogleMapAction = new OpenGoogleMapAction();
        showGoogleMapButton = new I18nButton(openGoogleMapAction);
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setOpaque(false);
        p.add(new I18nLabel("map.show"));
        p.add(showMapButton);
        p.add(showGoogleMapButton);
        return p;
    }

    private Component createCoordinatesComponent() {
        coordinatesLabel = new JLabel();
        coordinatesLabel.setFont(UIAgent.getInstance().getFontNormalBold());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setOpaque(false);
        p.add(new I18nLabel("coordinates"));
        p.add(coordinatesLabel);
        return p;
    }

    private Component createDepthComponent() {
        depthLabel = new JLabel();
        depthLabel.setFont(UIAgent.getInstance().getFontNormalBold());
        depthUnitLabel = new JLabel();
        setDepthUnitText();
        depthUnitLabel.setFont(UIAgent.getInstance().getFontNormalBold());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setOpaque(false);
        p.add(new I18nLabel("depth"));
        p.add(depthLabel);
        p.add(depthUnitLabel);
        return p;
    }

    private Component createAltitudeComponent() {
        altitudeLabel = new JLabel();
        altitudeLabel.setFont(UIAgent.getInstance().getFontNormalBold());
        altitudeUnitLabel = new JLabel();
        setAltitudeUnitText();
        altitudeUnitLabel.setFont(UIAgent.getInstance().getFontNormalBold());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setOpaque(false);
        p.add(new I18nLabel("altitude"));
        p.add(altitudeLabel);
        p.add(altitudeUnitLabel);
        return p;
    }

    private Component createNameComponent() {
        nameLabel = new JLabel();
        nameLabel.setFont(UIAgent.getInstance().getFontTitleDetail());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return nameLabel;
    }

    private void displayDiveSite(DiveSite diveSite) {
        this.currentDiveSite = diveSite;
        if (diveSite == null) {
            clear();
        } else {
            fillData(diveSite);
        }
    }

    private void clear() {
        nameLabel.setText(null);
        depthLabel.setText(null);
        altitudeLabel.setText(null);
        coordinatesLabel.setText(null);
        openGoogleMapAction.setCoordinates(null);
        cardLayout.show(this, "default");
    }

    private void fillData(DiveSite diveSite) {
        nameLabel.setText(diveSite.getName());
        depthLabel.setText(String.valueOf(UnitsAgent.getInstance().convertLengthFromModel(diveSite.getDepth())));
        altitudeLabel.setText(String.valueOf(UnitsAgent.getInstance().convertLengthFromModel(diveSite.getAltitude())));
        coordinatesLabel.setText(diveSite.hasCoordinates() ? diveSite.getCoordinates().toString() : null);
        showMapButton.setEnabled(diveSite.hasCoordinates());
        showGoogleMapButton.setEnabled(diveSite.hasCoordinates());
        if (diveSite.hasCoordinates()) {
            openGoogleMapAction.setCoordinates(diveSite.getCoordinates());
        } else {
            openGoogleMapAction.setCoordinates(null);
        }
        documentSlideShowButton.setEnabled(diveSite.getDocuments() != null && diveSite.getDocuments().size() > 0);
        cardLayout.show(this, "divesite");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(UnitsAgent.getInstance()) && arg.equals(UnitsAgent.UNITS_CHANGED)) {
            updateUnits();
        }
    }

    private void setAltitudeUnitText() {
        altitudeUnitLabel.setText("(" + UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
    }

    private void setDepthUnitText() {
        depthUnitLabel.setText("(" + UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
    }

    public void updateUnits() {
        displayDiveSite(currentDiveSite);
        setAltitudeUnitText();
        setDepthUnitText();
    }

    private class DiveSiteRunnable implements Runnable {

        private DiveSite diveSite;

        public DiveSiteRunnable(DiveSite diveSite) {
            this.diveSite = diveSite;
        }

        @Override
        public void run() {
            DiveSite ds = diveSite;
            if (diveSite != null && diveSite != currentDiveSite && diveSite.getLoadType() < DiveSite.LOAD_MEDIUM) {
                try {
                    ds = diveSiteManagerFacade.findDiveLocationsById(diveSite.getId(), DiveSite.LOAD_FULL);
                } catch (DataStoreException e) {
                    LOGGER.error(e);
                    ds = diveSite;
                }
            }
            displayDiveSite(ds);
        }
    }
}
