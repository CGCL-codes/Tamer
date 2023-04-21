package ru.ipo.dces.tests.test;

import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.plugins.ACMLitePlugin;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 30.01.2009
 * Time: 19:18:30
 */
public class TestPlugin {

    private static final String SERVER_URL = "http://vm-2.spb.ru/~posov/dces/dces.php";

    public static final String LOGIN = "admin";

    public static final String PASSWORD = "pass";

    public static final int CONTEST_ID = 50;

    public static final String CLIENT_PLUGIN_ALIAS = "ACMLitePlugin2";

    public static final String SERVER_PLUGIN_ALIAS = "MyDebugPlugin2";

    public static final String CLIENT_PLUGIN_PATH = "d:\\programming\\DCES\\plugins\\ACMLitePlugin.jar";

    public static final String SERVER_PLUGIN_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\MyDebugPlugin2.php";

    public static final String DEBUG_STATEMENT_FOLDER = "C:/programming/dces/debug-problem-folder/";

    public static final Class<? extends Plugin> PLUGIN_CLASS = ACMLitePlugin.class;

    public static final String PROBLEM_NAME = "Имя задачи";

    private static final String STATEMENT_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\1\\task";

    private static final String ANSWER_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\1\\answer\\01.a";

    public static void main(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
        HttpServer server = new HttpServer(SERVER_URL);
        ServerPluginProxy proxy = new ServerPluginProxy(server, LOGIN, PASSWORD, true);
        proxy.selectContest(CONTEST_ID);
        proxy.uploadClientPlugin(CLIENT_PLUGIN_ALIAS, new File(CLIENT_PLUGIN_PATH));
        proxy.uploadServerPlugin(SERVER_PLUGIN_ALIAS, new File(SERVER_PLUGIN_PATH));
        proxy.setStatementFolder(new File(DEBUG_STATEMENT_FOLDER));
        proxy.createProblem(CLIENT_PLUGIN_ALIAS, SERVER_PLUGIN_ALIAS, new File(STATEMENT_PATH), new File(ANSWER_PATH));
        proxy.newParticipant();
        PluginBox box = new PluginBox(PLUGIN_CLASS, proxy, PROBLEM_NAME);
        box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        box.setVisible(true);
    }
}
