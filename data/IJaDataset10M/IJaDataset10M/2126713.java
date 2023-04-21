package com.yeep.floatreader.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.yeep.basis.swing.widget.frame.TrayFrame;
import com.yeep.floatreader.Constants;
import com.yeep.floatreader.cfg.Configuration;
import com.yeep.floatreader.cfg.Settings;
import com.yeep.floatreader.hotkey.HotKeyHandler;
import com.yeep.floatreader.hotkey.HotKeyManager;
import com.yeep.floatreader.hotkey.JIntellitypeHotKeyManager;
import com.yeep.floatreader.model.Bookmark;
import com.yeep.floatreader.model.KeyHandler;
import com.yeep.floatreader.model.PageController;
import com.yeep.floatreader.model.RecentFile;
import com.yeep.floatreader.service.FloatReaderService;
import com.yeep.floatreader.service.FloatReaderServiceImpl;

@SuppressWarnings("serial")
public class MainFrame extends TrayFrame {

    private FloatReaderService floatReaderService = new FloatReaderServiceImpl();

    private Settings uiSettings;

    private MenuBar menuBar;

    private JLabel content;

    private JFileChooser fileChooser;

    private MouseDragListener frameMouseListener = new MouseDragListener();

    private HotKeyManager hotKeyManager = new JIntellitypeHotKeyManager();

    protected PageController pageController;

    private String charset = Constants.CHARSET_GBK;

    public MainFrame() {
        super();
        loadUI();
    }

    /**
	 * Load widgets
	 */
    private void loadUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.menuBar = new MenuBar(this);
        this.menuBar.addMouseMotionListener(new MouseAdapter() {
        });
        setJMenuBar(this.menuBar);
        initTray();
        initLayout();
        setTrayKey(KeyHandler.KEY_TRAY.getKeyCode());
        addMouseListener(this.frameMouseListener);
        addMouseMotionListener(this.frameMouseListener);
        addWindowListener(new WindowAdapter() {

            /**
			 * {@inheritDoc}
			 */
            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing");
            }

            /**
			 * {@inheritDoc}
			 */
            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed");
            }
        });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyHandler.KEY_PAGE_UP.getKeyCode()) {
                    renderPreviousPage();
                } else if (keyCode == KeyHandler.KEY_PAGE_DOWN.getKeyCode()) {
                    renderNextPage();
                } else if (keyCode == KeyEvent.VK_F2) {
                    openFile();
                } else if (keyCode == KeyEvent.VK_F3) {
                    openRecentFilesDialog();
                } else if (keyCode == KeyEvent.VK_F4) {
                    openBookmarkDialog();
                }
            }

            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ALT:
                        if (menuBar != null) {
                            menuBar.setVisible(!menuBar.isVisible());
                            if (menuBar.isVisible()) {
                                MainFrame.this.setSize(MainFrame.this.getWidth(), MainFrame.this.getHeight() + 30);
                            } else if (!menuBar.isVisible()) {
                                MainFrame.this.setSize(MainFrame.this.getWidth(), MainFrame.this.getHeight() - 30);
                            }
                        }
                        break;
                }
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                adjustOpacity(MainFrame.this, e.getWheelRotation());
            }
        });
        initUISettings(Configuration.getSettings());
        setVisible(true);
    }

    public void setTrayKey(Integer key) {
        if (hotKeyManager == null) return;
        hotKeyManager.registerHotKey(key, new HotKeyHandler() {

            @Override
            protected void handle() {
                MainFrame.this.switchTray();
            }
        });
    }

    /**
	 * initialize tray
	 */
    private void initTray() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("com/yeep/floatreader/resources/tray.gif"));
        PopupMenu popopMenu = new PopupMenu();
        MenuItem resumeMenu = new MenuItem("Resume");
        resumeMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fromTray();
            }
        });
        popopMenu.add(resumeMenu);
        MenuItem exitMenu = new MenuItem("Exit");
        exitMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fromTray();
                MainFrame.this.exit();
            }
        });
        popopMenu.add(exitMenu);
        TrayIcon trayIcon = new TrayIcon(icon.getImage(), "Float Reader", popopMenu);
        trayIcon.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    fromTray();
                }
            }
        });
        setTrayIcon(trayIcon);
    }

    /**
	 * Initialize ui properties
	 */
    private void initUISettings(Settings uiSettings) {
        this.uiSettings = uiSettings;
        setUndecorated(uiSettings.getWindowUndecorated());
        setAlwaysOnTop(uiSettings.getWindowAlwaysOnTop());
        setSize(uiSettings.getWindowWidth(), uiSettings.getWindowHeight());
        if (this.menuBar != null) this.menuBar.setVisible(uiSettings.getWindowShowMenu());
    }

    /**
	 * Create all widgets and add them to panel
	 */
    private void initLayout() {
        createWidgets();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(this.content, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
    }

    /**
	 * Create Widgets
	 */
    private void createWidgets() {
        this.content = new JLabel() {

            public int getWidth() {
                return super.getWidth() - 10;
            }

            public int getHeight() {
                return super.getHeight() - 5;
            }
        };
        this.content.setVerticalTextPosition(SwingConstants.CENTER);
        this.content.setHorizontalTextPosition(SwingConstants.CENTER);
        this.content.setFont(new Font("新宋体", Font.PLAIN, 15));
        this.fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        this.fileChooser.setFileFilter(filter);
    }

    /**
	 * Open a File and render the file
	 */
    protected void openFile() {
        if (this.fileChooser != null && this.fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fullFileName = this.fileChooser.getSelectedFile().getPath();
            if (this.fileChooser.getSelectedFile() != null) {
                this.pageController = loadFile(fullFileName);
                renderCurrentPage();
            }
        }
    }

    /**
	 * Reload page by the specific bookmark
	 * 
	 * @param bookmark
	 */
    protected void reloadByBookmark(Bookmark bookmark) {
        if (bookmark == null) return;
        try {
            this.pageController.reload(bookmark.getIndex());
            renderCurrentPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Reload page by the specific recent file
	 * 
	 * @param recentFile
	 */
    protected void reloadByRecentFile(RecentFile recentFile) throws IOException {
        if (recentFile == null) return;
        if (this.pageController == null) {
            try {
                this.pageController = new PageController(recentFile.getFilename(), this.charset, this.content.getWidth(), this.content.getHeight(), this.content.getFontMetrics(this.content.getFont()), recentFile.getIndex());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                displayErrors(new RuntimeException("File is not found!"));
            } catch (IOException e) {
                e.printStackTrace();
                displayErrors(new RuntimeException("File could not be loaded!"));
            }
        } else {
            this.pageController.reload(recentFile.getFilename(), recentFile.getIndex());
        }
        renderCurrentPage();
    }

    /**
	 * Set charset
	 * 
	 * @param newCharset
	 */
    public void setCharset(String newCharset) {
        this.charset = newCharset;
    }

    /**
	 * Apply the charset for the opened file
	 * 
	 * @param newCharset
	 */
    protected void applyCharset(String newCharset) {
        if (newCharset == null) return;
        setCharset(newCharset);
        try {
            this.pageController.applyCharset(newCharset);
            renderCurrentPage();
        } catch (IOException e) {
            e.printStackTrace();
            displayErrors(new RuntimeException("Error ocurrs while applying charset " + newCharset));
        }
    }

    /**
	 * Open the recent files dialog
	 */
    protected void openRecentFilesDialog() {
        new RecentFileDialog(this);
    }

    /**
	 * Open the bookmark dialog
	 */
    protected void openBookmarkDialog() {
        if (this.pageController != null) new BookmarkDialog(this);
    }

    /**
	 * Open the choose font dialog
	 */
    protected void openChooseFontDialog() {
        new ChooseFontDialog(this);
    }

    /**
	 * Open the bookmark dialog
	 */
    protected void openShortcutSettingDialog() {
        new ShortcutSettingDialog(this);
    }

    /**
	 * Change font of the display area
	 * 
	 * @param font
	 */
    protected void changeReaderFont(Font font) {
        this.content.setFont(font);
        try {
            this.pageController.reload(this.content.getWidth(), this.content.getHeight(), this.content.getFontMetrics(this.content.getFont()));
            renderCurrentPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Get font of the display area
	 * 
	 * @return
	 */
    protected Font getFontOfReader() {
        return this.content.getFont();
    }

    /**
	 * Load the content of the selected file
	 * 
	 * @param filename
	 */
    private PageController loadFile(String filename) {
        try {
            this.pageController = new PageController(filename, this.charset, this.content.getWidth(), this.content.getHeight(), this.content.getFontMetrics(this.content.getFont()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            displayErrors(new RuntimeException("File is not found!"));
        } catch (IOException e) {
            e.printStackTrace();
            displayErrors(new RuntimeException("File could not be loaded!"));
        }
        return this.pageController;
    }

    /**
	 * Render content for current page
	 */
    private void renderCurrentPage() {
        if (this.pageController == null || this.content == null) return;
        renderContent(this.pageController.currentPage().getPageContent());
    }

    /**
	 * Render content for next page
	 */
    private void renderNextPage() {
        if (this.pageController == null || this.content == null) return;
        renderContent(this.pageController.nextPage().getPageContent());
    }

    /**
	 * Render content for previous page
	 */
    private void renderPreviousPage() {
        if (this.pageController == null || this.content == null) return;
        renderContent(this.pageController.previousPage().getPageContent());
    }

    /**
	 * Render the content of the label
	 * 
	 * @param content
	 */
    private void renderContent(String content) {
        if (content != null && !"".equals(content)) this.content.setText(content);
    }

    /**
	 * Update recent files
	 */
    private void updateRecentFiles() {
        if (this.pageController == null) return;
        try {
            floatReaderService.updateRecentFiles(new RecentFile(this.pageController.getFileName(), this.pageController.getCurrentCharIndex()));
            this.menuBar.reloadRecentFileMenu();
        } catch (IOException e) {
            e.printStackTrace();
            displayErrors(new RuntimeException("Unknown error occurs!"));
        }
    }

    /**
	 * Adjust the Opacity of the window
	 * <p>
	 * 
	 * @param rotation
	 */
    private void adjustOpacity(Window window, int rotation) {
        float opacity = com.sun.awt.AWTUtilities.getWindowOpacity(window);
        float offset = rotation * 0.08f;
        System.out.println("1 rotation = " + rotation + ", offset = " + offset + ", opacity = " + opacity);
        if (opacity - offset <= 0.0f) opacity = 0.0f; else if (opacity - offset >= 1f) opacity = 1f; else opacity -= offset;
        System.out.println("2 opacity = " + opacity);
        com.sun.awt.AWTUtilities.setWindowOpacity(window, opacity);
    }

    /**
	 * This method will be invoked when mainframe is destoryed.
	 */
    protected void exit() {
        System.out.println("exit");
        updateRecentFiles();
        System.exit(0);
    }

    /**
	 * Display errors
	 */
    private void displayErrors(Exception exception) {
        if (exception != null) JOptionPane.showMessageDialog(this, exception.getMessage(), "OK", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * Listener for handle mouse dragging and mouse moving
	 */
    class MouseDragListener extends MouseAdapter {

        Point origin = new Point();

        boolean isDrag = false;

        boolean isResize = false;

        /** {@inheritDoc} */
        public void mousePressed(MouseEvent e) {
            if (e == null) return;
            origin.x = e.getX();
            origin.y = e.getY();
        }

        /** {@inheritDoc} */
        public void mouseDragged(MouseEvent e) {
            if (e == null) return;
            if (isResize) {
                resizeComponent(e);
            } else {
                moveComponent(e);
            }
        }

        /** {@inheritDoc} */
        public void mouseMoved(MouseEvent e) {
            if (e == null) return;
            int widthDiff = MainFrame.this.getWidth() - e.getX();
            int heightDiff = MainFrame.this.getHeight() - e.getY();
            isResize = false;
            if (widthDiff < 10 && widthDiff > -10 && heightDiff < 10 && heightDiff > -10) {
                isResize = true;
                MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            } else {
                isDrag = true;
                MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }

        /** {@inheritDoc} */
        public void mouseExited(MouseEvent e) {
            MainFrame.this.setCursor(Cursor.getDefaultCursor());
        }

        /** {@inheritDoc} */
        public void mouseReleased(MouseEvent e) {
            if (isResize && MainFrame.this.pageController != null) {
                try {
                    MainFrame.this.pageController.reload(MainFrame.this.content.getWidth(), MainFrame.this.content.getHeight(), MainFrame.this.content.getFontMetrics(MainFrame.this.content.getFont()));
                    MainFrame.this.renderCurrentPage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            MainFrame.this.setCursor(Cursor.getDefaultCursor());
        }

        /**
		 * Handler the resize operation
		 */
        private void resizeComponent(MouseEvent e) {
            if (e == null) return;
            MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            int originWidth = MainFrame.this.getWidth();
            int originHeight = MainFrame.this.getHeight();
            int widthOffset = e.getX() - originWidth;
            int heightOffset = e.getY() - originHeight;
            if ((originWidth <= uiSettings.getWindowWidth() && widthOffset < 0) || (originHeight <= uiSettings.getWindowHeight() && heightOffset < 0)) return;
            MainFrame.this.setSize(originWidth + widthOffset, originHeight + heightOffset);
            MainFrame.this.repaint();
        }

        /**
		 * Handler the moving operation
		 */
        private void moveComponent(MouseEvent e) {
            if (e == null) return;
            MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point locationPoint = MainFrame.this.getLocation();
            MainFrame.this.setLocation(locationPoint.x + e.getX() - origin.x, locationPoint.y + e.getY() - origin.y);
        }
    }
}
