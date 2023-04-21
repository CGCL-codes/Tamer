package org.jfonia.pitch;

import org.jfonia.connect5.basics.BasicValueNode;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.basics.Observer;
import org.jfonia.connect5.basics.ValueNode;
import org.jfonia.connect5.numerical.UnidirectionalIntSumRelation;
import org.jfonia.connect5.relations.UnidirectionalEqual;

/**
 *
 * @author Rik Bauwens
 */
public class RankNodes {

    private final int midiCentralC = 60;

    private boolean locked;

    private MutableValueNode<Integer> centralCRankNode;

    private UnidirectionalEqual<Integer> centralCRelation;

    private MutableValueNode<Integer> base40RankNode;

    private MutableValueNode<Integer> rankNode;

    private MutableValueNode<Integer> accidentalNode;

    private MutableValueNode<Integer> rank12Node;

    private MutableValueNode<Integer> midiRankNode;

    public RankNodes(MutableValueNode<Integer> base40RankNode) {
        requestLock();
        this.base40RankNode = base40RankNode;
        if (base40RankNode == null) return;
        this.centralCRankNode = new BasicValueNode<Integer>(0);
        MusicalInterval musicalInterval = Base40.toMusicalInterval(base40RankNode.getValue());
        this.rankNode = new BasicValueNode<Integer>(musicalInterval.getRank() + centralCRankNode.getValue());
        this.accidentalNode = new BasicValueNode<Integer>(musicalInterval.getAccidental());
        this.rank12Node = new BasicValueNode<Integer>(musicalInterval.getRank12());
        this.midiRankNode = new BasicValueNode<Integer>(musicalInterval.getRank12() + midiCentralC);
        new UnidirectionalIntSumRelation(this.rank12Node, new BasicValueNode<Integer>(midiCentralC), this.midiRankNode);
        init();
        removeLock();
    }

    private void init() {
        centralCRankNode.addObserver(new Observer() {

            public void onNotify(Object source) {
                if (!requestLock()) return;
                MusicalInterval musicalInterval = Base40.toMusicalInterval(base40RankNode.getValue());
                rankNode.setValue(musicalInterval.getRank() + centralCRankNode.getValue());
                rank12Node.setValue(musicalInterval.getRank12());
                removeLock();
            }
        });
        base40RankNode.addObserver(new Observer() {

            public void onNotify(Object source) {
                if (!requestLock()) return;
                requestLock();
                MusicalInterval musicalInterval = Base40.toMusicalInterval(base40RankNode.getValue());
                rankNode.setValue(musicalInterval.getRank() + centralCRankNode.getValue());
                rank12Node.setValue(musicalInterval.getRank12());
                removeLock();
            }
        });
        rankNode.addObserver(new Observer() {

            public void onNotify(Object source) {
                if (!requestLock()) return;
                requestLock();
                MusicalInterval musicalInterval = new MusicalInterval(rankNode.getValue() - centralCRankNode.getValue(), accidentalNode.getValue());
                rank12Node.setValue(musicalInterval.getRank12());
                base40RankNode.setValue(Base40.toBase40(musicalInterval));
                removeLock();
            }
        });
        accidentalNode.addObserver(new Observer() {

            public void onNotify(Object source) {
                if (!requestLock()) return;
                requestLock();
                MusicalInterval musicalInterval = new MusicalInterval(rankNode.getValue() - centralCRankNode.getValue(), accidentalNode.getValue());
                rank12Node.setValue(musicalInterval.getRank12());
                base40RankNode.setValue(Base40.toBase40(musicalInterval));
                removeLock();
            }
        });
        rank12Node.addObserver(new Observer() {

            public void onNotify(Object source) {
                if (!requestLock()) return;
                requestLock();
                MusicalInterval musicalInterval = new MusicalInterval(rank12Node.getValue());
                rankNode.setValue(musicalInterval.getRank() + centralCRankNode.getValue());
                accidentalNode.setValue(musicalInterval.getAccidental());
                base40RankNode.setValue(Base40.toBase40(musicalInterval));
                removeLock();
            }
        });
    }

    public RankNodes setCentralCRankNode(ValueNode<Integer> centralCRankNode) {
        if (centralCRelation != null) centralCRelation.switchOff();
        centralCRelation = new UnidirectionalEqual<Integer>(centralCRankNode, this.centralCRankNode);
        return this;
    }

    private boolean requestLock() {
        if (locked) return false; else {
            locked = true;
            return true;
        }
    }

    private void removeLock() {
        locked = false;
    }

    public MutableValueNode<Integer> getBase40RankNode() {
        return base40RankNode;
    }

    public MutableValueNode<Integer> getRankNode() {
        return rankNode;
    }

    public MutableValueNode<Integer> getAccidentalNode() {
        return accidentalNode;
    }

    public MutableValueNode<Integer> getRank12Node() {
        return rank12Node;
    }

    public MutableValueNode<Integer> getMidiRankNode() {
        return midiRankNode;
    }
}
