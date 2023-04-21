package net.sourceforge.piqle.runners.runs;

public interface TwoPlayerRun<TState> extends Run<TState> {

    double getRewardForEpisodePlayer1();

    double getRewardForEpisodePlayer2();
}
