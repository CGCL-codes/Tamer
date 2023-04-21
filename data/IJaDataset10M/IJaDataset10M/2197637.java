package bagaturchess.search.impl.uci_adaptor;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.movelist.BaseMoveList;
import bagaturchess.bitboard.impl.movelist.IMoveList;
import bagaturchess.opening.api.Entry;
import bagaturchess.opening.api.OpeningBook;
import bagaturchess.opening.api.OpeningBookFactory;
import bagaturchess.search.api.internal.ISearchInfo;
import bagaturchess.search.api.internal.ISearchMediator;
import bagaturchess.search.impl.info.SearchInfoFactory;

public class TimeSaver {

    private static OpeningBook ob;

    public TimeSaver() {
        ob = OpeningBookFactory.getBook();
    }

    public boolean beforeMove(IBitBoard bitboard, ISearchMediator mediator, boolean useOpening) {
        if (useOpening && ob != null) {
            Entry entry = ob.getEntry(bitboard.getHashKey(), bitboard.getColourToMove());
            if (entry != null) {
                int move = entry.getRandomEntry();
                ISearchInfo info = createInfo(move);
                mediator.changedMajor(info);
                if (mediator.getBestMoveSender() != null) mediator.getBestMoveSender().sendBestMove();
                return true;
            }
        }
        if (bitboard.hasSingleMove()) {
            IMoveList list = new BaseMoveList();
            if (bitboard.isInCheck()) {
                int count = bitboard.genKingEscapes(list);
                if (count != 1) {
                    throw new IllegalStateException();
                }
            } else {
                int count = bitboard.genAllMoves(list);
                if (count != 1) {
                    throw new IllegalStateException();
                }
            }
            int move = list.reserved_getMovesBuffer()[0];
            ISearchInfo info = createInfo(move);
            mediator.changedMajor(info);
            if (mediator.getBestMoveSender() != null) mediator.getBestMoveSender().sendBestMove();
            return true;
        }
        return false;
    }

    private static ISearchInfo createInfo(int move) {
        ISearchInfo info = SearchInfoFactory.getFactory().createSearchInfo();
        info.setBestMove(move);
        info.setPV(new int[] { move });
        return info;
    }
}
