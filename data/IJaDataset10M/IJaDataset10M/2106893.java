package com.sardak.blogoommer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import com.sardak.blogoommer.help.HelpPane;
import com.sardak.blogoommer.ui.data.Blog;
import com.sardak.blogoommer.ui.util.Constants;
import com.sardak.blogoommer.ui.util.MnemonicsHelper;
import com.sardak.blogoommer.ui.util.PrefUtil;

/**
 * UI for WYSYWYG blog editing.
 * @author Ren� Ghosh
 */
public class BlogEditor implements ActionListener {

    private JFrame frame = null;

    private JEditorPane editorPane;

    private JTextField subjectField;

    private JPanel contentPanel;

    private HTMLEditorKit editorKit = new HTMLEditorKit();

    private HTMLDocument doc = (HTMLDocument) editorKit.createDefaultDocument();

    private JToolBar topPanel;

    private JFileChooser fileChooser;

    private UI ui;

    private int id = -1;

    private JTextArea descriptionArea;

    /**
    * Constructor that takes the ui as parameter
    */
    public BlogEditor(UI ui, int id) {
        this.ui = ui;
        this.id = id;
        frame = new JFrame();
        frame.setTitle(Constants.get(Constants.BLOGTITLE));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setIconImage(IconReader.getIcon("logo.png").getImage());
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(true);
        editorPane.setEditorKit(editorKit);
        editorPane.setDocument(doc);
        editorPane.setText("<html><head><style> " + "p{font-family: verdana;font-size: 12pt} " + "body{font-family: verdana} " + "</style></head><body><p>" + "</p></body></html>");
        editorPane.setFont(new Font("Verdana", Font.PLAIN, 12));
        fileChooser = new JFileChooser();
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
        middlePanel.add(contentPanel, BorderLayout.CENTER);
        JPanel subjectPanel = new JPanel();
        subjectField = new JTextField();
        JPanel metaPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        metaPanel.setLayout(gbl);
        metaPanel.add(addLabel("  " + Constants.get(Constants.TITLE) + ":  ", gbl));
        metaPanel.add(addComponent(subjectField, gbl));
        metaPanel.add(addLabel("  " + Constants.get(Constants.EDITORSUMMARY) + ":  ", gbl));
        metaPanel.add(addComponent(new JScrollPane(descriptionArea), gbl));
        middlePanel.add(metaPanel, BorderLayout.NORTH);
        frame.getContentPane().add(middlePanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int width = 450, height = 600;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(width, height);
        frame.setLocation((int) (dim.getWidth() / 2.0 - width / 2.0), (int) (dim.getHeight() / 2.0 - height / 2.0));
        frame.setSize(width, height);
        buildToolBar();
        buildMenu();
        frame.show();
        subjectField.requestFocus();
    }

    /**
	 * add a component to the form
	 */
    private JComponent addComponent(JComponent component, GridBagLayout layout) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.7;
        if (!(component instanceof JScrollPane)) {
            constraints.fill = GridBagConstraints.BOTH;
        }
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        layout.setConstraints(component, constraints);
        return component;
    }

    /**
	 * add a label
	 */
    protected JLabel addLabel(String label, GridBagLayout layout) {
        GridBagConstraints constraints = new GridBagConstraints();
        String labelText = label;
        JLabel component = new JLabel(labelText);
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.3;
        layout.setConstraints(component, constraints);
        return component;
    }

    /**
    * set the properties from a blog
    */
    public void fromBlog(Blog blog) {
        subjectField.setText(blog.getSubject());
        descriptionArea.setText(blog.getSummary());
        try {
            editorKit.read(new StringReader(blog.getContent()), doc, editorPane.getCaretPosition());
            id = blog.getId();
            editorPane.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
    * Make menu item
    */
    private JMenuItem makeMenuItem(String label, JMenu menu) {
        JMenuItem item = new JMenuItem(Constants.get(label));
        item.setActionCommand(label);
        item.addActionListener(this);
        MnemonicsHelper.mnemonize(item);
        menu.add(item);
        return item;
    }

    /**
    * build the frame menu
    */
    public void buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu actionMenu = new JMenu(Constants.get(Constants.EDITORACTION));
        MnemonicsHelper.mnemonize(actionMenu);
        JMenu insertMenu = new JMenu(Constants.get(Constants.EDITORINSERT));
        JMenu helpMenu = new JMenu(Constants.get(Constants.HELP));
        MnemonicsHelper.mnemonize(insertMenu);
        MnemonicsHelper.mnemonize(helpMenu);
        menuBar.add(actionMenu);
        menuBar.add(insertMenu);
        menuBar.add(helpMenu);
        MnemonicsHelper.mnemonize(insertMenu);
        makeMenuItem(Constants.EDITORIMAGE, insertMenu).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        makeMenuItem(Constants.EDITORLINK, insertMenu).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        makeMenuItem(Constants.BLOGSAVE, actionMenu).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        makeMenuItem(Constants.BLOGUPDATE, actionMenu).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        makeMenuItem(Constants.HELPEDITOR, helpMenu).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        ;
    }

    /**
    * add a toolbar action
    */
    private JButton addToolBarAction(String mapReference, String text) {
        JButton button = topPanel.add(editorPane.getActionMap().get(mapReference));
        if (text.length() > 0) {
            button.setText(Constants.get(text));
        } else {
            button.setText("");
        }
        button.addActionListener(this);
        button.setFocusable(false);
        return button;
    }

    /**
    * build the toolbar
    */
    private void buildToolBar() {
        topPanel = new JToolBar();
        contentPanel.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        Font boldFont = new Font("Verdana", Font.BOLD, 12);
        Font underlineFont = new Font("Verdana", Font.PLAIN, 12);
        Font italicFont = new Font("Verdana", Font.ITALIC, 12);
        addToolBarAction("font-bold", Constants.BOLD).setFont(boldFont);
        addToolBarAction("font-underline", Constants.UNDERLINE).setFont(underlineFont);
        addToolBarAction("font-italic", Constants.ITALIC).setFont(italicFont);
        addToolBarAction("left-justify", "").setIcon(IconReader.getIcon("leftalign.png"));
        addToolBarAction("center-justify", "").setIcon(IconReader.getIcon("centeralign.png"));
        addToolBarAction("right-justify", "").setIcon(IconReader.getIcon("rightalign.png"));
        editorPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK), "font-bold");
        editorPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK), "font-italic");
        editorPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK), "font-underline");
    }

    /**
	 * save the current blog
	 */
    private void saveBlog(boolean update) {
        StringWriter writer = new StringWriter();
        try {
            editorKit.write(writer, doc, 0, editorPane.getText().length());
            ui.addBlog(new Blog(subjectField.getText().trim(), descriptionArea.getText().trim(), writer.toString().trim(), id), update);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Process frame events
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent actionEvent) {
        editorPane.requestFocus();
        String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals(Constants.EDITORIMAGE)) {
            String selectedText = editorPane.getSelectedText();
            selectedText = selectedText == null ? "" : selectedText;
            ImageLinkEditor editor = new ImageLinkEditor(frame);
            editor.linkHint(selectedText);
            editor.show();
            String text = editor.getTextValue(Constants.LINKTEXT);
            String link = editor.getTextValue(Constants.LINKLINK);
            String height = editor.getTextValue(Constants.IMAGEHEIGHT);
            height = height == null ? "" : height.trim();
            String width = editor.getTextValue(Constants.IMAGEWIDTH);
            width = width == null ? "" : width.trim();
            try {
                String widthString = width.length() > 0 ? " width=\"" + width + "\" " : "";
                String heightString = height.length() > 0 ? " height=\"" + height + "\" " : "";
                String html = "<a href=\"" + link + "\"><img " + widthString + heightString + " border=\"0\" alt=\"" + text + "\" src=\"" + link + "\"></a>";
                editorKit.insertHTML(doc, editorPane.getCaretPosition(), html, 0, 0, HTML.Tag.A);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else if (actionCommand.equals(Constants.EDITORLINK)) {
            String selectedText = editorPane.getSelectedText();
            selectedText = selectedText == null ? "" : selectedText;
            LinkEditor editor = new LinkEditor(frame);
            editor.linkHint(selectedText);
            editor.show();
            String text = editor.getTextValue(Constants.LINKTEXT);
            String link = editor.getTextValue(Constants.LINKLINK);
            try {
                int length = selectedText.length();
                doc.replace(editorPane.getSelectionStart(), length, "", null);
                editorKit.insertHTML(doc, editorPane.getCaretPosition(), "<a href=\"" + link + "\">" + text + "</a>&#160;", 0, 0, HTML.Tag.A);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else if (actionCommand.equals(Constants.BLOGSAVE)) {
            saveBlog(false);
            frame.dispose();
        } else if (actionCommand.equals(Constants.BLOGUPDATE)) {
            saveBlog(true);
            frame.dispose();
        } else if (actionCommand.equals(Constants.HELPEDITOR)) {
            String lang = PrefUtil.getLanguagePreference();
            lang = lang != "" ? lang + "/" : lang;
            new HelpPane(Constants.get(Constants.HELPEDITOR), "/" + lang + "editor.html");
        }
    }

    /**
	 * set data from an existing blog
	 */
    public void setBlog(Blog blog) {
        subjectField.setText(blog.getSubject());
        descriptionArea.setText(blog.getSummary());
        editorPane.setText(blog.getContent());
        id = blog.getId();
    }
}
