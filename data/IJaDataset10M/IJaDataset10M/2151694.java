package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.client.ContestConnection;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.GetContestResultsRequest;
import ru.ipo.dces.clientservercommunication.GetContestResultsResponse;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.extra.ComparePluginGenResultsView;
import ru.ipo.dces.plugins.admin.resultstable.ResultsTableModel;
import ru.ipo.dces.plugins.admin.resultstable.OneMessageTableModel;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import info.clearthought.layout.TableLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 0:39:44
 */
public class ResultsPlugin implements Plugin {

    private final JPanel mainPanel;

    private final JTable table;

    private final ContestChoosingPanel contestChoosingPanel;

    private GetContestResultsResponse resResponse = null;

    public ResultsPlugin(PluginEnvironment env) {
        this.mainPanel = new JPanel();
        this.table = new JTable();
        this.contestChoosingPanel = new ContestChoosingPanel();
        env.setTitle(Localization.getAdminPluginName(ResultsPlugin.class));
        JScrollPane scroll = new JScrollPane(table);
        mainPanel.setLayout(new TableLayout(new double[][] { { TableLayout.FILL }, { TableLayout.PREFERRED, TableLayout.FILL } }));
        mainPanel.add(scroll, "0, 1");
        table.setModel(new DefaultTableModel());
        contestChoosingPanel.addContestChangedActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                contestSelected(contestChoosingPanel.getContest());
            }
        });
        mainPanel.add(contestChoosingPanel, "0, 0");
        mainPanel.validate();
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    if (resResponse != null) {
                        ComparePluginGenResultsView v = new ComparePluginGenResultsView(resResponse.headers, resResponse.minorHeaders, resResponse.table, table.getSelectedRow());
                    }
                }
            }
        });
    }

    private void saveTableContents2CSV() {
        JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("csv files", "csv");
        fileopen.addChoosableFileFilter(filter);
        if (fileopen.showDialog(null, "Open file") != JFileChooser.APPROVE_OPTION) return;
        File file = fileopen.getSelectedFile();
        try {
            PrintWriter p = new PrintWriter(file);
            for (int j = 0; j < table.getColumnCount(); j++) {
                if (j != 0) p.append(',');
                p.append(table.getModel().getColumnName(j));
            }
            p.append('\n');
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    if (j != 0) p.append(',');
                    p.append(table.getModel().getValueAt(i, j).toString());
                }
                p.append('\n');
            }
            p.close();
            JOptionPane.showMessageDialog(null, "Файл успешно записан");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Файл записать не удалось");
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public void activate() {
        contestChoosingPanel.setVisible(Controller.isContestUnknownMode());
        contestChoosingPanel.refreshContestList();
        contestSelected(getContest());
    }

    private void showMessageInTable(String message) {
        table.setModel(new OneMessageTableModel(message));
    }

    public void deactivate() {
    }

    private ContestDescription getContest() {
        if (Controller.isContestUnknownMode()) return contestChoosingPanel.getContest(); else return Controller.getContestConnection().getContest();
    }

    private void contestSelected(ContestDescription contest) {
        if (contest == null) {
            showMessageInTable("Выберите соревнование");
            return;
        }
        try {
            GetContestResultsRequest crr = new GetContestResultsRequest();
            crr.contestID = contest.contestID;
            ContestConnection contestConnection = Controller.getContestConnection();
            crr.sessionID = Controller.getContestConnection() == null ? null : contestConnection.getSessionID();
            resResponse = Controller.getServer().doRequest(crr);
            ResultsTableModel model = new ResultsTableModel(resResponse.headers, resResponse.minorHeaders, resResponse.table);
            table.setModel(model);
        } catch (ServerReturnedError e) {
            showMessageInTable(e.getMessage());
        } catch (GeneralRequestFailureException e) {
        }
    }
}
