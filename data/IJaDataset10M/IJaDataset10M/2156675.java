package org.bitdrive.core.impl;

import org.bitdrive.access.dokan.api.DokanManager;
import org.bitdrive.core.settings.api.Settings;
import org.bitdrive.file.movies.api.LocalMovieList;
import org.bitdrive.network.core.api.Connection;
import org.bitdrive.network.core.api.P2PServer;
import org.bitdrive.network.history.api.ConnectionHistory;
import org.bitdrive.network.http.api.HTTPService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Activator implements BundleActivator {

    private Connection connection;

    private final String SETTING_USER_PASS = "SETTING_USER_PASS";

    private Object getServiceOrFail(BundleContext bundleContext, String name) throws Exception {
        ServiceReference ref = bundleContext.getServiceReference(name);
        if (ref != null) {
            return bundleContext.getService(ref);
        } else {
            throw new Exception("Failed to start (need " + name + ")");
        }
    }

    public void start(BundleContext bundleContext) throws Exception {
        String nick;
        String pass;
        String host = null;
        String folder = null;
        Scanner input;
        P2PServer server;
        HTTPService http;
        Settings settings;
        DokanManager dokanManager;
        LocalMovieList localMovieList;
        ConnectionHistory history;
        http = (HTTPService) getServiceOrFail(bundleContext, HTTPService.class.getName());
        settings = (Settings) getServiceOrFail(bundleContext, Settings.class.getName());
        history = (ConnectionHistory) getServiceOrFail(bundleContext, ConnectionHistory.class.getName());
        localMovieList = (LocalMovieList) getServiceOrFail(bundleContext, LocalMovieList.class.getName());
        dokanManager = (DokanManager) getServiceOrFail(bundleContext, DokanManager.class.getName());
        pass = settings.getString(SETTING_USER_PASS, "bitdrive");
        input = new Scanner(System.in);
        if ((nick = settings.getString(Settings.SETTING_USER_NICK)) == null) {
            while (nick == null) {
                System.out.print("Please enter nickname: ");
                nick = input.nextLine();
                if (nick.length() < 2) nick = null;
            }
            settings.setString(Settings.SETTING_USER_NICK, nick);
        }
        http.start();
        server = (P2PServer) getServiceOrFail(bundleContext, P2PServer.class.getName());
        server.loadSSL(nick, pass);
        server.start(settings.getInt(P2PServer.SETTING_SERVER_PORT, P2PServer.SETTING_SERVER_PORT_DEFAULT));
        for (Object[] item : history.getAutoConnectEntries()) server.connect((String) item[0], (Integer) item[1], false);
        while (folder == null) {
            File movieFolder;
            System.out.print("Add movie folder [enter = skip]: ");
            folder = input.nextLine();
            if (folder.equals("")) break;
            movieFolder = new File(folder);
            if (!movieFolder.exists()) {
                folder = null;
                continue;
            }
            System.out.print("Scanning folder...");
            localMovieList.scanFolder(movieFolder);
            System.out.println("done");
        }
        System.out.print("Updating movie folders...");
        localMovieList.update();
        System.out.println("done");
        System.out.print("Fetching meta data...");
        localMovieList.fetchMetaData();
        System.out.println("done");
        dokanManager.mount();
        while (host == null) {
            System.out.print("Please enter host: [q = quit]");
            host = input.nextLine();
            if (host.equals("q")) {
                dokanManager.unmount();
                System.exit(0);
            }
            if (host.length() < 2) {
                host = null;
                continue;
            }
            System.out.print("Connecting to '" + host + "'...");
            try {
                server.connect(host, P2PServer.SETTING_SERVER_PORT_DEFAULT, true);
                System.out.println("ok");
            } catch (IOException e) {
                System.out.println("failed!");
            }
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
    }
}
