package com.novatialabs.qttt.gwt.client;

import com.novatialabs.qttt.gwt.client.Move.State;

/**
 * A Notation for quantum tic tac toe that is suitable for use in a URL path.
 * <p>
 * Games look like:
 * <dl>
 * <dt></dt>
 * <dd>A game with no moves</dd>
 * <dt>12</dt>
 * <dd>marks 1 and 2</dd>
 * <dt>12/23</dt>
 * <dd>Marks in 1 and 2 then marks in 2 and 3</dd>
 * <dt>12/12</dt>
 * <dd>Marks in 1 and 2 then marks in 1 and 2</dd>
 * <dt>12/12S</dt>
 * <dd>Marks in 1 and 2 then marks in 1 and 2, with the entanglement collapsed
 * to 2 in the second move.</dd>
 * </dl>
 * </p>
 * 
 * @author nchalko
 * 
 */
public class UrlNotation extends BaseNotation implements Notation {

    @Override
    protected String getMoveSeperator() {
        return "/";
    }

    @Override
    public Move parseMove(String text) {
        int mark1 = 0;
        int mark2 = 0;
        State state = State.ENTANGLED;
        if (text == null) {
            return Move.BLANK_MOVE;
        }
        text = text.trim();
        if (text.length() == 0) {
            return Move.BLANK_MOVE;
        }
        mark1 = Integer.parseInt(text.substring(0, 1));
        text = text.substring(1);
        if (text.length() > 0) {
            mark2 = Integer.parseInt(text.substring(0, 1));
        }
        if (mark2 != 0 && mark1 > mark2) {
            final int temp = mark1;
            mark1 = mark2;
            mark2 = temp;
        }
        if (mark2 > 0) {
            text = text.substring(1);
            if (text.length() > 0) {
                String collapse = text.substring(0, 1);
                if ("F".equalsIgnoreCase(collapse)) {
                    state = State.COLLAPSED_TO_FIRST;
                } else if ("S".equalsIgnoreCase(collapse)) {
                    state = State.COLLAPSED_TO_SECOND;
                }
            }
        }
        return new Move(mark1, mark2, state);
    }

    @Override
    public String printMove(Move m) {
        StringBuilder result = new StringBuilder();
        if (m == null || m.getState() == State.NOT_PLAYED || m.getFirstMark() == 0) {
            return result.toString();
        }
        result.append(m.getFirstMark());
        if (m.getSecondMark() != 0) {
            result.append(m.getSecondMark());
            if (m.getState() == State.COLLAPSED_TO_FIRST) {
                result.append("F");
            } else if (m.getState() == State.COLLAPSED_TO_SECOND) {
                result.append("S");
            }
        }
        return result.toString();
    }
}
