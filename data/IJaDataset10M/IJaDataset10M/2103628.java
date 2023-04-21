package tikara.gui.resolutions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import shared.gui.dialogs.embedded.DialogButtons;
import shared.gui.dialogs.embedded.DialogPanelFactory;
import tikara.events.ActivateCouncil;
import tikara.events.NewResolution;
import tikara.events.NewTikaraModel;
import tikara.events.RemoveResolution;
import tikara.events.ResolutionPropertyChanged;
import tikara.gui.images.ImgUtil;
import tikara.gui.main.Context;
import tikara.gui.shared.TikaraAddButton;
import tikara.gui.shared.TikaraButton;
import tikara.gui.shared.TikaraEditButton;
import tikara.gui.shared.TikaraRemoveButton;
import tikara.gui.utilities.Language;
import tikara.model.Amendment;
import tikara.model.Council;
import tikara.model.Resolution;
import tikara.model.TikaraModel;
import tikara.model.Vote;
import tikara.model.VotingResult;

/**
 * Shows all resolutions and functions to add, remove, edit and open them.
 * 
 * GUI arrangement:
 * +------------------------------------------------+
 * | table showing all resolutions                  |
 * +------------------------------------------------+
 * | filler | Open resolution | add | remove | edit |
 * +------------------------------------------------+
 * 
    Copyright (c) 2009 by Serge Haenni
    
    This file is part of Tikara.

    Tikara is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tikara is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tikara.  If not, see <http://www.gnu.org/licenses/>.
    
 * @author Serge Haenni
 * @version 0.1, 2009-01-21
 *
 */
@SuppressWarnings("serial")
public class AllResolutions extends JPanel implements ActionListener, Observer, MouseListener {

    /**
	 * The table showing all resolutions
	 */
    private JTable allResolutionsTable;

    /**
	 * Table model of allResolutionsTable
	 */
    private AllResolutionsTableModel allResolutionsTableModel;

    /**
	 * Scrollpane of the allResolutionsTable
	 */
    private JScrollPane spAllResolutionsTable;

    /**
	 * One variable used for the whole class
	 */
    private GridBagConstraints gbc;

    /**
	 * Buttons to open, add, remove or edit a resolution
	 */
    private JButton add, remove, edit, open;

    /**
	 * Filler to push components to a specific position
	 */
    private JLabel filler;

    /**
	 * The resolution parent this display belongs to. Used to open resolutions 
	 * in the resolution parent's tabbed pane.
	 */
    private AllResolutionsView resolutionsView;

    /**
	 * Holds error/warning/info messages
	 */
    private JPanel errorPanel;

    /**
	 * Current language to get and display text from
	 */
    private Language lang;

    /**
	 * Constructor, calls the function to display the gui components
	 */
    public AllResolutions(AllResolutionsView resolutionsView) {
        this.resolutionsView = resolutionsView;
        Context.getInstance().addObserver(this);
        Context.getInstance().getTikaraModel().addObserver(this);
        for (Resolution r : Context.getInstance().getTikaraModel().getActiveCouncil().getResolutions()) {
            r.addObserver(this);
        }
        initComponents();
    }

    /**
	 * Initializes and shows all gui components
	 */
    private void initComponents() {
        setLayout(new GridBagLayout());
        lang = Context.getInstance().getTikaraModel().getCurrentLanguage();
        allResolutionsTableModel = new AllResolutionsTableModel();
        for (int i = 0; i < allResolutionsTableModel.getRowCount(); i++) {
            allResolutionsTableModel.removeRow(i);
        }
        for (Resolution r : Context.getInstance().getTikaraModel().getActiveCouncil().getResolutions()) {
            allResolutionsTableModel.addRow(r.toOverviewArray());
        }
        allResolutionsTable = new JTable(allResolutionsTableModel);
        allResolutionsTable.addMouseListener(this);
        Component c = allResolutionsTable.getDefaultRenderer(allResolutionsTable.getColumnClass(1)).getTableCellRendererComponent(allResolutionsTable, "none yet", false, false, 0, 0);
        allResolutionsTable.getColumn(allResolutionsTable.getColumnName(1)).setPreferredWidth(c.getPreferredSize().width);
        errorPanel = new JPanel(new BorderLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(errorPanel, gbc);
        spAllResolutionsTable = new JScrollPane(allResolutionsTable);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(spAllResolutionsTable, gbc);
        filler = new JLabel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1;
        add(filler, gbc);
        open = new TikaraButton();
        open.setIcon(ImgUtil.loadImg("start.png"));
        open.setToolTipText(lang.getResolutionsOpenResolutionTooltip());
        open.setBorderPainted(false);
        open.setContentAreaFilled(false);
        open.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(open, gbc);
        add = new TikaraAddButton();
        add.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        add(add, gbc);
        remove = new TikaraRemoveButton();
        remove.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        add(remove, gbc);
        edit = new TikaraEditButton();
        edit.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        add(edit, gbc);
    }

    /**
	 * Gets called if an action is received
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(open)) {
            if (allResolutionsTable.getSelectedColumn() != -1) {
                String resolutionName = (String) allResolutionsTableModel.getValueAt(allResolutionsTable.getSelectedRow(), 0);
                resolutionsView.openResolution(resolutionName);
            }
        } else if (e.getSource().equals(add)) {
            AddResolutionDialog ard = new AddResolutionDialog();
            ard.setVisible(true);
        } else if (e.getSource().equals(remove)) {
            if (allResolutionsTable.getSelectedRow() != -1) {
                final Council activeCouncil = Context.getInstance().getTikaraModel().getActiveCouncil();
                String resolutionName = (String) allResolutionsTableModel.getValueAt(allResolutionsTable.getSelectedRow(), 0);
                final Resolution resolution = activeCouncil.getResolutionByName(resolutionName);
                for (Vote v : activeCouncil.getVotes()) {
                    if (((v.getResolution() != null && v.equals(resolution.getVote())) || (v.getAmendment() != null && v.getAmendment().getResolution().equals(resolution))) && v.getOutcome() == VotingResult.PENDING) {
                        errorPanel.add(DialogPanelFactory.createWarningDialogPanel(lang.getResolutionsRemoveButAmendmentOnTheFloor(), EnumSet.of(DialogButtons.YES, DialogButtons.NO, DialogButtons.CANCEL), new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (e.getActionCommand().equals("Yes")) {
                                    for (Amendment a : resolution.getAmendments()) {
                                        if (a.getVote() != null) activeCouncil.removeVote(a.getVote().getCause());
                                    }
                                    if (resolution.getVote() != null) activeCouncil.removeVote(resolution.getVote().getCause());
                                    activeCouncil.removeResolution(resolution.getName());
                                } else if (e.getActionCommand().equals("No")) {
                                    activeCouncil.removeResolution(resolution.getName());
                                } else if (e.getActionCommand().equals("Cancel")) {
                                }
                                clearErrorPanel();
                            }
                        }), BorderLayout.CENTER);
                        setVisible(false);
                        setVisible(true);
                        return;
                    }
                }
                errorPanel.add(DialogPanelFactory.createWarningDialogPanel(lang.getResolutionsRemoveIncludingVotes(), EnumSet.of(DialogButtons.YES, DialogButtons.NO, DialogButtons.CANCEL), new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals("Yes")) {
                            for (Amendment a : resolution.getAmendments()) {
                                if (a.getVote() != null) activeCouncil.removeVote(a.getVote().getCause());
                            }
                            if (resolution.getVote() != null) activeCouncil.removeVote(resolution.getVote().getCause());
                            activeCouncil.removeResolution(resolution.getName());
                        } else if (e.getActionCommand().equals("No")) {
                            activeCouncil.removeResolution(resolution.getName());
                        } else if (e.getActionCommand().equals("Cancel")) {
                        }
                        clearErrorPanel();
                    }
                }), BorderLayout.CENTER);
                setVisible(false);
                setVisible(true);
            }
        } else if (e.getSource().equals(edit)) {
            if (allResolutionsTable.getSelectedRow() != -1) {
                Council activeCouncil = Context.getInstance().getTikaraModel().getActiveCouncil();
                String resolutionName = (String) allResolutionsTableModel.getValueAt(allResolutionsTable.getSelectedRow(), 0);
                Resolution r = activeCouncil.getResolutionByName(resolutionName);
                EditResolutionDialog erd = new EditResolutionDialog(r);
                erd.setVisible(true);
            }
        }
    }

    /**
	 * Clears the error panel of all messages within
	 */
    private void clearErrorPanel() {
        errorPanel.removeAll();
        setVisible(false);
        setVisible(true);
    }

    /**
	 * Gets called when the observable changes
	 */
    public void update(Observable obs, Object obj) {
        if (obs instanceof Context) {
            Context.getInstance().getTikaraModel().addObserver(this);
            return;
        }
        TikaraModel model = Context.getInstance().getTikaraModel();
        if (obj != null) {
            if (obj instanceof NewTikaraModel) {
                for (int i = 0; i < allResolutionsTableModel.getRowCount(); i++) {
                    allResolutionsTableModel.removeRow(i);
                }
                for (Resolution r : model.getActiveCouncil().getResolutions()) {
                    allResolutionsTableModel.addRow(r.toOverviewArray());
                }
                for (Resolution r : model.getActiveCouncil().getResolutions()) {
                    r.addObserver(this);
                }
            } else if (obj instanceof ActivateCouncil) {
                for (int i = 0; i < allResolutionsTableModel.getRowCount(); i++) {
                    allResolutionsTableModel.removeRow(i);
                }
                for (Resolution r : Context.getInstance().getTikaraModel().getActiveCouncil().getResolutions()) {
                    allResolutionsTableModel.addRow(r.toOverviewArray());
                }
                for (Resolution r : model.getActiveCouncil().getResolutions()) {
                    r.addObserver(this);
                }
            } else if (obj instanceof NewResolution) {
                NewResolution newResolutionEvent = (NewResolution) obj;
                for (Resolution r : newResolutionEvent.getNewResolutions()) {
                    allResolutionsTableModel.addRow(r.toOverviewArray());
                    r.addObserver(this);
                }
            } else if (obj instanceof RemoveResolution) {
                RemoveResolution removeResolutionEvent = (RemoveResolution) obj;
                for (Resolution r : removeResolutionEvent.getRemovedResolutions()) {
                    for (int i = 0; i < allResolutionsTableModel.getRowCount(); i++) {
                        if (((String) allResolutionsTableModel.getValueAt(i, 0)).equals(r.getName())) {
                            resolutionsView.removeResolution((String) allResolutionsTableModel.getValueAt(i, 0));
                            allResolutionsTableModel.removeRow(i);
                            break;
                        }
                    }
                }
            } else if (obj instanceof ResolutionPropertyChanged) {
                ResolutionPropertyChanged resolutionChangedEvent = (ResolutionPropertyChanged) obj;
                for (String key : resolutionChangedEvent.getChangedResolutions().keySet()) {
                    for (int i = 0; i < allResolutionsTableModel.getRowCount(); i++) {
                        if (((String) allResolutionsTableModel.getValueAt(i, 0)).equals(key)) {
                            Resolution r = resolutionChangedEvent.getChangedResolutions().get(key);
                            String[] row = r.toOverviewArray();
                            for (int j = 0; j < row.length; j++) {
                                allResolutionsTableModel.setValueAt(row[j], i, j);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(allResolutionsTable)) {
            if (e.getClickCount() == 2) {
                String resolutionName = (String) allResolutionsTableModel.getValueAt(allResolutionsTable.getSelectedRow(), 0);
                resolutionsView.openResolution(resolutionName);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
