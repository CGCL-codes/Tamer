package ee.ioc.cs.vsle.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import ee.ioc.cs.vsle.util.*;
import ee.ioc.cs.vsle.vclass.*;

/**
 * Menubar that contains buttons for choosing tools
 * and classes from the current package.
 */
public class Palette extends PaletteBase {

    protected Canvas canvas;

    public Palette(Canvas canv) {
        super();
        this.canvas = canv;
        init();
    }

    private void init() {
        toolBar = new JPanel();
        toolBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        toolBar.setOpaque(false);
        toolBar.setBorder(new EmptyBorder(2, 0, 2, 0));
        c.gridx = 0;
        toolBar.add(createToolPanel(), c);
        c.gridx = 2;
        toolBar.add(createZoomPanel(), c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, PANEL_SPACE.width, 0, PANEL_SPACE.width);
        toolBar.add(new ScrollableBar(createClassPanel(canvas.getPackage())), c);
        canvas.add(toolBar, BorderLayout.PAGE_START);
    }

    void reset() {
        canvas.remove(toolBar);
        init();
    }

    private JComponent createClassPanel(VPackage vPackage) {
        JPanel classPanel = new JPanel();
        classPanel.setOpaque(false);
        classPanel.setBorder(new EmptyBorder(2, 0, 2, 0));
        classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.LINE_AXIS));
        for (int i = 0; i < vPackage.getClasses().size(); i++) {
            PackageClass pClass = vPackage.getClasses().get(i);
            ImageIcon icon;
            if ("default.gif".equals(pClass.getIcon())) {
                icon = FileFuncs.getImageIcon("images/default.gif", false);
            } else {
                icon = FileFuncs.getImageIcon(canvas.getWorkDir() + pClass.getIcon(), true);
            }
            String actionCmd;
            if (pClass.getComponentType() == PackageClass.ComponentType.REL) {
                actionCmd = State.addRelObjPrefix + pClass.getName();
            } else {
                actionCmd = pClass.getName();
            }
            JToggleButton button = createButton(icon, pClass.getName() + " " + pClass.getDescription(), actionCmd);
            classPanel.add(button);
            if (i < vPackage.getClasses().size() - 1) {
                classPanel.add(Box.createRigidArea(BUTTON_SPACE));
            }
        }
        return classPanel;
    }

    private JComponent createToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.LINE_AXIS));
        toolPanel.setOpaque(false);
        toolPanel.setBorder(new EmptyBorder(2, 0, 2, 0));
        JToggleButton selection = createButton("images/mouse.gif", "Select / Drag", State.selection);
        selection.setSelected(true);
        toolPanel.add(selection);
        toolPanel.add(Box.createRigidArea(BUTTON_SPACE));
        JToggleButton relation = createButton("images/rel.gif", "Relation", State.addRelation);
        toolPanel.add(relation);
        return toolPanel;
    }

    @Override
    protected MouseListener getButtonMouseListener() {
        if (mouseListener == null) {
            mouseListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        int i = buttons.lastIndexOf(e.getSource());
                        if (i < 0) {
                            return;
                        }
                        String action = buttons.get(i).getActionCommand();
                        if (State.isAddObject(action) || State.isAddRelClass(action)) {
                            canvas.openClassCodeViewer(State.getClassName(action));
                        }
                    }
                }
            };
        }
        return mouseListener;
    }

    @Override
    protected ActionListener getZoomListener() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int i = ((JComboBox) e.getSource()).getSelectedIndex();
                canvas.setScale(ZOOM_LEVELS[i]);
            }
        };
    }

    @Override
    protected void setState(String state) {
        canvas.mListener.setState(state);
        canvas.drawingArea.grabFocus();
        canvas.drawingArea.repaint();
    }
}
