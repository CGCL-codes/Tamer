package org.espenra.adhoc;

import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.espenra.gui.FileSystemPanel;
import org.espenra.threads.FileSystemReaderThread;
import org.espenra.threads.UpdateListLabelWithDirectoryNameThread;
import org.espenra.threads.UpdateListLabelsWithNumbersThread;
import org.junit.Ignore;

@Ignore
public class FileSystemPanelTest {

    private static FileSystemPanel m_panel = new FileSystemPanel();

    public static void main(String[] args) {
        InitJPanel();
        JLabel toBeUpdated = m_panel.GetLabelOfTotalCount();
        JLabel directoryLabel = m_panel.GetCurrentDirectory();
        JTable fileList = m_panel.GetFileList();
        FileSystemReaderThread fileReaderThread = new FileSystemReaderThread();
        fileReaderThread.SetRoot(new File("C://"));
        UpdateListLabelsWithNumbersThread listLabelsThread = new UpdateListLabelsWithNumbersThread();
        listLabelsThread.SetLabel(toBeUpdated);
        UpdateListLabelWithDirectoryNameThread directoryThread = new UpdateListLabelWithDirectoryNameThread();
        directoryThread.SetLabel(directoryLabel);
        fileReaderThread.start();
        listLabelsThread.start();
        ;
        directoryThread.start();
        try {
            fileReaderThread.join();
            listLabelsThread.Stop();
            directoryThread.Stop();
        } catch (Exception ex) {
        }
    }

    private static void InitJPanel() throws HeadlessException {
        m_panel.setVisible(true);
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        frame.add(m_panel);
        frame.setVisible(true);
    }
}
