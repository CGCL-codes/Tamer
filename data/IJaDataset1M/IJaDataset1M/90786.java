package com.abiquo.virtualdisk;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.abiquo.guitools.SwingUtils;
import com.abiquo.virtualdisk.grid.VirtualDiskService;

/**
 * 
 * @author slizardo
 * 
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame {

    private VirtualDiskService virtualDiskService;

    private FilesListPanel filesListPanel;

    private DrivesPanel drivesPanel;

    public static void main(String[] args) {
        SwingUtils.setNativeLookAndFeel();
        MainGUI main = new MainGUI();
        SwingUtils.centerWindow(main);
        main.setVisible(true);
    }

    public MainGUI() {
        super();
        setAlwaysOnTop(true);
        setTitle("abiquo / Virtual disk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        virtualDiskService = new VirtualDiskService();
        try {
            virtualDiskService.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error has occured: " + e.getMessage());
            System.exit(1);
        }
        filesListPanel = new FilesListPanel(virtualDiskService);
        drivesPanel = new DrivesPanel(virtualDiskService, filesListPanel);
        organize();
    }

    private void organize() {
        add(drivesPanel, BorderLayout.CENTER);
        add(filesListPanel, BorderLayout.EAST);
        pack();
    }
}
