package gov.sns.jclass;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.klg.jclass.chart.beans.SimpleChart;
import com.klg.jclass.chart.EventTrigger;

public class StripChart extends CustomChart {

    public StripChart() {
        super();
        PopupMenuHandler handler = new PopupMenuHandler(this);
        JMenuItem stripDeltaItem = new JMenuItem("Strip Delta");
        stripDeltaItem.addActionListener(handler);
        JMenuItem stripSkipItem = new JMenuItem("Strip Skip");
        stripSkipItem.addActionListener(handler);
        _popupMenu.add(stripDeltaItem);
        _popupMenu.add(stripSkipItem);
    }
}
