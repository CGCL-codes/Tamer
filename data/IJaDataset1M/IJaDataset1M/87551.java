package pierre.textui;

import pierre.model.*;
import pierre.api.*;
import pierre.system.*;
import pierre.reports.*;
import pedro.mda.model.*;
import java.util.Arrays;

public class UseFreeTextMetaQueryValueAction extends NavigationAction {

    public static void main(String[] args) {
    }

    private ScreenControl mainMenuControl;

    private ScrollableScreenArea choiceListArea;

    private TextMetaQueryField textMetaQueryField;

    private BasicMenuDrivenAction parentAction;

    private String[] choices;

    public UseFreeTextMetaQueryValueAction(MenuApp menuApp, BasicMenuDrivenAction parentAction, TextMetaQueryField textMetaQueryField) {
        super(menuApp);
        this.parentAction = parentAction;
        this.textMetaQueryField = textMetaQueryField;
        errorReport = new ErrorReport(DeploymentForm.TEXT_MENU_APPLICATION);
        String mainMenuCode = BrowserServiceResources.getMessage("textui.general.controls.mainMenu.code");
        String mainMenuDisplayName = BrowserServiceResources.getMessage("textui.general.controls.mainMenu.displayName");
        mainMenuControl = new ScreenControl(mainMenuCode, mainMenuDisplayName);
        addControl(mainMenuControl);
        addQuitControl();
        resetScreen();
        addConstantArea(createTitleArea());
        establishPaddingArea();
        addControlArea();
    }

    private ConstantScreenArea createTitleArea() {
        ConstantScreenArea bannerArea = new ConstantScreenArea();
        String titleText = BrowserServiceResources.getMessage("textui.freeTextValue.title", textMetaQueryField.getName());
        bannerArea.addLine(titleText);
        bannerArea.addLine(createDividerLine(titleText.length()));
        String instructionsText = BrowserServiceResources.getMessage("textui.freeTextValue.instructions");
        bannerArea.addLine(instructionsText);
        bannerArea.addLine("");
        return bannerArea;
    }

    public void run() {
        try {
            for (; ; ) {
                showScreen();
                if (errorReport.getNumberOfErrors() > 0) {
                    System.out.println(errorReport.getPage());
                }
                String line = reader.readLine();
                textMetaQueryField.setValue(line);
                menuApp.push(parentAction);
                break;
            }
        } catch (Exception err) {
            err.printStackTrace(System.out);
        }
    }
}
