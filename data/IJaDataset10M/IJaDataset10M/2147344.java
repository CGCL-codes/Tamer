package ie.blackoutscout.backend.xml;

import ie.blackoutscout.common.beans.implementations.Standing;
import ie.blackoutscout.common.beans.interfaces.IBean;
import ie.blackoutscout.common.beans.interfaces.IStanding;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author John
 */
public class StandingXmlHandler extends BlackoutEntityXmlHandler {

    @Override
    public IBean readFromXml(Element el, Long timeStamp) {
        IStanding standing = new Standing();
        standing.setStandingId(getLongValue(el, "id"));
        standing.setStanding(getIntValue(el, "standing"));
        standing.setTeamId(getLongValue(el, "teamid"));
        standing.setPlayed(getIntValue(el, "played"));
        standing.setWin(getIntValue(el, "w"));
        standing.setLose(getIntValue(el, "l"));
        standing.setDraw(getIntValue(el, "d"));
        standing.setForPoints(getIntValue(el, "for"));
        standing.setAgainstPoints(getIntValue(el, "against"));
        standing.setBonus1(getIntValue(el, "b1"));
        standing.setBonus2(getIntValue(el, "b2"));
        standing.setPoints(getIntValue(el, "points"));
        standing.setBrTimeStamp(timeStamp);
        logger.info("New Standing: " + standing.getTeamId());
        return standing;
    }
}
