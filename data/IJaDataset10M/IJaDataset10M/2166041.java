package game;

/** This class, contains methods to manage the end of game rounds */
public class EndRoundManager {

    EndRoundCheckSieges ercSiegesForPlayer1;

    EndRoundCheckProjects ercProjectsForPlayer1;

    EndRoundCheckFeeding ercFeedingForPlayer1;

    EndRoundCheckGrowth ercGrowthForPlayer1;

    EndRoundCheckMegalopolis ercMegalopolisForPlayer1;

    EndRoundCheckGoodsAdjust ercGoodsAdjustForPlayer1;

    EndRoundCheckPhoros ercPhorosForPlayer1;

    EndRoundCheckSieges ercSiegesForPlayer2;

    EndRoundCheckProjects ercProjectsForPlayer2;

    EndRoundCheckFeeding ercFeedingForPlayer2;

    EndRoundCheckGrowth ercGrowthForPlayer2;

    EndRoundCheckMegalopolis ercMegalopolisForPlayer2;

    EndRoundCheckGoodsAdjust ercGoodsAdjustForPlayer2;

    EndRoundCheckPhoros ercPhorosForPlayer2;

    EndRoundInitializeNextRound erInitializeNextRound;

    Player firstPlayer;

    Player secondPlayer;

    public EndRoundManager(Game theGame) {
        if (theGame == null) {
            throw new IllegalArgumentException("Invalid value for EndRoundManager constructor, cannot be null");
        }
        if (theGame.getWhoHasTheTurn().equals(theGame.getAthensPlayer())) {
            firstPlayer = theGame.getSpartaPlayer();
            secondPlayer = theGame.getAthensPlayer();
        } else {
            firstPlayer = theGame.getAthensPlayer();
            secondPlayer = theGame.getSpartaPlayer();
        }
        ercSiegesForPlayer1 = new EndRoundCheckSieges(firstPlayer);
        ercProjectsForPlayer1 = new EndRoundCheckProjects(firstPlayer);
        ercFeedingForPlayer1 = new EndRoundCheckFeeding();
        ercGrowthForPlayer1 = new EndRoundCheckGrowth();
        ercMegalopolisForPlayer1 = new EndRoundCheckMegalopolis(firstPlayer);
        ercGoodsAdjustForPlayer1 = new EndRoundCheckGoodsAdjust(firstPlayer);
        ercPhorosForPlayer1 = new EndRoundCheckPhoros();
        ercSiegesForPlayer2 = new EndRoundCheckSieges(secondPlayer);
        ercProjectsForPlayer2 = new EndRoundCheckProjects(secondPlayer);
        ercFeedingForPlayer2 = new EndRoundCheckFeeding();
        ercGrowthForPlayer2 = new EndRoundCheckGrowth();
        ercMegalopolisForPlayer2 = new EndRoundCheckMegalopolis(secondPlayer);
        ercGoodsAdjustForPlayer2 = new EndRoundCheckGoodsAdjust(secondPlayer);
        ercPhorosForPlayer2 = new EndRoundCheckPhoros();
        erInitializeNextRound = new EndRoundInitializeNextRound(theGame);
    }
}
