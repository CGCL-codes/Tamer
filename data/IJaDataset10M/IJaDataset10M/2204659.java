package net.sf.insim4j.insim.state;

import java.util.Set;
import net.sf.insim4j.insim.InSimRequestPacket;
import net.sf.insim4j.insim.flags.StateFlag;

/**
 * State Flags Packet. <br />
 * To LFS. Used to set these states
 * 
 * <pre>
 * ISS_SHIFTU_FOLLOW    - following car
 * ISS_SHIFTU_NO_OPT    - SHIFT+U buttons hidden
 * ISS_SHOW_2D          - showing 2d display
 * ISS_MPSPEEDUP        - multiplayer speedup option
 * ISS_SOUND_MUTE       - sound is switched off
 * </pre>
 * 
 * Other states must be set by using keypresses or messages. See InSim.txt
 * 
 * @author Jiří Sotona
 */
public interface InSimStateFlagsPack extends InSimRequestPacket {

    /**
	 * Getter.
	 * 
	 * @return the flags
	 */
    public Set<StateFlag> getFlags();

    /**
	 * Getter. <br />
	 * 0 = off / 1 = on
	 * 
	 * @return the offOn
	 */
    public int getOffOn();

    /**
	 * Getter. <br />
	 * spare
	 * 
	 * @return the sp3
	 */
    public byte getSp3();
}
