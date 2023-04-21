package fi.passiba.groups.ui.pages.googlemap;

import java.io.IOException;
import java.util.List;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * This page is called by the ajax request in javascript and returns xml content
 * for the markers on the page.
 * 
 */
public class GetMarkersPage extends WebPage {

    public GetMarkersPage(final PageParameters params) {
        double north = 1000;
        double south = 1200;
        double east = 2500;
        double west = 3500;
        GetLocations getLoc = new GetLocations();
        List<LocationData> markerLocs = getLoc.getLocations(south, east, north, west);
        final Document markers = new Document();
        Element root = new Element("xml");
        markers.setRootElement(root);
        for (LocationData aLoc : markerLocs) {
            Element marker = new Element("marker");
            marker.setAttribute("lat", Double.toString(aLoc.getLat()));
            marker.setAttribute("lng", Double.toString(aLoc.getLng()));
            marker.setAttribute("id", Integer.toString(aLoc.getId()));
            root.addContent(marker);
        }
        getRequestCycle().setRequestTarget(new IRequestTarget() {

            public void detach(RequestCycle requestCycle) {
            }

            public void respond(RequestCycle requestCycle) {
                XMLOutputter output = new XMLOutputter();
                try {
                    output.output(markers, requestCycle.getResponse().getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
