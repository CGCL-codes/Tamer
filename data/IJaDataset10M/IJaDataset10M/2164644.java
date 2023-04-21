package playground.mrieser.core.mobsim.api;

public interface MobsimKeepAlive {

    /**
	 * @return <code>true</code> if the simulation should continue running,
	 * 		<code>false</code> otherwise.
	 */
    public boolean keepAlive();
}
