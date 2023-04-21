package org.Message.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class Page extends Composite {

    final HorizontalPanel pageChat = new HorizontalPanel();

    final CaptionPanel util = new CaptionPanel("Personne :");

    final VerticalPanel listUtilisateur = new VerticalPanel();

    final ListBox listConnecte = new ListBox() {

        {
            addDomHandler(new DoubleClickHandler() {

                @Override
                public void onDoubleClick(DoubleClickEvent pEvent) {
                    VerticalPanel contenuPrive = new VerticalPanel();
                    final ScrollPanel afficheurPrive = new ScrollPanel();
                    afficheurPrive.setStyleName("login");
                    afficheurPrive.setPixelSize(500, 100);
                    int i = listConnecte.getSelectedIndex();
                    String distination = listConnecte.getItemText(i);
                    contenuPrive.add(afficheurPrive);
                    chatPublic.add(contenuPrive, distination);
                }
            }, DoubleClickEvent.getType());
        }
    };

    final ArrayList<String> Affiche = new ArrayList<String>();

    GreetingServiceAsync service = GWT.create(GreetingService.class);

    MyHandler click = new MyHandler();

    final VerticalPanel champChat = new VerticalPanel();

    final CaptionPanel editeur = new CaptionPanel("Editeur Partager :");

    final VerticalPanel editingPaneau = new VerticalPanel();

    final TextArea editeurPartage = new TextArea();

    final HorizontalPanel boutons = new HorizontalPanel();

    final Button editer = new Button("Editer");

    final Button sauver = new Button("Sauver");

    final TabPanel chatPublic = new TabPanel();

    final CaptionPanel chatting = new CaptionPanel("Chat :");

    final VerticalPanel chattingPaneau = new VerticalPanel();

    final ScrollPanel afficheur = new ScrollPanel() {

        {
            pollTimer = new Timer() {

                public void run() {
                    update();
                }
            };
            pollTimer.scheduleRepeating(1000);
        }
    };

    ;

    final TextArea champSaisie = new TextArea();

    final HorizontalPanel paneauAvance = new HorizontalPanel();

    final DisclosurePanel editeurHTML = new DisclosurePanel("Personaliser votre message");

    final DisclosurePanel ajouterFichier = new DisclosurePanel("Ajouter un fichier");

    MyHandler click1 = new MyHandler();

    Utilisateur a = new Utilisateur();

    public String monNom;

    public Date maDate;

    boolean oui = false;

    int ij = 0;

    public Page() {
        initWidget(pageChat);
        pageChat.addStyleName("login");
        editeurPartage.setStyleName("login");
        editeurPartage.setEnabled(false);
        pageChat.setStyleName("positionChat");
        listConnecte.setVisibleItemCount(25);
        editeurPartage.setPixelSize(500, 100);
        afficheur.setPixelSize(500, 300);
        champSaisie.setPixelSize(510, 100);
        champSaisie.addStyleName("login");
        afficheur.addStyleName("login");
        listConnecte.setStyleName("listUtilisateur");
        editeur.add(editingPaneau);
        editingPaneau.add(editeurPartage);
        editingPaneau.add(boutons);
        boutons.add(editer);
        editer.addClickHandler(click);
        boutons.add(sauver);
        sauver.addClickHandler(click);
        chatting.add(chattingPaneau);
        FlowPanel flowPanel = new FlowPanel();
        chattingPaneau.add(chatPublic);
        chatPublic.add(flowPanel, "Public");
        flowPanel.add(afficheur);
        chatPublic.setPixelSize(505, 305);
        chatPublic.selectTab(0);
        chattingPaneau.add(champSaisie);
        chattingPaneau.add(new Button("envoyer", new ClickListener() {

            @Override
            public void onClick(Widget sender) {
                messaging();
                champSaisie.setValue("");
            }
        }));
        chattingPaneau.add(paneauAvance);
        paneauAvance.add(editeurHTML);
        paneauAvance.add(ajouterFichier);
        util.add(listUtilisateur);
        listUtilisateur.add(listConnecte);
        champChat.add(editeur);
        champChat.add(chatting);
        pageChat.add(util);
        pageChat.add(champChat);
        sauver.setVisible(false);
    }

    ArrayList<String> listMessage;

    HTML text = new HTML();

    static Timer pollTimer;

    void messaging() {
        final String messagePublic1 = monNom + " :" + champSaisie.getText();
        service.ajouterMessage(messagePublic1, new AsyncCallback<ArrayList<String>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ArrayList<String> result) {
                result.add(messagePublic1);
            }
        });
    }

    public void update() {
        pollTimer = new Timer() {

            @Override
            public void run() {
                service.getCurrentMessage(new AsyncCallback<ArrayList<String>>() {

                    public void onFailure(Throwable caught) {
                    }

                    @SuppressWarnings("unchecked")
                    public void onSuccess(ArrayList<String> result) {
                        HTML tex = new HTML();
                        listMessage = result;
                        tex.setHTML("");
                        afficheur.clear();
                        final StringBuilder builder = new StringBuilder();
                        for (Iterator itr = listMessage.iterator(); itr.hasNext(); ) {
                            builder.append(itr.next());
                        }
                        tex.setHTML(builder.toString());
                        afficheur.add(tex);
                        listMessage.clear();
                    }
                });
            }
        };
        pollTimer.scheduleRepeating(1000);
    }

    class MyHandler implements ClickHandler, KeyDownHandler {

        @Override
        public void onClick(ClickEvent event) {
            if (((Button) event.getSource()).getText().equals("Editer")) editeurPartage.setEnabled(true);
            editer.setEnabled(false);
            sauver.setVisible(true);
            if (((Button) event.getSource()).getText().equals("Sauver")) editeurPartage.setEnabled(false);
            editer.setEnabled(true);
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
        }
    }
}
