package com.rise.rois.server.model.warehouse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import com.rise.rois.server.model.ILoadLearningSessions;
import com.rise.rois.server.model.LearningSession;
import com.rise.rois.server.model.Session;
import com.rise.rois.server.util.DebugUtil;

public class LearningSessionWarehouse {

    private static HashMap<Integer, LearningSession> learningSessions = new HashMap<Integer, LearningSession>();

    public static void load(ILoadLearningSessions iLoader) {
        learningSessions = new HashMap<Integer, LearningSession>();
        DebugUtil.log(Level.INFO, "Loading Learning Sessions");
        Set<LearningSession> loadedSessions = iLoader.load();
        for (LearningSession learningSession : loadedSessions) {
            learningSessions.put(learningSession.getSessionId(), learningSession);
        }
        DebugUtil.log(Level.INFO, "Completed loading Learning Sessions: " + learningSessions.size());
    }

    public static Session getSession(int session_id) {
        return learningSessions.get(session_id);
    }

    public static void addSession(Session session) {
        if (session instanceof LearningSession) {
            LearningSession learningSession = (LearningSession) session;
            learningSessions.put(learningSession.getSessionId(), learningSession);
        } else {
            throw new RuntimeException("Session is not an Individual Session - " + session);
        }
    }

    public static void removeSession(int session_id) {
        learningSessions.remove(session_id);
    }

    public static int size() {
        return learningSessions.size();
    }

    public static Collection<LearningSession> getSessions() {
        return learningSessions.values();
    }

    public static void clear() {
        learningSessions.clear();
    }
}
