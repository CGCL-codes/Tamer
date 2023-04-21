package com.amarphadke.chess.server.rules.material;

import com.amarphadke.chess.server.domain.BoardPositionProvider;
import com.amarphadke.chess.server.rules.Rule;

public class SelfPieceUnderAttackRule implements Rule {

    public String getRuleDescription() {
        return null;
    }

    public String getRuleName() {
        return null;
    }

    public float getScore(BoardPositionProvider boardPosition) {
        return 0;
    }

    public int getWeightage() {
        return 0;
    }
}
