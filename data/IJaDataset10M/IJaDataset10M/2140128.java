package jcrosswords.bo;

import java.awt.Font;
import java.util.Collection;
import jcrosswords.domain.Board;
import jcrosswords.domain.ClueResponseTag;
import jcrosswords.domain.Crossword;

public interface CrosswordGeneratorBO {

    public Crossword generateCrossword(Board board, Collection<ClueResponseTag> clueResponseTags, Font font);
}
