package org.gvsig.symbology.gui.styling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import org.gvsig.symbology.fmap.styles.SimpleMarkerFillPropertiesStyle;
import org.gvsig.symbology.fmap.symbols.MarkerFillSymbol;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.styles.IMarkerFillPropertiesStyle;
import com.iver.cit.gvsig.fmap.core.symbols.AbstractMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMultiLayerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.styling.AbstractTypeSymbolEditor;
import com.iver.cit.gvsig.gui.styling.EditorTool;
import com.iver.cit.gvsig.gui.styling.SymbolEditor;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;
import com.iver.cit.gvsig.project.documents.view.legend.gui.JSymbolPreviewButton;

/**
 * <b>MarkerFill</b> allows the user to store and modify the properties that fills a
 * polygon with a padding made of markers and an outline<p>
 * <p>
 * This functionality is carried out thanks to two tabs (marker fill and MarkerFillProperties)
 * which are included in the panel to edit the properities of a symbol (SymbolEditor)
 * how is explained in AbstractTypeSymbolEditor.<p>
 * <p>
 * The first tab (marker fill)permits the user to select the marker for the padding and
 * other options such as the color for the fill (<b>btnChooseMarker</b>),to select the
 * ouline (<b>btnOutline</b>)and the distribution (grid or random) of the marker inside
 * the padding (<b>rdGrid,rdRandom</b>).
 * <p>
 * The second tab is implementes as a MarkerFillProperties class and offers the possibilities
 * to change the separtion and the offset.
 *
 *
 *@see MarkerFillProperties
 *@see AbstractTypeSymbolEditor
 *@author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MarkerFill extends AbstractTypeSymbolEditor implements ActionListener, ChangeListener {

    private ArrayList<JPanel> tabs = new ArrayList<JPanel>();

    private ColorChooserPanel markerCC;

    private JButton btnChooseMarker;

    private MarkerFillProperties panelStyle = new MarkerFillProperties();

    private JRadioButton rdGrid;

    private JRadioButton rdRandom;

    private IMarkerSymbol marker = SymbologyFactory.createDefaultMarkerSymbol();

    private JIncrementalNumberField txtOutlineWidth;

    private JSymbolPreviewButton btnOutline;

    private JSlider sldOutlineTransparency;

    private int outlineAlpha = 255;

    private ILineSymbol outline;

    private JCheckBox useBorder;

    /**
	 * constructor method
	 * @param owner
	 */
    public MarkerFill(SymbolEditor owner) {
        super(owner);
        initialize();
    }

    /**
	 * Initializes the parameters that allows the user to fill the padding of
	 * a polygon with a style made of markers.To do it, two tabs are created (marker
	 * fill and MarkerFillProperties)inside the SymbolEditor panel with default values
	 * for the different attributes.
	 */
    private void initialize() {
        JPanel myTab;
        {
            myTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            myTab.setName(PluginServices.getText(this, "marker_fill"));
            GridBagLayoutPanel p = new GridBagLayoutPanel();
            JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
            markerCC = new ColorChooserPanel(true);
            markerCC.setAlpha(255);
            markerCC.addActionListener(this);
            aux.add(markerCC);
            p.addComponent(PluginServices.getText(this, "color") + ":", aux);
            btnChooseMarker = new JButton(PluginServices.getText(this, "choose_marker"));
            btnChooseMarker.addActionListener(this);
            aux = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
            aux.add(btnChooseMarker);
            p.addComponent("", aux);
            ButtonGroup group = new ButtonGroup();
            rdGrid = new JRadioButton(PluginServices.getText(this, "grid"));
            rdGrid.addActionListener(this);
            rdRandom = new JRadioButton(PluginServices.getText(this, "random"));
            rdRandom.addActionListener(this);
            group.add(rdGrid);
            group.add(rdRandom);
            aux = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
            aux.add(rdGrid);
            aux.add(rdRandom);
            rdGrid.setSelected(true);
            p.addComponent("", aux);
            JPanel myTab2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
            GridBagLayoutPanel aux3 = new GridBagLayoutPanel();
            JPanel aux2 = new JPanel();
            btnOutline = new JSymbolPreviewButton(FShape.LINE);
            btnOutline.setPreferredSize(new Dimension(100, 35));
            aux2.add(btnOutline);
            aux3.addComponent(new JBlank(10, 10));
            useBorder = new JCheckBox(PluginServices.getText(this, "use_outline"));
            aux3.addComponent(useBorder, aux2);
            aux3.addComponent(new JBlank(10, 10));
            sldOutlineTransparency = new JSlider();
            sldOutlineTransparency.setValue(100);
            aux3.addComponent(PluginServices.getText(this, "outline") + ":", aux2);
            aux3.addComponent(PluginServices.getText(this, "outline_opacity") + ":", sldOutlineTransparency);
            txtOutlineWidth = new JIncrementalNumberField("", 25, 0, Double.MAX_VALUE, 1);
            aux3.addComponent(PluginServices.getText(this, "outline_width") + ":", txtOutlineWidth);
            myTab2.add(aux3);
            p.addComponent("", myTab2);
            myTab.add(p);
            useBorder.addActionListener(this);
            btnOutline.addActionListener(this);
            txtOutlineWidth.addActionListener(this);
            sldOutlineTransparency.addChangeListener(this);
        }
        tabs.add(myTab);
        tabs.add(panelStyle);
        panelStyle.addActionListener(this);
    }

    public void refreshControls(ISymbol layer) {
        if (layer == null) {
            System.err.println(getClass().getName() + ":: should be unreachable code");
            markerCC.setColor(Color.BLACK);
            rdGrid.setSelected(true);
            rdRandom.setSelected(false);
        } else {
            MarkerFillSymbol mfs = (MarkerFillSymbol) layer;
            int fillStyle = mfs.getMarkerFillProperties().getFillStyle();
            marker = mfs.getMarker();
            rdGrid.setSelected(fillStyle == SimpleMarkerFillPropertiesStyle.GRID_FILL);
            rdRandom.setSelected(fillStyle == SimpleMarkerFillPropertiesStyle.RANDOM_FILL);
            panelStyle.setModel(mfs.getMarkerFillProperties());
            markerCC.setColor(marker.getColor());
            sldOutlineTransparency.removeChangeListener(this);
            outline = mfs.getOutline();
            btnOutline.setSymbol(outline);
            useBorder.setSelected(mfs.hasOutline());
            if (outline != null) {
                outlineAlpha = outline.getAlpha();
                sldOutlineTransparency.setValue((int) ((outlineAlpha / 255D) * 100));
                txtOutlineWidth.setDouble(outline.getLineWidth());
            } else {
                sldOutlineTransparency.setValue(100);
            }
            sldOutlineTransparency.addChangeListener(this);
        }
    }

    public String getName() {
        return PluginServices.getText(this, "marker_fill_symbol");
    }

    public JPanel[] getTabs() {
        return (JPanel[]) tabs.toArray(new JPanel[0]);
    }

    public void actionPerformed(ActionEvent e) {
        JComponent comp = (JComponent) e.getSource();
        if (comp.equals(btnChooseMarker)) {
            ISymbolSelector symSelect = SymbolSelector.createSymbolSelector(marker, FShape.POINT);
            PluginServices.getMDIManager().addWindow(symSelect);
            marker = (AbstractMarkerSymbol) symSelect.getSelectedObject();
            if (marker == null) return;
        }
        if (!(marker instanceof IMultiLayerSymbol)) {
            marker.setColor(markerCC.getColor());
        }
        if (comp.equals(btnOutline)) {
            ISymbol sym = btnOutline.getSymbol();
            if (sym instanceof ILineSymbol) {
                ILineSymbol outline = (ILineSymbol) sym;
                if (outline != null) txtOutlineWidth.setDouble(outline.getLineWidth());
            }
        }
        fireSymbolChangedEvent();
    }

    public Class getSymbolClass() {
        return MarkerFillSymbol.class;
    }

    public ISymbol getLayer() {
        MarkerFillSymbol mfs = new MarkerFillSymbol();
        IMarkerFillPropertiesStyle prop = panelStyle.getMarkerFillProperties();
        prop.setFillStyle(rdGrid.isSelected() ? IMarkerFillPropertiesStyle.GRID_FILL : IMarkerFillPropertiesStyle.RANDOM_FILL);
        IMarkerSymbol myMarker = (IMarkerSymbol) SymbologyFactory.createSymbolFromXML(marker.getXMLEntity(), "theMarker");
        mfs.setMarker(myMarker);
        mfs.setMarkerFillProperties(prop);
        mfs.setHasOutline(useBorder.isSelected());
        outline = (ILineSymbol) btnOutline.getSymbol();
        if (outline != null) {
            outline.setLineWidth(txtOutlineWidth.getDouble());
            outline.setAlpha(outlineAlpha);
        }
        mfs.setOutline(outline);
        return mfs;
    }

    public EditorTool[] getEditorTools() {
        return null;
    }

    public void stateChanged(ChangeEvent e) {
        Object s = e.getSource();
        if (s.equals(sldOutlineTransparency)) {
            outlineAlpha = (int) (255 * (sldOutlineTransparency.getValue() / 100.0));
        }
        outline = (ILineSymbol) btnOutline.getSymbol();
        fireSymbolChangedEvent();
    }
}
