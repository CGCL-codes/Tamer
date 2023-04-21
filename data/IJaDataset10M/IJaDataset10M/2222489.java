package org.gocha.textbox;

import org.gocha.gui.TabbedFrame;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import org.gocha.gui.GuiFactory;
import org.gocha.gui.GuiNode;
import org.gocha.gui.GuiNodeTag;
import org.gocha.gui.GuiNodeUtil;
import org.gocha.gui.GuiTag;
import org.gocha.gui.GuiUtil;

/**
 * @author gocha
 */
public class TabbedMainFrame extends TabbedFrame {

    public TabbedMainFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("TextBox");
    }

    @Override
    protected Iterable<Component> createToolbarButtons() {
        GuiNode root = getHostRootGuiNode();
        GuiNodeUtil utl = new GuiNodeUtil();
        GuiNodeUtil.Expression e = GuiNodeUtil.exp();
        ArrayList<Component> result = new ArrayList<Component>();
        for (AbstractButton but : utl.createButtons(e.and(e.uidLike("/root/file/*"), e.not(e.uidLike("/root/file/saveas")), e.levelEquals(2)).select(root))) {
            result.add(but);
        }
        for (AbstractButton but : utl.createButtons(e.uidLike("/root/edit/*").select(root))) {
            result.add(but);
        }
        return result;
    }

    private TextBoxPanel createTextBoxPanel() {
        return new TextBoxPanel(TextBox.textBox().getTextIOConfig(), TextBox.textBox().getSyntaxes(), TextBox.textBox().getFlatTextConfig());
    }

    @GuiTag(path = { @GuiNodeTag(id = "root"), @GuiNodeTag(id = "file", name = "Файл"), @GuiNodeTag(id = "new", name = "Новый", order = -11, accelerator = "control N", smallIconResource = "document-new.png") })
    public void fileNew() {
        TextBoxPanel tbp1 = createTextBoxPanel();
        tbp1.setName("Без заголовка");
        getTabPane().getTabComponents().add(tbp1);
    }

    /**
     * Открывает диалог чтения файла
     */
    @GuiTag(path = { @GuiNodeTag(id = "root"), @GuiNodeTag(id = "file", name = "Файл"), @GuiNodeTag(id = "open", name = "Открыть ...", order = -10, accelerator = "control O", smallIconResource = "document-open.png") })
    public void fileOpen() {
        TextBoxPanel tbp1 = createTextBoxPanel();
        tbp1.setName("Без заголовка");
        getTabPane().getTabComponents().add(tbp1);
        tbp1.open();
    }

    @Override
    protected void onWindowClosed() {
        TextBox.textBox().fireExit(this);
        System.exit(0);
        super.onWindowClosed();
    }

    @Override
    protected void onWindowClosing() {
        super.onWindowClosing();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        GuiUtil.SetSystemLookAndFeel();
        GuiFactory fact = TextBox.textBoxFactory;
        TextBox.setFactory(fact);
        TextBox.setContext(TextBox.class);
        TextBox.instance().start(args);
        TextBox.textBox().postCreateInitialization();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TabbedMainFrame().setVisible(true);
            }
        });
    }
}
