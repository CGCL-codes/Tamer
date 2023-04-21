package com.plato.etoh.client.application.mode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.plato.etoh.client.application.ApplicantManager;
import com.plato.etoh.client.application.BasvuruBilgileriEkrani;
import com.plato.etoh.client.application.BasvuruyuTamamlaEkrani;
import com.plato.etoh.client.application.BireyselSorularEkrani;
import com.plato.etoh.client.application.LeftMenu;
import com.plato.etoh.client.application.OrtakSorularEkrani;
import com.plato.etoh.client.application.SifreDegistirEkrani;
import com.plato.etoh.client.application.kisi.KisiYonetimi;
import com.plato.etoh.client.model.Constants_fuckme;
import com.plato.etoh.client.util.UIUtil;

public class SimpleModeContainer implements ClickHandler {

    private BasvuruBilgileriEkrani basvuruBilgileriPanel;

    private BireyselSorularEkrani bireyselSorularEkrani;

    private OrtakSorularEkrani ortakSorularEkrani;

    private KisiYonetimi kisiYonetimi;

    private BasvuruyuTamamlaEkrani basvuruTamamlaEkrani;

    private SifreDegistirEkrani sifreDegistirEkrani;

    private SimpleModeContainer(Long applicationId) {
        FlexTable flexTable = new FlexTable();
        int row = 0;
        int column = 0;
        basvuruBilgileriPanel = new BasvuruBilgileriEkrani(applicationId);
        flexTable.setWidget(row, 0, basvuruBilgileriPanel);
        row++;
        bireyselSorularEkrani = new BireyselSorularEkrani();
        flexTable.setWidget(row, 0, bireyselSorularEkrani);
        row++;
        ortakSorularEkrani = new OrtakSorularEkrani();
        flexTable.setWidget(row, 0, ortakSorularEkrani);
        row++;
        HTML aciklamaHtml = new HTML("<div>Yukardaki sorulara cevapları istediğiniz zaman verebilirsiniz. " + "Kaydet tuşu ile cevaplarınızı kaydedebilirsiniz. " + "Tedbir amaçlı cevaplarınızı bilgisayarınıza da bir metin dosyasına kaydedin. " + "Kaydet tuşu başvuru anlamına gelmez. Tüm cevaplarınızı verdikten sonra başvurunuzu yapınız</div>");
        aciklamaHtml.addStyleName("information");
        flexTable.setWidget(row, 0, aciklamaHtml);
        row++;
        flexTable.setWidget(row, 0, new HTML("&nbsp;"));
        row++;
        Button saveApplicationButton = new Button("");
        saveApplicationButton.addClickHandler(this);
        saveApplicationButton.setText("Kaydet");
        flexTable.setWidget(row, 0, saveApplicationButton);
        row++;
        flexTable.setWidget(row, 0, new HTML("&nbsp;"));
        row++;
        kisiYonetimi = new KisiYonetimi();
        flexTable.setWidget(row, 0, kisiYonetimi);
        row++;
        sifreDegistirEkrani = new SifreDegistirEkrani();
        flexTable.setWidget(row, 0, sifreDegistirEkrani);
        row++;
        basvuruTamamlaEkrani = new BasvuruyuTamamlaEkrani();
        flexTable.setWidget(row, 0, basvuruTamamlaEkrani);
        row++;
        RootPanel.get("content").add(flexTable);
        putLeftMenu();
    }

    public static void initialise(Long applicatoinId) {
        SimpleModeContainer instance = new SimpleModeContainer(applicatoinId);
    }

    @Override
    public void onClick(ClickEvent event) {
        UIUtil.showSadceArkayiKarart();
        basvuruBilgileriPanel.save(event);
        bireyselSorularEkrani.save(event);
        ortakSorularEkrani.save(event);
        UIUtil.scrollUp();
        UIUtil.hideSadeceArkayiKarart();
    }

    public static void putLeftMenu() {
        VerticalPanel verticalPanel = new VerticalPanel();
        VerticalPanel outerVerticalPanel = new VerticalPanel();
        outerVerticalPanel.addStyleName("left-menu");
        verticalPanel.setStyleName("inner-left-menu");
        outerVerticalPanel.add(verticalPanel);
        HTML sidePanelHTML = new HTML("<br /><h3>E-Tohum</h3>");
        verticalPanel.add(sidePanelHTML);
        FocusPanel focusPanel = new FocusPanel();
        final Label label = new Label("Şu anda basit başvuru arayüzündesiniz. Gelişmiş arayüze geçmek için tıklayınız.");
        label.setStyleName("link-label");
        label.addMouseDownHandler(new MouseDownHandler() {

            @Override
            public void onMouseDown(MouseDownEvent event) {
                Cookies.setCookie(Constants_fuckme.COOKIE_ADVANCED_MODE_STRING, "true");
                ApplicantManager.reload();
            }
        });
        label.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                label.setStyleName("label-mouse-over");
            }
        });
        label.addMouseOutHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                label.setStyleName("label-mouse-out");
            }
        });
        focusPanel.add(label);
        verticalPanel.add(focusPanel);
        RootPanel.get("sidebar").clear();
        RootPanel.get("sidebar").add(outerVerticalPanel);
    }
}
