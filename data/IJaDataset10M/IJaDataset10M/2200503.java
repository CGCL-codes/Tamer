package dwarffortess.web.research;

import dwarffortress.tools.Logger;
import javax.swing.*;

public class LogController implements Logger {

    private JTextArea area;

    private int currentLevel = ALL_ASSUMPTIONS;

    private boolean hasUnusualMessages;

    private boolean hasMessages;

    public LogController(JTextArea area) {
        this.area = area;
        clear();
    }

    public void clear() {
        area.setText(String.format("Research log. Researcher version %s.%n", PrefTools.getResearcherVersion()));
        hasUnusualMessages = false;
        hasMessages = false;
    }

    public void log(int level, String message) {
        if (currentLevel >= level) {
            hasMessages = true;
            hasUnusualMessages |= (level != TEXT);
            area.append(message);
            if (!message.endsWith("\n")) {
                area.append("\n");
            }
        }
    }

    public void setLevel(int level) {
        currentLevel = level;
    }

    public boolean hasUnusualMessages() {
        return hasUnusualMessages;
    }

    public boolean hasMessages() {
        return hasMessages;
    }
}
