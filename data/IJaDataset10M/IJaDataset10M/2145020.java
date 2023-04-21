package bagaturchess.opening.run;

import java.io.FileNotFoundException;
import java.io.IOException;
import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.movelist.BaseMoveList;
import bagaturchess.bitboard.impl.movelist.IMoveList;
import bagaturchess.opening.api.OpeningBook;
import bagaturchess.opening.api.OpeningBookFactory;
import bagaturchess.opening.api.traverser.OpeningTraverser;
import bagaturchess.opening.api.traverser.OpeningsVisitor;
import bagaturchess.opening.impl.model.OpeningBookImpl_OnlyHashkeys;

public class ShortBookConverter implements OpeningsVisitor {

    private OpeningBookImpl_OnlyHashkeys book = new OpeningBookImpl_OnlyHashkeys();

    private OpeningBook full_ob;

    public void generate() throws FileNotFoundException, IOException, ClassNotFoundException {
        full_ob = OpeningBookFactory.load("./../OpeningGenerator/w.ob", "./../OpeningGenerator/b.ob");
        OpeningTraverser.traverseAll(full_ob, this);
    }

    public void begin() {
    }

    public void end() {
        book.store("./../WorkDir/short.ob");
    }

    public void visitPosition(IBitBoard bitboard) {
        IMoveList moves = new BaseMoveList(150);
        if (bitboard.isInCheck()) {
            bitboard.genKingEscapes(moves);
        } else {
            bitboard.genAllMoves(moves);
        }
        int cur_move = 0;
        while ((cur_move = moves.next()) != 0) {
            bitboard.makeMoveForward(cur_move);
            long new_hashkey = bitboard.getHashKey();
            int new_colour = bitboard.getColourToMove();
            if (full_ob.get(new_hashkey, new_colour) != 0) {
                book.add(bitboard.getHashKey(), 0);
            }
            bitboard.makeMoveBackward(cur_move);
        }
    }

    public static void main(String[] args) {
        ShortBookConverter sbg = new ShortBookConverter();
        try {
            sbg.generate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
