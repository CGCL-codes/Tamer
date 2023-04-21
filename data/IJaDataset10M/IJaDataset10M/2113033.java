package pastrywiki.client;

import java.io.Serializable;
import pastrywiki.client.www.Wiki;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class add all switch and object to GWT interface
 * @author Gabriel
 */
public class PanelWiki extends Composite implements ClickHandler {

    private GreetingServiceAsync greetingService = (GreetingServiceAsync) GWT.create(GreetingService.class);

    private StackPanel panelMenu = new StackPanel();

    private VerticalPanel navBar = new VerticalPanel();

    private VerticalPanel main = new VerticalPanel();

    private HTML editButton = new HTML("<a href='javascript:;'>New Wiki</a>", true);

    private HTML listButton = new HTML("<a href='javascript:;'>List Wiki</a>", true);

    private HTML randomButton = new HTML("<a href='javascript:;'>Random Wiki</a>", true);

    private HTML contactButton = new HTML("<a href='javascript:;'>Contact</a>", true);

    private Button send = new Button("Click me");

    private TextBox text = new TextBox();

    private Button editpage = new Button("Edit");

    private Wiki w = new Wiki();

    private HTML principal = new HTML("<h2>" + "What is Pastrywiki?" + "</h2>" + "<hr style=\"width: 100%; height: 4px;\">" + "<p style=\"text-align: left;\">" + "The PastryWiki project consist a new implementation to use p2p easypastry (node connection).<br>" + "You can introduce a Wiki Page if you consider interesting things to explain.<br>" + "It's simple to use, you can create a new Wiki Page, edit and consult anything.</p>" + "<p>For more information, please, contact me.</p>" + "<br>");

    private HTML contact = new HTML("<h2>" + "Contact to Staff" + "</h2>" + "<hr style=\"width: 100%; height: 4px;\">" + "<p style=\"text-align: left;\">" + "Name: Gabriel Ignaci Pérez Forcadell<br>" + "E-mail: gabypf@gmail.com<br>" + "Web Project: https://sourceforge.net/projects/uniwiki/" + "</p>" + "<p></p><br>");

    /**
	 * Constructor
	 */
    public PanelWiki() {
        Command cmdMain = new Command() {

            public void execute() {
                main.clear();
                main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                main.add(principal);
                RootPanel.get().add(main, 210, 200);
            }
        };
        Command cmdEdit = new Command() {

            public void execute() {
                newWiki();
            }
        };
        Command cmdList = new Command() {

            public void execute() {
                generatedList();
            }
        };
        Command cmdRandom = new Command() {

            public void execute() {
                seeRandomWiki();
            }
        };
        Command cmdContact = new Command() {

            public void execute() {
                main.clear();
                main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                main.add(contact);
                RootPanel.get().add(main, 210, 200);
            }
        };
        MenuBar navigation = new MenuBar(true);
        navigation.addItem("New Wiki", cmdEdit);
        navigation.addItem("List Wiki", cmdList);
        navigation.addItem("Random Wiki", cmdRandom);
        MenuBar headerBar = new MenuBar();
        headerBar.addItem("Main", cmdMain);
        headerBar.addItem("Navigation", navigation);
        headerBar.addItem("Contact", cmdContact);
        headerBar.setAnimationEnabled(true);
        headerBar.setWidth("100%");
        VerticalPanel listSearch = new VerticalPanel();
        listSearch.add(text);
        listSearch.add(send);
        VerticalPanel list = new VerticalPanel();
        list.add(editButton);
        list.add(listButton);
        list.add(randomButton);
        list.add(contactButton);
        panelMenu.setSize("200", "200");
        panelMenu.add(listSearch, "Search");
        panelMenu.add(list, "Navigation");
        editButton.addClickHandler(this);
        listButton.addClickHandler(this);
        send.addClickHandler(this);
        editpage.addClickHandler(this);
        randomButton.addClickHandler(this);
        contactButton.addClickHandler(this);
        navBar.add(panelMenu);
        navBar.setSize("200", "200");
        main.add(principal);
        RootPanel.get().add(headerBar, 200, 155);
        RootPanel.get().add(main, 210, 200);
        initWidget(navBar);
    }

    /**
	 * Method capture and apple functions to click in buttons
	 */
    @Override
    public void onClick(ClickEvent event) {
        Object sender = event.getSource();
        if (sender == editButton) {
            newWiki();
        } else if (sender == send) {
            main.clear();
            main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
            seePage(text.getText());
        } else if (sender == editpage) {
            main.clear();
            main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
            Editor newedit = new Editor(w.getId(), w.getValue());
            main.add(newedit);
            RootPanel.get().add(main, 210, 200);
        } else if (sender == listButton) {
            generatedList();
        } else if (sender == randomButton) {
            seeRandomWiki();
        } else if (sender == contactButton) {
            main.clear();
            main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
            main.add(contact);
            RootPanel.get().add(main, 210, 200);
        }
    }

    /**
	 * This method show editor to create new wiki page
	 */
    public void newWiki() {
        main.clear();
        main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        Editor newedit = new Editor();
        main.add(newedit);
        RootPanel.get().add(main, 210, 200);
    }

    /**
	 * This method show a wiki page
	 * @param name String name Wiki
	 */
    public void seePage(String name) {
        greetingService.editService(name, new AsyncCallback<Serializable>() {

            public void onSuccess(Serializable result) {
                w = (Wiki) result;
                HTML web = new HTML();
                if (w.getId().equals("empty")) {
                    web = new HTML("<p>" + w.getValue() + "</p>");
                    main.add(web);
                } else {
                    web = new HTML("<h2>" + w.getId() + "</h2>" + "<hr style=\"width: 100%; height: 4px;\">" + "<p>" + w.getValue() + "</p>");
                    main.add(web);
                    main.add(editpage);
                }
                RootPanel.get().add(main, 210, 200);
            }

            public void onFailure(Throwable caught) {
            }
        });
    }

    /**
	 * This method generate a list of wikis
	 */
    public void generatedList() {
        main.clear();
        main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        greetingService.get("listWiki", new AsyncCallback<Serializable>() {

            public void onSuccess(Serializable result) {
                Wiki listWiki = (Wiki) result;
                main.add(new HTML("<h2>All List Wiki</h2><hr style=\"width: 100%; height: 4px;\"><br><ul>"));
                String list[] = listWiki.getValue().split(",");
                for (int i = 0; i < list.length; i++) {
                    final String name = list[i];
                    HTML wikiLink = new HTML("<li style=\"text-align: left;\"><a id=\"" + name + "\"href='javascript:;'>" + name + "</a></li>");
                    wikiLink.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            main.clear();
                            main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                            seePage(name);
                        }
                    });
                    main.add(wikiLink);
                }
                main.add(new HTML("</ul>"));
                RootPanel.get().add(main, 210, 200);
            }

            public void onFailure(Throwable caught) {
            }
        });
    }

    /**
	 * This method show a random wiki
	 */
    public void seeRandomWiki() {
        main.clear();
        main.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        greetingService.get("listWiki", new AsyncCallback<Serializable>() {

            public void onSuccess(Serializable result) {
                Wiki listWiki = (Wiki) result;
                String list[] = listWiki.getValue().split(",");
                int r = Random.nextInt(list.length);
                String name = list[r];
                seePage(name);
            }

            public void onFailure(Throwable caught) {
            }
        });
    }
}
