package bsh.commands;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.PlexReader;

/**
	Set the global defaults for input logging. The default directory is 
  ~/plex/ and the default setting is false.	
	void setLogging( [ String default_dir ] )
	void setLogging( [ boolean default_on_flag ] )
*/
public class setLogging {

    public static String usage() {
        return "usage: setLogging( boolean default_on_flag )\n" + "       setLogging( String default_logdir )\n";
    }

    /**
     Implement setLogging(String default_dir) command.
	*/
    public static void invoke(Interpreter env, CallStack callstack, String default_dir) {
        PlexReader.setDefaultDir(default_dir);
    }

    /**
     Implement setLogging(boolean default_on_flag) command.
	*/
    public static void invoke(Interpreter env, CallStack callstack, boolean default_flag) {
        PlexReader.setDefaultFlag(default_flag);
    }
}
