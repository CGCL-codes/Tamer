package com.salas.bb.views;

import com.salas.bb.core.GlobalController;
import java.awt.*;
import javax.swing.*;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.swing.application.ActionManager;

/**
 * ChannelGuidePanel - Displays icons for each of the different channel guides, allowing
 * the user to pick which one they want to work with.
 * 
 */
public class ChannelGuidePanel extends JToolBar {

    Object userObject;

    AbstractButton bestBetsBtn;

    AbstractButton activeBtn;

    AbstractButton suggestedBtn;

    AbstractButton favoritesBtn;

    AbstractButton guideBtn;

    ButtonGroup btnGrp;

    public ChannelGuidePanel() {
        super(VERTICAL);
        setFloatable(false);
        putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.FALSE);
        btnGrp = new ButtonGroup();
        bestBetsBtn = addChannelGuideBtn(GlobalController.SELECT_BETS);
        favoritesBtn = addChannelGuideBtn(GlobalController.SELECT_FAVORITES);
        activeBtn = addChannelGuideBtn(GlobalController.SELECT_ACTIVE);
        suggestedBtn = addChannelGuideBtn(GlobalController.SELECT_SUGGESTED);
        guideBtn = addChannelGuideBtn(GlobalController.SELECT_GUIDE);
        bestBetsBtn.setSelected(true);
    }

    private AbstractButton addChannelGuideBtn(String actionCode) {
        AbstractButton newBtn = createToolBarRadioButton();
        newBtn.setAction(ActionManager.get(actionCode));
        newBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        newBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        add(newBtn);
        btnGrp.add(newBtn);
        newBtn.addMouseListener(GlobalController.SINGLETON.getMainframe().getCGBtnPopupAdapter());
        return newBtn;
    }

    /** Defines the margin used in toolbar buttons. */
    private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(8, 2, 8, 2);

    private static final Dimension TOOLBAR_BUTTON_PREFSIZE = new Dimension(100, 50);

    /**
	 * createToolBarRadioButton - From JGoodies - Create a nice looking radio-style button on toolbar 
	 * 
	 * @param iconpath - path to the icon file to be used.
	 * @return - Button so created.
	 */
    private AbstractButton createToolBarRadioButton() {
        JToggleButton button = new JToggleButton();
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        button.setMaximumSize(TOOLBAR_BUTTON_PREFSIZE);
        return button;
    }

    /**
	 * createToolBarButton - Also from JGoodies - Create a nice looking toolboar button
	 * 
	 * @param - path to the icon file to be used.
	 * @return - Button so created
	 */
    protected AbstractButton createToolBarButton() {
        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        button.setMaximumSize(TOOLBAR_BUTTON_PREFSIZE);
        return button;
    }
}
