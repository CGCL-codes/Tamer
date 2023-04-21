package org.boffyflow.ru.jathlete.data;

import org.boffyflow.ru.jathlete.data.*;
import org.boffyflow.ru.jathlete.compute.*;

/********************************************************************
 <pre>
 <B>Run</B>

 @version        1.0
 @author         Robert Uebbing
 
 Creation Date : 25-Jan-2001
  
 </pre>
********************************************************************/
public class Run extends Workout {

    private long _shoe = 0;

    private String _surface = "unknown";

    private String _kor = "unknown";

    public Run() {
    }

    public int getDistance() {
        int d = 0;
        for (int i = 0; i < _splits.size(); i++) {
            d += ((Split) _splits.get(i)).getDistance();
        }
        return d;
    }

    public int getTime() {
        double t = 0.0;
        for (int i = 0; i < _splits.size(); i++) {
            t += ((Split) _splits.get(i)).getTime();
        }
        return cGeneral.rnd(t);
    }

    public int getAverageHeartrate() {
        double hr = 0.0;
        for (int i = 0; i < _splits.size(); i++) {
            hr += ((Split) _splits.get(i)).getHeartrate();
        }
        return cGeneral.rnd(hr);
    }

    public void setSurface(String surface) {
        if (surface.length() == 0) {
            _surface = "unknown";
        } else {
            _surface = surface;
        }
    }

    public String getSurface() {
        return _surface;
    }

    public void setKindOfRun(String kor) {
        _kor = kor;
    }

    public String getKindOfRun() {
        return _kor;
    }

    public void setShoe(long shoe) {
        _shoe = shoe;
    }

    public long getShoe() {
        return _shoe;
    }

    /**
	 * Returns the XML respresentation of this object.
	 *
	 * @param		number of tabs (for beautification)
	 * @return  	A XML (String) representation of this object.
	 */
    public String toXML(int tabs) {
        StringBuffer sb = new StringBuffer();
        StringBuffer tstr = new StringBuffer();
        String cstr;
        String indoor;
        if (isIndoors()) {
            indoor = "yes";
        } else {
            indoor = "no";
        }
        for (int i = 0; i < tabs; i++) {
            tstr.append("\t");
        }
        if (getComment().length() > 0) {
            cstr = getComment();
        } else {
            cstr = "";
        }
        sb.append(tstr.toString() + "<Run start=\"" + getTimeStart() + "\" ");
        sb.append("end=\"" + getTimeEnd() + "\" indoors=\"" + indoor + "\" surface=\"" + getSurface() + "\" ");
        sb.append("kindofrun=\"" + getKindOfRun() + "\" shoe=\"" + getShoe() + "\">\n");
        sb.append(tstr.toString() + "\t<Location>" + getLocation() + "</Location>\n");
        sb.append(tstr.toString() + "\t<Comment rating=\"" + getRating() + "\">" + cstr + "</Comment>\n");
        for (int j = 0; j < getNumberOfSplits(); j++) {
            sb.append(getSplit(j).toXML(tabs + 1));
        }
        sb.append(tstr.toString() + "</Run>\n");
        return sb.toString();
    }
}
