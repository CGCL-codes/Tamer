package br.ufmg.ubicomp.decs.server.eventservice.manager;

public abstract class AbstractEventServiceManager extends AbstractManager {

    protected EventManager eventService = EventManager.getInstance();

    @Override
    protected String getEntityName() {
        return null;
    }

    @Override
    public EventManager getEventService() {
        return eventService;
    }
}
