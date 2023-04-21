package passreminder.ui;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import passreminder.IconChoiceFilter;
import passreminder.PassReminder;
import passreminder.UIManager;

public class IconsComposite extends Composite {

    private String filename;

    public IconsComposite(Composite parent, final IconSelectable iconSelection, String defaultIcon, Image defaultImage) {
        super(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        setLayout(layout);
        layout.numColumns = 12;
        layout.verticalSpacing = 9;
        File files = new File("icons");
        File[] fileslist = files.listFiles(new IconChoiceFilter());
        Button imageDefaultLabel = new Button(this, SWT.TOGGLE);
        imageDefaultLabel.setImage(defaultImage);
        imageDefaultLabel.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent evt) {
                filename = null;
                iconSelection.iconSelected("");
            }

            public void widgetDefaultSelected(SelectionEvent evt) {
            }
        });
        final Button[] buttonList = new Button[fileslist.length];
        for (int i = 0; i < buttonList.length; i++) {
            buttonList[i] = new Button(this, SWT.TOGGLE);
            buttonList[i].computeSize(20, 20);
            final String _f = fileslist[i].getPath().substring((UIManager.USER_ICON_FOLDER + System.getProperty("file.separator")).length());
            Image img = new Image(PassReminder.getInstance().getShell().getDisplay(), UIManager.USER_ICON_FOLDER + System.getProperty("file.separator") + _f);
            if (defaultIcon != null && defaultIcon.equals(_f)) {
                buttonList[i].setSelection(true);
                filename = _f;
            }
            buttonList[i].setImage(img);
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            buttonList[i].setLayoutData(data);
            buttonList[i].addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent evt) {
                    Button selectedButton = (Button) (evt.getSource());
                    filename = _f;
                    for (int j = 0; j < buttonList.length; j++) {
                        if (buttonList[j] != selectedButton) buttonList[j].setSelection(false);
                    }
                    iconSelection.iconSelected(filename);
                }

                public void widgetDefaultSelected(SelectionEvent evt) {
                }
            });
        }
        if (filename == null) imageDefaultLabel.setSelection(true);
    }
}
