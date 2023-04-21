package praktikumid.k11.p16b;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Frame extends JFrame {

    private DrawingPanel dp;

    private HashMap<Integer, Circle> circles = new HashMap<Integer, Circle>();

    public Frame() {
        setLocation(100, 100);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponentListener(new FrameListener());
        JPanel toolbar = new JPanel();
        toolbar.setBackground(Color.BLUE);
        String data[] = { "essa", "tessa", "kossa", "nossa", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d" };
        JList list = new JList(data);
        data[0] = "aa";
        list.setListData(data);
        JScrollPane p = new JScrollPane(list);
        p.setPreferredSize(new Dimension(100, 100));
        p.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ListSelectionModel lsm = list.getSelectionModel();
        lsm.addListSelectionListener(new ListListener());
        toolbar.add(p);
        add(toolbar, BorderLayout.NORTH);
        DrawingPanel dp = new DrawingPanel(this);
        this.dp = dp;
        add(dp, BorderLayout.CENTER);
    }

    public HashMap<Integer, Circle> getCircles() {
        return circles;
    }

    private class FrameListener implements ComponentListener {

        @Override
        public void componentHidden(ComponentEvent arg0) {
        }

        @Override
        public void componentMoved(ComponentEvent arg0) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            System.out.println("resized");
            Frame f = Frame.this;
        }

        @Override
        public void componentShown(ComponentEvent arg0) {
        }
    }

    private class ListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Frame f = Frame.this;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                int min = lsm.getMinSelectionIndex();
                int max = lsm.getMaxSelectionIndex();
                f.circles.clear();
                for (int i = min; i <= max; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        System.out.println("selected " + i);
                        Circle c = new Circle(50, 50, 10 + i * 5, 10 + i * 5, f.dp.getWidth(), f.dp.getHeight(), f.dp);
                        f.circles.put(i, c);
                    }
                }
                f.dp.repaint();
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Frame f = new Frame();
        f.setVisible(true);
    }
}
