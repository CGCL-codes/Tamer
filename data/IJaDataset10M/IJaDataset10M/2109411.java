package org.swemof.gui.echo.dashboard;

import org.swemof.gui.echo.style.ImageUtils;
import nextapp.echo.app.Column;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.layout.ColumnLayoutData;

/**
 * Welcome pane, gives basic directions.
 */
public class WelcomePane extends ContentPane {

    private static final Insets INSETS = new Insets(75, 20);

    private static final Font TITLE_FONT = new Font(null, Font.BOLD | Font.UNDERLINE, new Extent(18));

    private static final Font LIST_TITLE_FONT = new Font(null, Font.BOLD, new Extent(15));

    public WelcomePane() {
        setInsets(INSETS);
        initComponents();
    }

    private void initComponents() {
        Column col = new Column();
        Label title = new Label("Welcome to SWeMoF!");
        title.setFont(TITLE_FONT);
        col.add(title);
        Label selectionText = new Label("Please make a selection using the buttons on the left.");
        selectionText.setFont(LIST_TITLE_FONT);
        ColumnLayoutData listTitleLayout = new ColumnLayoutData();
        listTitleLayout.setInsets(new Insets(0, 15));
        selectionText.setLayoutData(listTitleLayout);
        col.add(selectionText);
        Label swemol = new Label(ImageUtils.getImage("swemol.png"));
        swemol.setToolTipText("Beware of the SWeMoL...");
        col.add(swemol);
        add(col);
    }
}
