package agonism.ch.sheep;

/**
 * This is the interface to your sheep. Each turn, the sheep can either {@link #graze} (eat
 * grass from the common area), or {@link #fast}. If your sheep grazes and the other sheep fasts,
 * then there is a net benefit to you. If your sheep fasts and the other sheep grazes, there is a net
 * loss to you. If both sheep fast, there is a minor benefit to both of you. If both sheep graze, there
 * is a minor loss to both of you.
 */
public interface MySheep extends Sheep {

    /**
	 * Instruct the sheep to fast (not graze) on this turn.
	 * @see Move#execute
	 */
    public void fast();

    /**
	 * Instruct the sheep to graze (eat the grass) on this turn.
	 * @see Move#execute
	 */
    public void graze();
}
