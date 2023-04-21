package solver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sudoku.Candidate;
import sudoku.SolutionStep;
import sudoku.SolutionType;
import sudoku.Sudoku2;
import sudoku.SudokuSet;

/**
 * Verboten sind alle Templates, die keine 1 an einer der bereits gesetzten Positionen haben:
 *    (positions & template) != positions
 * Verboten sind alle Templates, die eine 1 an einer nicht mehr erlaubten Position haben:
 *    (~(positions | allowedPositions) & template) != 0
 * Verboten sind alle Templates, die eine 1 an einer Position eines Templates haben, das aus
 *    allen verundeten Templates eines anderen Kandidaten gebildet wurde
 * Verboten sind alle Templates, die keine einzige �berlappungsfreie Kombination mit wenigstens
 *    einem Template einer anderen Ziffer haben
 *
 * Wenn die Templates bekannt sind:
 *    alle Templates OR: Alle Kandidaten, die nicht enthalten sind, k�nnen gel�scht werden
 *    alle Templates AND: Alle Positionen, die �brig bleiben, k�nnen gesetzt werden
 *    alle g�ltigen Kombinationen aus Templates zweier Ziffern bilden (OR), alle Ergebnisse
 *           AND: An allen verbliebenen Positionen k�nnen alle Kandidaten, die nicht zu einer dieser
 *           Ziffern geh�ren, eliminiert werden.
 *
 * @author hobiwan
 */
public class TemplateSolver extends AbstractSolver {

    private List<SolutionStep> steps;

    private SolutionStep globalStep = new SolutionStep(SolutionType.HIDDEN_SINGLE);

    /** Creates a new instance of TemplateSolver
     * @param finder 
     */
    public TemplateSolver(SudokuStepFinder finder) {
        super(finder);
    }

    @Override
    protected SolutionStep getStep(SolutionType type) {
        SolutionStep result = null;
        sudoku = finder.getSudoku();
        switch(type) {
            case TEMPLATE_SET:
                getTemplateSet(true);
                if (steps.size() > 0) {
                    result = steps.get(0);
                }
                break;
            case TEMPLATE_DEL:
                getTemplateDel(true);
                if (steps.size() > 0) {
                    result = steps.get(0);
                }
                break;
        }
        return result;
    }

    @Override
    protected boolean doStep(SolutionStep step) {
        boolean handled = true;
        sudoku = finder.getSudoku();
        switch(step.getType()) {
            case TEMPLATE_SET:
                int value = step.getValues().get(0);
                for (int index : step.getIndices()) {
                    sudoku.setCell(index, value);
                }
                break;
            case TEMPLATE_DEL:
                for (Candidate cand : step.getCandidatesToDelete()) {
                    sudoku.delCandidate(cand.getIndex(), cand.getValue());
                }
                break;
            default:
                handled = false;
        }
        return handled;
    }

    protected List<SolutionStep> getAllTemplates() {
        sudoku = finder.getSudoku();
        List<SolutionStep> oldSteps = steps;
        steps = new ArrayList<SolutionStep>();
        long millis1 = System.currentTimeMillis();
        getTemplateSet(false);
        getTemplateDel(false);
        millis1 = System.currentTimeMillis() - millis1;
        Logger.getLogger(getClass().getName()).log(Level.FINE, "getAllTemplates() gesamt: {0}ms", millis1);
        List<SolutionStep> result = steps;
        steps = oldSteps;
        return result;
    }

    private void getTemplateSet(boolean initSteps) {
        if (initSteps) {
            steps = new ArrayList<SolutionStep>();
        }
        SudokuSet setSet = new SudokuSet();
        for (int i = 1; i <= 9; i++) {
            setSet.set(finder.getSetValueTemplates(true)[i]);
            setSet.andNot(finder.getPositions()[i]);
            if (!setSet.isEmpty()) {
                globalStep.reset();
                globalStep.setType(SolutionType.TEMPLATE_SET);
                globalStep.addValue(i);
                for (int j = 0; j < setSet.size(); j++) {
                    globalStep.addIndex(setSet.get(j));
                }
                steps.add((SolutionStep) globalStep.clone());
            }
        }
    }

    private void getTemplateDel(boolean initSteps) {
        if (initSteps) {
            steps = new ArrayList<SolutionStep>();
        }
        SudokuSet setSet = new SudokuSet();
        for (int i = 1; i <= 9; i++) {
            setSet.set(finder.getDelCandTemplates(true)[i]);
            setSet.and(finder.getCandidates()[i]);
            if (!setSet.isEmpty()) {
                globalStep.reset();
                globalStep.setType(SolutionType.TEMPLATE_DEL);
                globalStep.addValue(i);
                for (int j = 0; j < setSet.size(); j++) {
                    globalStep.addCandidateToDelete(setSet.get(j), i);
                }
                steps.add((SolutionStep) globalStep.clone());
            }
        }
    }

    public static void main(String[] args) {
        Sudoku2 sudoku = new Sudoku2();
        sudoku.setSudoku(":0000:x:7.2.34.8.........2.8..51.74.......51..63.27..29.......14.76..2.8.........2.51.8.7:::");
        TemplateSolver ts = new TemplateSolver(null);
        long millis = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            List<SolutionStep> steps = ts.getAllTemplates();
        }
        millis = System.currentTimeMillis() - millis;
        System.out.println("Zeit: " + (millis / 100) + "ms");
    }
}
