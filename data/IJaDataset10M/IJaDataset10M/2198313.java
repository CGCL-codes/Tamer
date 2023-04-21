package ops.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import ops.Application;
import ops.WindowManager;

public class ApplicationFrame extends JFrame {

    private MenuBar menuBar;

    private JPanel mainPanel;

    private CardLayout cardLayout;

    private LoginStatusPanel loginStatusPanel;

    public static ApplicationFrame getInstance() {
        return Application.getInstance().getWindowManager().getApplicationFrame();
    }

    public ApplicationFrame() {
        super("OPS!");
        init();
    }

    private void init() {
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new FrameListener());
        initLayout();
        initMenu();
        loadPanels();
    }

    private void loadPanels() {
        WindowManager wm = WindowManager.getInstance();
        for (Entry<String, JPanel> entry : wm.getPanels().entrySet()) {
            mainPanel.add(entry.getValue(), entry.getKey());
        }
        showPanel("login.login");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        WindowManager wm = WindowManager.getInstance();
        JPanel panel = wm.getPanel(panelName);
        if (panel instanceof Refreshable) {
            ((Refreshable) panel).refresh();
        }
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout(5, 5);
        mainPanel = new JPanel(cardLayout);
        loginStatusPanel = new LoginStatusPanel();
        add(loginStatusPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initMenu() {
        menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }

    private class FrameListener extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }

    public MenuBar menuBar() {
        return menuBar;
    }

    public LoginStatusPanel statusPanel() {
        return loginStatusPanel;
    }
}
