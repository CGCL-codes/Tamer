package net.narusas.cafelibrary.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookListener;
import net.narusas.cafelibrary.Borrower;
import net.narusas.cafelibrary.apps.IconHolder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class BookEditPanel extends javax.swing.JPanel implements BookListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3728909642969558407L;

    private Book book;

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    private JButton publishDateButton;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel favoriteLabel;

    private JLabel jLabel11;

    private JButton purchaseDateLabel;

    private JLabel jLabel9;

    private JLabel jLabel8;

    private JTextField originalPriceField;

    private JLabel jLabel7;

    private JTextArea translatorTextArea;

    private JTextField isbnField;

    private JTextArea categoryTextArea;

    private JLabel jLabel2;

    private JTextArea publisherTextArea;

    private JLabel jLabel1;

    private JTextArea authorTextArea;

    private JTextArea titleTextArea;

    private JLabel coverLabel;

    private JLabel borrowerLabel;

    private JLabel jLabel14;

    private JTextArea notesTextArea;

    private JLabel jLabel13;

    private JTextArea descriptionTextArea;

    private JLabel jLabel12;

    private JPanel mainPanel;

    private JScrollPane contentScrollPane;

    private BookEditController controller;

    /**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
    public static void main(String[] args) {
        Book book = new Book(315, "ABC");
        book.setFavorite(3);
        book.setAuthor("Author");
        book.setCategory("�Ҽ�");
        book.setDescription("���� �Ҽ��Դϴ�");
        book.setIsbn("100101010");
        book.setNotes("Notes");
        book.setOriginalPrice("1000��");
        book.setPublisher("���ǻ�");
        book.setBorrower(new Borrower("�뿩��", null));
        book.setTranslator("����");
        book.setPublishDate(new Date());
        book.setPurchaseDate(new Date());
        BookEditPanel bp = new BookEditPanel();
        bp.setBook(book);
        JFrame frame = new JFrame();
        frame.getContentPane().add(bp);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public BookEditPanel() {
        super();
        controller = new BookEditController(this);
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(290, 543));
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            this.setBackground(new java.awt.Color(255, 255, 255));
            this.setBorder(BorderFactory.createTitledBorder("�󼼳��� ����"));
            this.setSize(290, 543);
            {
                contentScrollPane = new JScrollPane();
                this.add(contentScrollPane, BorderLayout.CENTER);
                contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                {
                    mainPanel = new JPanel();
                    GridBagLayout mainPanelLayout = new GridBagLayout();
                    contentScrollPane.setViewportView(mainPanel);
                    mainPanel.setBackground(new java.awt.Color(255, 255, 255));
                    mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.1 };
                    mainPanelLayout.rowHeights = new int[] { 74, 31, 7, 20, 20, 20, 20, 20, 20, 7, 20, 20, 20, 20, 20, 7 };
                    mainPanelLayout.columnWeights = new double[] { 0.0, 0.0, 0.0 };
                    mainPanelLayout.columnWidths = new int[] { 37, 48, 92 };
                    mainPanel.setLayout(mainPanelLayout);
                    mainPanel.setPreferredSize(new java.awt.Dimension(259, 502));
                    {
                        coverLabel = new JLabel();
                        mainPanel.add(coverLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
                        coverLabel.setText("cover");
                        coverLabel.setPreferredSize(new Dimension(72, 100));
                        coverLabel.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                        coverLabel.setSize(72, 100);
                    }
                    {
                        titleTextArea = new JTextArea();
                        mainPanel.add(titleTextArea, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 0, 5), 0, 0));
                        titleTextArea.setText("Rapid Development");
                        titleTextArea.setFont(new java.awt.Font("����", 1, 14));
                        titleTextArea.setLineWrap(true);
                        titleTextArea.setSize(163, 30);
                        titleTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        titleTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.bookTitleChanged();
                            }
                        });
                    }
                    {
                        authorTextArea = new JTextArea();
                        mainPanel.add(authorTextArea, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
                        authorTextArea.setText("author ( Steve McCollel )");
                        authorTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        authorTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.bookAuthorChanged();
                            }
                        });
                    }
                    {
                        jLabel1 = new JLabel();
                        mainPanel.add(jLabel1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel1.setText("출판사");
                        jLabel1.setForeground(new java.awt.Color(192, 192, 192));
                        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    {
                        publisherTextArea = new JTextArea();
                        mainPanel.add(publisherTextArea, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        publisherTextArea.setText("출판사");
                        publisherTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        publisherTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.publisherChanged();
                            }
                        });
                    }
                    {
                        jLabel2 = new JLabel();
                        mainPanel.add(jLabel2, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel2.setText("출판일");
                        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel2.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        publishDateButton = new JButton();
                        mainPanel.add(publishDateButton, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        publishDateButton.setText("2002.12.12");
                        publishDateButton.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                controller.selectPublishDate();
                            }
                        });
                    }
                    {
                        jLabel3 = new JLabel();
                        mainPanel.add(jLabel3, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel3.setText("장르");
                        jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel3.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        categoryTextArea = new JTextArea();
                        mainPanel.add(categoryTextArea, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        categoryTextArea.setText("<html>프로그래밍<br>개발");
                        categoryTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        categoryTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.categoryChanged();
                            }
                        });
                    }
                    {
                        jLabel4 = new JLabel();
                        mainPanel.add(jLabel4, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel4.setText("ISBN");
                        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel4.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        isbnField = new JTextField();
                        mainPanel.add(isbnField, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        isbnField.setText("89-527-2105-5");
                        isbnField.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                controller.isbnChanged();
                            }
                        });
                    }
                    {
                        jLabel5 = new JLabel();
                        mainPanel.add(jLabel5, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel5.setText("번역자");
                        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel5.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        translatorTextArea = new JTextArea();
                        mainPanel.add(translatorTextArea, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        translatorTextArea.setText("<html>프로그래밍<br>개발");
                        translatorTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.translatorChanged();
                            }
                        });
                    }
                    {
                        jLabel7 = new JLabel();
                        mainPanel.add(jLabel7, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel7.setText("가격");
                        jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel7.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        originalPriceField = new JTextField();
                        mainPanel.add(originalPriceField, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        originalPriceField.setText("10,000원");
                        originalPriceField.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                controller.originalPriceChanged();
                            }
                        });
                    }
                    {
                        jLabel8 = new JLabel();
                        mainPanel.add(jLabel8, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                        jLabel8.setText(" ");
                    }
                    {
                        jLabel9 = new JLabel();
                        mainPanel.add(jLabel9, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel9.setText("구매일");
                        jLabel9.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel9.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        purchaseDateLabel = new JButton();
                        mainPanel.add(purchaseDateLabel, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        purchaseDateLabel.setText("2002.12.12");
                        purchaseDateLabel.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                controller.selectPurchaseDate();
                            }
                        });
                    }
                    {
                        jLabel11 = new JLabel();
                        mainPanel.add(jLabel11, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel11.setText("선호도");
                        jLabel11.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel11.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        favoriteLabel = new JLabel();
                        mainPanel.add(favoriteLabel, new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        favoriteLabel.addMouseListener(new MouseAdapter() {

                            public void mouseClicked(MouseEvent e) {
                                controller.selectFavorite(e);
                            }
                        });
                        favoriteLabel.addMouseMotionListener(new MouseMotionAdapter() {

                            public void mouseDragged(MouseEvent e) {
                                controller.selectFavorite(e);
                            }
                        });
                    }
                    {
                        jLabel12 = new JLabel();
                        mainPanel.add(jLabel12, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel12.setText("설명");
                        jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel12.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        descriptionTextArea = new JTextArea();
                        mainPanel.add(descriptionTextArea, new GridBagConstraints(0, 13, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
                        descriptionTextArea.setText("notesTextArea");
                        descriptionTextArea.setLineWrap(true);
                        descriptionTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        descriptionTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.descriptionChanged();
                            }
                        });
                    }
                    {
                        jLabel13 = new JLabel();
                        mainPanel.add(jLabel13, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel13.setText("노트");
                        jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel13.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        notesTextArea = new JTextArea();
                        mainPanel.add(notesTextArea, new GridBagConstraints(0, 15, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
                        notesTextArea.setText("notesTextArea");
                        notesTextArea.setLineWrap(true);
                        notesTextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        notesTextArea.addFocusListener(new FocusAdapter() {

                            public void focusLost(FocusEvent e) {
                                controller.notesChanged();
                            }
                        });
                    }
                    {
                        jLabel14 = new JLabel();
                        mainPanel.add(jLabel14, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel14.setText("대여자");
                        jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel14.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        borrowerLabel = new JLabel();
                        mainPanel.add(borrowerLabel, new GridBagConstraints(2, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        borrowerLabel.setText("favorite");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBook(Book book) {
        if (this.book != null) {
            this.book.removeListener(this);
        }
        this.book = book;
        controller.setBook(book);
        if (book == null) {
            return;
        }
        this.book.addListener(this);
        setup(book);
    }

    private void setup(Book book) {
        getCoverLabel().setText("");
        getCoverLabel().setIcon(new ImageIcon("data/" + book.getId() + "S.jpg"));
        getCoverLabel().setPreferredSize(new Dimension(55, 70));
        getTitleTextArea().setText(book.getTitle());
        getAuthorTextArea().setText(book.getAuthor());
        getPublisherTextArea().setText(book.getPublisher());
        getPublishDateLabel().setText(toDateString(book.getPublishDate()));
        purchaseDateLabel.setText(toDateString(book.getPurchaseDate()));
        borrowerLabel.setText("borrower");
        categoryTextArea.setText(book.getCategory());
        translatorTextArea.setText(book.getTranslator());
        getIsbnField().setText(book.getIsbn());
        originalPriceField.setText(book.getOriginalPrice());
        getDescriptionTextArea().setText(book.getDescription());
        notesTextArea.setText(book.getNotes());
        getFavoriteLabel().setIcon(IconHolder.favorite[book.getFavorite()]);
    }

    private String toDateString(Date publishDate) {
        return publishDate == null ? "?" : formatter.format(publishDate);
    }

    public void bookChanged(Book book, String attrName, Object value) {
        setup(book);
    }

    public JButton getPublishDateLabel() {
        return publishDateButton;
    }

    public JLabel getFavoriteLabel() {
        return favoriteLabel;
    }

    public JTextField getOriginalPriceField() {
        return originalPriceField;
    }

    public JTextField getIsbnField() {
        return isbnField;
    }

    public JTextArea getCategoryTextArea() {
        return categoryTextArea;
    }

    public JTextArea getPublisherTextArea() {
        return publisherTextArea;
    }

    public JTextArea getAuthorTextArea() {
        return authorTextArea;
    }

    public JTextArea getTitleTextArea() {
        return titleTextArea;
    }

    public JLabel getCoverLabel() {
        return coverLabel;
    }

    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public JButton getPublishDateButton() {
        return publishDateButton;
    }

    public JButton getPurchaseDateLabel() {
        return purchaseDateLabel;
    }

    public JTextArea getTranslatorTextArea() {
        return translatorTextArea;
    }

    public JTextArea getNotesTextArea() {
        return notesTextArea;
    }
}

class BookEditController {

    DateChooser DATE_CHOOSER = new DateChooser((JFrame) null, "���� ����");

    private final BookEditPanel panel;

    private Book book;

    public BookEditController(BookEditPanel bookEditPanel) {
        this.panel = bookEditPanel;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void notesChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getNotesTextArea().getText();
        book.setNotes(text);
    }

    public void descriptionChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getDescriptionTextArea().getText();
        book.setDescription(text);
    }

    public void selectFavorite(MouseEvent e) {
        int x = e.getX();
        int favorite = book.getFavorite();
        if (x < 5) {
            favorite = 0;
        } else if (x < 20) {
            favorite = 1;
        } else if (x < 32) {
            favorite = 2;
        } else if (x < 44) {
            favorite = 3;
        } else if (x < 55) {
            favorite = 4;
        } else {
            favorite = 5;
        }
        book.setFavorite(favorite);
    }

    public void selectPurchaseDate() {
        if (book == null) {
            return;
        }
        Date d = book.getPurchaseDate();
        if (d == null) {
            d = new Date();
        }
        Date selected = DATE_CHOOSER.select(d);
        book.setPurchaseDate(selected);
    }

    public void originalPriceChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getOriginalPriceField().getText();
        book.setOriginalPrice(text);
    }

    public void translatorChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getTranslatorTextArea().getText();
        book.setTranslator(text);
    }

    public void isbnChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getIsbnField().getText();
        book.setIsbn(text);
    }

    public void categoryChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getCategoryTextArea().getText();
        book.setCategory(text);
    }

    public void selectPublishDate() {
        if (book == null) {
            return;
        }
        Date d = book.getPublishDate();
        if (d == null) {
            d = new Date();
        }
        Date selected = DATE_CHOOSER.select(d);
        book.setPublishDate(selected);
    }

    public void publisherChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getPublisherTextArea().getText();
        book.setPublisher(text);
    }

    public void bookAuthorChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getAuthorTextArea().getText();
        book.setAuthor(text);
    }

    public void bookTitleChanged() {
        if (book == null) {
            return;
        }
        String text = panel.getTitleTextArea().getText();
        book.setTitle(text);
    }
}
