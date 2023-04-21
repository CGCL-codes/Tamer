package com.blommesteijn.uva.sc.saf.runner.model.game;

public interface IDraw {

    void printAction(String text);

    void printScore(String text);

    void printOutputPane(String[] fighter, int position, int otherPosition);
}
