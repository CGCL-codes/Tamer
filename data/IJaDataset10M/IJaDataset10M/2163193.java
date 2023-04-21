package com.plato.etoh.client.admin.basvurulistele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;
import com.plato.etoh.client.GreetingService;
import com.plato.etoh.client.GreetingServiceAsync;
import com.plato.etoh.client.Preferences;
import com.plato.etoh.client.admin.AdminManager;
import com.plato.etoh.client.admin.basvuruinceleme.BasvuruInceleAnaEkran;
import com.plato.etoh.client.cache.CacheUtil;
import com.plato.etoh.client.cache.MyAsynchCallBack;
import com.plato.etoh.client.model.Admin;
import com.plato.etoh.client.model.ApplicationDTO;
import com.plato.etoh.client.model.Group;
import com.plato.etoh.client.model.MyQuery;
import com.plato.etoh.client.model.ResultAppListWrapper;
import com.plato.etoh.client.model.YoneticiBaseInterface;
import com.plato.etoh.client.util.UIUtil;

public class ListApplicationsScreen extends Composite {

    private static ListApplicationsScreen instance;

    private static Integer starredAndReturnedApplicationCount;

    private static Integer currentPageNumber;

    private static Integer totalStarredApplicationCount;

    private static MyQuery currentQuery;

    private static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    private static Button enSonSayfaLink = new Button();

    private static Button sonrakiSayfaLink = new Button();

    private static Button enBasSayfaLink = new Button();

    private static Button oncekiSayfaLink = new Button();

    private static int currentPage = 1;

    private static int numberOfPages = 0;

    private static HorizontalPanel pagingHolder;

    private static List<Hyperlink> pageLinkListForCss = new ArrayList<Hyperlink>();

    private ScrollTable scrollTable;

    private static FixedWidthFlexTable headerTable = null;

    private static FixedWidthFlexTable footerTable = null;

    private static FixedWidthGrid dataTable = null;

    public static boolean applicationListIsDirty = false;

    HTML titleHTML;

    private static ResultAppListWrapper resultWrapper;

    private ListApplicationsScreen() {
        FlexTable holder = new FlexTable();
        holder.setStyleName("form-table");
        holder.removeStyleName("filter-table");
        holder.setStyleName("filter-table");
        titleHTML = new HTML();
        holder.setWidget(0, 0, titleHTML);
        headerTable = TableUtil.createHeaderTable();
        dataTable = TableUtil.createDataTable();
        scrollTable = new ScrollTable(dataTable, headerTable);
        footerTable = createFooterTable();
        TableUtil.configurateScrollTable(scrollTable);
        holder.setWidget(1, 0, scrollTable);
        initWidget(holder);
    }

    public void loadDataTableAccordingToPageNumber() {
        MyQuery myQuery = AdvancedQueryOptionsPanel.getInstance().getQuery();
        loadDataTableAccordingToPageNumber(myQuery);
    }

    public static void loadDataTableAccordingToPageNumber(final MyQuery myQuery) {
        UIUtil.showGlassPanel();
        final YoneticiBaseInterface ADMIN = AdminManager.getSession().getAdmin();
        int fromLimit = Preferences.APPLICATION_COUNT_PER_PAGE * (currentPage - 1);
        int toLimit = fromLimit + Preferences.APPLICATION_COUNT_PER_PAGE;
        final int tempApplicationCount = fromLimit;
        if (myQuery.getStarred()) {
            fromLimit = 0;
            toLimit = 1000;
        }
        pagingHolder.clear();
        pagingHolder.add(new HTML("loading..."));
        greetingService.getBareApplications(currentPage, fromLimit, toLimit, myQuery, new AsyncCallback<ResultAppListWrapper>() {

            @Override
            public void onFailure(Throwable caught) {
                UIUtil.hideYukleniyorThingy();
                UIUtil.showMessage("Hata 25A.3 " + caught.getMessage());
            }

            @Override
            public void onSuccess(ResultAppListWrapper resultWrapper) {
                boolean starredFlag = false;
                loadTable(myQuery, tempApplicationCount, resultWrapper, starredFlag);
            }
        });
    }

    private static void loadTable(final MyQuery myQuery, final int tempApplicationCount, ResultAppListWrapper resultWrapper, boolean starredFlag) {
        ListApplicationsScreen.resultWrapper = resultWrapper;
        if (starredFlag == false) {
            ListApplicationsScreen.getInstance().refreshPagingBlock(myQuery.getStarred());
        } else {
            ListApplicationsScreen.getInstance().refreshStarredPagingBlock(myQuery.getStarred());
        }
        UIUtil.hideYukleniyorThingy();
        ArrayList<ApplicationDTO> result = resultWrapper.getResultAppList();
        if (result.size() == 0) {
            UIUtil.showMessage("Seçilen arama kriterleri için sonuç bulunamadı");
            return;
        }
        int i = 0;
        dataTable.clearAll();
        int applicationCount = tempApplicationCount;
        for (Iterator<ApplicationDTO> iterator = result.iterator(); iterator.hasNext(); applicationCount++) {
            final ApplicationDTO application = iterator.next();
            final HTML groupNameHTML = new HTML();
            final HTML groupMembersHTML = new HTML();
            Hyperlink starlink = new Hyperlink();
            if (application.getStarList().contains(AdminManager.getSession().getAdmin().getId())) {
                starlink.setHTML("<center><img src='images/starred.png'></center>");
                starlink.setTitle("+");
            } else {
                starlink.setHTML("<center><img src='images/notstarred.png'> </center>");
                starlink.setTitle("-");
            }
            ClickHandler starClickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    UIUtil.showGlassPanel();
                    Hyperlink link = (Hyperlink) event.getSource();
                    if (link.getTitle().indexOf("+") > -1) {
                        link.setTitle("- " + application.getId());
                        link.setHTML("<center><img src='images/notstarred.png'></center>");
                        greetingService.yildizIsaretle(AdminManager.getSession().getAdmin().getId(), application.getId(), false, new AsyncCallback<String>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                UIUtil.showMessage("Hata 27.1 " + caught.getMessage());
                                UIUtil.hideYukleniyorThingy();
                            }

                            @Override
                            public void onSuccess(String result) {
                                UIUtil.hideYukleniyorThingy();
                            }
                        });
                    } else {
                        link.setTitle("+ " + application.getId());
                        link.setHTML("<center><img src='images/starred.png'></center> ");
                        greetingService.yildizIsaretle(AdminManager.getSession().getAdmin().getId(), application.getId(), true, new AsyncCallback<String>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                UIUtil.showMessage("Hata 27.2");
                                UIUtil.hideYukleniyorThingy();
                            }

                            @Override
                            public void onSuccess(String result) {
                                UIUtil.hideYukleniyorThingy();
                            }
                        });
                    }
                }
            };
            starlink.addClickHandler(starClickHandler);
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.APPLICATION_COUNT_COLUMN_INDEX, new HTML((applicationCount + 1) + "."));
            ListApplicationsScreen.dataTable.getCellFormatter().setVerticalAlignment(i, TableUtil.APPLICATION_COUNT_COLUMN_INDEX, HasVerticalAlignment.ALIGN_MIDDLE);
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.BUTON_HOLDER_COLUMN_INDEX, createInceleIslem(application.getId()));
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.STAR_COLUMN_INDEX, starlink);
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.GROUP_NAME_COLUMN_INDEX, groupNameHTML);
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.GROUP_MEMBERS_COLUMN_INDEX, groupMembersHTML);
            ListApplicationsScreen.dataTable.setWidget(i, TableUtil.OYLAMA_COLUMN_INDEX, new HTML(String.valueOf(application.getOrtalamaPuan())));
            groupNameHTML.setText(application.getGroupName());
            CacheUtil.getGroup(application.getGroupId(), new MyAsynchCallBack() {

                @Override
                public void onSuccess(Object result) {
                    Group g = (Group) result;
                    groupNameHTML.setText(g.getGroupName());
                    for (Iterator iterator2 = g.getApplicantList().iterator(); iterator2.hasNext(); ) {
                        Long type = (Long) iterator2.next();
                        groupMembersHTML.setHTML(groupMembersHTML.getHTML() + "<img src='images/user.png'/>");
                    }
                }
            });
            String dateString = "";
            if (application.getBasvuruTarihi() != null) {
                String[] dateStringArray;
                dateString = application.getBasvuruTarihi().toGMTString();
                dateStringArray = dateString.split(" ");
                dateString = dateStringArray[0] + " " + dateStringArray[1] + " " + dateStringArray[2];
            }
            dataTable.setHTML(i, TableUtil.BASVURU_TARIHI_COLUMN_INDEX, dateString);
            if (application.getHasCompany() == null) {
                dataTable.setHTML(i, TableUtil.SIRKET_VARMI_COLUMN_INDEX, "");
            } else {
                String message = application.getHasCompany() ? "<center><img src=\"images/checked.gif\" title='Şirketi var' /></center>" : "";
                dataTable.setHTML(i, TableUtil.SIRKET_VARMI_COLUMN_INDEX, message);
            }
            if (application.getApplciationIsFinished() == null || application.getApplciationIsFinished() == false) {
                if (application.getStartAnswering() == true) {
                    dataTable.setHTML(i, TableUtil.CEVAPLAMA_BITTIMI_COLUMN_INDEX, "<center><img src=\"images/continues.png\" title='Devam ediyor' /></center>");
                } else {
                    dataTable.setHTML(i, TableUtil.CEVAPLAMA_BITTIMI_COLUMN_INDEX, "<center>başlamadı</center>");
                }
            } else {
                String message = "<center><img src=\"images/checked.gif\" title='Tamamlandı' /></ center>";
                dataTable.setHTML(i, TableUtil.CEVAPLAMA_BITTIMI_COLUMN_INDEX, message);
            }
            i++;
        }
    }

    protected FixedWidthFlexTable createFooterTable() {
        FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
        footerTable.setWidth("700px");
        FlexCellFormatter formatter = footerTable.getFlexCellFormatter();
        pagingHolder = new HorizontalPanel();
        pagingHolder.setStyleName("pagination_dk");
        footerTable.setWidget(0, 0, pagingHolder);
        formatter.setColSpan(0, 0, 6);
        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        scrollTable.setFooterTable(footerTable);
        return footerTable;
    }

    public void refreshPagingBlock(boolean starVarmi) {
        if (starVarmi) {
            pagingHolder.clear();
        } else {
            if (this.resultWrapper.getSize().intValue() == 0) {
                return;
            }
            pagingHolder.clear();
            numberOfPages = (int) Math.ceil(this.resultWrapper.getSize().doubleValue() / Preferences.APPLICATION_COUNT_PER_PAGE);
            ListApplicationsScreen.pageLinkListForCss.clear();
            enBasSayfaLink.setText("<<");
            enBasSayfaLink.addStyleName("disabled");
            pagingHolder.add(enBasSayfaLink);
            enBasSayfaLink.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (currentPage != 1) {
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).removeStyleName("currentpage");
                        ListApplicationsScreen.currentPage = 1;
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
                        ileriGeriOklariniKontrolEt();
                        loadDataTableAccordingToPageNumber();
                    }
                }
            });
            oncekiSayfaLink.setText("<");
            oncekiSayfaLink.addStyleName("disabled");
            pagingHolder.add(oncekiSayfaLink);
            oncekiSayfaLink.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (currentPage != 1) {
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).removeStyleName("currentpage");
                        ListApplicationsScreen.currentPage = ListApplicationsScreen.currentPage - 1;
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
                        ileriGeriOklariniKontrolEt();
                        loadDataTableAccordingToPageNumber();
                    }
                }
            });
            int pageRange = 9;
            for (int pageCount = 0; pageCount < numberOfPages; pageCount++) {
                final int m = pageCount;
                Hyperlink link = new Hyperlink();
                pageLinkListForCss.add(link);
                link.setText(Integer.toString(pageCount + 1));
                if ((currentPage - pageRange > (pageCount + 1))) {
                    continue;
                }
                if (currentPage + pageRange < (pageCount + 1)) {
                    continue;
                }
                pagingHolder.add(link);
                ClickHandler clickHandler = new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).removeStyleName("currentpage");
                        ListApplicationsScreen.currentPage = m + 1;
                        pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
                        ileriGeriOklariniKontrolEt();
                        loadDataTableAccordingToPageNumber();
                    }
                };
                link.addClickHandler(clickHandler);
            }
            pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
            if (numberOfPages > 1) {
                sonrakiSayfaLink.setText(">");
                pagingHolder.add(sonrakiSayfaLink);
                sonrakiSayfaLink.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (currentPage != numberOfPages) {
                            pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).removeStyleName("currentpage");
                            ListApplicationsScreen.currentPage = ListApplicationsScreen.currentPage + 1;
                            pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
                            ileriGeriOklariniKontrolEt();
                            loadDataTableAccordingToPageNumber();
                        }
                    }
                });
                enSonSayfaLink.setText(">>");
                pagingHolder.add(enSonSayfaLink);
                enSonSayfaLink.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (currentPage != numberOfPages) {
                            pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).removeStyleName("currentpage");
                            ListApplicationsScreen.currentPage = numberOfPages;
                            pageLinkListForCss.get(ListApplicationsScreen.currentPage - 1).addStyleName("currentpage");
                            ileriGeriOklariniKontrolEt();
                            loadDataTableAccordingToPageNumber();
                        }
                    }
                });
            }
        }
    }

    private void ileriGeriOklariniKontrolEt() {
        createFooterTable();
        if (currentPage == numberOfPages && numberOfPages > 1) {
            enSonSayfaLink.addStyleName("disabled");
            sonrakiSayfaLink.addStyleName("disabled");
            oncekiSayfaLink.removeStyleName("disabled");
            enBasSayfaLink.removeStyleName("disabled");
        } else if (currentPage == 1 && numberOfPages > 1) {
            enSonSayfaLink.removeStyleName("disabled");
            sonrakiSayfaLink.removeStyleName("disabled");
            enBasSayfaLink.addStyleName("disabled");
            oncekiSayfaLink.addStyleName("disabled");
        } else if (numberOfPages > 1) {
            enSonSayfaLink.removeStyleName("disabled");
            sonrakiSayfaLink.removeStyleName("disabled");
            oncekiSayfaLink.removeStyleName("disabled");
            enBasSayfaLink.removeStyleName("disabled");
        } else {
            enSonSayfaLink.addStyleName("disabled");
            sonrakiSayfaLink.addStyleName("disabled");
            enBasSayfaLink.addStyleName("disabled");
            oncekiSayfaLink.addStyleName("disabled");
        }
    }

    private static Widget createInceleIslem(final Long applicationId) {
        HorizontalPanel hp = new HorizontalPanel();
        Hyperlink secHyperLink = new Hyperlink();
        secHyperLink.setHTML("<img src='images/detail.png' />");
        secHyperLink.setTitle("incele");
        ClickHandler clickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                new BasvuruInceleAnaEkran(applicationId).show();
            }
        };
        secHyperLink.addClickHandler(clickHandler);
        hp.add(secHyperLink);
        return secHyperLink;
    }

    public static ListApplicationsScreen getInstance() {
        if (instance == null) {
            instance = new ListApplicationsScreen();
        }
        return instance;
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        ListApplicationsScreen.currentPage = currentPage;
    }

    public void setTitle(String title) {
        this.titleHTML.setHTML(title);
    }

    public static void loadStarredTableAccordingToPageNumber(final MyQuery query, final int pageNumber) {
        final YoneticiBaseInterface ADMIN = AdminManager.getSession().getAdmin();
        UIUtil.showGlassPanel();
        greetingService.getAdmin(ADMIN.getId(), new AsyncCallback<Admin>() {

            @Override
            public void onSuccess(Admin admin) {
                HashSet<Long> starHashSet = admin.getStarList();
                final ArrayList<Long> fullStarList = new ArrayList<Long>();
                fullStarList.addAll(starHashSet);
                Collections.reverse(fullStarList);
                if (fullStarList.size() == 0) {
                    UIUtil.hideYukleniyorThingy();
                    UIUtil.showMessage("You have not starred an application yet.");
                    dataTable.clearAll();
                    pagingHolder.clear();
                    return;
                }
                final ArrayList<Long> lastArrayList = new ArrayList<Long>();
                if (fullStarList.size() >= (Preferences.APPLICATION_COUNT_PER_PAGE * pageNumber)) {
                    int starSize = 0;
                    for (int i = 0; i < Preferences.APPLICATION_COUNT_PER_PAGE; i++) {
                        starSize = (Preferences.APPLICATION_COUNT_PER_PAGE * (pageNumber - 1)) + i;
                        lastArrayList.add(fullStarList.get(starSize));
                    }
                } else {
                    int starSize = 0;
                    int starNumber = fullStarList.size() % Preferences.APPLICATION_COUNT_PER_PAGE;
                    for (int i = 0; i < starNumber; i++) {
                        starSize = (Preferences.APPLICATION_COUNT_PER_PAGE * (pageNumber - 1)) + i;
                        lastArrayList.add(fullStarList.get(starSize));
                    }
                }
                final ArrayList<ApplicationDTO> applicationDTOArrayList = new ArrayList<ApplicationDTO>();
                starredAndReturnedApplicationCount = 0;
                if (lastArrayList.size() > 0) {
                    UIUtil.showGlassPanel();
                }
                for (int i = 0; i < lastArrayList.size(); i++) {
                    greetingService.getApplication(lastArrayList.get(i), new AsyncCallback<ApplicationDTO>() {

                        @Override
                        public void onSuccess(ApplicationDTO result) {
                            starredAndReturnedApplicationCount++;
                            applicationDTOArrayList.add(result);
                            if (starredAndReturnedApplicationCount == lastArrayList.size()) {
                                UIUtil.hideYukleniyorThingy();
                                callLoadTable(query, pageNumber, applicationDTOArrayList, fullStarList);
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            starredAndReturnedApplicationCount++;
                            if (starredAndReturnedApplicationCount == lastArrayList.size()) {
                                callLoadTable(query, pageNumber, applicationDTOArrayList, fullStarList);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                UIUtil.showMessage("Error E.506");
            }
        });
    }

    private static void callLoadTable(final MyQuery query, final int pageNumber, final ArrayList<ApplicationDTO> applicationDTOArrayList, ArrayList<Long> fullStarList) {
        ResultAppListWrapper resultAppListWrapper = new ResultAppListWrapper(applicationDTOArrayList, applicationDTOArrayList.size(), pageNumber);
        int fromLimit = Preferences.APPLICATION_COUNT_PER_PAGE * (pageNumber - 1);
        currentPageNumber = pageNumber;
        currentQuery = query;
        totalStarredApplicationCount = fullStarList.size();
        boolean starredFlag = true;
        loadTable(query, fromLimit, resultAppListWrapper, starredFlag);
    }

    private void refreshStarredPagingBlock(Boolean starred) {
        pagingHolder.clear();
        ListApplicationsScreen.pageLinkListForCss.clear();
        if (totalStarredApplicationCount == 0) {
            return;
        }
        int totalPageNumber = (totalStarredApplicationCount / Preferences.APPLICATION_COUNT_PER_PAGE) + 1;
        if (totalStarredApplicationCount % Preferences.APPLICATION_COUNT_PER_PAGE == 0) {
            totalPageNumber--;
        }
        if (totalPageNumber == 1) {
            return;
        }
        if (currentPageNumber == 1) {
            sonrakiSayfaLink.setText("Next Page ");
            pagingHolder.add(sonrakiSayfaLink);
        } else if (currentPageNumber == totalPageNumber) {
            oncekiSayfaLink.setText("Previous Page ");
            pagingHolder.add(oncekiSayfaLink);
        } else {
            sonrakiSayfaLink.setText("Next Page ");
            oncekiSayfaLink.setText("Previous Page ");
            pagingHolder.add(oncekiSayfaLink);
            pagingHolder.add(sonrakiSayfaLink);
        }
        oncekiSayfaLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                currentPageNumber--;
                loadStarredTableAccordingToPageNumber(currentQuery, currentPageNumber);
            }
        });
        sonrakiSayfaLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                currentPageNumber++;
                loadStarredTableAccordingToPageNumber(currentQuery, currentPageNumber);
            }
        });
        int fromNumber = (currentPageNumber - 1) * Preferences.APPLICATION_COUNT_PER_PAGE;
        int toNumber = currentPageNumber * Preferences.APPLICATION_COUNT_PER_PAGE;
        if (currentPageNumber == totalPageNumber) {
            toNumber = totalStarredApplicationCount;
        }
        HTML applicationCountLabel = new HTML("Showing <b>" + fromNumber + "</b> - <b>" + toNumber + "</b> of <b>" + totalStarredApplicationCount + "</b>");
        pagingHolder.add(applicationCountLabel);
    }
}
