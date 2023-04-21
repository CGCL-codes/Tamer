package view.datapath;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.codes.SimpleBlockCode;
import model.datapath.DataPath;
import model.datapath.DataPathElement;
import model.datapath.SimpleDataPath;
import view.FactoryReceiver;
import view.datapath.channels.BACView;
import view.datapath.channels.BSCView;
import view.datapath.channels.GilbertView;
import view.datapath.channels.NoNoiseChannelView;
import view.datapath.codes.BlockCodeNoneView;
import view.datapath.codes.HammingCodeView;
import view.datapath.codes.RepetitionCodeView;
import view.datapath.element.ElementView;
import view.datapath.element.Factory;

public class SimpleDataPathOptions extends JComponent implements DataPathOptions, Observer {

    private static final long serialVersionUID = 1L;

    private final ElementView<DataPathElement<Boolean, Boolean>>[] channelViews;

    private JComboBox channelSelect;

    private final ElementView<SimpleBlockCode>[] codeViews;

    private JComboBox codeSelect;

    private Factory<DataPathElement<Boolean, Boolean>> channelFactory;

    private Factory<SimpleBlockCode> codeFactory;

    private JPanel optionsArea;

    private FactoryReceiver<DataPath> parent;

    @SuppressWarnings("unchecked")
    public SimpleDataPathOptions(JPanel optionsArea, FactoryReceiver<DataPath> parent) {
        this.optionsArea = optionsArea;
        this.parent = parent;
        channelViews = new ElementView[] { new NoNoiseChannelView(), new BACView(), new BSCView(), new GilbertView() };
        codeViews = new ElementView[] { new BlockCodeNoneView(), new RepetitionCodeView(), new HammingCodeView() };
        channelFactory = channelViews[0].createFactory();
        codeFactory = codeViews[0].createFactory();
    }

    private void selectChannel(ElementView<DataPathElement<Boolean, Boolean>> view) {
        view.addObserver(this);
        channelFactory = view.createFactory();
        optionsArea.removeAll();
        optionsArea.add(view.createComponent());
        optionsArea.revalidate();
        parent.setFactory(createFactory());
    }

    private void selectCode(ElementView<SimpleBlockCode> view) {
        view.addObserver(this);
        codeFactory = view.createFactory();
        optionsArea.removeAll();
        optionsArea.add(view.createComponent());
        optionsArea.revalidate();
        parent.setFactory(createFactory());
    }

    public void update(Observable o, Object arg) {
        assert arg != null;
        for (ElementView<SimpleBlockCode> view : codeViews) {
            if (view == arg) {
                codeFactory = view.createFactory();
            }
        }
        for (ElementView<DataPathElement<Boolean, Boolean>> view : channelViews) {
            if (view == arg) {
                channelFactory = view.createFactory();
            }
        }
        parent.setFactory(createFactory());
    }

    public Factory<DataPath> createFactory() {
        return new Factory<DataPath>() {

            public DataPath create() {
                return new SimpleDataPath(codeFactory.create(), channelFactory.create());
            }
        };
    }

    public JPanel createPanel() {
        return new SDPOPanel();
    }

    public String getDataPathName() {
        return "Simple block data path";
    }

    private class SDPOPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public SDPOPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 0;
            add(createChannelSelect(), c);
            add(createCodeSelect(), c);
        }

        private JComponent createChannelSelect() {
            JPanel channelArea = new JPanel();
            channelArea.setLayout(new BoxLayout(channelArea, BoxLayout.X_AXIS));
            channelSelect = new JComboBox();
            for (ElementView<DataPathElement<Boolean, Boolean>> view : channelViews) {
                channelSelect.addItem(view.getElementName());
            }
            channelSelect.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    selectChannel(channelViews[channelSelect.getSelectedIndex()]);
                }
            });
            JButton channelButton = new JButton("Setup");
            channelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectChannel(channelViews[channelSelect.getSelectedIndex()]);
                }
            });
            channelArea.add(new JLabel("Channel:"));
            channelArea.add(channelSelect);
            channelArea.add(channelButton);
            return channelArea;
        }

        private JComponent createCodeSelect() {
            JPanel codeArea = new JPanel();
            codeArea.setLayout(new BoxLayout(codeArea, BoxLayout.X_AXIS));
            codeSelect = new JComboBox();
            for (ElementView<SimpleBlockCode> view : codeViews) {
                codeSelect.addItem(view.getElementName());
            }
            codeSelect.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    selectCode(codeViews[codeSelect.getSelectedIndex()]);
                }
            });
            JButton codeButton = new JButton("Setup");
            codeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectCode(codeViews[codeSelect.getSelectedIndex()]);
                }
            });
            codeArea.add(new JLabel("Code:"));
            codeArea.add(codeSelect);
            codeArea.add(codeButton);
            return codeArea;
        }
    }

    public void copyOptions(DataPathOptions other) {
        if (!(other instanceof SimpleDataPathOptions)) throw new IllegalArgumentException();
        SimpleDataPathOptions that = (SimpleDataPathOptions) other;
        for (int i = 0; i < channelViews.length; i++) {
            channelViews[i].copyOptions(that.channelViews[i]);
            channelSelect.setSelectedIndex(that.channelSelect.getSelectedIndex());
        }
        for (int i = 0; i < codeViews.length; i++) {
            codeViews[i].copyOptions(that.codeViews[i]);
            codeSelect.setSelectedIndex(that.codeSelect.getSelectedIndex());
        }
    }
}
