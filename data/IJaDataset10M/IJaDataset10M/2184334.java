package org.dvdcatalog.dvdc.ui.wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import org.dvdcatalog.dvdc.debug.Log;
import org.dvdcatalog.dvdc.objects.DVD;
import org.dvdcatalog.dvdc.ui.wizard.imdb.ImdbInfo;
import org.dvdcatalog.dvdc.ui.wizard.imdb.ImdbSearch;
import org.dvdcatalog.dvdc.ui.wizard.imdb.Downloader;
import org.dvdcatalog.dvdc.ui.ScreenManager;
import org.dvdcatalog.dvdc.Settings;

/**
 *  This is the Wizard Windos. It uses a CardLayout to change the conentents
 *  within the window.
 *
 * @author     lars
 * @created    November 21, 2004
 */
public class WizardFrame extends JFrame implements ActionListener {

    private ScreenManager sm;

    private ImdbInfo info;

    private JButton backButton = new JButton("< Back");

    private JButton nextButton = new JButton("Next >");

    private JButton skipButton = new JButton("Skip >>");

    private JButton cancelButton = new JButton("Cancel");

    private JTextField imdbAddress;

    private JTextField imdbSearch;

    private JLabel l1;

    private JLabel l2;

    private JLabel l3;

    private JLabel l4;

    private JTextArea textArea5;

    private JLabel l6;

    private JPanel cards;

    private int cardNr = 1;

    private ImdbInfo[] imdbAddresses;

    private JList list;

    private JLabel poster;

    private BufferedImage posterAsBI;

    private JProgressBar bar;

    /**
	 *  Constructor for the WizardFrame object
	 *
	 * @param  sm  Description of the Parameter
	 */
    public WizardFrame(ScreenManager sm) {
        super("IMDb Wizard");
        this.sm = sm;
        Component contents = createComponents();
        getContentPane().add(contents, BorderLayout.CENTER);
    }

    /**
	 * @return    the pane that holds the components
	 */
    private Component createComponents() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.add(createInputComponents());
        pane.add(new JSeparator());
        pane.add(createButtons());
        return pane;
    }

    /**
	 *  As seen below. Makes a CardLayout and the diffrent cards are added.
	 *
	 * @return    the pane that holds the components
	 */
    private Component createInputComponents() {
        cards = new JPanel(new CardLayout());
        cards.add(createCard1(), "INPUT");
        cards.add(createCard2(), "SEARCHING");
        cards.add(createCard3(), "LIST");
        cards.add(createCard4(), "RETRIEVING");
        cards.add(createCard5(), "INFO");
        return cards;
    }

    /**
	 *  Card nr 1. This card is for entering the movie to search for.
	 *
	 * @return    a JPanel which will be card nr 1
	 */
    private Component createCard1() {
        JPanel p = new JPanel();
        p.setAlignmentX(0.5f);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel l = new JLabel("Movie Title: ");
        imdbSearch = new JTextField(20);
        imdbSearch.addActionListener(this);
        p.add(l);
        p.add(imdbSearch);
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(createTopTitle("Enter a movietitle to search for", "Step 1 of 5"));
        toReturn.add(p);
        return toReturn;
    }

    /**
	 *  Card nr 2. This card will show a progress of the search.
	 *
	 * @return    a JPanel which will be card nr 2
	 */
    private Component createCard2() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setAlignmentX(0.5f);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel l = new JLabel("Searching");
        l.setAlignmentX(0.5f);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setAlignmentX(0.5f);
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        p.add(bar);
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(createTopTitle("Please wait while searching IMDb", "Step 2 of 5"));
        toReturn.add(p);
        return toReturn;
    }

    private Component createCard3() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setAlignmentX(0.5f);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel l = new JLabel("Select a movie from the list below");
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) actionPerformed(new ActionEvent(list, 0, null));
            }
        });
        list.addMouseListener(new MouseAdapter() {

            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) actionPerformed(new ActionEvent(list, 0, null));
            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        scrollPane.setMinimumSize(new Dimension(0, 50));
        scrollPane.setPreferredSize(new Dimension(0, 100));
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        p.add(scrollPane);
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(createTopTitle("Select your movie", "Step 3 of 5"));
        toReturn.add(p);
        return toReturn;
    }

    /**
	 *  Card nr 4. This card will show a progress of the downloading.
	 *
	 * @return    a JPanel which will be card nr 4
	 */
    private Component createCard4() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setAlignmentX(0.5f);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel l = new JLabel("Retrieving info");
        bar = new JProgressBar(0, 100);
        bar.setIndeterminate(false);
        bar.setString("0%");
        bar.setStringPainted(true);
        l.setAlignmentX(0.5f);
        bar.setAlignmentX(0.5f);
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        p.add(bar);
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(createTopTitle("Retrieving information about your movie", "Step 4 of 5"));
        toReturn.add(p);
        return toReturn;
    }

    /**
	 *  This will show what it got.
	 *
	 * @return    a JPanel which will be card nr 5
	 */
    private Component createCard5() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setAlignmentX(0.5f);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        right.setAlignmentX(0.5f);
        right.setAlignmentY(Component.TOP_ALIGNMENT);
        right.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        l1 = new JLabel();
        right.add(l1);
        right.add(Box.createRigidArea(new Dimension(0, 10)));
        l2 = new JLabel("Directed by ");
        right.add(l2);
        l3 = new JLabel("Genre is ");
        right.add(l3);
        l6 = new JLabel("Ratings are ");
        right.add(l6);
        l4 = new JLabel("Tagline: ");
        right.add(l4);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
        p2.setAlignmentX(Component.LEFT_ALIGNMENT);
        poster = new JLabel();
        poster.setPreferredSize(new Dimension(100, 140));
        poster.setBorder(BorderFactory.createLineBorder(Color.black));
        poster.setAlignmentY(Component.TOP_ALIGNMENT);
        p2.add(poster);
        p2.add(right);
        textArea5 = new JTextArea();
        textArea5.setLineWrap(true);
        textArea5.setWrapStyleWord(true);
        textArea5.setEditable(false);
        textArea5.setRows(3);
        JScrollPane scrollPane = new JScrollPane(textArea5);
        p.add(p2);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(scrollPane);
        p.add(Box.createVerticalGlue());
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(createTopTitle("Was this the right movie?", "Step 5 of 5"));
        toReturn.add(p);
        return toReturn;
    }

    private JPanel createTopTitle(String text1, String text2) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setBackground(Color.WHITE);
        JPanel higher = new JPanel();
        higher.setLayout(new BoxLayout(higher, BoxLayout.LINE_AXIS));
        higher.setBackground(Color.WHITE);
        JLabel title = new JLabel(text1);
        higher.add(title);
        higher.add(Box.createHorizontalGlue());
        JPanel lower = new JPanel();
        lower.setLayout(new BoxLayout(lower, BoxLayout.LINE_AXIS));
        lower.setBackground(Color.WHITE);
        JLabel step = new JLabel(text2);
        lower.add(Box.createHorizontalGlue());
        lower.add(step);
        p.add(higher);
        p.add(lower);
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.PAGE_AXIS));
        toReturn.add(p);
        toReturn.add(new JSeparator());
        toReturn.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
        return toReturn;
    }

    /**
	 *  Creates the buttons at the buttom.
	 *
	 * @return    a JPanel with buttons
	 */
    private Component createButtons() {
        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPane.setLayout(new FlowLayout());
        backButton.addActionListener(this);
        backButton.setEnabled(false);
        buttonPane.add(backButton);
        nextButton.addActionListener(this);
        buttonPane.add(nextButton);
        skipButton.addActionListener(this);
        buttonPane.add(skipButton);
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);
        return buttonPane;
    }

    /**
	 *  Handles the actions issued by the buttons.
	 *
	 * @param  e
	 */
    public void actionPerformed(ActionEvent e) {
        final Object src = e.getSource();
        JButton b = null;
        if (src instanceof JButton) b = (JButton) src;
        CardLayout cl = (CardLayout) (cards.getLayout());
        if (b == backButton) {
            if (cardNr == 3) {
                cl.show(cards, "INPUT");
                cardNr = 1;
                backButton.setEnabled(false);
            }
            if (cardNr == 5) {
                cl.show(cards, "LIST");
                cardNr = 3;
            }
        } else if (b == nextButton || src == imdbSearch || src == list) {
            cl.next(cards);
            cardNr++;
            if (cardNr == 2) {
                if (imdbSearch.getText().equals("")) {
                    cl.previous(cards);
                    cardNr--;
                    JOptionPane.showMessageDialog(this, "You need to enter a movie to search for!");
                } else {
                    nextButton.setEnabled(false);
                    backButton.setEnabled(false);
                    ImdbSearch searchInfo = new ImdbSearch(imdbSearch.getText(), this);
                    Downloader d = new Downloader(searchInfo);
                    d.start();
                }
            } else if (cardNr == 4) {
                if (list.getSelectedIndex() == -1) {
                    cl.previous(cards);
                    cardNr--;
                    JOptionPane.showMessageDialog(this, "You need to select a movie!");
                } else {
                    nextButton.setEnabled(false);
                    backButton.setEnabled(false);
                    info = new ImdbInfo(imdbAddresses[list.getSelectedIndex()].getAddress(), this);
                    Downloader d = new Downloader(info, bar);
                    d.start();
                }
            } else if (cardNr == 6) {
                Log.print(3, "Creating a DVD to send to the Edit Window");
                DVD dvd = new DVD();
                dvd.setValueFor("Title", info.getTitle());
                dvd.setValueFor("Director", info.getDirector());
                dvd.setValueFor("Genre", info.getGenre());
                dvd.setValueFor("Year", info.getYear());
                dvd.setValueFor("Rating", info.getRating());
                dvd.setValueFor("Tagline", info.getTagline());
                dvd.setValueFor("Plot", info.getPlot());
                dvd.setFromWizard(true);
                dvd.setPoster(posterAsBI);
                sm.openEditWindow(dvd);
                setVisible(false);
                dispose();
            }
        } else if (b == skipButton) {
            sm.openEditWindow(null);
            setVisible(false);
            dispose();
        } else if (b == cancelButton) {
            setVisible(false);
            dispose();
            sm.enableMainWindow();
        }
    }

    /**
	 *  This message is called when the fetching of data is done.
	 */
    public void nextPage(ImdbInfo[] imdbAddresses) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.next(cards);
        cardNr++;
        String[] moviesFound = new String[imdbAddresses.length];
        for (int i = 0; i < imdbAddresses.length; i++) {
            moviesFound[i] = imdbAddresses[i].getTitle();
        }
        list.setListData(moviesFound);
        this.imdbAddresses = imdbAddresses;
        backButton.setEnabled(true);
        nextButton.setEnabled(true);
    }

    /**
	 *  This message is called when the fetching of data is done.
	 */
    public void nextPage2(ImdbInfo info) {
        this.info = info;
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "INFO");
        cardNr = 5;
        l1.setText(info.getTitle());
        l2.setText("Directed by " + info.getDirector());
        l3.setText("Genre is " + info.getGenre());
        l4.setText("Tagline: " + info.getTagline());
        textArea5.setText(info.getPlot());
        l6.setText("Ratings are " + info.getRating());
        if (info.getPoster() == null) {
            Log.print(3, "Poster is null, using default poster");
            ImageIcon icon = new ImageIcon(Settings.imageDir + "poster.png");
            int status = icon.getImageLoadStatus();
            if (status == MediaTracker.ABORTED) {
                Log.print(3, "Loading of poster was aborted");
            } else if (status == MediaTracker.ERRORED) {
                Log.print(3, "Error loading poster");
            } else if (status == MediaTracker.COMPLETE) {
                Log.print(3, "Poster loaded sucsesfully");
            }
            poster.setIcon(icon);
        } else {
            poster.setIcon(new ImageIcon(info.getPoster()));
            posterAsBI = info.getPoster();
        }
        backButton.setEnabled(true);
        nextButton.setEnabled(true);
    }
}
