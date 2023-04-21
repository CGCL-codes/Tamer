package net.sf.josas.ui;

import static org.fest.swing.data.TableCell.row;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.fest.swing.core.ComponentFinder;
import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import net.sf.josas.model.CountryModel;
import net.sf.josas.model.ZipModel;
import net.sf.josas.om.AbstractDBTestCase;
import net.sf.josas.om.Country;
import net.sf.josas.om.ZipRecord;
import net.sf.josas.ui.common.ButtonNames;
import net.sf.josas.ui.swing.control.CountryController;
import net.sf.josas.ui.swing.control.ZipController;
import net.sf.josas.ui.swing.view.CountryDialog;
import net.sf.josas.ui.swing.view.ZipDialog;

/**
 * Test the use of the zip window.
 *
 * @author frederic
 *
 */
public class TestZipPanel extends AbstractDBTestCase {

    /** Standard code for testing. */
    private static final String TST_CODE = "88888";

    /** Standard code for testing. */
    private static final String TST_CODE2 = "99999";

    /** Standard city for testing. */
    private static final String TST_CITY = "City1";

    /** Standard city for testing. */
    private static final String TST_CITY2 = "City2";

    /** country code for testing. */
    private static final String TST_CTY_CODE = "888";

    /** Country name for testing. */
    private static final String TST_CTY_NAME = "Country";

    /** country code for testing. */
    private static final String TST_CTY_CODE2 = "999";

    /** Country name for testing. */
    private static final String TST_CTY_NAME2 = "CountryTwo";

    /** Expected number of table columns. */
    private static final int NB_COLUMNS = 3;

    /** Expected number of columns for city field. */
    private static final int NB_CITY_COLUMNS = 30;

    /** Frame fixture. */
    private static FrameFixture window;

    /**
    * Setup the database and the frame fixture for testing.
    */
    @BeforeClass
    public static void setupBeforeClass() {
        AbstractDBTestCase.setUpBeforeClass();
        ZipModel model = new ZipModel();
        ZipController controller = new ZipController(model, new CountryModel());
        JFrame frame = new JFrame();
        frame.getContentPane().add(controller.getView());
        window = new FrameFixture(frame);
        window.show();
    }

    /**
    * Clean up.
    */
    @AfterClass
    public static void teardownAfterClass() {
        window.cleanUp();
    }

    /**
    * Data initialization.
    *
    * @return Data set
    * @throws DataSetException
    *             forwards exception raised by dbUnit
    * @throws IOException
    *             forwards exception raised by dbUnit
    */
    @Override
    protected final IDataSet getDataSet() throws DataSetException, IOException {
        return new FlatXmlDataSet(new File(EMPTY_DATASET));
    }

    /**
    * Test default state of components.
    */
    @Test
    public final void testComponents() {
        List<ZipRecord> zrList = ZipRecord.getList();
        Assert.assertNotNull(zrList);
        window.button(ButtonNames.NEW).requireEnabled();
        window.button(ButtonNames.REMOVE).requireDisabled();
        window.button(ButtonNames.EDIT).requireDisabled();
        window.table().requireVisible();
        JTable table = window.table().target;
        Assert.assertEquals(NB_COLUMNS, table.getColumnCount());
        Assert.assertEquals(zrList.size(), table.getRowCount());
    }

    /**
    * Test adding a new zip code, editing it and deleting it.
    */
    @Test
    public final void testAddNewZipCode() {
        CountryModel ctyModel = new CountryModel();
        Country country = new Country(TST_CTY_CODE, TST_CTY_NAME);
        ctyModel.save(country, CountryModel.CREATION_MODE);
        country = new Country(TST_CTY_CODE2, TST_CTY_NAME2);
        ctyModel.save(country, CountryModel.CREATION_MODE);
        window.button(ButtonNames.NEW).click();
        ComponentFinder finder = window.robot.finder();
        JDialog addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        Assert.assertNotNull(addDlg);
        Assert.assertEquals(Messages.getString(ZipDialog.DIALOG_CREATION_TITLE), addDlg.getTitle());
        DialogFixture dialog = new DialogFixture(window.robot, addDlg);
        dialog.requireModal();
        dialog.requireVisible();
        Assert.assertNotNull(dialog.button(ButtonNames.OK));
        dialog.button(ButtonNames.OK).requireText(ButtonNames.OK);
        dialog.button(ButtonNames.OK).requireDisabled();
        Assert.assertNotNull(dialog.button(ButtonNames.CANCEL));
        JTextField textField = dialog.textBox(ZipDialog.CITY_TEXT).targetCastedTo(JTextField.class);
        Assert.assertEquals(NB_CITY_COLUMNS, textField.getColumns());
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY);
        dialog.comboBox(ZipDialog.COUNTRY_TEXT).selectItem(0);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        ZipRecord zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE, zip.getZipCode());
        Assert.assertEquals(TST_CITY, zip.getCity());
        Assert.assertEquals(TST_CTY_CODE, zip.getCountry().getCode());
        Assert.assertEquals(TST_CTY_NAME, zip.getCountry().getName());
        dialog.requireNotVisible();
        JTable table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.button(ButtonNames.REMOVE).requireDisabled();
        window.button(ButtonNames.EDIT).requireDisabled();
        window.table().click(row(0).column(0), MouseButton.LEFT_BUTTON);
        Assert.assertEquals(0, table.getSelectionModel().getLeadSelectionIndex());
        window.button(ButtonNames.REMOVE).requireEnabled();
        window.button(ButtonNames.EDIT).requireEnabled();
        window.button(ButtonNames.EDIT).click();
        JDialog editDlg = (JDialog) finder.findByName(ZipController.DIALOG_EDIT_NAME, true);
        Assert.assertEquals(Messages.getString(ZipDialog.DIALOG_EDITION_TITLE), editDlg.getTitle());
        dialog = new DialogFixture(window.robot, editDlg);
        dialog.button(ButtonNames.OK).requireEnabled();
        dialog.textBox(ZipDialog.CODE_TEXT).requireText(TST_CODE);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE2);
        dialog.textBox(ZipDialog.CITY_TEXT).requireText(TST_CITY);
        dialog.textBox(ZipDialog.CITY_TEXT).requireEnabled();
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY2);
        dialog.comboBox(ZipDialog.COUNTRY_TEXT).requireSelection(TST_CTY_NAME);
        dialog.button(ButtonNames.OK).click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE2, zip.getZipCode());
        Assert.assertEquals(TST_CITY2, zip.getCity());
        dialog.requireNotVisible();
        table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().cell(row(0).column(0)).requireValue(TST_CODE2);
        window.table().cell(row(0).column(1)).requireValue(TST_CITY2);
        window.table().click(row(0).column(0), MouseButton.LEFT_BUTTON);
        window.button(ButtonNames.REMOVE).requireEnabled();
        window.button(ButtonNames.REMOVE).click();
        window.optionPane().requireQuestionMessage();
        window.optionPane().noButton().click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE2, zip.getZipCode());
        Assert.assertEquals(TST_CITY2, zip.getCity());
        table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().cell(row(0).column(0)).requireValue(TST_CODE2);
        window.table().cell(row(0).column(1)).requireValue(TST_CITY2);
        window.table().click(row(0).column(0), MouseButton.LEFT_BUTTON);
        window.button(ButtonNames.REMOVE).click();
        window.optionPane().yesButton().click();
        Assert.assertEquals(0, ZipRecord.getList().size());
        table = window.table().target;
        Assert.assertEquals(0, table.getRowCount());
        window.button(ButtonNames.REMOVE).requireDisabled();
        window.button(ButtonNames.EDIT).requireDisabled();
    }

    /**
    * Testing of zip creation/edition with no defined country.
    */
    @Test
    public final void testNoCountry() {
        window.button(ButtonNames.NEW).click();
        ComponentFinder finder = window.robot.finder();
        JDialog addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        DialogFixture dialog = new DialogFixture(window.robot, addDlg);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        ZipRecord zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE, zip.getZipCode());
        Assert.assertEquals(TST_CITY, zip.getCity());
        Assert.assertNull(zip.getCountry());
        JTable table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().cell(row(0).column(0)).requireValue(TST_CODE);
        window.table().cell(row(0).column(1)).requireValue(TST_CITY);
        window.table().cell(row(0).column(2)).requireValue("");
        window.table().click(row(0).column(0), MouseButton.LEFT_BUTTON);
        window.button(ButtonNames.EDIT).click();
        JDialog editDlg = (JDialog) finder.findByName(ZipController.DIALOG_EDIT_NAME, true);
        dialog = new DialogFixture(window.robot, editDlg);
        dialog.button(ButtonNames.OK).requireEnabled();
        dialog.textBox(ZipDialog.CODE_TEXT).requireText(TST_CODE);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE2);
        dialog.textBox(ZipDialog.CITY_TEXT).requireText(TST_CITY);
        dialog.textBox(ZipDialog.CITY_TEXT).requireEnabled();
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY2);
        dialog.button(ButtonNames.OK).click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE2, zip.getZipCode());
        Assert.assertEquals(TST_CITY2, zip.getCity());
        Assert.assertNull(zip.getCountry());
        dialog.requireNotVisible();
        table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().cell(row(0).column(0)).requireValue(TST_CODE2);
        window.table().cell(row(0).column(1)).requireValue(TST_CITY2);
        window.table().cell(row(0).column(2)).requireValue("");
    }

    /**
    * Testing giving a not yet existing country.
    */
    @Test
    public final void testNotExistingCountry() {
        CountryModel ctyModel = new CountryModel();
        window.button(ButtonNames.NEW).click();
        ComponentFinder finder = window.robot.finder();
        JDialog addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        DialogFixture dialog = new DialogFixture(window.robot, addDlg);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY);
        dialog.comboBox(ZipDialog.COUNTRY_TEXT).enterText(TST_CTY_NAME);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        JDialog ctyDlg = (JDialog) finder.findByName(CountryController.DIALOG_ADD_NAME, true);
        DialogFixture ctyDialog = new DialogFixture(window.robot, ctyDlg);
        ctyDialog.textBox(CountryDialog.CODE_TEXT).requireText("   ");
        ctyDialog.textBox(CountryDialog.CODE_TEXT).setText(TST_CTY_CODE);
        ctyDialog.textBox(CountryDialog.NAME_TEXT).requireText(TST_CTY_NAME);
        ctyDialog.button(ButtonNames.OK).requireEnabled().click();
        Assert.assertEquals(1, ctyModel.getList().size());
        Country country = ctyModel.getList().get(0);
        Assert.assertEquals(TST_CTY_CODE, country.getCode());
        Assert.assertEquals(TST_CTY_NAME, country.getName());
        Assert.assertEquals(1, ZipRecord.getList().size());
        ZipRecord zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE, zip.getZipCode());
        Assert.assertEquals(TST_CITY, zip.getCity());
        Assert.assertEquals(TST_CTY_CODE, zip.getCountry().getCode());
        Assert.assertEquals(TST_CTY_NAME, zip.getCountry().getName());
        JTable table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().click(row(0).column(0), MouseButton.LEFT_BUTTON);
        window.table().doubleClick();
        JDialog editDlg = (JDialog) finder.findByName(ZipController.DIALOG_EDIT_NAME, true);
        dialog = new DialogFixture(window.robot, editDlg);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE2);
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY2);
        dialog.comboBox(ZipDialog.COUNTRY_TEXT).selectAllText().enterText(TST_CTY_NAME2);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        ctyDlg = (JDialog) finder.findByName(CountryController.DIALOG_ADD_NAME, true);
        ctyDialog = new DialogFixture(window.robot, ctyDlg);
        ctyDialog.textBox(CountryDialog.CODE_TEXT).requireText("   ");
        ctyDialog.textBox(CountryDialog.CODE_TEXT).setText(TST_CTY_CODE2);
        ctyDialog.textBox(CountryDialog.NAME_TEXT).requireText(TST_CTY_NAME2);
        ctyDialog.pressAndReleaseKeys(KeyEvent.VK_ESCAPE);
        Assert.assertEquals(1, ctyModel.getList().size());
        country = ctyModel.getList().get(0);
        Assert.assertEquals(TST_CTY_CODE, country.getCode());
        Assert.assertEquals(TST_CTY_NAME, country.getName());
        Assert.assertEquals(1, ZipRecord.getList().size());
        zip = ZipRecord.getList().get(0);
        Assert.assertEquals(TST_CODE2, zip.getZipCode());
        Assert.assertEquals(TST_CITY2, zip.getCity());
        Assert.assertNull(zip.getCountry());
        table = window.table().target;
        Assert.assertEquals(1, table.getRowCount());
        window.table().cell(row(0).column(0)).requireValue(TST_CODE2);
        window.table().cell(row(0).column(1)).requireValue(TST_CITY2);
        window.table().cell(row(0).column(2)).requireValue("");
    }

    /**
    * Test controls on field entries.
    */
    @Test
    public final void testControls() {
        window.button(ButtonNames.NEW).click();
        ComponentFinder finder = window.robot.finder();
        JDialog addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        DialogFixture dialog = new DialogFixture(window.robot, addDlg);
        dialog.button(ButtonNames.OK).requireDisabled();
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.button(ButtonNames.OK).requireDisabled();
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY);
        dialog.textBox(ZipDialog.CODE_TEXT).deleteText();
        dialog.button(ButtonNames.OK).requireDisabled();
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        window.button(ButtonNames.NEW).click();
        addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        dialog = new DialogFixture(window.robot, addDlg);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE);
        dialog.textBox(ZipDialog.CITY_TEXT).setText(TST_CITY);
        dialog.button(ButtonNames.OK).requireEnabled().click();
        dialog.optionPane().requireErrorMessage();
        dialog.requireVisible();
        dialog.optionPane().button().click();
        Assert.assertEquals(1, ZipRecord.getList().size());
        dialog.requireVisible();
        dialog.button(ButtonNames.CANCEL).click();
        window.button(ButtonNames.NEW).click();
        addDlg = (JDialog) finder.findByName(ZipController.DIALOG_ADD_NAME, true);
        dialog = new DialogFixture(window.robot, addDlg);
        dialog.textBox(ZipDialog.CODE_TEXT).setText(TST_CODE2).enterText("TEST");
        dialog.textBox(ZipDialog.CODE_TEXT).requireText(TST_CODE2);
        dialog.button(ButtonNames.CANCEL).click();
    }
}
