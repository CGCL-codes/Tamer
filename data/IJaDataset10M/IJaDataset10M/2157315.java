package coollemon.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.birosoft.liquid.LiquidLookAndFeel;
import coollemon.dataBase.DataManager;
import coollemon.kernel.ContactManager;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.net.*;
import java.util.Vector;

public class MainFrame extends JFrame implements ActionListener, WindowListener {

    JFrame m;

    private ContactManager mainManager;

    public static int w = 900, h = 620;

    public static int Mx = 0, My = 0, Mw = 1280, Mh = 20;

    public static Rectangle ReMyM = new Rectangle(Mx, My, Mw, Mh);

    public static int Dx = 400, Dy = 20;

    public InfoPane info = new InfoPane();

    public JPanel north = new JPanel(new BorderLayout());

    public JToolBar search = new JToolBar();

    public JTextField jtf = new JTextField(16);

    public JButton searchButton;

    public AllUser alluser = new AllUser();

    public User user = alluser.all.get(0);

    private MainFrame() {
        String au = TestFile.getString();
        ;
        File f = null;
        try {
            f = new File(au);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.alluser = (AllUser) ois.readObject();
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mainManager = ContactManager.getManager();
        searchButton = new JButton(new ImageIcon(MainFrame.class.getResource("/coollemon/res/png-0095.png")));
        searchButton.addActionListener(this);
        this.setSize(MainFrame.w, MainFrame.h);
        this.setTitle("phoneMe");
        search.add(jtf);
        search.add(searchButton);
        north.add(MyMenu.getInstance(), BorderLayout.NORTH);
        north.add(new MyToolBar(), BorderLayout.CENTER);
        north.add(search, BorderLayout.EAST);
        this.add("North", north);
        this.add("East", info);
        JMenuBar jmb = new JMenuBar();
        jmb.add(new JMenu("coollemon"));
        this.add("South", jmb);
        this.add("Center", ContactChooser.getInstance());
        this.addWindowListener(this);
        this.setVisible(true);
    }

    private static MainFrame instance;

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            LiquidLookAndFeel.setLiquidDecorations(true);
            MainFrame c = MainFrame.getInstance();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            c.setLocation((d.width - c.getWidth()) / 2, (d.height - c.getHeight()) / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.searchButton) {
            String name = this.jtf.getText();
            if ((name != null) && (name != "")) ContactChooser.getInstance().right.update(ContactManager.getManager().findByName(name));
            MainFrame.getInstance().update();
        }
    }

    public void update() {
        ContactChooser.getInstance().left.update();
        ContactChooser.getInstance().right.update();
        DataView.getInstance().myupdate(null);
        LabelView.getInstance().myupdate(null);
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        String au = TestFile.getString();
        try {
            File f = new File(au);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.alluser);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        String au = TestFile.getString();
        try {
            File f = new File(au);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.alluser);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataManager.dataManager.writeFile(ContactManager.getManager(), user.filepath.getAbsolutePath() + "\\data.xml");
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }
}
