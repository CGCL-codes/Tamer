package com.jme3.gde.nmgen.wizard;

import com.jme3.gde.nmgen.NavMeshGenerator;
import javax.swing.JPanel;
import org.openide.WizardDescriptor;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;

public final class NewNavMeshVisualPanel1 extends JPanel {

    private PropertySheet ps;

    /** Creates new form NewNavMeshVisualPanel1 */
    public NewNavMeshVisualPanel1() {
        initComponents();
        ps = new PropertySheet();
        ps.setNodes(new Node[] {});
        add(ps);
    }

    @Override
    public String getName() {
        return "Specify Settings";
    }

    public void loadSettings(WizardDescriptor wiz) {
        NavMeshGenerator gen = (NavMeshGenerator) wiz.getProperty("generator");
        ps.setNodes(new Node[] { new NavMeshGeneratorNode(gen) });
    }

    public void saveSettings(WizardDescriptor wiz) {
    }

    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }
}
