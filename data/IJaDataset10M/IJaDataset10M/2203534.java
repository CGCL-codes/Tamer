package redora.client.constants;

import com.google.gwt.core.client.GWT;

/**
 * Redora uses (GWT's) <a href="http://code.google.com/webtoolkit/doc/latest/DevGuideI18n.html">static string i18n</a>.
 *
 * @author Nanjing RedOrange (www.red-orange.cn)
 */
public interface RedoraConstants extends com.google.gwt.i18n.client.Constants {

    public static final RedoraConstants INSTANCE = GWT.create(RedoraConstants.class);

    @DefaultStringValue("Login")
    @Key("button.login")
    String login();

    @DefaultStringValue("Logout")
    @Key("button.logout")
    String logout();

    @DefaultStringValue("OK")
    @Key("button.OK")
    String ok();

    @DefaultStringValue("Cancel")
    @Key("button.Cancel")
    String cancel();

    @DefaultStringValue("Close")
    @Key("button.Close")
    String close();

    @DefaultStringValue("Save")
    @Key("button.Save")
    String save();

    @DefaultStringValue("Add")
    @Key("button.Add")
    String add();

    @DefaultStringValue("Delete")
    @Key("button.Delete")
    String delete();

    @DefaultStringValue("Trash")
    @Key("button.Trash")
    String trash();

    @DefaultStringValue("Undo")
    @Key("button.Undo")
    String undo();

    @DefaultStringValue("Refresh")
    @Key("button.Refresh")
    String refresh();

    @DefaultStringValue("Id")
    @Key("pojo.id.table")
    String idTable();

    @DefaultStringValue("Id")
    @Key("pojo.id.form")
    String idForm();

    @DefaultStringValue("Created")
    @Key("pojo.creationDate.table")
    String creationDateTable();

    @DefaultStringValue("Creation Date")
    @Key("pojo.creationDate.form")
    String creationDateForm();

    @DefaultStringValue("Updated")
    @Key("pojo.updateDate.table")
    String updateDateTable();

    @DefaultStringValue("Update Date")
    @Key("pojo.updateDate.form")
    String updateDateForm();

    @DefaultStringValue("Redora Manager")
    @Key("showcase.name")
    String name();

    @DefaultStringValue("Data")
    @Key("showcase.Data")
    String data();

    @DefaultStringValue("Upgrade")
    @Key("showcase.Upgrade")
    String upgrade();

    @DefaultStringValue("Check")
    @Key("showcase.Check")
    String check();

    @DefaultStringValue("Upgrade scripts")
    @Key("showcase.UpgradeScripts")
    String upgradeScripts();

    @DefaultStringValue("&copy; 2012 Redora (www.redora.net)")
    @Key("showcase.Disclaimer")
    String disclaimer();

    @DefaultStringValue("Drop")
    @Key("showcase.Drop")
    String drop();

    @DefaultStringValue("Drop all tables")
    @Key("showcase.DropAllTables")
    String dropAllTables();

    @DefaultStringValue("Model")
    @Key("showcase.Model")
    String model();

    @DefaultStringValue("About")
    @Key("showcase.About")
    String about();

    @DefaultStringValue("First")
    @Key("page.First")
    String firstPage();

    @DefaultStringValue("Last")
    @Key("page.Last")
    String lastPage();

    @DefaultStringValue("Next")
    @Key("page.Next")
    String nextPage();

    @DefaultStringValue("Previous")
    @Key("page.Previous")
    String previousPage();

    @DefaultStringValue("Goto")
    @Key("page.Goto")
    String gotoPage();

    @DefaultStringValue("details...")
    @Key("lazyCell.default")
    String lazyCellDefault();

    @DefaultStringValue("---select---")
    @Key("listbox.selectEmpty")
    String listboxSelectEmpty();
}
