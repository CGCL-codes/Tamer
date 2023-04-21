package net.sf.brightside.luxurycruise.tapestry.pages;

import java.util.List;
import net.sf.brightside.luxurycruise.core.tapestry.SpringBean;
import net.sf.brightside.luxurycruise.metamodel.Location;
import net.sf.brightside.luxurycruise.service.crud.impl.ReadCommand;
import net.sf.brightside.luxurycruise.service.misc.BeanLookup;
import net.sf.brightside.luxurycruise.service.misc.impl.SpringBeanLookup;
import net.sf.brightside.luxurycruise.tapestry.util.UniversalFinder;
import org.apache.tapestry.Asset;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.ioc.annotations.Inject;

public class LocationOverview {

    @Inject
    @Path("context:styles/styles.css")
    private Asset styles;

    public Asset getStyles() {
        return styles;
    }

    @Inject
    @SpringBean("net.sf.brightside.luxurycruise.service.crud.impl.ReadCommand")
    private ReadCommand<Location> reader;

    @InjectPage
    private LocationDetails details;

    private Location location;

    private Location someLocation;

    public Location getSomeLocation() {
        return someLocation;
    }

    public void setSomeLocation(Location someLocation) {
        this.someLocation = someLocation;
    }

    public List<Location> getAllLocations() {
        reader.setClazz(Location.class);
        return reader.execute();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @OnEvent(component = "locationDetailsLink")
    protected Object onShowDetails(String name) {
        UniversalFinder finder = new UniversalFinder();
        Location chosen = finder.find(getAllLocations(), "name", name);
        details.setLocation(chosen);
        return details;
    }

    @OnEvent(component = "newLocationLink")
    protected Object onAddNew() {
        details.setLocation(getFreshNewLocation());
        return details;
    }

    private Location getFreshNewLocation() {
        BeanLookup lookup = new SpringBeanLookup();
        return lookup.getBean(Location.class);
    }

    @OnEvent(component = "HomeLink")
    protected Object onHome() {
        return Index.class;
    }
}
