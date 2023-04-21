package net.sf.freesimrc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import net.sf.freesimrc.apps.asrcclone.dc.DisplayConfiguration;

public class ScopeBar extends JPanel {

    public static final String FSTRIP = "FSTRIP";

    public static final String METAR = "METAR";

    public static final String COMM = "COMM";

    public static final String ATC = "ATC";

    public static final String GEO = "GEO";

    public static final String RANGE_RINGS = "RR";

    public static final String RUNWAY = "RWY";

    public static final String RWY_CTRLIN = "RCL";

    public static final String VECTOR_T = "T";

    public static final String AIRPORT = "AP";

    public static final String VOR = "VOR";

    public static final String NDB = "NDB";

    public static final String FIX = "FIX";

    public static final String LOW = "L";

    public static final String HIGH = "H";

    public static final String ARTCC = "CC";

    public static final String HSECTOR = "HS";

    public static final String LSECTOR = "LS";

    public static final String ZOOM1 = "1";

    public static final String ZOOM2 = "2";

    public static final String ZOOM3 = "3";

    public static final String ZOOM4 = "4";

    public static final String ZOOM5 = "5";

    private JButton fstripButton;

    private JToolBar bar;

    private static final long serialVersionUID = 1L;

    private DisplayConfiguration dc;

    private JSlider jSlider_range = null;

    public ScopeBar(DisplayConfiguration dconf) {
        super();
        this.dc = dconf;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(736, 35));
        this.add(getBar(), BorderLayout.NORTH);
    }

    public JToolBar getBar() {
        if (bar == null) {
            bar = new JToolBar();
            bar.setMargin(new Insets(2, 2, 2, 2));
            final JButton metarButton = new JButton();
            metarButton.setBorder(new BevelBorder(BevelBorder.RAISED));
            metarButton.setText(ScopeBar.METAR);
            bar.add(metarButton);
            final JButton commButton = new JButton();
            commButton.setBorder(new BevelBorder(BevelBorder.RAISED));
            commButton.setText(ScopeBar.COMM);
            bar.add(commButton);
            final JButton atcButton = new JButton();
            atcButton.setBorder(new BevelBorder(BevelBorder.RAISED));
            atcButton.setText(ScopeBar.ATC);
            bar.add(atcButton);
            bar.addSeparator();
            final JButton button = new JButton();
            button.setBorder(new BevelBorder(BevelBorder.RAISED));
            button.setText(ScopeBar.ZOOM1);
            button.setToolTipText("Press to zoom, Ctrl/press to save");
            bar.add(button);
            final JButton button_1 = new JButton();
            button_1.setBorder(new BevelBorder(BevelBorder.RAISED));
            button_1.setText(ScopeBar.ZOOM2);
            button_1.setToolTipText("Press to zoom, Ctrl/press to save");
            bar.add(button_1);
            final JButton button_2 = new JButton();
            button_2.setBorder(new BevelBorder(BevelBorder.RAISED));
            button_2.setText(ScopeBar.ZOOM3);
            button_2.setToolTipText("Press to zoom, Ctrl/press to save");
            bar.add(button_2);
            final JButton button_3 = new JButton();
            button_3.setBorder(new BevelBorder(BevelBorder.RAISED));
            button_3.setText(ScopeBar.ZOOM4);
            button_3.setToolTipText("Press to zoom, Ctrl/press to save");
            bar.add(button_3);
            final JButton button_4 = new JButton();
            button_4.setBorder(new BevelBorder(BevelBorder.RAISED));
            button_4.setText(ScopeBar.ZOOM5);
            button_4.setToolTipText("Press to zoom, Ctrl/press to save");
            bar.add(button_4);
            bar.addSeparator();
            final JToggleButton showGeoButton = new JToggleButton();
            showGeoButton.setText(ScopeBar.GEO);
            showGeoButton.setToolTipText("Show/Hide geography");
            showGeoButton.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_GEO));
            bar.add(showGeoButton);
            final JToggleButton showRRButton = new JToggleButton();
            showRRButton.setText(ScopeBar.RANGE_RINGS);
            showRRButton.setToolTipText("Show/hide range rings");
            showRRButton.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_RANGE_RINGS));
            bar.add(showRRButton);
            bar.addSeparator();
            final JToggleButton showAP = new JToggleButton();
            showAP.setText(ScopeBar.AIRPORT);
            showAP.setToolTipText("Show/hide airports. Right click for airport frequency");
            showAP.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_APT));
            bar.add(showAP);
            final JToggleButton showRwy = new JToggleButton();
            showRwy.setText(ScopeBar.RUNWAY);
            showRwy.setToolTipText("Show/hide runways");
            showRwy.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_RWY));
            bar.add(showRwy);
            final JToggleButton showRCL = new JToggleButton();
            showRCL.setText(ScopeBar.RWY_CTRLIN);
            showRCL.setToolTipText("Show/hide runway centerline");
            showRCL.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_RWY_CTRLIN));
            bar.add(showRCL);
            final JToggleButton showVT = new JToggleButton();
            showVT.setText(ScopeBar.VECTOR_T);
            showVT.setToolTipText("Show/hide vectoring T");
            showVT.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_VECTOR_T));
            bar.add(showVT);
            bar.addSeparator();
            final JToggleButton showVOR = new JToggleButton();
            showVOR.setText(ScopeBar.VOR);
            showVOR.setToolTipText("Show/hide VORs. Right click for VOR frequencies");
            showVOR.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_VOR));
            bar.add(showVOR);
            final JToggleButton showNDB = new JToggleButton();
            showNDB.setText(ScopeBar.NDB);
            showNDB.setToolTipText("Show/hide NDBs. Right click for NDB frequencies");
            showNDB.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_NDB));
            bar.add(showNDB);
            final JToggleButton showFIX = new JToggleButton();
            showFIX.setText(ScopeBar.FIX);
            showFIX.setToolTipText("Show/hide Fixes");
            showFIX.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_FIX));
            bar.add(showFIX);
            bar.addSeparator();
            final JToggleButton showLow = new JToggleButton();
            showLow.setText(ScopeBar.LOW);
            showLow.setToolTipText("Show/hide Low airways. Right click for airway labels");
            showLow.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_L_AWY));
            bar.add(showLow);
            final JToggleButton showHigh = new JToggleButton();
            showHigh.setText(ScopeBar.HIGH);
            showHigh.setToolTipText("Show/hide High airways. Right click for airway labels");
            showHigh.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_H_AWY));
            bar.add(showHigh);
            bar.addSeparator();
            final JToggleButton showARTCC = new JToggleButton();
            showARTCC.setText(ScopeBar.ARTCC);
            showARTCC.setToolTipText("Show/hide ARTCC");
            showARTCC.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_ARTCC));
            bar.add(showARTCC);
            final JToggleButton showHS = new JToggleButton();
            showHS.setText(ScopeBar.HSECTOR);
            showHS.setToolTipText("Show/hide high sectors");
            showHS.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_ARTCC_HGHBND));
            bar.add(showHS);
            final JToggleButton showLS = new JToggleButton();
            showLS.setText(ScopeBar.LSECTOR);
            showLS.setToolTipText("Show/hide low sectors");
            showLS.setSelected(dc.getBooleanSetting(DisplayConfiguration.MAP_ARTCC_LOWBND));
            bar.add(showLS);
            bar.add(getJSlider_range());
        }
        return bar;
    }

    public Component[] getButtons() {
        return bar.getComponents();
    }

    /**
	 * This method initializes jSlider_range	
	 * 	
	 * @return javax.swing.JSlider	
	 */
    private JSlider getJSlider_range() {
        if (jSlider_range == null) {
            jSlider_range = new JSlider();
            jSlider_range.setMaximum(1500);
            jSlider_range.setMajorTickSpacing(100);
            jSlider_range.setMinorTickSpacing(10);
            jSlider_range.setName("Range");
            jSlider_range.setPaintTicks(true);
            jSlider_range.setToolTipText("Scope Range");
            jSlider_range.setMinimum(0);
        }
        return jSlider_range;
    }

    public void SetCurrentRange(int range) {
        getJSlider_range().setValue(range);
    }
}
