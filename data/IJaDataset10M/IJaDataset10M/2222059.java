package com.nexirius.framework.dataeditor;

import com.nexirius.framework.datamodel.*;
import com.nexirius.framework.dataviewer.DataViewer;
import com.nexirius.framework.textselector.TextSelector;
import com.nexirius.framework.textselector.TextSelectorManager;
import com.nexirius.util.StringVector;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringTokenizer;

/**
 * This class is used to display a button for a simple model which in turn is used to select a
 * value for the simple model from a list (see TextSelector)
 * This class creates a tool tip like completion tip for the target SimpleModel instance (method create tipComponent)
 */
public class SelectorEditor extends DataViewer {

    public static final String s_viewername = "SelectorEditor";

    DataModelCommand command;

    /**
     * Creates a new SelectorEditor which is will be displayed as a JButton
     */
    public SelectorEditor(SelectorModel model) {
        super(model);
        init();
    }

    private void init() {
        SimpleModel target = getSelectorModel().getTarget();
        if (target == null) {
            return;
        }
        target.addDataModelListener(new TargetListener());
        command = getSelectorModel().getPopupCommand();
    }

    public SelectorModel getSelectorModel() {
        return (SelectorModel) getDataModel();
    }

    public String getTargetText() {
        SimpleModel target = getSelectorModel().getTarget();
        if (target == null) {
            return "";
        }
        return target.getText();
    }

    /**
     * Creates the actual Swing JButton
     */
    public void create() {
        try {
            setJComponent(factory.createViewer(command, true).getJComponent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Empty implementation
     */
    public void update() {
    }

    /**
     * Only for debugging
     */
    public String getViewerName() {
        return s_viewername;
    }

    public TextSelector getTextSelector() {
        return TextSelectorManager.getInstance().getTextSelector(getSelectorModel().getTextSelectorName());
    }

    public TipComponent createTipComponent(StringVector tipText, boolean hasMore) {
        return new TipComponent(tipText, hasMore);
    }

    class TipComponent extends JPanel {

        StringVector tipText;

        int selected = 0;

        boolean hasMore;

        public TipComponent(StringVector tipText, boolean hasMore) {
            super(new GridLayout(1, 1));
            this.tipText = tipText;
            this.selected = 0;
            this.hasMore = hasMore;
            init();
        }

        private void init() {
            if (hasMore) {
                tipText.append("...");
            }
            setOpaque(true);
            setBorder(new BevelBorder(BevelBorder.LOWERED));
            update();
        }

        public void increment() {
            setSelectedIndex(getSelectedIndex() + 1);
        }

        public void decrement() {
            setSelectedIndex(getSelectedIndex() - 1);
        }

        public int getSelectedIndex() {
            return selected;
        }

        public String getSelected() {
            if (hasMore && selected == tipText.size() - 1) {
                return null;
            }
            String ret = tipText.getItem(selected);
            int index = ret.indexOf(';');
            if (index >= 0) {
                ret = ret.substring(0, index);
            }
            return ret;
        }

        public int setSelectedIndex(int n) {
            if (n < 0) {
                n = tipText.size() - 1;
            }
            selected = n % tipText.size();
            update();
            return selected;
        }

        public void update() {
            JPanel panel = new JPanel(new GridBagLayout());
            int i = 0;
            int columns = 1;
            panel.setOpaque(true);
            GridBagConstraints constr = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
            for (String s = tipText.firstItem(); s != null; s = tipText.nextItem()) {
                if (i == 0) {
                    if (s.indexOf(';') > 0) {
                        columns = new StringTokenizer(s, ";").countTokens();
                    }
                }
                String label[] = new String[columns];
                if (columns > 1) {
                    StringTokenizer st = new StringTokenizer(s, ";");
                    int labelcount = 0;
                    while (labelcount < columns) {
                        if (st.hasMoreTokens()) {
                            label[labelcount] = st.nextToken();
                            if (label[labelcount].length() == 0) {
                                label[labelcount] = " ";
                            }
                        } else {
                            label[labelcount] = " ";
                        }
                        ++labelcount;
                    }
                } else {
                    label[0] = s;
                }
                for (int col = 0; col < columns; ++col) {
                    JLabel jlabel = new JLabel(label[col]);
                    jlabel.setOpaque(true);
                    jlabel.setBorder(new EmptyBorder(1, 4, 1, 4));
                    if (i == selected) {
                        jlabel.setBackground(Color.lightGray);
                        jlabel.setForeground(Color.red);
                    } else {
                        jlabel.setBackground(Color.gray);
                        jlabel.setForeground(Color.white);
                    }
                    constr.gridx = col;
                    constr.gridy = i;
                    panel.add(jlabel, constr);
                }
                ++i;
            }
            this.removeAll();
            setLayout(new GridLayout(1, 1));
            add(panel);
            SwingUtilities.invokeLater(new Repainter(this));
        }
    }

    class Repainter implements Runnable {

        JComponent comp;

        public Repainter(JComponent comp) {
            this.comp = comp;
        }

        public void run() {
            comp.revalidate();
        }
    }

    class TargetListener extends DataModelAdaptor {

        StringVector tip = null;

        Object viewer = null;

        JComponent component = null;

        TipComponent tipComponent = null;

        KeyListener popupSelector = null;

        TextSelectorThread thread = null;

        public synchronized JComponent getTextFieldComponent() {
            if (viewer instanceof DataViewer) {
                if (((DataViewer) viewer).isCreated()) {
                    component = ((DataViewer) viewer).getJComponent();
                }
            }
            return component;
        }

        public synchronized void removeTip() {
            JComponent comp = getTextFieldComponent();
            if (comp != null && tipComponent != null) {
                tipComponent.setVisible(false);
                comp.getRootPane().getLayeredPane().remove(tipComponent);
                tipComponent = null;
                tip = null;
            }
        }

        public synchronized void setTip(StringVector tipText, boolean hasMore) {
            if (tipText == null && tip == null) {
                return;
            }
            removeTip();
            tip = tipText;
            JComponent comp = getTextFieldComponent();
            if (comp != null) {
                if (tip == null || tip.size() == 0) {
                    return;
                }
                tipComponent = createTipComponent(tip, hasMore);
                tipComponent.setSize(tipComponent.getPreferredSize());
                Dimension dim = comp.getSize();
                Point p1 = comp.getLocationOnScreen();
                Point p2 = comp.getRootPane().getLocationOnScreen();
                tipComponent.setLocation(p1.x - p2.x, p1.y - p2.y + dim.height);
                comp.getRootPane().getLayeredPane().add(tipComponent, JLayeredPane.POPUP_LAYER);
                comp.requestFocus();
                SwingUtilities.invokeLater(new Repainter(tipComponent));
            }
        }

        class PopupSelector extends KeyAdapter {

            public void accept() {
                try {
                    if (tipComponent != null) {
                        if (tipComponent.getSelected() == null) {
                            getTextFieldComponent().transferFocus();
                            command.doAction();
                            getTextFieldComponent().requestFocus();
                        } else {
                            getSelectorModel().setText(tipComponent.getSelected());
                            getTextFieldComponent().transferFocus();
                            getTextFieldComponent().requestFocus();
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            public void keyPressed(KeyEvent ev) {
                if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
                    accept();
                } else if (ev.getKeyCode() == KeyEvent.VK_UP && tipComponent != null) {
                    tipComponent.decrement();
                } else if (ev.getKeyCode() == KeyEvent.VK_DOWN && tipComponent != null) {
                    tipComponent.increment();
                } else if (ev.getKeyCode() == KeyEvent.VK_F2) {
                    getTextFieldComponent().transferFocus();
                    command.doAction();
                    getTextFieldComponent().requestFocus();
                }
            }
        }

        class TextSelectorThread extends Thread {

            String text;

            public TextSelectorThread(String text) {
                this.text = text;
                start();
            }

            public void run() {
                try {
                    sleep(300);
                } catch (InterruptedException ex) {
                    return;
                }
                TextSelector textSelector = getTextSelector();
                StringVector tipText = new StringVector();
                boolean hasMore = textSelector.getCompletionListFor(text, tipText);
                if (isInterrupted() || this != thread) {
                    return;
                }
                if (tipText.size() == 1 && text.equals(tipText.firstItem())) {
                    removeTip();
                } else {
                    setTip(tipText, hasMore);
                }
            }
        }

        public void dataModelChangeStructure(DataModelEvent event) {
        }

        public void dataModelGrabFocus(DataModelEvent event) {
        }

        public void dataModelEdit(DataModelEvent event) {
            switch(event.getId()) {
                case DataModelEvent.START_EDIT:
                    viewer = event.getViewer();
                    if (getTextFieldComponent() != null) {
                        getTextFieldComponent().addKeyListener(popupSelector = new PopupSelector());
                    }
                    break;
                case DataModelEvent.FINISH_EDIT:
                case DataModelEvent.CANCEL_EDIT:
                    if (getTextFieldComponent() != null) {
                        getTextFieldComponent().removeKeyListener(popupSelector);
                    }
                    removeTip();
                    viewer = null;
                    break;
                case DataModelEvent.VALUE_EDIT:
                    TextSelector textSelector = getTextSelector();
                    if (textSelector != null) {
                        String text = getTargetText();
                        if (text != null && text.length() >= textSelector.getMinCharsForPopup()) {
                            synchronized (this) {
                                if (thread != null) {
                                    thread.interrupt();
                                }
                                removeTip();
                                thread = new TextSelectorThread(text);
                            }
                        } else {
                            synchronized (this) {
                                if (thread != null) {
                                    thread.interrupt();
                                }
                                removeTip();
                                thread = null;
                            }
                        }
                    }
                    break;
                default:
                    return;
            }
        }
    }
}
