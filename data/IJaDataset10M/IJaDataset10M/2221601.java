package com.fuckingbrit.q3astats.urbanterror;

import com.fuckingbrit.q3astats.KillParser;
import com.fuckingbrit.q3astats.KillPair;

/**
  * <b>Mod:<b> Urban Terror (<a href="http://www.urbanterror.net">http://www.urbanterror.net</a>)<br>
  * <b>Weapon:</b> Beretta 92FS<br>
  * <b>Mod Version:</b> 2.5
  * @version 1.0
  * @author Michael Jervis (mike@fuckingbrit.com)
  */
public class UT292FS implements KillParser {

    /**
      * We must supply a default constructor. However this does nothing.
      */
    public UT292FS() {
    }

    /**
      * Implementation of a killParse.
      * The kill line for a berreta is is:<br>
      * <victim> was pistol whipped by <killer>.<br>
      * @param kill the kill string
      * @return a KillPair ojbect with the victim and the killer in it.
      */
    public KillPair killParse(String kill) {
        int end = kill.indexOf(" was pistol");
        String victim = kill.substring(0, end);
        int start = end + 23;
        end = kill.lastIndexOf(".");
        String killer = kill.substring(start, end);
        KillPair kp = new KillPair(killer, victim);
        return kp;
    }
}
