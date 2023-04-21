package launcher.text.selected;

import launcher.Launcher;
import launcher.browser.BrowserLauncherType;
import launcher.text.TextLauncher;
import org.gjt.sp.jedit.View;

public abstract class ComputeURLBrowserLauncher extends TextLauncher {

    public ComputeURLBrowserLauncher(String prop, Object[] args) {
        this(prop, args, false, false);
    }

    public ComputeURLBrowserLauncher(String prop, Object[] args, boolean stateful, boolean userDefined) {
        super(prop, args, stateful, userDefined);
    }

    public Launcher getBrowserLauncher() {
        return BrowserLauncherType.INSTANCE.getDefaultLauncher();
    }

    @Override
    public boolean launch(View view, Object resource) {
        Launcher browserLauncher = getBrowserLauncher();
        if (browserLauncher == null) return false;
        String url = computeURL(view, resource);
        if (url == null) return false;
        return browserLauncher.launch(view, url);
    }

    @Override
    public String getCode() {
        Launcher browserLauncher = getBrowserLauncher();
        if (browserLauncher == null) return null;
        return browserLauncher.getCode();
    }

    @Override
    public String getCode(View view, Object resolvedResource) {
        Launcher browserLauncher = getBrowserLauncher();
        if (browserLauncher == null) return null;
        return browserLauncher.getCode(view, resolvedResource);
    }

    protected abstract String computeURL(View view, Object resource);
}
