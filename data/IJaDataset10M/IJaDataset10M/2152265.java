package travel.wicket.app.home;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.modelibra.wicket.concept.CountryLanguageChoicePanel;
import org.modelibra.wicket.container.DmPage;
import org.modelibra.wicket.container.DmPanel;
import org.modelibra.wicket.security.AppSession;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import travel.Travel;
import travel.impression.Impression;
import travel.impression.message.Messages;
import travel.impression.place.Places;
import travel.impression.traveler.Traveler;
import travel.impression.traveler.Travelers;
import travel.reference.Reference;
import travel.reference.countrylanguage.CountryLanguage;
import travel.reference.countrylanguage.CountryLanguages;
import travel.wicket.app.TravelApp;
import travel.wicket.app.about.AboutPage;
import travel.wicket.app.upload.UploadPage;
import travel.wicket.impression.traveler.EntityUpdateTablePage;

/**
 * Application home page menu panel.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-05-01
 */
public class HomePageMenuPanel extends DmPanel {

    private static Log log = LogFactory.getLog(HomePageMenuPanel.class);

    public HomePageMenuPanel(final String wicketId, DmPage dmPage) {
        super(wicketId);
        try {
            TravelApp travelApp = (TravelApp) getApplication();
            Travel travel = travelApp.getTravel();
            Impression impression = travel.getImpression();
            Reference reference = travel.getReference();
            final AppSession appSession = getAppSession();
            ViewModel placesViewModel = new ViewModel();
            placesViewModel.setModel(impression);
            Places places = impression.getPlaces();
            Places travelersOrderedByName = places.getPlacesOrderedByName(true);
            placesViewModel.setEntities(travelersOrderedByName);
            View placesView = new View();
            placesView.setWicketId("placeUpdateTable");
            placesView.setPage(dmPage);
            placesView.setUpdate(true);
            Link placeUpdateTableLink = EntityUpdateTablePage.link(placesViewModel, placesView);
            add(placeUpdateTableLink);
            ViewModel travelersViewModel = new ViewModel();
            travelersViewModel.setModel(impression);
            Travelers travelers = impression.getTravelers();
            Travelers travelersOrderedByLastFirstName = travelers.getTravelersOrderedByLastFirstName(true);
            travelersViewModel.setEntities(travelersOrderedByLastFirstName);
            View travelersView = new View();
            travelersView.setWicketId("travelerUpdateTable");
            travelersView.setPage(dmPage);
            travelersView.setUpdate(true);
            Link travelerUpdateTableLink = EntityUpdateTablePage.link(travelersViewModel, travelersView);
            add(travelerUpdateTableLink);
            ViewModel travelerMessagesViewModel = new ViewModel();
            travelerMessagesViewModel.setModel(impression);
            View travelerMessagesView = new View();
            travelerMessagesView.setWicketId("messageUpdateTable");
            Traveler traveler = (Traveler) appSession.getSignedInUser();
            if (traveler != null) {
                Messages messages = traveler.getMessages();
                travelerMessagesViewModel.setEntities(messages);
                travelerMessagesView.setPage(dmPage);
                travelerMessagesView.setUpdate(true);
            }
            Link messageUpdateTableLink = EntityUpdateTablePage.link(travelerMessagesViewModel, travelerMessagesView);
            add(messageUpdateTableLink);
            if (traveler == null) {
                messageUpdateTableLink.setVisible(false);
            }
            Link signoutLink = new Link("signout") {

                public void onClick() {
                    appSession.invalidate();
                    setResponsePage(HomePage.class);
                }
            };
            add(signoutLink);
            if (!appSession.isUserSignedIn()) {
                signoutLink.setVisible(false);
            }
            add(new Link("about") {

                public void onClick() {
                    setResponsePage(AboutPage.class);
                }
            });
            ViewModel languageViewModel = new ViewModel();
            languageViewModel.setModel(reference);
            CountryLanguages languages = reference.getCountryLanguages();
            languageViewModel.setEntities(languages);
            String languageCode = null;
            CountryLanguage defaultLanguage = null;
            languageCode = appSession.getLocale().getLanguage();
            defaultLanguage = (CountryLanguage) languages.retrieveByCode(languageCode);
            if (defaultLanguage == null) {
                defaultLanguage = (CountryLanguage) languages.retrieveByCode("en");
            }
            languageViewModel.setEntity(defaultLanguage);
            languageViewModel.getUserProperties().addUserProperty("getLanguageMethod", "getLanguage");
            languageViewModel.getUserProperties().addUserProperty("getLanguageListMethod", "getLanguageList");
            languageViewModel.getUserProperties().addUserProperty("getLanguageCodeMethod", "getLanguageCode");
            View countryLanguageView = new View();
            countryLanguageView.setWicketId("languageChoiceSection");
            Panel languageChoicePanel = new CountryLanguageChoicePanel(languageViewModel, countryLanguageView);
            add(languageChoicePanel);
            if (!travel.getDomainConfig().isI18n()) {
                languageChoicePanel.setVisible(false);
            }
            add(new Link("upload") {

                public void onClick() {
                    setResponsePage(UploadPage.class);
                }
            });
        } catch (Exception e) {
            log.error("Error in HomePageMenuPanel: " + e.getMessage());
        }
    }
}
