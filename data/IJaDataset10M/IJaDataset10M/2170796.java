package IOGenerating;

import IOGenerating.Exceptions.InputGeneratingException;
import IOGenerating.Exceptions.OutputGeneratingException;
import Program.Program;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 *
 * @author Анна
 */
public class OutputFromFileGenerator extends OutputGenerator {

    private Program program;

    /**
     *
     * @param program
     */
    public OutputFromFileGenerator(Program program) {
        this.program = program;
    }

    /**
     *
     * @param testNumber
     * @return
     */
    public Reader getReader(int testNumber) throws OutputGeneratingException {
        Reader reader = null;
        try {
            reader = new FileReader(program.problem.getPathToTests() + "/" + program.problem.out[testNumber]);
        } catch (FileNotFoundException e) {
            throw new OutputGeneratingException("Test output not found: " + e);
        }
        return reader;
    }
}
