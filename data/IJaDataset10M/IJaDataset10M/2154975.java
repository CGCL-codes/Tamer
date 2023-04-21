package com.plato.etoh.client.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.plato.etoh.client.GreetingService;
import com.plato.etoh.client.GreetingServiceAsync;
import com.plato.etoh.client.util.UIUtil;
import com.plato.etoh.client.util.Util;

public class BasvuruyuTamamlaEkrani extends Composite {

    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    public BasvuruyuTamamlaEkrani() {
        FlexTable flexTable = new FlexTable();
        flexTable.setStyleName("decorated-table");
        flexTable.addStyleName("form-table");
        UIUtil.hideYukleniyor();
        initWidget(flexTable);
        {
            flexTable.setWidget(0, 0, new HTML("<h4>Başvuru Tamamlama</h4>"));
            flexTable.getFlexCellFormatter().setColSpan(0, 0, 3);
            HTML htmlBavurunuzuTamamladnzdaAadakiaadaki = new HTML("", true);
            htmlBavurunuzuTamamladnzdaAadakiaadaki.setHTML("<li>Başvurunuzu tamamlamak için  aşağıdaki linki kullanın.</li> " + "<li>Bu işlemi başvurunuzdaki tüm soruları cevapladığınızda yapmalısınız. </li>" + "<li>Tüm cevaplarınızı kontrol ettiğinizden emin olunuz.</li>" + "<li><b>Tamamlama işleminden sonra cevaplarınızı değiştirmeniz mümkün olmayacaktır</b>. </li>");
            htmlBavurunuzuTamamladnzdaAadakiaadaki.setStyleName("basvuru-tamamla");
            flexTable.setWidget(1, 0, htmlBavurunuzuTamamladnzdaAadakiaadaki);
        }
        {
            Hyperlink button = new Hyperlink();
            button.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    if (Window.confirm("Başvuruyu tamamladan önce tüm ortak soruların ve ekibinizdeki tüm üyelerin kişisel sorularının tamamlandığından emin olmalısınız.\nBaşvuruyu tamamla?")) {
                        greetingService.basvuruyuTamamla(ApplicantManager.getSession().getAppId(), new AsyncCallback<Boolean>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                UIUtil.showMessage("Hata 26.9.1 - Başvurunuz tamamlanamadı. Bağlantı hatası.");
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                if (result.booleanValue() == true) {
                                    greetingService.basvuruyuTamamlaMailleriniGonder(ApplicantManager.getSession().getUsername(), new AsyncCallback<Boolean>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            UIUtil.showMessage("Hata 29.9.2");
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                            Util.logout();
                                            RootPanel.get("content").clear();
                                            RootPanel.get("sidebar").clear();
                                            RootPanel.get("welcome").clear();
                                            RootPanel.get("content").add(new HTML("Başvurunuz tamamlanmıştır."));
                                        }
                                    });
                                } else {
                                    UIUtil.showMessage("İşleminiz başarısız. Tekrar deneyiniz.");
                                }
                            }
                        });
                    }
                }
            });
            button.setHTML("Cevaplar tamamlandı - başvuruyorum (uz)");
            button.setStyleName("basvuru-tamamla");
            flexTable.setWidget(2, 0, button);
        }
        RootPanel.get("content").clear();
    }
}
