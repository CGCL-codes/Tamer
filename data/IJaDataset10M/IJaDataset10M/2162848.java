package model.core.turns;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Transformation {

    private HashMap<Long, Integer> hour_function = new HashMap<Long, Integer>();

    private HashMap<Integer, Long> pos_function = new HashMap<Integer, Long>();

    public Transformation(Time h_inic, Time h_end) {
        int i = 0;
        int turn_duration = 30;
        Time t = h_inic;
        while (h_end.after(t)) {
            hour_function.put(new Long(t.getTime()), new Integer(i));
            pos_function.put(new Integer(i), new Long(t.getTime()));
            t.setTime(t.getTime() + 1000 * 60 * turn_duration);
            i++;
        }
    }

    public long getHour(int pos) {
        long l = ((Long) pos_function.get(new Integer(pos))).longValue();
        return l;
    }

    public int getPos(long hour) {
        int h = ((Integer) hour_function.get(new Long(hour))).intValue();
        return h;
    }

    /**
	 * 
	 * @param asignedTurns list of assigned turns
	 * @return a list of bolleans with all the assigned turns in it are true
	 */
    private List<Boolean> markGivenTurns(List<Turn> givenTurns) {
        List<Boolean> result = new ArrayList<Boolean>(pos_function.size());
        int r = 0;
        while (r < pos_function.size()) {
            Boolean b = new Boolean(false);
            result.add(b);
            r++;
        }
        Iterator<Turn> i = givenTurns.iterator();
        while (i.hasNext()) {
            long turn_hour = i.next().getTime().getTime();
            int pos = getPos(turn_hour);
            result.set(pos, new Boolean(true));
        }
        return result;
    }

    /**
	 * 
	 * @param turns list<Boolean> result of method chekGivenTurns
	 * @param turn_duration int that represents turn duration in minutes 
	 * @param intervals list of intevals in wich the doctor is not available 
	 * @return List of Booleans with the unavailable turns in true
	 */
    private List<Boolean> markNonAvailable(List<Boolean> turns, int turn_duration, List<Interval> intervals) {
        Iterator i = intervals.iterator();
        while (i.hasNext()) {
            Interval intervalo = (Interval) i.next();
            Time t = new Time(intervalo.getInic().getTime());
            Time hf = intervalo.getEnd();
            while (hf.after(t)) {
                int pos = getPos(t.getTime());
                turns.set(pos, new Boolean(true));
                t.setTime(t.getTime() + 1000 * 60 * turn_duration);
            }
        }
        return turns;
    }

    /**
	 * 
	 * @param turn_duration int that represents turn duration in minutes 
	 * @param intervals list of intevals in wich the doctor is not available 
	 * @param giventurns list of already given Turns 
	 * @return List of Booleans with the unavailable turns in true  
	 */
    public List<Boolean> getBusyTurns(int turn_duration, List<Interval> intervals, List<Turn> givenTurns) {
        List<Boolean> busys = markGivenTurns(givenTurns);
        List<Boolean> busyTurns = markNonAvailable(busys, turn_duration, intervals);
        return busyTurns;
    }

    /**
	 * 
	 * @param busyTurns List<Boolean> with busy turns in true
	 * @return List<Turn> with free turns
	 */
    public List<Turn> getFreeTurns(List<Boolean> busyTurns) {
        List<Turn> free_turns = new ArrayList<Turn>();
        int size = busyTurns.size();
        int i = 0;
        while (i < size) {
            Boolean b = (Boolean) busyTurns.get(i);
            if (!b.booleanValue()) free_turns.add(new Turn(new Time(getHour(i))));
            i++;
        }
        return free_turns;
    }

    public static void main(String[] args) {
        Transformation t = new Transformation(new Time(1000 * 60 * 60 * (8 + 3)), new Time(1000 * 60 * 60 * (20 + 3)));
        Turn t1 = new Turn(new Time(1000 * 60 * 60 * (8 + 3)));
        Turn t2 = new Turn(new Time(1000 * 60 * 60 * (15 + 3)));
        Turn t3 = new Turn(new Time(1000 * 60 * 60 * (15 + 3) + 1000 * 60 * 30 * 1));
        Turn t4 = new Turn(new Time(1000 * 60 * 60 * (19 + 3)));
        List<Turn> turnosDados = new ArrayList<Turn>();
        turnosDados.add(t1);
        turnosDados.add(t2);
        turnosDados.add(t3);
        turnosDados.add(t4);
        Interval i1 = new Interval(new Time(1000 * 60 * 60 * (12 + 3)), new Time(1000 * 60 * 60 * (14 + 3) + 1000 * 60 * 30 * 1));
        Interval i2 = new Interval(new Time(1000 * 60 * 60 * (15 + 3) + 1000 * 60 * 30 * 1), new Time(1000 * 60 * 60 * (18 + 3)));
        List<Interval> intervals = new ArrayList<Interval>();
        intervals.add(i1);
        intervals.add(i2);
        List<Boolean> resultado = t.getBusyTurns(30, intervals, turnosDados);
        List<Turn> libres = t.getFreeTurns(resultado);
        System.out.println(resultado.toString());
        System.out.println(libres.toString());
    }
}
