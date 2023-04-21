package net.sourceforge.processdash.ui.web.dash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DocumentOpenerImpl implements DocumentOpener {

    private List<DocumentOpener> delegates;

    DocumentOpenerImpl() {
        String packageName = DocumentOpenerImpl.class.getPackage().getName();
        delegates = new ArrayList<DocumentOpener>();
        for (String className : CLASSES) {
            try {
                String classFullName = packageName + "." + className;
                Object oneDelegate = Class.forName(classFullName).newInstance();
                delegates.add((DocumentOpener) oneDelegate);
            } catch (Throwable t) {
            }
        }
    }

    public boolean isSupported() {
        return !delegates.isEmpty();
    }

    public boolean openDocument(File doc) {
        for (DocumentOpener opener : delegates) if (opener.openDocument(doc)) return true;
        return false;
    }

    private static final String[] CLASSES = { "DocumentOpenerJava16", "DocumentOpenerWindows" };
}
