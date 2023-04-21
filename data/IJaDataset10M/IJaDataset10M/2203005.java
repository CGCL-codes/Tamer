package net.sf.nodeInsecure.core.components;

import com.wittams.gritty.RequestOrigin;
import com.wittams.gritty.ResizePanelDelegate;
import com.wittams.gritty.swing.GrittyTerminal;
import com.wittams.gritty.swing.TermPanel;
import net.sf.nodeInsecure.common.ExecutionContext;
import net.sf.nodeInsecure.core.Desktop;
import static net.sf.nodeInsecure.core.common.ResourceManager.getMessage;
import net.sf.nodeInsecure.core.components.internals.CommandInterpreter;
import net.sf.nodeInsecure.core.components.internals.NodeInsecureTty;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@SuppressWarnings("serial")
@ComponentDetails(iconPath = "/images/terminal.png", label = "Terminal", singleton = false, subMenu = "/Programs")
public class Terminal extends DesktopComponent {

    private GrittyTerminal terminal;

    PipedInputStream sysIn, termIn;

    PipedOutputStream sysOut, termOut;

    public Terminal(Desktop desktop, ExecutionContext context) throws IOException {
        super(getMessage("internalFrameTitle.terminal"), desktop, 600, 300, context);
        sysIn = new PipedInputStream();
        sysOut = new PipedOutputStream();
        termIn = new PipedInputStream(sysOut);
        termOut = new PipedOutputStream(sysIn);
    }

    private void sizeFrameForTerm(final JInternalFrame frame) {
        Dimension d = terminal.getPreferredSize();
        d.width += frame.getWidth() - frame.getContentPane().getWidth();
        d.height += frame.getHeight() - frame.getContentPane().getHeight();
        frame.setSize(d);
    }

    protected void doPostLaunchOperations() {
        super.doPostLaunchOperations();
        terminal = new GrittyTerminal(new CommandInterpreter(), context);
        TermPanel termPanel = terminal.getTermPanel();
        termPanel.setVisible(true);
        setVisible(true);
        setBounds(100, 100, 500, 300);
        sizeFrameForTerm(this);
        getContentPane().add("Center", terminal);
        pack();
        termPanel.setVisible(true);
        setVisible(true);
        final JInternalFrame thisFrame = this;
        setResizable(true);
        termPanel.setResizePanelDelegate(new ResizePanelDelegate() {

            public void resizedPanel(final Dimension pixelDimension, final RequestOrigin origin) {
                if (origin == RequestOrigin.Remote) sizeFrameForTerm(thisFrame);
            }
        });
        final NodeInsecureTty tty = new NodeInsecureTty(termIn, termOut);
        terminal.setTty(tty);
        terminal.start();
    }
}
