package org.wizard4j.flowchart.tree;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.wizard4j.flowchart.ELoopType;
import org.wizard4j.validation.ValidationException;

public class Loop extends NonLeaf {

    protected String help = null;

    protected ELoopType loopType;

    protected int minNbrOfIterations = 0;

    protected int maxNbrOfIterations = 0;

    protected List<String> branchNameList = new ArrayList<String>();

    protected String anotherIterationQuestion = null;

    protected String condition = null;

    public Loop(Element loopElement, TreeElement parent, int defaultMinNbrOfIterations, int defaultMaxNbrOfIterations) throws ValidationException {
        super(null, parent);
        name = loopElement.getAttribute("name");
        if (name.trim().length() == 0) {
            throw new ValidationException("The attribute 'name' can not be empty.");
        }
        label = loopElement.getAttribute("label");
        if (label.trim().length() == 0) {
            throw new ValidationException("The attribute 'label' can not be empty (element name = '" + name + "').");
        }
        String mode = loopElement.getAttribute("mode");
        loopType = ELoopType.getTypeByLabel(mode);
        if (loopType == null) {
            throw new ValidationException("The attribute 'mode' has an invalid value '" + mode + "').");
        }
        help = loopElement.getAttribute("help");
        if (help.trim().length() == 0) {
            help = null;
        }
        String minNbrOfIterationsString = loopElement.getAttribute("minNbrOfIterations");
        String maxNbrOfIterationsString = loopElement.getAttribute("maxNbrOfIterations");
        if (loopType == ELoopType.LIST) {
            String list = loopElement.getAttribute("list");
            StringTokenizer stringTokenizer = new StringTokenizer(list, ",");
            while (stringTokenizer.hasMoreTokens()) {
                String branchName = stringTokenizer.nextToken().trim();
                if (branchName.contains(":") || Character.isDigit(branchName.charAt(0))) {
                    throw new ValidationException("The branch name " + branchName + " is not an xs:NCName.");
                }
                branchNameList.add(branchName);
            }
            if ("".equals(minNbrOfIterationsString) && "".equals(maxNbrOfIterationsString)) {
                minNbrOfIterations = branchNameList.size();
                maxNbrOfIterations = minNbrOfIterations;
            } else {
                if ("".equals(minNbrOfIterationsString)) {
                    minNbrOfIterations = 0;
                } else {
                    minNbrOfIterations = Integer.parseInt(minNbrOfIterationsString);
                }
                if ("".equals(maxNbrOfIterationsString)) {
                    maxNbrOfIterations = branchNameList.size();
                } else {
                    maxNbrOfIterations = Integer.parseInt(maxNbrOfIterationsString);
                }
                if (minNbrOfIterations > maxNbrOfIterations) {
                    throw new ValidationException("Value of maxNbrOfIterations (" + maxNbrOfIterations + ") is smaller than value of minNbrOfIterations (" + minNbrOfIterations + ") (element name = '" + name + "').");
                }
            }
        } else if (loopType == ELoopType.NBROFITERATIONS) {
            if ("".equals(minNbrOfIterationsString)) {
                minNbrOfIterations = defaultMinNbrOfIterations;
            } else {
                minNbrOfIterations = Integer.parseInt(minNbrOfIterationsString);
            }
            if ("".equals(maxNbrOfIterationsString)) {
                maxNbrOfIterations = defaultMaxNbrOfIterations;
            } else {
                maxNbrOfIterations = Integer.parseInt(maxNbrOfIterationsString);
            }
            if (minNbrOfIterations > maxNbrOfIterations) {
                throw new ValidationException("Value of maxNbrOfIterations (" + maxNbrOfIterations + ") is smaller than value of minNbrOfIterations (" + minNbrOfIterations + ") (element name = '" + name + "').");
            }
        } else if (loopType == ELoopType.USERINTERRUPTED) {
            if ("".equals(minNbrOfIterationsString)) {
                minNbrOfIterations = defaultMinNbrOfIterations;
            } else {
                minNbrOfIterations = Integer.parseInt(minNbrOfIterationsString);
            }
            if ("".equals(maxNbrOfIterationsString)) {
                maxNbrOfIterations = defaultMaxNbrOfIterations;
            } else {
                maxNbrOfIterations = Integer.parseInt(maxNbrOfIterationsString);
            }
            if (minNbrOfIterations > maxNbrOfIterations) {
                throw new ValidationException("Value of maxNbrOfIterations (" + maxNbrOfIterations + ") is smaller than value of minNbrOfIterations (" + minNbrOfIterations + ") (element name = '" + name + "').");
            }
            anotherIterationQuestion = loopElement.getAttribute("anotherIterationQuestion");
            if (anotherIterationQuestion.trim().length() == 0) {
                throw new ValidationException("The attribute 'anotherIterationQuestion' can not be empty.");
            }
        } else if (loopType == ELoopType.DOWHILE) {
            minNbrOfIterations = 1;
            if ("".equals(maxNbrOfIterationsString)) {
                maxNbrOfIterations = defaultMaxNbrOfIterations;
            } else {
                maxNbrOfIterations = Integer.parseInt(maxNbrOfIterationsString);
            }
            if (minNbrOfIterations > maxNbrOfIterations) {
                throw new ValidationException("Value of maxNbrOfIterations (" + maxNbrOfIterations + ") is smaller than value of minNbrOfIterations (" + minNbrOfIterations + ") (element name = '" + name + "').");
            }
            condition = loopElement.getAttribute("condition");
            if (condition.trim().length() == 0) {
                throw new ValidationException("The attribute 'condition' can not be empty.");
            }
        }
    }

    public String getHelp() {
        return help;
    }

    public ELoopType getLoopType() {
        return loopType;
    }

    public int getMinNbrOfIterations() {
        return minNbrOfIterations;
    }

    public int getMaxNbrOfIterations() {
        return maxNbrOfIterations;
    }

    public List<String> getBranchNameList() {
        return branchNameList;
    }

    public String getAnotherIterationQuestion() {
        return anotherIterationQuestion;
    }

    public String getCondition() {
        return condition;
    }
}
