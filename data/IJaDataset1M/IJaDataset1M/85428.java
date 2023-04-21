package ch.gmtech.lab.gui;

import ch.gmtech.lab.domain.Command;

public class UserChoice {

    public UserChoice(Display display, Message userQuestion) {
        _promptText = userQuestion;
        _display = display;
    }

    public void askFor(Command aChoice) {
        printQuestionOn(display());
        display().call(aChoice);
    }

    private Display display() {
        return _display;
    }

    private void printQuestionOn(Display aConsole) {
        _promptText.printOn(aConsole);
    }

    private Message _promptText;

    private final Display _display;
}
