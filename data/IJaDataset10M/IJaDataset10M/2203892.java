package wotlas.client.screen.plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import wotlas.client.ClientDirector;
import wotlas.client.screen.JPanelPlugIn;
import wotlas.common.ResourceManager;
import wotlas.libs.aswing.AButton;
import wotlas.libs.aswing.ARadioButton;
import wotlas.libs.aswing.ATextArea;
import wotlas.libs.aswing.ATextField;
import wotlas.utils.Debug;

/** Plug In to create/save/use HTML macros... 
 *
 * @author Aldiss, Fred McMaster
 */
public class MacroPlugIn extends JPanelPlugIn {

    /** The name of the client macros config file.
     */
    private static final String MACROS_PREFIX = "macro-";

    private static final String MACROS_SUFFIX = ".cfg";

    /** Max Number of macros the user can create.
     */
    private static final int MAX_MACROS = 20;

    /** TextFields for macros
     */
    private ATextField macrosFields[];

    /** RadioButtons for macros
     */
    private ARadioButton macrosRadios[];

    /** JPanel for macros
     */
    private JPanel macrosPanel[];

    /** 'New' macro button.
     */
    private AButton newMacroButton;

    /** 'Use' macro button.
     */
    private AButton useMacroButton;

    /** 'Del' macro button.
     */
    private AButton delMacroButton;

    /** 'Save' macro button.
     */
    private AButton saveMacroButton;

    /** Center panel where the macros are set...
     */
    private JPanel centerPanel;

    /** Exclusive group of macros.
     */
    private ButtonGroup macrosGroup;

    /** Constructor.
     */
    public MacroPlugIn() {
        super();
        setLayout(new BorderLayout());
        ATextArea taInfo = new ATextArea("You can create here HTML macros to use in the chat." + " Each macro has a shortcut @N@ where N is the macro's index.");
        taInfo.setLineWrap(true);
        taInfo.setWrapStyleWord(true);
        taInfo.setEditable(false);
        taInfo.setAlignmentX(0.5f);
        taInfo.setOpaque(false);
        add(taInfo, BorderLayout.NORTH);
        this.centerPanel = new JPanel(new GridLayout(MacroPlugIn.MAX_MACROS, 1, 5, 5));
        add(new JScrollPane(this.centerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        this.macrosGroup = new ButtonGroup();
        this.macrosFields = new ATextField[MacroPlugIn.MAX_MACROS];
        this.macrosRadios = new ARadioButton[MacroPlugIn.MAX_MACROS];
        this.macrosPanel = new JPanel[MacroPlugIn.MAX_MACROS];
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));
        buttonsPanel.setBackground(Color.white);
        add(buttonsPanel, BorderLayout.SOUTH);
        ImageIcon im_newup = ClientDirector.getResourceManager().getImageIcon("new-mini-up.gif");
        ImageIcon im_newdo = ClientDirector.getResourceManager().getImageIcon("new-mini-do.gif");
        ImageIcon im_useup = ClientDirector.getResourceManager().getImageIcon("use-mini-up.gif");
        ImageIcon im_usedo = ClientDirector.getResourceManager().getImageIcon("use-mini-do.gif");
        ImageIcon im_delup = ClientDirector.getResourceManager().getImageIcon("del-mini-up.gif");
        ImageIcon im_deldo = ClientDirector.getResourceManager().getImageIcon("del-mini-do.gif");
        ImageIcon im_saveup = ClientDirector.getResourceManager().getImageIcon("save-mini-up.gif");
        ImageIcon im_savedo = ClientDirector.getResourceManager().getImageIcon("save-mini-do.gif");
        this.newMacroButton = new AButton(im_newup);
        this.newMacroButton.setRolloverIcon(im_newdo);
        this.newMacroButton.setPressedIcon(im_newdo);
        this.newMacroButton.setBorderPainted(false);
        this.newMacroButton.setContentAreaFilled(false);
        this.newMacroButton.setFocusPainted(false);
        this.newMacroButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addMacro("");
            }
        });
        this.useMacroButton = new AButton(im_useup);
        this.useMacroButton.setRolloverIcon(im_usedo);
        this.useMacroButton.setPressedIcon(im_usedo);
        this.useMacroButton.setBorderPainted(false);
        this.useMacroButton.setContentAreaFilled(false);
        this.useMacroButton.setFocusPainted(false);
        this.useMacroButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String macro = getMacro(getSelectedIndex());
                ClientDirector.getDataManager().getClientScreen().getChatPanel().sendChatMessage(macro);
            }
        });
        this.delMacroButton = new AButton(im_delup);
        this.delMacroButton.setRolloverIcon(im_deldo);
        this.delMacroButton.setPressedIcon(im_deldo);
        this.delMacroButton.setBorderPainted(false);
        this.delMacroButton.setContentAreaFilled(false);
        this.delMacroButton.setFocusPainted(false);
        this.delMacroButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeMacro(getSelectedIndex());
            }
        });
        this.saveMacroButton = new AButton(im_saveup);
        this.saveMacroButton.setRolloverIcon(im_savedo);
        this.saveMacroButton.setPressedIcon(im_savedo);
        this.saveMacroButton.setBorderPainted(false);
        this.saveMacroButton.setContentAreaFilled(false);
        this.saveMacroButton.setFocusPainted(false);
        this.saveMacroButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save(getMacros());
            }
        });
        buttonsPanel.add(this.newMacroButton);
        buttonsPanel.add(this.useMacroButton);
        buttonsPanel.add(this.delMacroButton);
        buttonsPanel.add(this.saveMacroButton);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /** Called once to initialize the plug-in.
     *  @return if true we display the plug-in, return false if something fails during
     *          this init(), this way the plug-in won't be displayed.
     */
    @Override
    public boolean init() {
        String macros[] = load();
        for (int i = 0; i < macros.length; i++) addMacro(macros[i]);
        return true;
    }

    /** Called when we need to reset the content of this plug-in.
     */
    @Override
    public void reset() {
        this.centerPanel.removeAll();
        this.macrosGroup = new ButtonGroup();
        for (int i = 0; i < MacroPlugIn.MAX_MACROS; i++) {
            this.macrosFields[i] = null;
            this.macrosRadios[i] = null;
            this.macrosPanel[i] = null;
        }
        init();
    }

    /** To search & process a text with macros. A macro can not be used twice in a line.
     *  @param text text to parse for macros '@NN@' calls.
     */
    public String processMacros(String text) {
        boolean macrosFound[] = new boolean[MacroPlugIn.MAX_MACROS];
        for (int i = 0; i < MacroPlugIn.MAX_MACROS; i++) macrosFound[i] = false;
        do {
            int pos = 0, pos2 = -1, id = -1;
            do {
                if (pos2 + 1 == text.length()) return text;
                pos = text.indexOf('@', pos2 + 1);
                if (pos < 0 || pos == text.length() - 1) return text;
                pos2 = text.indexOf('@', pos + 1);
                if (pos2 < 0) return text;
                if (pos2 - pos > 3) continue;
                try {
                    id = Integer.parseInt(text.substring(pos + 1, pos2));
                } catch (Exception e) {
                }
            } while (id < 0);
            String macro;
            if (id > MacroPlugIn.MAX_MACROS - 1 || this.macrosFields[id] == null) macro = "#macro not found#"; else if (macrosFound[id]) macro = "#macro used twice#"; else {
                macro = this.macrosFields[id].getText();
                macrosFound[id] = true;
            }
            StringBuffer buf = new StringBuffer(text.substring(0, pos));
            buf.append(macro);
            if (pos2 + 1 < text.length()) buf.append(text.substring(pos2 + 1, text.length()));
            text = buf.toString();
        } while (true);
    }

    /** To add a new macros to our list.
     * @param macro text of the macro
     */
    protected void addMacro(String macro) {
        int index = 0;
        while (index < MacroPlugIn.MAX_MACROS && this.macrosFields[index] != null) index++;
        if (index == MacroPlugIn.MAX_MACROS) {
            JOptionPane.showMessageDialog(null, "Maximum number of Macros reached !", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.macrosPanel[index] = new JPanel(new BorderLayout());
        this.macrosRadios[index] = new ARadioButton("" + index);
        this.macrosFields[index] = new ATextField(macro, 12);
        this.macrosPanel[index].add(this.macrosRadios[index], BorderLayout.WEST);
        this.macrosPanel[index].add(this.macrosFields[index], BorderLayout.EAST);
        this.centerPanel.add(this.macrosPanel[index]);
        this.macrosGroup.add(this.macrosRadios[index]);
        this.macrosRadios[index].setSelected(true);
        this.centerPanel.validate();
    }

    /** To remove a new macros to our list.
     * @param index index of the macros to remove
     */
    protected void removeMacro(int index) {
        if (index < 0) return;
        this.centerPanel.remove(this.macrosPanel[index]);
        this.macrosGroup.remove(this.macrosRadios[index]);
        this.macrosPanel[index] = null;
        this.macrosRadios[index] = null;
        this.macrosFields[index] = null;
        int i;
        for (i = index; i < MacroPlugIn.MAX_MACROS - 1 && this.macrosPanel[i + 1] != null; i++) {
            this.macrosPanel[i] = this.macrosPanel[i + 1];
            this.macrosRadios[i] = this.macrosRadios[i + 1];
            this.macrosFields[i] = this.macrosFields[i + 1];
            this.macrosRadios[i].setText("" + i);
        }
        if (this.macrosFields[i] != null) {
            this.macrosPanel[i] = null;
            this.macrosRadios[i] = null;
            this.macrosFields[i] = null;
        }
        this.centerPanel.validate();
        this.centerPanel.repaint();
        if (this.macrosRadios[index] != null) this.macrosRadios[index].setSelected(true); else if (index - 1 >= 0 && this.macrosRadios[index - 1] != null) this.macrosRadios[index - 1].setSelected(true); else if (this.macrosRadios[0] != null) this.macrosRadios[0].setSelected(true);
    }

    /** To get our macros list.
     * @return all the macros
     */
    protected String[] getMacros() {
        int nb = 0;
        while (nb < MacroPlugIn.MAX_MACROS && this.macrosFields[nb] != null) nb++;
        String list[] = new String[nb];
        for (int i = 0; i < nb; i++) list[i] = this.macrosFields[i].getText();
        return list;
    }

    /** To get a macro from our list.
     * @param index inde of the macro to get
     */
    protected String getMacro(int index) {
        if (index < 0 || index >= MacroPlugIn.MAX_MACROS || this.macrosFields[index] == null) return "Macro not found !! (" + index + ")";
        return this.macrosFields[index].getText();
    }

    /** To get the selected index in the macro list.
     *  @return the first selected index, -1 if none
     */
    protected int getSelectedIndex() {
        for (int i = 0; i < MacroPlugIn.MAX_MACROS && this.macrosPanel[i] != null; i++) if (this.macrosRadios[i].isSelected()) return i;
        return -1;
    }

    /** Returns the name of the plug-in that will be displayed in the JPlayerPanel.
     * @return a short name for the plug-in
     */
    @Override
    public String getPlugInName() {
        return "Macro";
    }

    /** Returns the name of the plug-in's author.
     * @return author name.
     */
    @Override
    public String getPlugInAuthor() {
        return "Wotlas Team (Aldiss & Fred)";
    }

    /** Returns the tool tip text that will be displayed in the JPlayerPanel.
     * @return a short tool tip text
     */
    @Override
    public String getToolTipText() {
        return "To create HTML macros";
    }

    /** Eventual index in the list of JPlayerPanels
     * @return -1 if the plug-in has to be added at the end of the plug-in list,
     *         otherwise a positive integer for a precise location.
     */
    @Override
    public int getPlugInIndex() {
        return -1;
    }

    /** Tells if this plug-in is a system plug-in that represents some base
     *  wotlas feature.
     * @return true means system plug-in, false means user plug-in
     */
    @Override
    public boolean isSystemPlugIn() {
        return true;
    }

    /** Persistence class containing all the macros.
     */
    public static class MacrosList {

        private String macros[];

        public MacrosList() {
        }

        public String[] getMacros() {
            return this.macros;
        }

        public void setMacros(String[] macros) {
            this.macros = macros;
        }
    }

    /** To load the Macro config file. If the file is not found we return an empty list.
     *  @return the loaded macros list, an empty array if no config file was found
     */
    public String[] load() {
        ResourceManager rManager = ClientDirector.getResourceManager();
        String fileName = rManager.getExternalMacrosDir() + MacroPlugIn.MACROS_PREFIX + ClientDirector.getDataManager().getMyPlayer().getPrimaryKey() + MacroPlugIn.MACROS_SUFFIX;
        if (new File(fileName).exists()) {
            MacrosList list = (MacrosList) rManager.loadObject(fileName);
            if (list != null) return list.getMacros();
        }
        Debug.signal(Debug.NOTICE, null, "No macros config found...");
        String listStr[] = new String[1];
        listStr[0] = "";
        return listStr;
    }

    /** To load the Macro config file. If the file is not found we return an empty list.
     * @param macros macros list to save
     * @return true if the save succeeded, false otherwise
     */
    public boolean save(String[] macros) {
        ResourceManager rManager = ClientDirector.getResourceManager();
        MacrosList list = new MacrosList();
        list.setMacros(macros);
        if (rManager.saveObject(list, rManager.getExternalMacrosDir() + MacroPlugIn.MACROS_PREFIX + ClientDirector.getDataManager().getMyPlayer().getPrimaryKey() + MacroPlugIn.MACROS_SUFFIX)) return true;
        Debug.signal(Debug.ERROR, this, "Failed to save macros.");
        return false;
    }
}
