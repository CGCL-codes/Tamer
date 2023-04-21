package com.mgensystems.mdss.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import com.mgensystems.eventmanager.EventManager;
import com.mgensystems.mdss.dependency.ImportDependencies;
import com.mgensystems.mdss.event.ClassFoundEvent;
import com.mgensystems.mdss.event.FileScannedEvent;
import com.mgensystems.mdss.event.ScannerFinishedEvent;
import com.mgensystems.mdss.event.ScannerStartedEvent;

public abstract class SourceScanner {

    private List<String> extensions = new ArrayList<String>();

    public abstract Map<String, ImportDependencies> scan(File f, boolean verbose);

    protected Iterator getFiles(File f) {
        return FileUtils.iterateFiles(f, extensions.toArray(ArrayUtils.EMPTY_STRING_ARRAY), true);
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public void setExtensions(String extensions) {
        String[] m = extensions.split(",");
        if (m.length > 1) {
            this.extensions.addAll(Arrays.asList(m));
        } else {
            this.extensions.add(extensions);
        }
    }

    protected void fireFileScannedEvent(FileScannedEvent event) {
        EventManager.addEvent(event);
    }

    protected void fireScanFinishedEvent(ScannerFinishedEvent event) {
        EventManager.addEvent(event);
    }

    protected void fireClassFoundEvent(ClassFoundEvent event) {
        EventManager.addEvent(event);
    }

    protected void fireScannerStartedEvent(ScannerStartedEvent event) {
        EventManager.addEvent(event);
    }
}
