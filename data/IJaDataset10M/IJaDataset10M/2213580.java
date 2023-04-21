package net.sf.insim4j.insim.racetracking;

import java.util.Set;
import net.sf.insim4j.insim.InSimResponsePacket;
import net.sf.insim4j.insim.flags.ConfirmFlag;
import net.sf.insim4j.insim.flags.PlayerFlag;

/**
 * InSim Result packet. <br />
 * From LFS. Result (qualify or confirmed finish)
 * 
 * @author Jiří Sotona
 */
public interface InSimResult extends InSimResponsePacket {

    /**
	 * Getter. <br />
	 * player's unique id
	 * 
	 * @return the PLID
	 */
    public int getPLID();

    /**
	 * Getter. <br />
	 * UserName
	 * 
	 * @return the userName
	 */
    public String getUserName();

    /**
	 * Getter. <br />
	 * NickName
	 * 
	 * @return the NickName
	 */
    public String getNickName();

    /**
	 * Getter. <br />
	 * Number plate - NO ZERO AT END!; String[8]
	 * 
	 * @return the NumberPlate
	 */
    public String getPlate();

    /**
	 * Getter. <br />
	 * Car name; String[4]
	 * 
	 * @return the carName
	 */
    public String getCarName();

    /**
	 * Getter. <br />
	 * total race time (ms)
	 * 
	 * @return the total race time
	 */
    public long getRaceTime();

    /**
	 * Getter. <br />
	 * best lap time (ms)
	 * 
	 * @return the best lap time
	 */
    public long getBestTime();

    /**
	 * Getter.
	 * 
	 * @return the spA
	 */
    public byte getSpA();

    /**
	 * Getter. <br />
	 * number of pit stops
	 * 
	 * @return the numStops
	 */
    public int getNumStops();

    /**
	 * Getter. <br />
	 * Confirmation flags
	 * 
	 * @return the flags
	 */
    public Set<ConfirmFlag> getConfirmFlags();

    /**
	 * Getter.
	 * 
	 * @return the spB
	 */
    public byte getSpB();

    /**
	 * Getter. <br />
	 * laps completed
	 * 
	 * @return the lapsDone
	 */
    public int getLapsDone();

    /**
	 * Getter. <br />
	 * player flags
	 * 
	 * @return the flags
	 */
    public Set<PlayerFlag> getPlayerFlags();

    /**
	 * Getter. <br />
	 * finish or qualify pos (0 = win / 255 = not added to table)
	 * 
	 * @return the resultNum
	 */
    public int getResultNum();

    /**
	 * Getter. <br />
	 * total number of results (qualify doesn't always add a new one)
	 * 
	 * @return the numResults
	 */
    public int getNumResults();

    /**
	 * Getter. <br />
	 * penalty time in seconds (already included in race time)
	 * 
	 * @return the penaltySecs
	 */
    public int getPenaltySecs();
}
