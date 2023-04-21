package gui;

import events.EdicolaModelEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import listener.TableCatalogoClickListener;
import catalog.ArticoliEnum;
import catalog.Articolo;
import structure.Edicola;
import utility.SortType;
import utility.UtilityConversion;
import controller.EdicolaViewController;
import controller.UIController;

/**
 * Rappresenta la finestra di gestione della comunicazione con l'edicola. 
 * Si apre una volta che la connessione con l'edicola � stabilita  
 */
@SuppressWarnings("serial")
public class EdicolaConnectedView extends UIModelEventListenerView {

    private JPanel _centerPanel;

    private JPanel _leftPanel;

    private JFrame _thisFrame;

    private Container _container;

    private JPanel _searchPanel;

    private JPanel _orderPanel;

    private JPanel _buttonPanel;

    private JLabel _labelTitolo;

    private JLabel _labelCategoria;

    private JTextField _textTitolo;

    private JComboBox _categoriaCombo;

    private JButton _buttonShow;

    private JButton _buttonShowAll;

    private JLabel _labelOrder;

    private JComboBox _columnCombo;

    private JButton _buttonDisconnect;

    private JButton _buttonNewsletter;

    private JTable _table;

    private JScrollPane _catalogScrollPane;

    private JFrame _mainFrame;

    private Edicola _edicola;

    /**
	 * Costruttore
	 * @param containerFrame � il frame principale
	 */
    public EdicolaConnectedView(JFrame containerFrame) {
        super(containerFrame);
        _thisFrame = containerFrame;
        initializeView();
    }

    /**
	 * Inizializza la vista completa
	 */
    private void initializeView() {
        _container = _thisFrame.getContentPane();
        _container.setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon(getClass().getResource("image/house.png"));
        _thisFrame.setIconImage(image.getImage());
        initializeCenterPanel();
        initializeLeftPanel();
        int width = 850;
        int height = 440;
        _thisFrame.setSize(width, height);
        _thisFrame.setMinimumSize(new Dimension(width, height));
        _thisFrame.setMaximumSize(new Dimension(width, height));
        _thisFrame.setLocation(200, 100);
        _thisFrame.pack();
        _thisFrame.setVisible(true);
        _thisFrame.setResizable(false);
    }

    /**
	 * Inizializza il pannello di sinistra
	 */
    private void initializeLeftPanel() {
        _leftPanel = new JPanel();
        int width = 240;
        int height = 350;
        _leftPanel.setPreferredSize(new Dimension(width, height));
        _leftPanel.setMaximumSize(new Dimension(width, height));
        _leftPanel.setMinimumSize(new Dimension(width, height));
        _container.add(_leftPanel, BorderLayout.WEST);
        _searchPanel = new JPanel();
        _searchPanel.setBorder(new TitledBorder("Search "));
        int widths = 240;
        int heights = 190;
        _searchPanel.setPreferredSize(new Dimension(widths, heights));
        _searchPanel.setMaximumSize(new Dimension(widths, heights));
        _searchPanel.setMinimumSize(new Dimension(widths, heights));
        _labelTitolo = new JLabel("Title");
        _labelCategoria = new JLabel("Category");
        _textTitolo = new JTextField();
        String[] categories = { "-------------------", UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.DVD), UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.LIBRO), UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.FUMETTO), UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.QUOTIDIANO), UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.RIVISTAMENSILE), UtilityConversion.getCategoryStringFromEnum(ArticoliEnum.RIVISTASETTIMANALE) };
        _categoriaCombo = new JComboBox(categories);
        _buttonShow = new JButton("Show Search");
        _buttonShow.setToolTipText("Show selected catalog");
        _buttonShowAll = new JButton("Show All Catalog");
        _buttonShowAll.setToolTipText("Show all catalog");
        GroupLayout searchLayout = new javax.swing.GroupLayout(_searchPanel);
        _searchPanel.setLayout(searchLayout);
        searchLayout.setHorizontalGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(searchLayout.createSequentialGroup().addContainerGap().addGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_labelTitolo).addComponent(_labelCategoria)).addGap(10).addGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(_textTitolo).addComponent(_categoriaCombo).addComponent(_buttonShow, 150, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(_buttonShowAll, 150, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10)));
        searchLayout.setVerticalGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(searchLayout.createSequentialGroup().addContainerGap().addGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelTitolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(_textTitolo)).addGap(10).addGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(_categoriaCombo)).addGap(15).addComponent(_buttonShow).addGap(10).addComponent(_buttonShowAll).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        _orderPanel = new JPanel();
        _orderPanel.setBorder(new TitledBorder("Order by "));
        int widtho = 240;
        int heighto = 90;
        _orderPanel.setPreferredSize(new Dimension(widtho, heighto));
        _orderPanel.setMaximumSize(new Dimension(widtho, heighto));
        _orderPanel.setMinimumSize(new Dimension(widtho, heighto));
        _labelOrder = new JLabel("Order by");
        String[] col = { "---------------", "Title", "Category", "Price" };
        _columnCombo = new JComboBox(col);
        GroupLayout orderLayout = new javax.swing.GroupLayout(_orderPanel);
        _orderPanel.setLayout(orderLayout);
        orderLayout.setHorizontalGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(orderLayout.createSequentialGroup().addContainerGap().addComponent(_labelOrder).addGap(10).addComponent(_columnCombo).addGap(10, 10, 10)));
        orderLayout.setVerticalGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(orderLayout.createSequentialGroup().addContainerGap().addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10).addComponent(_columnCombo)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        _buttonPanel = new JPanel();
        _buttonPanel.setBorder(new TitledBorder("Options "));
        int widthb = 240;
        int heightb = 120;
        _buttonPanel.setPreferredSize(new Dimension(widthb, heightb));
        _buttonPanel.setMaximumSize(new Dimension(widthb, heightb));
        _buttonPanel.setMinimumSize(new Dimension(widthb, heightb));
        _buttonDisconnect = new JButton("Disconnect");
        _buttonDisconnect.setToolTipText("Disconnect from Edicola");
        _buttonNewsletter = new JButton("Newsletter");
        _buttonNewsletter.setToolTipText("Subscribe / Unsubscribe Newsletter");
        GroupLayout buttonLayout = new javax.swing.GroupLayout(_buttonPanel);
        _buttonPanel.setLayout(buttonLayout);
        buttonLayout.setHorizontalGroup(buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(buttonLayout.createSequentialGroup().addContainerGap().addGap(35, 35, 35).addGroup(buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_buttonNewsletter, 160, 160, 160).addComponent(_buttonDisconnect, 160, 160, 160)).addGap(35, 35, 35).addContainerGap()));
        buttonLayout.setVerticalGroup(buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(buttonLayout.createSequentialGroup().addContainerGap().addComponent(_buttonNewsletter).addGap(10).addComponent(_buttonDisconnect).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new GroupLayout(_leftPanel);
        _leftPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_searchPanel).addGap(5).addComponent(_orderPanel).addGap(5).addComponent(_buttonPanel))));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(_searchPanel).addGap(5).addComponent(_orderPanel).addGap(5).addComponent(_buttonPanel)));
    }

    /**
	 * Inizializza il pannello centrale
	 */
    private void initializeCenterPanel() {
        _centerPanel = new JPanel();
        _container.add(_centerPanel, BorderLayout.EAST);
        _centerPanel.setBorder(new TitledBorder("Catalog "));
        int widthb = 600;
        int heightb = 350;
        _centerPanel.setPreferredSize(new Dimension(widthb, heightb));
        _centerPanel.setMaximumSize(new Dimension(widthb, heightb));
        _centerPanel.setMinimumSize(new Dimension(widthb, heightb));
        _table = new JTable(new CatalogTableModel());
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _table.setShowGrid(true);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _table.getColumnModel().getColumn(0).setPreferredWidth(180);
        _table.getColumnModel().getColumn(1).setPreferredWidth(172);
        _table.getColumnModel().getColumn(2).setPreferredWidth(120);
        _table.getColumnModel().getColumn(3).setPreferredWidth(80);
        _catalogScrollPane = new JScrollPane(_table);
        _catalogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        _catalogScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        _catalogScrollPane.setPreferredSize(new Dimension(570, 370));
        _centerPanel.add(_catalogScrollPane);
        _table.addMouseListener(new TableCatalogoClickListener());
    }

    @Override
    public void setController(UIController controller, boolean subscribeViewToModelsEvents) {
        if (controller == null) return;
        super.setController(controller, subscribeViewToModelsEvents);
        _buttonShow.setActionCommand("" + TypeActionGui.BUTTON_SHOW);
        _buttonShow.addActionListener((EdicolaViewController) getController());
        _buttonShowAll.setActionCommand("" + TypeActionGui.BUTTON_SHOWALL);
        _buttonShowAll.addActionListener((EdicolaViewController) getController());
        _buttonDisconnect.setActionCommand("" + TypeActionGui.BUTTON_DISCONNECT);
        _buttonDisconnect.addActionListener((EdicolaViewController) getController());
        _buttonNewsletter.setActionCommand("" + TypeActionGui.BUTTON_NEWSLETTER);
        _buttonNewsletter.addActionListener((EdicolaViewController) getController());
        _columnCombo.setActionCommand("" + TypeActionGui.SELECT_SORT_TYPE);
        _columnCombo.addActionListener((EdicolaViewController) getController());
        _thisFrame.addWindowListener((EdicolaViewController) getController());
    }

    /**
	 * Evento tramite il quale la vista viene notificata del cambiamento del model. 
	 * Devono essere gestiti tutti i diversi comportamenti da tenere a seconda del cambiamento
	 * @param event � l'evento scatenato dal modello
	 */
    public void notifyModelEvents(EdicolaModelEvent event) {
        TypeActionModel typeAction = event.getTypeAction();
        switch(typeAction) {
            case SORT_CATALOG:
                {
                    sortCatalog();
                    break;
                }
            case DISCONNECT:
                {
                    disconnect();
                    break;
                }
            case SHOW:
                {
                    showSearch(event.getCatalog());
                    break;
                }
            case SHOW_ALL:
                {
                    showCatalog(event.getCatalog());
                    break;
                }
            case ADD_INFO_MESSAGE:
                {
                    showMessage(event.getMessage());
                    break;
                }
            case SHOW_NEWSLETTER:
                {
                    showNewsletter(event.getMessage(), event.getMessage1());
                    break;
                }
            default:
                break;
        }
    }

    /**
	 * Ordina gli articoli del ctaalogo in base al tipo di ordinamento richiesto
	 */
    private void sortCatalog() {
        String type = getSelectedSortType();
        SortType sortType;
        if (type.equalsIgnoreCase(SortType.CATEGORY.toString())) sortType = SortType.CATEGORY; else if (type.equalsIgnoreCase(SortType.PRICE.toString())) sortType = SortType.PRICE; else sortType = SortType.TITLE;
        ((CatalogTableModel) _table.getModel()).sortRows(sortType);
    }

    /**
	 * Chiude la finestra visualizzata
	 */
    private void disconnect() {
        _mainFrame.setEnabled(true);
        _thisFrame.setVisible(false);
    }

    /**
	 * Visualizza il catalogo o una sua parte in seguito ad una ricerca
	 * @param collection � la collezione di articoli da visualizzare
	 */
    private void showSearch(Vector<Articolo> collection) {
        showCatalog(collection);
        _textTitolo.setText("");
        _categoriaCombo.setSelectedIndex(0);
    }

    /**
	 * Visualizza il catalogo complessivo
	 * @param collection � la collezione di articoli da visualizzare
	 */
    private void showCatalog(Vector<Articolo> collection) {
        String sortType = _columnCombo.getSelectedItem().toString();
        if (sortType.equals("")) setCatalog(collection); else {
            if (sortType.equalsIgnoreCase(SortType.CATEGORY.toString())) ((CatalogTableModel) _table.getModel()).setCollection(collection, SortType.CATEGORY); else if (sortType.equalsIgnoreCase(SortType.PRICE.toString())) ((CatalogTableModel) _table.getModel()).setCollection(collection, SortType.PRICE); else ((CatalogTableModel) _table.getModel()).setCollection(collection, SortType.TITLE);
        }
    }

    /**
	 * Visualizza un messaggio a video mediante un JOptionPane
	 * @param message � il messaggio da visualizzare
	 */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(_thisFrame, message, "Operation Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * Visualizza il contenuto della newsletter
	 * @param message � il contenuto della newsletter
	 */
    private void showNewsletter(String edicolaName, String newsletter) {
        NewsletterViewer viewer = new NewsletterViewer(new JFrame(), edicolaName + " - Newsletter");
        viewer.setNewsletterText(newsletter);
        viewer.setVisible(true);
    }

    /**
	 * imposta il catalogo nella tabella
	 */
    public void setCatalog(Vector<Articolo> collection) {
        ((CatalogTableModel) _table.getModel()).setCollection(collection);
    }

    /**
	 * Restituisce il tipo di ordinamento selezionato per visualizzare il catalogo
	 * @return String � il tipo di ordinamento richiesto
	 */
    public String getSelectedSortType() {
        return _columnCombo.getSelectedItem().toString();
    }

    /**
	 * Imposta il frame della finestra principale
	 */
    public void setMainFrame(JFrame frame) {
        _mainFrame = frame;
    }

    /**
	 * Restituisce il titolo (o una parte) dell'articolo da cercare
	 * @return String � la stringa che rappresenta il titolo (o una parte) dell'articolo 
	 */
    public String getTitle() {
        return _textTitolo.getText();
    }

    /**
	 * Restituisce la categoria dell'articolo da cercare
	 * @return String � la stringa che rappresenta la categoria dell'articolo 
	 */
    public String getCategory() {
        return _categoriaCombo.getSelectedItem().toString();
    }

    /**
	 * Imposta l'edicola con cui si � connessi
	 */
    public void setEdicola(Edicola edicola) {
        _edicola = edicola;
    }

    /**
	 * Restituisce l'edicola con cui si � connessi
	 * @return Edicola � l'edicola con cui si � connessi
	 */
    public Edicola getEdicola() {
        return _edicola;
    }
}
