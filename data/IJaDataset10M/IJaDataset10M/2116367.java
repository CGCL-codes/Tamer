package com.tomczarniecki.s3;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.tomczarniecki.s3.gui.Constants;
import javax.swing.table.JTableHeader;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

public class S3DropBoxDriver extends JFrameDriver {

    public S3DropBoxDriver(int timeout) {
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(Constants.MAIN_WINDOW_NAME), showingOnScreen()), new AWTEventQueueProber(timeout, 100));
    }

    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(withLabelText(""), withLabelText("File Name"), withLabelText("Size"), withLabelText("Last Modified")));
    }

    public void showsRowWith(String fileName, String fileSize, String lastModified) {
        JTableDriver table = new JTableDriver(this, named(Constants.MAIN_TABLE_NAME));
        table.hasRow(matching(withLabelText(""), withLabelText(fileName), withLabelText(fileSize), withLabelText(lastModified)));
    }
}
