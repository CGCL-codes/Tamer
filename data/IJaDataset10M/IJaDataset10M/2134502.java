package freeguide.common.gui;

import freeguide.common.lib.fgspecific.Application;
import freeguide.common.lib.fgspecific.data.TVChannel;
import freeguide.common.lib.fgspecific.data.TVData;
import freeguide.common.lib.fgspecific.data.TVIteratorProgrammes;
import freeguide.common.lib.fgspecific.data.TVProgramme;
import freeguide.common.lib.general.StringHelper;
import freeguide.common.lib.general.Utils;
import freeguide.common.plugininterfaces.IModuleStorage;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * Provides search options and displays results. Results are displayed in
 * a scrolling list of  {@link TVProgramme}.  Views (or any other class for
 * that matter) can register for mouse events on this list by specifying a
 * {@link MouseListener}  in the constructor.  An example of how to use  this
 * is the class {@link ProgrammeSelectedHandler}
 *
 * @author Graham Benton
 * @version 2
 */
public class SearchDialog extends JDialog {

    private JTextField searchForText;

    private JButton searchButton;

    private JButton closeButton;

    private JList resultList;

    private JPanel jPanelQuestion;

    private Box jPanelInput;

    private JPanel jPanelOptions;

    private Box jPanelButtons;

    private Box jPanelResult;

    private JCheckBox caseSensitive;

    private JCheckBox includeFinished;

    private JCheckBox searchDescription;

    private JCheckBox searchSubtitle;

    private DefaultListModel foundModel = new DefaultListModel();

    /**
     * Class Constructor from owner and  a Mouse listener.
     *
     * @param owner the <code>JFrame</code> from which the dialog is
     *        displayed
     * @param m the MouseListener to handle MouseEvents from Result List.
     */
    public SearchDialog(JFrame owner, MouseListener mouseListener, KeyListener keyListener) {
        super(owner, Application.getInstance().getLocalizedMessage("search"));
        initComponents();
        resultList.addMouseListener(mouseListener);
        resultList.addKeyListener(keyListener);
        Utils.centreDialog(owner, this);
        setVisible(true);
    }

    /**
     * Adds l to MouseListener list of the programme list.
     *
     * @param l the MouseListener to handle MouseEvents from the Result List.
     */
    public void addMouseListener(MouseListener l) {
        resultList.addMouseListener(l);
    }

    /**
     * Sets up the dialog layout.
     */
    private void initComponents() {
        jPanelQuestion = new JPanel(new FlowLayout());
        searchForText = new JTextField(20);
        jPanelQuestion.add(searchForText);
        jPanelButtons = new Box(BoxLayout.X_AXIS);
        searchButton = new JButton(Application.getInstance().getLocalizedMessage("search"));
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchButton.setMnemonic(KeyEvent.VK_S);
        jPanelButtons.add(searchButton);
        closeButton = new JButton(Application.getInstance().getLocalizedMessage("close"));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                dispose();
            }
        });
        closeButton.setMnemonic(KeyEvent.VK_C);
        jPanelButtons.add(closeButton);
        jPanelOptions = new JPanel(new GridLayout(2, 2));
        caseSensitive = new JCheckBox(Application.getInstance().getLocalizedMessage("case_sensitive"));
        caseSensitive.setMnemonic(KeyEvent.VK_A);
        jPanelOptions.add(caseSensitive);
        includeFinished = new JCheckBox(Application.getInstance().getLocalizedMessage("include_finished"));
        includeFinished.setMnemonic(KeyEvent.VK_F);
        jPanelOptions.add(includeFinished);
        searchDescription = new JCheckBox(Application.getInstance().getLocalizedMessage("search_descriptions"));
        searchDescription.setMnemonic(KeyEvent.VK_D);
        jPanelOptions.add(searchDescription);
        searchSubtitle = new JCheckBox(Application.getInstance().getLocalizedMessage("search_subtitles"));
        searchSubtitle.setMnemonic(KeyEvent.VK_U);
        searchSubtitle.setSelected(true);
        jPanelOptions.add(searchSubtitle);
        jPanelInput = new Box(BoxLayout.Y_AXIS);
        jPanelInput.add(jPanelQuestion);
        jPanelInput.add(jPanelButtons);
        jPanelInput.add(jPanelOptions);
        jPanelResult = new Box(BoxLayout.Y_AXIS);
        jPanelResult.add(new JLabel(Application.getInstance().getLocalizedMessage("results")));
        jPanelResult.setBorder(BorderFactory.createEtchedBorder());
        resultList = new JList(foundModel);
        resultList.setCellRenderer(new FoundProgrammeCellRenderer());
        JScrollPane listScroller = new JScrollPane(resultList);
        listScroller.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        listScroller.setPreferredSize(new Dimension(300, 250));
        jPanelResult.add(listScroller);
        getContentPane().add(jPanelInput, BorderLayout.NORTH);
        getContentPane().add(jPanelResult, BorderLayout.CENTER);
        getRootPane().setDefaultButton(searchButton);
        pack();
    }

    /**
     * Performs the actual search and places result in list.
     *
     * @param evt Event that occurred
     */
    private void searchButtonActionPerformed(ActionEvent evt) {
        foundModel.clear();
        IModuleStorage.Info info = Application.getInstance().getDataStorage().getInfo().cloneInfo();
        TVData data;
        try {
            info.channelsList = null;
            data = Application.getInstance().getDataStorage().get(info);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), Application.getInstance().getLocalizedMessage("Error.Load.Title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        data.iterate(new TVIteratorProgrammes() {

            /**
                 * Called for every channel iterated
                 * through. If we need some progress for the user, this would
                 * be a good place to add it.
                 *
                 * @param channel DOCUMENT ME!
                 */
            protected void onChannel(TVChannel channel) {
            }

            /**
                 * Checks to see if the given string
                 * matches according to  the options set.
                 *
                 * @param toMatch - toMatch the string to check
                 *
                 * @return DOCUMENT_ME!
                 */
            protected boolean matchText(String toMatch) {
                if ((toMatch == null) || (toMatch == StringHelper.EMPTY_STRING)) {
                    return false;
                }
                String myMatch = new String(toMatch);
                String lookingFor = new String(searchForText.getText());
                if (!caseSensitive.isSelected()) {
                    myMatch = myMatch.toLowerCase();
                    lookingFor = lookingFor.toLowerCase();
                }
                if (myMatch.indexOf(lookingFor) == -1) {
                    return false;
                } else {
                    return true;
                }
            }

            /**
                 * Called for every Programme. Check that
                 * the programme matches the parameters, and if so add it to
                 * the list.
                 *
                 * @param programme DOCUMENT ME!
                 */
            protected void onProgramme(TVProgramme programme) {
                if (includeFinished.isSelected() || (programme.getEnd() > System.currentTimeMillis())) {
                    if ((matchText(programme.getTitle())) || (searchDescription.isSelected() && matchText(programme.getDescription())) || (searchSubtitle.isSelected() && matchText(programme.getSubTitle()))) {
                        foundModel.addElement(programme);
                    }
                }
            }
        });
    }

    /**
     * Class for displaying programme information in a JList. Displays
     * Channel, Date, Time and Title.
     */
    class FoundProgrammeCellRenderer extends JLabel implements ListCellRenderer {

        /**
         * DOCUMENT_ME!
         *
         * @param list DOCUMENT_ME!
         * @param value DOCUMENT_ME!
         * @param index DOCUMENT_ME!
         * @param isSelected DOCUMENT_ME!
         * @param cellHasFocus DOCUMENT_ME!
         *
         * @return DOCUMENT_ME!
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            TVProgramme programme = (TVProgramme) value;
            String progInfo = programme.getChannel().getDisplayName() + ' ' + DateFormat.getInstance().format(new Date(programme.getStart())) + ' ' + programme.getTitle();
            setText(progInfo);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    /**
     * Example MouseAdaptor that constructing classes can use to get
     * selection.
     */
    class ProgrammeSelectedHandler extends MouseAdapter {

        /**
         * DOCUMENT_ME!
         *
         * @param e DOCUMENT_ME!
         */
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JList programmeList = (JList) e.getSource();
                System.out.println(programmeList.getSelectedValue().toString());
            }
        }
    }
}
