package com.ecomponentes.hibernate.boardquestion;

import java.io.Serializable;

/**
 * A class that represents a row in the 'tb_board_question' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TbBoardQuestion extends AbstractTbBoardQuestion implements Serializable {

    /**
     * Simple constructor of TbBoardQuestion instances.
     */
    public TbBoardQuestion() {
    }

    /**
     * Constructor of TbBoardQuestion instances given a simple primary key.
     * @param idBoardQuestion
     */
    public TbBoardQuestion(java.lang.Integer idBoardQuestion) {
        super(idBoardQuestion);
    }
}
