package cn.ekuma.erp.client;

import com.smartgwt.client.util.SC;

public class DebugConsoleCommand implements Command {

    @Override
    public void execute() {
        SC.showConsole();
    }
}
