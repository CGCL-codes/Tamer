package de.uni_leipzig.lots.common.objects.training;

import de.uni_leipzig.lots.common.objects.Entity;
import de.uni_leipzig.lots.common.objects.Parent;
import de.uni_leipzig.lots.common.objects.task.*;
import de.uni_leipzig.lots.common.util.PersistentCollectionGetter;
import de.uni_leipzig.lots.common.util.PersistentCollectionSetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kiel
 * @version $Id: UserMultipleChoiceTask.java,v 1.9 2007/10/23 06:30:34 mai99bxd Exp $
 * @hibernate.joined-subclass table="usermultiplechoicetask" node="user-multiple-choice-task"
 * @hibernate.key column="id"
 * @hibernate.cache usage="read-write"
 * @see de.uni_leipzig.lots.common.objects.task.MultipleChoiceTask
 */
public class UserMultipleChoiceTask extends UserTask<MultipleChoiceTask> implements Parent, AutoCorrectable {

    /**
     * The choices which the user sees.
     */
    @NotNull
    private List<UserChoice> userChoices = new ArrayList<UserChoice>();

    /**
     * Constructor for hibernate.
     */
    public UserMultipleChoiceTask() {
    }

    public UserMultipleChoiceTask(@NotNull UserTrainingPaper userTrainingPaper, int index) {
        super(userTrainingPaper, index);
    }

    @NotNull
    public Class<UserMultipleChoiceTask> getEntityType() {
        return UserMultipleChoiceTask.class;
    }

    @Override
    public boolean isAutoCorrectable() {
        return true;
    }

    /**
     * Bewertet die Lösung der Multiplechoiceaufgabe.
     * <p/>
     * Zu jeder Multiplechoiceaufgabe existiert ein {@link #getMarkingScale() Bewertungsmaßstab}. Daraus wird
     * eine maximale Punktzahl, minimale Punktzahl und Strafpunktzahl extrahiert.
     * <p/>
     * Bei jeder {@link UserChoice#isSolutionRight() falsch gelösten} Antwort der Multiplechoiceaufgabe, wird
     * von der Gesamtpunktzahl die Strafpunktzahl abgezogen. Allerdings kann die Punktzahl nie unter die
     * minimale Punktzahl fallen. Wenn überhaupt keine Antwort gelöst wurde, gilt die Multiplechoiceaufgabe
     * als nicht bearbeitet und der Benutzer erhält null Punkte für diese Multiplechoiceaufgabe.
     *
     * @return die Bewertung dieser Multiplechoiceaufgabe
     */
    public synchronized Marking correct() {
        MultipleChoiceMarkingScale markingScale = getMarkingScale();
        if (markingScale == null) {
            throw new IllegalArgumentException("Can not correct the UserMultipleChoiceTask \"" + toString() + "\" because its MarkingScale is null.");
        }
        int points = markingScale.getMaxPoints();
        final int minPoints = markingScale.getMinPoints();
        final int penalty = markingScale.getPenalty();
        boolean minOneChecked = false;
        for (UserChoice userChoice : getUserChoices()) {
            assert this == userChoice.getUserMultipleChoiceTask();
            if (userChoice.isChecked()) minOneChecked = true;
            if (!userChoice.isSolutionRight()) points -= penalty;
        }
        if (minOneChecked) {
            points = (points < minPoints) ? minPoints : points;
        } else {
            points = 0;
        }
        UserTaskMarking userTaskMarking = new UserTaskMarking();
        userTaskMarking.setMaxPoints(markingScale.getMaxPoints());
        userTaskMarking.setPoints(points);
        setMarking(userTaskMarking);
        setCorrector(null);
        return userTaskMarking;
    }

    /**
     * <h3>Hibernate hints:</h3> <ul> <li> Use <tt>cascade="all, delete-orphan"</tt> to delete userchoices
     * when they are removed from this list. This is done because the userchoices can't live without having a
     * task as parent.</li> </ul>
     *
     * @return
     * @hibernate.list cascade="all, delete-orphan" inverse="true" node="user-choices" fetch="join"
     * @hibernate.key column="usermultiplechoicetaskid" not-null="true"
     * @hibernate.list-index column="index"
     * @hibernate.one-to-many class="de.uni_leipzig.lots.common.objects.training.UserChoice"
     * @hibernate.cache usage="read-write"
     * @see UserChoice
     */
    @NotNull
    @PersistentCollectionGetter
    public List<UserChoice> getUserChoices() {
        return userChoices;
    }

    @PersistentCollectionSetter
    public void setUserChoices(@NotNull List<UserChoice> userChoices) {
        this.userChoices = userChoices;
    }

    @Override
    @Nullable
    public MultipleChoiceMarkingScale getMarkingScale() {
        MarkingScale trainingPaperMarkingScale = getUserTrainingPaper().getMarkingScale();
        if (trainingPaperMarkingScale != null && trainingPaperMarkingScale instanceof MultipleChoiceMarkingScale) {
            return (MultipleChoiceMarkingScale) trainingPaperMarkingScale;
        } else {
            return getOriginal().getMarkingScale();
        }
    }

    public String getQuestion() {
        return getOriginal().getQuestion();
    }

    public int getNumberOfRightChoices() {
        return getOriginal().getNumberOfRightChoices();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMultipleChoiceTask)) return false;
        final UserMultipleChoiceTask userTask = (UserMultipleChoiceTask) o;
        if (!getUserTrainingPaper().equals(userTask.getUserTrainingPaper())) return false;
        if (getIndex() != userTask.getIndex()) return false;
        return true;
    }

    @Override
    public final int hashCode() {
        int result;
        result = getUserTrainingPaper().hashCode();
        result = 29 * result + getIndex();
        return result;
    }

    @Override
    protected StringBuffer getAttributes() {
        StringBuffer sb = super.getAttributes();
        sb.append(", userChoices = ").append(userChoices);
        return sb;
    }
}
