package de.itar.swing.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import de.itar.exceptions.BiboExceptionHelper;
import de.itar.logic.HjDesc;
import de.itar.logic.MediumDesc;
import de.itar.logic.search.SearchHjDesc;
import de.itar.logic.util.Zwischenablage;
import de.itar.resources.Const;
import de.itar.resources.Constants;
import de.itar.swing.MainFrame;
import de.itar.swing.dialogs.MediumEditDialog;

public class TablePopup extends JPopupMenu implements ActionListener {

    private HjDesc hjDesc;

    private MediumDesc selectedMediumDesc;

    private JMenuItem menuShow;

    private JMenuItem menuEdit;

    private JMenuItem menuDelete;

    private JMenuItem menuDublicate;

    private JMenuItem menuCopy;

    private JMenuItem menuPast;

    private JMenuItem menuCut;

    private JMenuItem menuEditStatus;

    public TablePopup(JTable owner, int x, int y, String hjDesc, int selectedRow) {
        this.hjDesc = MainFrame.getInstance().getHjDescmanager().getHjDesc(hjDesc);
        this.selectedMediumDesc = this.hjDesc.getBuchDesc(selectedRow);
        buildMenu();
        show(owner, x, y);
    }

    public TablePopup(JScrollPane owner, int x, int y, String hjDesc) {
        this.hjDesc = MainFrame.getInstance().getHjDescmanager().getHjDesc(hjDesc);
        buildMenu();
        menuDelete.setEnabled(false);
        menuDublicate.setEnabled(false);
        menuEdit.setEnabled(false);
        menuEditStatus.setEnabled(false);
        menuShow.setEnabled(false);
        menuCopy.setEnabled(false);
        menuCut.setEnabled(false);
        show(owner, x, y);
    }

    private void buildMenu() {
        menuShow = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_SHOW_BOOK), (Const.IMAGE_SHOW_C));
        menuShow.addActionListener(this);
        add(menuShow);
        addSeparator();
        menuEdit = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_EDIT_BOOK), (Const.IMAGE_EDIT_C));
        menuEdit.addActionListener(this);
        add(menuEdit);
        menuEditStatus = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_EDIT_STATUS));
        menuEditStatus.addActionListener(this);
        add(menuEditStatus);
        addSeparator();
        menuCopy = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_COPY));
        menuCopy.addActionListener(this);
        add(menuCopy);
        menuCut = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_CUT));
        menuCut.addActionListener(this);
        add(menuCut);
        menuPast = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_PAST));
        menuPast.addActionListener(this);
        add(menuPast);
        menuDublicate = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_DUPLICATE_BOOK), Const.IMAGE_DUBLICATE_C);
        menuDublicate.addActionListener(this);
        add(menuDublicate);
        addSeparator();
        menuDelete = new JMenuItem(Const.getInstance().getProperty(Constants.BUTTON_REMOVE_BOOK), Const.IMAGE_DELETE_C);
        menuDelete.addActionListener(this);
        add(menuDelete);
        if (this.hjDesc instanceof SearchHjDesc) {
            menuDelete.setEnabled(false);
            menuDublicate.setEnabled(false);
            menuCut.setEnabled(false);
        }
        if (!Zwischenablage.hasData()) {
            menuPast.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == menuShow) {
            new MediumEditDialog(MainFrame.getInstance(), selectedMediumDesc, MediumEditDialog.SHOW_ENTRY).setVisible(true);
        } else if (source == menuEdit) {
            new MediumEditDialog(MainFrame.getInstance(), selectedMediumDesc, MediumEditDialog.EDIT_ENTRY).setVisible(true);
        } else if (source == menuDelete) {
            MainFrame.getInstance().removeBook();
        } else if (source == menuDublicate) {
            MediumEditDialog bed = new MediumEditDialog(MainFrame.getInstance(), hjDesc.createBuchDesc(Const.getInstance().getProperty(Constants.CREATE_NEW_BOOK)), MediumEditDialog.IS_NEW_ENTRY);
            try {
                bed.copyValuesFrom(selectedMediumDesc);
            } catch (Exception ex) {
                BiboExceptionHelper.showErrorDialog(ex);
            }
            bed.setVisible(true);
        } else if (source == menuEditStatus) {
            new MediumEditDialog(MainFrame.getInstance(), selectedMediumDesc, MediumEditDialog.MODE_LEND).setVisible(true);
        } else if (source == menuCopy) {
            try {
                Zwischenablage.put(selectedMediumDesc, Zwischenablage.MODE_COPY);
            } catch (Exception ex) {
                BiboExceptionHelper.showErrorDialog(ex);
            }
        } else if (source == menuCut) {
            try {
                Zwischenablage.put(selectedMediumDesc, Zwischenablage.MODE_CUT);
            } catch (Exception ex) {
                BiboExceptionHelper.showErrorDialog(ex);
            }
        } else if (source == menuPast) {
            try {
                boolean mode = Zwischenablage.isCutMode();
                MediumDesc toInsert = Zwischenablage.get();
                MainFrame.getInstance().pasteInto(mode, hjDesc, toInsert);
            } catch (Exception ex) {
                BiboExceptionHelper.showErrorDialog(ex);
            }
        }
    }
}
