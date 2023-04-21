package com.fuckingbrit.q3astats;

/**
  * All games support kills. But the kill messages may vary. The message may
  * have killer before victim, or visa versa, the message may have all sorts
  * of fluff in it. The cleanest way to allow the detection of victim and killer
  * is to have a way to parse each kill message specificaly.<p>
  * The KillParser interface defines this tool. Each game's XML definition will
  * specify which class can handle parsing that kill. That class must implement
  * this interface. One class could handle all kill messages if you wanted a
  * spagetti class.
  * @see com.fuckingbrit.q3astats.Game
  * @author Michael Jervis (mike@fuckingbrit.com)
  * @version 1.0
  */
public interface KillParser {

    /**
      * This method parses a kill and returns killer and victim.
      * @param kill The kill string.
      * @return A killer and victim object.
      * @see com.fuckingbrit.q3astats.KillPair
      */
    public KillPair killParse(String kill);
}
