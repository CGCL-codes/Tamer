package jorgan.session;

/**
 * An element that is aware of an {@link OrganSession}.
 */
public interface SessionAware {

    public void setSession(OrganSession session);
}
