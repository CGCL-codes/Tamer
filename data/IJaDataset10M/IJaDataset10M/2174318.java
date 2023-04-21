package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.ClientAnchor;
import org.zkoss.poi.ss.usermodel.Picture;
import org.zkoss.poi.xssf.usermodel.XSSFClientAnchor;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zssex.util.PictureHelper;

/**
 * @author henri
 *
 */
public class MovePictureComposer extends GenericForwardComposer {

    private Spreadsheet myss;

    public void onClick$move(MouseEvent evt) throws Exception {
        Worksheet sheet = myss.getSelectedSheet();
        Range rng = Ranges.range(sheet);
        for (Picture pic : sheet.getPictures()) {
            ClientAnchor anchor = pic.getPreferredSize();
            anchor.setRow1(anchor.getRow1() + 2);
            anchor.setRow2(anchor.getRow2() + 2);
            rng.movePicture(pic, anchor);
        }
    }
}
