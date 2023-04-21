package judge.object.classes;

import judge.object.alive.*;
import judge.object.*;
import judge.server.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class cleric extends CharacterClass {

    String ClassType = "cleric";

    public boolean CanBeClassCleric(Player p) {
        boolean returnval;
        if (p.getWis() < 9) returnval = false; else returnval = true;
        return returnval;
    }
}
