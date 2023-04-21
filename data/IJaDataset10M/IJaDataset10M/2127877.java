package net.narusas.cafelibrary.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
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
public class BookDetailPanel extends javax.swing.JPanel implements BookListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3728909642969558407L;

    private Book book;

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    private JLabel publishDateLabel;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel favoriteLabel;

    private JLabel jLabel11;

    private JLabel purchaseDateLabel;

    private JLabel jLabel9;

    private JLabel jLabel8;

    private JLabel originalPriceLabel;

    private JLabel jLabel7;

    private JLabel translatorLabel;

    private JLabel isbnLabel;

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
        BookDetailPanel bp = new BookDetailPanel();
        bp.setBook(book);
        JFrame frame = new JFrame();
        frame.getContentPane().add(bp);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public BookDetailPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(290, 543));
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            this.setBorder(BorderFactory.createTitledBorder("�󼼳���"));
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
                    mainPanel.setAutoscrolls(true);
                    {
                        coverLabel = new JLabel();
                        mainPanel.add(coverLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
                        coverLabel.setText("cover");
                        coverLabel.setPreferredSize(new Dimension(80, 110));
                        coverLabel.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                        coverLabel.setSize(80, 110);
                    }
                    {
                        titleTextArea = new JTextArea();
                        mainPanel.add(titleTextArea, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 0, 5), 0, 0));
                        titleTextArea.setText("Rapid Development");
                        titleTextArea.setFont(new java.awt.Font("����", 1, 14));
                        titleTextArea.setLineWrap(true);
                        titleTextArea.setSize(163, 30);
                        titleTextArea.setEditable(false);
                    }
                    {
                        authorTextArea = new JTextArea();
                        mainPanel.add(authorTextArea, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
                        authorTextArea.setText("author ( Steve McCollel )");
                        authorTextArea.setEditable(false);
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
                        publisherTextArea.setEditable(false);
                        publisherTextArea.setLineWrap(true);
                    }
                    {
                        jLabel2 = new JLabel();
                        mainPanel.add(jLabel2, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel2.setText("출판일");
                        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel2.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        publishDateLabel = new JLabel();
                        mainPanel.add(publishDateLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        publishDateLabel.setText("2002.12.12");
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
                    }
                    {
                        jLabel4 = new JLabel();
                        mainPanel.add(jLabel4, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel4.setText("ISBN");
                        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel4.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        isbnLabel = new JLabel();
                        mainPanel.add(isbnLabel, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        isbnLabel.setText("89-527-2105-5");
                    }
                    {
                        jLabel5 = new JLabel();
                        mainPanel.add(jLabel5, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel5.setText("번역자");
                        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel5.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        translatorLabel = new JLabel();
                        mainPanel.add(translatorLabel, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        translatorLabel.setText("<html>프로그래밍<br>개발");
                    }
                    {
                        jLabel7 = new JLabel();
                        mainPanel.add(jLabel7, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
                        jLabel7.setText("가격");
                        jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
                        jLabel7.setForeground(new java.awt.Color(192, 192, 192));
                    }
                    {
                        originalPriceLabel = new JLabel();
                        mainPanel.add(originalPriceLabel, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        originalPriceLabel.setText("10,000원");
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
                        purchaseDateLabel = new JLabel();
                        mainPanel.add(purchaseDateLabel, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                        purchaseDateLabel.setText("2002.12.12");
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
                        descriptionTextArea.setEditable(false);
                        descriptionTextArea.setLineWrap(true);
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
                        notesTextArea.setEditable(false);
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
        clear();
        this.book = book;
        if (book == null) {
            return;
        }
        this.book.addListener(this);
        setup(book);
    }

    private void clear() {
        getCoverLabel().setText("");
        getCoverLabel().setIcon(null);
        getTitleTextArea().setText(null);
        getAuthorTextArea().setText(null);
        getPublisherTextArea().setText(null);
        getPublishDateLabel().setText(null);
        purchaseDateLabel.setText(null);
        borrowerLabel.setText(null);
        categoryTextArea.setText(null);
        translatorLabel.setText(null);
        getIsbnLabel().setText(null);
        originalPriceLabel.setText(null);
        getDescriptionTextArea().setText(null);
        notesTextArea.setText(null);
        getFavoriteLabel().setIcon(null);
    }

    private void setup(Book book) {
        getCoverLabel().setText("");
        ImageIcon icon = new ImageIcon("data/" + book.getId() + "S.jpg");
        getCoverLabel().setIcon(icon);
        if (icon.getIconWidth() == -1 || icon.getIconHeight() == -1) {
            getCoverLabel().setPreferredSize(new Dimension(72, 100));
            getCoverLabel().setText("No cover");
            getCoverLabel().setHorizontalAlignment(JLabel.CENTER);
        } else {
            getCoverLabel().setPreferredSize(new Dimension(icon.getIconWidth() + 2, icon.getIconHeight() + 2));
        }
        getTitleTextArea().setText(book.getTitle());
        getAuthorTextArea().setText(book.getAuthor());
        getPublisherTextArea().setText(book.getPublisher());
        getPublishDateLabel().setText(toDateString(book.getPublishDate()));
        purchaseDateLabel.setText(toDateString(book.getPurchaseDate()));
        borrowerLabel.setText(book.getBorrower() == null ? null : book.getBorrower().toString());
        categoryTextArea.setText(book.getCategory());
        translatorLabel.setText(book.getTranslator());
        getIsbnLabel().setText(book.getIsbn());
        originalPriceLabel.setText(book.getOriginalPrice());
        getDescriptionTextArea().setText(book.getDescription());
        notesTextArea.setText(book.getNotes());
        getFavoriteLabel().setIcon(IconHolder.favorite[book.getFavorite()]);
    }

    private String toDateString(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }

    public void bookChanged(Book book, String attrName, Object value) {
        setup(book);
    }

    public JLabel getPublishDateLabel() {
        return publishDateLabel;
    }

    public JLabel getFavoriteLabel() {
        return favoriteLabel;
    }

    public JLabel getOriginalPriceLabel() {
        return originalPriceLabel;
    }

    public JLabel getIsbnLabel() {
        return isbnLabel;
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
}
