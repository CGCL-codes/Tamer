package com.testonica.kickelhahn.core.ui.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.testonica.kickelhahn.core.Constant;
import com.testonica.kickelhahn.core.KickelhahnInfo;
import com.testonica.kickelhahn.core.elements.board.Board;
import com.testonica.kickelhahn.core.manager.zoom.ZoomEvent;
import com.testonica.kickelhahn.core.manager.zoom.ZoomListener;
import com.testonica.kickelhahn.core.manager.zoom.ZoomManager;

public class OutlookWindow extends JDialog implements ComponentListener, MouseMotionListener, MouseListener, ZoomListener {

    protected FramedBoardPanel boardImage;

    private JSlider slider = new JSlider((int) (ZoomManager.ZOOM_LIST[0] * 100), (int) (ZoomManager.ZOOM_LIST[ZoomManager.ZOOM_LIST.length - 1] * 100));

    private JPanel panelCenter;

    private JPanel panelControl;

    private JButton buttonZoomOut = new JButton("-");

    private JButton buttonZoomIn = new JButton("+");

    private Board board;

    private JScrollPane scroll;

    private ActionListener actionSetZoom = new AbstractAction() {

        public void actionPerformed(ActionEvent arg0) {
            double newZoom = board.getZoom();
            if (arg0.getSource() == buttonZoomOut) {
                int i = 1;
                while ((newZoom = ZoomManager.ZOOM_LIST[ZoomManager.ZOOM_LIST.length - i]) >= board.getZoom()) {
                    i++;
                    if (i > ZoomManager.ZOOM_LIST.length) break;
                }
            } else if (arg0.getSource() == buttonZoomIn) {
                int i = 0;
                while ((newZoom = ZoomManager.ZOOM_LIST[i]) <= board.getZoom()) {
                    i++;
                    if (i == ZoomManager.ZOOM_LIST.length) break;
                }
            }
            updateZoomButtons(newZoom);
            ZoomManager.getInstance().fireZoomChanged(new ZoomEvent(newZoom));
        }
    };

    private ChangeListener sliderListener;

    public OutlookWindow(KickelhahnInfo info, Board board) throws HeadlessException {
        super(info.getFrame());
        setTitle("Outlook Window");
        this.board = board;
        boardImage = new FramedBoardPanel(board);
        boardImage.addMouseMotionListener(this);
        boardImage.addMouseListener(this);
        setModal(false);
        setSize(200, 200);
        toFront();
        setLocationRelativeTo(info.getFrame());
        addComponentListener(this);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(3, 3));
        scroll = new JScrollPane(boardImage);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sliderListener = new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                double newZoom = ((double) slider.getValue()) / 100;
                if (newZoom != ZoomManager.getInstance().getZoom()) {
                    ZoomManager.getInstance().fireZoomChanged(new ZoomEvent(newZoom));
                }
            }
        };
        slider.addChangeListener(sliderListener);
        panelCenter = new JPanel();
        panelCenter.setBorder(new EmptyBorder(3, 3, 3, 3));
        panelCenter.setLayout(new GridLayout(1, 1));
        panelCenter.add(scroll);
        buttonZoomOut.setFocusable(false);
        buttonZoomOut.setMargin(new Insets(0, 0, 0, 0));
        buttonZoomOut.setMinimumSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomOut.setPreferredSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomOut.setMaximumSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomOut.addActionListener(actionSetZoom);
        buttonZoomIn.setFocusable(false);
        buttonZoomIn.setMargin(new Insets(0, 0, 0, 0));
        buttonZoomIn.setMinimumSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomIn.setPreferredSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomIn.setMaximumSize(Constant.TOOLBAR_ICON_SIZE);
        buttonZoomIn.addActionListener(actionSetZoom);
        Dimension sliderSize = new Dimension(140, 20);
        slider.setMinimumSize(sliderSize);
        slider.setPreferredSize(sliderSize);
        slider.setMaximumSize(sliderSize);
        panelControl = new JPanel();
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.X_AXIS));
        panelControl.add(Box.createHorizontalGlue());
        panelControl.add(buttonZoomOut);
        panelControl.add(slider);
        panelControl.add(buttonZoomIn);
        panelControl.add(Box.createHorizontalGlue());
        add(panelCenter, BorderLayout.CENTER);
        add(panelControl, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        if (!isVisible()) return;
        boardImage.fit(panelCenter.getSize());
        refreshFrame();
        int borderLeftRight = 0;
        int borderTopBottom = 0;
        if (boardImage.getPreferredSize().width > 0) {
            borderLeftRight = (panelCenter.getSize().width - boardImage.getPreferredSize().width) / 2;
            borderTopBottom = (panelCenter.getSize().height - boardImage.getPreferredSize().height) / 2;
        }
        panelCenter.setBorder(new EmptyBorder(borderTopBottom, borderLeftRight, borderTopBottom, borderLeftRight));
        repaint();
    }

    public void componentResized(ComponentEvent arg0) {
        refresh();
    }

    public void componentMoved(ComponentEvent arg0) {
    }

    public void componentShown(ComponentEvent arg0) {
    }

    public void componentHidden(ComponentEvent arg0) {
    }

    public void mouseDragged(MouseEvent arg0) {
        refreshBoard();
    }

    private void refreshBoard() {
        double relationX = (double) boardImage.getRectangle().x / (double) boardImage.getWidth();
        double relationY = (double) boardImage.getRectangle().y / (double) boardImage.getHeight();
        double relationW = (double) boardImage.getRectangle().width / (double) boardImage.getWidth();
        double relationH = (double) boardImage.getRectangle().height / (double) boardImage.getHeight();
        int rectangleX = (int) (board.getWidth() * relationX);
        int rectangleY = (int) (board.getHeight() * relationY);
        int rectangleWidth = (int) (board.getWidth() * relationW);
        int rectangleHeight = (int) (board.getHeight() * relationH);
        board.scrollRectToVisible(new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight));
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        refreshBoard();
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void zoomChanged(ZoomEvent event) {
        setTitle("Outlook Window (" + (int) (event.getZoom() * 100) + "%)");
        slider.removeChangeListener(sliderListener);
        slider.setValue((int) (event.getZoom() * 100));
        slider.addChangeListener(sliderListener);
        updateZoomButtons(event.getZoom());
        refresh();
    }

    private void refreshFrame() {
        if ((board.getWidth() > 0) & (board.getHeight() > 0)) {
            double relationX = (double) board.getHolder().getHorizontalScrollBar().getValue() / (double) board.getHolder().getHorizontalScrollBar().getMaximum();
            double relationY = (double) board.getHolder().getVerticalScrollBar().getValue() / (double) board.getHolder().getVerticalScrollBar().getMaximum();
            double relationW = (double) board.getHolder().getVisibleRect().width / (double) board.getWidth();
            double relationH = (double) board.getHolder().getVisibleRect().height / (double) board.getHeight();
            int rectangleX = (int) (boardImage.getWidth() * relationX);
            int rectangleY = (int) (boardImage.getHeight() * relationY);
            int rectangleWidth = (int) (boardImage.getWidth() * relationW);
            int rectangleHeight = (int) (boardImage.getHeight() * relationH);
            boardImage.setRectangle(new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight));
        }
    }

    private void updateZoomButtons(double newZoom) {
        buttonZoomIn.setEnabled(newZoom < ZoomManager.ZOOM_LIST[ZoomManager.ZOOM_LIST.length - 1]);
        buttonZoomOut.setEnabled(newZoom > ZoomManager.ZOOM_LIST[0]);
    }

    public void forceRepaint() {
        if (isVisible()) boardImage.paintOffScreen();
    }
}
