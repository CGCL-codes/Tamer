package ArenaSimulator;

import com.thoughtworks.xstream.annotations.*;
import java.util.ArrayList;

/**
 * A clan is a collection of evolutionally related robots.
 */
@XStreamAlias("clan")
public class Clan {

    @XStreamImplicit
    private ArrayList<Robot> _members;

    @XStreamAlias("radius")
    @XStreamAsAttribute
    private Double _radius;

    @XStreamAlias("angularVelocity")
    @XStreamAsAttribute
    private Double _maximumAngularVelocity;

    @XStreamAlias("linearVelocity")
    @XStreamAsAttribute
    private Double _maximumLinearVelocity;

    @XStreamAlias("restingSonar")
    @XStreamAsAttribute
    private Double _restingSonarDistance;

    public Clan() {
        readResolve();
    }

    private Object readResolve() {
        if (_radius == null) _radius = 5.0;
        if (_maximumAngularVelocity == null) _maximumAngularVelocity = 2.0;
        if (_maximumLinearVelocity == null) _maximumLinearVelocity = 2.0;
        if (_restingSonarDistance == null) _restingSonarDistance = 20.0;
        if (_members == null) {
            _members = new ArrayList<Robot>();
            for (int i = 0; i < 10; i++) {
                _members.add(new Robot(this));
            }
        }
        for (int i = 0; i < _members.size(); i++) {
            _members.get(i).setClan(this);
        }
        return this;
    }

    /**
     * Returns the radius in centimeters of clan member robots.
     * @return 
     */
    public Double getRobotRadius() {
        return _radius;
    }

    /**
      * Returns the maximum angular velocity in centimeters of clan member robots.
      * @return 
      */
    public Double getRobotAngularVelocity() {
        return _maximumAngularVelocity;
    }

    /**
     * Returns the maximum linear velocity in centimeters of clan member robots.
     * @return 
     */
    public Double getRobotLinearVelocity() {
        return _maximumLinearVelocity;
    }

    /**
     * Returns the distance in centimeters at which the angled sonar intercepts
     * the ground for clan member robots.
     * @return 
     */
    public Double getRobotRestingSonarDistance() {
        return _restingSonarDistance;
    }

    /**
     * Returns the index of the poorest scoring clan member.  This is the member
     * that is at risk of being knocked out of the clan by a better scoring child.
     * @return
     */
    public int indexPoorestScoringMember() {
        Double poorestScore = 100000.0;
        int poorestScoringMember = 0;
        for (int i = 0; i < _members.size(); i++) {
            if (_members.get(i).getScore() < poorestScore) {
                poorestScore = _members.get(i).getScore();
                poorestScoringMember = i;
            }
        }
        return poorestScoringMember;
    }

    /**
     * Compares the nominee's score to that of the lowest-scoring clan member and replaces
     * the that member with the nominee if the nominee's score is higher.
     * @param nominee
     */
    public void nominate(Robot nominee) {
        int poorestScoringIndex = indexPoorestScoringMember();
        Double poorestScore = _members.get(poorestScoringIndex).getScore();
        if (nominee.getScore() > poorestScore + 100.0) _members.set(poorestScoringIndex, nominee);
    }

    /**
     * Randomly obtains a child from one of the clan members.
     * @return 
     */
    public Robot getChild() {
        int parent = (int) (Math.random() * _members.size());
        return _members.get(parent).getClone();
    }

    /**
     * Adds a new member to the clan without replacing an existing member.  This increases
     * the size of the clan by one.
     * @param member
     * @see ArenaSimulator.Clan.Nominate
     */
    public void add(Robot member) {
        member.setClan(this);
        _members.add(member);
    }

    public void add(Robot member, int count) {
        member.setClan(this);
        _members.add(member);
        for (int i = 1; i < count; i++) {
            _members.add(member.getClone());
        }
    }
}
