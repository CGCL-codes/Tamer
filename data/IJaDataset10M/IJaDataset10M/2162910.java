package edu.asu.vogon.fedora.texts.ui;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import edu.asu.vogon.fedora.connect.service.FedoraConnectionPool;
import edu.asu.vogon.fedora.texts.ui.control.FedoraConnectionsLabelProvider;
import edu.asu.vogon.fedora.texts.ui.control.FedoraConnectionsContentProvider;

public class FedoraTextsView extends ViewPart implements Observer {

    public static final String ID = "edu.asu.vogon.fedora.texts.ui.FedoraTextsView.navigation";

    private TreeViewer viewer;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        Composite header = new Composite(parent, SWT.NONE);
        header.setLayout(new FillLayout());
        {
            GridData data = new GridData();
            data.grabExcessHorizontalSpace = true;
            data.horizontalAlignment = GridData.FILL;
            header.setLayoutData(data);
        }
        Composite viewerComposite = new Composite(parent, SWT.NONE);
        viewerComposite.setLayout(new FillLayout());
        {
            GridData data = new GridData();
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            viewerComposite.setLayoutData(data);
        }
        viewer = new TreeViewer(viewerComposite);
        viewer.setContentProvider(new FedoraConnectionsContentProvider());
        viewer.setLabelProvider(new FedoraConnectionsLabelProvider());
        viewer.setInput(FedoraConnectionPool.INSTANCE);
        FedoraConnectionPool.INSTANCE.addObserver(this);
    }

    @Override
    public void setFocus() {
    }

    public void update(Observable o, Object arg) {
        if (o instanceof FedoraConnectionPool) {
            viewer.refresh();
        }
    }
}
