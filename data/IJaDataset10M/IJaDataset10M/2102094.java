package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 11, 2008
 * Time: 4:42:44 PM
 */
public class PerceptronController implements Controller, Evolvable {

    Perceptron drivePerceptron;

    public PerceptronController() {
        this.drivePerceptron = new Perceptron(4, 2);
    }

    private PerceptronController(Perceptron mlp) {
        this.drivePerceptron = mlp;
    }

    public Action control(SensorModel model) {
        Action action = new Action();
        double[] inputs = { 1, model.getSpeed(), model.getAngleToTrackAxis(), model.getTrackPosition() };
        double[] outputs = drivePerceptron.propagate(inputs);
        action.gear = 1;
        action.accelerate = outputs[0] * 2;
        action.steering = outputs[1] * 2;
        return action;
    }

    public void mutate() {
        drivePerceptron.mutate();
    }

    public Evolvable copy() {
        return new PerceptronController(drivePerceptron.copy());
    }

    public void reset() {
    }
}
