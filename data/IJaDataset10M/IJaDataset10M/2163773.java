package pobr;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import pobr.filters.Filter;
import pobr.filters.FilterInstaller;

public class ImageView extends JInternalFrame implements InternalFrameListener, ActionListener {

    static final Dimension START_SIZE = new Dimension(300, 300);

    static final int PROCESS_MENU_POSITION = 1;

    JScrollPane scroll = new JScrollPane();

    ImagePanel imagePanel;

    MainWindow mWindow;

    Image2Process image;

    JMenu menu = new JMenu("Process");

    Hashtable filters;

    MouseMotionAdapter move = new MouseMotionAdapter() {

        @Override
        public void mouseMoved(MouseEvent e) {
            try {
                setTitle(e.getX() + "," + e.getY() + "  R:" + image.getRed(e.getX(), e.getY()) + "G:" + image.getGreen(e.getX(), e.getY()) + "B:" + image.getBlue(e.getX(), e.getY()));
            } catch (Exception e1) {
            }
        }
    };

    public void actionPerformed(ActionEvent e) {
        String sel = e.getActionCommand();
        Filter filter = (Filter) (filters.get(sel));
        filter.process(image);
        imagePanel.repaint();
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        System.out.println("Image activated");
        mWindow.getJMenuBar().add(menu, PROCESS_MENU_POSITION);
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
        System.out.println("Image deactivated");
        mWindow.getJMenuBar().remove(menu);
        mWindow.getJMenuBar().revalidate();
    }

    public void internalFrameClosing(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public ImageView(String imageName, Image2Process image, MainWindow mWin) {
        super(imageName + " (" + image.getWidth() + "," + image.getHeight() + ")", true, true, true, true);
        this.image = image;
        this.mWindow = mWin;
        setPreferredSize(START_SIZE);
        imagePanel = new ImagePanel(this);
        imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        scroll.setViewportView(imagePanel);
        getContentPane().add(scroll);
        addInternalFrameListener(this);
        imagePanel.addMouseMotionListener(move);
        filters = FilterInstaller.install(menu, this);
    }

    public void showMe() {
        pack();
        setVisible(true);
    }

    public Image2Process getImage2Process() {
        return image;
    }

    public void setImage2Process(Image2Process image) {
        this.image = image;
    }
}
