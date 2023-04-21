package org.photouploader;

/**
 *
 */
public interface IORequestor {

    public void showMessage(String message);

    public void showErrorMessage(String message);

    public void showProgression(String message);

    /**
     *
     * @param title
     * @param question
     * @param answers
     * @return the answer
     */
    public String askquestion(String title, String question, String[] answers);
}
