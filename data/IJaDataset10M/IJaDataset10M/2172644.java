package alt.djudge.frontend.server.gui;

import java.util.Map;
import alt.djudge.frontend.server.datatypes.ContestEntry;
import alt.djudge.frontend.server.datatypes.ContestProblemEntry;
import alt.djudge.frontend.server.datatypes.ProblemEntry;
import alt.djudge.frontend.server.models.ContestsModel;
import alt.djudge.frontend.server.models.ProblemsModel;

public class ContestProblemsFormatter extends AbstractFormatter<ContestProblemEntry> {

    Map<Long, ProblemEntry> problemsMap = ProblemsModel.getEntriesMap();

    Map<Long, ContestEntry> contestsMap = ContestsModel.getEntriesMap();

    @Override
    protected void addClassData(ContestProblemEntry entry) {
        ContestProblemEntry p = (ContestProblemEntry) entry;
        addTableData(p.getId());
        addTableData(contestsMap.get(p.getContestId()).getName());
        addTableData(problemsMap.get(p.getProblemId()).getSid() + ": " + problemsMap.get(p.getProblemId()).getName());
        addTableData(p.getSid());
        addTableData(p.getName());
        addTableData(p.getActive());
    }

    @Override
    protected void addClassHeaders() {
        addTableHeader("ID");
        addTableHeader("Contest");
        addTableHeader("Problem");
        addTableHeader("Sid");
        addTableHeader("Name");
        addTableHeader("Active");
    }

    @Override
    protected String getCustomRowClass(ContestProblemEntry entry) {
        return null;
    }
}
