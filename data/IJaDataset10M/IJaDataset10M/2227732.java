package es.ehrflex.client.main;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class EHRflexViewPort extends Viewport {

    private static EHRflexViewPort instance;

    public static EHRflexViewPort getInstance() {
        if (EHRflexViewPort.instance == null) {
            EHRflexViewPort.instance = new EHRflexViewPort();
        }
        return EHRflexViewPort.instance;
    }

    private EHRflexViewPort() {
        this.setLayout(new RowLayout());
        this.add(EHRflexHeader.getInstance(), new RowData(1, EHRflexHeader.HEIGHT));
        this.add(EHRflexPanel.getInstance(), new RowData(1, 1));
    }
}
