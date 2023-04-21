package org.kommando.application.win32;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.kommando.application.core.Application;
import org.kommando.application.core.ApplicationProvider;
import org.kommando.core.util.Assert;
import org.kommando.filesystem.core.FileSource;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Variant;

/**
 * win32 {@link ApplicationProvider} implementation.
 * <p>
 * Uses the ActiveX WScript.Shell object to load the installed applications
 * 
 * @author Peter De Bruycker
 */
public class Win32ApplicationProvider implements ApplicationProvider {

    private Win32ApplicationContext context;

    public Win32ApplicationProvider(Win32ApplicationContext context) {
        Assert.argumentNotNull("context", context);
        this.context = context;
    }

    @Override
    public List<Application> getApplications() {
        List<Application> applications = new ArrayList<Application>();
        ComThread.InitSTA();
        ActiveXComponent shell = new ActiveXComponent("WScript.Shell");
        String programs = shell.invoke("SpecialFolders", new Variant("Programs")).getString();
        String allUserPrograms = shell.invoke("SpecialFolders", new Variant("AllUsersPrograms")).getString();
        collectApplications(programs, applications);
        collectApplications(allUserPrograms, applications);
        shell.safeRelease();
        ComThread.Release();
        return applications;
    }

    private void collectApplications(String directory, List<Application> results) {
        FileSource fileSource = new FileSource(directory, false, false, -1, Arrays.asList(".exe", ".lnk"));
        for (File file : fileSource.getFiles()) {
            results.add(new Win32Application(context, file));
        }
    }
}
