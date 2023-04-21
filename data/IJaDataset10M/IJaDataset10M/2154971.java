package gui;

import java.awt.FlowLayout;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import controller.listeners.PageViewerToolBarListener;
import utils.IdsObservable;
import utils.IdsObserver;
import utils.ObservableType;

public class PageViewerToolBar extends JToolBar implements IdsObserver {

    private static final long serialVersionUID = -7922559924246555813L;

    private JButton zoomIn;

    private JTextField zoomValue;

    private JButton zoomOut;

    private JButton binarize;

    private JButton skewCorrect;

    private JButton squareSelect;

    private JButton handDrawSelect;

    private JButton automaticCharDetection;

    public PageViewerToolBar(ResourceBundle localLanguageMenu) {
        setFloatable(false);
        setLayout(new FlowLayout());
        buildToolBar(localLanguageMenu);
    }

    public void buildToolBar(ResourceBundle localLanguageMenu) {
        zoomOut = new JButton("-");
        add(zoomOut);
        zoomValue = new JTextField(4);
        zoomValue.setHorizontalAlignment(JTextField.CENTER);
        add(zoomValue);
        add(new JLabel("%"));
        zoomIn = new JButton("+");
        add(zoomIn);
        binarize = new JButton(localLanguageMenu.getString("BinariserImage"));
        add(binarize);
        skewCorrect = new JButton(localLanguageMenu.getString("SkewCorrect"));
        add(skewCorrect);
        squareSelect = new JButton("[]");
        add(squareSelect);
        handDrawSelect = new JButton("@");
        add(handDrawSelect);
        automaticCharDetection = new JButton(localLanguageMenu.getString("AutoCharactersDetect"));
        add(automaticCharDetection);
    }

    public void assignListeners(PageViewerToolBarListener pageViewerToolBarListener) {
        zoomIn.addActionListener(pageViewerToolBarListener);
        zoomOut.addActionListener(pageViewerToolBarListener);
        zoomValue.addKeyListener(pageViewerToolBarListener);
        zoomValue.addFocusListener(pageViewerToolBarListener);
        binarize.addActionListener(pageViewerToolBarListener);
        skewCorrect.addActionListener(pageViewerToolBarListener);
        squareSelect.addActionListener(pageViewerToolBarListener);
        handDrawSelect.addActionListener(pageViewerToolBarListener);
        automaticCharDetection.addActionListener(pageViewerToolBarListener);
    }

    public JButton getZoomIn() {
        return zoomIn;
    }

    public JTextField getZoomValue() {
        return zoomValue;
    }

    public JButton getZoomOut() {
        return zoomOut;
    }

    public JButton getBinarize() {
        return binarize;
    }

    public JButton getSkewCorrect() {
        return skewCorrect;
    }

    public JButton getSquareSelect() {
        return squareSelect;
    }

    public JButton getHandDrawSelect() {
        return handDrawSelect;
    }

    public JButton getAutomaticCharDetection() {
        return automaticCharDetection;
    }

    public void observableUpdated(IdsObservable o, ObservableType observableType) {
        switch(observableType) {
            case CONFIGURATION:
                configuration.Configuration conf = (configuration.Configuration) o;
                getZoomValue().setText(conf.getZoom() + "");
                System.out.println(getClass());
                break;
            default:
                break;
        }
    }

    public void setEnable(boolean b) {
        getZoomIn().setEnabled(b);
        getZoomValue().setEnabled(b);
        getZoomOut().setEnabled(b);
        getBinarize().setEnabled(b);
        getSkewCorrect().setEnabled(b);
        getSquareSelect().setEnabled(b);
        getHandDrawSelect().setEnabled(b);
        getAutomaticCharDetection().setEnabled(b);
    }
}
