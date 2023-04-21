package br.com.visualmidia.ui;

import java.beans.Statement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import br.com.visualmidia.system.GDWindowControl;
import br.com.visualmidia.ui.controlcenter.HomeControlCenter;

public class HomeReportsRepository extends Composite {

    private Label titleLabel;

    private Composite titleComposite;

    private Composite itensAreaComposite;

    private Color defaultBackgroundColor = new Color(null, 176, 185, 216);

    public HomeReportsRepository(Composite parent, int top, int topPx, int left, int leftPx, int right, int rightPx) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.marginWidth = 1;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);
        setBackground(defaultBackgroundColor);
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setForeground(new Color(null, 121, 136, 179));
                gc.drawLine(0, 0, 0, getClientArea().height);
                gc.drawLine(0, 0, getClientArea().width, 0);
                gc.drawLine(getClientArea().width - 1, 0, getClientArea().width - 1, getClientArea().height);
                gc.drawLine(0, getClientArea().height - 1, getClientArea().width - 1, getClientArea().height - 1);
                gc.dispose();
            }
        });
        FormData data = new FormData();
        data.top = new FormAttachment(top, topPx);
        data.left = new FormAttachment(left, leftPx);
        data.right = new FormAttachment(right, rightPx);
        this.setLayoutData(data);
    }

    public void setTitle(String title) {
        if (titleComposite == null) createTitle();
        titleLabel.setText(title);
    }

    private void createTitle() {
        titleComposite = new Composite(this, SWT.NONE);
        titleComposite.setLayout(new RowLayout());
        Color titleBackgroundColor = new Color(null, 151, 163, 201);
        Color titleFontColor = new Color(null, 51, 60, 97);
        Font titleFont = new Font(null, "Arial", 8, SWT.BOLD);
        titleComposite.setBackground(titleBackgroundColor);
        titleComposite.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setForeground(new Color(null, 210, 217, 244));
                gc.drawLine(0, 0, titleComposite.getClientArea().width - 1, 0);
                gc.dispose();
            }
        });
        titleLabel = new Label(titleComposite, SWT.NONE);
        titleLabel.setBackground(titleBackgroundColor);
        titleLabel.setForeground(titleFontColor);
        titleLabel.setFont(titleFont);
        titleBackgroundColor.dispose();
        titleFontColor.dispose();
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 20;
        titleComposite.setLayoutData(gridData);
    }

    public void addItem(String itemTitle, final Object screen, final String method) {
        itensAreaComposite = new Composite(this, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 3;
        gridLayout.marginWidth = 5;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 5;
        itensAreaComposite.setLayout(gridLayout);
        final Color itensAreaFontColorEnter = new Color(null, 51, 60, 97);
        final Color itensAreaFontColor = new Color(null, 0, 0, 0);
        itensAreaComposite.setBackground(defaultBackgroundColor);
        Label image = new Label(itensAreaComposite, SWT.NONE);
        image.setBackground(defaultBackgroundColor);
        image.setImage(new Image(null, "img/mark.png"));
        final Label label = new Label(itensAreaComposite, SWT.NONE);
        label.setText(itemTitle);
        label.setBackground(defaultBackgroundColor);
        label.addListener(SWT.MouseEnter, new Listener() {

            public void handleEvent(Event arg0) {
                label.setForeground(itensAreaFontColorEnter);
            }
        });
        label.addListener(SWT.MouseExit, new Listener() {

            public void handleEvent(Event arg0) {
                label.setForeground(itensAreaFontColor);
            }
        });
        label.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event arg0) {
                if (screen instanceof MainScreen) executeMethod(method, GDWindowControl.getInstance()); else ((HomeControlCenter) screen).goTo(method);
            }
        });
    }

    public void executeMethod(String methodName, Object whereTheMethodWillBeExecuted) {
        try {
            new Statement(whereTheMethodWillBeExecuted, methodName, new Object[] {}).execute();
        } catch (Exception e) {
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
