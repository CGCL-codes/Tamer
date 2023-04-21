package neembuu.vfs;

/**
 * This is yet to be designed.<br/><br/>
 * Each file gets it own instance of DiskManager.
 * DiskManager saves data to the disk;
 * minimum duty expected by it. <br/>
 * Ideally a disk manager should have the following features :<ol>
 * <li>Reduce the delay caused in serving read requests by caching
 * recently downloaded data as it is known that this would be surely
 * be requested very soon.</li>
 * <li>Reduce number of writes to the disk</li>
 * </ol>
 * <br/>
 * For inspiration refer to source of vuze : com.aelitis.azureus.core.diskmanager <br/>
 * Vuze had a disk manager since 2003. DiskManager makes more sense in
 * Neembuu, because for Neembuu, main purpose of diskmanager optimize
 * "watch as you download" by satisfying read requests more quickly.
 * In olden days diskmanager in vuze mainly served to reduce load
 * on the disk and increase it's life, right now with implementation
 * of "Watch as you download" in vuze, the diskmanager automatically also
 * serves to optimize completion of read requests.
 * @author Shashank Tulsyan
 */
public interface DiskManager {
}
