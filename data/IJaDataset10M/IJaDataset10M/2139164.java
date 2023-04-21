package bagaturchess.bitboard.tests.pawnstructure;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.Board;
import bagaturchess.bitboard.impl.Fields;
import bagaturchess.bitboard.impl.Figures;
import bagaturchess.bitboard.impl.eval.pawns.model.ModelBuilder;
import bagaturchess.bitboard.impl.eval.pawns.model.Pawn;
import bagaturchess.bitboard.impl.eval.pawns.model.PawnsModel;

public abstract class Test extends Fields {

    private PawnsModel model;

    protected IBitBoard bitboard;

    public Test() {
        bitboard = new Board(getFEN(), null);
        model = ModelBuilder.build(bitboard);
    }

    public abstract String getFEN();

    public abstract void validate();

    protected void validatePassers(int colour, long fields, int ranks) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        int all_ranks = 0;
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isPassed()) {
                ver |= p[i].getField();
                all_ranks += p[i].getRank();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
        if (all_ranks != ranks) {
            throw new IllegalStateException("ranks=" + ranks + ", all_ranks=" + all_ranks);
        }
    }

    protected void validateUnstoppablePassers(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isPassedUnstoppable()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateGuards(int colour, long fields, int distance) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        int all_distance = 0;
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isGuard()) {
                ver |= p[i].getField();
                all_distance += p[i].getGuardRemoteness();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
        if (all_distance != distance) {
            throw new IllegalStateException("all_distance=" + all_distance + ", distance=" + distance);
        }
    }

    protected void validateStorms(int colour, long fields, int distance) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        int all_distance = 0;
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isStorm()) {
                ver |= p[i].getField();
                all_distance += p[i].getStormCloseness();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
        if (all_distance != distance) {
            throw new IllegalStateException("all_distance=" + all_distance + ", distance=" + distance);
        }
    }

    protected void validateDoubled(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isDoubled()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateBackward(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isBackward()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateIsolated(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isIsolated()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateSupported(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].isSupported()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateCannotBeSupported(int colour, long fields) {
        int count = -1;
        Pawn[] p = null;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWCount();
            p = model.getWPawns();
        } else {
            count = model.getBCount();
            p = model.getBPawns();
        }
        long ver = 0L;
        for (int i = 0; i < count; i++) {
            if (p[i].cannotBeSupported()) {
                ver |= p[i].getField();
            }
        }
        if (ver != fields) {
            throw new IllegalStateException("ver=" + ver + ", fields=" + fields);
        }
    }

    protected void validateIslandsCount(int colour, int expected) {
        int count = 0;
        if (colour == Figures.COLOUR_WHITE) {
            count = model.getWIslandsCount();
        } else {
            count = model.getBIslandsCount();
        }
        if (count != expected) {
            throw new IllegalStateException("count" + count + ", expected=" + expected);
        }
    }

    protected void validateHalfOpenedFiles(int colour, long verticals) {
        if (colour == Figures.COLOUR_WHITE) {
            if (verticals != model.getWHalfOpenedFiles()) {
                throw new IllegalStateException("white" + verticals + ", model.getWHalfOpennedFiles()=" + model.getWHalfOpenedFiles());
            }
        } else {
            if (verticals != model.getBHalfOpenedFiles()) {
                throw new IllegalStateException("black" + verticals + ", model.getBHalfOpennedFiles()=" + model.getBHalfOpenedFiles());
            }
        }
    }

    protected void validateOpenedFiles(long verticals) {
        if (verticals != model.getOpenedFiles()) {
            throw new IllegalStateException("white" + verticals + ", model.getOpenedFiles()=" + model.getOpenedFiles());
        }
    }

    protected void validateKingVerticals(int colour, long verticals) {
        if (colour == Figures.COLOUR_WHITE) {
            if (verticals != model.getWKingVerticals()) {
                throw new IllegalStateException("white" + verticals + ", model.getWKingVerticals()=" + model.getWKingVerticals());
            }
        } else {
            if (verticals != model.getBKingVerticals()) {
                throw new IllegalStateException("black" + verticals + ", model.getBKingVerticals()=" + model.getBKingVerticals());
            }
        }
    }

    protected void validateKingOpenedAndSemiOpened(int colour, int openedCount, int selfSemiOpenedCount, int opSemiOpenedCount) {
        if (colour == Figures.COLOUR_WHITE) {
            if (openedCount != model.getWKingOpenedFiles()) {
                throw new IllegalStateException("openedCount=" + openedCount + ", model.getWKingOpenedFiles()=" + model.getWKingOpenedFiles());
            }
            if (selfSemiOpenedCount != model.getWKingSemiOwnOpenedFiles()) {
                throw new IllegalStateException("selfSemiOpenedCount=" + openedCount + ", model.getWKingSemiOwnOpenedFiles()=" + model.getWKingSemiOwnOpenedFiles());
            }
            if (opSemiOpenedCount != model.getWKingSemiOpOpenedFiles()) {
                throw new IllegalStateException("opSemiOpenedCount=" + openedCount + ", model.getWKingSemiOpOpenedFiles()=" + model.getWKingSemiOpOpenedFiles());
            }
        } else {
            if (openedCount != model.getBKingOpenedFiles()) {
                throw new IllegalStateException("openedCount=" + openedCount + ", model.getBKingOpenedFiles()=" + model.getBKingOpenedFiles());
            }
            if (selfSemiOpenedCount != model.getBKingSemiOwnOpenedFiles()) {
                throw new IllegalStateException("selfSemiOpenedCount=" + openedCount + ", model.getBKingSemiOwnOpenedFiles()=" + model.getBKingSemiOwnOpenedFiles());
            }
            if (opSemiOpenedCount != model.getBKingSemiOpOpenedFiles()) {
                throw new IllegalStateException("opSemiOpenedCount=" + openedCount + ", model.getBKingSemiOpOpenedFiles()=" + model.getBKingSemiOpOpenedFiles());
            }
        }
    }

    protected void validateWeakFields(int colour, long weak) {
        if (colour == Figures.COLOUR_WHITE) {
            if (weak != model.getWWeakFields()) {
                throw new IllegalStateException("weak=" + weak + ", model.getWWeakFields()=" + model.getWWeakFields());
            }
        } else {
            if (weak != model.getBWeakFields()) {
                throw new IllegalStateException("weak=" + weak + ", model.getBWeakFields()=" + model.getBWeakFields());
            }
        }
    }
}
