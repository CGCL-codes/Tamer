package deadend.ai.cat;

import deadend.ai.StrategyInterface;
import deadend.game.DeadEndGame;
import deadend.ai.cat.neuralnetwork.*;
import universalUtil.neuralnetwork.*;
import deadend.globalenum.Directions;
import deadend.game.GameConfigClass;

/**
 *
 * @author Yang JiaJian
 */
public class NeuralCat implements StrategyInterface {

    NeuralNetwork TheBrain;

    private DeadEndGame game;

    wekaANN annCat;

    private String dburl;

    /**
     *
     * @param game
     * @param catNum
     * @param onlyWin
     * @param dburl
     * @param numOfInputs
     * @param numOfHidden
     * @param numOfOutput
     */
    public NeuralCat(DeadEndGame game, int catNum, boolean onlyWin, String dburl, int numOfInputs, int numOfHidden, int numOfOutput) {
        this.game = game;
        this.dburl = dburl;
        wekaANN ann = new wekaANN();
        ann.initialize(numOfInputs, numOfHidden, numOfOutput);
        String odbc = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=";
        String strurl = odbc + dburl + "\\Dog" + ".mdb";
        ann.LoadData(strurl);
        this.annCat = ann;
    }

    @Override
    public Directions compute(deadend.game.DeadEndGame thegame) {
        int catToDog1x, catToDog1y, catToDog2x, catToDog2y, catToExitX, catToExitY;
        double catToDog1, catToDog2;
        int turn;
        catToDog1x = game.player.getPosition().x - game.dogs.dogTeam.get(0).getPosition().x;
        catToDog1y = game.player.getPosition().y - game.dogs.dogTeam.get(0).getPosition().y;
        catToDog2x = game.player.getPosition().x - game.dogs.dogTeam.get(1).getPosition().x;
        catToDog2y = game.player.getPosition().y - game.dogs.dogTeam.get(1).getPosition().y;
        catToExitX = game.player.getPosition().x - game.door.get(0).x;
        catToExitY = game.player.getPosition().y - game.door.get(0).y;
        catToDog1 = game.player.getPosition().distance(game.dogs.dogTeam.get(0).getPosition());
        catToDog2 = game.player.getPosition().distance(game.dogs.dogTeam.get(1).getPosition());
        double catDog1Angle;
        if (game.player.getPosition().x - game.dogs.dogTeam.get(0).getPosition().x != 0) {
            catDog1Angle = (game.player.getPosition().y - game.dogs.dogTeam.get(0).getPosition().y) / (game.player.getPosition().x - game.dogs.dogTeam.get(0).getPosition().x);
        } else {
            catDog1Angle = 1;
        }
        double catDog2Angle;
        if (game.player.getPosition().x - game.dogs.dogTeam.get(1).getPosition().x != 0) {
            catDog2Angle = (game.player.getPosition().y - game.dogs.dogTeam.get(1).getPosition().y) / (game.player.getPosition().x - game.dogs.dogTeam.get(1).getPosition().x);
        } else {
            catDog2Angle = 1;
        }
        int catToLeft = game.player.getPosition().x - 0;
        int catToRight = GameConfigClass.GridX - game.player.getPosition().x;
        int catToTop = game.player.getPosition().y - 0;
        int catToBottom = GameConfigClass.GridY - game.player.getPosition().y;
        int dog1ToLeft = game.dogs.dogTeam.get(0).getPosition().x - 0;
        int dog1ToRight = GameConfigClass.GridX - game.dogs.dogTeam.get(0).getPosition().x;
        int dog1ToTop = game.dogs.dogTeam.get(0).getPosition().y - 0;
        int dog1ToBottom = GameConfigClass.GridY - game.dogs.dogTeam.get(0).getPosition().y;
        int dog2ToLeft = game.dogs.dogTeam.get(1).getPosition().x - 0;
        int dog2ToRight = GameConfigClass.GridX - game.dogs.dogTeam.get(1).getPosition().x;
        int dog2ToTop = game.dogs.dogTeam.get(1).getPosition().y - 0;
        int dog2ToBottom = GameConfigClass.GridY - game.dogs.dogTeam.get(1).getPosition().y;
        double dogInnerDist = game.dogs.dogTeam.get(0).getPosition().distance(game.dogs.dogTeam.get(1).getPosition());
        int dog1ToExitX = game.dogs.dogTeam.get(0).getPosition().x - game.door.get(0).x;
        int dog1ToExitY = game.dogs.dogTeam.get(0).getPosition().x - game.door.get(0).y;
        int dog2ToExitX = game.dogs.dogTeam.get(1).getPosition().x - game.door.get(0).x;
        int dog2ToExitY = game.dogs.dogTeam.get(1).getPosition().x - game.door.get(0).y;
        turn = game.step;
        this.annCat.setInput(0, catToDog1x);
        this.annCat.setInput(1, catToDog1y);
        this.annCat.setInput(2, catToDog2x);
        this.annCat.setInput(3, catToDog2y);
        this.annCat.setInput(4, catToDog1);
        this.annCat.setInput(5, catToDog2);
        this.annCat.setInput(6, catToExitX);
        this.annCat.setInput(7, catToExitY);
        this.annCat.setInput(8, catDog1Angle);
        this.annCat.setInput(9, catDog2Angle);
        this.annCat.setInput(10, catToLeft);
        this.annCat.setInput(11, catToRight);
        this.annCat.setInput(12, catToTop);
        this.annCat.setInput(13, catToBottom);
        this.annCat.setInput(14, dog1ToLeft);
        this.annCat.setInput(15, dog1ToRight);
        this.annCat.setInput(16, dog1ToTop);
        this.annCat.setInput(17, dog1ToBottom);
        this.annCat.setInput(18, dog2ToLeft);
        this.annCat.setInput(19, dog2ToRight);
        this.annCat.setInput(20, dog2ToTop);
        this.annCat.setInput(21, dog2ToBottom);
        this.annCat.setInput(22, dogInnerDist);
        this.annCat.setInput(23, dog1ToExitX);
        this.annCat.setInput(24, dog1ToExitY);
        this.annCat.setInput(25, dog2ToExitX);
        this.annCat.setInput(26, dog2ToExitY);
        this.annCat.setInput(27, turn);
        this.annCat.feedForward();
        int k = this.annCat.getOutputIDByProbability();
        Directions d = Directions.Still;
        d = this.loadChoice(dburl + ".mdb", k);
        return d;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\MCState300-CS-Win")) return "ANN-MCState300-CS-Win";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\MCState-ZigZag-Win")) return "ANN-MCState300-Zigzag-Win";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\MCState300-Square-Win")) return "ANN-MCState300-Square-Win";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\C1")) return "ANN-Cluster-A";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\C2")) return "ANN-Cluster-B";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\C3")) return "ANN-Cluster-C";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\C4")) return "ANN-Cluster-D";
        if (this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\Ref")) return "ANN-Unclustered";
        return this.dburl;
    }

    private Directions loadChoice(String dburl, int maxID) {
        boolean comUrl;
        comUrl = this.dburl.equalsIgnoreCase("db\\enhanced\\current\\Cluster-ZZ\\C1");
        return Directions.Still;
    }

    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
