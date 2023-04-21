package com.codemonster.nato.configuration;

import java.awt.*;

/**
 */
public class AppConfiguration {

    private Point appLocation = new Point(0, 0);

    private Dimension appSize = new Dimension(935, 500);

    private Point helpLocation = new Point(0, 0);

    private Dimension helpSize = new Dimension(855, 500);

    private int mainPanelVerticalSplit = 230;

    private int detailPanelHorizontalSplit = 215;

    private Color detailPanelBackground = new Color(204, 204, 204);

    /**
     * The location where we begin when looking for models.
     */
    private String modelPath = "models";

    /**
     * The location where we begin when looking for templates.
     */
    private String templatesPath = "templates/";

    /**
     * The location where we store reports.
     */
    private String reportsPath = "reports/";

    /**
     * While the chooser may start in the templates path, the user is free to navigate anywhere and finally choose a file. We
     * store that template fully-qualified file name here.
     */
    private String reportTemplate;

    private String currentModelFile = "";

    private String reportSuffix = ".html";

    private String reportUserName = "DefaultUser";

    private boolean saveModelOnExit = true;

    /**
     * By default, we make tasks with completion dates urgent when they are one day away.
     */
    private int urgencyGraceDays = 1;

    public int getUrgencyGraceDays() {
        return urgencyGraceDays;
    }

    public void setUrgencyGraceDays(int urgencyGraceDays) {
        this.urgencyGraceDays = urgencyGraceDays;
    }

    public boolean isSaveModelOnExit() {
        return saveModelOnExit;
    }

    public void setSaveModelOnExit(boolean saveModelOnExit) {
        this.saveModelOnExit = saveModelOnExit;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public String getReportUserName() {
        return reportUserName;
    }

    public void setReportUserName(String reportUserName) {
        this.reportUserName = reportUserName;
    }

    public String getReportSuffix() {
        return reportSuffix;
    }

    public void setReportSuffix(String reportSuffix) {
        this.reportSuffix = reportSuffix;
    }

    public String getCurrentModelFile() {
        return currentModelFile;
    }

    public void setCurrentModelFile(String currentModelFile) {
        this.currentModelFile = currentModelFile;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public String getReportsPath() {
        return reportsPath;
    }

    public void setReportsPath(String reportsPath) {
        this.reportsPath = reportsPath;
    }

    public Point getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(Point appLocation) {
        this.appLocation = appLocation;
    }

    public Dimension getAppSize() {
        return appSize;
    }

    public void setAppSize(Dimension appSize) {
        this.appSize = appSize;
    }

    public Point getHelpLocation() {
        return helpLocation;
    }

    public void setHelpLocation(Point helpLocation) {
        this.helpLocation = helpLocation;
    }

    public Dimension getHelpSize() {
        return helpSize;
    }

    public void setHelpSize(Dimension helpSize) {
        this.helpSize = helpSize;
    }

    public int getMainPanelVerticalSplit() {
        return mainPanelVerticalSplit;
    }

    public void setMainPanelVerticalSplit(int mainPanelVerticalSplit) {
        this.mainPanelVerticalSplit = mainPanelVerticalSplit;
    }

    public int getDetailPanelHorizontalSplit() {
        return detailPanelHorizontalSplit;
    }

    public void setDetailPanelHorizontalSplit(int detailPanelHorizontalSplit) {
        this.detailPanelHorizontalSplit = detailPanelHorizontalSplit;
    }

    public Color getDetailPanelBackground() {
        return detailPanelBackground;
    }

    public void setDetailPanelBackground(Color detailPanelBackground) {
        this.detailPanelBackground = detailPanelBackground;
    }
}
