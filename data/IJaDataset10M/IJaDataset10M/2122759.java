package net.sf.pdfizer.wizards;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import net.sf.pdfizer.MediaComposite;
import net.sf.pdfizer.PDFiZerPlugin;
import net.sf.pdfizer.model.Catalog;
import net.sf.pdfizer.model.ICatalogReferencer;
import net.sf.pdfizer.model.ModelController;
import net.sf.pdfizer.preferences.PreferenceConstants;
import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SelectCatalogPage extends WizardPage implements ICatalogReferencer {

    private static final Logger LOG = Logger.getLogger(SelectCatalogPage.class);

    private Combo catalogCombo;

    private Text pathText;

    private String catalogName;

    private Catalog catalog;

    protected SelectCatalogPage(ImageDescriptor id) {
        super("chose_catalog");
        setTitle("Add document to catalog");
        setDescription("Select target catalog");
        setImageDescriptor(id);
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        layout.verticalSpacing = 9;
        Label subjectLabel = new Label(container, SWT.NULL);
        subjectLabel.setText("&Catalog:");
        catalogCombo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
        fillMediaList(catalogCombo);
        catalogCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                catalogName = catalogCombo.getItem(catalogCombo.getSelectionIndex());
                LOG.debug("media name set:" + catalogName);
                catalog = ModelController.getInstance().getCatalog(catalogName);
                pathText.setText(String.valueOf(catalog.getRealPath()));
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        catalogCombo.setLayoutData(gd);
        Label pathLabel = new Label(container, SWT.NULL);
        pathLabel.setText("&Path:");
        pathText = new Text(container, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        pathText.setLayoutData(gd);
        boolean haveCatalogs = catalogCombo.getItemCount() > 0;
        if (haveCatalogs) catalogName = catalogCombo.getItem(0); else {
            setErrorMessage(getDescription() + ": There are no catalogs to populate!");
        }
        setPageComplete(haveCatalogs);
        setControl(container);
    }

    private void fillMediaList(Combo c) {
        Collection mediaCollection = ModelController.getInstance().getAllMediaCatalogs();
        for (Iterator catalogColIter = mediaCollection.iterator(); catalogColIter.hasNext(); ) {
            Catalog catalog = (Catalog) catalogColIter.next();
            System.out.println("Catalog path:" + catalog.getPath());
            if (new File(catalog.getPath()).exists()) {
                catalogCombo.add(catalog.getName());
            } else {
                String mp = PDFiZerPlugin.getInstance().getPreferenceStore().getString(PreferenceConstants.REMOVABLE_MEDIA_PATH);
                if (catalog.isRemovable() && catalog.getPath().equals(MediaComposite.CDDVD_MEDIA) && new File(mp).exists()) {
                    catalogCombo.add(catalog.getName());
                }
            }
        }
    }

    public String getCatalogName() {
        return catalogName;
    }

    public Catalog getCatalog() {
        return catalog;
    }
}
