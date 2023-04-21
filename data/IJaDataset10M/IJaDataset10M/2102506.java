package at.fhj.itm.controller;

public class Model extends ListPlayers {

    private String modelNickName = null;

    private int modelScore = 0;

    private int modelPlayerTeam = 0;

    public Model() {
    }

    public Model(String fname, int score, int team) {
        setModelNickName(fname);
        setModelScore(score);
        setModelPlayerTeam(team);
    }

    public void setModelScore(int modelScore) {
        this.modelScore = modelScore;
    }

    public void setModelNickName(String modelNickName) {
        this.modelNickName = modelNickName;
    }

    public void incModelScore(int increaseBy) {
        this.modelScore += increaseBy;
    }

    public int getModelScore() {
        return this.modelScore;
    }

    public String getModelNickName() {
        return this.modelNickName;
    }

    public void outputScore() {
        System.out.println("The Score: " + modelScore);
    }

    public void outputNickName() {
        System.out.println("The Nickname: " + modelNickName);
    }

    /**
	 * @Function addPlayerToList()
	 * @Description: Add's the Object "Player" to ArrayList 
	 */
    public void addPlayerToList() {
        getModelList().add(this);
    }

    public int getModelPlayerTeam() {
        return modelPlayerTeam;
    }

    public void setModelPlayerTeam(int modelPlayerTeam) {
        this.modelPlayerTeam = modelPlayerTeam;
    }
}
