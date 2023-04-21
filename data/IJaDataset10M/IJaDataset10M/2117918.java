package blueprint4j.gui.db;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import blueprint4j.db.DataException;
import blueprint4j.db.Entity;
import blueprint4j.db.FieldGroup;
import blueprint4j.db.VectorFieldGroup;
import blueprint4j.gui.Binder;
import blueprint4j.report.db.VectorFieldReportGroup;
import blueprint4j.utils.BindException;
import blueprint4j.utils.Log;

public class EntityWizard extends JDialog implements ActionListener {

    private WizardPane panes[] = null;

    private int index = 0;

    private Binder binder = new Binder();

    private JPanel struct_panel = new JPanel(), head_panel = new JPanel(), main_panel = new JPanel(), btn_panel = new JPanel();

    private JButton btn_prev = new JButton("Prev"), btn_next = new JButton("Next");

    private Boolean completed_ok = null;

    private Frame owner = null;

    public EntityWizard(Frame owner, String name, WizardPane panes[]) throws BindException, java.io.IOException {
        super(owner, name, true);
        init(name, panes);
    }

    public EntityWizard(JDialog owner, String name, WizardPane panes[]) throws BindException, java.io.IOException {
        super(owner, name, true);
        init(name, panes);
    }

    private void init(String name, WizardPane panes[]) throws BindException, java.io.IOException {
        this.panes = panes;
        struct_panel.setLayout(new BorderLayout());
        head_panel.setLayout(new BorderLayout());
        main_panel.setLayout(new BorderLayout());
        btn_panel.setLayout(new BorderLayout());
        btn_panel.add(btn_prev, BorderLayout.WEST);
        btn_panel.add(btn_next, BorderLayout.EAST);
        JLabel head_label = new JLabel(name) {

            public void paint(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g);
            }
        };
        head_label.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 40));
        head_panel.add(head_label, BorderLayout.CENTER);
        struct_panel.add(head_panel, BorderLayout.NORTH);
        struct_panel.add(main_panel, BorderLayout.CENTER);
        struct_panel.add(btn_panel, BorderLayout.SOUTH);
        btn_prev.addActionListener(this);
        btn_next.addActionListener(this);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure", "Are you sure", JOptionPane.YES_NO_OPTION) == 0) {
                    completed_ok = new Boolean(false);
                    setVisible(false);
                }
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(struct_panel, BorderLayout.CENTER);
    }

    public Entity show(Entity entity) throws DataException {
        try {
            index = 0;
            binder = new Binder();
            for (int i = 0; i < panes.length; i++) {
                panes[i].setEntity(entity);
                panes[i].build(binder);
            }
            build();
            setVisible(true);
            for (; completed_ok == null; ) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
            if (completed_ok.booleanValue()) {
                return entity;
            } else {
                return null;
            }
        } catch (Throwable th) {
            throw new DataException(th);
        }
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btn_prev) {
            index--;
        }
        if (event.getSource() == btn_next) {
            index++;
        }
        build();
        repaint();
    }

    private void build() {
        try {
            btn_prev.setEnabled(index > 0);
            if (index < panes.length - 1) {
                btn_next.setText("Next");
            } else {
                btn_next.setText("Done");
            }
            if (index >= panes.length) {
                binder.save();
                completed_ok = new Boolean(true);
                setVisible(false);
            } else {
                main_panel.removeAll();
                main_panel.add(panes[index].getPanel(), BorderLayout.CENTER);
                pack();
                setLocationRelativeTo(owner);
            }
        } catch (Throwable th) {
            Log.debug.out(th);
            JOptionPane.showMessageDialog(this, th.getMessage());
        }
    }

    public static class WizardPane {

        private String wizard_name = null;

        private Entity entity = null;

        private VectorFieldGroup groups = null;

        private JPanel container = new JPanel();

        private DataPanelEntity panel = null;

        public WizardPane(String wizard_name, FieldGroup groups[]) {
            this.wizard_name = wizard_name;
            this.entity = entity;
            this.groups = new VectorFieldGroup(groups);
        }

        public void build(Binder binder) throws BindException, java.io.IOException {
            container.removeAll();
            panel = new DataPanelEntity(wizard_name, groups, new VectorFieldReportGroup());
            panel.setBinderBindable(binder, entity);
            container.add(panel);
        }

        public void setEntity(Entity entity) {
            groups = entity.getFieldGroups().mapGroup(groups.toArray());
            this.entity = entity;
        }

        public JPanel getPanel() {
            return container;
        }
    }

    public static WizardPane[] toWizardPane(String wizard_name, Entity entity) {
        WizardPane[] pane = new WizardPane[entity.getFieldGroups().size()];
        for (int i = 0; i < entity.getFieldGroups().size(); i++) {
            pane[i] = new WizardPane(wizard_name, new FieldGroup[] { entity.getFieldGroups().get(i) });
        }
        return pane;
    }
}
