package de.andreavicentini.tid.vorverkauf.registrationapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.andreavicentini.tid.vorverkauf.domain.client.IPerson;
import de.andreavicentini.tid.vorverkauf.domain.client.PersonImpl;
import de.andreavicentini.tid.vorverkauf.domain.client.PersonService;
import de.andreavicentini.tid.vorverkauf.domain.client.PersonServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RegistrationApp implements EntryPoint {

    PersonServiceAsync persons = GWT.create(PersonService.class);

    private final class ClickHandlerImplementation implements ClickHandler {

        public void onClick(ClickEvent event) {
            boolean isValid = true;
            IPerson p = new PersonImpl();
            p.setTitle(IPerson.Title.valueOf(fTitle.getValue(fTitle.getSelectedIndex())));
            p.setName(fName.getText());
            p.setFirstName(fFirstName.getText());
            p.setMail(fMail.getText());
            p.setInterestedInEvents(fInterestedInEvents.getValue());
            try {
                Integer value = Integer.parseInt(fKarten.getText());
                p.setTicketCount(value);
                fKarten.removeStyleName("vorverkauf-Error");
                fKarten.setTitle(null);
            } catch (NumberFormatException e) {
                fKarten.addStyleName("vorverkauf-Error");
                fKarten.setTitle(fKarten.getText() + " is not a valid number!");
                isValid = false;
            }
            isValid &= checkMandatory(fName, Elements.name);
            if (isValid) persons.register(p, new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    System.out.println("failure");
                    ErrorDialog d = new ErrorDialog(caught);
                    d.center();
                }

                public void onSuccess(String result) {
                    System.out.println("success");
                    fConfirmation.setText(result);
                    fConfirmation.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
                    pDeck.showWidget(1);
                }
            });
        }

        private boolean checkMandatory(TextBox text, Elements element) {
            if (isEmpty(text.getText())) {
                text.addStyleName("vorverkauf-Error");
                text.setTitle(element.label() + " muss ausgefüllt werden!");
                return false;
            } else {
                text.removeStyleName("vorverkauf-Error");
                return true;
            }
        }
    }

    public enum Elements {

        title(null), name("Name"), firstName("Vorname"), karten("Anzahl Karten"), mail("Mail Adresse");

        private final String label;

        Elements(String display) {
            this.label = display;
        }

        public String label() {
            return this.label;
        }
    }

    private final TextBox fName = new TextBox();

    private final TextBox fFirstName = new TextBox();

    private final CheckBox fInterestedInEvents = new CheckBox();

    private final TextBox fMail = new TextBox();

    private final Label fEuro = new Label(" ");

    private final TextBox fKarten = new TextBox();

    private final ListBox fTitle = new ListBox();

    private final Label fMessage = new Label(" ");

    private final Label fConfirmation = new Label();

    private final DeckPanel pDeck = new DeckPanel();

    public void onModuleLoad() {
        fTitle.addItem("Herr");
        fTitle.addItem("Frau");
        fKarten.setText("1");
        fEuro.setText("(" + 1 * IPerson.TICKET + ",- €)");
        fKarten.addKeyUpHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                try {
                    Integer value = Integer.parseInt(fKarten.getText());
                    fEuro.setText("(" + value * IPerson.TICKET + ",- €)");
                } catch (NumberFormatException e) {
                }
            }
        });
        VerticalPanel fFlow = new VerticalPanel();
        fFlow.add(fKarten);
        fFlow.add(fEuro);
        FormTable table = new FormTable(Elements.values().length);
        table.addRow(Elements.title, fTitle);
        table.addRow(Elements.firstName, fFirstName);
        table.addRow(Elements.name, fName);
        table.addRow(Elements.mail, fMail);
        table.addRow(Elements.karten, fFlow);
        Button fSend = new Button("OK");
        fSend.addClickHandler(new ClickHandlerImplementation());
        DockPanel pRegistration = new DockPanel();
        {
            pRegistration.add(table, DockPanel.CENTER);
            pRegistration.add(fSend, DockPanel.SOUTH);
            pRegistration.add(fMessage, DockPanel.NORTH);
        }
        pDeck.add(pRegistration);
        pDeck.add(fConfirmation);
        pDeck.showWidget(0);
        RootPanel.get("hier").add(pDeck);
    }

    private boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    private static class ErrorDialog extends DialogBox {

        public ErrorDialog(Throwable exception) {
            setText(exception.getMessage());
            this.setAnimationEnabled(true);
            Button b = new Button("OK");
            this.setWidget(b);
            b.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    ErrorDialog.this.hide();
                }
            });
            b.setFocus(true);
        }
    }

    private static class OKDialog extends DialogBox {

        public OKDialog(String text) {
            this.setText(text);
            Button b = new Button("OK");
            this.setWidget(b);
            b.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    OKDialog.this.hide();
                }
            });
        }
    }
}
