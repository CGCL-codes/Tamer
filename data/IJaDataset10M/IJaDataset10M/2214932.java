package druid.dialogs.jdbc.entityselector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.TButton;
import org.dlib.gui.TDialog;
import org.dlib.gui.flextable.FlexTableColumn;
import druid.core.jdbc.RecordList;
import druid.util.jdbc.RecordListPanel;

public class EntitySelector extends TDialog implements ActionListener {

    private static RecordList rl;

    private TButton tbImport = new TButton("Import", "import", this);

    public EntitySelector(Frame frame) {
        super(frame, "Choose Entities to import", true);
        buildRL();
        RecordListPanel rlp = new RecordListPanel();
        rlp.refresh(rl);
        rlp.setEditable(true);
        JPanel p = new JPanel();
        FlexLayout flexL = new FlexLayout(1, 2, 4, 4);
        flexL.setColProp(0, FlexLayout.EXPAND);
        flexL.setRowProp(0, FlexLayout.EXPAND);
        p.setLayout(flexL);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        p.add("0,0,x,x", rlp);
        p.add("0,1,c", tbImport);
        getContentPane().add(p, BorderLayout.CENTER);
        p.setPreferredSize(new Dimension(250, 150));
        showDialog();
    }

    private void buildRL() {
        if (rl != null) return;
        FlexTableColumn ftc;
        rl = new RecordList();
        rl.addColumn("Entity", 200);
        ftc = rl.addColumn("Import", 100);
        ftc.setEditable(true);
        rl.newRecord();
        rl.addToRecord("Tables");
        rl.addToRecord(Boolean.TRUE);
        rl.newRecord();
        rl.addToRecord("Views");
        rl.addToRecord(Boolean.TRUE);
        rl.newRecord();
        rl.addToRecord("Procedures");
        rl.addToRecord(Boolean.TRUE);
        rl.newRecord();
        rl.addToRecord("Functions");
        rl.addToRecord(Boolean.TRUE);
        rl.newRecord();
        rl.addToRecord("Sequences");
        rl.addToRecord(Boolean.TRUE);
    }

    private boolean getBoolValue(int pos) {
        return ((Boolean) rl.getRecordAt(pos).elementAt(1)).booleanValue();
    }

    public boolean importTables() {
        return getBoolValue(0);
    }

    public boolean importViews() {
        return getBoolValue(1);
    }

    public boolean importProcedures() {
        return getBoolValue(2);
    }

    public boolean importFunctions() {
        return getBoolValue(3);
    }

    public boolean importSequences() {
        return getBoolValue(4);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("import")) hide();
    }
}
