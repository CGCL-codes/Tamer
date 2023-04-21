package br.ufal.ic.ptl.interpreter.instructions;

import java.util.List;

public class ChatInstructionWrapper extends Instruction {

    private String subject, group1, group2;

    private List<String> fileList;

    private int duration;

    public ChatInstructionWrapper() {
    }

    public ChatInstructionWrapper(String subject, String group1, String group2, List<String> fileList, int duration) {
        super();
        this.subject = subject;
        this.group1 = group1;
        this.group2 = group2;
        this.fileList = fileList;
        this.duration = duration;
    }

    /**
	 * @return the duration
	 */
    public int getDuration() {
        return duration;
    }

    /**
	 * @param duration
	 *            the duration to set
	 */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
	 * @return the fileList
	 */
    public List<String> getFileList() {
        return fileList;
    }

    /**
	 * @param fileList
	 *            the fileList to set
	 */
    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    /**
	 * @return the group1
	 */
    public String getGroup1() {
        return group1;
    }

    /**
	 * @param group1
	 *            the group1 to set
	 */
    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    /**
	 * @return the group2
	 */
    public String getGroup2() {
        return group2;
    }

    /**
	 * @param group2
	 *            the group2 to set
	 */
    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    /**
	 * @return the subject
	 */
    public String getSubject() {
        return subject;
    }

    /**
	 * @param subject
	 *            the subject to set
	 */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public void execute() {
    }
}
