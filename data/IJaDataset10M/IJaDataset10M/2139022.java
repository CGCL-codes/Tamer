package de.glossmaker.gui.bibtex.bars;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventTopicSubscriber;
import de.glossmaker.bib.datastructure.EBibTeXReference;
import de.glossmaker.bib.undo.UndoBibStack;
import de.glossmaker.gloss.language.Translation;
import de.glossmaker.gui.bibtex.BibItemEvents;
import de.glossmaker.gui.bibtex.EEventCommands;
import de.glossmaker.gui.bibtex.EFileOperations;
import de.glossmaker.gui.bibtex.EUserInteration;
import de.glossmaker.gui.gloss.bars.HelpItemChangeListener;
import de.glossmaker.gui.gloss.help.HelpSystem;
import de.glossmaker.undo.EUndoCommands;

/**
 * 
 * @author Markus Flingelli
 *
 */
public class BibTexItemsToolBar extends JToolBar implements EventTopicSubscriber<BibItemEvents> {

    private static final long serialVersionUID = -6934078123670740933L;

    private JButton mNew = null;

    private JButton mOpen = null;

    private JButton mSave = null;

    private JPopupMenu mSavePopUp = null;

    private JButton mAdd = null;

    private JPopupMenu mAddPopUp = null;

    private JButton mRemove = null;

    private JPopupMenu mRemovePopUp = null;

    private JButton mUndo = null;

    private JButton mRedo = null;

    private JButton mHelp = null;

    private JButton mClose = null;

    private Translation mTranslation = null;

    private ActionListener mActionListener = null;

    private JMenuItem mSaveFile = null;

    private JMenuItem mSaveAsFile = null;

    private JMenuItem mRemoveSelected;

    private JMenuItem mRemoveAll;

    public BibTexItemsToolBar(ActionListener actionListener) {
        mActionListener = actionListener;
        for (EEventCommands event : EEventCommands.values()) {
            EventBus.subscribe(event.toString(), this);
        }
        for (EUndoCommands event : EUndoCommands.values()) {
            EventBus.subscribe(event.toString(), this);
        }
        mTranslation = Translation.getInstance();
        initialize();
    }

    private void initialize() {
        mNew = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/new.png")));
        mNew.setActionCommand(EFileOperations.FILE_NEW.toString());
        mNew.addActionListener(mActionListener);
        mNew.addChangeListener(new HelpItemChangeListener("section.bibeditor.new"));
        this.add(mNew);
        mOpen = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/open.png")));
        mOpen.setActionCommand(EFileOperations.FILE_OPEN.toString());
        mOpen.addActionListener(mActionListener);
        mOpen.addChangeListener(new HelpItemChangeListener("section.bibeditor.open"));
        this.add(mOpen);
        mSavePopUp = new JPopupMenu();
        mSaveFile = new JMenuItem();
        mSaveFile.setActionCommand(EFileOperations.FILE_SAVE.toString());
        mSaveFile.addActionListener(mActionListener);
        mSaveFile.addChangeListener(new HelpItemChangeListener("section.bibeditor.save"));
        mSavePopUp.add(mSaveFile);
        mSaveAsFile = new JMenuItem();
        mSaveAsFile.setActionCommand(EFileOperations.FILE_SAVE_AS.toString());
        mSaveAsFile.addActionListener(mActionListener);
        mSaveAsFile.addChangeListener(new HelpItemChangeListener("section.bibeditor.save"));
        mSavePopUp.add(mSaveAsFile);
        mSave = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/save.png")));
        mSave.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mSavePopUp.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        mSave.addChangeListener(new HelpItemChangeListener("section.bibeditor.save"));
        this.add(mSave);
        this.add(new Separator());
        mAddPopUp = new JPopupMenu();
        for (EBibTeXReference reference : EBibTeXReference.values()) {
            JMenuItem menuItem = new JMenuItem(reference.toString().toLowerCase());
            menuItem.setActionCommand(reference.toString());
            menuItem.addActionListener(mActionListener);
            menuItem.addChangeListener(new HelpItemChangeListener("section.bibeditor.add." + reference.toString().toLowerCase()));
            mAddPopUp.add(menuItem);
        }
        mAdd = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/add.png")));
        mAdd.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mAddPopUp.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        mAdd.setActionCommand(EEventCommands.ADD.toString());
        mAdd.addActionListener(mActionListener);
        mAdd.addChangeListener(new HelpItemChangeListener("section.bibeditor.add"));
        this.add(mAdd);
        mRemovePopUp = new JPopupMenu();
        mRemoveSelected = new JMenuItem();
        mRemoveSelected.setActionCommand(EEventCommands.REMOVE_SELECTED.toString());
        mRemoveSelected.addActionListener(mActionListener);
        mRemoveSelected.addChangeListener(new HelpItemChangeListener("section.bibeditor.remove"));
        mRemovePopUp.add(mRemoveSelected);
        mRemoveAll = new JMenuItem();
        mRemoveAll.setActionCommand(EEventCommands.REMOVE_ALL.toString());
        mRemoveAll.addActionListener(mActionListener);
        mRemoveAll.addChangeListener(new HelpItemChangeListener("section.bibeditor.remove"));
        mRemovePopUp.add(mRemoveAll);
        mRemove = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/remove.png")));
        mRemove.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mRemovePopUp.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        mRemove.addChangeListener(new HelpItemChangeListener("section.bibeditor.remove"));
        this.add(mRemove);
        this.add(new Separator());
        mUndo = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/undo.png")));
        mUndo.setActionCommand(EUndoCommands.UNDO.toString());
        mUndo.addActionListener(mActionListener);
        mUndo.setEnabled(false);
        mUndo.addChangeListener(new HelpItemChangeListener("section.bibeditor.undo"));
        this.add(mUndo);
        mRedo = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/redo.png")));
        mRedo.setActionCommand(EUndoCommands.REDO.toString());
        mRedo.addActionListener(mActionListener);
        mRedo.setEnabled(false);
        mRedo.addChangeListener(new HelpItemChangeListener("section.bibeditor.redo"));
        this.add(mRedo);
        this.add(new Separator());
        mHelp = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/help.png")));
        HelpSystem.getInstance().enableHelpOnButton(mHelp, "section.bibeditor", null);
        mHelp.addChangeListener(new HelpItemChangeListener("section.bibeditor"));
        this.add(mHelp);
        this.add(new Separator());
        mClose = new JButton(new ImageIcon(BibTexItemsToolBar.class.getResource("/icons/close.png")));
        mClose.setActionCommand(EUserInteration.CLOSE_WINDOW.toString());
        mClose.addActionListener(mActionListener);
        mClose.addChangeListener(new HelpItemChangeListener("section.bibeditor.close"));
        this.add(mClose);
        changeLanguage();
    }

    private void changeLanguage() {
        mNew.setToolTipText(mTranslation.getValue("bibtex.toolbar.new.tooltip"));
        mOpen.setToolTipText(mTranslation.getValue("bibtex.toolbar.open.tooltip"));
        mSave.setToolTipText(mTranslation.getValue("bibtex.toolbar.save.tooltip"));
        mSaveFile.setText(mTranslation.getValue("bibtex.toolbar.save.popup.text"));
        mSaveFile.setToolTipText(mTranslation.getValue("bibtex.toolbar.save.popup.tooltip"));
        mSaveAsFile.setText(mTranslation.getValue("bibtex.toolbar.save_as.popup.text"));
        mSaveAsFile.setToolTipText(mTranslation.getValue("bibtex.toolbar.save_as.popup.tooltip"));
        mAdd.setToolTipText(mTranslation.getValue("bibtex.toolbar.add.tooltip"));
        mRemove.setToolTipText(mTranslation.getValue("bibtex.toolbar.remove.tooltip"));
        mRemoveSelected.setText(mTranslation.getValue("bibtex.toolbar.remove.selected.text"));
        mRemoveSelected.setToolTipText(mTranslation.getValue("bibtex.toolbar.remove.selected.tooltip"));
        mRemoveAll.setText(mTranslation.getValue("bibtex.toolbar.remove.all.text"));
        mRemoveAll.setToolTipText(mTranslation.getValue("bibtex.toolbar.remove.all.tooltip"));
        mUndo.setToolTipText(mTranslation.getValue("bibtex.toolbar.undo.tooltip"));
        mRedo.setToolTipText(mTranslation.getValue("bibtex.toolbar.redo.tooltip"));
        mHelp.setToolTipText(mTranslation.getValue("bibtex.toolbar.help.tooltip"));
        mClose.setToolTipText(mTranslation.getValue("bibtex.toolbar.close.tooltip"));
    }

    @Override
    public void onEvent(String command, BibItemEvents events) {
        mRedo.setEnabled(UndoBibStack.getInstance().redoSize() > 0);
        mUndo.setEnabled(UndoBibStack.getInstance().undoSize() > 0);
    }
}
