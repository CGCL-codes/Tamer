package org.gu.junyang.solver.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.gu.junyang.utilities.ArrayListUtils;

public class SolverNonGreedy extends Solver {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2857339775918949315L;

    CellList newlySetCells = null;

    /**
     * possible outcome:
     * true: solved, please check multiple solutions flag
     * false: not solved (with limited capability level
     * BoardException is thrown, the puzzle is not solvable;
     * 
     * IMPORTANT: do not reuse a solver class
     *
     * @return
     * @throws BoardException
     */
    @Override
    public boolean Solve() throws BoardException {
        if (cellPositionList == null) makeCellList();
        addToBoardHistory();
        sanityCheck();
        Solve1();
        if (guessed) {
            logger.debug("end search.");
        }
        if (solutions.size() > 1) {
            logger.info("multiple solutions found: " + solutions.size());
        }
        if (solved) {
            for (int i = HIGHEST_CAPABILITY_LEVELS - 1; i >= 0; i--) {
                if (capabilitiesUsed[i]) {
                    highestCapabilityUsed = i + 1;
                    break;
                }
            }
        }
        return (solved);
    }

    @Override
    protected void Solve1() throws BoardException {
        while (unknowns != 0) {
            if (CrossEliminateAll()) {
                capabilitiesUsed[0] = true;
                addToBoardHistory();
                continue;
            }
            if (newlySetCells == null) {
                newlySetCells = new CellList();
            }
            if (singleDestinationAll()) {
                capabilitiesUsed[1] = true;
                addToBoardHistory();
                continue;
            }
            if (limitedCandidates2Search() || limitedCandidates3Search()) {
                capabilitiesUsed[2] = true;
                addToBoardHistory();
                continue;
            }
            if (limitedDestination()) {
                capabilitiesUsed[3] = true;
                addToBoardHistory();
                continue;
            }
            if (dualRowLimitedDestination() || dualColLimitedDestination()) {
                capabilitiesUsed[4] = true;
                addToBoardHistory();
                continue;
            }
            if (xwingRow() || xwingCol()) {
                capabilitiesUsed[5] = true;
                addToBoardHistory();
                continue;
            }
            break;
        }
        if (unknowns == 0) {
            sanityCheck();
            solved = true;
            logger.info("success - guess count: " + guessCount);
            if (guessed) {
                guessBoards.add(guessBoard.toSimpleString());
            } else {
                guessBoards.add("");
            }
            if (solutions.size() == FIND_ALL_SOLUTIONS_LIMIT) {
                findAllSolutionsLimitExceeded = true;
            } else {
                solutions.add(toString());
                if (recordBoardHistory) {
                    solutionBoardIndexes.add(boardHistory.size() - 1);
                }
                if (recordTranscript) {
                    solutionTranscriptIndexes.add(transcript.size() - 1);
                }
            }
        } else {
            guessed = true;
            SolveRecursive();
            capabilitiesUsed[HIGHEST_CAPABILITY_LEVELS - 1] = true;
            if (!solved) {
                throw new BoardException("Trial and error can not find a solution.");
            }
        }
    }

    @Override
    protected boolean CrossEliminate(int row, int col, int iVal) {
        boolean bSet = false;
        bSet |= EliminateRow(row, iVal);
        bSet |= EliminateCol(col, iVal);
        bSet |= EliminateBlock(row, col, iVal);
        if (bSet) patternCellList.add(sudokuCells[row][col]);
        return bSet;
    }

    @Override
    public boolean CrossEliminateAll() {
        boolean bSet = false;
        if (newlySetCells != null) {
            CellList cellList = new CellList();
            cellList.addAll(newlySetCells);
            newlySetCells.clear();
            while (cellList.size() != 0) {
                Cell cell = cellList.get(0);
                bSet |= CrossEliminate(cell.row, cell.col, cell.value);
                cellList.remove(0);
                if (bSet) break;
            }
            return bSet;
        }
        Board originalBoard = new Board(this);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (originalBoard.sudokuCells[i][j].known) {
                    int iTmp = sudokuCells[i][j].value;
                    bSet |= CrossEliminate(i, j, iTmp);
                    if (bSet) return bSet;
                }
            }
        }
        return bSet;
    }

    @Override
    protected boolean singleDestination(int cellSetIdx) {
        boolean bSet = false;
        int count[] = new int[9];
        Cell cLastCell[] = new Cell[9];
        for (Cell pCell : validCellSets[cellSetIdx]) {
            if (!pCell.known) {
                for (Integer value : pCell.candidates) {
                    count[value - 1]++;
                    cLastCell[value - 1] = pCell;
                }
            } else {
                count[pCell.value - 1]++;
                cLastCell[pCell.value - 1] = pCell;
            }
        }
        for (int value = 0; value < 9; value++) {
            if (count[value] == 1) {
                if (cLastCell[value].known) {
                    continue;
                }
                int iRow = cLastCell[value].row;
                int iCol = cLastCell[value].col;
                setValue(iRow, iCol, value + 1);
                if (newlySetCells != null) newlySetCells.add(sudokuCells[iRow][iCol]);
                bSet = true;
                if (recordTranscript) {
                    transcriptTemp.add("SingleDestination (2): " + toPositionString(iRow, iCol) + "=" + (value + 1));
                    actions.add(Action.SET_VALUE);
                }
                patternCellList.addAll(validCellSets[cellSetIdx]);
                return bSet;
            }
        }
        return bSet;
    }

    @Override
    public boolean singleDestinationAll() {
        boolean bSet = false;
        for (int i = TOTAL_CELL_SETS - 1; i >= 0; i--) {
            bSet |= singleDestination(i);
            if (bSet) return bSet;
        }
        return bSet;
    }

    @Override
    protected boolean EliminateCandidates(int row, int col, Candidates candidates) {
        boolean bSet = false;
        if (sudokuCells[row][col].known) {
            return false;
        }
        for (Integer it : candidates) {
            bSet |= removeCandidate(row, col, it);
        }
        if (sudokuCells[row][col].known) {
            if (newlySetCells != null) newlySetCells.add(sudokuCells[row][col]);
            int iTmp = sudokuCells[row][col].value;
            if (recordTranscript) {
                transcriptTemp.add("EliminateCandidates (1): " + toPositionString(row, col) + "=" + iTmp);
                actions.add(Action.SET_VALUE);
            }
            if (debug) {
                logger.debug(toString());
            }
        }
        return bSet;
    }

    @Override
    protected boolean limitedCandidatesTest(int cellSetIndex, ArrayList<Cell> cell_list) {
        boolean bSet = false;
        Candidates cCandTmp = new Candidates();
        for (Cell it : cell_list) {
            if (it.known) {
                throw new Error("error: cell is known.");
            }
            cCandTmp.addAll(it.candidates);
        }
        if (cCandTmp.size() > cell_list.size()) {
            return false;
        }
        int cellCnt = cell_list.size();
        patternCellList.addAll(cell_list);
        for (Cell it : validCellSets[cellSetIndex]) {
            if (cell_list.contains(it)) {
                continue;
            }
            if (it.known) {
                continue;
            }
            boolean transcriptAdded = false;
            if (recordRemoveCandidate && recordTranscript) {
                transcriptTemp.add("LimitedCandidates (3): " + it.toPositionString() + " remove " + cCandTmp.toString());
                actions.add(Action.REMOVE_CANDIDATE);
                transcriptAdded = true;
            }
            boolean changed = EliminateCandidates(it.row, it.col, cCandTmp);
            if (!changed && transcriptAdded) {
                transcriptTemp.remove(transcriptTemp.size() - 1);
                actions.remove(actions.size() - 1);
            }
            bSet |= changed;
        }
        if (!bSet) {
            ArrayListUtils.removeTailLength(patternCellList, cellCnt);
        }
        return bSet;
    }

    @Override
    protected boolean limitedCandidates2Search() {
        boolean bSet = false;
        CellList cCellPair = new CellList();
        for (int i = 0; i < TOTAL_CELL_SETS; i++) {
            CellList cellList = validCellLists[i];
            for (int j = 0; j < 9; j++) {
                Cell it1Cell = cellList.get(j);
                if (it1Cell.known) {
                    continue;
                }
                if (it1Cell.candidates.size() > 2) {
                    continue;
                }
                cCellPair.add(it1Cell);
                for (int k = j + 1; k < 9; k++) {
                    Cell it2Cell = cellList.get(k);
                    if (it2Cell.known) {
                        continue;
                    }
                    if (it2Cell.candidates.size() > 2) {
                        continue;
                    }
                    cCellPair.add(it2Cell);
                    bSet |= limitedCandidatesTest(i, cCellPair);
                    if (bSet) return bSet;
                    cCellPair.remove(1);
                    if (it1Cell.known) {
                        break;
                    }
                }
                cCellPair.remove(0);
            }
        }
        return bSet;
    }

    @Override
    protected boolean limitedCandidates3Search() {
        boolean bSet = false;
        CellList cThreeCell = new CellList();
        for (int i = 0; i < TOTAL_CELL_SETS; i++) {
            CellList cellList = validCellLists[i];
            for (int j = 0; j < 9; j++) {
                Cell it1Cell = cellList.get(j);
                if (it1Cell.known) {
                    continue;
                }
                if (it1Cell.candidates.size() > 3) {
                    continue;
                }
                cThreeCell.add(it1Cell);
                for (int k = j + 1; k < 9; k++) {
                    Cell it2Cell = cellList.get(k);
                    if (it2Cell.known) {
                        continue;
                    }
                    if (it2Cell.candidates.size() > 3) {
                        continue;
                    }
                    cThreeCell.add(it2Cell);
                    for (int l = k + 1; l < 9; l++) {
                        Cell it3Cell = cellList.get(l);
                        if (it3Cell.known) {
                            continue;
                        }
                        if (it3Cell.candidates.size() > 3) {
                            continue;
                        }
                        cThreeCell.add(it3Cell);
                        bSet |= limitedCandidatesTest(i, cThreeCell);
                        if (bSet) return bSet;
                        cThreeCell.remove(2);
                        if (it2Cell.known || it1Cell.known) {
                            break;
                        }
                    }
                    cThreeCell.remove(1);
                    if (it1Cell.known) {
                        break;
                    }
                }
                cThreeCell.remove(0);
            }
        }
        return bSet;
    }

    @Override
    protected boolean limitedDestination() {
        boolean bSet = false;
        for (int i = 18; i < TOTAL_CELL_SETS; i++) {
            CellSet cCandToCells[] = new CellSet[10];
            for (int j = 0; j < 10; j++) {
                cCandToCells[j] = new CellSet();
            }
            for (Cell cell : validCellSets[i]) {
                if (cell.known) {
                    continue;
                }
                for (Integer candidate : cell.candidates) {
                    cCandToCells[candidate].add(cell);
                }
            }
            for (int j = 1; j <= 9; j++) {
                if (cCandToCells[j].size() == 2 || cCandToCells[j].size() == 3) {
                    Iterator<Cell> it = cCandToCells[j].iterator();
                    Cell cell = it.next();
                    int row = cell.row;
                    boolean rowSame = true;
                    int col = cell.col;
                    boolean colSame = true;
                    while (it.hasNext()) {
                        cell = it.next();
                        if (cell.row != row) {
                            rowSame = false;
                        }
                        if (cell.col != col) {
                            colSame = false;
                        }
                        if (!rowSame && !colSame) {
                            break;
                        }
                    }
                    int k;
                    if (rowSame) {
                        k = row;
                    } else if (colSame) {
                        k = 9 + col;
                    } else {
                        continue;
                    }
                    int cellCnt = cCandToCells[j].size();
                    patternCellList.addAll(cCandToCells[j]);
                    for (Cell cell2 : validCellSets[k]) {
                        if (cell2.known || cCandToCells[j].contains(cell2)) {
                            continue;
                        }
                        bSet |= RemoveCandidateWithCheck(cell2.row, cell2.col, j, "LimitedDestination (4)");
                    }
                    if (bSet) {
                        return bSet;
                    } else {
                        ArrayListUtils.removeTailLength(patternCellList, cellCnt);
                    }
                }
            }
        }
        return bSet;
    }

    @Override
    protected boolean dualRowLimitedDestination() {
        boolean bSet = false;
        Candidates cRows1 = new Candidates();
        Candidates cRows2 = new Candidates();
        Iterator<Integer> pRow;
        for (int i = 18; i < TOTAL_CELL_SETS; i += 3) {
            for (int iVal = 1; iVal <= 9; iVal++) {
                for (int j = i; j < i + 3; j++) {
                    for (int k = j + 1; k < i + 3; k++) {
                        if (validCellSets[j].CountCandidateRows(iVal, cRows1) == 2 && validCellSets[k].CountCandidateRows(iVal, cRows2) == 2) {
                            int iRow11, iRow12, iRow21, iRow22;
                            pRow = cRows1.iterator();
                            iRow11 = pRow.next();
                            iRow12 = pRow.next();
                            pRow = cRows2.iterator();
                            iRow21 = pRow.next();
                            iRow22 = pRow.next();
                            if ((iRow11 == iRow21 && iRow12 == iRow22) || (iRow11 == iRow22 && iRow12 == iRow21)) {
                                int l;
                                for (l = i; l < i + 3; l++) {
                                    if (l != j && l != k) {
                                        break;
                                    }
                                }
                                int cellCnt = 0;
                                for (Cell cell : validCellLists[j]) if (cell.row == iRow11 || cell.row == iRow12) {
                                    patternCellList.add(cell);
                                    cellCnt++;
                                }
                                for (Cell cell : validCellLists[k]) if (cell.row == iRow21 || cell.row == iRow22) {
                                    patternCellList.add(cell);
                                    cellCnt++;
                                }
                                for (Cell pCell : validCellSets[l]) {
                                    if (!pCell.known && (pCell.row == iRow11 || pCell.row == iRow12)) {
                                        bSet |= RemoveCandidateWithCheck(pCell.row, pCell.col, iVal, "DualRowLimitedDestination (5)");
                                    }
                                }
                                if (bSet) {
                                    return bSet;
                                } else {
                                    ArrayListUtils.removeTailLength(patternCellList, cellCnt);
                                }
                            }
                        }
                    }
                }
            }
        }
        return bSet;
    }

    @Override
    protected boolean dualColLimitedDestination() {
        boolean bSet = false;
        Candidates cCols1 = new Candidates();
        Candidates cCols2 = new Candidates();
        Iterator<Integer> pCol;
        for (int i = 18; i < 21; i++) {
            for (int iVal = 1; iVal <= 9; iVal++) {
                for (int j = i; j < i + 9; j += 3) {
                    for (int k = j + 3; k < i + 9; k += 3) {
                        if (validCellSets[j].CountCandidateCols(iVal, cCols1) == 2 && validCellSets[k].CountCandidateCols(iVal, cCols2) == 2) {
                            int iCol11, iCol12, iCol21, iCol22;
                            pCol = cCols1.iterator();
                            iCol11 = pCol.next();
                            iCol12 = pCol.next();
                            pCol = cCols2.iterator();
                            iCol21 = pCol.next();
                            iCol22 = pCol.next();
                            if ((iCol11 == iCol21 && iCol12 == iCol22) || (iCol11 == iCol22 && iCol12 == iCol21)) {
                                int l;
                                for (l = i; l < i + 9; l += 3) {
                                    if (l != j && l != k) {
                                        break;
                                    }
                                }
                                int cellCnt = 0;
                                for (Cell cell : validCellLists[j]) if (cell.col == iCol11 || cell.col == iCol12) {
                                    patternCellList.add(cell);
                                    cellCnt++;
                                }
                                for (Cell cell : validCellLists[k]) if (cell.col == iCol21 || cell.col == iCol22) {
                                    patternCellList.add(cell);
                                    cellCnt++;
                                }
                                for (Cell pCell : validCellSets[l]) {
                                    if (!pCell.known && (pCell.col == iCol11 || pCell.col == iCol12)) {
                                        bSet |= RemoveCandidateWithCheck(pCell.row, pCell.col, iVal, "DualColLimitedDestination (5)");
                                    }
                                }
                                if (bSet) {
                                    return bSet;
                                } else {
                                    ArrayListUtils.removeTailLength(patternCellList, cellCnt);
                                }
                            }
                        }
                    }
                }
            }
        }
        return bSet;
    }

    @Override
    protected boolean RemoveCandidateWithCheck(int row, int col, int val, String prompt) {
        if (!removeCandidate(row, col, val)) {
            return false;
        }
        if (recordRemoveCandidate && recordTranscript) {
            transcriptTemp.add(prompt + ": " + toPositionString(row, col) + " remove " + val);
            actions.add(Action.REMOVE_CANDIDATE);
        }
        if (sudokuCells[row][col].known) {
            if (newlySetCells != null) newlySetCells.add(sudokuCells[row][col]);
            int iTmp = sudokuCells[row][col].value;
            if (recordTranscript) {
                transcriptTemp.add(prompt + ": " + toPositionString(row, col) + "=" + iTmp);
                actions.add(Action.SET_VALUE);
            }
            if (debug) {
                logger.debug(toString());
            }
        }
        return true;
    }

    @Override
    protected void SolveRecursive() {
        boolean foundUnknown = false;
        int i = 0, j = 0;
        int savedIdx = cellIdx;
        while (cellIdx < cellPositionList.size()) {
            CellPosition locaterCell = cellPositionList.get(cellIdx++);
            i = locaterCell.row;
            j = locaterCell.col;
            if (!sudokuCells[i][j].known) {
                foundUnknown = true;
                break;
            }
        }
        if (!foundUnknown) {
            cellIdx = savedIdx;
            return;
        }
        byte[] ba = serializeToByteArray();
        ArrayList<Integer> candidates = makeCandidateList(i, j);
        for (Integer candidate : candidates) {
            setValue(i, j, candidate);
            if (recordTranscript) {
                transcriptTemp.add("TrialAndError (7): trying " + toPositionString(i, j) + "=" + candidate);
                actions.add(Action.SET_VALUE);
            }
            guessCount++;
            guessStack.push(new SetValueAction(i, j, candidate));
            guessBoard.setValue(i, j, candidate);
            newlySetCells.add(sudokuCells[i][j]);
            if (debug) {
                logger.debug(toString());
            }
            try {
                sanityCheck();
                Solve1();
                if (solved) {
                    if (solutions.size() > 1 && !findAllSolutions) {
                        return;
                    }
                    if (noMultipleSolutionCheck) {
                        return;
                    }
                    if (findAllSolutionsLimitExceeded) {
                        return;
                    }
                }
            } catch (BoardException e) {
            }
            newlySetCells.clear();
            transcriptTemp.add("TrialAndError: back out " + toPositionString(i, j) + "=" + candidate);
            actions.add(Action.SET_VALUE);
            guessCount--;
            guessStack.pop();
            guessBoard.reset(i, j);
            deserializeFromByteArray(ba);
            addToBoardHistory();
            if (debug) {
                logger.debug(toString());
            }
        }
        cellIdx = savedIdx;
    }

    /**
     * the xwing - 2 rows
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    protected boolean xwingRow() {
        Set<Integer>[][] colMaps = new Set[9][9];
        for (int row = 0; row < 9; row++) {
            for (int c = 0; c < 9; c++) colMaps[row][c] = new HashSet<Integer>();
            for (int col = 0; col < 9; col++) {
                for (int candidate : sudokuCells[row][col].getCandidates()) {
                    colMaps[row][candidate - 1].add(col);
                }
            }
        }
        boolean bset = false;
        for (int row1 = 0; row1 < 8; row1++) {
            for (int c = 0; c < 9; c++) {
                if (colMaps[row1][c].size() != 2) continue;
                for (int row2 = row1 + 1; row2 < 9; row2++) {
                    if (colMaps[row2][c].size() != 2) continue;
                    if (colMaps[row1][c].equals(colMaps[row2][c])) {
                        Iterator<Integer> it = colMaps[row1][c].iterator();
                        while (it.hasNext()) {
                            int col = it.next();
                            patternCellList.add(sudokuCells[row1][col]);
                            patternCellList.add(sudokuCells[row2][col]);
                        }
                        for (int col : colMaps[row1][c]) bset |= xwingRowRemoveCandidateWithCheck(row1, row2, col, c + 1);
                        if (bset) {
                            return bset;
                        } else {
                            ArrayListUtils.removeTailLength(patternCellList, 4);
                        }
                    }
                }
            }
        }
        return bset;
    }

    /**
     * the xwing - 2 rows
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    protected boolean xwingCol() {
        Set<Integer>[][] rowMaps = new Set[9][9];
        for (int col = 0; col < 9; col++) {
            for (int c = 0; c < 9; c++) rowMaps[col][c] = new HashSet<Integer>();
            for (int row = 0; row < 9; row++) {
                for (int candidate : sudokuCells[row][col].getCandidates()) {
                    rowMaps[col][candidate - 1].add(row);
                }
            }
        }
        boolean bset = false;
        for (int col1 = 0; col1 < 8; col1++) {
            for (int c = 0; c < 9; c++) {
                if (rowMaps[col1][c].size() != 2) continue;
                for (int col2 = col1 + 1; col2 < 9; col2++) {
                    if (rowMaps[col2][c].size() != 2) continue;
                    if (rowMaps[col1][c].equals(rowMaps[col2][c])) {
                        Iterator<Integer> it = rowMaps[col1][c].iterator();
                        while (it.hasNext()) {
                            int row = it.next();
                            patternCellList.add(sudokuCells[row][col1]);
                            patternCellList.add(sudokuCells[row][col2]);
                        }
                        for (int row : rowMaps[col1][c]) bset |= xwingColRemoveCandidateWithCheck(col1, col2, row, c + 1);
                        if (bset) {
                            return bset;
                        } else {
                            ArrayListUtils.removeTailLength(patternCellList, 4);
                        }
                    }
                }
            }
        }
        return bset;
    }
}
