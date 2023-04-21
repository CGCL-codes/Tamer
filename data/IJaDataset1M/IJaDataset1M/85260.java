package components.tabletask;

import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import components.calendar.theinterface.JPanelDay;
import db.model.Timeteble;
import forms.FormAddAndUpdateTebltask;

public class JPanelTaskColumn extends JPanel {

    private Timeteble timeteble;

    private final JPanel jPanel = new JPanel();

    private final JPanel panel_1 = new JPanel();

    private final JSplitPane splitPane = new JSplitPane();

    private JPanelTask panelTask;

    private JPanelDay panel;

    private final JPanel panel_2 = new JPanel();

    private final JPanel panel_3 = new JPanel();

    private final JPanel panel_4 = new JPanel();

    private final JPanel panel_5 = new JPanel();

    public JPanelTaskColumn(boolean bool, Timeteble timeteble) {
        if (bool == true) {
            this.timeteble = timeteble;
            panelTask = new JPanelTask(this.timeteble);
            getPanelTask().setTimeteble(timeteble);
            addHierarchyListener(new HierarchyListener() {

                @Override
                public void hierarchyChanged(HierarchyEvent arg0) {
                    repaint();
                }
            });
            setLayout(new GridLayout(0, 1, 0, 0));
            add(splitPane);
            panel = new JPanelDay(timeteble.getStartTimeTasca() + "") {

                @Override
                public void openDayTask() {
                }

                @Override
                public void setClor() {
                }

                @Override
                public void Entered() {
                }

                @Override
                public void Exited() {
                }

                @Override
                public void isOnClick() {
                }

                @Override
                public void dubleClick() {
                }
            };
            splitPane.setLeftComponent(panel);
            panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
            splitPane.setRightComponent(panel_1);
            setMigLayout();
            panel_1.add(panel_2, "2, 2, fill, fill");
            panel_2.setLayout(new GridLayout(1, 0, 0, 0));
            panel_1.add(panel_3, "4, 2, fill, fill");
            panel_3.setLayout(new GridLayout(1, 0, 0, 0));
            panel_1.add(panel_4, "6, 2, fill, fill");
            panel_5.setLayout(new GridLayout(1, 0, 0, 0));
            panel_1.add(panel_5, "8, 2, fill, fill");
            panel_5.setLayout(new GridLayout(1, 0, 0, 0));
            panel_5.add(panelTask);
            panelTask.addMouseListener(new MyMause());
            panelTask.addMouseMotionListener(new MyMuve());
        } else {
            panelTask = null;
        }
    }

    public void setMigLayout() {
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.GROWING_BUTTON_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.GROWING_BUTTON_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.GROWING_BUTTON_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.GROWING_BUTTON_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("min:grow") }));
    }

    public JPanel getPanel_1() {
        return panel_1;
    }

    public JPanel getPanel_2() {
        return panel_2;
    }

    public JPanel getPanel_3() {
        return panel_3;
    }

    public JPanel getPanel_4() {
        return panel_4;
    }

    public JPanel getPanel_5() {
        return panel_5;
    }

    public JPanel getPanel() {
        return jPanel;
    }

    public JPanelTask getPanelTask() {
        return panelTask;
    }

    public void setPanelTask(JPanelTask panelTask) {
        this.panelTask = panelTask;
    }

    public class MyMause extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            System.out.println("public void mousePressed(MouseEvent event)");
        }

        @Override
        public void mouseReleased(MouseEvent event) {
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            System.out.println("public void mouseClicked(MouseEvent event)");
            if (event.getClickCount() == 2) {
                FormAddAndUpdateTebltask addTebltask = new FormAddAndUpdateTebltask("update", timeteble);
                addTebltask.show();
                addTebltask.setSize(500, 500);
            }
        }
    }

    public class MyMuve implements MouseMotionListener {

        /**
		 * ����������� ������
		 */
        @Override
        public void mouseDragged(MouseEvent event) {
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }
    }

    public class MyM {

        public java.awt.Event d() {
            return new Event(panel_2, WHEN_IN_FOCUSED_WINDOW, getFont());
        }
    }
}
