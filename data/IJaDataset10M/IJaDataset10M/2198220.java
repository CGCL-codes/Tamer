package GUI.composites;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import GUI.CompositeLoaderDialog;
import GUI.ListenerScriptColumn;
import GUI.cache.ImageCache;
import bean.Prodotto;
import core.ApplicationCore;

/**
 * Pannello di visualizzazione dei prodotti 'locali' ovvero sul DB
 * Mostra una tabella preceduta da una serie di filtri di visualizzazione
 * 
 * @author pegoraro
 * 
 *  [SVN] Last commit: $Author: donacot $ 
 *  [SVN] URL: $URL: http://donatoguy.svn.sourceforge.net/svnroot/donatoguy/src/GUI/composites/CercaProdottoComposite.java $
 * 
 * @version [SVN] $Rev: 194 $
 * @since 0.1.132
 *
 */
public class CercaProdottoComposite extends Composite {

    private static Table table;

    private Text textCodice;

    private Text textBarcode;

    private Text textDescrizione;

    private Text textMarca;

    private Text textModello;

    private Text textCategoria;

    private static Logger log = Logger.getLogger(OpzioniComposite.class.getName());

    private static Image icon = ImageCache.getImage("080587-glossy-black-comment-bubble-icon-business-magnifying-glass-ps.png");

    private static Image iconWand = ImageCache.getImage("080679-glossy-black-comment-bubble-icon-business-wand1-sc43.png");

    private CompositeLoaderDialog load;

    /**
	 * Crea il composite di ricerca
	 * 
	 * @param parent
	 * @param style
	 */
    public CercaProdottoComposite(final Composite parent, int style, final ApplicationCore core) {
        super(parent, style);
        setLayout(new FormLayout());
        load = new CompositeLoaderDialog(core, parent.getShell(), style);
        final CompositeLoaderDialog loader = load;
        final Group filtriDiRicercaGroup = new Group(this, SWT.NONE);
        filtriDiRicercaGroup.setLayout(new FormLayout());
        final FormData fd_filtriDiRicercaGroup = new FormData();
        fd_filtriDiRicercaGroup.bottom = new FormAttachment(0, 165);
        fd_filtriDiRicercaGroup.right = new FormAttachment(100, -10);
        fd_filtriDiRicercaGroup.top = new FormAttachment(0, 0);
        fd_filtriDiRicercaGroup.left = new FormAttachment(0, 10);
        filtriDiRicercaGroup.setLayoutData(fd_filtriDiRicercaGroup);
        filtriDiRicercaGroup.setText("Filtri di Ricerca");
        table = new Table(this, SWT.BORDER);
        final FormData fd_table = new FormData();
        fd_table.top = new FormAttachment(filtriDiRicercaGroup, 6);
        fd_table.right = new FormAttachment(filtriDiRicercaGroup, 0, SWT.RIGHT);
        fd_table.bottom = new FormAttachment(100, -10);
        fd_table.left = new FormAttachment(filtriDiRicercaGroup, 0, SWT.LEFT);
        table.setLayoutData(fd_table);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        textCodice = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text = new FormData();
        fd_text.bottom = new FormAttachment(0, 27);
        fd_text.top = new FormAttachment(0, 6);
        fd_text.right = new FormAttachment(0, 200);
        fd_text.left = new FormAttachment(0, 91);
        textCodice.setText("<Mostra Tutti>");
        textCodice.setLayoutData(fd_text);
        Label lblCodice = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblCodice = new FormData();
        fd_lblCodice.bottom = new FormAttachment(0, 24);
        fd_lblCodice.top = new FormAttachment(0, 9);
        fd_lblCodice.right = new FormAttachment(0, 72);
        fd_lblCodice.left = new FormAttachment(0, 17);
        lblCodice.setLayoutData(fd_lblCodice);
        lblCodice.setText("Codice");
        Label lblBarcode = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblBarcode = new FormData();
        fd_lblBarcode.bottom = new FormAttachment(0, 21);
        fd_lblBarcode.top = new FormAttachment(0, 6);
        fd_lblBarcode.right = new FormAttachment(0, 303);
        fd_lblBarcode.left = new FormAttachment(0, 248);
        lblBarcode.setLayoutData(fd_lblBarcode);
        lblBarcode.setText("Barcode");
        textBarcode = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text_1 = new FormData();
        fd_text_1.right = new FormAttachment(100, -86);
        fd_text_1.bottom = new FormAttachment(0, 27);
        fd_text_1.top = new FormAttachment(0, 6);
        fd_text_1.left = new FormAttachment(0, 309);
        textBarcode.setLayoutData(fd_text_1);
        textDescrizione = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text_2 = new FormData();
        fd_text_2.right = new FormAttachment(100, -86);
        fd_text_2.bottom = new FormAttachment(0, 54);
        fd_text_2.top = new FormAttachment(0, 33);
        fd_text_2.left = new FormAttachment(0, 91);
        textDescrizione.setText("<Mostra Tutti>");
        textDescrizione.setLayoutData(fd_text_2);
        Label lblDescrizione = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblDescrizione = new FormData();
        fd_lblDescrizione.bottom = new FormAttachment(0, 51);
        fd_lblDescrizione.top = new FormAttachment(0, 36);
        fd_lblDescrizione.right = new FormAttachment(0, 85);
        fd_lblDescrizione.left = new FormAttachment(0, 17);
        lblDescrizione.setLayoutData(fd_lblDescrizione);
        lblDescrizione.setText("Descrizione");
        Label lblFornitore = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblFornitore = new FormData();
        fd_lblFornitore.bottom = new FormAttachment(0, 78);
        fd_lblFornitore.top = new FormAttachment(0, 63);
        fd_lblFornitore.right = new FormAttachment(0, 72);
        fd_lblFornitore.left = new FormAttachment(0, 17);
        lblFornitore.setLayoutData(fd_lblFornitore);
        lblFornitore.setText("Fornitore");
        Combo combo = new Combo(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_combo = new FormData();
        fd_combo.right = new FormAttachment(100, -234);
        fd_combo.bottom = new FormAttachment(0, 81);
        fd_combo.top = new FormAttachment(0, 60);
        fd_combo.left = new FormAttachment(0, 91);
        combo.setLayoutData(fd_combo);
        Label lblMarca = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblMarca = new FormData();
        fd_lblMarca.bottom = new FormAttachment(0, 104);
        fd_lblMarca.top = new FormAttachment(0, 89);
        fd_lblMarca.right = new FormAttachment(0, 72);
        fd_lblMarca.left = new FormAttachment(0, 17);
        lblMarca.setLayoutData(fd_lblMarca);
        lblMarca.setText("Marca");
        textMarca = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text_3 = new FormData();
        fd_text_3.bottom = new FormAttachment(0, 110);
        fd_text_3.top = new FormAttachment(0, 89);
        fd_text_3.right = new FormAttachment(0, 200);
        fd_text_3.left = new FormAttachment(0, 91);
        textMarca.setText("<Mostra Tutti>");
        textMarca.setLayoutData(fd_text_3);
        textCategoria = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text_8 = new FormData();
        fd_text_8.right = new FormAttachment(100, -86);
        fd_text_8.bottom = new FormAttachment(0, 110);
        fd_text_8.top = new FormAttachment(0, 89);
        fd_text_8.left = new FormAttachment(0, 309);
        textCategoria.setLayoutData(fd_text_8);
        Label lblFamcat = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblFamcat = new FormData();
        fd_lblFamcat.bottom = new FormAttachment(0, 104);
        fd_lblFamcat.top = new FormAttachment(0, 89);
        fd_lblFamcat.right = new FormAttachment(0, 303);
        fd_lblFamcat.left = new FormAttachment(0, 248);
        lblFamcat.setLayoutData(fd_lblFamcat);
        lblFamcat.setText("Fam/Cat");
        Label lblModello = new Label(filtriDiRicercaGroup, SWT.NONE);
        final FormData fd_lblModello = new FormData();
        fd_lblModello.bottom = new FormAttachment(0, 136);
        fd_lblModello.top = new FormAttachment(0, 121);
        fd_lblModello.right = new FormAttachment(0, 72);
        fd_lblModello.left = new FormAttachment(0, 17);
        lblModello.setLayoutData(fd_lblModello);
        lblModello.setText("Modello");
        textModello = new Text(filtriDiRicercaGroup, SWT.BORDER);
        final FormData fd_text_4 = new FormData();
        fd_text_4.bottom = new FormAttachment(0, 142);
        fd_text_4.top = new FormAttachment(0, 121);
        fd_text_4.right = new FormAttachment(0, 200);
        textModello.setText("<Mostra Tutti>");
        fd_text_4.left = new FormAttachment(0, 91);
        textModello.setLayoutData(fd_text_4);
        try {
            List<Prodotto> found = core.findProdotti(new Prodotto());
            refreshProdottiTable(found);
        } catch (SQLException e) {
            log.error("Impossibile aggiornare la tabella dei prodotti:", e);
        }
        ModifyListener searcher = new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                try {
                    Prodotto pivot = new Prodotto();
                    if (textMarca.getText().compareTo("<Mostra Tutti>") != 0) pivot.setMarca(textMarca.getText());
                    if (textCodice.getText().compareTo("<Mostra Tutti>") != 0) pivot.setCodice(textCodice.getText());
                    if (textModello.getText().compareTo("<Mostra Tutti>") != 0) pivot.setModello(textModello.getText());
                    if (textDescrizione.getText().compareTo("<Mostra Tutti>") != 0) pivot.setDescrizione(textDescrizione.getText());
                    List<Prodotto> found = core.findProdotti(pivot);
                    refreshProdottiTable(found);
                } catch (SQLException e) {
                    log.error("Impossibile aggiornare la tabella dei prodotti:", e);
                }
            }
        };
        textMarca.addModifyListener(searcher);
        textCodice.addModifyListener(searcher);
        textDescrizione.addModifyListener(searcher);
        textModello.addModifyListener(searcher);
        table.addListener(SWT.MouseDoubleClick, new Listener() {

            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt)) {
                            if (i == 0) {
                                loader.createContents(ProdottoDetailComposite.class.getName(), (Prodotto) item.getData(), new Point(450, 300));
                                loader.open();
                                return;
                            } else if (i == 1) {
                                loader.createContents(GeneratoreComposite.class.getName(), (Prodotto) item.getData(), new Point(450, 300));
                                loader.open();
                                return;
                            }
                            System.out.println("Item " + index + "-" + i);
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
            }
        });
    }

    /**
	 * Aggiorna la tabella dei prodotti, svuotandola e 
	 * ri-popolandola con l'input
	 * 
	 * @param prods List dei prodotti da visualizzare
	 */
    public static void refreshProdottiTable(List<Prodotto> prods) {
        table.removeAll();
        Iterator<Prodotto> it = prods.iterator();
        if (table.getColumnCount() < 2) {
            final TableColumn iconTableColumn = new TableColumn(table, SWT.NONE);
            iconTableColumn.setWidth(36);
            iconTableColumn.setText("");
            iconTableColumn.setToolTipText("Fare doppio click su questa colonna per aprire il dettaglio del prodotto corrispondente");
            final TableColumn expTableColumn = new TableColumn(table, SWT.NONE);
            expTableColumn.setWidth(36);
            expTableColumn.setText("");
            expTableColumn.setToolTipText("Fare doppio click su questa colonna per aprire la generazione del HTML per il prodotto corrispondente");
            final TableColumn codiceTableColumn = new TableColumn(table, SWT.NONE);
            codiceTableColumn.setWidth(60);
            codiceTableColumn.setText("Codice");
            codiceTableColumn.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.STRING_COMPARATOR));
            final TableColumn categoriaTableColumn = new TableColumn(table, SWT.NONE);
            categoriaTableColumn.setWidth(60);
            categoriaTableColumn.setText("Categoria");
            categoriaTableColumn.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.STRING_COMPARATOR));
            final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
            newColumnTableColumn.setWidth(100);
            newColumnTableColumn.setText("Marca");
            newColumnTableColumn.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.STRING_COMPARATOR));
            final TableColumn ModelloColumnTableColumn = new TableColumn(table, SWT.NONE);
            ModelloColumnTableColumn.setWidth(120);
            ModelloColumnTableColumn.setText("Modello");
            ModelloColumnTableColumn.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.STRING_COMPARATOR));
            final TableColumn newColumnTableColumnCat = new TableColumn(table, SWT.NONE);
            newColumnTableColumnCat.setWidth(250);
            newColumnTableColumnCat.setText("Serie");
            newColumnTableColumnCat.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.STRING_COMPARATOR));
            final TableColumn newColumnTableColumnPrezzo = new TableColumn(table, SWT.NONE);
            newColumnTableColumnPrezzo.setWidth(80);
            newColumnTableColumnPrezzo.setText("Prezzo");
            newColumnTableColumnPrezzo.addSelectionListener(new ListenerScriptColumn(table, ListenerScriptColumn.INT_COMPARATOR));
        }
        int rowcnt = 0;
        while (it.hasNext()) {
            Prodotto type = (Prodotto) it.next();
            int c = 0;
            final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
            newItemTableItem.setData(type);
            newItemTableItem.setImage(c++, icon);
            newItemTableItem.setImage(c++, iconWand);
            newItemTableItem.setText(c++, type.getCodice());
            newItemTableItem.setText(c++, type.getCategoria());
            newItemTableItem.setText(c++, type.getMarca());
            newItemTableItem.setText(c++, type.getNomeModello());
            newItemTableItem.setText(c++, type.getNomeSerie());
            if (type.getPrezzo() != null && type.getPrezzo().compareTo("") != 0) newItemTableItem.setText(3, type.getPrezzo() + "€");
            rowcnt++;
        }
    }

    @Override
    protected void checkSubclass() {
    }
}
