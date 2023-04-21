package cn.edu.pku.dr.requirement.elicitation.action;

import cn.edu.pku.dr.requirement.elicitation.data.Problem;
import easyJ.business.proxy.CompositeDataProxy;
import easyJ.common.EasyJException;
import java.util.ArrayList;
import cn.edu.pku.dr.requirement.elicitation.data.Solution;

public class ProblemIntercepter extends easyJ.http.servlet.SingleDataAction {

    boolean delete;

    private static int i = 0;

    public boolean[] judgeProblem(Problem problem) throws EasyJException {
        boolean[] judgeresult = new boolean[2];
        CompositeDataProxy cdp = CompositeDataProxy.getInstance();
        Problem result = new Problem();
        result = (Problem) cdp.get(problem);
        ArrayList solutions = result.getSolutions();
        judgeresult[0] = true;
        Solution solution = new Solution();
        if (solutions.size() > 0) {
            int i = 0;
            while (judgeresult[0] == true && i < solutions.size()) {
                solution = (Solution) solutions.get(i);
                if (solution.getIsVoting().equals("Y")) {
                    judgeresult[0] = false;
                }
                i++;
            }
        }
        judgeresult[1] = true;
        return judgeresult;
    }

    public boolean testSolution() {
        boolean test;
        test = true;
        return test;
    }
}
