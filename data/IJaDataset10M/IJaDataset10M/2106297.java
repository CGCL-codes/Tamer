package foldTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;

@SuppressWarnings("serial")
public class OptionPane extends AbstractOptionPane {

    public static final String MESSAGE = "messages.foldTools.";

    public static final String PROP = "props.foldTools.";

    private JSpinner before, after, delay;

    private DefaultListModel handlerModel;

    private JList handlers;

    private JButton addHandler, removeHandler, editHandler;

    public OptionPane() {
        super("foldTools");
        setBorder(new EmptyBorder(5, 5, 5, 5));
        addSeparator();
        addComponent(new JLabel(getMessage("foldContext.label")));
        before = getContextLinesUi(getLinesBefore());
        addComponent(getMessage("linesBefore"), before);
        after = getContextLinesUi(getLinesAfter());
        addComponent(getMessage("linesAfter"), after);
        delay = new JSpinner(new SpinnerNumberModel(getFollowCaretDelay(), 0, 5000, 50));
        addComponent(getMessage("followCaretDelay"), delay);
        addSeparator();
        addComponent(new JLabel(getMessage("compositeHandlers")));
        handlerModel = new DefaultListModel();
        handlers = new JList(handlerModel);
        handlers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addComponent(new JScrollPane(handlers));
        JPanel p = new JPanel();
        addHandler = new JButton(getMessage("addHandler"));
        p.add(addHandler);
        removeHandler = new JButton(getMessage("removeHandler"));
        p.add(removeHandler);
        editHandler = new JButton(getMessage("editHandler"));
        p.add(editHandler);
        addComponent(p);
        addHandler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addHandler();
            }
        });
        removeHandler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeHandler();
            }
        });
        editHandler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editHandler();
            }
        });
        loadModes(new LoadedModeProcessor() {

            public void process(HandlerItem handler) {
                handlerModel.addElement(handler);
            }
        });
    }

    private String getMessage(String prop) {
        return jEdit.getProperty(MESSAGE + prop);
    }

    private void addHandler() {
        CompositeHandlerDialog d = new CompositeHandlerDialog(jEdit.getActiveView());
        d.setVisible(true);
        if (d.wasCancelled()) return;
        handlerModel.addElement(d.getHandler());
    }

    private void removeHandler() {
        int i = handlers.getSelectedIndex();
        if (i >= 0) handlerModel.remove(i);
    }

    private void editHandler() {
        int i = handlers.getSelectedIndex();
        if (i < 0) return;
        CompositeHandlerDialog d = new CompositeHandlerDialog(jEdit.getActiveView(), (HandlerItem) handlerModel.get(i));
        d.setVisible(true);
        if (d.wasCancelled()) return;
        handlerModel.set(i, d.getHandler());
    }

    @Override
    public void _save() {
        setLinesBefore(before);
        setLinesAfter(after);
        jEdit.setIntegerProperty(PROP + "followCaretDelay", Integer.valueOf((Integer) delay.getValue()));
        saveModes();
    }

    public interface LoadedModeProcessor {

        void process(HandlerItem handler);
    }

    public static void loadModes(LoadedModeProcessor processor) {
        int num = jEdit.getIntegerProperty(PROP + "compositeModes");
        for (int i = 0; i < num; i++) {
            String base = PROP + "compositeMode." + i + ".";
            HandlerItem handler = HandlerItem.load(base);
            if (handler != null) processor.process(handler);
        }
    }

    private void saveModes() {
        jEdit.setIntegerProperty(PROP + "compositeModes", handlerModel.getSize());
        for (int i = 0; i < handlerModel.getSize(); i++) {
            HandlerItem handler = (HandlerItem) handlerModel.get(i);
            handler.save(PROP + "compositeMode." + i + ".");
            handler.createService();
        }
    }

    public static JSpinner getContextLinesUi(int value) {
        return new JSpinner(new SpinnerNumberModel(value, 1, 10, 1));
    }

    public static int getLinesBefore() {
        return jEdit.getIntegerProperty(PROP + "linesBefore", 1);
    }

    public static void setLinesBefore(JSpinner spinner) {
        jEdit.setIntegerProperty(PROP + "linesBefore", ((Integer) spinner.getValue()).intValue());
    }

    public static int getLinesAfter() {
        return jEdit.getIntegerProperty(PROP + "linesAfter", 1);
    }

    public static void setLinesAfter(JSpinner spinner) {
        jEdit.setIntegerProperty(PROP + "linesAfter", ((Integer) spinner.getValue()).intValue());
    }

    public static int getFollowCaretDelay() {
        return jEdit.getIntegerProperty(PROP + "followCaretDelay", 500);
    }
}
