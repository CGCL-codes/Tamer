package uk.ac.lkl.migen.system.util;

import java.util.ArrayList;
import java.util.List;
import uk.ac.lkl.common.util.config.ConfigurationException;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.migen.system.MiGenContext;
import uk.ac.lkl.migen.system.ai.feedback.intervention.FeedbackInteractivityLevel;
import uk.ac.lkl.migen.system.ai.feedback.intervention.FeedbackInterruptionLevel;
import uk.ac.lkl.migen.system.ai.feedback.intervention.FeedbackMessageType;
import uk.ac.lkl.migen.system.ai.feedback.intervention.FeedbackModality;
import uk.ac.lkl.migen.system.ai.feedback.intervention.Intervention;
import uk.ac.lkl.migen.system.ai.feedback.intervention.InterventionGeneratedOccurrence;
import uk.ac.lkl.migen.system.ai.feedback.intervention.InterventionShownOccurrence;
import uk.ac.lkl.migen.system.ai.feedback.strategy.FeedbackStrategyType;
import uk.ac.lkl.migen.system.ai.um.IndicatorClass;
import uk.ac.lkl.migen.system.ai.um.SimpleEventIndicator;
import uk.ac.lkl.migen.system.ai.um.occurrence.SimpleIndicatorOccurrence;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.ui.ecollaborator.ActivityDocument;
import uk.ac.lkl.migen.system.expresser.ui.ecollaborator.ActivityDocumentXMLString;
import uk.ac.lkl.migen.system.server.MiGenServerCommunicator;
import uk.ac.lkl.migen.system.server.User;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.task.TaskIdentifier;

/**
 * This class posts in the DB the indicators related
 * to the feedback layer: feedback intervention seen
 * by students.
 * 
 * @author sergut
 *
 */
public class FeedbackEventOccurrenceDbPoster {

    /**
     * The user set that using this model 
     */
    private UserSet userSet;

    private MiGenServerCommunicator serverCommunicator;

    public FeedbackEventOccurrenceDbPoster(String studentName) throws ConfigurationException, RestletException {
        serverCommunicator = createServerCommunicator();
        User user = getUser(serverCommunicator, studentName);
        if (user == null) throw new IllegalArgumentException("User '" + studentName + "' does not exist in DB.");
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        this.userSet = getUserSet(serverCommunicator, userList);
        if (userSet == null) throw new IllegalArgumentException("User '" + studentName + "' does not exist in DB.");
        System.out.println("STATUS: " + "Server communicator and user set correctly.");
    }

    public UserSet getUserSet(MiGenServerCommunicator serverCommunicator, List<User> users) {
        System.out.println("STATUS: " + "Getting user set...");
        if (serverCommunicator == null || !serverCommunicator.isEnabled()) return null;
        int userCount = users.size();
        List<UserSet> userSets = serverCommunicator.getUserSetList();
        for (UserSet userSet : userSets) {
            if (userSet.size() == userCount) {
                List<User> usersFromServer = userSet.getUsers();
                boolean mismatch = false;
                for (User user : usersFromServer) {
                    if (!users.contains(user)) {
                        mismatch = true;
                        break;
                    }
                }
                if (!mismatch) {
                    return userSet;
                }
            }
        }
        return null;
    }

    private User getUser(MiGenServerCommunicator serverCommunicator, String username) {
        if (serverCommunicator == null || !serverCommunicator.isEnabled()) return null;
        System.out.println("STATUS: " + "Checking user '" + username + "' in DB...");
        List<User> users = serverCommunicator.getUserList();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("STATUS: " + "Retrieved user '" + username + "' from DB...");
                return user;
            } else {
                System.out.println("STATUS: " + "Found user '" + user.getUsername() + "' in DB. Looking up next...");
            }
        }
        return null;
    }

    private MiGenServerCommunicator createServerCommunicator() throws ConfigurationException, RestletException {
        System.out.println("STATUS: " + "Creating MiGen Server Communicator...");
        if (MiGenContext.isServerCommunicatorEnabled()) return MiGenContext.getServerCommunicator();
        String serverName = MiGenConfiguration.getServerName();
        if (serverName == null) throw new ConfigurationException("No server defined.");
        int serverPort = MiGenConfiguration.getServerPort();
        System.out.println("STATUS: " + "MiGen Server Communicator created.");
        return new MiGenServerCommunicator(serverName, serverPort);
    }

    private static ExpresserModel fetchModelHandleFromServer(MiGenServerCommunicator migenServerCommunicator, long timeStamp, TimeStampMatch timeStampMatch, TaskIdentifier taskIdentifier, String typeOfSave, UserSet userSet) {
        List<ExpresserModel> expresserModelList = migenServerCommunicator.getExpresserModelList(userSet);
        if (expresserModelList == null) {
            return null;
        }
        long bestTimeDifference = Long.MAX_VALUE;
        ExpresserModel bestModel = null;
        long mostRecentTimeStamp = 0;
        ExpresserModel newestModel = null;
        String taskIdString = null;
        if (taskIdentifier != null) {
            taskIdString = Integer.toString(taskIdentifier.getId());
        }
        for (ExpresserModel model : expresserModelList) {
            String name = model.getName();
            if (name == null) {
                continue;
            }
            String[] nameParts = name.split("_");
            if (nameParts.length < 2) {
                continue;
            }
            if (typeOfSave != null && (nameParts.length < 3 || !typeOfSave.equals(nameParts[2]))) {
                continue;
            }
            String modelTaskIdentifierString = nameParts[0];
            if (taskIdentifier != null && modelTaskIdentifierString != null && !modelTaskIdentifierString.equals(taskIdString)) {
                continue;
            }
            long modelTimeStamp;
            try {
                modelTimeStamp = Long.parseLong(nameParts[1], 16);
            } catch (NumberFormatException e) {
                continue;
            }
            if (mostRecentTimeStamp < modelTimeStamp) {
                mostRecentTimeStamp = modelTimeStamp;
                newestModel = model;
            }
            long modelTimeDifference = 0;
            switch(timeStampMatch) {
                case CLOSEST_TIMESTAMP:
                    modelTimeDifference = Math.abs(modelTimeStamp - timeStamp);
                    break;
                case CLOSEST_EARLIER_TIMESTAMP:
                    modelTimeDifference = timeStamp - modelTimeStamp;
                    break;
                case CLOSEST_LATER_TIMESTAMP:
                    modelTimeDifference = modelTimeStamp - timeStamp;
                    break;
            }
            if (modelTimeDifference >= 0 && modelTimeDifference < bestTimeDifference) {
                bestTimeDifference = modelTimeDifference;
                bestModel = model;
            }
        }
        if (bestModel == null) {
            bestModel = newestModel;
        }
        return bestModel;
    }

    private void post(String eventName, Long timestamp) {
        System.out.println("STATUS: " + "Fetching the model on the timestamp...");
        ExpresserModel expresserModel = fetchModelHandleFromServer(this.serverCommunicator, timestamp, TimeStampMatch.CLOSEST_EARLIER_TIMESTAMP, TaskIdentifier.TRAINTRACK, null, userSet);
        if (expresserModel == null) throw new IllegalArgumentException("No model to post indicator to (have you typed the right timestamp?).");
        System.out.println("STATUS: " + "model fetched from the server for timestamp " + timestamp);
        if ("callTeacher".equals(eventName)) {
            postCallTeacher(expresserModel, timestamp);
        } else if ("globalAllocationCorrect".equals(eventName)) {
            postSimpleEventIndicator(IndicatorClass.CORRECT_GLOBAL_RULE_CREATED, expresserModel, timestamp);
        } else if ("globalAllocationIncorrect".equals(eventName)) {
            postSimpleEventIndicator(IndicatorClass.INCORRECT_GLOBAL_RULE_CREATED, expresserModel, timestamp);
        } else {
            System.out.println("Event name '" + eventName + "' is not valid.\nExiting...");
            System.exit(-1);
        }
    }

    private void postSimpleEventIndicator(SimpleEventIndicator indicator, ExpresserModel expresserModel, Long timestamp) {
        SimpleIndicatorOccurrence occurrence = new SimpleIndicatorOccurrence(indicator);
        System.out.println("STATUS: " + "Sending request to post 'correct global expression created' for user ID '" + userSet.getNamesAsString() + "' on model handle '" + expresserModel.getName() + "'...");
        serverCommunicator.addSimpleIndicatorOccurrence(userSet, expresserModel, occurrence);
        System.out.println("STATUS: " + "Finished posting requests.");
    }

    @SuppressWarnings("deprecation")
    private void postCallTeacher(ExpresserModel expresserModel, Long timestamp) {
        Intervention intervention = new Intervention(FeedbackStrategyType.CALL_TEACHER, FeedbackInterruptionLevel.EXPLICITLY_REQUESTED, FeedbackInteractivityLevel.PASSIVE, FeedbackModality.TEXT, FeedbackMessageType.COMMENT);
        InterventionGeneratedOccurrence generatedOccurrence = new InterventionGeneratedOccurrence(intervention, timestamp);
        InterventionShownOccurrence shownOccurrence = new InterventionShownOccurrence(intervention, (timestamp + 20));
        System.out.println("STATUS: " + "Sending request to post intervention generated for user ID '" + userSet.getNamesAsString() + "' on model handle '" + expresserModel.getName() + "'...");
        serverCommunicator.addFeedbackInterventionGenerated(userSet, expresserModel, generatedOccurrence);
        System.out.println("STATUS: " + "Sending request to post intervention shown for user ID '" + userSet.getNamesAsString() + "' on model handle '" + expresserModel.getName() + "'...");
        serverCommunicator.addFeedbackInterventionShown(userSet, expresserModel, shownOccurrence);
        System.out.println("STATUS: " + "Finished posting requests.");
    }

    public static ExpresserModel fetchModelFromServer(MiGenServerCommunicator serverCommunicator, long timeStamp, TimeStampMatch timeStampMatch, TaskIdentifier taskIdentifier, UserSet userSet) {
        ActivityDocument activityDocument = fetchActivityDocumentFromServer(serverCommunicator, timeStamp, timeStampMatch, taskIdentifier, null, userSet);
        if (activityDocument == null) {
            return null;
        }
        List<ExpresserModel> models = activityDocument.getModels();
        if (models != null && models.size() >= 1) {
            return models.get(0);
        } else {
            MiGenUtilities.printError("STATUS: " + "Expected the activity document to have an associated model.");
            return null;
        }
    }

    public static ActivityDocument fetchActivityDocumentFromServer(MiGenServerCommunicator migenServerCommunicator, long timeStamp, TimeStampMatch timeStampMatch, TaskIdentifier taskIdentifier, String typeOfSave, UserSet userSet) {
        List<ExpresserModel> expresserModelList = migenServerCommunicator.getExpresserModelList(userSet);
        if (expresserModelList == null) {
            return null;
        }
        long bestTimeDifference = Long.MAX_VALUE;
        ExpresserModel bestModel = null;
        long mostRecentTimeStamp = 0;
        ExpresserModel newestModel = null;
        String taskIdString = null;
        if (taskIdentifier != null) {
            taskIdString = Integer.toString(taskIdentifier.getId());
        }
        for (ExpresserModel model : expresserModelList) {
            String name = model.getName();
            if (name == null) {
                continue;
            }
            String[] nameParts = name.split("_");
            if (nameParts.length < 2) {
                continue;
            }
            if (typeOfSave != null && (nameParts.length < 3 || !typeOfSave.equals(nameParts[2]))) {
                continue;
            }
            String modelTaskIdentifierString = nameParts[0];
            if (taskIdentifier != null && modelTaskIdentifierString != null && !modelTaskIdentifierString.equals(taskIdString)) {
                continue;
            }
            long modelTimeStamp;
            try {
                modelTimeStamp = Long.parseLong(nameParts[1], 16);
            } catch (NumberFormatException e) {
                continue;
            }
            if (mostRecentTimeStamp < modelTimeStamp) {
                mostRecentTimeStamp = modelTimeStamp;
                newestModel = model;
            }
            long modelTimeDifference = 0;
            switch(timeStampMatch) {
                case CLOSEST_TIMESTAMP:
                    modelTimeDifference = Math.abs(modelTimeStamp - timeStamp);
                    break;
                case CLOSEST_EARLIER_TIMESTAMP:
                    modelTimeDifference = timeStamp - modelTimeStamp;
                    break;
                case CLOSEST_LATER_TIMESTAMP:
                    modelTimeDifference = modelTimeStamp - timeStamp;
                    break;
            }
            if (modelTimeDifference >= 0 && modelTimeDifference < bestTimeDifference) {
                bestTimeDifference = modelTimeDifference;
                bestModel = model;
            }
        }
        if (bestModel == null) {
            bestModel = newestModel;
        }
        if (bestModel != null) {
            ActivityDocumentXMLString activityDocumentXMLString = migenServerCommunicator.getActivityDocumentXMLString(userSet, bestModel);
            if (activityDocumentXMLString != null) {
                return activityDocumentXMLString.getActivityDocument();
            } else {
                MiGenUtilities.printError("STATUS: " + "Model named " + bestModel.getName() + " does not have an associated activity document.");
                return null;
            }
        } else {
            return null;
        }
    }

    public static void usage() {
        System.out.println("\nUSAGE: poster <userName> <timestamp> <eventName>\n");
        System.out.println("Example: java uk.ac.lkl.migen.system.util.FeedbackEventOccurrenceDbPoster HH107977299 1291806695349 callTeacher");
        System.out.println("\nAvailable event names are:\n");
        System.out.println(" - callTeacher: post an intervention generated and an intervention shown for feedback strategy CALL_TEACHER.");
        System.out.println(" - globalAllocationCorrect: post a simple event indicator for CORRECT_GLOBAL_ALLOCATION_CREATED");
        System.out.println(" - globalAllocationIncorrect: post a simple event indicator for INCORRECT_GLOBAL_ALLOCATION_CREATED");
        System.exit(-1);
    }

    public static void main(String[] args) throws ConfigurationException, RestletException {
        if (args.length != 3) usage();
        try {
            System.out.println("Current time: " + System.currentTimeMillis());
            String studentName = args[0];
            Long timestamp = Long.parseLong(args[1]);
            String eventName = args[2];
            FeedbackEventOccurrenceDbPoster poster = new FeedbackEventOccurrenceDbPoster(studentName);
            poster.post(eventName, timestamp);
            System.out.println("STATUS: All requests posted. Waiting for server to accept them before closing. ");
            while (poster.isWaiting()) {
                try {
                    Thread.sleep(1000);
                    if (poster.isWaiting()) System.out.println("STATUS: Still waiting...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            poster.shutdown();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: bad timestamp.");
            usage();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
            usage();
        }
    }

    private void shutdown() {
        if (serverCommunicator.isQueueEmpty()) {
            System.out.println("Work done. Thank you for your cooperation. Good night, and good luck.");
            System.exit(0);
        } else throw new IllegalStateException("Should never shutdown if queue is not empty!!");
    }

    private boolean isWaiting() {
        if (serverCommunicator.isQueueEmpty()) return false;
        return true;
    }
}
