package nl.contentanalysis.inet.editor;

import java.util.LinkedHashSet;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class AnnotationTableCursor extends TableCursor2 {

    public AnnotationTableCursor(Table parent, int style) {
        super(parent, style);
    }

    protected Set<Listener> tableKeyDownListeners = new LinkedHashSet<Listener>();

    public void addTableKeyDownListener(Listener l) {
        tableKeyDownListeners.add(l);
    }

    public void removeTableKeyDownListener(Listener l) {
        tableKeyDownListeners.remove(l);
    }

    @Override
    protected void keyDown(Event event) {
        for (Listener l : tableKeyDownListeners) l.handleEvent(event);
        if (event.doit) super.keyDown(event);
    }

    protected boolean blocktabs;

    public void setBlockTabs(boolean value) {
        blocktabs = true;
    }

    public boolean isBlockTabs() {
        return blocktabs;
    }

    @Override
    protected void traverse(Event event) {
        if (blocktabs && (event.detail == SWT.TRAVERSE_TAB_NEXT || event.detail == SWT.TRAVERSE_TAB_PREVIOUS)) event.doit = false; else super.traverse(event);
    }
}
