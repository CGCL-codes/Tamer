package jcomplexity.gui;

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QTableView;

public class Utils {

    public static void setupTable(QTableView table) {
        table.setContentsMargins(10, 10, 10, 10);
        QHeaderView header = table.horizontalHeader();
        header.setHighlightSections(false);
        header.setDefaultSectionSize(150);
        header.setDefaultAlignment(AlignmentFlag.AlignLeft);
        header.setResizeMode(header.count() - 1, ResizeMode.Stretch);
    }
}
