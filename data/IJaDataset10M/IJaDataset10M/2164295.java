package kr.ac.kaist.swrc.jhannanum.demo;

import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;

/**
 * This is a demo program of HanNanum that helps users to utilize the HanNanum library easily.
 * It uses a predefined work flow for morphological analysis and POS tagging with 9 morpheme tags,
 * which can be used by users who want simpler POS tagging results. <br>
 * <br>
 * It performs POS tagging for a Korean document with the following procedure:<br>
 * 		1. Create two predefined work flows for comparison between POS tagging with 69 tags and with 9 tags.<br>
 * 		2. Activate the work flows in multi-thread mode.<br>
 * 		3. Analyze a document using each work flow.<br>
 * 		4. Print the result on the console.<br>
 * 		5. Close the work flow.<br>
 * 
 * @author Sangwon Park (hudoni@world.kaist.ac.kr), CILab, SWRC, KAIST
 */
public class WorkflowSimplePos09 {

    public static void main(String[] args) {
        Workflow workflow1 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_HMM_POS_TAGGER);
        Workflow workflow2 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_POS_SIMPLE_09);
        try {
            workflow1.activateWorkflow(true);
            workflow2.activateWorkflow(true);
            String sentence = "학교에서조차도 그 사실을 모르고 있었다.";
            workflow1.analyze(sentence);
            workflow2.analyze(sentence);
            System.out.println("# POS tagging result with 69 tags.\n");
            System.out.println(workflow1.getResultOfSentence());
            System.out.println("# POS tagging result with 9 tags.\n");
            System.out.println(workflow2.getResultOfSentence());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        workflow1.close();
        workflow2.close();
    }
}
