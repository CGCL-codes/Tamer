package raceclient.ga;

import raceclient.Action;

/**
 *
 * @author Diego
 */
public interface EvaluableIndividual {

    public int getDrive(int angle, int trackPos, int speed, int track);

    public int getDrive(int angle, int trackPos, int speed, int track, int opp);

    public float[] getDrivePop(int angle, int trackPos, int speed, int track);

    public void createRandom(GAParams params);

    public String mutate(GAParams params, boolean[] rules);

    public EvaluableIndividual cross(EvaluableIndividual ind, GAParams params);

    public float getBrake(float value);

    public float getAccel(float value);

    public float getFitness();

    public float getFitness1();

    public float getFitness2();

    public float getCircuitLength();

    public float[] getGenome(int which);

    public boolean hasStarted();

    public void setGenome(int which, float[] values);

    public void setFitness(float f);

    public boolean setRemainingDistance(float distRaced);

    public void setStarted(boolean started);

    public void printGenome();

    public Action getCurrentAction(float step);

    public void setAction(Action ac);
}
