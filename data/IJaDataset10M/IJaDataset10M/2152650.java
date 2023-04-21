package com.ivis.xprocess.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.annotations.Element;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.ScoredSet;

@Element(designator = "PG")
public interface PrioritizedGroup extends XchangeElement, Cloneable {

    /**
     * Property name for Weight
     */
    public static final String WEIGHT = "WEIGHT";

    /**
     * Property name for Percent Lowest
     */
    public static final String PERCENT_LOWEST = "PERCENT_LOWEST";

    /**
     * Property name for Scores
     */
    public static final String SCORES = "SCORES";

    /**
     * Property name for Folder. Although its called folder is
     * should be "subject" - since it is a Prioritizable it
     * could be an Xtask
     */
    public static final String FOLDER = "FOLDER";

    /**
     * @return the factor by which normalised scores of tasks in this group are
     *         multiplied to provide their overall score.
     */
    @Property(name = WEIGHT, propertyType = PropertyType.FLOAT)
    public double getWeight();

    /**
     * Set the factor by which normalised scores of tasks in this group are
     *         multiplied to provide their overall score.
     * @param weight
     */
    public void setWeight(double weight);

    /**
     * THe subject is the Prioritizable object that contains this PrioritizedGroup
     * @param folder
     */
    public void setSubject(Prioritizable folder);

    /**
     * @return the subject
     */
    public Prioritizable getSubject();

    /**
     * @return The factor used to give the normalised score of the lowest task.
     *         The highest rank task in the group is given a score of 1.0. The
     *         lowest ranked will have a normalised score that is this
     *         percentage below the highest. Percentage must be between 100.0
     *         and 0.0. Unranked tasks, if any, will be considered as if lowest
     *         ranked.
     */
    @Property(name = PERCENT_LOWEST, propertyType = PropertyType.FLOAT)
    double getPercentLowest();

    /**
     * Set The factor used to give the normalised score of the lowest task.
     *         The highest rank task in the group is given a score of 1.0.
     *
     * @param percentLowest
     */
    void setPercentLowest(double percentLowest);

    /**
     * Underneath this creates a BigDecimal and puts the getWeight()
     * return float into it.
     *
     * @return the highest score
     */
    BigDecimal getHighestScore();

    /**
     * An alternative method of setting the weight via a BigDecimal.
     *
     * @param score
     */
    void setHighestScore(BigDecimal score);

    /**
     * The lowest is worked out by:
     * (getWeight() * getPercentLowest()) / 100.0
     *
     * @return the lowest score
     */
    BigDecimal getLowestScore();

    /**
     * An alternative way of setting the lowest percentage just by passing the score,
     * then underneath it convert it to a percentage:
     * (score.doubleValue() / getWeight()) * 100.0
     *
     * @param score
     */
    void setLowestScore(BigDecimal score);

    /**
     * @return the persisted raw scores for those tasks that have scores.
     *
     * The scores indicate the relative position of the tasks but not
     * necessarily their normalized scores. Scores for tasks that are not
     * members of the group may be returned and should be ignored. Tasks which
     * do not have a score may be assumed to be below that of the lowest
     * recorded score. This method does not check the parent-child hierarchy.<BR>
     *
     * Note: The raw scores may be changed by other methods, or by changing
     * membership of the group, possibly without changing the relative position
     * of the tasks. It is not therefore a reliable guide to priority and other
     * mthods such as getPrioritizedTasks are to be preferred.
     */
    @Property(name = SCORES, propertyType = PropertyType.RECORDSET)
    public Set<Score> getScores();

    public void resetScores(Set<Score> scores);

    /**
     * @return scores for all the tasks in the scored set, in score order - a
     *         scored set is passed in so that if this has already been obtained
     *         it does not need to be recalculated. Use getScoredSet() to get
     *         it!
     *
     * The tasks returned will be at one level only, i.e. if a task appears
     * neither its ancestors nor descendants will appear.
     * If the scores contain scores for both an ancestor and a descendant:
     * <BR> - the ancestor is not returned<BR> - its children are returned
     * (provided that those children do not have children with scores)
     * <BR> - the children will have a score equal to the higher of their
     * own score and the parent's score<BR>
     *
     * The highest normalized score will be 100.0; the lowest normalized score
     * will be *percentLowest*. Tasks may have equal position resulting in equal
     * normalized score. The normalized score for a task is calculated from its
     * score, S, the highest score H, and the lowest score, L and the
     * percentLowest, P. The normalized score = P + (100 - P)*(S-L)/(H-L)
     */
    public Map<Xtask, Double> getNormalizedScores();

    /**
     * Get the normalized scores for a specific ScoredSet. Use of BigDecimals
     * can avoid the issue with double arithmetic that can lead to inaccurate
     * numbers, i.e. what should be 1.1 comes back as 1.10000000001 after using
     * floats in arithmetic.
     *
     * @param scoredSet
     * @return an array of BigDecimals
     */
    public BigDecimal[] getNormalizedScores(ScoredSet<Xtask> scoredSet);

    @Property(propertyType = PropertyType.REFERENCE)
    public Xproject getProject();

    /**
     * @return the highest level tasks that are members of the prioritized group
     *         (i.e. parents not their children) If a task is included in the
     *         returned set it implies that the parent of the task is NOT a
     *         member.
     */
    @Property(propertyType = PropertyType.REFERENCESET)
    public Set<Xtask> getMembers();

    /**
     * @return true if the task is a member <BR> - a child task is a member if
     *         its parent is a member - a parent task may not be a member even
     *         if all its children are members (though normally it will be!)
     */
    public boolean hasAsMember(Xtask task);

    /**
     * @return a list of task-sets in priority order.<BR>
     *         The sets include all the members of the PG (i.e. the membership
     *         of WorkPackages and Categories.<BR>
     *
     * Note: this method checks the parent-child hierarchy when called and
     * returns tasks at only one level. Thus if an ancestor task appears its
     * descendants will not (and vice versa). The level returned is the lowest
     * level at which an explicit score has been stored. Note also that the
     * method does not modify the PrioritizedGroup object.
     */
    public List<Set<Xtask>> getPrioritizedTasks();

    /**
     * @return the ScoredSet of Tasks within this PrioritizedGroup
     */
    public ScoredSet<Xtask> getScoredSet();

    /**
     * Reset the scores for a specific ScoredSet list.
     *
     * @param list
     */
    public void resetScoredSet(ScoredSet<Xtask> list);
}
