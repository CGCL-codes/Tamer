package quizgame.protocol.admin.boardediting;

import quizgame.common.Category;
import quizgame.protocol.admin.*;

/**
 * @author rheo
 */
public class Board implements AdminPacket {

    public Category[] categories;

    public String name;
}
