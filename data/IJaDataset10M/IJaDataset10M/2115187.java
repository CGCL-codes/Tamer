package agex.sim;

import java.util.Date;

public interface Sim2RealTime {

    public Date getRealTime(long simTime) throws Exception;

    public int getNumberOfCycles();

    /**
      * @return true se � uma simulacao de dados intraday (falso caso contr�rio)
      */
    public boolean isIntraday();
}
