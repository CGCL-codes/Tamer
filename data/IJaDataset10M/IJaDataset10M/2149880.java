package sk.tuke.ess.sim.scheme;

import java.util.ArrayList;
import java.util.List;

final class ErrorReporter {

    private List<String> errMessages = new ArrayList<String>();

    /**
     * Pripojí chybovu správu.
     * @param msg chybová správa.
     */
    public void addError(String msg) {
        errMessages.add(msg);
    }

    /**
     * 
     * @return true ak obsahuje chybové správy, inak false.
     */
    public boolean hasErrors() {
        return !errMessages.isEmpty();
    }

    /**
     * Vytvorí reťazec s popisom všetkých chýb, ktoré sa vyskytli pri parsingu.
     * @return reťazec s popisom všetkých chýb, ktoré sa vyskytli pri parsingu.
     */
    public String report() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("\nChyby [").append(errMessages.size()).append("]:\n");
        for (String err : errMessages) {
            reportBuilder.append(err).append('\n');
        }
        return reportBuilder.toString();
    }
}
