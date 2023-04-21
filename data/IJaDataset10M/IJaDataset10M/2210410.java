package druid.util.jdbc.dataeditor.record.largedata;

import java.awt.Font;
import java.io.UnsupportedEncodingException;
import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.TTextArea;

class AsciiPanel extends JPanel {

    private TTextArea txaValue = new TTextArea();

    private Font font = new Font("Monospaced", Font.PLAIN, 12);

    public AsciiPanel() {
        FlexLayout flexL = new FlexLayout(1, 1);
        flexL.setColProp(0, FlexLayout.EXPAND);
        flexL.setRowProp(0, FlexLayout.EXPAND);
        setLayout(flexL);
        add("0,0,x,x", txaValue);
        txaValue.setEditable(false);
        txaValue.setFont(font);
    }

    public void setValue(byte[] data) {
        if (data == null) txaValue.setText(""); else {
            try {
                txaValue.setText(new String(data, "US-ASCII"));
            } catch (UnsupportedEncodingException e) {
                txaValue.setText("<< error converting string >>");
            }
        }
    }
}
