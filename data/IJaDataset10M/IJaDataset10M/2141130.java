package frostcode.icetasks.gui.info;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import frostcode.icetasks.Settings;
import frostcode.icetasks.gui.BaseDialog;
import frostcode.icetasks.i18n.I18n;

@Singleton
public class LogInfoDialog extends BaseDialog {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(LogInfoDialog.class);

    private final JTextArea contentArea;

    private StringBuilder logContent;

    @Inject
    public LogInfoDialog(final I18n i18n, final Settings settings) {
        super("icons/information.png", settings);
        setTitle(i18n.get("menu.info.log"));
        setLayout(new MigLayout("wrap 1, fill"));
        contentArea = new JTextArea(20, 200);
        contentArea.setEditable(false);
        add(new JScrollPane(contentArea), "width 600lp, grow");
        JButton copyToClipboard = new JButton(i18n.get("menu.info.system.clipboard"));
        add(copyToClipboard, "split 2, wmin pref+10px");
        JButton ok = new JButton(i18n.get("dialog.ok"));
        add(ok, "tag ok");
        getRootPane().setDefaultButton(ok);
        copyToClipboard.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection contents = new StringSelection(logContent.toString());
                sysClip.setContents(contents, contents);
            }
        });
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        pack();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            logContent = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator + ".icetasks.log"));
                String line = null;
                while ((line = reader.readLine()) != null) logContent.append(line).append("\n");
                reader.close();
            } catch (IOException e) {
                log.error("Cannot read log file", e);
                logContent.append("[error reading file]");
            }
            contentArea.setText(logContent.toString());
        }
        super.setVisible(visible);
    }
}
