package GUI.composites;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import openDocument.LoadFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import GUI.ListenerScriptColumn;
import GUI.cache.ImageCache;
import GUI.notifier.NotificationType;
import GUI.notifier.NotifierDialog;
import core.ApplicationCore;

public class ImportatoreComposite extends Composite implements ICustomPanel {

    private ApplicationCore core;

    /**
	 * Crea un composite di importazione, dal quale
	 * selezionare un file per l'immissione automatica
	 * nel DB locale.
	 * 
	 * @param parent pannello da riempire
	 * @param core Core dell'applicazione
	 */
    public ImportatoreComposite(Composite parent, ApplicationCore core) {
        super(parent, SWT.NONE);
        this.setLayout(new FormLayout());
        this.core = core;
        this.fill();
    }

    @Override
    public String getDisplayName() {
        return "Importazione Prodotti";
    }

    private void fill() {
        Group group = new Group(this, SWT.SHADOW_OUT);
        FormData fd_group = new FormData();
        fd_group.left = new FormAttachment(0);
        fd_group.right = new FormAttachment(100);
        fd_group.top = new FormAttachment(0);
        fd_group.bottom = new FormAttachment(100);
        group.setLayoutData(fd_group);
        group.setLayout(new FormLayout());
        Label lblInserisciIlPercorso = new Label(group, SWT.NONE);
        FormData fd_lblInserisciIlPercorso = new FormData();
        lblInserisciIlPercorso.setLayoutData(fd_lblInserisciIlPercorso);
        lblInserisciIlPercorso.setText("File .ODS:");
        final StyledText filePathText;
        filePathText = new StyledText(group, SWT.BORDER);
        fd_lblInserisciIlPercorso.right = new FormAttachment(filePathText, -5, SWT.LEFT);
        fd_lblInserisciIlPercorso.top = new FormAttachment(filePathText, 0, SWT.TOP);
        FormData fd_filePathText = new FormData();
        fd_filePathText.bottom = new FormAttachment(0, 35);
        fd_filePathText.right = new FormAttachment(100, -106);
        fd_filePathText.top = new FormAttachment(0, 14);
        fd_filePathText.left = new FormAttachment(0, 80);
        filePathText.setLayoutData(fd_filePathText);
        Button apriFile = new Button(group, SWT.NONE);
        FormData fd_apriFile = new FormData();
        fd_apriFile.left = new FormAttachment(filePathText, 9, SWT.DEFAULT);
        fd_apriFile.top = new FormAttachment(filePathText, 0, SWT.TOP);
        apriFile.setLayoutData(fd_apriFile);
        apriFile.setToolTipText("Sfoglia");
        apriFile.setText("Sfoglia...");
        final Button processaButton = new Button(group, SWT.NONE);
        FormData fd_processaButton = new FormData();
        fd_processaButton.left = new FormAttachment(43, 0);
        fd_processaButton.top = new FormAttachment(filePathText, 5, SWT.BOTTOM);
        processaButton.setLayoutData(fd_processaButton);
        processaButton.setToolTipText("Esamina il file Excel e inserisce i dati nel DataBase");
        processaButton.setText("Processa");
        final Table tabellaPreview = new Table(group, SWT.BORDER);
        FormData fd_tabellaPreview = new FormData();
        fd_tabellaPreview.right = new FormAttachment(100, -5);
        fd_tabellaPreview.bottom = new FormAttachment(100, -8);
        fd_tabellaPreview.left = new FormAttachment(0, 5);
        fd_tabellaPreview.top = new FormAttachment(processaButton, 9);
        tabellaPreview.setLayoutData(fd_tabellaPreview);
        tabellaPreview.setToolTipText("Anteprima del file importato");
        tabellaPreview.setEnabled(false);
        tabellaPreview.setLinesVisible(true);
        tabellaPreview.setHeaderVisible(true);
        processaButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                try {
                    int importati = core.importaProdotti(filePathText.getText());
                    if (importati == 0) NotifierDialog.notify("Nessun Prodotto importato!", "Non si sono verificati errori, ma nessun Prodotto è stato importato. Verificare il modello del file immesso.", NotificationType.TRANSACTION_FAIL); else NotifierDialog.notify(importati + " Prodotti Importati", "I nuovi prodotti sono stati salvati.", NotificationType.SUCCESS);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    NotifierDialog.notify("Errore Importazione Prodotti", "La configurazione del proxy non è valida:\n" + e.getMessage(), NotificationType.ERROR);
                }
                refreshAnteprimaTable(tabellaPreview, filePathText.getText());
                tabellaPreview.setEnabled(true);
                return;
            }
        });
        apriFile.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tabellaPreview.setEnabled(false);
                Button b = (Button) e.widget;
                FileDialog locnDir = new FileDialog(b.getShell(), SWT.OPEN);
                locnDir.setFilterPath("C:\\");
                String to = locnDir.open();
                if (to != null) filePathText.setText(to);
            }
        });
    }

    /**
	 * Parte di questo metodo andrebbe spostato e reso astratto, cosi e` molto
	 * lento assomiglia ad un test...
	 * 
	 * @param tabellaPreview
	 *            Tabella SWT su cui mostrare gli oggetti
	 * @param filepath
	 *            path del file da processare
	 */
    public void refreshAnteprimaTable(Table tabellaPreview, String filepath) {
        tabellaPreview.removeAll();
        LoadFile test = new LoadFile(filepath);
        LinkedList<HashMap<Integer, Object>> valuesSheet = test.getFileContent();
        Iterator<HashMap<Integer, Object>> it = valuesSheet.iterator();
        int rowcnt = 0;
        while (it.hasNext()) {
            HashMap<Integer, Object> type = (HashMap<Integer, Object>) it.next();
            if (rowcnt == 0) {
                int numcolonne = type.size();
                int cycle = 0;
                String[] intestazioni = new String[numcolonne];
                Set<Integer> vals = type.keySet();
                Iterator<Integer> it2 = vals.iterator();
                while (it2.hasNext()) {
                    Integer object = (Integer) it2.next();
                    String thisone = (String) (type.get(object).toString());
                    intestazioni[cycle++] = thisone;
                    final TableColumn newColumnTableColumn = new TableColumn(tabellaPreview, SWT.NONE);
                    newColumnTableColumn.setWidth(100);
                    newColumnTableColumn.setText(thisone);
                    newColumnTableColumn.addSelectionListener(new ListenerScriptColumn(tabellaPreview, ListenerScriptColumn.STRING_COMPARATOR));
                }
            } else {
                int tmp = 0;
                final TableItem newItemTableItem = new TableItem(tabellaPreview, SWT.BORDER);
                Set<Integer> vals = type.keySet();
                Iterator<Integer> it2 = vals.iterator();
                while (it2.hasNext()) {
                    Integer object = (Integer) it2.next();
                    newItemTableItem.setText(tmp++, type.get(object).toString());
                }
            }
            rowcnt++;
        }
    }

    @Override
    protected void checkSubclass() {
    }

    @Override
    public Composite getContent() {
        return this;
    }

    @Override
    public Image getIcon() {
        return ImageCache.getImage("080619-glossy-black-comment-bubble-icon-business-power-button4.png");
    }
}
