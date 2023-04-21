package org.openqa.selenium.server.browserlaunchers;

import org.openqa.selenium.server.BrowserConfigurationOptions;
import org.openqa.selenium.server.RemoteControlConfiguration;

public class SafariLauncher implements BrowserLauncher {

    final BrowserLauncher realLauncher;

    public SafariLauncher(BrowserConfigurationOptions browserOptions, RemoteControlConfiguration configuration, String sessionId, String browserLaunchLocation) {
        String mode = browserOptions.get("mode");
        if (mode == null) mode = "filebased";
        if ("default".equals(mode)) mode = "filebased";
        if ("filebased".equals(mode)) {
            realLauncher = new SafariFileBasedLauncher(browserOptions, configuration, sessionId, browserLaunchLocation);
            return;
        }
        boolean proxyInjectionMode = browserOptions.is("proxyInjectionMode") || "proxyInjection".equals(mode);
        boolean globalProxyInjectionMode = configuration.getProxyInjectionModeArg();
        if (proxyInjectionMode && !globalProxyInjectionMode) {
            if (proxyInjectionMode) {
                throw new RuntimeException("You requested proxy injection mode, but this server wasn't configured with -proxyInjectionMode on the command line");
            }
        }
        proxyInjectionMode = globalProxyInjectionMode;
        if (proxyInjectionMode) {
            realLauncher = new ProxyInjectionSafariCustomProfileLauncher(browserOptions, configuration, sessionId, browserLaunchLocation);
            return;
        }
        if (!"proxy".equals(mode)) {
            throw new RuntimeException("Unrecognized browser mode: " + mode);
        }
        realLauncher = new SafariCustomProfileLauncher(browserOptions, configuration, sessionId, browserLaunchLocation);
    }

    public void close() {
        realLauncher.close();
    }

    public Process getProcess() {
        return realLauncher.getProcess();
    }

    public void launchHTMLSuite(String suiteUrl, String baseUrl) {
        realLauncher.launchHTMLSuite(suiteUrl, baseUrl);
    }

    public void launchRemoteSession(String url) {
        realLauncher.launchRemoteSession(url);
    }
}
