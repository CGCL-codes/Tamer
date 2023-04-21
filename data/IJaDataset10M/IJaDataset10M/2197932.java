package de.dhbwmannheim.tim.ap;

import java.util.ResourceBundle;
import de.dhbwmannheim.tim.entity.Commands;
import de.dhbwmannheim.tim.entity.Emoticons;
import de.dhbwmannheim.tim.entity.Objects;
import de.dhbwmannheim.tim.entity.ObjectsCommandsBox;
import de.dhbwmannheim.tim.kb.KnowledgeBase;
import de.dhbwmannheim.tim.learn.Learning;
import de.dhbwmannheim.tim.meta.PropertiesFactory;
import de.dhbwmannheim.tim.scp.forms.DebugWindow;
import de.dhbwmannheim.tim.tum.TextUnderstanding;

public class Controller {

    private static ResourceBundle myProperties = PropertiesFactory.getPropertiesByName("EPCodes");

    public String speechInput(String textInput) throws Exception {
        DebugWindow.newLogMessage("Call the TUM module to analyze the string", "Controller", "");
        TextUnderstanding tum = new TextUnderstanding();
        ObjectsCommandsBox ocb = new ObjectsCommandsBox();
        ocb = tum.AnalyzeString(textInput);
        Commands learnedCommand = new Commands();
        boolean invalidCommand = false;
        if (ocb.getCommands().getName() == null) {
            DebugWindow.newLogMessage("Call the Learning component because an invalid command has been found", "Controller", "");
            Learning learn = new Learning();
            learnedCommand = learn.learnCommand();
            invalidCommand = true;
        }
        Objects learnedObject = new Objects();
        boolean invalidObject = false;
        for (Objects iterateObject : ocb.getObjects()) {
            if (iterateObject.getName() == null || iterateObject.getShapes() == null) {
                invalidObject = true;
            }
        }
        if (invalidObject) {
            DebugWindow.newLogMessage("Call the Learning component because an invalid object has been found", "Controller", "");
            Learning learn = new Learning();
            learnedObject = learn.learnObject();
        }
        boolean executeInput = true;
        if ((invalidCommand && !learnedCommand.getName().equals("")) || (invalidObject && !learnedObject.getName().equals(""))) {
            executeInput = false;
        }
        int ecGroupId = 0;
        if (executeInput) {
            DebugWindow.newLogMessage("Call the Action Planner component", "Controller", "");
            ApManagement ap = new ApManagement();
            ap.analyzeAction(ocb);
        } else {
            DebugWindow.newLogMessage("A command or an object has not been learned correctly", "Controller", "");
            ecGroupId = new Integer(myProperties.getString("fehler"));
        }
        DebugWindow.newLogMessage("Get the emotion from the Knowledge Base", "Controller", "");
        KnowledgeBase kb = new KnowledgeBase();
        Emoticons emotion = kb.getEmoticonByGroupId(ecGroupId);
        return emotion.getPath();
    }
}
