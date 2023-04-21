package com.nullfish.app.jfd2.viewer;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.JFDComponent;
import com.nullfish.app.jfd2.ui.container2.ContainerPosition;
import com.nullfish.app.jfd2.ui.container2.JFDOwner;
import com.nullfish.app.jfd2.ui.container2.TitleUpdater;
import com.nullfish.app.jfd2.viewer.action.CloseAction;
import com.nullfish.lib.keymap.KeyStrokeMap;

public class FileViewerContainerPanel extends JPanel implements JFDComponent {

    private JFDOwner owner;

    private TitleUpdater updater;

    private FileViewer viewer;

    private JFD jfd;

    public static final String PARAM_CONSTRAINTS = "constraints";

    public static final String CLOSE = "close";

    public FileViewerContainerPanel(FileViewer viewer) {
        this.viewer = viewer;
        CloseAction closeAction = new CloseAction(viewer);
        getActionMap().put(CLOSE, closeAction);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStrokeMap.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CLOSE);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStrokeMap.getKeyStroke(KeyEvent.VK_ENTER, 0), CLOSE);
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStrokeMap.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CLOSE);
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStrokeMap.getKeyStroke(KeyEvent.VK_ENTER, 0), CLOSE);
    }

    public void setTitleUpdater(TitleUpdater updater) {
        this.updater = updater;
    }

    public void open(JFD jfd) {
        this.jfd = jfd;
        JFDOwner owner = jfd.getJFDOwner();
        ContainerPosition pos = getConstraints().getPosition(jfd);
        owner.addComponent(this, pos, updater);
        owner.setActiveComponent(this);
    }

    public void close() {
        if (jfd != null) {
            JFDOwner owner = jfd.getJFDOwner();
            if (owner != null) {
                owner.setActiveComponent(jfd);
            }
        }
        JFDOwner owner = getJFDOwner();
        if (owner != null) {
            owner.removeComponent(this);
        }
    }

    public void setOwner(JFDOwner owner) {
        this.owner = owner;
    }

    public JFDOwner getJFDOwner() {
        return owner;
    }

    public Component getComponent() {
        return this;
    }

    private FileViewerConstraints getConstraints() {
        return FileViewerConstraints.getInstance((String) viewer.getParam(PARAM_CONSTRAINTS));
    }

    public void dispose() {
    }

    public FileViewer getViewer() {
        return viewer;
    }

    public void focusChanged() {
    }
}
