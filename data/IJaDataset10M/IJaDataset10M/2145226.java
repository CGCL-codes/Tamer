package org.hardtokenmgmt.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.hardtokenmgmt.core.ui.BaseView;
import org.hardtokenmgmt.core.ui.UIHelper;

/**
 * View of the Error page.
 * 
 * 
 * @author Philip Vendil 2007 feb 16
 *
 * @version $Id$
 */
public class ErrorView extends BaseView implements IErrorView {

    private static final long serialVersionUID = 1L;

    private JLabel logoLabel = null;

    public JLabel errorNameLabel = null;

    private JLabel titleLabel = null;

    private JLabel youCanReportLabel = null;

    private JButton backButton = null;

    private JButton reportErrorButton = null;

    private JLabel statusLabel = null;

    /**
	 * Default constuct
	 *
	 */
    public ErrorView() {
        super();
        initialize();
    }

    @Override
    protected void initialize() {
        statusLabel = new JLabel();
        statusLabel.setBounds(new Rectangle(288, 269, 571, 17));
        youCanReportLabel = new JLabel();
        youCanReportLabel.setBounds(new Rectangle(288, 240, 573, 17));
        youCanReportLabel.setText(UIHelper.getText("error.youcanreport"));
        this.setSize(new Dimension(UIHelper.getAppWidth(), UIHelper.getAppHeight()));
        this.setLayout(null);
        errorNameLabel = new JLabel();
        errorNameLabel.setBounds(ToLiMaGUI.getStatusLabelPos());
        logoLabel = new JLabel();
        logoLabel.setBounds(ToLiMaGUI.getLogoPos());
        logoLabel.setIcon(UIHelper.getLogo());
        titleLabel = new JLabel();
        titleLabel.setText(UIHelper.getText("error.anerrorhaveoccured"));
        titleLabel.setBounds(ToLiMaGUI.getTitleLabelPos());
        titleLabel.setFont(UIHelper.getTitleFont());
        this.add(logoLabel, null);
        this.add(titleLabel, null);
        this.add(errorNameLabel, null);
        this.add(youCanReportLabel, null);
        this.add(getBackButton(), null);
        this.add(getReportErrorButton(), null);
        this.add(statusLabel, null);
    }

    public JButton getBackButton() {
        if (backButton == null) {
            backButton = new JButton();
            backButton.setText(UIHelper.getText("common.back"));
            backButton.setBounds(ToLiMaGUI.getBackButtonPos());
            backButton.setIcon(UIHelper.getImage("back.gif"));
        }
        return backButton;
    }

    public JLabel getErrorNameLabel() {
        return errorNameLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getYouCanReportLabel() {
        return youCanReportLabel;
    }

    public JButton getReportErrorButton() {
        if (reportErrorButton == null) {
            reportErrorButton = new JButton();
            reportErrorButton.setBounds(new Rectangle(288, 316, 324, 66));
            reportErrorButton.setText(UIHelper.getText("error.reporterror"));
            reportErrorButton.setIcon(UIHelper.getImage("sendrapport.gif"));
        }
        return reportErrorButton;
    }

    @Override
    public void close() {
    }
}
