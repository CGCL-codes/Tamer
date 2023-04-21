package view.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class UIMenuGrafik extends JMenu {

    JMenuItem it_histogramm, it_wahrscheinlichkeitsnetz, it_laufkarte, it_imrkarte, it_xygrafik, it_xyMatrixgrafik, it_konturliniengrafik, it_dreiecksgrafik, it_wirkungsliniengrafik, it_3dgrafik;

    public UIMenuGrafik(String s) {
        super(s);
        it_histogramm = new JMenuItem("Histogramm");
        it_wahrscheinlichkeitsnetz = new JMenuItem("Wahrscheinlichkeitsnetz");
        it_laufkarte = new JMenuItem("Laufkarte");
        it_imrkarte = new JMenuItem("IMR Karte");
        it_xygrafik = new JMenuItem("X-Y Grafik");
        it_xyMatrixgrafik = new JMenuItem("X-Y Matrixgrafik");
        it_konturliniengrafik = new JMenuItem("Konturliniengrafik");
        it_dreiecksgrafik = new JMenuItem("Dreiecksgrafik");
        it_wirkungsliniengrafik = new JMenuItem("Wirkungsliniengrafik");
        it_3dgrafik = new JMenuItem("3D Grafik");
        add(it_histogramm);
        add(it_wahrscheinlichkeitsnetz);
        add(it_laufkarte);
        add(it_imrkarte);
        add(it_xygrafik);
        add(it_xyMatrixgrafik);
        add(it_konturliniengrafik);
        add(it_dreiecksgrafik);
        add(it_wirkungsliniengrafik);
        add(it_3dgrafik);
    }
}
