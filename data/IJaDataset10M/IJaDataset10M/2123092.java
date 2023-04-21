package ru.cos.sim.driver.route;

import ru.cos.sim.driver.RoadRoute;
import ru.cos.sim.services.UpdatingRouteServiceClient;
import ru.cos.sim.services.ServiceLocator;

public class UpdatingRouteProvider extends AbstractRouteProvider {

    public UpdatingRouteProvider(float updatePeriod) {
        this.updatePeriod = updatePeriod;
        this.routeServiceClient = ServiceLocator.getInstance().createUpdatingRouteServiceClient();
    }

    private float getRandomFloat() {
        return ServiceLocator.getInstance().getRandomizationService().getRandom().nextFloat();
    }

    private RoadRoute route, updatedRoute;

    private final UpdatingRouteServiceClient routeServiceClient;

    private final float updatePeriod;

    private float updateTimeout;

    @Override
    protected void init() {
        updatedRoute = route = ServiceLocator.getInstance().getInitialRouteService().findRoute(getSourceLinkId(), getDestinationNodeId());
        updateTimeout = getRandomFloat() * updatePeriod;
    }

    @Override
    public RoadRoute getCurrentRoute() {
        checkInitialization();
        return route;
    }

    @Override
    public void act(float dt) {
        checkInitialization();
        if (isTimeToUpdate(dt) || isUpdatedRouteRejected()) updateRoute();
        adoptUpdatedRoute();
    }

    private boolean isTimeToUpdate(float dt) {
        if ((updateTimeout -= dt) < 0) {
            updateTimeout += updatePeriod;
            return true;
        }
        return false;
    }

    private boolean isUpdatedRouteRejected() {
        return updatedRoute == null;
    }

    protected void rejectUpdatedRoute() {
        updatedRoute = null;
    }

    protected boolean isRouteAdoptable() {
        return updatedRoute != null && updatedRoute.getLinks().size() > 0 && updatedRoute.getLinks().get(0) == getSourceLinkId();
    }

    protected void adoptUpdatedRoute() {
        if (updatedRoute != route) if (isRouteAdoptable()) route = updatedRoute; else rejectUpdatedRoute();
    }

    protected void updateRoute() {
        updatedRoute = routeServiceClient.findRoute(getSourceLinkId(), getDestinationNodeId());
    }
}
