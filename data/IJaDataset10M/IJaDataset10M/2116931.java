package net.innig.macker.example.modularity.game;

public interface Move {

    public String getName();

    public int getScoreFor(Move otherPlayersMove);
}
