package researchgrants.parts.GrantRequest.perennialStipends;

import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import researchgrants.parts.LoggedData.panels.events.SaveValidationEvent;
import researchgrants.parts.panels.*;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import researchgrants.parts.LoggedData.panels.LoggedDataPanelsNumber;
import javax.swing.event.ChangeListener;
import researchgrants.parts.LoggedData.panels.events.SaveValidationListener;
import researchgrants.parts.db.Db;
import researchgrants.parts.panelsManager.CompoundDataPanelsManager;
import researchgrants.parts.panelsManager.LoggedDataPanelsManager;
import researchgrants.utils.MyDialog;

/**
 * A compound panel to display and edit Perennial Stipends, which is a field (expectedPerennialStipends) in GrantRequest and a field (actualPerennialStipends) in GrantRequestStatusChangeAwarded.
 * This component is made of a year editable field, and a repeating (as the number of years) DirectMoney and IndirectMoney fields which are encompassed in PanelsStipend.
 * 
 * Two majorly different behaviors occur:
 * If a new PerennialStipends: All panels are initialized with defaults. Upon changes within any inner panel (year, yearly' panel, components within yearly' panels), global changes occur (validation for allowing or disallowing save, resizing years). Then when save occurs (when everything is valid only), this panel saves first and later all yearly panels save.
 * If an existing PerennialStipends: All panels are initialized with the previously saved values. The years' panels are resized upon change to the year field. Saving occurs independendtly for each field.
 * @author DOStudent1
 */
public class PanelsPerennialStipends extends PanelsCompoundView {

    private PerennialStipends perennialStipends = null;

    private boolean isDataInitialized = false;

    private int defaultYears = 1;

    private Long[] defaultDirectMoney = new Long[1];

    private Long[] defaultIndirectMoney = new Long[1];

    private JPanel pnlContent = new JPanel(new BorderLayout(0, 0));

    private JPanel pnlStipends = new JPanel();

    private LoggedDataPanelsManager yearsView;

    private LoggedDataPanelsNumber yearsPanel;

    private PanelsStipend[] panelsPerennialStipends;

    public void setDefaultData(PerennialStipends original) {
        String currentYears = original.getCurrentYears();
        if ((currentYears != null) && (!currentYears.equals(""))) {
            defaultYears = Integer.parseInt(original.getCurrentYears());
            Stipend[] originalStipends = original.getStipends();
            defaultDirectMoney = new Long[originalStipends.length];
            defaultIndirectMoney = new Long[originalStipends.length];
            for (int i = 0; i < originalStipends.length; i++) {
                Stipend stipend = originalStipends[i];
                if (stipend.getCurrentDirectMoney() == null) {
                    defaultDirectMoney[i] = null;
                } else {
                    if (stipend.getCurrentDirectMoney().equals("")) {
                        defaultDirectMoney[i] = null;
                    } else {
                        defaultDirectMoney[i] = Long.parseLong(stipend.getCurrentDirectMoney());
                    }
                }
                if (stipend.getCurrentIndirectMoney() == null) {
                    defaultIndirectMoney[i] = null;
                } else {
                    if (stipend.getCurrentIndirectMoney().equals("")) {
                        defaultIndirectMoney[i] = null;
                    } else {
                        defaultIndirectMoney[i] = Long.parseLong(stipend.getCurrentIndirectMoney());
                    }
                }
            }
        }
    }

    public void setData(PerennialStipends perennialStipends) {
        this.perennialStipends = perennialStipends;
        isDataInitialized = true;
    }

    @Override
    public Component getComponent() {
        redrawContent();
        return (pnlContent);
    }

    private void redrawContent() {
        pnlContent.removeAll();
        JPanel pnlYears = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlYears.add(new JLabel("Years:"));
        yearsPanel = new LoggedDataPanelsNumber(true, 1, true, 20);
        if (isDataInitialized) {
            yearsPanel.setData(perennialStipends.getLoggedDataYears());
            yearsPanel.addSaveValidationListener(new SaveValidationListener() {

                public void saveValidationSaved(SaveValidationEvent e) {
                    if (yearsPanel.getEdittedValue().equals("")) {
                        perennialStipends.setYears(0);
                    } else {
                        perennialStipends.setYears(Integer.parseInt(yearsPanel.getEdittedValue()));
                    }
                    redrawContent();
                }
            });
        } else {
            yearsPanel.setDefaultValue(Integer.toString(defaultYears));
            yearsPanel.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    readjustDefaultValues();
                    redrawStipendsContent();
                    notifyAllChangeListeners(new ChangeEvent(this));
                }
            });
        }
        yearsPanel.setParent(getParentWindow(), getParentContainer());
        yearsView = new LoggedDataPanelsManager();
        yearsPanel.addChangeListener(yearsView);
        yearsView.startAsNew(!isDataInitialized);
        yearsView.setMandatory(false);
        yearsView.setPanels(yearsPanel);
        pnlYears.add(yearsView);
        yearsView.display();
        pnlContent.add(pnlYears, BorderLayout.NORTH);
        redrawStipendsContent();
        pnlContent.add(pnlStipends, BorderLayout.CENTER);
    }

    private void redrawStipendsContent() {
        pnlStipends.removeAll();
        pnlStipends.setLayout(new BoxLayout(pnlStipends, BoxLayout.Y_AXIS));
        int requestedYears;
        if (isDataInitialized) {
            requestedYears = perennialStipends.getStipends().length;
        } else {
            requestedYears = defaultDirectMoney.length;
        }
        panelsPerennialStipends = new PanelsStipend[requestedYears];
        for (int i = 0; i < requestedYears; i++) {
            PanelsStipend panelsStipend = new PanelsStipend();
            panelsPerennialStipends[i] = panelsStipend;
            if (isDataInitialized) {
                panelsStipend.setData(perennialStipends.getStipends()[i]);
            } else {
                panelsStipend.setDefaultValue(i + 1, defaultDirectMoney[i], defaultIndirectMoney[i]);
                panelsStipend.addChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e) {
                        notifyAllChangeListeners(new ChangeEvent(this));
                    }
                });
            }
            panelsStipend.setParent(getParentWindow(), pnlStipends);
            CompoundDataPanelsManager compoundDataPanelsManager = new CompoundDataPanelsManager();
            compoundDataPanelsManager.setPanels(panelsStipend);
            compoundDataPanelsManager.setMandatory(false);
            pnlStipends.add(compoundDataPanelsManager);
            compoundDataPanelsManager.display();
        }
        getParentWindow().pack();
    }

    private void readjustDefaultValues() {
        if ((!yearsPanel.isValid()) || (yearsPanel.getEdittedValue().equals(""))) {
            defaultDirectMoney = new Long[0];
            defaultIndirectMoney = new Long[0];
        } else {
            defaultYears = Integer.parseInt(yearsPanel.getEdittedValue());
            Long[] oldDefaultDirectMoney = defaultDirectMoney;
            Long[] oldDefaultIndirectMoney = defaultIndirectMoney;
            defaultDirectMoney = new Long[defaultYears];
            defaultIndirectMoney = new Long[defaultYears];
            for (int i = 0; i < defaultYears; i++) {
                if (i < oldDefaultDirectMoney.length) {
                    defaultDirectMoney[i] = oldDefaultDirectMoney[i];
                    defaultIndirectMoney[i] = oldDefaultIndirectMoney[i];
                } else {
                    defaultDirectMoney[i] = null;
                    defaultIndirectMoney[i] = null;
                }
            }
        }
    }

    @Override
    public boolean isValid() {
        boolean allValid = true;
        if (!yearsPanel.isValid()) {
            allValid = false;
        }
        for (int i = 0; i < panelsPerennialStipends.length; i++) {
            PanelsStipend panelsStipend = panelsPerennialStipends[i];
            if (!panelsStipend.isValid()) {
                allValid = false;
            }
        }
        return (allValid);
    }

    /**
     * Saves a new PerennialStipends from the current values
     */
    public void saveData() {
        int newPerennialStipendsId = 0;
        try {
            Connection conn = Db.openDbConnection();
            PreparedStatement createPerennialStipendsStatement = Db.createPreparedStatement(conn, "INSERT INTO tblPerennialStipends (YearsRef) VALUES (?)");
            int newLoggedYearsId = MyDialog.saveLoggedDataPanelsManagerData(yearsView);
            createPerennialStipendsStatement.setInt(1, newLoggedYearsId);
            createPerennialStipendsStatement.executeUpdate();
            newPerennialStipendsId = Db.getLastIdentity();
            createPerennialStipendsStatement.close();
            Db.closeDbConnection(conn);
            for (int i = 0; i < panelsPerennialStipends.length; i++) {
                PanelsStipend panelsStipend = panelsPerennialStipends[i];
                panelsStipend.setDefaultPerennialStipendsId(newPerennialStipendsId);
                panelsStipend.saveData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PanelsPerennialStipends.class.getName()).log(Level.SEVERE, null, ex);
        }
        perennialStipends = PerennialStipends.getByID(newPerennialStipendsId);
    }

    public PerennialStipends getPerennialStipends() {
        return (perennialStipends);
    }
}
