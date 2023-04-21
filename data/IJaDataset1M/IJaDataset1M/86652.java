package fitlibrary.traverse.workflow;

import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;
import fitlibrary.typed.TypedObject;

public interface DoCaller {

    boolean isValid();

    TypedObject run(Row row, TestResults testResults) throws Exception;

    String ambiguityErrorMessage();

    String getPartialErrorMessage();

    boolean isAmbiguous();

    boolean isProblem();

    boolean partiallyValid();

    Exception problem();
}
