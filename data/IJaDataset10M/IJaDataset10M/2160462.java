package emil.poker.entities;

import emil.poker.ai.NumOfDecisions;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Deprecated
public class PlayerType_StartHandsHist implements Serializable, NumOfDecisions {

    PlayerType_StartHandsHistKey playerType_StartHandsHistKey;

    double numOfCalls = 0;

    double numOfRaises = 0;

    double numOfFolds = 0;

    double numOfChecks = 0;

    @Id
    public PlayerType_StartHandsHistKey getPlayerType_StartHandsHistKey() {
        return playerType_StartHandsHistKey;
    }

    public void setPlayerType_StartHandsHistKey(PlayerType_StartHandsHistKey playerType_StartHandsHistKey) {
        this.playerType_StartHandsHistKey = playerType_StartHandsHistKey;
    }

    @Embeddable
    public class PlayerType_StartHandsHistKey implements Serializable {

        PlayerTypeEntity playerTypeEntity;

        StartHandsHist startHandsHist;

        @ManyToOne
        public StartHandsHist getStartHandsHist() {
            return startHandsHist;
        }

        public void setStartHandsHist(StartHandsHist startHandsHist) {
            this.startHandsHist = startHandsHist;
        }

        @ManyToOne
        public PlayerTypeEntity getPlayerTypeEntity() {
            return playerTypeEntity;
        }

        public void setPlayerTypeEntity(PlayerTypeEntity playerTypeEntity) {
            this.playerTypeEntity = playerTypeEntity;
        }

        @Override
        public boolean equals(Object arg0) {
            if (arg0 instanceof PlayerType_StartHandsHistKey) {
                PlayerType_StartHandsHistKey playerType_StartHandsHistKey = (PlayerType_StartHandsHistKey) arg0;
                return playerType_StartHandsHistKey.getPlayerTypeEntity().equals(playerTypeEntity) && playerType_StartHandsHistKey.getStartHandsHist().equals(startHandsHist);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return startHandsHist.hashCode() + playerTypeEntity.hashCode();
        }
    }

    public double getNumOfCalls() {
        return numOfCalls;
    }

    public void setNumOfCalls(double numOfCalls) {
        this.numOfCalls = numOfCalls;
    }

    public double getNumOfChecks() {
        return numOfChecks;
    }

    public void setNumOfChecks(double numOfChecks) {
        this.numOfChecks = numOfChecks;
    }

    public double getNumOfFolds() {
        return numOfFolds;
    }

    public void setNumOfFolds(double numOfFolds) {
        this.numOfFolds = numOfFolds;
    }

    public double getNumOfRaises() {
        return numOfRaises;
    }

    public void setNumOfRaises(double numOfRaises) {
        this.numOfRaises = numOfRaises;
    }
}
