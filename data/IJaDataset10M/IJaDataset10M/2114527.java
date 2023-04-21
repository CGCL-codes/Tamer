package computationServers;

public class CointossManager {

    public String stackEvents[] = new String[100];

    public int globalExpenses = 0;

    public int globalEarned = 0;

    public int globalLoop = 0;

    public int globalWinFlag = 0;

    public void coinSimulate(int trials) {
        int counterHeads = 0;
        int counterTails = 0;
        int loopCounter = 0;
        int expensesOccured = 0;
        int moneyEarned = 0;
        int winFlag = 0;
        terminatingLoop: for (int i = 0; i < trials; i++) {
            if (Math.random() < 0.5) {
                counterTails = 0;
                stackEvents[i] = "Heads";
                counterHeads = counterHeads + 1;
                expensesOccured = expensesOccured + 1;
                loopCounter = loopCounter + 1;
            } else {
                counterHeads = 0;
                stackEvents[i] = "Tails";
                counterTails = counterTails + 1;
                expensesOccured = expensesOccured + 1;
                loopCounter = loopCounter + 1;
            }
            if (counterHeads == 3 || counterTails == 3) {
                winFlag = 1;
                break terminatingLoop;
            }
        }
        if (winFlag == 1) {
            globalWinFlag = 1;
            moneyEarned = 25 - expensesOccured;
        } else {
            moneyEarned = 0;
        }
        globalExpenses = expensesOccured;
        globalEarned = moneyEarned;
        globalLoop = loopCounter;
    }
}
