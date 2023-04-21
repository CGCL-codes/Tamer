package de.beas.explicanto.client.rcp.pageedit2;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import de.bea.services.vidya.client.datastructures.CHotspot;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.hotspot.HotspotEditor;
import de.beas.explicanto.client.rcp.hotspot.HotspotEditor.CancelledException;

public class PageComponentHotspot extends PageComponent {

    private static final Logger log = Logger.getLogger(PageComponentHotspot.class);

    private Button button;

    public PageComponentHotspot(Composite parent, PageRegion region, CHotspot component) {
        super(parent, region, component);
    }

    protected CHotspot getHotspot() {
        return (CHotspot) getComponent();
    }

    protected Control createContent(final Composite parent) {
        log.debug("creating content composite");
        button = new Button(parent, SWT.NONE);
        String name = getHotspot().getName();
        button.setText(name != null ? name : I18N.translate("components.names.hotspot"));
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                openEditor(parent.getShell());
            }
        });
        return button;
    }

    protected Color getActiveBorderColor() {
        return ColorConstants.lightGray;
    }

    protected Color getInactiveBorderColor() {
        return ColorConstants.black;
    }

    protected void openEditor(Shell parent) {
        if (!isPageParentLocked()) return;
        try {
            HotspotEditor he = new HotspotEditor(parent, getHotspot());
            boolean modified = he.open();
            if (modified) {
                region.getEditor().setModified(true);
                button.setText(getHotspot().getName());
            }
        } catch (CancelledException e) {
        }
    }

    protected String getComponentType() {
        return "components.names.hotspot";
    }
}
