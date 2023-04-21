package net.f.ui;

import net.f.app.AppController;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextField;
import net.f.Content;
import net.f.Global;
import net.f.Item;
import net.f.ItemType;
import net.f.app.AppContext;
import net.f.query.Order;
import net.f.query.Query;
import net.f.query.TextDisplay;
import net.f.util.StringUtils;

/**
 *
 * @author dahgdevash@gmail.com
 */
public class SearchForm extends Form implements CommandListener, CommandType {

    private CommandListener prev;

    private int cmdType;

    private Command resetCommand;

    private Command searchCommand;

    private Command gpsCommand;

    private Command dataCommand;

    private Command cancelCommand;

    public SearchForm(CommandListener listener) {
        super("Search Form");
        this.prev = listener;
        append(new MapItem((ItemCommandListener) listener));
        append(new TextField("Search", "", 500, TextField.ANY));
        append(new ItemTypeGroup());
        append(new GlobalGroup());
        append(new PrivacyGroup());
        append(new BooleanGroup("Sms Enabled", new String[] { "Any", "Yes" }, 1));
        append(new SortGroup());
        append(new BooleanGroup("Dir", new String[] { "Ascending", "Descending" }, 0));
        append(new LocaleGroup());
        addCommand(getSearchCommand());
        addCommand(getGPSCommand());
        addCommand(getDataCommand());
        addCommand(getResetCommand());
        addCommand(getCancelCommand());
        setCommandListener(this);
    }

    public int commandType() {
        return cmdType;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == searchCommand) {
            this.search();
        } else if (c == dataCommand) {
            this.data();
        } else if (c == resetCommand) {
            this.reset();
        } else if (c == gpsCommand) {
            this.gps();
        } else if (c == cancelCommand) {
            this.cancel();
        }
        if (prev != null) {
            prev.commandAction(null, this);
        }
    }

    private void search() {
        this.cmdType = Flow.SEARCH_OK;
        updateContext();
        AppController controller = AppController.instance();
        controller.search();
    }

    private void data() {
        this.cmdType = Flow.SEARCH_DATA;
    }

    public void gps() {
        this.cmdType = Flow.UNDEF;
        MapItem mapItem = (MapItem) get(0);
        AppController.instance().useGPS(true);
        mapItem.update();
    }

    private void cancel() {
        this.cmdType = Flow.SEARCH_CANCEL;
    }

    public Command getSearchCommand() {
        if (searchCommand == null) {
            searchCommand = new Command("Search", Command.OK, 0);
        }
        return searchCommand;
    }

    public Command getGPSCommand() {
        if (gpsCommand == null) {
            gpsCommand = new Command("GPS Location", Command.OK, 0);
        }
        return gpsCommand;
    }

    public Command getResetCommand() {
        if (resetCommand == null) {
            resetCommand = new Command("Reset", Command.OK, 0);
        }
        return resetCommand;
    }

    public Command getCancelCommand() {
        if (cancelCommand == null) {
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);
        }
        return cancelCommand;
    }

    public Command getDataCommand() {
        if (dataCommand == null) {
            dataCommand = new Command("Advanced Query", Command.ITEM, 0);
        }
        return dataCommand;
    }

    private void updateContext() {
        AppContext ctx = AppContext.instance();
        Query query = ctx.getQuery();
        TextDisplay text = query.getText(true);
        Content content = text.getLabel(true);
        Item item = query.getItem(true);
        int ix = 1;
        TextField searchField = (TextField) get(ix++);
        ItemTypeGroup itemTypeGroup = (ItemTypeGroup) get(ix++);
        GlobalGroup globalGroup = (GlobalGroup) get(ix++);
        PrivacyGroup privacyGroup = (PrivacyGroup) get(ix++);
        BooleanGroup smsGroup = (BooleanGroup) get(ix++);
        SortGroup sortGroup = (SortGroup) get(ix++);
        BooleanGroup dirGroup = (BooleanGroup) get(ix++);
        LocaleGroup localeGroup = (LocaleGroup) get(ix++);
        String locale = localeGroup.getValue();
        ItemType itemType = itemTypeGroup.getValue();
        Global global = globalGroup.getValue();
        Integer itemTypeId = (itemType != null) ? itemType.getId() : null;
        Integer globalId = (global != null) ? global.getId() : null;
        content.setLocale(locale);
        content.setOutline(StringUtils.toNull(searchField.getString()));
        item.setType(itemTypeId);
        if (!ctx.advSearch()) {
            item.setGlobalId(globalId);
        }
        privacyGroup.updateContext();
        Boolean smsEnabled = smsGroup.getValue();
        if (smsEnabled != null && smsEnabled.booleanValue()) {
            query.getAdquery(true).getSms(true);
        }
        query.setOrder(new Order(sortGroup.getSortValue(), dirGroup.getValue()));
    }

    public void init() {
        AppContext ctx = AppContext.instance();
        ctx.setQueryType(AppContext.TYPE_QUERY);
    }

    private void reset() {
        AppContext ctx = AppContext.instance();
        ctx.resetQuery();
    }
}
