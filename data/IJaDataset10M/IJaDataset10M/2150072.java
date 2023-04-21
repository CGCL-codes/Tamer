package serviceImplementations.evaluation;

import java.util.Random;

/**
 * Initializes the evaluation process.
 * 
 * @author Michael Sch�fer
 *
 */
public class StartEvaluation {

    private static int NUMBER_TRANSACTIONS = 70;

    private static boolean USE_ABSTRACTSERVICE = false;

    private static int COMPENSATION_FAILURE_PROBABILITY = 30;

    private static long TIME_SLEEP = 2000;

    private static int NUMBER_PARTICIPANTS_MAX = 20;

    private static int NUMBER_PARTICIPANTS_MIN = 3;

    /**
	 * The main method that starts the evaluation process.
	 * @param args
	 */
    public static void main(String[] args) {
        StatisticsManager.getInstance().startEvaluation(NUMBER_TRANSACTIONS);
        ClientStarter starter;
        int numberParticipants;
        Random randomGenerator = new Random();
        for (int i = 0; i < NUMBER_TRANSACTIONS; i++) {
            numberParticipants = randomGenerator.nextInt(NUMBER_PARTICIPANTS_MAX + 1);
            if (numberParticipants < NUMBER_PARTICIPANTS_MIN) {
                numberParticipants = NUMBER_PARTICIPANTS_MIN;
            }
            starter = new ClientStarter(i + 1, NUMBER_TRANSACTIONS, USE_ABSTRACTSERVICE, numberParticipants, COMPENSATION_FAILURE_PROBABILITY);
            starter.start();
            try {
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
