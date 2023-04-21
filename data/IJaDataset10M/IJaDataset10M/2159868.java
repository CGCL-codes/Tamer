package fr.crnan.videso3d.layers;

import java.util.List;
import fr.crnan.videso3d.graphics.Route;

/**
 * Layer destiné à afficher les routes<br />
 * Permet d'afficher sélectivement une ou plusieurs routes, selon leur nom ou leur type
 * @author Bruno Spyckerelle
 * @version 0.2.1
 */
public interface RoutesLayer {

    public void addRoute(Route route, String name);

    public Route getRoute(String name);

    public void displayAllRoutes();

    public void hideAllRoutes();

    public void displayAllRoutesPDR();

    public void displayAllRoutesAwy();

    public void hideAllRoutesPDR();

    public void hideAllRoutesAWY();

    public void displayRoute(String route);

    public void hideRoute(String route);

    public void highlight(String name);

    public void unHighlight(String name);

    public List<String> getVisibleRoutes();
}
