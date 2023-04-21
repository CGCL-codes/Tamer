package ca.sqlpower.architect.ddl.critic;

/**
 * Listener that can be added to a {@link Criticizer} to be notified when
 * criticisms get added, removed, or changed.
 */
public interface CriticismListener {

    public void criticismAdded(CriticismEvent e);

    public void criticismRemoved(CriticismEvent e);
}
