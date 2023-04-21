package net.innig.macker.example.modularity.game.impl;

import net.innig.macker.example.modularity.game.*;
import java.util.*;

public class PrisonersDilemmaGame extends AbstractGame {

    public Set getLegalMoves() {
        return PrisonersDilemmaMove.ALL;
    }
}
