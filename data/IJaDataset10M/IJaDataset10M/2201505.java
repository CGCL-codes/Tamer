package playground.dgrether.analysis;

import org.matsim.basic.v01.Id;
import org.matsim.utils.geometry.CoordI;

/**
 * This Class is able to write a PlanComparison Object to a String.
 * @author dgrether
 *
 */
public class PlanComparisonStringWriter implements PlanComparisonWriter {

    /**
	 * A StringBuffer to concat the result.
	 */
    private StringBuffer _buffer;

    /**
	 * @see playground.dgrether.analysis.PlanComparisonWriter#write(playground.dgrether.analysis.PlanComparison)
	 */
    public void write(PlanComparison pc) {
        double score1, score2;
        String linesep = System.getProperty("line.separator");
        CoordI coordinates;
        _buffer = new StringBuffer();
        _buffer.append("Id");
        _buffer.append("\t");
        _buffer.append("X-Coordinate");
        _buffer.append("\t");
        _buffer.append("Y-Coordinate");
        _buffer.append("\t");
        _buffer.append("Score first plan");
        _buffer.append("\t");
        _buffer.append("Score second plan");
        _buffer.append("\t");
        _buffer.append("Improvement");
        _buffer.append(linesep);
        for (Id i : pc.getPersonIds()) {
            score1 = pc.getFirstScore(i);
            score2 = pc.getSecondScore(i);
            coordinates = pc.getHomeLocation(i).getCoord();
            _buffer.append(i.toString());
            _buffer.append("\t");
            _buffer.append(coordinates.getX());
            _buffer.append("\t");
            _buffer.append(coordinates.getY());
            _buffer.append("\t");
            _buffer.append(score1);
            _buffer.append("\t");
            _buffer.append(score2);
            _buffer.append("\t");
            if (score1 < score2) _buffer.append("1"); else if (score1 == score2) _buffer.append("0"); else _buffer.append("-1");
            _buffer.append(" ");
            _buffer.append(linesep);
        }
    }

    /**
	 *
	 * @return the resulting String
	 */
    public String getResult() {
        return _buffer.toString();
    }
}
