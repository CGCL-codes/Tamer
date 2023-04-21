package fi.passiba.groups.ui.pages.search;

import fi.passiba.biblestudy.BibleStudyApplication;
import fi.passiba.biblestudy.BibleStudySession;
import fi.passiba.groups.ui.model.Constants;
import fi.passiba.groups.ui.model.DomainModelIteratorAdaptor;
import fi.passiba.groups.ui.model.HashcodeEnabledCompoundPropertyModel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import fi.passiba.groups.ui.pages.BasePage;
import fi.passiba.groups.ui.pages.group.EditGroupInfo;
import fi.passiba.groups.ui.pages.group.ViewGroupInfo;
import fi.passiba.groups.ui.pages.user.EditPersonContact;
import fi.passiba.hibernate.PaginationInfo;
import fi.passiba.services.address.IAddressService;
import fi.passiba.services.authenticate.IAuthenticator;
import fi.passiba.services.group.IGroupServices;
import fi.passiba.services.group.persistance.Groups;
import fi.passiba.services.persistance.Adress;
import fi.passiba.services.persistance.Person;
import fi.passiba.services.search.ISearchService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.GMapHeaderContributor;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GIcon;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;
import wicket.contrib.gmap.api.GMarkerOptions;
import wicket.contrib.gmap.api.GOverlay;
import wicket.contrib.gmap.api.GPoint;
import wicket.contrib.gmap.api.GSize;

public final class ListGroupsPanel extends Panel {

    @SpringBean
    private IGroupServices groupService;

    @SpringBean
    private IAddressService addressservice;

    @SpringBean
    private ISearchService searchService;

    @SpringBean
    private IAuthenticator authenticate;

    private Page backPage;

    private int startPage = 1, window = 20;

    private String searchType = "", searchString;

    public ListGroupsPanel(String id, String searchCriteria, String searchString, Page backPage) {
        super(id);
        this.backPage = backPage;
        init(searchCriteria, searchString, startPage, window);
    }

    public ListGroupsPanel(String id, Page backPage, long personid) {
        super(id);
        this.backPage = backPage;
        List<Groups> groupsCol = groupService.findGroupsByPersonId(personid);
        add(populateSearchResult(groupsCol, true));
        add(createSearchResultMap(groupsCol));
    }

    private void addNextandPrevious() {
        final Person loggedInPerson = getLoggInPerson();
        Link previous = new Link("previous") {

            String country = "", city = "";

            public void onClick() {
                if (loggedInPerson != null) {
                    city = loggedInPerson.getAdress().getCity();
                    country = loggedInPerson.getAdress().getCountry();
                }
                List<Groups> results = new ArrayList<Groups>(0);
                try {
                    if (searchType.equals(Constants.GroupSearchOption.TYPE.getOption())) {
                        results = searchService.findGroupsByType(country, searchString, startPage - 1, window);
                    } else if (searchType.equals(Constants.GroupSearchOption.CITY.getOption())) {
                        results = searchService.findGroupsByLocation(country, searchString, startPage - 1, window);
                    } else {
                        results = searchService.findGroupsByName(searchString, startPage - 1, window);
                    }
                } catch (ParseException ex) {
                    throw new WicketRuntimeException(ex);
                }
                setResponsePage(ListGroups.class);
            }
        };
        Link next = new Link("next") {

            String country = "", city = "";

            public void onClick() {
                if (loggedInPerson != null) {
                    city = loggedInPerson.getAdress().getCity();
                    country = loggedInPerson.getAdress().getCountry();
                }
                List<Groups> results = new ArrayList<Groups>(0);
                try {
                    if (searchType.equals(Constants.GroupSearchOption.TYPE.getOption())) {
                        results = searchService.findGroupsByType(country, searchString, startPage + 1, window);
                    } else if (searchType.equals(Constants.GroupSearchOption.CITY.getOption())) {
                        results = searchService.findGroupsByLocation(country, searchString, startPage + 1, window);
                    } else {
                        results = searchService.findGroupsByName(searchString, startPage + 1, window);
                    }
                } catch (ParseException ex) {
                    throw new WicketRuntimeException(ex);
                }
                setResponsePage(ListGroups.class);
            }
        };
        add(previous);
        add(next);
    }

    private void init(String searchCriteria, String searchString, int startPage, int window) {
        List<Groups> results = new ArrayList<Groups>(0);
        final Person loggedInPerson = getLoggInPerson();
        String country = "", city = "";
        if (loggedInPerson != null) {
            city = loggedInPerson.getAdress().getCity();
            country = loggedInPerson.getAdress().getCountry();
        }
        if (searchCriteria != null && searchString != null) {
            this.searchType = searchCriteria;
            this.searchString = searchString;
            try {
                if (searchCriteria.equals(Constants.GroupSearchOption.TYPE.getOption())) {
                    results = searchService.findGroupsByType(country, searchString, startPage, window);
                } else if (searchCriteria.equals(Constants.GroupSearchOption.CITY.getOption())) {
                    results = searchService.findGroupsByLocation(country, searchString, startPage, window);
                } else {
                    results = searchService.findGroupsByName(searchString, startPage, window);
                }
            } catch (ParseException ex) {
                throw new WicketRuntimeException(ex);
            }
        }
        add(populateSearchResult(results, false));
        add(createSearchResultMap(results));
    }

    private Person getLoggInPerson() {
        List<Person> persons = authenticate.findPerson(BibleStudySession.get().getPerson().getFk_userid().getUsername());
        Person currentLogInPerson = null;
        if (persons != null && !persons.isEmpty()) {
            currentLogInPerson = persons.get(0);
        }
        return currentLogInPerson;
    }

    private GOverlay createOverlay(String title, GLatLng latLng, String image, String shadow) {
        GIcon icon = new GIcon(urlFor(new ResourceReference(ListGroups.class, image)).toString(), urlFor(new ResourceReference(ListGroups.class, shadow)).toString()).iconSize(new GSize(64, 64)).shadowSize(new GSize(64, 64)).iconAnchor(new GPoint(19, 40)).infoWindowAnchor(new GPoint(9, 2)).infoShadowAnchor(new GPoint(18, 25));
        return new GMarker(latLng, new GMarkerOptions(title, icon));
    }

    private RefreshingView populateSearchResult(final List<Groups> results, final boolean removeFromGroup) {
        RefreshingView groups = new RefreshingView("groups") {

            @Override
            protected Iterator getItemModels() {
                return new DomainModelIteratorAdaptor<Groups>(results.iterator()) {

                    @Override
                    protected IModel model(final Object object) {
                        return new HashcodeEnabledCompoundPropertyModel((Groups) object);
                    }
                };
            }

            @Override
            protected void populateItem(Item item) {
                Link linkview = new Link("view", item.getModel()) {

                    public void onClick() {
                        Groups g = (Groups) getModelObject();
                        setResponsePage(new ViewGroupInfo(getPage(), g.getId()));
                    }
                };
                linkview.add(new Label("name", new PropertyModel(item.getModel(), "name")));
                item.add(linkview);
                item.add(new Label("congregationname", new PropertyModel(item.getModel(), "congregationname")));
                item.add(new Label("type", new PropertyModel(item.getModel(), "grouptypename")));
                item.add(new ExternalLink("link", new PropertyModel(item.getModel(), "congregationwebsiteurl")).add(new Label("congregationwebsiteurl", new PropertyModel(item.getModel(), "congregationwebsiteurl"))));
                item.add(new Link("edit", item.getModel()) {

                    public void onClick() {
                        Groups g = (Groups) getModelObject();
                        setResponsePage(new EditGroupInfo(getPage(), g.getId()));
                    }
                });
                Link delete = new Link("delete", item.getModel()) {

                    public void onClick() {
                        Groups g = (Groups) getModelObject();
                        if (removeFromGroup != true) {
                            groupService.deleteGroup(g);
                            setResponsePage(ListGroups.class);
                        } else {
                            final Person loggedInPerson = getLoggInPerson();
                            groupService.deleteGroupPersonFromGroup(loggedInPerson.getId(), g.getId());
                            setResponsePage(new EditPersonContact(getPage(), loggedInPerson.getId()));
                        }
                    }
                };
                item.add(delete);
                Link join = new Link("join", item.getModel()) {

                    public void onClick() {
                        Groups g = (Groups) getModelObject();
                        final Person loggedInPerson = getLoggInPerson();
                        List<Person> groupPersons = groupService.findGroupsPersonsByGroupId(g.getId());
                        Set<Groups> groups = new HashSet<Groups>(0);
                        groups.add(g);
                        loggedInPerson.setGroups(groups);
                        if (removeFromGroup != true) {
                            groupPersons.add(loggedInPerson);
                            Set<Person> persons = new HashSet<Person>(0);
                            for (Person per : groupPersons) {
                                persons.add(per);
                            }
                            g.setGrouppersons(persons);
                            groupService.updateGroup(g);
                            setResponsePage(ListGroups.class);
                        }
                    }
                };
                if (!removeFromGroup) {
                    join.setVisible(true);
                } else {
                    join.setVisible(false);
                }
                item.add(join);
            }
        };
        groups.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
        return groups;
    }

    private GMap2 createSearchResultMap(List<Groups> results) {
        GMap2 bottomMap = new GMap2("bottomPanel", new GMapHeaderContributor(BibleStudyApplication.get().getGoogleMapsAPIkey()));
        bottomMap.setOutputMarkupId(true);
        bottomMap.setMapType(GMapType.G_NORMAL_MAP);
        bottomMap.addControl(GControl.GLargeMapControl);
        bottomMap.setScrollWheelZoomEnabled(true);
        bottomMap.setZoom(9);
        bottomMap.setCenter(new GLatLng(60.226280212402344, 24.820398330688477));
        if (results != null) {
            for (Groups group : results) {
                long address_id = group.getAdress().getId();
                Adress address = addressservice.findAddressByAddressId(address_id);
                if (address.getLocation_lat() != 0 && address.getLocation_lng() != 0) {
                    bottomMap.addOverlay(createOverlay(group.getName(), new GLatLng(address.getLocation_lat(), address.getLocation_lng()), "groups.png", "shadow.png"));
                }
            }
            bottomMap.setVisible(true);
        } else {
            bottomMap.setVisible(false);
        }
        return bottomMap;
    }
}
