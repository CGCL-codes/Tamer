package druid.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.ImagePanel;
import org.dlib.gui.TDialog;
import druid.core.config.Config;
import druid.util.gui.TitleLabel;

public class BasicDialog extends TDialog {

    private ImagePanel imgPanel = new ImagePanel();

    private JPanel innerPanel = new JPanel();

    private TitleLabel tlCaption = new TitleLabel();

    public BasicDialog(Frame frame) {
        super(frame, "", true);
        JPanel mainPanel = new JPanel();
        FlexLayout flexL = new FlexLayout(2, 2, 0, 4);
        flexL.setColProp(1, FlexLayout.EXPAND);
        flexL.setRowProp(1, FlexLayout.EXPAND);
        mainPanel.setLayout(flexL);
        imgPanel.setBackground(Color.white);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 4));
        mainPanel.add("0,0,c,x,1,2", imgPanel);
        mainPanel.add("1,0,x", tlCaption);
        mainPanel.add("1,1,x,x", innerPanel);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void setCaption(String title) {
        tlCaption.setText(title);
        setTitle(title);
    }

    public void setImage(String image) {
        imgPanel.setImage(Config.dir.images + "/" + image);
    }

    public JPanel getInnerPanel() {
        return innerPanel;
    }
}
