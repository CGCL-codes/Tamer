package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Di�logo para edici�n de nodos en la herramiento de �rbole de decisi�n
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ExpressionEditorDialog extends JPanel implements ButtonsPanelListener, IWindow, IWindowListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4471419877633981558L;

    private View view = null;

    private ExpressionEditorPanel expressionEditorPanel = null;

    private DecisionTreePanel decisionTreePanel = null;

    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
        m_viewinfo.setTitle(PluginServices.getText(this, "editor_expresiones"));
        m_viewinfo.setHeight(this.getHeight());
        m_viewinfo.setWidth(this.getWidth());
        return m_viewinfo;
    }

    public void actionButtonPressed(ButtonsPanelEvent e) {
        if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
            close();
        }
    }

    public ExpressionEditorDialog(int width, int height, DecisionTreePanel decisionTreePanel) {
        this.setSize(width, height);
        this.decisionTreePanel = decisionTreePanel;
        this.view = decisionTreePanel.getView();
        this.setLayout(new BorderLayout());
        this.add(getExpressionEditorPanel(), BorderLayout.CENTER);
    }

    public void windowActivated() {
    }

    public void windowClosed() {
    }

    /**
	 * Acciones a ejecutar cuando se cancela
	 */
    public void close() {
        try {
            PluginServices.getMDIManager().closeWindow(this);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public ExpressionEditorPanel getExpressionEditorPanel() {
        if (expressionEditorPanel == null) {
            expressionEditorPanel = new ExpressionEditorPanel(this, decisionTreePanel);
        }
        return expressionEditorPanel;
    }

    public DecisionTreePanel getDecisionTreePanel() {
        return decisionTreePanel;
    }

    public Object getWindowProfile() {
        return WindowInfo.DIALOG_PROFILE;
    }
}
