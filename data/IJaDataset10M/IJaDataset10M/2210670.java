package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import catalog.Quotidiano;

/**
 * Finestra che rappresenta i contenuti della classe Quotidiano e consente di inserirli, visualizzarli 
 * e modificarli  
 */
@SuppressWarnings("serial")
public class QuotidianoWindow extends JDialog implements ActionListener {

    private JFrame _thisFrame;

    private Container _container;

    private JPanel _panel;

    private JPanel _panelData;

    private JLabel _labelTitle;

    private JLabel _labelDescription;

    private JLabel _labelPrice;

    private JLabel _labelType;

    private JLabel _labelNumber;

    private JLabel _labelYear;

    private JLabel _labelInsert;

    private JLabel _labelEuro;

    private JTextField _textTitle;

    private JTextField _textPrice;

    private JTextField _textYear;

    private JTextField _textNumber;

    private JTextField _textInsert;

    private JButton _buttonOk;

    private JButton _buttonCancel;

    private Quotidiano _quotidiano;

    private JRadioButton _radioLocal;

    private JRadioButton _radioNaz;

    private JScrollPane _descriptionScrollPane;

    private JTextPane _textPaneDescription;

    /**
	 * Costruttore con parametri
	 * @param frame � il frame della finestra
	 * @param title � il titolod ella finestra
	 */
    public QuotidianoWindow(JFrame frame, String title) {
        super(frame, title, true);
        _thisFrame = frame;
        initializeView();
    }

    /**
	 * inizializza la vista
	 */
    private void initializeView() {
        _container = _thisFrame.getContentPane();
        _container.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(About.class.getResource("image/newspaper.png")).getImage());
        int width = 400;
        int height = 430;
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setLocation(300, 200);
        this.pack();
        this.setResizable(false);
        _panel = new JPanel();
        this.add(_panel);
        _panelData = new JPanel();
        _panelData.setBorder(new TitledBorder("Quotidiano Info "));
        int widths = 370;
        int heights = 340;
        _panelData.setPreferredSize(new Dimension(widths, heights));
        _panelData.setMaximumSize(new Dimension(widths, heights));
        _panelData.setMinimumSize(new Dimension(widths, heights));
        _labelTitle = new JLabel("Title");
        _labelDescription = new JLabel("Description");
        _labelPrice = new JLabel("Price");
        _labelType = new JLabel("Type");
        _labelInsert = new JLabel("Insert");
        _labelNumber = new JLabel("Number");
        _labelYear = new JLabel("Year");
        _labelEuro = new JLabel("�");
        _textTitle = new JTextField();
        _textPrice = new JTextField();
        _textInsert = new JTextField();
        _textNumber = new JTextField();
        _textYear = new JTextField();
        _radioLocal = new JRadioButton("Local");
        _radioLocal.addActionListener(this);
        _radioLocal.setActionCommand("radioLoc");
        _radioNaz = new JRadioButton("National");
        _radioNaz.setSelected(true);
        _radioNaz.addActionListener(this);
        _radioNaz.setActionCommand("radioNaz");
        _descriptionScrollPane = new JScrollPane();
        _descriptionScrollPane.setBounds(400, 12, 286, 149);
        _textPaneDescription = new JTextPane();
        _descriptionScrollPane.setViewportView(_textPaneDescription);
        _descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        _descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        int dimTextBox = 70;
        GroupLayout panelDataLayout = new javax.swing.GroupLayout(_panelData);
        _panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelDataLayout.createSequentialGroup().addContainerGap().addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_labelTitle).addComponent(_labelDescription).addComponent(_labelInsert).addComponent(_labelType).addComponent(_labelNumber).addComponent(_labelYear).addComponent(_labelPrice)).addGap(20).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_textTitle).addComponent(_descriptionScrollPane).addComponent(_textInsert).addGroup(panelDataLayout.createSequentialGroup().addComponent(_radioLocal).addGap(20).addComponent(_radioNaz).addGap(20)).addComponent(_textNumber, dimTextBox, dimTextBox, dimTextBox).addComponent(_textYear, dimTextBox, dimTextBox, dimTextBox).addGroup(panelDataLayout.createSequentialGroup().addComponent(_textPrice, dimTextBox, dimTextBox, dimTextBox).addGap(10).addComponent(_labelEuro))).addContainerGap()));
        panelDataLayout.setVerticalGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelDataLayout.createSequentialGroup().addContainerGap().addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelTitle).addGap(15).addComponent(_textTitle)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelDescription).addComponent(_descriptionScrollPane, 70, 70, 70)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelInsert).addComponent(_textInsert)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelType).addComponent(_radioLocal).addComponent(_radioNaz)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelNumber).addComponent(_textNumber)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelYear).addComponent(_textYear)).addGap(15).addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(_labelPrice).addComponent(_textPrice).addComponent(_labelEuro)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        _buttonOk = new JButton("OK");
        _buttonOk.addActionListener(this);
        _buttonCancel = new JButton("Cancel");
        _buttonCancel.addActionListener(this);
        javax.swing.GroupLayout layout = new GroupLayout(_panel);
        _panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(_panelData).addGap(10).addGroup(layout.createSequentialGroup().addComponent(_buttonOk, 80, 80, 80).addGap(15).addComponent(_buttonCancel, 80, 80, 80).addGap(3))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(_panelData).addGap(10).addGroup(layout.createParallelGroup().addComponent(_buttonOk).addGap(10).addComponent(_buttonCancel)).addContainerGap()));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        this.setVisible(false);
        return;
    }

    /**
	 * Visualizza le caratteristiche del quotidiano da modificare
	 * @param quotidiano � l'oggetto da modificare
	 */
    public void setQuotidiano(Quotidiano quotidiano) {
        _quotidiano = quotidiano;
        _textTitle.setText(_quotidiano.getTitle());
        _textTitle.setCaretPosition(0);
        _textPaneDescription.setText(_quotidiano.getDescription());
        _textPaneDescription.setCaretPosition(0);
        _textInsert.setText(_quotidiano.getInsert());
        _textYear.setText("" + _quotidiano.getYear());
        _textNumber.setText("" + _quotidiano.getNumber());
        _textPrice.setText("" + _quotidiano.getPrice());
        _radioLocal.setSelected(_quotidiano.isLocale());
        _radioNaz.setSelected(!_quotidiano.isLocale());
    }

    /**
	 * Imposta i campi come readOnly
	 */
    public void setReadOnly() {
        _textInsert.setEditable(false);
        _textNumber.setEditable(false);
        _textPrice.setEditable(false);
        _textTitle.setEditable(false);
        _textYear.setEditable(false);
        _textPaneDescription.setEditable(false);
        _radioLocal.setEnabled(false);
        _radioNaz.setEnabled(false);
    }
}
