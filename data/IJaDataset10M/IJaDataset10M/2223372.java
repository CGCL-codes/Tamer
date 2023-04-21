package ctags.sidekick.options;

import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import sidekick.ModeOptionPane;
import sidekick.ModeOptionPaneController;
import sidekick.ModeOptionPaneController.ModeOptionPaneDelegate;
import ctags.sidekick.FilterManager;
import ctags.sidekick.IconProviderManager;
import ctags.sidekick.MapperManager;
import ctags.sidekick.SorterManager;
import ctags.sidekick.TextProviderManager;

@SuppressWarnings("serial")
public class ModeOptionsPane extends JPanel implements ModeOptionPane, ModeOptionPaneDelegate {

    ModeOptionPaneController controller;

    Vector<ModeOptionPaneDelegate> subPanes;

    public ModeOptionsPane() {
        controller = new ModeOptionPaneController(this);
        subPanes = new Vector<ModeOptionPaneDelegate>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        CtagsCmdOptionsPane invocationPane = new CtagsCmdOptionsPane();
        invocationPane.setMaximumSize(invocationPane.getPreferredSize());
        invocationPane.setAlignmentX(LEFT_ALIGNMENT);
        add(invocationPane);
        subPanes.add(invocationPane);
        JPanel optionPanes = new JPanel(new GridLayout(1, 0));
        optionPanes.setAlignmentX(LEFT_ALIGNMENT);
        add(optionPanes);
        ObjectProcessorListEditor mapperPane = new ObjectProcessorListEditor(MapperManager.getInstance());
        optionPanes.add(mapperPane);
        subPanes.add(mapperPane);
        ObjectProcessorListEditor sorterPane = new ObjectProcessorListEditor(SorterManager.getInstance());
        optionPanes.add(sorterPane);
        subPanes.add(sorterPane);
        ObjectProcessorListEditor filterPane = new ObjectProcessorListEditor(FilterManager.getInstance());
        optionPanes.add(filterPane);
        subPanes.add(filterPane);
        optionPanes = new JPanel(new GridLayout(1, 0));
        optionPanes.setAlignmentX(LEFT_ALIGNMENT);
        add(optionPanes);
        ObjectProcessorListEditor textProviderPane = new ObjectProcessorListEditor(TextProviderManager.getInstance());
        optionPanes.add(textProviderPane);
        subPanes.add(textProviderPane);
        ObjectProcessorListEditor iconProviderPane = new ObjectProcessorListEditor(IconProviderManager.getInstance());
        optionPanes.add(iconProviderPane);
        subPanes.add(iconProviderPane);
    }

    public void modeSelected(String mode) {
        controller.modeSelected(mode);
    }

    public void save() {
        controller.save();
    }

    public void cancel() {
        controller.cancel();
    }

    public void setUseDefaults(boolean b) {
        controller.setUseDefaults(b);
    }

    public boolean getUseDefaults(String mode) {
        return controller.getUseDefaults(mode);
    }

    public Object createModeProps(String mode) {
        Vector<Object> props = new Vector<Object>();
        for (int i = 0; i < subPanes.size(); i++) props.add(subPanes.get(i).createModeProps(mode));
        return props;
    }

    public JComponent getUIComponent() {
        return this;
    }

    public void resetModeProps(String mode) {
        for (int i = 0; i < subPanes.size(); i++) subPanes.get(i).resetModeProps(mode);
    }

    @SuppressWarnings("unchecked")
    public void saveModeProps(String mode, Object props) {
        Vector<Object> v = (Vector<Object>) props;
        for (int i = 0; i < subPanes.size(); i++) subPanes.get(i).saveModeProps(mode, v.get(i));
    }

    @SuppressWarnings("unchecked")
    public void updatePropsFromUI(Object props) {
        Vector<Object> v = (Vector<Object>) props;
        for (int i = 0; i < subPanes.size(); i++) subPanes.get(i).updatePropsFromUI(v.get(i));
    }

    @SuppressWarnings("unchecked")
    public void updateUIFromProps(Object props) {
        Vector<Object> v = (Vector<Object>) props;
        for (int i = 0; i < subPanes.size(); i++) subPanes.get(i).updateUIFromProps(v.get(i));
    }

    public boolean hasModeProps(String mode) {
        for (int i = 0; i < subPanes.size(); i++) if (subPanes.get(i).hasModeProps(mode)) return true;
        return false;
    }
}
