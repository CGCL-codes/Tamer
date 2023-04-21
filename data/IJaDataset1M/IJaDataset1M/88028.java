package com.doculibre.intelligid.wicket.components.reports.links;

import java.util.ArrayList;
import java.util.List;
import wicket.PageMap;
import wicket.markup.html.WebPage;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.link.PopupSettings;
import wicket.model.IModel;
import wicket.model.Model;
import com.doculibre.intelligid.reports.general.ReportParameters;
import com.doculibre.intelligid.wicket.components.reports.links.jasperReports.JasperConservedPDFDownloadLink;
import com.doculibre.intelligid.wicket.components.reports.links.jasperReports.JasperConservedPDFViewPageLink;
import com.doculibre.intelligid.wicket.components.reports.links.jasperReports.JasperCreateAndDownloadLink;
import com.doculibre.intelligid.wicket.components.reports.links.jasperReports.JasperCreateAndViewPageLink;
import com.doculibre.intelligid.wicket.components.reports.links.jasperReports.JasperRegenerateLink;
import com.doculibre.intelligid.wicket.components.reports.links.jxl.JXLCreateAndDownloadLink;

public class ReportsLinksFactory {

    @SuppressWarnings("serial")
    public static Link getCorrectViewLinkFor(String id, final IModel paramsModel, String fileName, final IModel isVisibleModel) {
        PageMap popupPageMap = PageMap.forName("popupReport");
        ReportParameters params = (ReportParameters) paramsModel.getObject(null);
        paramsModel.detach();
        if (params.isJasperReport()) {
            if (params.isConserved()) {
                return new PageLink(id, new JasperConservedPDFViewPageLink(params, fileName)) {

                    @Override
                    public boolean isVisible() {
                        Boolean b = (Boolean) isVisibleModel.getObject(null);
                        return super.isVisible() && b != null && b;
                    }

                    @Override
                    protected void onDetach() {
                        isVisibleModel.detach();
                        paramsModel.detach();
                        super.onDetach();
                    }
                }.setPopupSettings(new PopupSettings(popupPageMap, PopupSettings.RESIZABLE));
            } else {
                return new PageLink(id, new JasperCreateAndViewPageLink(paramsModel, fileName)) {

                    @Override
                    public boolean isVisible() {
                        Boolean b = (Boolean) isVisibleModel.getObject(null);
                        return super.isVisible() && b != null && b;
                    }

                    @Override
                    protected void onDetach() {
                        isVisibleModel.detach();
                        paramsModel.detach();
                        super.onDetach();
                    }
                }.setPopupSettings(new PopupSettings(popupPageMap, PopupSettings.RESIZABLE));
            }
        } else {
            return (Link) new PageLink(id, WebPage.class) {

                @Override
                protected void onDetach() {
                    paramsModel.detach();
                    super.onDetach();
                }
            }.setVisible(false);
        }
    }

    public static Link getCorrectViewLinkFor(String id, IModel paramsModel, String fileName) {
        return ReportsLinksFactory.getCorrectViewLinkFor(id, paramsModel, fileName, new Model(true));
    }

    @SuppressWarnings("serial")
    public static Link getCorrectDownloadLinkFor(String id, ReportParameters params, String fileName, final IModel isVisibleModel) {
        if (params.isJasperReport()) {
            if (params.isConserved()) {
                return new JasperConservedPDFDownloadLink(id, params, fileName) {

                    @Override
                    public boolean isVisible() {
                        Boolean b = (Boolean) isVisibleModel.getObject(null);
                        return super.isVisible() && b != null && b;
                    }
                };
            } else {
                return new JasperCreateAndDownloadLink(id, params, fileName) {

                    @Override
                    public boolean isVisible() {
                        Boolean b = (Boolean) isVisibleModel.getObject(null);
                        return super.isVisible() && b != null && b;
                    }
                };
            }
        } else if (params.isJXLReport()) {
            return new JXLCreateAndDownloadLink(id, params, fileName) {

                @Override
                public boolean isVisible() {
                    Boolean b = (Boolean) isVisibleModel.getObject(null);
                    return super.isVisible() && b != null && b;
                }
            };
        } else {
            return (Link) new PageLink(id, WebPage.class).setVisible(false);
        }
    }

    public static Link getCorrectDownloadLinkFor(String id, ReportParameters params, String fileName) {
        return ReportsLinksFactory.getCorrectDownloadLinkFor(id, params, fileName, new Model(true));
    }

    public static Link getRefreshLink(String id, List<ReportParameters> paramsList) {
        List<ReportParameters> reportsParamsConserves = new ArrayList<ReportParameters>();
        for (ReportParameters params : paramsList) {
            if (params.isConserved()) {
                reportsParamsConserves.add(params);
            }
        }
        if (reportsParamsConserves.isEmpty()) {
            return (Link) new PageLink(id, WebPage.class).setVisible(false);
        } else {
            return new JasperRegenerateLink("regenerateLink", reportsParamsConserves);
        }
    }
}
