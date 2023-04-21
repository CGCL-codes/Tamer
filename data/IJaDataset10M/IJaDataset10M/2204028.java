package fr.vtt.gattieres.gcs.gui.challenger.print;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import fr.vtt.gattieres.gcs.data.RaceData;
import fr.vtt.gattieres.gcs.gui.challenger.ChallengerTableModel;
import fr.vtt.gattieres.gcs.resources.Resource;
import fr.vtt.gattieres.gcs.resources.ResourceManager;

public class JChallengerPageViewer extends JPageViewer {

    protected ResourceManager resMgr;

    protected Class cl;

    private PrintableTable view;

    private JFrame f;

    private int page;

    public JChallengerPageViewer(RaceData data, PageFormat printFormat, int page) {
        super(data, printFormat);
        resMgr = ResourceManager.getInstance();
        cl = Resource.class;
        initUI();
        this.page = page;
    }

    @Override
    protected void initUI() {
        ChallengerTableModel model = new ChallengerTableModel(data);
        view = new PrintableTable(model, data == null ? null : data.getTopPage(), resMgr.getString(cl, Resource.KEY_PRINT_PAGE_CHALLENGER_LABEL));
        for (int i = 0; i < view.getColumnModel().getColumnCount(); i++) {
            TableColumn col = view.getColumnModel().getColumn(i);
            col.setCellRenderer(new PrintTableCellRenderer());
        }
        view.getTableHeader().setBounds(0, 0, view.getColumnModel().getTotalColumnWidth(), (int) view.getTableHeader().getPreferredSize().getHeight());
        view.getTableHeader().setDefaultRenderer(new ChallengerHeaderRenderer());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        try {
            view.print(g2, super.printFormat, page - 1);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public void clean() {
        if (f != null) {
            f.dispose();
        }
    }

    public int getPrintCode() {
        return view.getPrintCode();
    }

    public int getPrintPageCount() {
        return view.getPrintPageCount((int) super.printFormat.getImageableHeight(), (int) super.printFormat.getImageableWidth());
    }

    public Printable getPrintable() {
        return this.view;
    }
}
