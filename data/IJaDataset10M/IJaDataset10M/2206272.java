package ArenaSimulator;

import java.awt.Color;
import java.awt.Graphics2D;
import com.thoughtworks.xstream.annotations.*;
import java.util.ArrayList;

/**
 * Defines a physical barrier in the arena.
 */
@XStreamAlias("feature")
public class Feature {

    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String _name;

    @XStreamAlias("type")
    @XStreamAsAttribute
    protected String _type;

    @XStreamAlias("time")
    @XStreamAsAttribute
    protected Double _timeLimit;

    @XStreamImplicit
    private ArrayList<Vector2D> _nodes = new ArrayList<Vector2D>();

    private transient Boolean _isWall;

    private transient Boolean _isLedge;

    private transient Boolean _isGoal;

    private transient Boolean _timerOn;

    private transient Double _endTime;

    private transient Double _score;

    public Feature() {
        readResolve();
    }

    public Feature(String type, Double time) {
        setType(type);
        _timeLimit = time;
        readResolve();
    }

    public Object readResolve() {
        if (_type == null) _type = "wall";
        if (_timeLimit == null) _timeLimit = 10.0;
        _isWall = false;
        _isLedge = false;
        _isGoal = false;
        _timerOn = false;
        _endTime = 0.0;
        reset();
        setType(_type);
        return this;
    }

    private void setType(String type) {
        _type = type;
        if (_type.equalsIgnoreCase("wall")) {
            _isWall = true;
        } else if (_type.equalsIgnoreCase("ledge")) {
            _isLedge = true;
        } else if (_type.equalsIgnoreCase("goal")) {
            _isGoal = true;
        }
    }

    /**
     * Defines a barrier which spans points A (aX, aY) and B (bX, bY).  The barrier
     * is either a wall (isWall = true) or a ledge (isWall = false).
     * @param aX
     * @param aY
     * @param bX
     * @param bY
     * @param isWall
     */
    public void define(Double aX, Double aY, Double bX, Double bY, String type) {
        addNode(aX, aY);
        addNode(bX, bY);
        setType(type);
    }

    public void addNode(Double x, Double y) {
        _nodes.add(new Vector2D(x, y));
    }

    public int getNodeCount() {
        return _nodes.size();
    }

    public void draw(int x, int y, Double scale, Graphics2D g2d) {
        if (_isWall) {
            g2d.setColor(Color.blue);
        } else if (_isLedge) {
            g2d.setColor(Color.red);
        } else if (_isGoal) {
            g2d.setColor(Color.green);
        }
        for (int i = 1; i < getNodeCount(); i++) {
            g2d.drawLine(_nodes.get(i - 1).getScaledX(scale) + x, _nodes.get(i - 1).getScaledY(scale) + y, _nodes.get(i).getScaledX(scale) + x, _nodes.get(i).getScaledY(scale) + y);
        }
    }

    /**
     * Records the number of points scored by the robot on the feature.
     * @return 
     */
    public Double getScore() {
        return _score;
    }

    public Vector2D getNode(int index) {
        if (index < getNodeCount()) {
            return _nodes.get(index);
        } else {
            return _nodes.get(0);
        }
    }

    public Double getMaximumX() {
        Double maximum = 0.0;
        Double value = 0.0;
        for (int i = 0; i < _nodes.size(); i++) {
            value = _nodes.get(i).getX();
            if (value > maximum) {
                maximum = value;
            }
        }
        return maximum;
    }

    public Double getMinimumX() {
        Double minimum = 0.0;
        Double value = 0.0;
        for (int i = 0; i < _nodes.size(); i++) {
            value = _nodes.get(i).getX();
            if (value < minimum) {
                minimum = value;
            }
        }
        return minimum;
    }

    public Double getMaximumY() {
        Double maximum = 0.0;
        Double value = 0.0;
        for (int i = 0; i < _nodes.size(); i++) {
            value = _nodes.get(i).getY();
            if (value > maximum) {
                maximum = value;
            }
        }
        return maximum;
    }

    public Double getMinimumY() {
        Double minimum = 0.0;
        Double value = 0.0;
        for (int i = 0; i < _nodes.size(); i++) {
            value = _nodes.get(i).getY();
            if (value < minimum) {
                minimum = value;
            }
        }
        return minimum;
    }

    /**
     * Is true if the currentTime is greater than the end time for the feature's timer.
     * @param Time in seconds.
     * @return 
     */
    public Boolean timeIsUp(Double currentTime) {
        if (currentTime > _endTime) return true; else return false;
    }

    /**
     * Resets the feature feature by zeroing the timer.
     */
    public void startTimer(Double currentTime) {
        if (!_timerOn) {
            _timerOn = true;
            _endTime = currentTime + _timeLimit;
        }
    }

    public Double stopTimer(Double currentTime) {
        if (_timerOn) {
            _timerOn = false;
            return 100 + 100 * (_endTime - currentTime) / _endTime;
        } else {
            return 0.0;
        }
    }

    /**
     * Is true if the feature is a wall.
     * @return 
     */
    public Boolean isWall() {
        return _isWall;
    }

    /**
     * Is true if the feature is a ledge.
     * @return 
     */
    public Boolean isLedge() {
        return _isLedge;
    }

    /**
     * Resets the feature so that it can be used to test another robot.
     */
    public void reset() {
        startTimer(0.0);
        _score = 0.0;
    }
}
