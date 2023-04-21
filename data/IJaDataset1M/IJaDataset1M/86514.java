package com.javaeedev.web.bbs;

import java.util.List;
import com.javaeedev.domain.BoardCategory;

/**
 * Bean used to combine category and boards.
 * 
 * @author Xuefeng
 */
public class ComplexGroup {

    private BoardCategory boardCategory;

    private List<ComplexBoard> boards;

    public BoardCategory getBoardCategory() {
        return boardCategory;
    }

    public void setBoardCategory(BoardCategory boardCategory) {
        this.boardCategory = boardCategory;
    }

    public List<ComplexBoard> getBoards() {
        return boards;
    }

    public void setBoards(List<ComplexBoard> boards) {
        this.boards = boards;
    }
}
