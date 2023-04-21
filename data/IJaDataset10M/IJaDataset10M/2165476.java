package yapgen.base.knowledge.plan.experience;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapgen.base.BaseObject;
import yapgen.base.knowledge.character.Character;
import yapgen.base.knowledge.character.CharacterCoordinate;
import yapgen.base.knowledge.plan.decision.PlanSynthesis;
import yapgen.base.engine.Engine;

/**
 *
 * @author riccardo
 */
public class PlanExperienceManager extends BaseObject {

    private PlanExperienceBackend backend;

    public void put(Character character, PlanSynthesis planSynthesis) {
        CharacterCoordinate coord = new CharacterCoordinate(character);
        try {
            doPut(coord, planSynthesis);
        } catch (IOException ex) {
            Logger.getLogger(PlanExperienceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PlanSynthesis get(Character character) {
        CharacterCoordinate coord = new CharacterCoordinate(character);
        PlanSynthesis ret = null;
        try {
            ret = doGet(coord);
        } catch (IOException ex) {
            Logger.getLogger(PlanExperienceManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PlanExperienceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private void doPut(CharacterCoordinate coord, PlanSynthesis planSynthesis) throws IOException {
        backend.doPut(coord, planSynthesis);
    }

    private PlanSynthesis doGet(CharacterCoordinate coord) throws IOException, ClassNotFoundException {
        return backend.doGet(coord);
    }

    private static PlanExperienceManager instance;

    private PlanExperienceManager() {
        String backendConfig = Engine.getInstance().getConfig().ExperienceBackend;
        BackendEnum backendEnum = BackendEnum.valueOf(backendConfig.toUpperCase());
        switch(backendEnum) {
            case FILE:
                {
                    try {
                        backend = new FileMapBackend();
                    } catch (IOException ex) {
                        Logger.getLogger(PlanExperienceManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case INMEMORY:
                {
                    backend = new InMemoryMapBackend();
                }
                break;
        }
    }

    public static PlanExperienceManager getInstance() {
        return instance;
    }

    private static enum BackendEnum {

        FILE, INMEMORY
    }

    public static void init() {
        instance = new PlanExperienceManager();
    }
}
