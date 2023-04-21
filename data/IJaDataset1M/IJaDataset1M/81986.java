package fildiv.jremcntl.client.ui;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import fildiv.jremcntl.client.core.AppConstants;
import fildiv.jremcntl.client.core.AppContext;
import fildiv.jremcntl.client.core.ClientConnection;
import fildiv.jremcntl.client.core.DebugAppContext;
import fildiv.jremcntl.client.core.ServerEventListener;
import fildiv.jremcntl.client.core.ServerMessageListener;
import fildiv.jremcntl.client.util.UIUtils;
import fildiv.jremcntl.common.core.MobileKeys;
import fildiv.jremcntl.common.core.cn.ClientConnector;
import fildiv.jremcntl.common.core.config.CommandData;
import fildiv.jremcntl.common.core.config.Config;
import fildiv.jremcntl.common.core.config.ConfigData;
import fildiv.jremcntl.common.core.config.Context;
import fildiv.jremcntl.common.core.config.ContextData;
import fildiv.jremcntl.common.core.msg.CommandMessage;
import fildiv.jremcntl.common.util.Utils;
import fildiv.jremcntl.common.util.log.Logger;

public class ControlPanelScreen extends AbstractScreen implements CommandListener {

    private static final Command CMD_BACK = new Command("Back", Command.BACK, 1);

    private static final Command CMD_CLOSE = new Command("Close", Command.BACK, 3);

    private static final Command CMD_LOGS = new Command("Logs", Command.SCREEN, 4);

    private static final Command CMD_DEBUG = new Command("Temp", Command.SCREEN, 4);

    private ClientConnector connector;

    private ServerMessageListener sml;

    private Config config;

    private Context currCtx = null;

    private Form cpForm;

    private Form cnForm;

    private CommandPanelScreen cmdScreen;

    private ServerEventListener sel = new ServerEventListener() {

        public void connectionClosed() {
            onConnectionClosed();
        }

        public void connectionLost() {
            onConnectionLost();
        }

        public void commandMessageReceived(CommandMessage msg) {
            onCommandResponse(msg);
        }
    };

    public ControlPanelScreen(AppContext appCtx, Screen backScreen) {
        super(appCtx, backScreen);
        config = null;
        cnForm = new Form(AppConstants.CONTACTING_SERVER_CAPTION);
        cmdScreen = new CommandPanelScreen(appCtx);
        cmdScreen.setBackScreen(this);
    }

    public void setLogger(Logger logger) {
        super.setLogger(logger);
        cmdScreen.setLogger(logger);
    }

    private void addConfigInfo() {
        StringItem si = UIUtils.createStringItem(UIUtils.MEDIUM_FONT_BOLD, StringItem.LAYOUT_CENTER | StringItem.LAYOUT_NEWLINE_AFTER, config.getName());
        cpForm.append(si);
        if (!"".equals(Utils.safeString(config.getDesc()))) {
            si = UIUtils.createStringItem(UIUtils.SMALL_FONT_PLAINED, StringItem.LAYOUT_CENTER | StringItem.LAYOUT_NEWLINE_AFTER, "(" + config.getDesc() + ")");
            cpForm.append(si);
        }
        String info = "" + config.getContexts().size() + " Contexts" + "\n" + config.getCmdsCount() + " Commands";
        si = UIUtils.createStringItem(UIUtils.MEDIUM_FONT_PLAIN, StringItem.LAYOUT_CENTER | StringItem.LAYOUT_VCENTER, info);
        cpForm.append(si);
    }

    protected void addControlPanelCommands() {
        cpForm.addCommand(CMD_CLOSE);
        cpForm.addCommand(CMD_LOGS);
        Vector contexts = config.getContexts();
        for (int index = 0; index < contexts.size(); ++index) {
            ContextData ctx = (ContextData) contexts.elementAt(index);
            Command cmd = new Command(ctx.getDesc(), Command.SCREEN, index);
            cpForm.addCommand(cmd);
        }
    }

    private void getConfig() {
        if (config != null) return;
        if (!getAppCtx().inDebug()) {
            ClientConnection cc = (ClientConnection) connector.getConnection();
            config = cc.getConfig();
        } else {
            config = DebugAppContext.getTestConfig();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (d == cpForm) {
            if (c == CMD_DEBUG) {
            } else if (c == CMD_CLOSE) disconnectAndHide(); else if (c == CMD_LOGS) showLogs(); else contextSelected(c);
        } else {
            if (c == CMD_BACK) disconnectAndHide(); else cmdScreen.hide();
        }
    }

    private void disconnectAndHide() {
        showWaitGauge(cpForm, "Disconnecting ...");
        try {
            sml.interrupt();
            connector.close();
        } catch (Exception e) {
            logInfo("Error while disconnect : " + e.getMessage());
        } finally {
            hideWaitGauge();
            config = null;
            connector = null;
            hide();
        }
    }

    private void contextSelected(Command c) {
        Vector contexts = config.getContexts();
        for (int index = 0; index < contexts.size(); ++index) {
            Context ctx = (Context) contexts.elementAt(index);
            if (c.getLabel().equals(ctx.getDesc())) {
                currCtx = ctx;
                break;
            }
        }
        onContextSelected();
    }

    private void onContextSelected() {
        cmdScreen.setContext(currCtx);
        cmdScreen.show();
    }

    protected void onActivate() {
        currCtx = null;
        getDisplay().setCurrent(cpForm);
    }

    protected void onConnectionClosed() {
        cmdScreen.reset();
        show();
        disconnectAndHide();
    }

    private void onConnectionLost() {
        show();
        disconnectAndHide();
    }

    private void onCommandResponse(CommandMessage response) {
    }

    public void setConnector(ClientConnector connector) {
        this.connector = connector;
        ClientConnection cn = (ClientConnection) connector.getConnection();
        cmdScreen.setConnection(cn);
        getConfig();
        initControlPanelForm();
        sml = new ServerMessageListener(cn, sel);
        sml.addServerEventListener(cmdScreen);
    }

    private void initControlPanelForm() {
        cpForm = new Form(AppConstants.CMDPANEL_SCREEN_CAPTION);
        cpForm.setCommandListener(this);
        addConfigInfo();
        addControlPanelCommands();
    }
}
