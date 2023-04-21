package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;

/**
 * Propriedades da aplica��o. <BR>
 * Se n�o existir um arquivo de idiomas o <i>default</i> � ingl�s.
 * 
 * @author <a href="http://spycorp.org">SpyCorp</a>
 * @version 1.0
 */
public class SpyProperties {

    public static String AXISY_TITLE = "Total Measurement";

    public static String CHART_FRAME_TITLE = "Chart to day: ";

    public static String DAILY_AXISX_TITLE = "Hour";

    public static String DAILY_OPTION_STRING = "Daily";

    public static String DAILY_TITLE = "Daily";

    public static String DATE_LABEL = "Target Time";

    public static String DAY_ATTRIBUTE_NAME = "dia";

    public static String DIR_LABEL = "User Directory";

    public static String DIR_NAME;

    public static String EMPTY_DIR_ERROR_MESSAGE = "Directory hasn't files!\nSelect a valid directory!";

    public static String EXIT_MESSAGE = "Exit SpyReport?";

    public static String ERROR_MESSAGE = "No data in this period.";

    public static String FILE_CHOOSER_TEXT = "Select the User Directory";

    public static String HOUR_ATTRIBUTE_NAME = "hora";

    public static String KEY_ATTRIBUTE_NAME = "Teclado";

    public static String MAIN_APPLICATION_TITLE = "Spy007 Report";

    public static String MAKE_CHART_BUTTON_TEXT = "Make chart";

    public static String MAKE_REPORT_BUTTON_TEXT = "Make report";

    public static String MINUTE_ATTRIBUTE_NAME = "minute";

    public static String MONTH_ATTRIBUTE_NAME = "mes";

    public static String MONTHLY_AXISX_TITLE = "Day in Month";

    public static String MONTHLY_OPTION_STRING = "Monthly";

    public static String MONTHLY_TITLE = "Monthly";

    public static String MOUSE_ATTRIBUTE_NAME = "Mouse";

    public static String NO_USER_ERROR_MESSAGE = "Directory hasn't files!\nSelect a valid directory!";

    public static String PERIOD_STRING = "Period";

    public static String REPORT_DV1_TITLE = "Key/Min";

    public static String REPORT_DV2_TITLE = "Mouse/Min";

    public static String REPORT_FRAME_TITLE = "Report to day: ";

    public static String REPORT_MINUTE_STRING = "Qt Min";

    public static String REPORT_TIP_TEXT = "N/A = No data in this period";

    public static String SEARCH_BUTTON_TEXT = "Find";

    public static String START_MESSAGE = "Select the user directory";

    public static String USER_ATTIBUTE_NAME = "usuario";

    public static String USER_DIR_NAME;

    public static String USER_NAME;

    public static String USER_LABEL = "User";

    public static String WEEKLY_AXISX_TITLE = "Day in Week";

    public static String WEEKLY_OPTION_STRING = "Weekly";

    public static String WEEKLY_TITLE = "Weekly";

    public static String YEAR_ATTRIBUTE_NAME = "ano";

    public static String YEARLY_AXISX_TITLE = "Month in Year";

    public static String YEARLY_OPTION_STRING = "Yearly";

    public static String YEARLY_TITLE = "Yearly";

    /**
	 * Configura as propriedades de propriedade com os dados do arquivo
	 * fornecido como par�metro.<br>
	 * Se n�o existir o arquivo ou for o arquivo inv�lido, as propriedades
	 * permanecem com o valor <i>default</i>
	 * 
	 * @param propertiesFile
	 *            O arquivo de propriedades
	 */
    public static void loadProperties(File propertiesFile) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propertiesFile));
            AXISY_TITLE = props.getProperty("AXISY_TITLE");
            CHART_FRAME_TITLE = props.getProperty("CHART_FRAME_TITLE");
            DAILY_AXISX_TITLE = props.getProperty("DAILY_AXISX_TITLE");
            DAILY_OPTION_STRING = props.getProperty("DAILY_OPTION_STRING");
            DAILY_TITLE = props.getProperty("DAILY_TITLE");
            DATE_LABEL = props.getProperty("DATE_LABEL");
            DAY_ATTRIBUTE_NAME = props.getProperty("DAY_ATTRIBUTE_NAME");
            DIR_LABEL = props.getProperty("DIR_LABEL");
            DIR_NAME = props.getProperty("DIR_NAME");
            AXISY_TITLE = props.getProperty("AXISY_TITLE");
            EMPTY_DIR_ERROR_MESSAGE = props.getProperty("EMPTY_DIR_ERROR_MESSAGE");
            EXIT_MESSAGE = props.getProperty("EXIT_MESSAGE");
            ERROR_MESSAGE = props.getProperty("ERROR_MESSAGE");
            FILE_CHOOSER_TEXT = props.getProperty("FILE_CHOOSER_TEXT");
            HOUR_ATTRIBUTE_NAME = props.getProperty("HOUR_ATTRIBUTE_NAME");
            KEY_ATTRIBUTE_NAME = props.getProperty("KEY_ATTRIBUTE_NAME");
            MAIN_APPLICATION_TITLE = props.getProperty("MAIN_APPLICATION_TITLE");
            MAKE_CHART_BUTTON_TEXT = props.getProperty("MAKE_CHART_BUTTON_TEXT");
            MAKE_REPORT_BUTTON_TEXT = props.getProperty("MAKE_REPORT_BUTTON_TEXT");
            MINUTE_ATTRIBUTE_NAME = props.getProperty("MINUTE_ATTRIBUTE_NAME");
            MONTH_ATTRIBUTE_NAME = props.getProperty("MONTH_ATTRIBUTE_NAME");
            MONTHLY_AXISX_TITLE = props.getProperty("MONTHLY_AXISX_TITLE");
            MONTHLY_OPTION_STRING = props.getProperty("MONTHLY_OPTION_STRING");
            MONTHLY_TITLE = props.getProperty("MONTHLY_TITLE");
            MOUSE_ATTRIBUTE_NAME = props.getProperty("MOUSE_ATTRIBUTE_NAME");
            NO_USER_ERROR_MESSAGE = props.getProperty("NO_USER_ERROR_MESSAGE");
            PERIOD_STRING = props.getProperty("PERIOD_STRING");
            REPORT_DV1_TITLE = props.getProperty("REPORT_DV1_TITLE");
            REPORT_DV2_TITLE = props.getProperty("REPORT_DV2_TITLE");
            REPORT_FRAME_TITLE = props.getProperty("REPORT_FRAME_TITLE");
            REPORT_MINUTE_STRING = props.getProperty("REPORT_MINUTE_STRING");
            REPORT_TIP_TEXT = props.getProperty("REPORT_TIP_TEXT");
            SEARCH_BUTTON_TEXT = props.getProperty("SEARCH_BUTTON_TEXT");
            START_MESSAGE = props.getProperty("START_MESSAGE");
            USER_ATTIBUTE_NAME = props.getProperty("USER_ATTIBUTE_NAME");
            USER_LABEL = props.getProperty("USER_LABEL");
            WEEKLY_AXISX_TITLE = props.getProperty("WEEKLY_AXISX_TITLE");
            WEEKLY_OPTION_STRING = props.getProperty("WEEKLY_OPTION_STRING");
            WEEKLY_TITLE = props.getProperty("WEEKLY_TITLE");
            YEAR_ATTRIBUTE_NAME = props.getProperty("YEAR_ATTRIBUTE_NAME");
            YEARLY_AXISX_TITLE = props.getProperty("YEARLY_AXISX_TITLE");
            YEARLY_OPTION_STRING = props.getProperty("YEARLY_OPTION_STRING");
            YEARLY_TITLE = props.getProperty("YEARLY_TITLE");
            UIManager.put("FileChooser.lookInLabelMnemonic", props.getProperty("LOOK_IN_LABEL_MNEMONIC"));
            UIManager.put("FileChooser.lookInLabelText", props.getProperty("LOOK_IN_LABEL_TEXT"));
            UIManager.put("FileChooser.saveInLabelText", props.getProperty("SAVE_IN_LABEL_TEXT"));
            UIManager.put("FileChooser.saveButtonText", props.getProperty("SAVE_BUTTON_TEXT"));
            UIManager.put("FileChooser.openButtonText", props.getProperty("OPEN_BUTTON_TEXT"));
            UIManager.put("FileChooser.cancelButtonText", props.getProperty("CANCEL_BUTTON_TEXT"));
            UIManager.put("FileChooser.saveButtonToolTipText", props.getProperty("SAVE_BUTTON_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.openButtonToolTipText", props.getProperty("OPEN_BUTTON_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.cancelButtonToolTipText", props.getProperty("CANCEL_BUTTON_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.fileNameLabelMnemonic", props.getProperty("FILE_NAME_LABEL_MNEMONIC"));
            UIManager.put("FileChooser.fileNameLabelText", props.getProperty("FILE_NAME_LABEL_TEXT"));
            UIManager.put("FileChooser.filesOfTypeLabelMnemonic", props.getProperty("FILES_OF_TYPE_LABEL_MNEMONIC"));
            UIManager.put("FileChooser.filesOfTypeLabelText", props.getProperty("FILES_OF_TYPE_LABEL_TEXT"));
            UIManager.put("FileChooser.upFolderToolTipText", props.getProperty("UP_FOLDER_TOOP_TIP_TEXT"));
            UIManager.put("FileChooser.upFolderAccessibleName", props.getProperty("UP_FOLDER_ACCESSIBLE_NAME"));
            UIManager.put("FileChooser.homeFolderToolTipText", props.getProperty("HOME_FOLDER_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.homeFolderAccessibleName", props.getProperty("HOME_FOLDER_ACCESSIBLE_NAME"));
            UIManager.put("FileChooser.newFolderToolTipText", props.getProperty("NEW_FOLDER_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.newFolderAccessibleName", props.getProperty("NEW_FOLDER_ACCESSIBLE_NAME"));
            UIManager.put("FileChooser.listViewButtonToolTipText", props.getProperty("LIST_VIEW_BUTTON_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.listViewButtonAccessibleName", props.getProperty("LIST_VIEW_BUTTON_ACCESSIBLE_NAME"));
            UIManager.put("FileChooser.detailsViewButtonToolTipText", props.getProperty("DETAILS_VIEW_BUTTON_TOOL_TIP_TEXT"));
            UIManager.put("FileChooser.detailsViewButtonAccessibleName", props.getProperty("DETAILS_VIEW_BUTTON_ACCESSIBLE_NAME"));
            UIManager.put("FileChooser.fileNameHeaderText", props.getProperty("FILE_NAME_HEADER_TEXT"));
            UIManager.put("FileChooser.fileSizeHeaderText", props.getProperty("FILE_SIZE_HEADER_TEXT"));
            UIManager.put("FileChooser.fileTypeHeaderText", props.getProperty("FILE_TYPE_HEADER_TEXT"));
            UIManager.put("FileChooser.fileDateHeaderText", props.getProperty("FILE_DATE_HEADER_TEXT"));
            UIManager.put("FileChooser.fileAttrHeaderText", props.getProperty("FILE_ATTR_HEADER_TEXT"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
