package jeplus;

import java.io.Serializable;

/**
 *
 * @author yzhang
 */
public class EPlusConfigPortable extends EPlusConfig implements Serializable {

    public String Platform = "windows";

    public String EpExeDir = "./";

    public String EpExeCmd = "EnergyPlus.exe";

    public String EpExpObjCmd = "ExpandObjects.exe";

    public String EpMacroCmd = "EPMacro.exe";

    public String EpRVCmd = "ReadVarsEso.exe";

    public String EpWthrDir = "./WeatherData/";

    public String LogFile = "jeplus.log";

    public EPlusConfigPortable() {
    }

    ;
}
