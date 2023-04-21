package com.ivis.xprocess.ui.util.priorities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import com.ivis.xprocess.core.Score;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.impl.ScoreImpl;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.util.debug.DebugUtil;

public class DisplayOrderedScoreSet implements ScoreProvider {

    private Map<String, Score> rawScores;

    private Map<Integer, Set<Score>> mappedScores;

    private int count;

    public DisplayOrderedScoreSet() {
        super();
        rawScores = new HashMap<String, Score>();
        mappedScores = new HashMap<Integer, Set<Score>>();
    }

    public DisplayOrderedScoreSet(Iterable<Score> scores) {
        super();
        rawScores = new HashMap<String, Score>();
        mappedScores = new HashMap<Integer, Set<Score>>();
        Set<Score> scoreSet;
        int i = 0;
        int position = 0;
        for (Score score : scores) {
            i++;
            if (score.getTask() != null) {
                rawScores.put(score.getTask().getUuid(), score);
            }
            scoreSet = mappedScores.get(score.getScore());
            if (scoreSet == null) {
                scoreSet = new HashSet<Score>();
            }
            position = scoreSet.size() + 1;
            if (score.getIntProperty(POSITION) == 0) {
                score.setIntProperty(POSITION, position);
            }
            scoreSet.add(score);
            mappedScores.put(score.getScore(), scoreSet);
        }
        count = i;
    }

    public void reset(Iterable<Score> newScores) {
        mappedScores = new HashMap<Integer, Set<Score>>();
        rawScores = new HashMap<String, Score>();
        for (Score score : newScores) {
            add(score);
            if (score.getTask() != null) {
                rawScores.put(score.getTask().getUuid(), score);
            }
        }
    }

    public Comparator<? super Score> comparator() {
        return null;
    }

    public SortedSet<Score> subSet(Score start, Score stop) {
        boolean add = false;
        SortedSet<Score> scores = new DisplayOrderedScoreSet();
        for (Score score : this) {
            if (add) {
                scores.add(score);
            }
            if (score.equals(start)) {
                add = true;
            }
            if (score.equals(stop)) {
                add = false;
            }
        }
        return scores;
    }

    public SortedSet<Score> headSet(Score score) {
        SortedSet<Score> scores = new DisplayOrderedScoreSet();
        for (Score s : this) {
            if (s.equals(score)) {
                break;
            }
            scores.add(s);
        }
        return scores;
    }

    public SortedSet<Score> tailSet(Score score) {
        SortedSet<Score> scores = new DisplayOrderedScoreSet();
        boolean add = false;
        for (Score s : this) {
            if (add) {
                scores.add(s);
            }
            if (s.equals(score)) {
                add = true;
            }
        }
        return scores;
    }

    public Score first() {
        return toArray(new Score[0])[0];
    }

    public Score last() {
        if (count < 1) {
            return null;
        }
        return toArray(new Score[0])[count - 1];
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count > 0;
    }

    public boolean contains(Object compareTo) {
        if (!(compareTo instanceof Score) && !(compareTo instanceof Xtask)) {
            return false;
        }
        for (Score score : this) {
            if (score.equals(compareTo)) {
                return true;
            }
            if ((score.getTask() != null) && (score.getTask().equals(compareTo))) {
                return true;
            }
        }
        return false;
    }

    public Iterator<Score> iterator() {
        DisplayOrderedScoresIterator iterator = new DisplayOrderedScoresIterator(toArray(new Score[0]), this);
        return iterator;
    }

    public Object[] toArray() {
        Object[] scores = new Score[count];
        int c = 0;
        for (Integer i : mappedScores.keySet()) {
            for (Score score : mappedScores.get(i)) {
                scores[c] = score;
                c++;
            }
        }
        Arrays.sort(scores);
        return scores;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] arg0) {
        Score[] scores = new Score[count];
        int c = 0;
        for (int i : mappedScores.keySet()) {
            Set<Score> scoreSet = mappedScores.get(i);
            for (Score score : scoreSet) {
                scores[c] = score;
                c++;
            }
        }
        Arrays.sort(scores);
        return (T[]) scores;
    }

    public boolean add(Score score) {
        boolean isNew;
        int s = score.getScore();
        Set<Score> scores = mappedScores.get(score.getScore());
        if (scores == null) {
            scores = new HashSet<Score>();
            isNew = true;
            if (score.getIntProperty(POSITION) == 0) {
                score.setIntProperty(POSITION, 1);
            }
            scores.add(score);
        } else {
            isNew = scores.add(score);
        }
        mappedScores.put(s, scores);
        if (score.getTask() != null) {
            rawScores.put(score.getTask().getUuid(), score);
        }
        if (isNew) {
            count++;
        }
        return isNew;
    }

    public boolean remove(Object obj) {
        if (!(obj instanceof Score) && !(obj instanceof Xtask)) {
            return false;
        }
        Score score;
        if (obj instanceof Xtask) {
            score = find((Xtask) obj);
        } else {
            score = (Score) obj;
        }
        if (score == null) {
            return false;
        }
        Set<Score> scores = mappedScores.get(score.getScore());
        if (scores == null) {
            return false;
        }
        boolean returnValue = scores.remove(score);
        rawScores.remove(score);
        if (returnValue) {
            count--;
        }
        return returnValue;
    }

    public boolean containsAll(Collection<?> arg0) {
        return false;
    }

    public boolean addAll(Collection<? extends Score> scores) {
        if (scores == null) {
            return false;
        }
        if (scores.size() == 0) {
            return false;
        }
        boolean exists = false;
        for (Score score : scores) {
            exists = add(score);
        }
        return exists;
    }

    public boolean retainAll(Collection<?> arg0) {
        return false;
    }

    public boolean removeAll(Collection<?> arg0) {
        return false;
    }

    public void clear() {
        this.mappedScores.clear();
        this.rawScores.clear();
        this.count = 0;
    }

    public Score find(Xtask task) {
        return rawScores.get(task.getUuid());
    }

    public void dump() {
        DebugUtil.debugSystemOut("DUMP\n====\n");
        if (count == 0) {
            DebugUtil.debugSystemOut("<empty set>");
            return;
        }
        for (Score score : this) DebugUtil.debugSystemOut(score.toString());
    }

    public boolean isLonely(Score score) {
        Set<Score> scoreSet = getScoreSet(score);
        if (scoreSet == null) {
            return false;
        }
        if (scoreSet.size() == 1) {
            return true;
        }
        return false;
    }

    private Set<Score> getScoreSet(Score score) {
        Set<Score> scoreSet;
        for (int i : mappedScores.keySet()) {
            scoreSet = mappedScores.get(i);
            if (scoreSet.contains(score)) {
                return scoreSet;
            }
        }
        return null;
    }

    public Score before(Score score) {
        Score previous = null;
        for (Score s : this) {
            if (score.equals(s)) {
                break;
            }
            previous = s;
        }
        return previous;
    }

    public Score after(Score score) {
        Score next = null;
        Iterator<?> iter = iterator();
        while (iter.hasNext()) {
            if (iter.next().equals(score)) {
                if (iter.hasNext()) {
                    next = (Score) iter.next();
                }
            }
        }
        return next;
    }

    @Override
    public String toString() {
        StringBuffer scoreList = new StringBuffer();
        for (Score score : this) scoreList.append(score + ":" + score.getScore() + "\n");
        return scoreList.toString();
    }

    public boolean isFirstInSet(Score target) {
        Set<Score> scores = mappedScores.get(target.getScore());
        if (scores == null) {
            return false;
        }
        if (scores.size() == 1) {
            return false;
        }
        Score[] sortedScores = scores.toArray(new Score[0]);
        Arrays.sort(sortedScores);
        if (target.equals(sortedScores[0])) {
            return true;
        }
        return false;
    }

    public boolean isLastInSet(Score target) {
        Set<Score> scores = mappedScores.get(target.getScore());
        if (scores == null) {
            return false;
        }
        if (scores.size() == 1) {
            return false;
        }
        Score[] sortedScores = scores.toArray(new Score[0]);
        Arrays.sort(sortedScores);
        if (target.equals(sortedScores[sortedScores.length - 1])) {
            return true;
        }
        return false;
    }

    public Score lastInSet(Score target) {
        Set<Score> scores = mappedScores.get(target.getScore());
        if (scores == null) {
            return null;
        }
        Score[] scoreSet = scores.toArray(new Score[0]);
        Arrays.sort(scoreSet);
        return scoreSet[scoreSet.length - 1];
    }

    public Score firstInSet(Score target) {
        Set<Score> scores = mappedScores.get(target.getScore());
        if (scores == null) {
            return null;
        }
        Score[] scoreSet = scores.toArray(new Score[0]);
        Arrays.sort(scoreSet);
        return scoreSet[0];
    }

    public void add(Xtask task, int scoreValue) {
        ScoreImpl score = (ScoreImpl) UIPlugin.getPersistenceHelper().createRecord(Score.class, task);
        score.initialize(task, scoreValue);
        add(score);
    }

    public void add(Xtask task, int scoreValue, int position) {
        ScoreImpl score = (ScoreImpl) UIPlugin.getPersistenceHelper().createRecord(Score.class, task);
        score.initialize(task, scoreValue);
        score.setIntProperty(POSITION, position);
        add(score);
    }

    public Set<Score> getSetFor(Score score) {
        return mappedScores.get(score.getScore());
    }

    public boolean isLonely(Xtask task) {
        return isLonely(find(task));
    }

    public ScoreProvider newSet() {
        return new DisplayOrderedScoreSet();
    }

    class DisplayOrderedScoresIterator implements Iterator<Score> {

        private Score[] scores;

        private Score lastScore;

        private int count;

        private int len;

        private Set<?> set;

        private DisplayOrderedScoresIterator(Score[] scores, Set<?> set) {
            this.scores = scores;
            count = 0;
            len = scores.length;
            this.set = set;
        }

        public boolean hasNext() {
            return count < len;
        }

        public Score next() {
            Score score = null;
            if (hasNext()) {
                score = scores[count];
                lastScore = score;
                count++;
                return score;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            set.remove(lastScore);
        }
    }
}
